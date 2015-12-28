
var mainToolbar = null;
//左边的树
var leftTree=new LeftTree();
//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="物资名称#wzmc#textField,型号规格#xhgg#textField,物资编码#wzbm#textField".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="物资名称#wzmc#textField,型号规格#xhgg#textField,物资编码#wzbm#textField".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs,labelWidth:70,txtWidth:70});

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
	items:[rightPanel,searchPanel],
	buttons:[{text:'确定',handler:function(){
		window.close();
	}}]
}

