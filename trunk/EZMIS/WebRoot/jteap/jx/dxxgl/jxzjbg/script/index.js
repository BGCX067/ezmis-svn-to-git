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
	if (button.id == 'btnSaveBg' || button.id == 'btnEditBg' || button.id == 'btnDelBg')
		button.setDisabled(true);
}

/**
 * 保存报告
 */
function btnSaveBg_Click() {
	rightForm.saveForm();
}

/**
 * 编辑报告
 */
function btnEditBg_Click() {
	rightForm.setFormDisabled(false);
	rightForm.initData();
	var btnSaveBg = mainToolbar.items.get('btnSaveBg');
	if (btnSaveBg) {
		btnSaveBg.setDisabled(false);
	}
	
}

/**
 * 删除报告
 */
function btnDelBg_Click() {
	rightForm.delSelected();
}

var leftTree = new LeftTree();
var rightForm = new RightForm();

// 中间
var lyCenter = {
	layout : 'border',
	id : 'center-panel',
	region : 'center',
	minSize : 175,
	maxSize : 400,
	border : false,
	margins : '0 0 0 0',
	items : [rightForm]
}

// 北边
var lyNorth = {
	id : 'north-panel',
	region : 'north',
	height : 27,
	border : false,
	items : [mainToolbar]
}
