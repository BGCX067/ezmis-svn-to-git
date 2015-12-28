
//工具栏  
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnModiTable' || button.id=='btnDelTable' || button.id=='btnRebuildTable'
		|| button.id=='btnAddColumn'|| button.id=='btnModiColumn'||button.id=='btnDelColumn') 
		button.setDisabled(true);
}

function initButtons(){
	mainToolbar.items.get('btnModiTable').setDisabled(true);
	mainToolbar.items.get('btnDelTable').setDisabled(true);
	mainToolbar.items.get('btnRebuildTable').setDisabled(true);
	
	mainToolbar.items.get('btnAddColumn').setDisabled(true);
	mainToolbar.items.get('btnModiColumn').setDisabled(true);
	mainToolbar.items.get('btnDelColumn').setDisabled(true);
}

/**
 * 添加表
 */
function btnAddTable_Click(){
	var obj = {};
	obj.rootNode = leftTree.getRootNode();
	
	var url = contextPath + '/jteap/yx/fdzbfxykh/set/tableInfoSet.jsp';
	showIFModule(url,"添加指标表定义","true",400,220,obj);
	leftTree.getRootNode().reload();
	initButtons();
}
/**
 * 修改表
 */
function btnModiTable_Click(){
	var oNode = leftTree.getSelectionModel().getSelectedNode();
	var obj = {};
	obj.rootNode = leftTree.getRootNode();
	obj.oNode = oNode;
	
	var url = contextPath + '/jteap/yx/fdzbfxykh/set/tableInfoSet.jsp';
	showIFModule(url,"修改指标表定义","true",400,220,obj);
	leftTree.getRootNode().reload();
	initButtons();
}
/**
 * 删除表
 */
function btnDelTable_Click(){
	leftTree.delTable();
	initButtons();
}
/**
 * 同步物理表
 */
function btnRebuildTable_Click(){
	var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"物理表同步中,请稍等..."}); 
	myMask.show();
	
	var oNode = leftTree.getSelectionModel().getSelectedNode();
	//判断是否已经存在表定义对象
 	Ext.Ajax.request({
 		url: link7+"?directiveId="+oNode.id,
 		success: function(ajax){
 			var responseText = ajax.responseText;	
 			var responseObject = Ext.util.JSON.decode(responseText);
 			if(responseObject.success){
 				alert("物理表同步成功");
 			}else{
 				alert("物理表同步失败");
 				
 			}
 			myMask.hide();
 		},
 		failure:function(){
 			alert("连接超时,同步失败");
 			myMask.hide();
 		},
 		method:'POST'
 	})
}	

/**
 * 新增字段
 */
function btnAddColumn_Click(){
	var oNode = leftTree.getSelectionModel().getSelectedNode();
	if(oNode == null){
		return;
	}
	
	var obj = {};
	obj.directiveId = oNode.id;
	obj.grid = columnGrid;
	
	var url = contextPath + '/jteap/yx/fdzbfxykh/set/columnInfoSet.jsp';
	showIFModule(url,"添加小指标","true",400,320,obj);
}

/**
 * 修改字段
 */
function btnModiColumn_Click(){
	var select = columnGrid.getSelectionModel().getSelections()[0];
    var oNode = leftTree.getSelectionModel().getSelectedNode();
    if(oNode == null){
		return;
	}
    
	var obj = {};
	obj.directiveId = oNode.id;
	obj.grid = columnGrid;
	obj.select = select;
	
	var url = contextPath + '/jteap/yx/fdzbfxykh/set/columnInfoSet.jsp?modi=m';
	showIFModule(url,"修改小指标","true",400,320,obj);
}

/**
 * 删除字段
 */
function btnDelColumn_Click(){
	var select = columnGrid.getSelectionModel().getSelections();
    columnGrid.deleteSelect(select);
}

var columnGrid=new ColumnGrid();

//左边的树
var leftTree=new LeftTree();

//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="指标名称#directiveName#textField,取数编码#dataTable#textField".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="指标名称#directiveName#textField,取数编码#dataTable#textField".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});

//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[searchPanel,columnGrid]
}

//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [mainToolbar]
}
