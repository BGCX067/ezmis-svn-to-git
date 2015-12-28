<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>

<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	//分类树
	var link1="${contextPath}/jteap/component/livegrid/BigDtCatalogAction!showTreeAction.do";
	
	//删除分类
	var link2="${contextPath}/jteap/component/livegrid/BigDtCatalogAction!deleteNodeAction.do";
	
	//保存分类
	var link3="${contextPath}/jteap/component/livegrid/BigDtCatalogAction!saveOrUpdateCatalogAction.do";
	
	
	//列表
	var link4="${contextPath}/jteap/component/livegrid/BigDtAction!showListAction.do";
	
	//livegrid列表
	var link7="${contextPath}/jteap/component/livegrid/BigDtAction!showListForLiveGridAction.do";
	//删除
	var link5="${contextPath}/jteap/component/livegrid/BigDtAction!removeAction.do";
	
	//添加修改功能
	var link6="${contextPath}/jteap/component/livegrid/BigDtForm.jsp";
	
</script>