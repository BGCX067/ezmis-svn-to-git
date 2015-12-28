
//数据字典
var dict_dqgzgl=$dictList("dqgzgl");
var dict_dqgzzy=$dictList("dqgzzy");

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
	items:[rightGrid]
}
