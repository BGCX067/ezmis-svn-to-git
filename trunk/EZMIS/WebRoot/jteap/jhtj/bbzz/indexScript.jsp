 
<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>

<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<jteap:dict catalog="BBZT"/>
<script>	
	var link1="${contextPath}/jteap/jhtj/bbsjydy/bbIOAction!showTreeAction.do";
	
	var link2="${contextPath}/jteap/jhtj/bbzz/bbzzAction!deleteNodeAction.do";
	
	var link3="${contextPath}/jteap/jhtj/bbzz/bbzzAction!saveUpdateAction.do";

	var link4="${contextPath}/jteap/jhtj/bbzz/bbzzAction!showListAction.do";
	
	var link5="${contextPath}/jteap/jhtj/bbzz/bbzzAction!removeAction.do";
	
	var link6="${contextPath}/jteap/jhtj/bbsjydy/bbzcForm.jsp";
	
	var link7="${contextPath}/jteap/jhtj/bbzz/bbzzAction!showUpdateAction.do";

	var link8="${contextPath}/jteap/jhtj/bbzz/bbzzAction!showListAction.do";
	
	var link9="${contextPath}/jteap/jhtj/bbzz/bbzzAction!findInterfaceAction.do";
	
	//给接口初始化
	var link10="${contextPath}/jteap/jhtj/sjydy/tjAppIOAction!createInterfaceBySqlAction.do";
	//接口展示页面
	var link11="${contextPath}/jteap/jhtj/sjydy/initInterfaceForm.jsp";
	//报表制作
	var link12="${contextPath}/jteap/jhtj/bbzz/bbzzForm.jsp";
	//生成报表
	var link13="${contextPath}/jteap/jhtj/bbzz/bbzzAction!generateBbAction.do";
	//判断报表是否存在
	var link14="${contextPath}/jteap/jhtj/bbzz/bbzzAction!isExistAction.do";
	//设置状态
	var link15="${contextPath}/jteap/jhtj/bbzz/statusForm.jsp";
	var link16="${contextPath}/jteap/jhtj/bbzz/bbzzAction!updateStatusAction.do";
	var link17="${contextPath}/jteap/jhtj/bbzz/bbzzAction!isScStateAction.do";
	
</script>