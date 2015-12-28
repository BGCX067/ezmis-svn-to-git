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
	var link4 = "${contextPath}/jteap/sb/sbtzgl/JctzAction!showListAction.do";
	
	//删除设备基础台帐
	var link5 = "${contextPath}/jteap/sb/sbtzgl/JctzAction!removeAction.do";
	
	//保存或修改设备基础台帐
	var link6 = "${contextPath}/jteap/sb/sbtzgl/JctzAction!saveOrUpdateAction.do";
	
	//修改设备基础台帐
	var link9 = "${contextPath}/jteap/sb/sbtzgl/JctzAction!showUpdateAction.do";
	
</script>