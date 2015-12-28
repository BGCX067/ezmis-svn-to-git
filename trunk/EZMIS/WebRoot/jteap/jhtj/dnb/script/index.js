
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
	if(button.id=='btnModiCatalog'||button.id=='btnDelCatalog'||button.id=='btnAddsjdy'||button.id=='btnDelsjdy'){ 
		button.setDisabled(true);
	}
}
/**
 * 数据定义
 */
function btnAddsjdy_Click(){
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var result=showModule(link10+"?currentSysId="+oNode.id,true,550,400);
	if(result=="true"){
		leftTree.getRootNode().reload();
	}
}


/**
 * 删除定义
 */
function btnDelsjdy_Click(){
    leftTree.deleteChildrenSelectedNode();
}


/**
 * 删除分类
 */
function btnDelCatalog_Click(){
	leftTree.deleteSelectedNode();
}

/**
 * 添加分类
 */
function btnAddCatalog_Click(){
	var result=showModule(link6,true,550,400);
	if(result=="true"){
		leftTree.getRootNode().reload();
	}
}

/**
 * 修改分类
 */
function btnModiCatalog_Click(){
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	if(oNode!=null){
		var result=showModule(link6+"?connid="+oNode.id,true,550,400);
		if(result=="true"){
			leftTree.getRootNode().reload();
		}
	}
}


	
//左边的树 右边的grid

var leftTree=new LeftTree();

var lyNorth={
	id:'north-panel',
	region:'north',
	height:400,
	border:false,
 	html:'<iframe width="100%" height="100%" name="sjFrame" src=""></iframe>'
}

var rightGrid=new RightGrid();

var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[lyNorth,rightGrid]
}



