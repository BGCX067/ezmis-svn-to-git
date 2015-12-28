<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>

<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>

<script>
	//请求小指标表定义树形结构
	var link1="${contextPath}/jteap/yx/fdzbfxykh/DirectiveTableInfoAction!showTreeAction.do";
	
	//保存小指标表定义
	var link2="${contextPath}/jteap/yx/fdzbfxykh/DirectiveTableInfoAction!saveUpdateAction.do";
	
	//修改小指标表定义
	var link3="${contextPath}/jteap/yx/fdzbfxykh/DirectiveTableInfoAction!showUpdateAction.do";	
	
	//删除小指标表定义、删除小指标的字段定义
	var link4="${contextPath}/jteap/yx/fdzbfxykh/DirectiveTableInfoAction!deleteTableInfoAction.do";	
	
	//判断小指标表定义是否存在物理表
	var link5="${contextPath}/jteap/yx/fdzbfxykh/DirectiveTableInfoAction!isExistPhysicTableAction.do";
	
	//删除小指标对应的物理记录表
	var link6="${contextPath}/jteap/yx/fdzbfxykh/DirectiveTableInfoAction!deletePhysicTableAction.do";	
	
	//同步小指标记录物理表
	var link7="${contextPath}/jteap/yx/fdzbfxykh/DirectiveTableInfoAction!rebuildPhysicTableAction.do";
	
	//请求小指标字段定义列表
	var link8="${contextPath}/jteap/yx/fdzbfxykh/DirectiveColumnInfoAction!showListAction.do";
	
	//保存小指标字段定义
	var link9="${contextPath}/jteap/yx/fdzbfxykh/DirectiveColumnInfoAction!saveColumnInfoAction.do";	
	
	//删除小指标字段定义
	var link10="${contextPath}/jteap/yx/fdzbfxykh/DirectiveColumnInfoAction!removeColumnInfoAction.do";
		
</script>