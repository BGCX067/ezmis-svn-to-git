<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>

<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	var link1="${contextPath}/jteap/dbdef/DefTableInfoAction!showTreeSchemaAction.do";
	
	var link2="";
	var link3="";
</script>