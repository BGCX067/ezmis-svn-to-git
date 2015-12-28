<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>

<script>
	
	//显示需求计划申请列表
	var link1 = "${contextPath}/jteap/wz/wzlysq/WzlysqxxAction!showListAction.do";
	
	//获取指定流程实例
	var link2="${contextPath}/jteap/wz/wzlysq/WzlysqxxAction!showProcessinstance.do";
	
	// model模式弹出页面
	var link3 = "${contextPath}/jteap/ModuleWindowForm.jsp";
	
	// 查看签收页面
	var link4 = "${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showViewAction.do";
	
	
</script>