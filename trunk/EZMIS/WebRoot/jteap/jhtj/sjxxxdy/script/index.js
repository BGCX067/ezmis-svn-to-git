
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
	   button.id=='btnImpCatalog'){
		button.setDisabled(true);
	}
}

/**
 * 增加分类
 */
function btnAddCatalog_Click(){
	var title="数据信息项定义向导";
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var kid=oNode.attributes.kid;
	var args="url|"+link6+"?kid="+kid+";title|"+title;
	var result=showModule(link11,"yes",720,430,args);
	if(result=="true"){
		var url=link4+"?kid="+kid;
    	rightGrid.changeToListDS(url);
    	rightGrid.getStore().reload();
	}
}

/**
 * 修改分类
 */
function btnModiCatalog_Click(){
	var title="数据信息项定义向导";
	var select=rightGrid.getSelectionModel().getSelections()[0];
	var args="url|"+link6+"?id="+select.json.id+";title|"+title;
	var result=showModule(link11,"yes",720,430,args);
	if(result=="true"){
		var oNode=leftTree.getSelectionModel().getSelectedNode();
		var kid=oNode.attributes.kid;
		var url=link4+"?kid="+kid;
    	rightGrid.changeToListDS(url);
    	rightGrid.getStore().reload();
	}
}

/**
 * 删除数据源
 */
function btnDelCatalog_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var kid=oNode.attributes.kid;
	rightGrid.deleteSelect(select,kid);
}


function btnImpCatalog_Click(){
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var kid=oNode.attributes.kid;
	var result=showModule(link20+"?kid="+kid,"yes",400,350);
	if(result=="true"){
		alert("导入成功!");
		var url=link4+"?kid="+kid;
    	rightGrid.changeToListDS(url);
    	rightGrid.getStore().reload();
	}
}

	
//左边的树 右边的grid

var leftTree=new LeftTree();
var rightGrid=new RightGrid();

//用户查询面板								
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});

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
//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [mainToolbar]
}


