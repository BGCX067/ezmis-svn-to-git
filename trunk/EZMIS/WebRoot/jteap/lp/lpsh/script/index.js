var mainToolbar = new Ext.Toolbar({
	height : 26,
	listeners : {
		render : function(tb) {
			operationsToolbarInitialize(tb);
		}
	}
});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button) {
	if (button.id == 'btnSh' || button.id == 'btnCkpmxx')
		button.setDisabled(true);
}

/**
 * 两票审核
 */
function btnSh_Click() {
	var record = rightGrid.getSelectionModel().getSelected();
	var arg = "";
	arg += "id|" + record.get('ID') + ";";
	arg += "ph|" + record.get('PH') + ";";
	arg += "pmc|" + record.get('PMC') + ";";
	arg += "jhkssj|" + record.get('JHKSSJ') + ";";
	arg += "jhjssj|" + record.get('JHJSSJ') + ";";
	arg += "tablename|" + record.get('TABLENAME') + ";";
	arg += "shr|" + ((record.get('SHR') == null) ? "" : record.get('SHR')) + ";";
	arg += "shbm|" + ((record.get('SHBM') == null) ? "" : record.get('SHBM')) + ";";
	arg += "shsj|" + ((record.get('SHSJ') == null) ? "" : record.get('SHSJ')) + ";";
	arg += "shzt|" + ((record.get('SHZT') == null) ? "" : record.get('SHZT')) + ";";
	arg += "pz|" + ((record.get('PZ') == null) ? "" : record.get('PZ'));

	var url = link3;
	var retValue = showModule(url, true, 590, 320, arg);
	if (retValue == "true") {
		rightGrid.getStore().reload();
	}
}

/**
 * 查看票面信息
 */
function btnCkpmxx_Click() {
	var record = rightGrid.getSelectionModel().getSelected();
	var id = record.get('ID');
	Ext.Ajax.request({
		url : link6,
		method : 'POST',
		params : {
			id : id
		},
		success : function(ajax) {
			var resText = ajax.responseText;
			var obj = Ext.decode(resText);
			if (obj.success) {
				var pid = obj.pid;
				var url = link7;
				var windowUrl = link8 + "?pid=" + pid + "&status=false";
				var args = "url|" + windowUrl + ";title|" + '查看流程';
				var retValue = showModule(url, "yes", 1100, 650, args);
			} else {
				alert('数据异常，请联系管理员！');
			}
		},
		failure : function() {
			alert('数据异常，请联系管理员！');
		}
	})
}

// 查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs = "开始时间#jhkssj#dateField,结束时间#jhjssj#dateField".split(',');
// 查询面板中默认显示的条件，格式同上
var searchDefaultFs = "开始时间#jhkssj#dateField,结束时间#jhjssj#dateField".split(',');
var searchPanel = new SearchPanel({
	region : 'north',
	searchDefaultFs : searchDefaultFs,
	searchAllFs : searchDefaultFs
});

// 左边的树 右边的grid
var rightGrid = new RightGrid();
var leftTree = new LeftTree();
leftTree.expandAll();

// 中间
var lyCenter = {
	layout : 'border',
	id : 'center-panel',
	region : 'center',
	minSize : 175,
	maxSize : 400,
	border : false,
	margins : '0 0 0 0',
	items : [searchPanel, rightGrid]
}

// 北边
var lyNorth = {
	id : 'north-panel',
	region : 'north',
	height : 27,
	border : false,
	items : [mainToolbar]
}
