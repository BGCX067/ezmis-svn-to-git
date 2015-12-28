<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>

<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	var link1="${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showListAction.do";
	//人员选择页面的url
	var link2="${contextPath}/jteap/system/person/personSelect.jsp" ;

</script>
