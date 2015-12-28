<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
    // 查询
	var link1="${contextPath}/jteap/jx/qxgl/QxcxAction!showQxcxAction.do";
	
	// model模式弹出页面
	var link2="${contextPath}/jteap/ModuleWindowForm.jsp";
	
	// 查看流程页面
	var link3 = "${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showViewAction.do";
	
	// 部门树
	var link4 = "${contextPath}/jteap/jx/qxgl/GroupExtendForJxAction!showSbQxdTreeAction.do";
</script>