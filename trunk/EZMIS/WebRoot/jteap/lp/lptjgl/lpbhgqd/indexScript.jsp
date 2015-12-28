<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	// 部门两票显示列表
	var link1="${contextPath}/jteap/lp/lptjgl/LptjglAction!showLpbhgqdListAction.do";
	
	// 查询面板 班组下拉树
	var link2="${contextPath}/jteap/lp/pkgl/GroupExtendForLpAction!showGzpTreeAction.do";
	
	// 两票票面查看,获得流程ID
	var link3="${contextPath}/jteap/lp/lpsh/LpshAction!getPidAction.do";
	
	// model模式弹出页面
	var link4 = "${contextPath}/jteap/ModuleWindowForm.jsp";
	
	// 查看签收页面
	var link5 = "${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showViewAction.do";
</script>
