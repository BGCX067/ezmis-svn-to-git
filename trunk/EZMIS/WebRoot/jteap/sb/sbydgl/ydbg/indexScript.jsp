<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	// 查看当前登录人的待处理缺陷单
	var link1="${contextPath}/jteap/sb/sbydgl/SbydbgAction!showDclQxdAction.do";

	// 查看已处理缺陷单
	var link2="${contextPath}/jteap/sb/sbydgl/SbydbgAction!showYclQxdAction.do";
	
	// 查看草稿箱
	var link3="${contextPath}/jteap/sb/sbydgl/SbydbgAction!showCgxAction.do";
	
	// 释放签收
	var link4="${contextPath}/jteap/wfengine/tasktodo/TaskToDoAction!releaseSignInAction.do";
	
	// 查看全厂
	var link5="${contextPath}/jteap/sb/sbydgl/SbydbgAction!showQcAction.do";
	
	// 查看作废
	var link6="${contextPath}/jteap/sb/sbydgl/SbydbgAction!showZfQxdAction.do";
	
	// 作废缺陷单
	var link7="${contextPath}/jteap/sb/sbydgl/SbydbgAction!cancelQxdAction.do";
	
	// 查看流程
	var link8="${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showUpdateAction.do";
	
	// model模式弹出页面
	var link9 = "${contextPath}/jteap/ModuleWindowForm.jsp";
	
	// 查看签收页面
	var link10 = "${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showViewAction.do";

	// 专业转发
	var link11="${contextPath}/jteap/sb/sbydgl/SbydbgAction!zyzfAction.do";
	
	// 撤销
	var link12="${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!cancelNodeAction.do";
</script>