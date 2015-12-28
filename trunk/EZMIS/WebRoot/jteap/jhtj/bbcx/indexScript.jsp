 
<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>

<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<jteap:dict catalog="BBZT"/>
<script>	
	var link1="${contextPath}/jteap/jhtj/bbcx/bbcxAction!showTreeAction.do";
	
	var link2="${contextPath}/jteap/jhtj/bbcx/bbcxAction!deleteNodeAction.do";
	
	var link3="${contextPath}/jteap/jhtj/bbcx/bbcxAction!saveUpdateAction.do";

	var link4="${contextPath}/jteap/jhtj/bbcx/bbcxAction!showListAction.do";
	
	var link5="${contextPath}/jteap/jhtj/bbcx/bbcxAction!removeAction.do";
	
	var link6="${contextPath}/jteap/jhtj/bbsjydy/bbzcForm.jsp";
	
	var link7="${contextPath}/jteap/jhtj/bbcx/bbcxAction!showUpdateAction.do";

	var link8="${contextPath}/jteap/jhtj/bbcx/bbcxAction!showListAction.do";
	
	var link9="${contextPath}/jteap/jhtj/bbcx/bbcxAction!findInterfaceAction.do";
	
	var link10="${contextPath}/jteap/jhtj/bbcx/bbcxAction!dynaAddSearPanelAction.do";
	
	//报表制作
	var link12="${contextPath}/jteap/jhtj/bbzz/bbzzForm.jsp";
</script>