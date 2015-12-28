//工具栏
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnModiCatalog' || button.id=='btnDelCatalog' || button.id=='btnAddCatalog'
	   || button.id=='btnAdd' || button.id=='btnModify' || button.id=='btnDel')
		button.setDisabled(true);
}

/**
 * 添加基础台帐
 */
function btnAdd_Click(){
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var obj = {};
	//设备台帐分类Id
	obj.sbtzCatalogId = oNode.id;
	alert(obj.sbtzCatalogId);
	var url = contextPath + '/jteap/sb/sbtzgl/jctz/jctzinfo.jsp?sbtzCatalogId='+obj.sbtzCatalogId;
	showIFModule(url,"基础台帐信息","true",500,360,obj);
	rightGrid.getStore().reload();
		
}

/**
 * 修改基础台帐
 */
function btnModify_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		var obj = {};
		//设备台帐分类Id
		obj.id = select.data.id;
		obj.kks = select.data.kks;
		obj.sbbm = select.data.sbbm;
		obj.ybmc = select.data.ybmc;
		obj.xsjgf = select.data.xsjgf;
		obj.dw = select.data.dw;
		obj.sl = select.data.sl;
		obj.azdd = select.data.azdd;
		obj.xtth = select.data.xtth;
		obj.yt = select.data.yt;
		obj.remark = select.data.remark;
		obj.sbtzCatalogId = select.data.sbtzCatalog.id;
		var url = contextPath + '/jteap/sb/sbtzgl/jctz/jctzinfo.jsp' + "?modi=m&sbtzCatalogId="+obj.sbtzCatalogId;
		showIFModule(url,"基础台帐信息","true",500,360,obj);
		rightGrid.getStore().reload();
	}
	
}

/**
 * 删除基础台帐
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

/**
 * 运行台帐
 */
function btnYxtz_Click(){
	var selections = rightGrid.getSelections();//获取被选中的行
	if(selections == null || selections == ""){
		alert("请选择一条记录!");
		return;
	}
	var obj = {};
	//设备台帐分类Id
	obj.sbbm = selections[0].data.sbbm;
	var url = contextPath + '/jteap/sb/sbtzgl/yxtz/index.jsp?sbbm='+obj.sbbm;
	showIFModule(url,"运行台帐信息","true",800,600,obj);
}

var leftTree=new LeftTree();
//设备台帐查询面板
//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="设备编码#sbbm#textField,设备名称#ybmc#textField,安装位置#azdd#textField,型式及规范#xsjgf#textField,系统图号#xtth#textField".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="设备编码#sbbm#textField,设备名称#ybmc#textField,安装位置#azdd#textField,型式及规范#xsjgf#textField,系统图号#xtth#textField".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs,labelWidth:70});


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
