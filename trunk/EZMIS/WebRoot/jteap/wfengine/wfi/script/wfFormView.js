/**
 * 跟踪流程
 */
function btnWFTrace_Click(){
	var flowConfigId = $("flowConfigId").value;
	var pid = $("pid").value;
	var url=contextPath + "/jteap/wfengine/wfi/WorkFlowInstanceAction!wfTraceAction.do?flowConfigId="+flowConfigId+"&pid="+pid;
	var result=showModule(url,true,800,600);
}

/**
 * 流程日志
 */
function btnWFLog_Click(){
	var pid = $("pid").value;
	var url=contextPath+"/jteap/wfengine/wfi/wfLog.jsp?pid="+pid
	var result=showModule(url,true,600,400);
}

/**
 * 操作日志
 */
function btnXqjhsqOperLog_Click() {
	var pid = $("pid").value;
	//只有需求计划申请才有操作日志
	if($("formSn").value == "TB_WZ_XQJHSQ"){
		var url = contextPath + "/jteap/wz/wzxqjhsq/wfLog.jsp?pid=" + pid
		var result = showModule(url, true, 600, 400);
	}else{
		alert("该流程实例没有操作记录!");
	}
}

/**
 * 打印
 */
function print(){
	var formFrame = $("formFrame")
	if (formType == '02') {//cform
		if(typeof formFrame.contentWindow.saveFormInAjax == 'function'){
			var docid = formFrame.contentWindow.saveFormInAjax(false);
			$("docid").value = docid;
		}
	} else if (formType == '01') {//eform
		if (typeof formFrame.contentWindow.submitForm == 'function') {
			var docid = formFrame.contentWindow.f_SaveForm(function(docid){
				var myForm = window.parent.myForm;
				myForm.docid.value = docid;
			})
		}
	}

	formFrame.TANGER_OCX_PrintDoc();
}

/**
 * 签收
 * 对于有签收人的代办，只能看到流程跟踪和流程日志按钮
 * 对于打开代办后，被其他人抢先签收，会返回前台已被签收信息。同时其他按钮不可用
 */
function btnSignIn_Click() {
	if (window.confirm('是否要签收此表单吗?')) {
		var url = contextPath+"/jteap/wfengine/wfi/WorkFlowInstanceAction!signInAction.do";
		var param = {};
		param.pid = $('pid').value;
		param.token = $('token').value;
		param.formSn = $("formSn").value;
		//同步请求数据
		AjaxRequest_Sync(url,param,function(req){
			var responseText = req.responseText;
			var responseObj = Ext.decode(responseText);
			if(responseObj.success){
				$('token').value = '';
				$('myForm').submit();
			} else {
				alert("已被他人签收！")
			}
		});
	}
}