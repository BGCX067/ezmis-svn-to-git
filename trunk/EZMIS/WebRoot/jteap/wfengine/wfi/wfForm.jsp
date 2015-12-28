<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.wfengine.workflow.model.FlowConfig"%>
<%@page import="com.jteap.core.utils.StringUtil"%>
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
	String type=flowConfig.getFormtype();
	String formId = flowConfig.getFormId();
	String processPageUrl = flowConfig.getProcess_url();
	Object docidObj = request.getAttribute("docid");
	String docid = (docidObj==null?"":docidObj.toString());
	String nodeVarJson = request.getAttribute("nodeVarJson")==null?"":request.getAttribute("nodeVarJson")+"";
	String process_form_id = request.getAttribute("process_form_id")==null?"":request.getAttribute("process_form_id")+"";
	if(StringUtil.isNotEmpty(nodeVarJson)){
		nodeVarJson = StringUtil.encodeChar(nodeVarJson);
	}
	if (StringUtil.isNotEmpty(process_form_id)) {
		formId = process_form_id;		
	}
	//新增加参数,专门用于外部向表单内部传递参数用
	String param=(String)request.getAttribute("param");
	
	String formSn = "";
	int formFrameHeight = 0;
	//01-EFORM 02-CFORM 03-文档 04-无
	if ("01".equals(type)) {
		EForm eform = eformManager.get(formId);
		formSn = eform.getSn();
		formFrameHeight = eform.getHeight();
		processPageUrl=contextPath+"/jteap/form/eform/eformRec.jsp?formSn="+eform.getSn()+"&docid="+docid+"&param="+param;
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
		<script type="text/javascript" src="${contextPath}/jteap/wfengine/wfi/script/wfForm.js"></script>
		<script type="text/javascript">
			var editableInputs = [];
			var formType = <%= type%>;
			var url = "<%=processPageUrl%>";
			var formFrameHeight = "<%=formFrameHeight%>";
		</script>
		<style>
		</style>

	</head>

	<body onload="onload();">
		<div id="toolBar" style="margin:2px 2px 2px 2px;height:25px;padding:10 10 10 10;background-color: #efefef;border: solid 1px #e1e1e1">
			<button id="btnSave" onclick="save();" style="display:none;"></button>
			<button id="btnNext" onclick="goNextTask();" style="display:none;"></button>
			<button id="btnBack" onclick="goBackTask();" style="display:none;"></button>
			<button id="btnCancel" onclick="goCancelTask();" style="display:none"></button>
			<button id="btnJump" onclick="btnSelectTask_Click();" style="display:none;"></button>
			<button id="btnTrace" onclick="btnWFTrace_Click();" style="display:none;"></button>
			<button id="btnLog" onclick="btnWFLog_Click();" style="display:none;"></button>
			<button id="btnOperLog" onclick="btnXqjhsqOperLog_Click();" style="display:none;"></button>
			<button id="btnPrint" onclick="print();" style="display:none;"></button>
			<button id="btnExit" onclick="window.close()" style="display:none;"></button>
			<button id="btnOpenWFV" onclick="if($('paramDiv').style.display=='block') $('paramDiv').style.display='none'; else $('paramDiv').style.display='block';" style="display:none;">参数开关</button>
		</div>
		<div id="paramDiv" style="display:none;margin:2px 2px 2px 2px;overflow-y:scroll;height:130px;padding:10 10 10 10;background-color: #efefef;border: solid 1px #e1e1e1">
			<form name="myForm" method="post" action="${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!saveUpdateAction.do">
				<input type="hidden" name="showbutton" value="${showbutton}"></input>
				<input type="hidden" name="dbid" value="${dbid}"></input>
				<input type="hidden" name="backReason" />
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
						<td>当前任务实例编号：</td>
						<td><input type="text" name="tid" value="${ti.id}" size="100"/></td>
					</tr>
					<tr>
						<td>当前TOKEN编号：</td>
						<td><input type="text" name="token" value="${token}" size="100"/></td>
					</tr>					
					
					<tr>
						<td>当前环节：</td>
						<td><input type="text" name="curNodeName" value="${curNodeName}" size="100"/></td>
					</tr>
					<tr>
						<td>当前环节处理人：</td>
						<td><input type="text" name="curActors" value="${curActors}" size="100"/></td>
					</tr>
					<tr>
						<td>当前环节变量：</td>
						<td><input type="text" name="nodeVarJson" value='${nodeVarJson}' size="100"/></td>
					</tr>
					
					<tr>
						<td>上一环节名称：</td>
						<td><input type="text" name="preNodeName" value="${preNodeName}" size="100"/></td>
					</tr>
					<tr>
						<td>上一环节处理人：</td>
						<td><input type="text" name="preActor" value="${preActor}" size="100"/></td>
					</tr>
					<tr>
						<td>下一环节名称：</td>
						<td><input type="text" name="nextNodes" value="${nextNodes}" size="100"/></td>
					</tr>
					<tr>
						<td>下一环节处理人：</td>
						<td><input type="text" name="nextActors" value="${nextActors}" size="100"/></td>
					</tr>					
					<tr>
						<td>起草人：</td>
						<td><input type="text" name="creator" value="${creator}" size="100"/></td>
					</tr>
					<tr>
						<td>起草时间：</td>
						<td><input type="text" name="creatdt" value="${creatdt}" size="100"/></td>
					</tr>
					<tr>
						<td>流程名称：</td>
						<td><input type="text" name="flowName" value="${flowName}" size="100"/></td>
					</tr>
					<tr>
						<td>流程版本：</td>
						<td><input type="text" name="ver" value="${ver}" size="100"/></td>
					</tr>
					
					<tr>
						<td>业务表数据：</td>
						<td><textarea name="recordJson" rows="5" cols="30"></textarea></td>
					</tr>
					
					<tr>
						<td>当前Sel处理类型：</td>
						<td><input type="text" name="processKind" value='${processKind}' size="100"/></td>
					</tr>
					<tr>
						<td>当前表单SN</td>
						<td><input type="text" name="formSn" value='<%=formSn%>' size="100"/></td>
					</tr>
				</table>
			</form>
			
		</div>
		<div>
			<iframe id="formFrame" name="formFrame" frameborder="yes" scrolling="auto" width="100%"></iframe>
		</div>

	</body>
</html>
