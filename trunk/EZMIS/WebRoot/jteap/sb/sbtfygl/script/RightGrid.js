Ext.DatePicker.prototype.setValue = function(value) {
	if (typeof value == 'number') {
		value = new Date(value);
	}
	var old = this.value;
	this.value = value.clearTime(true);
	if (this.el) {
		this.update(this.value);
	}
};

var LogDateField = Ext.extend(Ext.form.DateField, {
			setValue : function(date) {
				LogDateField.superclass.setValue.call(this, date);
			},

			parseDate : function(value) {
				var dt = null;

				if (typeof value == 'object' && value.time) {
					dt = new Date(value.time);
				} else if (typeof value == 'number') {
					dt = new Date(value);
				} else {
					dt = LogDateField.superclass.parseDate.call(this, value);
				}
				return dt;
			},
			getValue : function() {
				var result = 0;
				var dt = LogDateField.superclass.getValue.call(this);
				if (dt != null && dt != "") {
					result = dt.getTime();
				}
				return result;

			}

		});

/**
 * 字段列表
 */
RightGrid = function() {
	var defaultDs = this.getDefaultDS(link4 + "?limit=20");
	defaultDs.load();
	var grid = this;
	this.pageToolbar = new Ext.PagingToolbar({
				pageSize : 20,
				store : defaultDs,
				displayInfo : true,
				displayMsg : '共{2}条数据，目前为 {0} - {1} 条',
				emptyMsg : "没有符合条件的数据",
				items : ['-', {
							text : '导出Excel',
							handler : function() {
								exportExcel(grid, true);
							}
						},'-','<font color="red" style="font-weight:bold">双击编辑数据</font>']
			});
	RightGrid.superclass.constructor.call(this, {
				ds : defaultDs,
				cm : this.getColumnModel(),
				sm : this.sm,
				margins : '2px 2px 2px 2px',
				width : 600,
				height : 300,
				loadMask : true,
				frame : true,
				region : 'center',
				tbar : this.pageToolbar,
				stripeRows : true
			});

	// this.getStore().reload();
	this.on('beforerender', function(param) {
				this.sm.singleSelect = true;
			});
	// 当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange", function(oCheckboxSModel) {

				var btnDel = mainToolbar.items.get('btnSave');
				var btnModify = mainToolbar.items.get('btnDqkgl');

				if (oCheckboxSModel.getSelections().length == 1) {
					if (btnModify)
						btnModify.setDisabled(false);
				} else {
					if (btnModify)
						btnModify.setDisabled(true);
				}

				if (oCheckboxSModel.getSelections().length <= 0) {
					if (btnDel)
						btnDel.setDisabled(true);
				} else {
					if (btnDel)
						btnDel.setDisabled(false);
				}
			});

	this.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
				var select = rightGrid.getSelectionModel().getSelections()[0];
			});
}

Ext.extend(RightGrid, Ext.grid.EditorGridPanel, {

			sm : new Ext.grid.CheckboxSelectionModel(),

			/**
			 * 取得默认数据源 返回数据格式
			 * {success:true,totalCount:20,list:[{id:'123',field1:'111',...,field5'222'},{...},...]}
			 */
			getDefaultDS : function(url) {
				var ds = new Ext.data.Store({
							proxy : new Ext.data.ScriptTagProxy({
										url : url
									}),
							reader : new Ext.data.JsonReader({
										root : 'list',
										totalProperty : 'totalCount',
										id : 'id'
									}, ["id", "jz", "jzzt", "tfysj", "syr",
											"syyj", "sysj", "remark"]),
							remoteSort : true,
							// 如果为true,每次store在加载时或者一条记录被删除时, 清除所有改动过的记录信息
							pruneModifiedRecords : true
						});
				return ds;
			},
			/**
			 * JctzAction 列模型
			 */
			getColumnModel : function() {

				var grid = this;
				var cm = new Ext.grid.ColumnModel([this.sm,
						{
							id : 'jz',
							header : "机组",
							width : 100,
							sortable : true,
							dataIndex : 'jz'
						}, {
							id : 'jzzt',
							header : "机组状态",
							width : 100,
							sortable : true,
							dataIndex : 'jzzt'
						}, {
							id : 'tfysj',
							header : "停复役时间",
							width : 120,
							sortable : true,
							dataIndex : 'tfysj',
							renderer : function(value, metadata, record,
									rowIndex, colIndex, store) {
								var tfysj = (value == null)
										? null
										: formatDate(new Date(value["time"]),
												"yyyy-MM-dd");
								return tfysj;
							}
						}, {
							id : 'syr',
							header : "审阅人",
							width : 80,
							sortable : true,
							dataIndex : 'syr',
							editor : new Ext.form.TextField({
										allowBlank : true,
										maxLength : 10
									})
						}, {
							id : 'syyj',
							header : "审阅意见",
							width : 150,
							sortable : true,
							dataIndex : 'syyj',
							editor : new Ext.form.TextArea({
								listeners :{
									render:function(ta){
										ta.el.dom.rows = 5;
									}
								}
							})
						}, {
							id : 'sysj',
							header : "审阅时间",
							width : 120,
							sortable : true,
							dataIndex : 'sysj',
							editor : new LogDateField({
										id : 'logdt',
										format : 'Y-m-d'
									}),
							renderer : function(value, metadata, record,
									rowIndex, colIndex, store) {
								var time = 0;
								if (typeof value == "number") {
									time = value;
								} else if (typeof value == "object") {
									time = value.time;
								}
								dt = formatDate(new Date(time), "yyyy-MM-dd");
								return dt;
							}
						}, {
							id : 'remark',
							header : "备注",
							width : 150,
							sortable : true,
							dataIndex : 'remark',
							editor : new Ext.form.TextArea({
								listeners :{
									render:function(ta){
										ta.el.dom.rows = 5;
									}
								}
							})
						}]);
				return cm;
			},
			/**
			 * 切换数据源->LogAction!showList
			 */
			changeToListDS : function(url) {
				var ds = this.getDefaultDS(url);
				var cm = this.getColumnModel();
				this.pageToolbar.bind(ds);
				this.reconfigure(ds, cm);
			},
			/**
			 * 删除
			 */
			deleteSelect : function(select) {
				var selections = this.getSelections();// 获取被选中的行
				var rightGrid = this;
				var ids = "";
				Ext.each(selections, function(selectedobj) {
							ids += selectedobj.id + ",";// 取得他们的id并组装
						});
				if (window.confirm("确认删除选中的条目吗？")) {
					Ext.Ajax.request({
								url : link5,
								success : function(ajax) {
									var responseText = ajax.responseText;
									var responseObject = Ext.util.JSON
											.decode(responseText);
									if (responseObject.success) {
										alert("删除成功");
										rightGrid.getStore().reload();
									} else {
										alert(responseObject.msg);
									}
								},
								failure : function() {
									alert("提交失败");
								},
								method : 'POST',
								params : {
									ids : ids
								}// Ext.util.JSON.encode(selections.keys)
							});
				}
			}

		});