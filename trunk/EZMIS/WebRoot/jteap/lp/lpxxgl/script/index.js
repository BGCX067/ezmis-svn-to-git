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
	if (button.id == 'btnAddAqcsCatalog' || button.id == 'btnModiAqcsCatalog' || button.id == 'btnDelAqcsCatalog'
			|| button.id == 'btnModiAqcs' || button.id == 'btnDelAqcs' || button.id == 'btnAddAqcs')
		button.setDisabled(true);
}

/**
 * 添加安全措施
 */
function btnAddAqcs_Click() {
	var oNode = leftTree.getSelectionModel().getSelectedNode();
	var url = link6+"?flid="+oNode.id+"&flmc="+encodeURIComponent(oNode.text);
	var result = showModule(url, true, 530, 170);
	if (result == "true") {
		rightGrid.getStore().reload();
	}
}
/**
 * 修改安全措施
 */
function btnModiAqcs_Click() {
	var record = rightGrid.getSelectionModel().getSelected();
	var nodeId = record.get('aqcsCatalog.id');
	var flmc = record.get('aqcsCatalog.flmc');
	var url = link6+"?flid="+nodeId+"&flmc="+encodeURIComponent(flmc)+"&id="+record.get('id');
	var result = showModule(url, true, 530, 170);
	if (result == "true") {
		rightGrid.getStore().reload();
	}
}

/**
 * 删除安全措施
 */
function btnDelAqcs_Click() {
	var select = rightGrid.getSelectionModel().getSelections();
	rightGrid.deleteSelect(select);
}

/**
 * 删除分类
 */
function btnDelAqcsCatalog_Click() {
	leftTree.deleteSelectedNode();
}

/**
 * 添加分类
 */
function btnAddAqcsCatalog_Click() {
	leftTree.createNode();
}

/**
 * 修改分类
 */
function btnModiAqcsCatalog_Click() {
	leftTree.modifyNode();
}

var leftTree = new LeftTree();
// 左边的树 右边的grid

// 查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs = "所属分类#aqcsCatalog.flmc#textField, 措施名称#csmc#textField,措施内容#csnr#textField".split(",");
// 查询面板中默认显示的条件，格式同上
var searchDefaultFs = "所属分类#aqcsCatalog.flmc#textField, 措施名称#csmc#textField,措施内容#csnr#textField".split(",");
var searchPanel = new SearchPanel({
	searchDefaultFs : searchDefaultFs,
	searchAllFs : searchAllFs
});

var rightGrid = new RightGrid();

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
