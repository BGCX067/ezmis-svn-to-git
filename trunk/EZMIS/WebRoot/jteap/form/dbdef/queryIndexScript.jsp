<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>

<%
//为了实现分级权限
PersonManager personManager=(PersonManager)SpringContextUtil.getBean("personManager");
//isRoot & curPersonId is defined in import.jsp
String adminGroupIds=isRoot?"":personManager.findAdminGroupIdsOfThePerson(curPersonId);

String tableid=request.getParameter("tableid");


%>
<script>
	var adminGroupIds="<%=adminGroupIds%>";
	//表定义schema节点
	var link1="${contextPath}/jteap/form/dbdef/DefColumnInfoAction!getColumnInfoButLobAction.do";
	
	//加载表数据
	var link2="${contextPath}/jteap/form/dbdef/PhysicTableAction!showTableDataAction.do";
	
</script>