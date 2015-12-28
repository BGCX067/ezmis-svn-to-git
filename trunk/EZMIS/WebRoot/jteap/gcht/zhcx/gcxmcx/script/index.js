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

// 左边的树 右边的grid
var rightGrid = new RightGrid();
var leftTree = new LeftTree();
var searchAllFs = "项目编号#xmbh#textField,项目名称#xmmc#textField,项目类型#xmlx#comboBox,申请部门#sqbm#comboTree,创建时间#cjsj#yearField".split(',');
var searchDefaultFs = "项目编号#xmbh#textField,项目名称#xmmc#textField,项目类型#xmlx#comboBox,申请部门#sqbm#comboTree,创建时间#cjsj#yearField".split(',');
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