<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>

<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	//显示自定义表单分类树
	var link1="${contextPath}/jteap/form/eform/EFormCatalogAction!showTreeAction.do";
	
	//删除分类
	var link2="${contextPath}/jteap/form/eform/EFormCatalogAction!deleteNodeAction.do";
	
	//保存分类
	var link3="${contextPath}/jteap/form/eform/EFormCatalogAction!saveOrUpdateCatalogAction.do";
	
	//显示自定义表单列表
	var link4="${contextPath}/jteap/form/eform/EFormAction!showListAction.do";
	
	//删除表单
	var link5="${contextPath}/jteap/form/eform/EFormAction!removeAction.do";
	
	//定稿
	var link6="${contextPath}/jteap/form/eform/EFormAction!finalManuscriptAction.do";
	
	//移动表单至一个分类
	var link7 = "${contextPath}/jteap/form/eform/EFormAction!moveFormToCatalog.do";
	
</script>
