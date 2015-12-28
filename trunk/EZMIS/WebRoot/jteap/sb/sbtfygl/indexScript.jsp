<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>

<script>
	
	//显示设备评级分类树
	var link1 = "${contextPath}/jteap/sb/sbpjgl/SbpjCatalogAction!showTreeAction.do";
	
	//删除设备评级分类
	var link2 = "${contextPath}/jteap/sb/sbpjgl/SbpjCatalogAction!deleteNodeAction.do";
	
	//保存设备评级分类
	var link3 = "${contextPath}/jteap/sb/sbpjgl/SbpjCatalogAction!saveOrUpdateCatalogAction.do";

	//显示设备停复役列表
	var link4 = "${contextPath}/jteap/sb/sbtfygl/SbtfyxxAction!showListAction.do";
	
	//更新设备停复役信息
	var link5 = "${contextPath}/jteap/sb/sbtfygl/SbtfyxxAction!updateDataAction.do";
	
	//保存或修改设备停复役信息
	var link6 = "${contextPath}/jteap/sb/sbtfygl/SbtfyxxAction!saveOrUpdateAction.do";
	
</script>