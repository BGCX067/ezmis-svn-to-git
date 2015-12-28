
//工具栏  
//1.添加分类
//2.修改分类
//3.删除分类
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
});
/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnModiCatalog' || button.id=='btnDelCatalog' 
		|| button.id=='btnAddEFormCForm' || button.id=='btnAddExcelCForm'
		|| button.id=='btnModifyCForm' || button.id=='btnDelCForm') 
		button.setDisabled(true);
}

/**
 * 添加数据权限
 */
function btnAddDataperm_Click(){
	var title="权限维护";
	var args="url|"+link7+";title|"+title;
	var result=showModule(link9,"yes",700,430,args);
	if(result=="true"){
		rightGrid.getStore().reload();
	}
}
/**
 * 修改权限
 */
function btnModiDataperm_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		var url=link7+"?id="+select.json.id+"&tablename="+select.json.tablename;
		var title="权限维护";
		var args="url|"+url+";title|"+title;
		result=showModule(link9,"yes",700,430,args);
		if(result=="true"){
			rightGrid.getStore().reload();
		}
	}
	
}

/**
 * 删除数据权限
 */
function btnDelDataperm_Click(){
	var select=rightGrid.getSelectionModel().getSelections();
    rightGrid.deleteSelect(select);
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
	leftTree.createNode();
}

/**
 * 修改分类
 */
function btnModiCatalog_Click(){
	leftTree.modifyNode();
}





	
//左边的树 右边的grid



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


