
<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	//查看全厂、审批状态为'合同生效'、【合同审批】中的合同
	var link1="${contextPath}/jteap/gcht/htzx/HeTongZhongJieAction!showQcsxHeTongAction.do";
	
	//终结【合同审批】中的合同、【合同付款审批】中的关联合同
	var link2="${contextPath}/jteap/gcht/htzx/HeTongZhongJieAction!zhongJieHeTongAction.do";
	
	//展示合同专业分类树
	var link3 = "${contextPath}/jteap/gcht/htsp/HeTongCatalogAction!showTreeAction.do";
	
	//【部门组织】树
	var link16 = "${contextPath}/jteap/system/group/GroupAction!showTreeAction.do";
</script>