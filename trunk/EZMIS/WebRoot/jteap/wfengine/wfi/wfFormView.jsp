<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.wfengine.workflow.model.FlowConfig"%>
<%@page import="com.jteap.form.eform.model.EForm"%>
<%@page import="com.jteap.form.cform.model.CForm"%>
<%@page import="com.jteap.form.eform.manager.EFormManager"%>
<%@page import="com.jteap.form.cform.manager.CFormManager"%>

<%@ include file="/inc/import.jsp"%>
<html>
<%	
	EFormManager eformManager = (EFormManager) SpringContextUtil.getBean("eformManager");
	CFormManager cformManager = (CFormManager) SpringContextUtil.getBean("cformManager");
	FlowConfig flowConfig=(FlowConfig)request.getAttribute("flowConfig");
	String isEdit = request.getParameter("isEdit");
	String type=flowConfig.getFormtype();
	String formId = flowConfig.getFormId();
	String processPageUrl = flowConfig.getProcess_url();
	Object docidObj = request.getAttribute("docid");
	String docid = (docidObj==null?"":docidObj.toString());
	String formSn = "";
	int formFrameHeight = 0;
	//01-EFORM 02-CFORM 03-文档 04-无
	if ("01".equals(type)) {
		EForm eform = eformManager.get(formId);
		formSn = eform.getSn();
		formFrameHeight = eform.getHeight();
		processPageUrl=contextPath+"/jteap/form/eform/eformRec.jsp?formSn="+eform.getSn()+"&docid="+docid;
	} else if("02".equals(type)) {
		CForm cform = cformManager.get(formId);
		formSn = cform.getSn();
		processPageUrl=contextPath+"/jteap/form/cform/excelFormRecWF.jsp?formId="+cform.getId()+"&docid="+docid;
	}
%>
	<head>
		<%@ include file="/inc/meta.jsp"%>
		<%@ include file="/inc/ext-all.jsp" %>
		<title>JTEAP 2.0</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
		<script type="text/javascript" src="${contextPath}/jteap/wfengine/wfi/script/wfFormView.js"></script>
		<script type="text/javascript">
			var formType = <%= type%>;
			var isEdit = <%=isEdit%>;
			var editableInputs = [];
			function onload(){
				var url="<%=processPageUrl%>";
				var formFrameHeight = "<%=formFrameHeight%>";
				var toolbarHeight = $('toolbar').style.height
				toolbarHeight = toolbarHeight.substring(0, toolbarHeight.indexOf("px"));
				var height = formFrameHeight - toolbarHeight-22;
				$('formFrame').height = height;
				$("formFrame").src=url;
				if (isEdit == true) {
					document.getElementById('btnSignIn').style.display = '';
				}
			}
		</script>
		<style>
		</style>

	</head>

	<body onload="onload();">
		<div id="toolBar" style="margin:2px 2px 2px 2px;height:30px;padding:10 10 10 10;background-color: #efefef;border: solid 1px #e1e1e1">
			<button id="btnSignIn" onclick="btnSignIn_Click();" style='display:none'>签收</button>
			<button id="btnTrace" onclick="btnWFTrace_Click();">流程跟踪</button>
			<button id="btnLog" onclick="btnWFLog_Click();">流程日志</button>
			<button id="btnOperLog" onclick="btnXqjhsqOperLog_Click();">操作日志</button>
			<button id="btnExit" onclick="window.close()">退出</button>
		</div>
		<div id="paramDiv" style="display:none;margin:2px 2px 2px 2px;overflow-y:scroll;height:130px;padding:10 10 10 10;background-color: #efefef;border: solid 1px #e1e1e1">
			<form name="myForm" method="post" action="${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showUpdateAction.do">
				<table>
					<tr>
						<td>业务数据编号：</td>
						<td><input type="text" name="docid" value="<%=docid%>" size="100"/></td>
					</tr>
					<tr>
						<td>流程实例编号:</td>
						<td><input id="pid" type="text" name="pid" value="${pi.id}" size="100"/></td>
					</tr>
					<tr>
						<td>流程配置编号：</td>
						<td><input type="text" name="flowConfigId" value="${flowConfig.id}" size="100"/></td>
					</tr>
					<tr>
						<td>当前TOKEN编号：</td>
						<td><input type="text" name="token" value="${token}" size="100"/></td>
					</tr>		
					<tr>
						<td>流程名称：</td>
						<td><input type="text" name="flowName" value="${flowName}" size="100"/></td>
					</tr>	
					<tr>
						<td>当前表单SN</td>
						<td><input type="text" name="formSn" value='<%=formSn%>' size="100"/></td>
					</tr>		
				</table>
			</form>
			
		</div>
		<div>
			<iframe id="formFrame" name="formFrame" frameborder="yes" scrolling="auto" height="90%" width="100%"></iframe>
		</div>

	</body>
</html>
