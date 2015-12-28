 
<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>

<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>	
	//列表
	var link4="${contextPath}/jteap/jhtj/jkbldy/tjInterFaceAction!showListAction.do";
	
	//删除
	var link5="${contextPath}/jteap/jhtj/jkbldy/tjInterFaceAction!removeAction.do";
	
	//添加修改功能
	var link6="${contextPath}/jteap/jhtj/jkbldy/jkbldyForm.jsp";
	
	var link7="${contextPath}/jteap/jhtj/jkbldy/tjInterFaceAction!showUpdateAction.do";
	
	var link8="${contextPath}/jteap/jhtj/jkbldy/tjInterFaceAction!saveUpdateAction.do";
</script>