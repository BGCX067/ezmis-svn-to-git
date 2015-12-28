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
	if (button.id == 'btnModiJh' || button.id == 'btnDelJh') {
		button.setDisabled(true);
	}
}

/**
 * 新建计划
 */
function btnAddJh_Click() {
	var url = link2;
	var retValue = showModule(url, true, 620, 380);
	if (retValue == "true") {
		rightGrid.getStore().reload();
	}
}

/**
 * 修改计划
 */
function btnModiJh_Click() {
	var record = rightGrid.getSelectionModel().getSelected();
	var url = link2 + "?id=" + record.get('id');
	var retValue = showModule(url, true, 620, 380);
	if (retValue == "true") {
		rightGrid.getStore().reload();
	}
}

/**
 * 删除计划
 */
function btnDelJh_Click() {
	rightGrid.delSelected();
}

// 左边的树 右边的grid
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
