<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>

<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	var link1="${contextPath}/jteap/iplock/IPLockAction!showListAction.do";
	
	var link2="${contextPath}/jteap/iplock/IPLockAction!saveUpdateAction.do";
	
	//删除
	var link3="${contextPath}/jteap/iplock/IPLockAction!removeAction.do";
	
	
	//显示修改内容
	var link4="${contextPath}/jteap/iplock/IPLockAction!showUpdateAction.do";
</script>
