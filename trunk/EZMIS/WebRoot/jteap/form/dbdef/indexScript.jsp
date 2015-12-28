<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>

<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>

<%
//为了实现分级权限
PersonManager personManager=(PersonManager)SpringContextUtil.getBean("personManager");
//isRoot & curPersonId is defined in import.jsp
String adminGroupIds=isRoot?"":personManager.findAdminGroupIdsOfThePerson(curPersonId);

%>
<script>
	var adminGroupIds="<%=adminGroupIds%>";
	//表定义schema节点
	var link1="${contextPath}/jteap/form/dbdef/DefTableInfoAction!showTreeSchemaAction.do";
	
	//请求表定义树形结构
	var link2="${contextPath}/jteap/form/dbdef/DefTableInfoAction!showTreeAction.do";
	
	//请求字段定义列表
	var link3="${contextPath}/jteap/form/dbdef/DefColumnInfoAction!showListAction.do";
	
	//保存表
	var link4="${contextPath}/jteap/form/dbdef/DefTableInfoAction!saveUpdateTableAction.do";
	
	//删除表
	var link5="${contextPath}/jteap/form/dbdef/DefTableInfoAction!deleteTableAction.do";	
	
	//修改表
	var link6="${contextPath}/jteap/form/dbdef/DefTableInfoAction!showTableUpdateAction.do";	
	
	//修改字段
	var link7="${contextPath}/jteap/form/dbdef/DefColumnInfoAction!showUpdateAction.do";
	
	//保存字段
	var link8="${contextPath}/jteap/form/dbdef/DefColumnInfoAction!saveColumnInfoAction.do";	
	
	//保存字段
	var link15="${contextPath}/jteap/form/dbdef/DefColumnInfoAction!resetSortNoAction.do";	
	
	//删除字段
	var link9="${contextPath}/jteap/form/dbdef/DefColumnInfoAction!removeAction.do";	
	
	//取得所有物理表
	var link10="${contextPath}/jteap/form/dbdef/PhysicTableAction!showTreeAction.do";	
	
	//判断是否存在指定的定义表
	var link11="${contextPath}/jteap/form/dbdef/DefTableInfoAction!isExistDefTableAction.do";
	
	//导入物理表
	var link12="${contextPath}/jteap/form/dbdef/PhysicTableAction!importPhysicTableAction.do";
	
	//重建物理表
	var link13="${contextPath}/jteap/form/dbdef/PhysicTableAction!rebuildPhysicTableAction.do";

	//移动数据
	var link14="${contextPath}/jteap/form/dbdef/DefColumnInfoAction!moveFieldAction.do";
	
	var link16= "${contextPath}/jteap/form/dbdef/DefTableInfoAction!showTreeDataSourceAction.do";
	
	var link17="${contextPath}/jteap/form/dbdef/DefDataSourceAction!showListAction.do";
	
	//删除物理表
	var link18="${contextPath}/jteap/form/dbdef/DefTableInfoAction!deletePhysicTableAction.do";	
	
	//判断表定义是否存在物理表
	var link19="${contextPath}/jteap/form/dbdef/DefTableInfoAction!isExistPhysicTableAction.do";
	
	//移动表单分类树节点
	var link20="${contextPath}/jteap/form/dbdef/DefTableInfoAction!moveNodeAction.do";
	
	//保存分类节点
	var link21="${contextPath}/jteap/form/dbdef/DefTableInfoAction!saveOrUpdateCatalogAction.do";
	
	//删除分类节点
	var link22="${contextPath}/jteap/form/dbdef/DefTableInfoAction!deleteNodeAction.do";
	
	//获取所有分类
	var link23="${contextPath}/jteap/form/dbdef/DefTableInfoAction!findDefTableCatalogAction.do";
	
</script>