/**
 * 字段列表
 */
RightGrid = function() {
	var defaultDs = this.getDefaultDS(link1);
	defaultDs.load();
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
				var btnModiBpbj = mainToolbar.items.get('btnModiBpbj');
				var btnDelBpbj = mainToolbar.items.get('btnDelBpbj');

				if (oCheckboxSModel.getSelections().length == 1) {
					if (btnModiBpbj)
						btnModiBpbj.setDisabled(false);
					if (btnDelBpbj)
						btnDelBpbj.setDisabled(false);
				} else if (oCheckboxSModel.getSelections().length > 1) {
					if (btnModiBpbj)
						btnModiBpbj.setDisabled(true);
					if (btnDelBpbj)
						btnDelBpbj.setDisabled(false);
				} else {
					if (btnModiBpbj)
						btnModiBpbj.setDisabled(true);
					if (btnDelBpbj)
						btnDelBpbj.setDisabled(true);
				}

			})

	this.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
				var oNode = leftTree.getSelectionModel().getSelectedNode();
				var record = grid.getSelectionModel().getSelected();
				var url = link9 + "?zyflId=" + oNode.id + "&id=" + record.get('id');
				showModule(url, true, 560, 280);
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
									}, ["id", "bpbjZyfl", "sbbm", "sbmc", "xsjgg", "dw", "edsl", "sjsl", "yjsl",
											"remark"]),
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
							width : 160,
							sortable : true,
							dataIndex : 'sbmc',
							renderer : function(value, metadata, record, rowIndex, columnIndex, store) {
								if (record.get('yjsl') > record.get('sjsl')) {
									return "<font color='red'>" + value + "</font>";
								} else {
									return value;
								}
							}
						}, {
							id : 'xsjgg',
							header : "型式及规格",
							width : 90,
							sortable : true,
							dataIndex : 'xsjgg'
						}, {
							id : 'dw',
							header : "单位",
							width : 50,
							align : 'center',
							sortable : true,
							dataIndex : 'dw'
						}, {
							id : 'edsl',
							header : "额定数量",
							align : 'right',
							width : 70,
							sortable : true,
							dataIndex : 'edsl'
						}, {
							id : 'sjsl',
							header : "实际数量",
							align : 'right',
							width : 70,
							sortable : true,
							dataIndex : 'sjsl'
						}, {
							id : 'yjsl',
							header : "预警数量",
							align : 'right',
							width : 70,
							sortable : true,
							dataIndex : 'yjsl'
						}, {
							id : 'remark',
							header : "备注说明",
							width : 190,
							sortable : true,
							dataIndex : 'remark',
							renderer : function(value, metadata, record, rowIndex, columnIndex, store) {
								metadata.attr = 'ext:qtip="' + value + '"';
								return value;
							}
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
								url : link8,
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
