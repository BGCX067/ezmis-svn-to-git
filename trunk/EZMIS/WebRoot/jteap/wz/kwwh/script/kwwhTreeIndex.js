
//左边的树
var leftTree=new LeftTree();
	
//左边的树 右边的grid
var kwTreeGrid = new kwTreeGrid();
//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[kwTreeGrid],
	buttons:[{text:'确定',handler:function(){
		window.close();
	}}]
}
