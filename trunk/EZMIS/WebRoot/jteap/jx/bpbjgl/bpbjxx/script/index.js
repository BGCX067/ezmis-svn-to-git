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
	if (button.id == 'btnModiZyfl' || button.id == 'btnDelZyfl' || button.id == 'btnAddBpbj'
			|| button.id == 'btnModiBpbj' || button.id == 'btnDelBpbj' || button.id == 'btnWzsq')
		button.setDisabled(true);
}

/**
 * 增加专业分类
 */
function btnAddZyfl_Click() {
	var oNode = leftTree.getSelectionModel().getSelectedNode();
	if (oNode.getDepth() == 2) {
		alert("此节点下不能增加专业分类");
		return;
	}
	leftTree.createZyfl();
}

/**
 * 修改专业分类
 */
function btnModiZyfl_Click() {
	leftTree.modifyZyfl();
}

/**
 * 删除专业分类
 */
function btnDelZyfl_Click() {
	leftTree.delZyfl();
}

/**
 * 增加备品备件信息
 */
function btnAddBpbj_Click() {
	var oNode = leftTree.getSelectionModel().getSelectedNode();
	var url = link7 + "?zyflId=" + oNode.id;
	var retValue = showModule(url, true, 560, 280);
	if (retValue == "true") {
		rightGrid.getStore().reload();
	}
}

/**
 * 修改备品备件信息
 */
function btnModiBpbj_Click() {
	var oNode = leftTree.getSelectionModel().getSelectedNode();
	var record = rightGrid.getSelectionModel().getSelected();
	var url = link7 + "?zyflId=" + oNode.id + "&id=" + record.get('id');
	var retValue = showModule(url, true, 560, 280);
	if (retValue == "true") {
		rightGrid.getStore().reload();
	}
}

/**
 * 删除备品备件信息
 */
function btnDelBpbj_Click() {
	rightGrid.delSelected();
}

/**
 * 物资申请
 */
function btnWzsq_Click() {
}

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
	items : [rightGrid]
}

// 北边
var lyNorth = {
	id : 'north-panel',
	region : 'north',
	height : 27,
	border : false,
	items : [mainToolbar]
}
