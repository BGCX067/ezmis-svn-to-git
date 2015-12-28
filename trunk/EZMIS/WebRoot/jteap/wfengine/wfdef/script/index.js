var mainToolbar=new Ext.Toolbar(
	{
		height:25,
		listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
/*		items:[
			{id:'btnAddCatalog',text:'新建分类',disabled:true,cls: 'x-btn-text-icon',icon:'icon/icon_1.gif',listeners:{click:btnAddCatalog_Click}},
			{id:'btnModiCatalog',text:'修改分类',disabled:true,cls: 'x-btn-text-icon',icon:'icon/icon_3.gif',listeners:{click:btnModiCatalog_Click}},
			{id:'btnDelCatalog',text:'删除分类',disabled:true,cls: 'x-btn-text-icon',icon:'icon/icon_2.gif',listeners:{click:btnDelCatalog_Click}},
			{id:'btnAddFlow',text:'新建流程',disabled:true,cls: 'x-btn-text-icon',icon:'icon/icon_5.gif',listeners:{click:btnAddFlow_Click}},
			{id:'btnDelFlow',text:'删除流程',tooltip:'删除该流程的所有版本',disabled:true,cls: 'x-btn-text-icon',icon:'icon/icon_6.gif',listeners:{click:btnDelFlow_Click}},
			{id:'btnBackFlow',text:'版本回退',tooltip:'流程版本回退到上一个版本',disabled:true,cls: 'x-btn-text-icon',icon:'icon/icon_16.png',listeners:{click:btnBackFlow_Click}},
			{id:'btnIssueFlow',text:'发布流程',disabled:true,cls: 'x-btn-text-icon',icon:'icon/icon_7.gif',listeners:{click:btnIssueFlow_Click}},
			{id:'btnDraftFlow',text:'起草流程',disabled:true,cls: 'x-btn-text-icon',icon:'icon/icon_10.gif',listeners:{click:btnDraftFlow_Click}},
			{id:'btnModiFlow',text:'修改流程',disabled:true,cls: 'x-btn-text-icon',icon:'icon/icon_11.gif',listeners:{click:btnModiFlow_Click}},
			{id:'btnImportFlow',text:'导入流程',disabled:false,cls: 'x-btn-text-icon',icon:'icon/icon_12.gif',listeners:{click:btnImportFlow_Click}},
			{id:'btnExportFlow',text:'导出流程',disabled:true,cls: 'x-btn-text-icon',icon:'icon/icon_13.gif',listeners:{click:btnExportFlow_Click}},
			{id:'btnValidateFlow',text:'验证流程',disabled:true,cls: 'x-btn-text-icon',icon:'icon/icon_15.gif',listeners:{click:btnValidateFlow_Click}}
			{id:'btnCopyFlow',text:'复制流程',disabled:true,cls: 'x-btn-text-icon',icon:'icon/icon_17.gif',listeners:{click:btnCopyFlow_Click}}
		]
*/
	}
);

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnDelFlow' || button.id=='btnBackFlow'|| button.id=='btnIssueFlow' 
		|| button.id=='btnDraftFlow' || button.id=='btnModiFlow' || button.id=='btnExportFlow'
		|| button.id=='btnValidateFlow' || button.id=='btnCopyFlow') 
		button.setDisabled(true);
}

//添加流程目录
function btnAddCatalog_Click() {
	workFlowCatalogTree.createNode();
}

//修改流程目录
function btnModiCatalog_Click() {
	workFlowCatalogTree.modifyNode();
}

//删除流程目录
function btnDelCatalog_Click() {
	workFlowCatalogTree.deleteSelectedNode() ;
}

/**
 * 回退操作
 */
function btnBackFlow_Click(){
	var item = workFlowGrid.getSelectionModel().getSelections()[0];
	
	if(item.get("version")<=1){
		alert("已经是最后一个版本，无法进行回退操作");
		return;
	}
	if(!confirm("是否进行回退版本操作")){
		return ; 
	}
	
	var myMask = showExtMask('正在回退流程,请稍候...');
	
	var id=item.id;
	Ext.Ajax.request(
		{
			url : link8,
			params : 'ids='+id, 
			method : 'POST' ,
			success : function(ajax) {
				var responseText=ajax.responseText;	
		 		var responseObject=Ext.util.JSON.decode(responseText);
		 		if(responseObject.success){
		 			alert("回退成功");
		 			workFlowGrid.getStore().reload();
		 		}else{
		 			alert(responseObject.msg);
		 		}
				myMask.hide();		
			}
		}
	)
}
//添加流程
function btnAddFlow_Click() {
	if(workFlowCatalogTree.getSelectionModel().getSelectedNode()==null) {
		return ;
	}
	if(workFlowCatalogTree.getSelectionModel().getSelectedNode()==workFlowCatalogTree.root){
		return ;
	}
	var id = workFlowCatalogTree.getSelectionModel().getSelectedNode().attributes.id ;
	var text = workFlowCatalogTree.getSelectionModel().getSelectedNode().attributes.text ;
	var url="addFlow.jsp" ;
	var result=showModule(url,true,800,600,"id|"+id+";name|"+text) ;
	if(result) {
		setTimeout(function(){
					workFlowGrid.getStore().reload();
			    }, 250);
	}
	
}

