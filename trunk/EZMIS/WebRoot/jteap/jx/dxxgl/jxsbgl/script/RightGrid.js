/**
 * 字段列表
 */
RightGrid = function() {
	var defaultDs = this.getDefaultDS(link2);
	var grid = this;
	this.pageToolbar = new Ext.PagingToolbar({
				pageSize : CONSTANTS_PAGE_DEFAULT_LIMIT,
				store : defaultDs,
				displayInfo : true,
				displayMsg : '共{2}条数据，目前为 {0} - {1} 条',
				emptyMsg : "没有符合条件的数据",
				items : ['-', {
							text : '导出Excel',
							handler : function() {
								exportExcel(grid, true);
							}
						}]
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
				tbar : this.pageToolbar
			});

	this.getSelectionModel().on("selectionchange", function(oCheckboxSModel) {
				var btnModiJxsb = mainToolbar.items.get('btnModiJxsb');
				var btnDelJxsb = mainToolbar.items.get('btnDelJxsb');

				if (oCheckboxSModel.getSelections().length == 1) {
					if (btnModiJxsb)
						btnModiJxsb.setDisabled(false);
					if (btnDelJxsb)
						btnDelJxsb.setDisabled(false);
				} else if (oCheckboxSModel.getSelections().length >= 1) {
					if (btnModiJxsb)
						btnModiJxsb.setDisabled(true);
					if (btnDelJxsb)
						btnDelJxsb.setDisabled(false);
				} else {
					if (btnModiJxsb)
						btnModiJxsb.setDisabled(true);
					if (btnDelJxsb)
						btnDelJxsb.setDisabled(true);
				}

			})
}

Ext.extend(RightGrid, Ext.grid.GridPanel, {
			sm : new Ext.grid.CheckboxSelectionModel(),

			/**
			 * 取得默认数据源 返回数据格式
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
									}, ["id", "sszy", "sbmc", "jxzq", "xmjb", "xmxh", "sortNo"]),
							remoteSort : true
						});
				return ds;
			},

			/**
			 * PersonAction 列模型
			 */
			getColumnModel : function() {
				var cm = new Ext.grid.ColumnModel([this.sm, {
							id : 'sbmc',
							header : "设备名称",
							width : 300,
							sortable : true,
							dataIndex : 'sbmc'
						}, {
							id : 'jxzq',
							header : "检修周期",
							width : 80,
							align : 'center',
							sortable : true,
							dataIndex : 'jxzq'
						}, {
							id : 'xmjb',
							header : "项目级别",
							width : 70,
							sortable : true,
							dataIndex : 'xmjb'
						}, {
							id : 'xmxh',
							header : "项目序号",
							width : 70,
							sortable : true,
							dataIndex : 'xmxh'
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
			delSelected : function() {
				var selections = this.getSelections();
				var ids = "";
				var grid = this;
				Ext.each(selections, function(selectedobj) {
							ids += selectedobj.id + ",";
						});
				if (window.confirm("确认删除吗")) {
					Ext.Ajax.request({
								url : link6,
								success : function(ajax) {
									var responseText = ajax.responseText;
									var responseObject = Ext.util.JSON.decode(responseText);
									if (responseObject.success) {
										alert("删除成功");
										grid.getStore().reload();
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
								}
							});
				}
			}
		});
