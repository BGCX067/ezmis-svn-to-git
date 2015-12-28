
//工具栏
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnModiCatalog' || button.id=='btnDelCatalog'
	   || button.id=='btnAdd' || button.id=='btnModi' || button.id=='btnDel')
		button.setDisabled(true);
}

/**
 * 添加定期工作
 */
function btnAdd_Click(){
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var obj = {};
	//运行日志分类Id
	obj.logCatalogId = oNode.id;
	obj.logCatalogName = oNode.text;
	
	var url = contextPath + '/jteap/yx/runlog/set/tableInfo.jsp';
	showIFModule(url,"添加报表信息","true",850,650,obj);
	rightGrid.getStore().reload();
	
}

/**
 * 修改定期工作
 */
function btnModi_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		var obj = {};
		//运行日志分类Id
		obj.id = select.id;
		obj.logCatalogId = select.data.logCatalogId;
		obj.logCatalogName = select.data.logCatalogName;
		obj.tableName = select.data.tableName;
		obj.tableCode = select.data.tableCode;
		obj.caiyangdian = select.data.caiyangdian;
		obj.sortno = select.data.sortno;
		obj.remark = select.data.remark;
		var url = contextPath + '/jteap/yx/runlog/set/tableInfo.jsp';
		showIFModule(url,"修改报表信息","true",850,650,obj);
		rightGrid.getStore().reload();
	}
	
}

/**
 * 删除定期工作
 */
function btnDel_Click(){
	var select=rightGrid.getSelectionModel().getSelections();
    rightGrid.deleteSelect(select);
}

/**
 * 添加分类
 */
function btnAddCatalog_Click(){
	leftTree.createNode();
}

/**
 * 删除分类
 */
function btnDelCatalog_Click(){
	leftTree.deleteSelectedNode();
}

/**
 * 修改分类
 */
function btnModiCatalog_Click(){
	leftTree.modifyNode();
}

var leftTree=new LeftTree();
//左边的树 右边的grid

//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="表名#tableName#textField";
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="表名#tableName#textField";
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});

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
