// 工具栏
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
	if (button.id == 'btnModi' || button.id == 'btnDel') {
		button.setDisabled(true);
	}
}

/**
 * 新建
 */
function btnAdd_Click() {
	var url = link2;
	var retValue = showModule(url, true, 580, 300);
	if (retValue) {
		rightGrid.getStore().reload();
	}
}

/**
 * 修改
 */
function btnModi_Click() {
	var record = rightGrid.getSelectionModel().getSelected();
	var url = link2 + "?id=" + record.get('id');
	var retValue = showModule(url, true, 580, 300);
	if (retValue) {
		rightGrid.getStore().reload();
	}
}

/**
 * 删除
 */
function btnDel_Click() {
	rightGrid.deleteSelect();
}

// 查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs = "设备名称#sbmc#textField,动作时间#dzsj1#dateFieldShowTime,至#dzsj2#dateFieldShowTime".split(",");
// 查询面板中默认显示的条件，格式同上
var searchDefaultFs = "设备名称#sbmc#textField,动作时间#dzsj1#dateFieldShowTime,至#dzsj2#dateFieldShowTime".split(",");
var searchPanel = new SearchPanel({
	searchDefaultFs : searchDefaultFs,
	searchAllFs : searchAllFs,
	txtWidth : 150
});

var rightGrid = new RightGrid();
rightGrid.getStore().reload();

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