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
	var retValue = showModule(url, true, 580, 265);
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
	var retValue = showModule(url, true, 580, 265);
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

// 左边的树 右边的grid
var rightGrid = new RightGrid();
var searchAllFs = "装设时间#zssj1#dateFieldShowTime,至#zssj2#dateFieldShowTime,接地线编号#jdxbh#textField,机组#jz#comboboxJz".split(',');
var searchDefaultFs = "装设时间#zssj1#dateFieldShowTime,至#zssj2#dateFieldShowTime,接地线编号#jdxbh#textField,机组#jz#comboboxJz".split(',');
var searchPanel = new SearchPanel({
	region : 'north',
	searchDefaultFs : searchDefaultFs,
	searchAllFs : searchDefaultFs,
	labelWidth : 80,
	txtWidth : 150
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
	items : [rightGrid,searchPanel]
}

// 北边
var lyNorth = {
	id : 'north-panel',
	region : 'north',
	height : 27,
	border : false,
	items : [mainToolbar]
}
