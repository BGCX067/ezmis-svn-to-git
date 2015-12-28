 
<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>

<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>	
	var link1="${contextPath}/jteap/jhtj/bbzc/bbzcAction!showTreeAction.do";
	
	var link2="${contextPath}/jteap/jhtj/bbzc/bbzcAction!deleteNodeAction.do";
	
	var link3="${contextPath}/jteap/jhtj/bbzc/bbzcAction!saveUpdateAction.do";

	//列表
	var link4="${contextPath}/jteap/jhtj/bbzc/bbzcAction!showListAction.do";
	
	//删除
	var link5="${contextPath}/jteap/jhtj/bbzc/bbzcAction!removeAction.do";
	
	//添加修改功能
	var link6="${contextPath}/jteap/jhtj/bbzc/bbzcForm.jsp";
	
	var link7="${contextPath}/jteap/jhtj/bbzc/bbzcAction!showUpdateAction.do";

	var link8="${contextPath}/jteap/jhtj/bbzc/bbIndexAction!showListAction.do";
	var link9="${contextPath}/jteap/jhtj/bbzc/bbindexForm.jsp";
	var link10="${contextPath}/jteap/jhtj/bbzc/bbIndexAction!saveUpdateAction.do";
	var link11="${contextPath}/jteap/jhtj/bbzc/bbIndexAction!showUpdateAction.do";
	var link12="${contextPath}/jteap/jhtj/bbzc/bbIndexAction!removeAction.do";
	var link13="${contextPath}/jteap/jhtj/bbzc/bbzcAction!validateNameUniqueAction.do";
	var link14="${contextPath}/jteap/jhtj/bbzc/bbIndexAction!validateNameUniqueAction.do";
</script>