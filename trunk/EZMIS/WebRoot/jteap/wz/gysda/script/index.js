
//工具栏  
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnModify' || button.id=='btnDel' ) 
		button.setDisabled(true);
}

/**
 * 添加EForm表单
 */
function btnAdd_Click(){
	var result=showModule(link6,true,585,440);
	rightGrid.getStore().reload();
}

/**
 * 修改表单
 */
function btnModify_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		var url=link6+"&docid="+select.json.id;
		result=showModule(url,true,585,440);
//		if(result=="" || result==null){
			rightGrid.getStore().reload();
//		}
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
 * 统计供应商的供应额
 */
function btnMoneySum_Click(){
	window.open(link7)
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
var searchAllFs="供应商名称#gysmc#textField,供应商编码#bm#textField,电话#dh#textField,地址#dz#textField,是否协议单位#sfxydw#ck,是否市内单位#sfsndw#ck".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="供应商名称#gysmc#textField,供应商编码#bm#textField,电话#dh#textField,地址#dz#textField,是否协议单位#sfxydw#ck,是否市内单位#sfsndw#ck".split(",");
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


