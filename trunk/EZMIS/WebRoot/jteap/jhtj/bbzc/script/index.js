
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
		button.id=='btnAddBb'||
		button.id=='btnModiBb'||
		button.id=='btnDelBb'){ 
		button.setDisabled(true);
	}
}
/**
 * 添加EForm表单
 */
function btnAddCatalog_Click(){
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var parentId="";
 	if(!oNode.isRoot){
 		parentId=oNode.id;
 	}
	var result=showModule(link6+"?parentId="+parentId,true,400,180);
	if(result=="true"){
		alert("增加分类成功!");
		leftTree.getRootNode().reload();
	}
}
/**
 * 修改表单
 */
function btnModiCatalog_Click(){
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	if(oNode!=null&&!oNode.isRootNode()){
		var url=link6+"?id="+oNode.id;
		result=showModule(url,true,400,180);
		if(result=="true"){
			alert("修改分类成功!");
			leftTree.getRootNode().reload();
		}
	}
}

/**
 * 删除表单
 */
function btnDelCatalog_Click(){
	leftTree.deleteSelectedNode();
}

/**
 * 添加分类
 */
function btnAddBb_Click(){
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var result=showModule(link9+"?flid="+oNode.id,true,400,250);
	if(result=="true"){
		alert("增加报表成功!");
		rightGrid.getStore().reload();
	}
}

/**
 * 修改分类
 */
function btnModiBb_Click(){
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		var result=showModule(link9+"?id="+select.json.id+"&flid="+oNode.id,true,400,250);
		if(result=="true"){
			alert("修改报表成功!");
			rightGrid.getStore().reload();
		}
	}
}

function btnDelBb_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		rightGrid.deleteSelect(select);
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
	items:[rightGrid]
}
//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [mainToolbar]
}


