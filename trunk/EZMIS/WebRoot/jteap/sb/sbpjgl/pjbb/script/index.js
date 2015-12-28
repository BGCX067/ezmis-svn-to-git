var queryParams = "";

//工具栏
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnModiCatalog' || button.id=='btnDelCatalog' || button.id=='btnAddCatalog'
	   || button.id=='btnPj' || button.id=='btnAddPj' || button.id=='btnDelPj')
		button.setDisabled(true);
}

/**
 * 添加评级设备
 */
function btnAddPj_Click(){
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var obj = {};
	//设备评级分类Id
	obj.sbpjCatalogId = oNode.id;
	obj.pjfl = oNode.text;
	var url = contextPath + "/jteap/sb/sbpjgl/pjxx/pjxxinfo.jsp?sbpjCatalogId="+obj.sbpjCatalogId+"&type=addPj";
	showIFModule(url,"设备评级信息","true",700,300,obj);
	rightGrid.getStore().reload();
}

/**
 * 评级
 */
function btnPj_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		var obj = {};
		//设备台帐分类Id
		obj.id = select.data.id;
		obj.pjfl = select.data.pjfl;
		obj.sbbm = select.data.sbbm;
		obj.sbmc = select.data.sbmc;
		obj.sbgg = select.data.sbgg;
		obj.scpjrq = select.data.scpjrq;
		obj.scpjjb = select.data.scpjjb;
		obj.scpjr = select.data.scpjr;
		obj.bcpjrq = select.data.bcpjrq;
		obj.bcpjjb = select.data.bcpjjb;
		obj.bcpjr = select.data.bcpjr;
		obj.remark = select.data.remark;
		obj.sbpjCatalogId = select.data.sbpjCatalog.id;
		var url = contextPath + '/jteap/sb/sbpjgl/pjxx/pjxxinfo.jsp' + "?modi=m&sbpjCatalogId="+obj.sbpjCatalogId+"&type=pj";
		showIFModule(url,"设备评级信息","true",700,300,obj);
		rightGrid.getStore().reload();
	}
	
}

/**
 * 删除评级设备
 */
function btnDelPj_Click(){
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

//左边的树 
var leftTree=new LeftTree();

//评级查询面板
//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="评级分类#pjfl#comboBox,开始日期#startDate#dateFieldShowTime,结束日期#endDate#dateFieldShowTime".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="评级分类#pjfl#comboBox,开始日期#startDate#dateFieldShowTime,结束日期#endDate#dateFieldShowTime".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});

//右边的grid
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

/**
 * 点击导出按钮 path是Ajax请求的路径 cm是所要导出的GridPanel的ColumnModel,用来动态的获取header
 * flag标志GridPanel中是否有checkbox
 */
// exportExcel = function(path,cm,flag) {
/**
 * 点击导出按钮 obj是要导出表的GridPanel对象（有一约定：obj的exportLink属性）
 * flag标志GridPanel中是否有checkbox
 */
exportExcel = function(obj, flag) {
	var path = obj.store.proxy.url;
	var paths = path.split("?")
	var temp1 = paths[0]
	var temp3 = paths[1]
	temp1 = temp1.split("!")[0]
	path = contextPath + "/jteap/sb/sbpjgl/SbpjAction!exportExcelForSbbg.do?" + temp3
	var cm = obj.getColumnModel();
	var sum = cm.getColumnCount();
	var j = 1;
	if (!flag) {
		j = 0
	}
	var paraHeader = "";
	var paraDataIndex = "";
	var paraWidth = ""
	for (var i = j;i < sum; i++) {
		if(!cm.isHidden(i)){
			paraHeader += cm.getColumnHeader(i) + ","
			paraDataIndex += cm.getDataIndex(i) + ","
			paraWidth += cm.getColumnWidth(i) + ","
		}
	}
	paraHeader = paraHeader.substr(0, paraHeader.length - 1)
	paraDataIndex = paraDataIndex.substr(0, paraDataIndex.length - 1)
	paraWidth = paraWidth.substr(0, paraWidth.length - 1)
	path = path + "&paraHeader=" + encodeURIComponent(paraHeader)
			+ "&paraDataIndex=" + encodeURIComponent(paraDataIndex)
			+ "&paraWidth=" + encodeURIComponent(paraWidth);
	// alert(path);
	window.open(path)
}
