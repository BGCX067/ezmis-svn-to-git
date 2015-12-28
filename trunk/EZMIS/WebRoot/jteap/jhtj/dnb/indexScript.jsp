 
<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>

<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>	
	var link1="${contextPath}/jteap/jhtj/dnb/dnbAction!showTreeAction.do";
	
	var link2="${contextPath}/jteap/jhtj/dnb/dnbAction!getDataTableAction.do";
	
	var link3="${contextPath}/jteap/jhtj/ljydy/appSystemConnAction!saveUpdateAction.do";

	var link4="${contextPath}/jteap/jhtj/dnb/dnbAction!showListAction.do";
	var link5="${contextPath}/jteap/jhtj/ljydy/appSystemFieldAction!removeAction.do";
	
	var link6="${contextPath}/jteap/jhtj/ljydy/appSystemConnForm.jsp";
	
	var link7="${contextPath}/jteap/jhtj/ljydy/appSystemConnAction!validateAppSystemConnNameUniqueAction.do";
	var link8="${contextPath}/jteap/jhtj/ljydy/appSystemConnAction!validateDataBaseConn.do";
	var link9="${contextPath}/jteap/jhtj/ljydy/appSystemConnAction!showUpdateAction.do";
	var link10="${contextPath}/jteap/jhtj/ljydy/dataDefineForm.jsp";
	var link11="${contextPath}/jteap/jhtj/ljydy/appSystemConnAction!getTablbsBySistemAction.do";
	var link12="${contextPath}/jteap/jhtj/ljydy/appSystemConnAction!getAllFieldInfoInTableAction.do";
	var link13="${contextPath}/jteap/jhtj/ljydy/appSystemConnAction!saveDataDefineAction.do";
	var link14="${contextPath}/jteap/jhtj/ljydy/appSystemFieldAction!deleteNodeAction.do";
</script>