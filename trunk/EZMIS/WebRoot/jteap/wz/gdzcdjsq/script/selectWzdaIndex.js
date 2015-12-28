
var mainToolbar = null;
//左边的树
var leftTree=new LeftTree();

//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
//	items:[searchPanel,rightPanel],
	items:[rightPanel],
	buttons:[{text:'确定',handler:function(){
		window.close();
	}}]
}

