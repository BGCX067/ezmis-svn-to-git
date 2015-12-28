
//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="负责部门#fzbm#comboBox,年月#ny#datepicker".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="负责部门#fzbm#comboBox,年月#ny#datepicker".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});

var nowDate = new Date();
var nowYm = nowDate.format("Y-m");

Ext.getCmp("sf#ny").setValue(nowYm);

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
