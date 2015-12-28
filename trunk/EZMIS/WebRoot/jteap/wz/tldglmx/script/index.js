
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
	if(button.id=='btnModiCatalog' || button.id=='btnDelCatalog' 
		|| button.id=='btnAddEFormCForm' || button.id=='btnAddExcelCForm'
		|| button.id=='btnModifyCForm' || button.id=='btnDelCForm') 
		button.setDisabled(true);
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
var searchAllFs="退料单编号#tldgl.bh#textField,退料部门#tldgl.tlbm#textField,操作人#tldgl.czr#persons,物资名称#wzda.wzmc#wzda,所属仓库#wzda.kw.ckid#comboCK,验收人#tldgl.ysr#persons,退料时间起始#tlStart#dateField,退料时间终止#tlEnd#dateField,退料人#tldgl.tlr#persons,工程类别#tldgl.gclb#textField,工程项目#tldgl.gcxm#textField,财务负责人#cwfzr#persons".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="退料单编号#tldgl.bh#textField,退料部门#tldgl.tlbm#textField,操作人#tldgl.czr#persons,物资名称#wzda.wzmc#wzda,所属仓库#wzda.kw.ckid#comboCK,验收人#tldgl.ysr#persons,退料时间起始#tlStart#dateField,退料时间终止#tlEnd#dateField,退料人#tldgl.tlr#persons,工程类别#tldgl.gclb#textField,工程项目#tldgl.gcxm#textField,财务负责人#cwfzr#persons".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs,labelWidth:80});

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


