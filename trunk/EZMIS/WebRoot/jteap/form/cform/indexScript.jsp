<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>

<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	//显示自定义表单分类树
	var link1="${contextPath}/jteap/form/cform/CFormCatalogAction!showTreeAction.do";
	
	//删除分类
	var link2="${contextPath}/jteap/form/cform/CFormCatalogAction!deleteNodeAction.do";
	
	//保存分类
	var link3="${contextPath}/jteap/form/cform/CFormCatalogAction!saveOrUpdateCatalogAction.do";
	
	//显示自定义表单列表
	var link4="${contextPath}/jteap/form/cform/CFormAction!showListAction.do";
	
	//删除表单
	var link5="${contextPath}/jteap/form/cform/CFormAction!removeAction.do";
</script>
