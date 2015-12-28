<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	// 部门两票显示列表
	var link1="${contextPath}/jteap/lp/lptjgl/LptjglAction!showBmLpListAction.do?isBm=false";
	
	// 查询面板 班组下拉树
	var link2="${contextPath}/jteap/lp/pkgl/GroupExtendForLpAction!showGzpTreeAction.do";

	// 两票票面查看,获得流程ID
	var link3="${contextPath}/jteap/lp/lpsh/LpshAction!getPidAction.do";
	
	// model模式弹出页面
	var link4 = "${contextPath}/jteap/ModuleWindowForm.jsp";
	
	// 查看具体班组两票页面
	var link5="${contextPath}/jteap/lp/lptjgl/bzlptj/bzlpcx.jsp";
	
	// 具体部门两票显示列表
	var link6="${contextPath}/jteap/lp/lpzhcx/LpzhcxAction!showListAction.do";
		
	// 查看签收页面
	var link7 = "${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showViewAction.do";
</script>
