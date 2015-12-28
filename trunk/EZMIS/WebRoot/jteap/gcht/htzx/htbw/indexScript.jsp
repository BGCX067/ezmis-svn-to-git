
<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	//查看审批状态不为作废、终结,【合同审批】中的合同备忘
	var link1 = "${contextPath}/jteap/gcht/htzx/HeTongBeiWangAction!showQcHtbwAction.do";
	
	//展示合同专业分类树
	var link3 = "${contextPath}/jteap/gcht/htsp/HeTongCatalogAction!showTreeAction.do";
	
	//保存合同备忘信息
	var link4 = "${contextPath}/jteap/gcht/htzx/HeTongBeiWangAction!saveHtbwAction.do";
	
	//删除合同备忘信息
	var link5 = "${contextPath}/jteap/gcht/htzx/HeTongBeiWangAction!removeAction.do"; 
	
	//查看审批状态不为作废、终结的合同名称、合同编号
	var link6 = "${contextPath}/jteap/gcht/htzx/HeTongBeiWangAction!findAllHtmcAction.do";
	
</script>