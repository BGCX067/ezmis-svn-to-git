<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	// 查看当前登录人的待处理工作票
	var link1="${contextPath}/jteap/lp/lspgl/LspglAction!showDclLspAction.do";

	// 查看已处理工作票
	var link2="${contextPath}/jteap/lp/lspgl/LspglAction!showYclLspAction.do";
	
	// 查看草稿箱
	var link3="${contextPath}/jteap/lp/lspgl/LspglAction!showCgxAction.do";
	
	// 释放签收
	var link4="${contextPath}/jteap/wfengine/tasktodo/TaskToDoAction!releaseSignInAction.do";
	
	// 查看全厂
	var link5="${contextPath}/jteap/lp/lspgl/LspglAction!showQcAction.do";
	
	// 查看作废
	var link6="${contextPath}/jteap/lp/lspgl/LspglAction!showZfLspAction.do";
	
	// 作废工作票
	var link7="${contextPath}/jteap/lp/lspgl/LspglAction!cancelGzpAction.do";
	
	// 查看流程
	var link8="${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showUpdateAction.do";
	
	// model模式弹出页面
	var link9 = "${contextPath}/jteap/ModuleWindowForm.jsp";
	
	// 查看签收页面
	var link10 = "${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showViewAction.do";

	// 工作票票库查看页面
	var link11="${contextPath}/jteap/lp/lspgl/pkgl.jsp";

	// 撤销
	var link12="${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!cancelNodeAction.do";
	
	// 工作票票库显示列表
	var link13="${contextPath}/jteap/lp/pkgl/PkglAction!showListAction.do";
	
	// 根据流程配置内码获取最新版本流程配置
	var link14="${contextPath}/jteap/wfengine/workflow/FlowConfigAction!getFlowConfigIdByNm.do";
</script>