
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
		button.id=='btnDelCatalog'){ 
		button.setDisabled(true);
	}
}
/**
 * 添加EForm表单
 */
function btnAddCatalog_Click(){
	var title="报表数据源维护";
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var args="url|"+link10+"?bbindexid="+oNode.id+"&isUpdate=false"+";title|"+title;
	var result=showModule(link9,"yes",700,430,args);
	if(result=="true"){
		bbIOGrid.getStore().reload();
	}
}
/**
 * 修改表单
 */
function btnModiCatalog_Click(){
	var title="报表数据源维护";
	var select=bbIOGrid.getSelectionModel().getSelections()[0];
	if(select){
		var args="url|"+link10+"?id="+select.json.id+"&isUpdate=true"+";title|"+title;
		var result=showModule(link9,"yes",700,430,args);
		if(result=="true"){
			bbIOGrid.getStore().reload();
		}
	}
}

/**
 * 删除表单
 */
function btnDelCatalog_Click(){
	var select=bbIOGrid.getSelectionModel().getSelections()[0];
	if(select){
		bbIOGrid.deleteSelect(select);
	}
}


	
//左边的树 右边的grid

var leftTree=new LeftTree();
var bbIOGrid=new BbIOGrid();
var bbSjzdGrid=new BbSjzdGrid();

//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[bbIOGrid,bbSjzdGrid]
}
//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [mainToolbar]
}


