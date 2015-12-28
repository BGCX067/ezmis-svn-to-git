// 左边的树 右边的grid
var rightGrid = new RightGrid();
var leftTree = new LeftTree();

// 查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs = "票号#ph#textField,票类型#pfl#comboBoxPfl,发现班组#bz#comboBoxBz,开始时间#jhkssj#dateField,结束时间#jhjssj#dateField"
		.split(',');
// 查询面板中默认显示的条件，格式同上
var searchDefaultFs = "票号#ph#textField,票类型#pfl#comboBoxPfl,发现班组#bz#comboBoxBz,开始时间#jhkssj#dateField,结束时间#jhjssj#dateField"
		.split(',');
var searchPanel = new SearchPanel({
	region : 'north',
	searchDefaultFs : searchDefaultFs,
	searchAllFs : searchDefaultFs
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
	items : [searchPanel, rightGrid]
}
