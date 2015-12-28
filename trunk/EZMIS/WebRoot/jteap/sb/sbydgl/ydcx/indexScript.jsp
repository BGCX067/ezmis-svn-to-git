<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>

<script>
	
	//显示设备台帐分类树
	var link1 = "${contextPath}/jteap/sb/sbtzgl/SbtzCatalogAction!showTreeAction.do";
	
	//删除设备台帐分类
	var link2 = "${contextPath}/jteap/sb/sbtzgl/SbtzCatalogAction!deleteNodeAction.do";
	
	//保存设备台帐分类
	var link3 = "${contextPath}/jteap/sb/sbtzgl/SbtzCatalogAction!saveOrUpdateCatalogAction.do";
	
	//显示设备台帐列表
	var link4 = "${contextPath}/jteap/sb/sbydgl/SbydxxAction!showListAction.do";
	
	//删除设备基础台帐
	var link5 = "${contextPath}/jteap/sb/sbydgl/SbydxxAction!removeAction.do";
	
	//保存或修改设备基础台帐
	var link6 = "${contextPath}/jteap/sb/sbydgl/SbydxxAction!saveOrUpdateAction.do";
	
	//修改设备基础台帐
	var link9 = "${contextPath}/jteap/sb/sbydgl/SbydxxAction!showUpdateAction.do";
	
	//获取指定流程实例
	var link10 = "${contextPath}/jteap/sb/sbydgl/SbydxxAction!showProcessinstance.do";
	
	// model模式弹出页面
	var link11 = "${contextPath}/jteap/ModuleWindowForm.jsp";
	
	// 查看签收页面
	var link12 = "${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showViewAction.do";
	
	
</script>