<!-- 当前模块所有具有权限的操作 -->
<jteap:operations />
<script>
	// 供应商信息显示列表
	var link1="${contextPath}/jteap/gcht/gysgl/GysglAction!showGysxxAction.do";

	// 供应商分类树显示
	var link2="${contextPath}/jteap/gcht/gysgl/GysCatalogAction!showTreeAction.do";
	
	// 添加招标分类
	var link3="${contextPath}/jteap/gcht/gysgl/GysCatalogAction!addGysFlAction.do";
	
	// 删除招标分类
	var link4="${contextPath}/jteap/gcht/gysgl/GysCatalogAction!deleteNodeAction.do";
	
	//删除表单
	var link5 = "${contextPath}/jteap/form/eform/EFormAction!delEFormRecAction.do";
	
</script>