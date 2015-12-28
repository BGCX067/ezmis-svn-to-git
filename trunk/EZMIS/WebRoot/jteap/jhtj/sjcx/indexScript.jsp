 
<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>


<jteap:operations/>
<script>	
	var link1="${contextPath}/jteap/jhtj/sjcx/sjcxAction!showTreeAction.do";
	
	var link2="${contextPath}/jteap/jhtj/sjcx/sjcxAction!deleteNodeAction.do";
	
	var link3="${contextPath}/jteap/jhtj/sjcx/sjcxAction!saveUpdateAction.do";

	var link4="${contextPath}/jteap/jhtj/sjcx/sjcxAction!showListAction.do";
	var link5="${contextPath}/jteap/jhtj/sjcx/sjcxAction!removeAction.do";
	var link6="${contextPath}/jteap/jhtj/sjcx/sjcxAction!showIndexAction.do";
	var link7="${contextPath}/jteap/jhtj/sjcx/sjcxAction!dynaAddSearPanelAction.do";
	var link8="${contextPath}/jteap/jhtj/sjcx/sjcxAction!findDynaDataAction.do";
	var link9="${contextPath}/jteap/jhtj/sjcx/sjcxAction!findDayListAction.do";
</script>