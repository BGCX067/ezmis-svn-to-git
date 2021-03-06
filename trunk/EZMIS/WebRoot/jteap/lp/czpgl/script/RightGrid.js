var dicts = $dictListAjax("SSZY");
function getQxzyCnName(qxzy) {
	for (var i = 0;i < dicts.length; i++) {
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
	var defaultDs = this.getDefaultDS();
	var grid = this;
	this.pageToolbar = new Ext.PagingToolbar({
		pageSize : CONSTANTS_PAGE_DEFAULT_LIMIT,
		store : defaultDs,
		displayInfo : true,
		displayMsg : '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg : "没有符合条件的数据",
		items : ['-', '<font color="red">*双击查看详细信息</font>']
	});
	RightGrid.superclass.constructor.call(this, {
		ds : defaultDs,
		cm : this.getColumnModel(),
		sm : this.sm,
		margins : '2px 2px 2px 2px',
		width : 600,
		height : 300,
		loadMask : true,
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
		} else if (oCheckboxSModel.getSelections().length == 1) {
			if (btnProcess != null) {
				btnProcess.setDisabled(false);
			}
			if (node.id == 'dcl') {
				if (btnCancel != null) {
					btnCancel.setDisabled(false);
				}
			} else if (node.id == 'cgx') {
				if (btnDelSq != null) {
					btnDelSq.setDisabled(false);
				}
			} else if (node.id == 'ycl') {
				if (btnUndo != null) {
					btnUndo.setDisabled(false);
				}
				if(btnCancel != null){
					btnCancel.setDisabled(true);
				}
			} else if (node.id =='zf'){
				if(btnCancel != null){
					btnCancel.setDisabled(true);
				}
			}
		}
	});

	this.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
		if (rightGrid.getSelectionModel().getSelections().length != 1) {
			alert("请选取一条记录查看");
			return;
		}

		var node = leftTree.getSelectionModel().getSelectedNode();
		var select = rightGrid.getSelectionModel().getSelections()[0];
		if (node.id == 'dcl') {
			var id = select.json.TASKTODOID;
			var pid = select.json.FLOW_INSTANCE_ID;
			var token = select.json.TOKEN;

			// 弹出流程查看窗口
			var url = link9;
			var windowUrl = link10 + "?pid=" + pid + "&id=" + id + "&token=" + token + "&isEdit=true";
			var args = "url|" + windowUrl + ";title|" + '查看流程';
			var retValue = showModule(url, "yes", 1100, 650, args);

			// 进行释放签收操作
			Ext.Ajax.request({
				url : link4,
				method : 'POST',
				params : {
					pid : pid,
					token : token
				},
				success : function(ajax) {
					var responseText = ajax.responseText;
					var obj = Ext.decode(responseText);
					if (obj.success) {
						rightGrid.getStore().reload();
					} else {
						alert("数据库操作异常，请联系管理员！");
					}
				},
				failure : function() {
					alert("数据库操作异常，请联系管理员！");
				}
			})
			rightGrid.getStore().reload();
		} else if (node.id == 'cgx') {
			var url = link9;
			var windowUrl = link8 + "?pid=" + select.get("ID_");
			var args = "url|" + windowUrl + ";title|" + '查看流程';
			var retValue = showModule(url, "yes", 1100, 650, args);
			rightGrid.getStore().reload();
		} else {
			var url = link9;
			var windowUrl = link10 + "?pid=" + select.get("ID_") + "&status=false";
			var args = "url|" + windowUrl + ";title|" + '查看流程';
			var retValue = showModule(url, "yes", 1100, 650, args);
		}
	});

}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
	sm : new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	}),

	/**
	 * 取得默认数据源 返回数据格式
	 */
	getDefaultDS : function(status, params) {
		var url = "";
		var procFields = [];
		// 待处理
		if (status == 'dcl') {
			url = link1;
			procFields = ["PH", "BZMC", "FZRMC", "JHKSSJ", "JHJSSJ", "CZKSSJH", "CZKSSJM", "CZJSSJH", "CZJSSJM", "STATUS", "ID",
					"PMC", "FLOW_TOPIC", "TASKTODOID", "FLOW_NAME", "FLOW_INSTANCE_ID", "CURRENT_TASKNAME",
					"POST_PERSON", "POST_TIME", "TOKEN", "CURSIGNIN", "curSignInName"];
			// 已处理
	} else if (status == 'ycl') {
		url = link2;
		procFields = ["PH", "BZMC", "FZRMC", "JHKSSJ", "JHJSSJ", "CZKSSJH", "CZKSSJM", "CZJSSJH", "CZJSSJM", "STATUS", "ID",
				"PMC", 'ID_', 'FLOW_NAME', 'START_', 'END_', 'PROCESSINSTANCE_', 'FLOW_CONFIG_ID'];
		// 草稿箱
	} else if (status == 'cgx') {
		url = link3;
		procFields = ["PH", "BZMC", "FZRMC", "JHKSSJ", "JHJSSJ", "CZKSSJH", "CZKSSJM", "CZJSSJH", "CZJSSJM", "STATUS", "ID",
				"PMC", 'ID_', 'FLOW_NAME', 'START_', 'END_', 'PROCESSINSTANCE_', 'FLOW_CONFIG_ID'];
		// 作废
	} else if (status == 'zf') {
		url = link6;
		procFields = ["PH", "BZMC", "FZRMC", "JHKSSJ", "JHJSSJ", "CZKSSJH", "CZKSSJM", "CZJSSJH", "CZJSSJM", "STATUS", "ID",
				"PMC", 'ID_', 'FLOW_NAME', 'START_', 'END_', 'PROCESSINSTANCE_', 'FLOW_CONFIG_ID'];
		// 全厂
	} else if (status == 'qc') {
		url = link5;
		procFields = ["PH", "BZMC", "FZRMC", "JHKSSJ", "JHJSSJ", "CZKSSJH", "CZKSSJM", "CZJSSJH", "CZJSSJM", "STATUS", "ID",
				"PMC", 'ID_', 'FLOW_NAME', 'START_', 'END_', 'PROCESSINSTANCE_', 'FLOW_CONFIG_ID'];
	}

	url += params;
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
		cm = new Ext.grid.ColumnModel([{
			id : 'PH',
			header : "票号",
			width : 120,
			sortable : true,
			dataIndex : 'PH'
		}, {
			id : 'FLOW_NAME',
			header : "票种类",
			width : 120,
			sortable : true,
			dataIndex : 'FLOW_NAME'
		}, {
			id : 'FZRMC',
			header : "操作人",
			width : 80,
			sortable : true,
			dataIndex : 'FZRMC'
		}, {
			id : 'STATUS',
			header : "操作票状态",
			width : 80,
			sortable : true,
			dataIndex : 'STATUS'
		}, {
			id : 'BZMC',
			header : "发现班组",
			width : 100,
			sortable : true,
			dataIndex : 'BZMC'
		}, {
			id : 'JHKSSJ',
			header : "填票日期",
			width : 120,
			sortable : true,
			dataIndex : 'JHKSSJ'
		}, {
			id : 'CZKSSJH',
			header : "开始时间",
			width : 120,
			sortable : true,
			dataIndex : 'CZJSSJH',
			renderer : function(value, metadata, record) {
				var minute = record.get('CZKSSJM');
				return value + "时" + minute + "分";
			}
		}, {
			id : 'CZJSSJH',
			header : "结束时间",
			width : 120,
			sortable : true,
			dataIndex : 'CZJSSJH',
			renderer : function(value, metadata, record) {
				var minute = record.get('CZJSSJM');
				return value + "时" + minute + "分";
			}
		}, {
			id : 'POST_PERSON',
			header : "发送人",
			width : 80,
			sortable : true,
			dataIndex : 'POST_PERSON'
		}, {
			id : 'POST_TIME',
			header : "发送时间",
			width : 130,
			sortable : true,
			dataIndex : 'POST_TIME'
		}, {
			id : 'curSignInName',
			header : "当前签收人",
			width : 75,
			sortable : true,
			dataIndex : 'curSignInName'
		}]);
		// 其他
	} else {
		cm = new Ext.grid.ColumnModel([{
			id : 'PH',
			header : "票号",
			width : 120,
			sortable : true,
			dataIndex : 'PH'
		}, {
			id : 'FLOW_NAME',
			header : "票种类",
			width : 120,
			sortable : true,
			dataIndex : 'FLOW_NAME'
		}, {
			id : 'FZRMC',
			header : "操作人",
			width : 80,
			sortable : true,
			dataIndex : 'FZRMC'
		}, {
			id : 'STATUS',
			header : "操作票状态",
			width : 80,
			sortable : true,
			dataIndex : 'STATUS'
		}, {
			id : 'BZMC',
			header : "发现班组",
			width : 100,
			sortable : true,
			dataIndex : 'BZMC'
		}, {
			id : 'JHKSSJ',
			header : "填票日期",
			width : 120,
			sortable : true,
			dataIndex : 'JHKSSJ'
		}, {
			id : 'CZKSSJH',
			header : "开始时间",
			width : 120,
			sortable : true,
			dataIndex : 'CZJSSJH',
			renderer : function(value, metadata, record) {
				var minute = record.get('CZKSSJM');
				return value + "时" + minute + "分";
			}
		}, {
			id : 'CZJSSJH',
			header : "结束时间",
			width : 120,
			sortable : true,
			dataIndex : 'CZJSSJH',
			renderer : function(value, metadata, record) {
				var minute = record.get('CZJSSJM');
				return value + "时" + minute + "分";
			}
		}, {
			id : 'START_',
			header : "流程开始时间",
			width : 140,
			sortable : true,
			dataIndex : 'START_',
			renderer : function(value, metadata, record, rowIndex, colIndex, store) {
				var dt = formatDate(new Date(value), "yyyy-MM-dd hh:mm:ss");
				return dt;
			}
		}, {
			id : 'END_',
			header : "流程结束时间",
			width : 140,
			sortable : true,
			dataIndex : 'END_',
			renderer : function(value, metadata, record, rowIndex, colIndex, store) {
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
changeToListDS : function(status, params) {
	var ds = this.getDefaultDS(status, params);
	var cm = this.getColumnModel(status);
	this.pageToolbar.bind(ds);
	this.reconfigure(ds, cm);
}

});
