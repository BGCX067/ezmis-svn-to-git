<!-- 当前模块所有具有权限的操作 -->
<jteap:operations />
<script>
	// 显示合同分类树
	var link1="${contextPath}/jteap/gcht/htsp/HeTongCatalogAction!showTreeAction.do";

	// 查看全厂
	var link2="${contextPath}/jteap/gcht/htsp/HeTongShenPiAction!showQcHeTongAction.do?tableName=TB_HT_WZHT";
	
	// 查看签收页面
	var link3 = "${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showViewAction.do";
	
	// 部门树
	var link4="${contextPath}/jteap/lp/pkgl/GroupExtendForLpAction!showTreeAction.do";
	
	// 合同付款列表页面
	var link5="${contextPath}/jteap/gcht/zhcx/jbxxcx/wzhtcx/htFkGrid.jsp?tableName=TB_HT_WZHTFK";
	
	// 合同付款列表查询
	var link6="${contextPath}/jteap/gcht/htzx/HeTongFkShenPiAction!showQcHeTongAction.do";
	
	// model模式弹出页面
	var link7="${contextPath}/jteap/ModuleWindowForm.jsp";
	
	// 合同统计页面
	var link8="${contextPath}/jteap/gcht/zhcx/jbxxcx/wzhtcx/htntjForm.jsp";
	
	// 合同统计
	var link9="${contextPath}/jteap/gcht/zhcx/GchtZhcxAction!getZxntjAction.do";
</script>