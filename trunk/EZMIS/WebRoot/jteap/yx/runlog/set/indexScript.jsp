<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>

<script>
	
	//显示运行日志分类树
	var link1 = "${contextPath}/jteap/yx/runlog/LogsCatalogAction!showTreeAction.do";
	
	//删除运行日志分类
	var link2 = "${contextPath}/jteap/yx/runlog/LogsCatalogAction!deleteNodeAction.do";
	
	//保存运行日志分类
	var link3 = "${contextPath}/jteap/yx/runlog/LogsCatalogAction!saveOrUpdateCatalogAction.do";
	
	//显示报表列表
	var link4 = "${contextPath}/jteap/yx/runlog/LogsTableInfoAction!showListAction.do";
	
	//删除报表
	var link5 = "${contextPath}/jteap/yx/runlog/LogsTableInfoAction!removeAction.do";
	
	//保存或修改报表
	var link6 = "${contextPath}/jteap/yx/runlog/LogsTableInfoAction!saveOrUpdateAction.do";
	
	//显示单个报表
	var link7 = "${contextPath}/jteap/yx/runlog/LogsTableInfoAction!showUpdateAction.do";
	
	//显示指标列表
	var link8 = "${contextPath}/jteap/yx/runlog/LogsColumnInfoAction!showListAction.do";
	
	//删除指标列表
	var link9 = "${contextPath}/jteap/yx/runlog/LogsColumnInfoAction!removeAction.do";
	
	//保存或修改指标列表
	var link10 = "${contextPath}/jteap/yx/runlog/LogsColumnInfoAction!saveUpdateAction.do";
	
	//显示单个指标
	var link11 = "${contextPath}/jteap/yx/runlog/LogsColumnInfoAction!showUpdateAction.do";
	
	//根据表编号、采样点重建对应的记录物理表
	var link12 = "${contextPath}/jteap/yx/runlog/LogsTableInfoAction!rebuildPhysicLogTable.do";
	
</script>