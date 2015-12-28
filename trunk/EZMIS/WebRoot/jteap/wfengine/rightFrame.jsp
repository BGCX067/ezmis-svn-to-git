<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.system.resource.model.Module"%>
<%@page import="com.jteap.system.resource.manager.ModuleManager"%>
<%@page import="java.util.Collection"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@ include file="/inc/import.jsp"%>
<html>
	<head>
		<%@ include file="/inc/meta.jsp"%>
		<meta http-equiv="x-ua-compatible" content="ie=7" />
		<%@ include file="indexScript.jsp"%>
		<title>Module页面中右边公共页面</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
		<link rel="stylesheet" type="text/css" href="styles.css"></link>
		<%
			String moduleId = (String) request.getParameter("moduleId");
			String childId = (String) request.getParameter("childId");
			ModuleManager moduleManager = (ModuleManager) SpringContextUtil.getBean("moduleManager");
			Module module = (Module)moduleManager.get(moduleId);
			String moduleResName = (String)module.getResName();
		%>
		<%@ include file="/inc/ext-all.jsp"%>
		<script type="text/javascript" src="${contextPath}/script/date.js"></script>
		<script type="text/javascript" src="FunctionPanel.js" charset="UTF-8"></script>
		<script type="text/javascript" src="RightGrid.js" charset="UTF-8"></script>
		<script type="text/javascript" src="${contextPath}/script/ext-extend/form/SelectBox.js" charset="UTF-8"></script>
		<script type="text/javascript" src="${contextPath}/script/ext-extend/SearchField.js"></script>
		<script type="text/javascript" src="${contextPath}/script/ext-extend/tab/TabCloseMenu.js"></script>
		<script type="text/javascript" src="${contextPath}/script/ext-extend/form/UniqueTextField.js" charset="UTF-8"></script>
		<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelPanel.js"></script>
		<script type="text/javascript" src="${contextPath}/script/ext-extend/form/TitlePanel.js"></script>
		<script type="text/javascript" src="EditCurrPresonForm.js" charset="UTF-8"></script>
		<script type="text/javascript" src="ChangePasswordForm.js" charset="UTF-8"></script>
		<script type="text/javascript" src="MainMenu.js" charset="UTF-8"></script>
		<script type="text/javascript" src="index.js" charset="UTF-8"></script>
	</head>

	<body>
		<div id="center">
		</div>
	</body>
</html>
