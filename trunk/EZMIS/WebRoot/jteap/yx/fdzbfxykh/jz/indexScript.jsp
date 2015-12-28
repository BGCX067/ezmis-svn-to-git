<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>

<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>

<script>
	
	//构建小指标查询页面的 列模型
	var link1 = "${contextPath}/jteap/yx/fdzbfxykh/DirectiveTableInfoAction!buildColsAction.do";
	
	//构建小指标查询页面的 行数据
	var link2 = "${contextPath}/jteap/yx/fdzbfxykh/DirectiveTableInfoAction!buildRowsAction.do";
	
	//取数
	var link3 = "${contextPath}/jteap/yx/fdzbfxykh/DirectiveTableInfoAction!xzbQsAction.do";
	
</script>