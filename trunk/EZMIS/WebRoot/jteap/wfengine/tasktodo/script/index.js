var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
//	items:[
//			{disabled:true,id:'btnWFTrace',text:'流程跟踪',cls: 'x-btn-text-icon',icon:'icon/icon_2.gif',listeners:{click:btnWFTrace_Click}},
//			{disabled:true,id:'btnWFLog',text:'流程日志',cls: 'x-btn-text-icon',icon:'icon/icon_2.gif',listeners:{click:btnWFLog_Click}},
//			{disabled:true,id:'btnWFOpenForm',text:'查看',cls: 'x-btn-text-icon',icon:'icon/icon_4.gif',listeners:{click:btnWFOpenForm_Click}}
//		]
});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnWFTrace' || button.id=='btnWFLog' 
		|| button.id=='btnWFOpenForm') 
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
	var id=select.json.flowInstance;;
	var url=contextPath + "/jteap/wfengine/wfi/wfLog.jsp?pid="+id
	var result=showModule(url,true,800,645);
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
	//如果没有选择或者选择了多条记录
	if(rightGrid.getSelectionModel().getSelections().length != 1){
		alert("请选择一条数据!");
		return;
	}
	var select=rightGrid.getSelectionModel().getSelections()[0];
	var flowInstanceId=select.json.flowInstance;
	var flowConfigId = select.json.flowConfig.id;
	var url=contextPath + "/jteap/wfengine/wfi/WorkFlowInstanceAction!wfTraceAction.do?flowConfigId="+flowConfigId+"&pid="+flowInstanceId;
	var result=showModule(url,true,800,645);
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
		var id=select.json.id;
		var formId=select.json.flowInstance;
		var token = select.json.token;

		// 弹出流程查看窗口
		var url = contextPath + "/jteap/ModuleWindowForm.jsp" 
		var windowUrl=contextPath+"/jteap/wfengine/wfi/WorkFlowInstanceAction!showViewAction.do?pid="+formId + "&id=" + id + "&token=" + token+"&isEdit=true";;
		var args="url|"+windowUrl+";title|"+'查看流程';
		var retValue = showModule(url,"yes",800,600,args);
		
		// 进行释放签收操作
		Ext.Ajax.request({
			url : link2,
			method : 'POST',
			params : {
				pid : formId,
				token : token
			},
			success : function(ajax) {
				var responseText = ajax.responseText;
				var obj = Ext.decode(responseText);
				if (obj.success) {
					rightGrid.getStore().reload();
				} else {
					alert("数据库操作异常，请联系管理员！");
				}
			},
			failure : function() {
				alert("数据库操作异常，请联系管理员！");
			}
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


