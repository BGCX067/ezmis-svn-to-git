<%@page import="com.jteap.system.doclib.manager.*"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>

<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	//添加文档级别
	var link1="${contextPath}/jteap/doclib/DoclibLevelAction!saveUpdateAction.do";
	
	//显示修改文档级别信息
	var link2="${contextPath}/jteap/doclib/DoclibLevelAction!showmodifyDoclibInfoAction.do";
		
	//显示文档级别列表
	var link4="${contextPath}/jteap/doclib/DoclibLevelAction!showListAction.do";
	
	//删除文档级别
	var link5="${contextPath}/jteap/doclib/DoclibLevelAction!removeAction.do";

	//级别角色列表
	var link7="${contextPath}/jteap/doclib/DoclibLevelRoleAction!showDoclibLevelRoleAction.do";
	
	
	//删除级别角色
	var link9="${contextPath}/jteap/doclib/DoclibLevelRoleAction!removeAction.do";
	
	//请求角色树
    var link16="${contextPath}/jteap/doclib/DoclibRoleTreeAction!showRoleTreeForCheckAction.do";
	
	//请求已经选择的角色
    var link18="${contextPath}/jteap/doclib/DoclibLevelRoleAction!showDoclibLevelRoleCheckedAction.do";
</script>
