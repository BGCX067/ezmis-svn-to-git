//工具栏  
//1.添加分类
//2.修改分类
//3.删除分类
//4.添加字段
//5.修改字段
//6.删除字段
var mainToolbar = new Ext.Toolbar({height:26,
	items:[
		{disabled:false,id:'btnAddDocumentLevel',text:'添加文档级别',cls: 'x-btn-text-icon',icon:'icon/icon_1.gif',listeners:{click:btnAddDocument_Click}},
		{disabled:true,id:'btnModiDocumentLevel',text:'修改文档级别',cls: 'x-btn-text-icon',icon:'icon/icon_3.gif',listeners:{click:btnModiDocument_Click}},
		{disabled:true,id:'btnDelDocumentLevel',text:'删除文档级别',cls: 'x-btn-text-icon',icon:'icon/icon_2.gif',listeners:{click:btnDelDocument_Click}}
	]
});
/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnModiDocumentLevel' || button.id=='btnDelDocumentLevel' )
		button.setDisabled(true);
}

//添加文档级别

function btnAddDocument_Click(){
	var  doclibLevelAddWindow = new DoclibLevelAddWindow();
	doclibLevelAddWindow.show();
}

//修改文档级别
function btnModiDocument_Click(){
	/*
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	*/
	var select=rightGrid.getSelectionModel().getSelections()[0];

    	if(!select){
    		Ext.Msg.alert('提示', "请选择一个文档!");
    	}else{
    		//Ext.Msg.alert('提示', "马上转向编辑页面!");
    		
    		//var doclibAddForm = new DoclibAddWindow(select.get("id"));
			//treeEditor.editNode = oNode;
    		//treeEditor.startEdit(oNode.ui.textNode);
    		
    		/*
    		var url=contextPath+"/jteap/doclib/doclibEditForm.jsp?catalogId="+oNode.id+"&doclibId="+select.get("id");
			var result=showModule(url,true,800,645);
			if(result=="true"){
				rightGrid.getStore().reload();
			}
			*/
			
			var  doclibLevelEditWindow = new DoclibLevelEditWindow(select.get("id"));
			doclibLevelEditWindow.show();
			doclibLevelEditWindow.loadData();
    	}
    
  
	

}

//删除文档级别

function btnDelDocument_Click(){
	rightGrid.deleteSelectedDoclib();
}


//左边的树 右边的grid
//var leftTree=new LeftTree();
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
