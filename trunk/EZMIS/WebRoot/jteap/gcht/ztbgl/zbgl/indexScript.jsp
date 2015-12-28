<!-- 当前模块所有具有权限的操作 -->
<jteap:operations />
<script>
	// 招标信息显示列表
	var link1="${contextPath}/jteap/gcht/ztbgl/ZtbglAction!showZbxxAction.do";

	// 招标分类树显示
	var link2="${contextPath}/jteap/gcht/ztbgl/ZtbCatalogAction!showTreeAction.do";
	
	// 添加招标分类
	var link3="${contextPath}/jteap/gcht/ztbgl/ZtbCatalogAction!addZtbFlAction.do";
	
	// 删除招标分类
	var link4="${contextPath}/jteap/gcht/ztbgl/ZtbCatalogAction!deleteNodeAction.do";
	
	//删除表单
	var link5 = "${contextPath}/jteap/form/eform/EFormAction!delEFormRecAction.do";
	
</script>