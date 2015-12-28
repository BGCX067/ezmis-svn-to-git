
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
	var title="数据分类维护";
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var parentId="";
	if(oNode.id.indexOf("ynode")==-1){
		parentId=oNode.id;
	}
	var result=showModule(link6+"?parentId="+parentId,"yes",600,230);
	if(result=="true"){
		leftTree.getRootNode().reload();
	}
}

/**
 * 修改分类
 */
function btnModiCatalog_Click(){
	var title="数据分类维护";
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var result=showModule(link6+"?id="+oNode.id,"yes",600,230);
	if(result=="true"){
		leftTree.getRootNode().reload();
	}
}

/**
 * 删除数据源
 */
function btnDelCatalog_Click(){
	leftTree.deleteSelectedNode();
}

/**
 * 增加字段信息
 */
function btnAddKey_Click(){
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var kid=oNode.attributes.kid;
	if(kid!=""){
		var result=showModule(link9+"?kid="+kid,true,550,280);
		if(result=="true"){
			rightGrid.getStore().reload();
		}
	}
}


/**
 * 修改字段信息
 */
function btnModiKey_Click(){
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var select=rightGrid.getSelectionModel().getSelections()[0];
	var kid=oNode.attributes.kid;
	var id=select.json.id;
	if(kid!=""&&id!=""){
		var result=showModule(link9+"?kid="+kid+"&id="+id,true,550,280);
		if(result=="true"){
			rightGrid.getStore().reload();
		}
	}
}


function btnDelKey_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select!=null){
		rightGrid.deleteSelect(select);
	}
}


function btnGhostKey_Click(){
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var kid=oNode.attributes.kid;
	if(kid!=""){
		leftTree.ghostState(kid);
	}
}
	
//左边的树 右边的grid

var leftTree=new LeftTree();
var rightGrid=new RightGrid();

//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[rightGrid,leftTree]
}
//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [mainToolbar]
}


