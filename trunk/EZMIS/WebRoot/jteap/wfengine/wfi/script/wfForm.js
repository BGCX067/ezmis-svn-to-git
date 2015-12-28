/**
 * 初始化
 */
function onload() {
	var toolbarHeight = $('toolbar').style.height
	toolbarHeight = toolbarHeight.substring(0, toolbarHeight.indexOf("px"));
	var height = formFrameHeight - toolbarHeight-22;
	$('formFrame').height = height;
	$("formFrame").src = url;

	// 设置可编辑域
	if ($("nodeVarJson").value != '') {
		editableInputs = eval($("nodeVarJson").value);
	}

	// 根据JSON字符串来决定哪个按钮显示
	var showbutton = document.getElementById("showbutton").value;
	// 如果流程已经结束,就默认显示下面几个按钮
	if (showbutton == null || showbutton == "") {
		showbutton = "[{name:'btnTrace',mark:'流程跟踪',show:'显示'},{name:'btnLog',mark:'流程日志',show:'显示'},{name:'btnOperLog',mark:'操作日志',show:'显示'},{name:'btnPrint',mark:'打印',show:'显示'},{name:'btnExit',mark:'退出',show:'显示'}]";
	}
	
	var objArr = new Array();
	objArr = Ext.decode(showbutton);
	for (var i = 0;i < objArr.length; i++) {
		var name = objArr[i].name;
		var mark = objArr[i].mark;
		var show = objArr[i].show;
		if (show == "显示") {
			// 显示该元素
			document.getElementById(name).style.display = '';
		} else if (show == "不显示") {
			// 隐藏该元素
			document.getElementById(name).style.display = 'none';
		}
		// 设置该元素显示的值
		document.getElementById(name).value = mark;
	}
}

/**
 * 保存
 */
function save() {
	// 先保存表单，再保存流程，保存表单采取Ajax方式同步提交自定义表单,并返回业务数据的id
	var formFrame = $("formFrame")
	// cform
	if (formType == '02') {
		if (typeof formFrame.contentWindow.saveFormInAjax == 'function') {
			// 保存cform表单
			var docid = formFrame.contentWindow.saveFormInAjax(false);
			$("docid").value = docid;
			$("recordJson").value = formFrame.contentWindow.myForm.recordJson.value;
		}
		// 保存流程
		$("myForm").submit();
		// eform
	} else if (formType == '01') {
		if (typeof formFrame.contentWindow.submitForm == 'function') {
			// 保存eform表单
			var docid = formFrame.contentWindow.f_SaveForm(function(docid) {
				var myForm = window.parent.myForm;
				myForm.docid.value = docid;
				// 保存流程
					myForm.submit();
				}, true)
		}
	}
}

/**
 * 处理跳转到指定环节
 * 
 * @perms 权限对象 [
 *        {"username":"邹全发","node":"部门经理审批","processKind":"single","actors":"0845","isOneProcessActor":null,"processMode":"1","isChooseActor":false},
 *        {"username":"邹全发","node":"总经理审批","processKind":"single","actors":"0845","isOneProcessActor":null,"processMode":"1","isChooseActor":false} ]
 */
function processGoSelect(perms) {
	var url = contextPath + "/jteap/wfengine/wfi/wfGoSelectForm.jsp";
	var arg = {};
	arg.perms = perms;

	var retVal = showModule2(url, true, 450, 400, arg);

	var node = "";
	var actors = "";

	if (retVal == null) {
		return;
	} else {
		// 指定环节名
		node = retVal.node;
		// 指定环节的处理人
		actors = retVal.actors;
	}

	// 跳转到指定环节
	var actionUrl = contextPath + "/jteap/wfengine/wfi/WorkFlowInstanceAction!goToSelectedNodeAction.do?node=" + node
			+ "&actors=" + actors;
	$("myForm").action = actionUrl;

	var formFrame = $("formFrame")
	if (formType == '02') {// cform
		if (typeof formFrame.contentWindow.saveFormInAjax == 'function') {
			var docid = formFrame.contentWindow.saveFormInAjax(false);
			if (docid == "")
				return;
			$("docid").value = docid;
			$("recordJson").value = formFrame.contentWindow.myForm.recordJson.value;
		}
		$("myForm").submit();
	} else if (formType == '01') {// eform
		if (typeof formFrame.contentWindow.submitForm == 'function') {
			var docid = formFrame.contentWindow.f_SaveForm(function(docid) {
				if (docid == "")
					return;
				var myForm = window.parent.myForm;
				myForm.docid.value = docid;
				myForm.submit();
			})
		}
	}
}

/**
 * 转到下一步动作
 */
