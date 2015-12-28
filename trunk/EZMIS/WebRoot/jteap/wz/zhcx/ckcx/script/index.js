
//工具栏  
//1.添加分类
//2.修改分类
//3.删除分类
var mainToolbar = new Ext.Toolbar({height:26,
	items:[]
});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
}

/**
 * 添加EForm表单
 */
function btnAdd_Click(){
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	
	var result=showModule(link6,true,800,645);
	if(result=="true"){
		rightGrid.getStore().reload();
	}
}
/**
 * 修改表单
 */
function btnModify_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		var url=link6+"?id="+select.json.id;
		result=showModule(url,true,800,645);
		if(result=="true"){
			rightGrid.getStore().reload();
		}
	}
	
}

/**
 * 删除表单
 */
function btnDel_Click(){
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


//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="出库时间起#dtStart#dateField,出库时间止#dtEnd#dateField,出库类别#crklb#comboCkrzlb,出库仓库#ckbh#comboCkgl,操作人#czr#Czr".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="出库时间起#dtStart#dateField,出库时间止#dtEnd#dateField,出库类别#crklb#comboCkrzlb,出库仓库#ckbh#comboCkgl,操作人#czr#Czr".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs,title:'出库日志查询',labelWidth:80});

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


