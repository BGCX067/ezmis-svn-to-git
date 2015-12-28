
//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="记录类别#jllb#comboBox,岗位类别#gwlb#comboBox,关键字#jlnr#textField,开始时间#startDate#dateField,结束时间#endDate#dateField,记录人#jlr#textField".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="记录类别#jllb#comboBox,岗位类别#gwlb#comboBox,关键字#jlnr#textField,开始时间#startDate#dateField,结束时间#endDate#dateField,记录人#jlr#textField".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});

var rightGrid=new RightGrid();
rightGrid.getStore().reload();

//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[searchPanel,rightGrid]
}
