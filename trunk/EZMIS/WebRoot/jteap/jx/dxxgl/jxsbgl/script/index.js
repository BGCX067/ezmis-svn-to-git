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
	if (button.id == 'btnAddJxsb' || button.id == 'btnModiJxsb' || button.id == 'btnDelJxsb')
		button.setDisabled(true)
}

/**
 * 增加检修设备
 */
function btnAddJxsb_Click() {
	var node = leftTree.getSelectionModel().getSelectedNode();
	var url = link5 + "?sszy=" + node.attributes.value;
	var retValue = showModule(url, true, 300, 170);
	if (retValue == "true") {
		rightGrid.getStore().reload();
	}
}

/**
 * 修改检修设备
 */
function btnModiJxsb_Click() {
	var record = rightGrid.getSelectionModel().getSelected();
	var node = leftTree.getSelectionModel().getSelectedNode();
	var url = link5 + "?sszy=" + node.attributes.value + "&id=" + record.get('id');
	var retValue = showModule(url, true, 300, 170);
	if (retValue == "true") {
		rightGrid.getStore().reload();
	}
}

/**
 * 删除检修设备
 */
function btnDelJxsb_Click() {
	rightGrid.delSelected();
}

// 左边的树 右边的grid
var rightGrid = new RightGrid();
var leftTree = new LeftTree();
var searchAllFs = "设备名称#sbmc#textField".split(",");
var searchDefaultFs = "设备名称#sbmc#textField".split(",");

var searchPanel = new SearchPanel({
			searchDefaultFs : searchDefaultFs,
			searchAllFs : searchAllFs,
			labelWidth : 70,
			txtWidth : 120
		});

// 中间
var lyCenter = {
	layout : 'border',
	id : 'center-panel',
	region : 'center',
	minSize : 175,
	maxSize : 400,
	border : false,
	margins : '0 0 0 0',
	items : [rightGrid, searchPanel]
}

// 北边
var lyNorth = {
	id : 'north-panel',
	region : 'north',
	height : 27,
	border : false,
	items : [mainToolbar]
}
