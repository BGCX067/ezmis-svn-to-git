
//工具栏  
//1.添加分类
//2.修改分类
//3.删除分类
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnModify' || button.id=='btnDel') 
		button.setDisabled(true);
}

/**
 * 添加EForm表单
 */
function btnAdd_Click(){
	var url=link6;
	result=showModule(url,true,583,394);
	rightGrid.getStore().reload();
}
/**
 * 修改表单
 */
function btnModify_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		var url=link6+"&docid="+select.json.id;
		result=showModule(url,true,583,394);
		rightGrid.getStore().reload();
	}
}

/**
 * 删除表单
 */
function btnDel_Click(){
	var select=rightGrid.getSelectionModel().getSelections();
    var result=rightGrid.deleteSelect(select);
    if(result!='cancle') clearMxGrid();
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

/**
 * 清空子表Grid显示
 */
function clearMxGrid(){
	var whereSql = "obj.fjhsd.id ='" + 0 +"'";
	mxGrid.store.baseParams={queryParamsSql:whereSql};
	mxGrid.store.load();
}



var rightGrid=new RightGrid();
var mxGrid = new MxRightGrid();

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

//南边
var lySouth={
	layout:'border',
	id:'south-panel',
	region:'south',
	minSize: 175,
	maxSize: 400,
	height:200,
	border:false,
	margins:'0 0 0 0',
	items:[mxGrid]
}
