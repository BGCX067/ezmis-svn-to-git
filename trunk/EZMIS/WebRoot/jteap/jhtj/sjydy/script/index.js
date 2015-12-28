
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
	   button.id=='btnModisjzd'||
	   button.id=='btnShowsjzd'){ 
		button.setDisabled(true);
	}
}








/**
 * 新建数据源
 */
function btnAddCatalog_Click(){
	var title="数据源维护";
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var args="url|"+link6+"?currentSysId="+oNode.id+"&isUpdate=false"+";title|"+title;
	var result=showModule(link7,"yes",700,430,args);
	if(result=="true"){
		leftTree.getRootNode().reload();
	}
}

/**
 * 修改数据源
 */
function btnModiCatalog_Click(){
	var title="数据源维护";
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var currentSysId=oNode.parentNode.id;
	var args="url|"+link6+"?currentSysId="+currentSysId+"&id="+oNode.id+"&isUpdate=true"+";title|"+title;
	var result=showModule(link7,"yes",700,430,args);
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
 * 修改字段信息
 */
function btnModisjzd_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select!=null){
		var result=showModule(link17+"?sjzdid="+select.json.id,true,400,220);
		if(result=="true"){
			alert("修改成功!");
			rightGrid.getStore().reload();
		}
	}
}


/**
 * 数据源基本信息
 */
function btnShowsjzd_Click(){
    var oNode=leftTree.getSelectionModel().getSelectedNode();
	if(oNode!=null){
		var result=showModule(link20+"?connid="+oNode.id+"&isEdit=true",true,550,400);
		if(result=="true"){
		}
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