function goNextTask() {
	var docid;
	var formFrame = $("formFrame")
	// cform
	if (formType == '02') {
		// 转到下一步之前需要验证数据有效性
		if (typeof formFrame.contentWindow.Excel_Validate == 'function') {
			var valRet = formFrame.contentWindow.Excel_Validate();
			if (valRet == false)
				return;
		}
		// 保存表单
		if (typeof formFrame.contentWindow.saveFormInAjax == 'function') {
			docid = formFrame.contentWindow.saveFormInAjax(false);
			$("docid").value = docid;
			$("recordJson").value = formFrame.contentWindow.myForm.recordJson.value;
		}
		goNextTaskProcess();
		// eform
	} else if (formType == '01') {
		// //转到下一步之前需要验证数据有效性
		// if(typeof formFrame.contentWindow.Eform_Validate == 'function'){
		// var valRet = formFrame.contentWindow.Eform_Validate();
		// if(valRet == false)
		// return;
		// }
		// 保存表单
		if (typeof formFrame.contentWindow.submitForm == 'function') {
			var docid = formFrame.contentWindow.f_SaveForm(function(docid) {
				var myForm = window.parent.myForm;
				myForm.docid.value = docid;
				window.parent.goNextTaskProcess();
			})
		}
	}
}

/**
 * 跳转到下一步处理，判断是否已创建流程实例。 如果已创建则进行下一步操作；否则创建流程实例
 */
function goNextTaskProcess() {
	var pid = $("pid").value;
	var flowConfigId = $("flowConfigId").value;

	// 自定义表单验证通过之后进行下一步动作
	// 流程实例已经创建
	if (pid != null && pid != "") {
		// 得到环节权限，并进行下一步操作
		getNodePerm(flowConfigId, pid);
	} else {
		// 流程实例还没有创建
		var curNodeName = $("curNodeName").value;
		var curActors = $("curActors").value;
		var nodeVarJson = $("nodeVarJson").value;

		var url = contextPath + "/jteap/wfengine/wfi/WorkFlowInstanceAction!draftNewWorkFlowInstance.do";
		var param = {};
		param.flowConfigId = flowConfigId;
		param.docid = $("docid").value;
		param.recordJson = $("recordJson").value;
		param.curNodeName = curNodeName;
		param.curActors = curActors;
		param.pid = pid;
		param.formSn = $("formSn").value
		// 同步请求数据
		AjaxRequest_Sync(url, param, function(req) {
			var responseText = req.responseText;
			var responseObj = responseText.evalJSON();
			if (responseObj.success) {
				var resultObj = responseObj.data;
				$("pid").value = resultObj.pid;
				$("tid").value = resultObj.tid;
				$("token").value = resultObj.token;
				$("nextNodes").value = resultObj.nextNodes;
				$("nextActors").value = resultObj.nextActors;
				$("creator").value = resultObj.creator;
				$("creatdt").value = resultObj.creatdt;
				$("flowName").value = resultObj.flowName;
				$("ver").value = resultObj.ver;
				getNodePerm(flowConfigId, resultObj.pid);
			}
		});
	}
}

/**
 * 得到环节权限，并进行下一步操作
 */
function getNodePerm(flowConfigId, pid) {
	var nodesname = $("nextNodes").value;
	var nodesactor = $("nextActors").value;
	var recordJson = $("recordJson").value;
	var token = $("token").value;
	var docid = $("docid").value;
	var formSn = $("formSn").value;
	if (nodesname != "") {
		var url = contextPath + "/jteap/wfengine/wfi/WorkFlowInstanceAction!getNodePermissionAction.do";
		var param = {};
		param.flowConfigId = flowConfigId;
		param.nodesname = nodesname;
		param.nodesactor = nodesactor;
		param.pid = pid;
		param.recordJson = recordJson;
		param.token = token;
		param.preNodeName = $("preNodeName").value;
		param.docid = docid;
		param.formSn = formSn;

		// 同步请求数据
		AjaxRequest_Sync(url, param, function(req) {
			var responseText = req.responseText;
			var responseObj = Ext.decode(responseText);
			if (responseObj.success) {
				var perms = responseObj;
				if (perms.ncList.size() <= 0) {
					alert("条件不满足，无法向下进行，也无法后退");
					return;
				}
//				if(formSn == 'TB_JX_QXGL_QXD_SB'&&param.nodesname=='检修班消缺'&&param.preNodeName==''){
				if(formSn == 'TB_JX_QXGL_QXD_SB'&&param.nodesname=='检修班消缺'){
					var actionUrl = contextPath + "/jteap/wfengine/wfi/WorkFlowInstanceAction!goToNextNodeAction.do?selNodeIsBack=false&selNodeName="+nodesname+"&selActorIds="+nodesactor+"&selNextNodeType=task";
					$("myForm").action = actionUrl;
					$("myForm").submit();
				}
				else{
					processGoNext(perms);
				}
			}
		});
	} else if (nodesname == "") {
		// 如果下一环节名称是空,表示该流程已经结束
		alert("流程已经结束");
		return;
	}
}

