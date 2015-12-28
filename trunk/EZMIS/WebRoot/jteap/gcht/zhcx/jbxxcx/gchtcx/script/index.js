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
}

/**
 * 合同年统计
 */
function btnTotal_Click() {
	var url = link8 + '?tableName=TB_HT_GCHT';
	var args = "url|" + url + ";title|" + "工程合同详细信息";
	showModule(link7, true, 555, 170, args);
}

// 左边的树 右边的grid
var rightGrid = new RightGrid();
var leftTree = new LeftTree();
var searchAllFs = "合同名称#HTMC#textField,合同编号#HTBH#textField,申请部门#SQBM#comboTreeBm,审批状态#STATUS#comboStatus,操作时间#PROCESS_DATE1#dateField,至#PROCESS_DATE2#dateField"
		.split(',');
var searchDefaultFs = "合同名称#HTMC#textField,合同编号#HTBH#textField,申请部门#SQBM#comboTreeBm,审批状态#STATUS#comboStatus,操作时间#PROCESS_DATE1#dateField,至#PROCESS_DATE2#dateField"
		.split(',');
var searchPanel = new SearchPanel({
	region : 'north',
	searchDefaultFs : searchDefaultFs,
	searchAllFs : searchDefaultFs,
	txtWidth : 140
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