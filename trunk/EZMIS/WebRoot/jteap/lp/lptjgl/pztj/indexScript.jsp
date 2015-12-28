<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	// 按票种统计显示列表
	var link1="${contextPath}/jteap/lp/lptjgl/LptjglAction!showPztjAction.do";
	
	// 具体部门两票显示列表
	var link2="${contextPath}/jteap/lp/lpzhcx/LpzhcxAction!showListAction.do";
	
	// 两票票面查看,获得流程ID
	var link3="${contextPath}/jteap/lp/lpsh/LpshAction!getPidAction.do";
	
	// model模式弹出页面
	var link4 = "${contextPath}/jteap/ModuleWindowForm.jsp";
	
	// 查看具体部门两票页面
	var link5="${contextPath}/jteap/lp/lptjgl/pztj/pzcx.jsp";
	
	// 查看签收页面
	var link6 = "${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showViewAction.do";
</script>