/**
 * 处理下一步
 * 
 * @perms 权限对象 [
 *        {"username":"邹全发","node":"部门经理审批","processKind":"single","actors":"0845","isOneProcessActor":null,"processMode":"1","isChooseActor":false},
 *        {"username":"邹全发","node":"总经理审批","processKind":"single","actors":"0845","isOneProcessActor":null,"processMode":"1","isChooseActor":false} ]
 */
function processGoNext(perms) {
	if(perms.ncList[0].name == '流程结束'){
		var actionUrl = contextPath + "/jteap/wfengine/wfi/WorkFlowInstanceAction!goToNextNodeAction.do";
		createHiddenField($('myForm'), "selNodeName", '流程结束');
		createHiddenField($('myForm'), "selActorIds", '');
		createHiddenField($('myForm'), "processKind", 'single');
		createHiddenField($('myForm'), "selNextNodeType", 'EndState');
		createHiddenField($('myForm'), "selNodeIsBack", false);
		$("myForm").action = actionUrl;
		$("myForm").submit();
	}else{
		var url = "wfGoNextForm.jsp";
		var arg = {};
		arg.perms = perms;
		var retVal = showModule2(url, true, 450, 370, arg);
		var nodeName = "";
		var actorIds = "";
		var processKind = "";
	
		if (retVal == null) {
			return;
		} else {
			nodeName = retVal.nodeName;
			actorIds = retVal.actorIds;
			processKind = $("processKind").value;
		}
		var actionUrl = contextPath + "/jteap/wfengine/wfi/WorkFlowInstanceAction!goToNextNodeAction.do";
		createHiddenField($('myForm'), "selNodeName", nodeName);
		createHiddenField($('myForm'), "selActorIds", actorIds);
		createHiddenField($('myForm'), "processKind", processKind);
		createHiddenField($('myForm'), "selNextNodeType", retVal.nodeType);
		createHiddenField($('myForm'), "selNodeIsBack", retVal.isBackNode);
		$("myForm").action = actionUrl;
		$("myForm").submit();
	}
	
}

function goBackTask() {
	// 填写回退原因
	var url = "wfGoBackReason.jsp";
	var result = showModule(url, true, 400, 250);
	if (result != null) {
		$('backReason').value = result;
		var actionUrl = contextPath + "/jteap/wfengine/wfi/WorkFlowInstanceAction!goBackNodeAction.do";
		$("myForm").action = actionUrl;

		var formFrame = $("formFrame")
		if (formType == '02') {// cform
			$("recordJson").value = formFrame.contentWindow.myForm.recordJson.value;
		}
		$("myForm").submit();
	}
}

/**
 * 转到指定的环节
 */
function btnSelectTask_Click() {
	var token = $("token").value;
	var flowConfigId = $("flowConfigId").value;
	var pid = $("pid").value;
	if (pid == "" || $("nextNodes").value == "") {
		alert("无法转到指定的环节!")
		return;
	}
	var url = contextPath + "/jteap/wfengine/wfi/WorkFlowInstanceAction!getAllNodePermissionAction.do";
	var param = {};
	param.flowConfigId = flowConfigId;
	param.pid = pid;
	param.token = token;

	// 同步请求数据
	AjaxRequest_Sync(url, param, function(req) {
		var responseText = req.responseText;
		var responseObj = Ext.decode(responseText);

		if (responseObj.success) {
			var perms = responseObj.data;
			processGoSelect(perms);
		}
	});
}

/**
 * 跟踪流程
 */
function btnWFTrace_Click() {
	var flowConfigId = $("flowConfigId").value;
	var pid = $("pid").value;
	var url = contextPath + "/jteap/wfengine/wfi/WorkFlowInstanceAction!wfTraceAction.do?flowConfigId=" + flowConfigId
			+ "&pid=" + pid;
	var result = showModule(url, true, 800, 600);
}

/**
 * 流程日志
 */
function btnWFLog_Click() {
	var pid = $("pid").value;
	var url = contextPath + "/jteap/wfengine/wfi/wfLog.jsp?pid=" + pid
	var result = showModule(url, true, 600, 400);
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
function print() {
	var formFrame = $("formFrame")
	if (formType == '02') {// cform
		if (typeof formFrame.contentWindow.saveFormInAjax == 'function') {
			var docid = formFrame.contentWindow.saveFormInAjax(false);
			$("docid").value = docid;
		}
	} else if (formType == '01') {// eform
		if (typeof formFrame.contentWindow.submitForm == 'function') {
			var docid = formFrame.contentWindow.f_SaveForm(function(docid) {
				var myForm = window.parent.myForm;
				myForm.docid.value = docid;
			})
		}
	}

	formFrame.contentWindow.doPrintForm();
}

/**
 * 撤销
 */
function goCancelTask() {
	var actionUrl = contextPath + "/jteap/wfengine/wfi/WorkFlowInstanceAction!cancelNodeAction.do";
	$("myForm").action = actionUrl;
	$("myForm").submit();
}
