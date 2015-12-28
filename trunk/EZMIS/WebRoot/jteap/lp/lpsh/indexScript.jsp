<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	// 两票显示树
	var link1="${contextPath}/jteap/lp/pkgl/PkglAction!showLpTreeAction.do";
	
	// 两票显示列表
	var link2="${contextPath}/jteap/lp/lpsh/LpshAction!showListAction.do";
	
	// 两票审核窗口
	var link3="${contextPath}/jteap/lp/lpsh/lpshEditForm.jsp";
	
	// 显示审核部门树
	var link4="${contextPath}/jteap/lp/pkgl/GroupExtendForLpAction!showTreeAction.do";
	
	// 保存两票审核信息
	var link5="${contextPath}/jteap/lp/lpsh/LpshAction!saveOrUpdateAction.do";
	
	// 两票票面查看,获得流程ID
	var link6="${contextPath}/jteap/lp/lpsh/LpshAction!getPidAction.do";
	
	// model模式弹出页面
	var link7 = "${contextPath}/jteap/ModuleWindowForm.jsp";
	
	// 查看签收页面
	var link8 = "${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showViewAction.do";
	
	// 两票审核查看窗口
	var link10="${contextPath}/jteap/lp/lpsh/lpshViewForm.jsp";
	
</script>
