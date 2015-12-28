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
	if (button.id == 'btnMakeBz' || button.id == 'btnModiPz' || button.id == 'btnDelPz')
		button.setDisabled(true);
}

/**
 * 添加标准票
 */
function btnAddPz_Click() {
	var url = link5;
	var retValue = showModule(url, true, 590, 320);
	if (retValue == "true") {
		rightGrid.getStore().reload();
	}
}

/**
 * 生成标准票
 */
function btnMakeBz_Click() {
	var records = rightGrid.getSelectionModel().getSelections();
	var ids = new Array();
	for (var i = 0; i < records.length; i++) {
		var record = records[i];
		if (record.get('isBzp') == '1') {
			alert('所选票中已有标准票，只能将非标准票生成为标准票！');
			return;
		} else {
			ids.push(record.get('id'));
		}
	}
	Ext.Ajax.request({
		url : link9,
		method : 'POST',
		params : {
			ids : ids.toString()
		},
		success : function(ajax) {
			var resText = ajax.responseText;
			var obj = Ext.decode(resText);
			if (obj.success && obj.success == true) {
				alert('标准票生成成功！');
				rightGrid.getStore().reload();
			} else {
				alert(obj.msg)
			}
		},
		failure : function() {
			alert('数据库异常，请联系管理员！');
		}
	})
}

/**
 * 修改票种
 */
function btnModiPz_Click() {
	var record = rightGrid.getSelectionModel().getSelected();
	var id = record.get('id');
	var url = link5 + "?id=" + id;
	var retValue = showModule(url, true, 590, 320);
	if (retValue == "true") {
		rightGrid.getStore().reload();
	}
}

/**
 * 删除票种
 */
function btnDelPz_Click() {
	rightGrid.delSelected();
}

// 查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs = "票名称#pmc#textField,票状态#pzt#textField".split(',');
// 查询面板中默认显示的条件，格式同上
var searchDefaultFs = "票名称#pmc#textField,票状态#pzt#textField".split(',');
var searchPanel = new SearchPanel({
	region : 'north',
	searchDefaultFs : searchDefaultFs,
	searchAllFs : searchDefaultFs
});

// 左边的树 右边的grid
var rightGrid = new RightGrid();
var leftTree = new LeftTree();

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
