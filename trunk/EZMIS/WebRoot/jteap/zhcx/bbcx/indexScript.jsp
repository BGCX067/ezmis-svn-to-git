 
<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>

<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<jteap:dict catalog="BBZT"/>
<script>	

	var link4="${contextPath}/jteap/jhtj/bbcx/bbcxAction!showListAction.do";
	var link5="${contextPath}/jteap/jhtj/bbcx/bbcxAction!removeAction.do";
	
	var link6="${contextPath}/jteap/jhtj/bbsjydy/bbzcForm.jsp";
	
	var link7="${contextPath}/jteap/jhtj/bbcx/bbcxAction!showUpdateAction.do";

	var link8="${contextPath}/jteap/jhtj/bbcx/bbcxAction!showListAction.do";
	
	var link9="${contextPath}/jteap/jhtj/bbcx/bbcxAction!findInterfaceAction.do";
	
	var link10="${contextPath}/jteap/jhtj/bbcx/bbcxAction!dynaAddSearPanelAction.do";
	
	//查询报表
	var link11="${contextPath}/jteap/jhtj/bbcx/bbcxAction!bbcxByZhcxAction.do";
	//报表制作
	var link12="${contextPath}/jteap/zhcx/bbcx/index.jsp";
	//生成报表
	var link13="${contextPath}/jteap/jhtj/bbzz/bbzzAction!generateBbAction.do";
</script>