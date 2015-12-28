var dicts = $dictListAjax("WZXQJHSQ");
function getStatusCnName(qxzy) {
	for (var i = 0; i < dicts.length; i++) {
		var dict = dicts[i];
		if (dict.value == qxzy) {
			return dict.key;
		}
	}
}
/**
 * 字段列表
 */
RightGrid = function() {
	var defaultDs = this.getDefaultDS(link1);
	var grid = this;
	this.pageToolbar = new Ext.PagingToolbar({
				pageSize : CONSTANTS_PAGE_DEFAULT_LIMIT,
				store : defaultDs,
				displayInfo : true,
				displayMsg : '共{2}条数据，目前为 {0} - {1} 条',
				emptyMsg : "没有符合条件的数据"
				/*
				items : ['-', {
							text : '导出Excel',
							handler : function() {
								exportExcel(grid, true);
							}
						}, '-', '<font color="red"></font>']
						*/
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
			
	// 当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange", function(oCheckboxSModel) {
			var node = leftTree.getSelectionModel().getSelectedNode();
			var btnProcess = mainToolbar.items.get("btnProcess");
			var btnCancel = mainToolbar.items.get("btnCancel");
			var btnDelSq = mainToolbar.items.get("btnDelSq");
			var btnUndo = mainToolbar.items.get("btnUndo");

			if (btnProcess != null) {
				btnProcess.setDisabled(true);
			}
			if (btnCancel != null) {
				btnCancel.setDisabled(true);
			}
			if (btnDelSq != null) {
				btnDelSq.setDisabled(true);
			}
			if (btnUndo != null) {
				btnUndo.setDisabled(true);
			}
			if (oCheckboxSModel.getSelections().length > 1) {
				if (node.id == 'cgx') {
					if (btnDelSq != null) {
						btnDelSq.setDisabled(false);
					}
				}
			} else if (oCheckboxSModel.getSelections().length = 1) {
				if (btnProcess != null) {
					btnProcess.setDisabled(false);
				}
				if (node.id == 'dcl') {
					if (btnProcess != null) {
						btnProcess.setDisabled(false);
						btnCancel.setDisabled(false);
						btnUndo.setDisabled(true);
						btnDelSq.setDisabled(true);
					}
				} else if (node.id == 'cgx') {
					if (btnCancel != null) {
						btnProcess.setDisabled(false);
						btnDelSq.setDisabled(false);
						btnCancel.setDisabled(true);
						btnUndo.setDisabled(true);
					}
				} else if (node.id == 'ycl') {
					if (btnUndo != null) {
						btnUndo.setDisabled(false);
						btnCancel.setDisabled(true);
						btnProcess.setDisabled(true);
						btnDelSq.setDisabled(true);
					}
				} else if (node.id == 'zf') {
					if (btnDelSq != null) {
						btnDelSq.setDisabled(false);
						btnUndo.setDisabled(true);
						btnCancel.setDisabled(true);
						btnProcess.setDisabled(true);
					}
				}
			}
		});

	this.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
				var select = rightGrid.getSelectionModel().getSelections()[0];
				if (select.get("type") == "EFORM") {
					// var eformUrl=select.get("eformUrl");

					var url = "/jteap/cform/fceform/common/djframe.htm?djsn="
							+ select.get("sn") + "&catalogName="
							+ select.get("eformUrl")
					window.open(CONTEXT_PATH + url);
				}

				if (select.get('type') == "EXCEL") {
					var url = CONTEXT_PATH
							+ "/jteap/cform/excelFormRec.jsp?cformId="
							+ select.json.id;
					var features = "menubar=no,toolbar=no,width=800,height=600";
					window.open(url, "_blank", features);
				}
			});

}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
	sm : new Ext.grid.CheckboxSelectionModel(),

	/**
	 * 取得默认数据源 返回数据格式
	 * {success:true,totalCount:20,list:[{id:'123',field1:'111',...,field5'222'},{...},...]}
	 */
	getDefaultDS : function(status) {
		var url = "";
		var procFields = [];
		// 待处理
		if (status == 'dcl') {
			url = link2;
			procFields = ['ID', 'BH', 'GCLB', 'GCXM', 'SQBMMC','FLOW_STATUS', 'CZYXM',
					'SQSJ','ZT', 'STATUS', "FLOW_TOPIC", "TASKTODOID", "FLOW_NAME",
					"FLOW_INSTANCE_ID", "CURRENT_TASKNAME", "POST_PERSON",
					"POST_TIME", "TOKEN", "CURSIGNIN", "CURSIGNINNAME"];
			// 已处理
		} else if (status == 'ycl') {
			url = link3;
			procFields = ['ID', 'BH', 'GCLB', 'GCXM', 'SQBMMC','FLOW_STATUS', 'CZYXM',
					'SQSJ', 'STATUS', 'FLOW_NAME', 'START_', 'END_',
					'PROCESSINSTANCE_', 'FLOW_CONFIG_ID'];
			// 草稿箱
		} else if (status == 'cgx') {
			url = link1;
			procFields = ['ID', 'BH', 'GCLB', 'GCXM', 'SQBMMC','FLOW_STATUS', 'CZYXM',
					'SQSJ', 'STATUS', 'ID_', 'FLOW_NAME', 'START_', 'END_',
					'PROCESSINSTANCE_', 'FLOW_CONFIG_ID'];
			// 作废
		} else if (status == 'zf') {
			url = link4;
			procFields = ['ID', 'BH', 'GCLB', 'GCXM', 'SQBMMC','FLOW_STATUS', 'CZYXM',
					'SQSJ', 'STATUS', 'ID_', 'FLOW_NAME', 'START_', 'END_',
					'PROCESSINSTANCE_', 'FLOW_CONFIG_ID'];
			// 全厂
		} else if (status == 'all') {
			url = link;
			procFields = ['ID', 'BH', 'GCLB', 'GCXM', 'SQBMMC','FLOW_STATUS', 'CZYXM',
					'SQSJ', 'STATUS', 'ID_', 'FLOW_NAME', 'START_', 'END_',
					'PROCESSINSTANCE_', 'FLOW_CONFIG_ID'];
		}
		var ds = new Ext.data.Store({
					proxy : new Ext.data.ScriptTagProxy({
								url : url
							}),
					reader : new Ext.data.JsonReader({
								root : 'list',
								totalProperty : 'totalCount',
								id : 'id'
							}, procFields),
					remoteSort : true
				});
				
		return ds;
	},

	/**
	 * PersonAction 列模型
	 */
	getColumnModel : function(status) {
		var cm;
		// 待处理
		if (status == 'dcl') {
			cm = new Ext.grid.ColumnModel([this.sm, {
						id : 'CURRENT_TASKNAME',
						header : "流程步骤",
						width : 120,
						sortable : true,
						dataIndex : 'CURRENT_TASKNAME'
					}, {
						id : 'BH',
						header : "物资借料申请编号",
						width : 120,
						sortable : true,
						dataIndex : 'BH'
					}, {
						id : 'POST_PERSON',
						header : "提交人",
						width : 100,
						sortable : true,
						dataIndex : 'POST_PERSON'
					}, {
						id : 'GCLB',
						header : "工程类别",
						width : 80,
						sortable : true,
						dataIndex : 'GCLB'
					}, {
						id : 'GCXM',
						header : "工程项目",
						width : 100,
						sortable : true,
						dataIndex : 'GCXM'
					}, {
						id : 'SQBMMC',
						header : "申请部门",
						width : 100,
						sortable : true,
						dataIndex : 'SQBMMC'
					}, {
						id : 'CZYXM',
						header : "操作员",
						width : 100,
						sortable : true,
						dataIndex : 'CZYXM'
					}, {
						id : 'SQSJ',
						header : "申请时间",
						width : 100,
						sortable : true,
						dataIndex : 'SQSJ',
						renderer : function(value, metadata, record, rowIndex,
								colIndex, store) {
							if (!value)
								return;
							var dt = formatDate(new Date(value), "yyyy-MM-dd");
							return dt;
						}
					}, {
						id : 'FLOW_STATUS',
						header : "物资借料申请单状态",
						width : 100,
						sortable : true,
						dataIndex : 'FLOW_STATUS'
//						renderer : function(value, metadata, record, rowIndex,
//								colIndex, store) {
//							if (!value)
//								return "填写申请";
//							return getStatusCnName(value);
//						}
					}, {
						id : 'curSignInName',
						header : "当前签收人",
						width : 75,
						sortable : true,
						dataIndex : 'CURSIGNINNAME'
					}]);
			// 其他
		} else {
			cm = new Ext.grid.ColumnModel([this.sm, {
				id : 'FLOW_STATUS',
				header : "物资借料申请单状态",
				width : 120,
				sortable : true,
				dataIndex : 'FLOW_STATUS'
//				renderer : function(value, metadata, record, rowIndex,
//						colIndex, store) {
//					if (!value)
//						return "填写申请";
//					return getStatusCnName(value);
//				}
			}, {
				id : 'BH',
				header : "物资借料申请编号",
				width : 160,
				sortable : true,
				dataIndex : 'BH'
			}, {
				id : 'GCLB',
				header : "工程类别",
				width : 80,
				sortable : true,
				dataIndex : 'GCLB'
			}, {
				id : 'GCXM',
				header : "工程项目",
				width : 100,
				sortable : true,
				dataIndex : 'GCXM'
			}, {
				id : 'SQBMMC',
				header : "申请部门",
				width : 100,
				sortable : true,
				dataIndex : 'SQBMMC'
			}, {
				id : 'CZYXM',
				header : "操作员",
				width : 100,
				sortable : true,
				dataIndex : 'CZYXM'
			}, {
				id : 'SQSJ',
				header : "申请时间",
				width : 100,
				sortable : true,
				dataIndex : 'SQSJ',
				renderer : function(value, metadata, record, rowIndex,
						colIndex, store) {
					if (!value)
						return;
					var dt = formatDate(new Date(value), "yyyy-MM-dd");
					return dt;
				}
			}, {
				id : 'START_',
				header : "开始时间",
				width : 140,
				sortable : true,
				dataIndex : 'START_',
				renderer : function(value, metadata, record, rowIndex,
						colIndex, store) {
					var dt = formatDate(new Date(value), "yyyy-MM-dd hh:mm:ss");
					return dt;
				}
			}, {
				id : 'END_',
				header : "结束时间",
				width : 140,
				sortable : true,
				dataIndex : 'END_',
				renderer : function(value, metadata, record, rowIndex,
						colIndex, store) {
					if (value == null) {
						return "";
					}
					var dt = formatDate(new Date(value), "yyyy-MM-dd hh:mm:ss");
					return dt;
				}
			}]);
		}
		return cm;
	},
	/**
	 * 切换数据源->LogAction!showList
	 */
	changeToListDS : function(status) {
		var ds = this.getDefaultDS(status);
		var cm = this.getColumnModel(status);
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
