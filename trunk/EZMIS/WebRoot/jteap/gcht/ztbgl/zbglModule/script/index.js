// 左边的树 右边的grid
var rightGrid = new RightGrid();
var leftTree = new LeftTree();
var searchAllFs = "项目名称#xmmc#textField,招标批次#zbpc#textField,招标方式#zbfs#comboZbfs,招标地址#zbdz#textField,计划时间#jhzbsj1#dateField,至#jhzbsj2#dateField,实际时间#sjzbsj1#dateField,至#sjzbsj2#dateField"
		.split(',');
var searchDefaultFs = "项目名称#xmmc#textField,招标批次#zbpc#textField,招标方式#zbfs#comboZbfs,招标地址#zbdz#textField,计划时间#jhzbsj1#dateField,至#jhzbsj2#dateField,实际时间#sjzbsj1#dateField,至#sjzbsj2#dateField"
		.split(',');
var searchPanel = new SearchPanel({
	region : 'north',
	searchDefaultFs : searchDefaultFs,
	searchAllFs : searchDefaultFs,
	txtWidth : 100
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
