 
<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>

<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	//分类树
	var link1="${contextPath}/jteap/system/dataperm/DatapermCatalogAction!showTreeAction.do";
	
	//删除分类
	var link2="${contextPath}/jteap/system/dataperm/DatapermCatalogAction!deleteNodeAction.do";
	
	//保存分类
	var link3="${contextPath}/jteap/system/dataperm/DatapermCatalogAction!saveOrUpdateCatalogAction.do";
	
	
	//列表
	var link4="${contextPath}/jteap/system/dataperm/DatapermAction!showListAction.do";
	
	//删除
	var link5="${contextPath}/jteap/system/dataperm/DatapermAction!removeAction.do";
	
	//添加修改功能
	var link6="${contextPath}/jteap/system/dataperm/DatapermForm.jsp";
	//选择列表
	var link7="${contextPath}/jteap/system/dataperm/TableForm.jsp";
	//显示列表内容
	var link8="${contextPath}/jteap/system/dataperm/TableToClassAction!showListAction.do";
	//模板
	var link9="${contextPath}/jteap/system/dataperm/templateFrame.jsp";
	//动态生成列信息
	var link10="${contextPath}/jteap/system/dataperm/DatapermAction!findDynaColumns.do";
	//动态读取数据
	var link11="${contextPath}/jteap/system/dataperm/DatapermAction!findDynaData.do";
	//验证权限名是否重复
	var link12="${contextPath}/jteap/system/dataperm/DatapermAction!beforeSave.do";
	//保存或更新
	var link13="${contextPath}/jteap/system/dataperm/DatapermAction!saveUpdateAction.do";
	
	var link14="${contextPath}/jteap/system/dataperm/DatapermAction!showUpdateAction.do";
	//增加表和类的映射表
	var link15="${contextPath}/jteap/system/dataperm/AddTableClassForm.jsp";
	
	var link16="${contextPath}/jteap/system/dataperm/TableToClassAction!showUpdateAction.do";
	
	//增加表和类的映射表
	var link17="${contextPath}/jteap/system/dataperm/TableToClassAction!saveUpdateAction.do";
	
	var link18="${contextPath}/jteap/system/dataperm/TableToClassAction!removeAction.do";
</script>