<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>

<script>
	
	// 查看已批准的物资申请
	var link1="${contextPath}/jteap/wz/xqjhsq/XqjhsqfpxxAction!showDsxXqjhAction.do";
	
	//获取指定流程实例
	var link2="${contextPath}/jteap/wz/xqjh/XqjhDetailAction!showProcessinstance.do";
	
	// model模式弹出页面
	var link3 = "${contextPath}/jteap/ModuleWindowForm.jsp";
	
	// 查看签收页面
	var link4 = "${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showViewAction.do";
	
	//组织结构树
	var link5="${contextPath}/jteap/wz/xqjhsq/GroupExtendForWzAction!showWzSqbmTreeAction.do";
	
	// 计划员用户
	var link6="${contextPath}/jteap/wz/xqjhsq/XqjhsqDetailAction!showPersons.do?type=0";
	
	
</script>