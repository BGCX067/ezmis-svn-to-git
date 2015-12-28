//工具栏  
//1.添加分类
//2.修改分类
//3.删除分类
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
/*	items:[
		{disabled:false,id:'btnAddCatalog',text:'添加分类',cls: 'x-btn-text-icon',icon:'icon/icon_1.gif',listeners:{click:btnAddCatalog_Click}},
		{disabled:true,id:'btnModiCatalog',text:'修改分类',cls: 'x-btn-text-icon',icon:'icon/icon_3.gif',listeners:{click:btnModiCatalog_Click}},
		{disabled:true,id:'btnDelCatalog',text:'删除分类',cls: 'x-btn-text-icon',icon:'icon/icon_2.gif',listeners:{click:btnDelCatalog_Click}},
		
		{disabled:true,id:'btnAddEFormCForm',text:'新建EFORM表单',cls: 'x-btn-text-icon',icon:'icon/icon_4.gif',listeners:{click:mainToolBarButtonClick}},
		{disabled:true,id:'btnAddExcelCForm',text:'新建Excel表单',cls: 'x-btn-text-icon',icon:'icon/icon_5.gif',listeners:{click:mainToolBarButtonClick}},
		{disabled:true,id:'btnModifyCForm',text:'修改表单',cls: 'x-btn-text-icon',icon:'icon/icon_6.gif',listeners:{click:mainToolBarButtonClick}},
		{disabled:true,id:'btnDelCForm',text:'删除表单',cls: 'x-btn-text-icon',icon:'icon/icon_7.gif',listeners:{click:mainToolBarButtonClick}}
		]*/
});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnModiCatalog' || button.id=='btnDelCatalog' 
		|| button.id=='btnAddEFormCForm' || button.id=='btnAddExcelCForm'
		|| button.id=='btnModifyCForm' || button.id=='btnDelCForm'
		|| button.id=='btnFinalManuscript') 
		button.setDisabled(true);
}

/**
 * 添加Excel表单
 */
function btnAddExcelCForm_Click(){
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var url=contextPath+"/jteap/form/eform/eformEdit.jsp?catalogId="+oNode.id;
	var result=showModule(url,true,800,600);
	if(result=="true"){
		rightGrid.getStore().reload();
	}
	//var features="menubar=no,toolbar=no,width=800,height=600";
	//window.open(url,"_blank",features);
	
}
/**
 * 修改表单
 */
function btnModifyCForm_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		var url=contextPath+"/jteap/form/eform/eformEdit.jsp?id="+select.json.id+"&catalogId="+select.json.catalogId;
		result=showModule(url,true,800,600);
		//var features="menubar=no,toolbar=no,width=800,height=600";
		//window.open(url,"_blank",features);
		if(result=="true"){
			rightGrid.getStore().reload();
		}
	}
	
}

/**
 * 删除表单
 */
function btnDelCForm_Click(){
	var select=rightGrid.getSelectionModel().getSelections();
    rightGrid.delForm(select);
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
 * 定稿按钮
 */
function btnFinalManuscript_Click(){
	var select=rightGrid.getSelectionModel().getSelections();
	if(select.length < 1){
		alert('请选择需要定稿的表单');
		return;
	}
	
	var id = select[0].id;
	Ext.Ajax.request({
		url: link6,
		method: 'post',
		params: {id:id},
		success: function(ajax){
			eval("responseObj="+ajax.responseText);
			if(responseObj.success == true){
				alert('定稿成功');
				rightGrid.store.reload();
			}
		},
		failure: function(){
			alert('服务器忙,请稍后再试...');
		}
	});
}


	
	
//左边的树 右边的grid
var leftTree=new LeftTree();
var rightGrid=new RightGrid();
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


