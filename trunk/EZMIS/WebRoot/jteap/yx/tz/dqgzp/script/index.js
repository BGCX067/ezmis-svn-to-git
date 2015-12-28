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
	if (button.id == 'btnModi' || button.id == 'btnDel' || button.id=='btnModiAll') {
		button.setDisabled(true);
	}
}

/**
 * 新建
 */
function btnAdd_Click() {
	var url = link2;
	var retValue = showModule(url, true, 680, 600);
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
	var retValue = showModule(url, true, 680, 600);
	if (retValue) {
		rightGrid.getStore().reload();
	}
}

/**
 * 修改全部记录
 */
function btnModiAll_Click() {
	var record = rightGrid.getSelectionModel().getSelected();
	var url = link8 + "?id=" + record.get('id');
	var retValue = showModule(url, true, 680, 600);
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
var searchAllFs = "工作票编号#gzpbh#textField,批准工作期限#pzgzqx1#dateFieldShowTime,至#pzgzqx2#dateFieldShowTime,工作票状态#gzpzt#comboZt,许可开工时间#xksj1#dateFieldShowTime,至#xksj2#dateFieldShowTime,地点及任务#ddjrw#textField,终结时间#zjsj1#dateFieldShowTime,至#zjsj2#dateFieldShowTime"
		.split(',');
var searchDefaultFs = "工作票编号#gzpbh#textField,批准工作期限#pzgzqx1#dateFieldShowTime,至#pzgzqx2#dateFieldShowTime,工作票状态#gzpzt#comboZt,许可开工时间#xksj1#dateFieldShowTime,至#xksj2#dateFieldShowTime,地点及任务#ddjrw#textField,终结时间#zjsj1#dateFieldShowTime,至#zjsj2#dateFieldShowTime"
		.split(',');
var searchPanel = new SearchPanel({
	region : 'north',
	searchDefaultFs : searchDefaultFs,
	searchAllFs : searchDefaultFs,
	txtWidth : 160,
	labelWidth : 100
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
