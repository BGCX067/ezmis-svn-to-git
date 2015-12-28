var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
//	items:[
//			{disabled:true,id:'btnResetFlow',text:'流程重启',cls: 'x-btn-text-icon',icon:'icon/icon_2.gif',listeners:{click:btnResetFlow_Click}},
//			{disabled:true,id:'btnWFTrace',text:'流程跟踪',cls: 'x-btn-text-icon',icon:'icon/icon_2.gif',listeners:{click:btnWFTrace_Click}},
//			{disabled:true,id:'btnWFLog',text:'流程日志',cls: 'x-btn-text-icon',icon:'icon/icon_2.gif',listeners:{click:btnWFLog_Click}},
//			{disabled:true,id:'btnOpenForm',text:'查看',cls: 'x-btn-text-icon',icon:'icon/icon_4.gif',listeners:{click:btnWFOpenForm_Click}},
//			{disabled:true,id:'btnDel',text:'删除',cls: 'x-btn-text-icon',icon:'icon/icon_4.gif',listeners:{click:btnDel_Click}}
//		]
});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnResetFlow' || button.id=='btnWFTrace' 
		|| button.id=='btnWFLog' || button.id=='btnWFOpenForm' || button.id=='btnDel')
		button.setDisabled(true);
}

/**
 * 流程日志
 */
function btnWFLog_Click(){
	//如果没有选择或者选择了多条记录
	if(rightGrid.getSelectionModel().getSelections().length != 1){
		alert("请选择一条数据!");
		return;
	}
	var select=rightGrid.getSelectionModel().getSelections()[0];
	var id=select.json.ID_;
	var url="wfLog.jsp?pid="+id
	var result=showModule(url,true,800,645);
}
/**
 * 删除流程实例
 */
function btnDel_Click(){
	//如果没有选择或者选择了多条记录
	if(rightGrid.getSelectionModel().getSelections().length < 1){
		alert("请至少选择一条数据!");
		return;
	}
	var url=contextPath+"/jteap/wfengine/wfi/WorkFlowInstanceAction!deleteProcessInstranceAction.do";
	var select=rightGrid.getSelectionModel().getSelections();
	var ids=getIds(select);
	if(window.confirm("确认删除选中的流程实例吗?")){
			var myMask = showExtMask("流程实例删除中，请稍候。。。");
			//提交数据
		 	Ext.Ajax.request({
		 		url:url,
		 		success:function(ajax){
		 			var responseText=ajax.responseText;	
		 			var responseObject=Ext.decode(responseText);
		 			
		 			if(responseObject.success){
		 				alert("删除成功");
		 				rightGrid.getStore().reload();
		 				myMask.hide();
		 			}else{
		 				alert("删除失败");
		 			}
		 		},
		 		failure:function(){
		 			alert("提交失败");
		 		},
		 		method:'POST',
		 		params:{ids:ids}
		 	})	
       }
	
}

function getIds(select){
	  var ids="";
		for(var i=0;i<select.length;i++){
		    var temp=select[i];
			ids+=temp.id+",";
		}
	return ids;
}
/**
 * 流程跟踪
 */
function btnWFTrace_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	var flowConfigId=select.json.FLOW_CONFIG_ID;
	var url=contextPath + "/jteap/wfengine/wfi/WorkFlowInstanceAction!wfTraceAction.do?flowConfigId="+flowConfigId+"&pid="+select.get("ID_");
	var result=showModule(url,true,800,700);
	if(result=="true"){
		rightGrid.getStore().reload();
	}
}

/**
 * 查看
 */
function btnWFOpenForm_Click(){
	//如果没有选择或者选择了多条记录
	if(rightGrid.getSelectionModel().getSelections().length != 1){
		alert("请选择一条数据!");
		return;
	}
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		var formId=select.json.FLOW_FORM_ID;
		//var url=contextPath+"/jteap/wfengine/wfi/WorkFlowInstanceAction!showUpdateAction.do?pid="+select.id;
		//var features="menubar=no,toolbar=no,width=800,height=600";
		//window.open(url,"_blank",features);
		
		var url = contextPath + "/jteap/ModuleWindowForm.jsp" 
		var windowUrl=contextPath+"/jteap/wfengine/wfi/WorkFlowInstanceAction!showUpdateAction.do?pid="+select.id;
		var args="url|"+windowUrl+";title|"+'查看流程';
		var retValue = showModule(url,"yes",800,600,args);
		rightGrid.getStore().reload();
	}
}

/**
 * 重启流程
 */
function btnResetFlow_Click(){
	var url=contextPath+"/jteap/wfengine/wfi/WorkFlowInstanceAction!resetFlowAction.do";
	var select=rightGrid.getSelectionModel().getSelections();
	
	if(select.length != 1){
		alert("请选择一条数据!");
		return;
	}
	//流程实例的ID
	var id=select[0].id;

	//关联表单的ID
	var formId=select[0].json.FLOW_FORM_ID;
	if(window.confirm("确认重新启动流程实例吗?")){
			var myMask = showExtMask("流程实例重启中,请稍候...");
			//提交数据
		 	Ext.Ajax.request({
		 		url:url,
		 		success:function(ajax){
		 			var responseText=ajax.responseText;	
		 			var responseObject=Ext.decode(responseText);
		 			
		 			if(responseObject.success){
		 				alert(responseObject.msg);
		 				rightGrid.getStore().reload();
		 				myMask.hide();
		 			}else{
		 				alert(responseObject.msg);
		 			}
		 		},
		 		failure:function(){
		 			alert("提交失败");
		 		},
		 		method:'POST',
		 		params:{id:id,formId:formId}
		 	})	
       }
}

	
//左边的树 右边的grid
var leftTree=new LeftTree();
var rightGrid=new RightGrid();
//用户查询面板								
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
	items:[rightGrid,searchPanel]
}

//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [mainToolbar]
}