//删除流程
function btnDelFlow_Click() {
	if(!confirm("当进行删除操作后,流程的所有版本将会被删除。是否进行删除操作?")){
		return ; 
	}
	var items = workFlowGrid.getSelectionModel().getSelections();
	var id = "" ;
	for(var i=0 ; i<items.length ; i++) {
		id += items[i].id + ",";
	}
	id = id.substring(0 , id.length - 1 ) ;
	
	var myMask = showExtMask('流程正在删除,请稍候...');
	
	Ext.Ajax.request(
		{
			url : link5,
			params : 'ids='+id, 
			method : 'POST' ,
			success : function(ajax) {
				var responseText=ajax.responseText;	
		 		var responseObject=Ext.util.JSON.decode(responseText);
	 			alert("删除成功");
	 			workFlowGrid.getStore().reload();
		 		myMask.hide();
			}
		}
	)
}

//发布流程
function btnIssueFlow_Click() {
	var select=workFlowGrid.getSelectionModel().getSelections()[0];
	var pd_id=select.json.pd_id;
	/*if(pd_id!=null && pd_id>0){
		alert('该流程已经发布,不能重复发布已发布的流程');
		return;
	}*/
	
	var myMask = showExtMask('流程发布中，请稍候...');

	//提交数据
 	Ext.Ajax.request({
 		url:link7+"?flowConfigId="+select.json.id,
 		success:function(ajax){
 			var responseText=ajax.responseText;	
 			var responseObject=Ext.util.JSON.decode(responseText);
 			if(responseObject.success){
 				alert("流程发布成功");
 				workFlowGrid.getStore().reload();
 			}else
 				alert("流程发布失败："+responseObject.msg);
 			
 			myMask.hide();
 			
 		},
 		failure:function(){
 			alert("提交失败");
 		},
 		method:'POST',
 		params:{}
 	})
}

//修改流程
function btnModiFlow_Click() {
	if(workFlowGrid.getSelectionModel().getSelections().length != 1) {
		return ;
	}
	if(workFlowCatalogTree.getSelectionModel().getSelectedNode()==null) {
		return ;
	}
	var id = workFlowCatalogTree.getSelectionModel().getSelectedNode().attributes.id ;
	var text = workFlowCatalogTree.getSelectionModel().getSelectedNode().attributes.text ;
	var url="addFlow.jsp" ;
	var result=showModule(url,true,800,600,"flowId|"+workFlowGrid.getSelectionModel().getSelected().id) ;
	if(result){
		setTimeout(function(){
				workFlowGrid.getStore().reload();
		    }, 250);
	}
}

//起草流程
function btnDraftFlow_Click() {
	var url=contextPath+"/jteap/wfengine/wfi/WorkFlowInstanceAction!showUpdateAction.do";
	var select=workFlowGrid.getSelectionModel().getSelections()[0];
	if(select){
		var pdid = select.get("pd_id");
		if(pdid == null || pdid<=0){
			alert("该流程还没有发布,请先发布");
			return;
		}
		var url=url+"?flowConfigId="+select.id;
		var title="起草流程";
		var args="url|"+url+";title|"+title;
		var result=showModule(contextPath+"/jteap/ModuleWindowForm.jsp","yes",800,600,args);
	}
}

var isImport;
//导入流程
function btnImportFlow_Click() {
	isImport = false;
	var url = contextPath+'/jteap/wfengine/wfdef/upload.jsp';
	var arg = {
		title:'JTEAP-WF导入流程',
		url:url
	};
	showModule2(contextPath+"/jteap/ModuleWindowForm.jsp",true,300,100,arg);
	if(isImport){
		workFlowGrid.getStore().reload();
	}
}

//导出流程
function btnExportFlow_Click() {
	if(workFlowGrid.getSelectionModel().getSelections().length != 1) {
		return ;
	}
	var id = workFlowGrid.getSelectionModel().getSelections()[0].id ;
	var path = link6 +"?id="+id ;
	window.open(path)
}

//验证流程
function btnValidateFlow_Click() {
	if(workFlowGrid.getSelectionModel().getSelections().length != 1) {
		return ;
	}

	var myMask = showExtMask('正在验证流程,请稍候...');
	
	var id = workFlowGrid.getSelectionModel().getSelections()[0].id ;
	
	Ext.Ajax.request({
		url : link9,
		params : {
			"id" : id
		},
		success : function(response, options) {
			myMask.hide();
			var responseText=response.responseText;	
 			var responseObject=Ext.util.JSON.decode(responseText);
 			if(responseObject.success){
 				alert(responseObject.msg);
 				workFlowGrid.getStore().reload();
 			}else
 				alert(responseObject.msg);
		},
		scope : this,
		failure : function(response, options) {
			myMask.hide();
			alert(responseObject.msg);
		}
	});
	
}

function btnCopyFlow_Click(){
	if(workFlowGrid.getSelectionModel().getSelections().length != 1) {
		return ;
	}

	var myMask = showExtMask('正在复制流程,请稍候...');
		
	var id = workFlowGrid.getSelectionModel().getSelections()[0].id ;
	
	Ext.Ajax.request({
		url : link10,
		params : {
			"id" : id
		},
		success : function(response, options) {
			myMask.hide();
			var responseText=response.responseText;	
 			var responseObject=Ext.util.JSON.decode(responseText);
 			if(responseObject.success){
 				alert(responseObject.msg);
 				workFlowGrid.getStore().reload();
 			}else
 				alert(responseObject.msg);
		},
		scope : this,
		failure : function(response, options) {
			myMask.hide();
			var responseText=response.responseText;	
 			var responseObject=Ext.util.JSON.decode(responseText);
			alert(responseObject.msg);
		}
	});
}


//左边的树 右边的grid
var workFlowCatalogTree=new WorkFlowCatalogTree();
var workFlowGrid=new WorkFlowGrid();
			

//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	items:[workFlowGrid]
}

//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [mainToolbar]
}
