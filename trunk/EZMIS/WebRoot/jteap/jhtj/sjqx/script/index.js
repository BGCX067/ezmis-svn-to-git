
//工具栏  
//1.添加分类
//2.修改分类
//3.删除分类
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
//	items:[
//		{disabled:true,id:'btnAdd',text:'新建',cls: 'x-btn-text-icon',icon:'icon/icon_4.gif',listeners:{click:mainToolBarButtonClick}},
//		{disabled:true,id:'btnModify',text:'修改',cls: 'x-btn-text-icon',icon:'icon/icon_6.gif',listeners:{click:mainToolBarButtonClick}},
//		{disabled:true,id:'btnDel',text:'删除',cls: 'x-btn-text-icon',icon:'icon/icon_7.gif',listeners:{click:mainToolBarButtonClick}}
//		]
});
/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnAddCatalog'||
	   button.id=='btnModiCatalog'||
	   button.id=='btnDelCatalog'||
	   button.id=='btnAddKey'||
	   button.id=='btnModiKey'||
	   button.id=='btnDelKey'||
	   button.id=='btnGhostKey'){ 
		button.setDisabled(true);
	}
}








/**
 * 增加分类
 */
function btnAddCatalog_Click(){
	var flTree=sjqxForm.getFlTree();
	var bbTree=sjqxForm.getBbTree();
	
	var flResult = flTree.getQxNameAndId();
	flResult = "[" + flResult + "]";
	var flArrays = Ext.decode(flResult);
	//var userName = "";
	var flId = "";
	for (var i = 0;i < flArrays.length; i++) {
		//根节点不被包含在内
		if(flArrays[i].id.indexOf('ynode')==-1)
		flId += flArrays[i].id + ",";
	}
	
	var bbResult = bbTree.getQxNameAndId();
	bbResult = "[" + bbResult + "]";
	var bbArrays = Ext.decode(bbResult);
	//var userName = "";
	var bbId = "";
	for (var i = 0;i < bbArrays.length; i++) {
		//根节点不被包含在内
		if(bbArrays[i].id.indexOf('ynode')==-1)
		bbId += bbArrays[i].id + ",";
	}
	
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	
	//组装
	var param = {};
	// 将权限ID放入对象
	param.flids=flId.substr(0, flId.length - 1);
	param.bbids=bbId.substr(0, bbId.length - 1);
	param.roleid=oNode.id;
	AjaxRequest_Sync(link8, param, function(req) {
		var responseText = req.responseText;
		var responseObj = Ext.decode(responseText);
		if (responseObj.success) {
			alert("保存成功");
		}
	});
}
	
//左边的树 右边的grid

var leftTree=new LeftTree();
var sjqxForm=new SjqxForm("");

//中间
var lyCenter = {
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	height:500,
	margins:'0 0 0 0',
	items:[sjqxForm]
}
//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [mainToolbar]
}


