<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	// 查看当前登录人的待审批合同
	var link1="${contextPath}/jteap/gcht/htsp/HeTongShenPiAction!showDspHeTongAction.do";

	// 查看已审批合同
	var link2="${contextPath}/jteap/gcht/htsp/HeTongShenPiAction!showYspHeTongAction.do";
	
	// 查看草稿箱
	var link3="${contextPath}/jteap/gcht/htsp/HeTongShenPiAction!showCgxHeTongAction.do";
	
	// 释放签收
	var link4="${contextPath}/jteap/wfengine/tasktodo/TaskToDoAction!releaseSignInAction.do";
	
	// 查看全厂
	var link5="${contextPath}/jteap/gcht/htsp/HeTongShenPiAction!showQcHeTongAction.do";
	
	// 查看作废
	var link6="${contextPath}/jteap/gcht/htsp/HeTongShenPiAction!showYzfHeTongAction.do";
	
	// 作废合同审批
	var link7="${contextPath}/jteap/gcht/htsp/HeTongShenPiAction!cancelHeTongAction.do";
	
	// 查看流程
	var link8="${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showUpdateAction.do";
	
	//删除合同表单数据
	var link9 = "${contextPath}/jteap/form/eform/EFormAction!delEFormRecAction.do";
	
	// 查看签收页面
	var link10 = "${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showViewAction.do";
	
	// 撤销
	var link12="${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!cancelNodeAction.do";
	
	//显示合同分类树
	var link13 = "${contextPath}/jteap/gcht/htsp/HeTongCatalogAction!showTreeAction.do";
	
	//删除合同分类
	var link14 = "${contextPath}/jteap/gcht/htsp/HeTongCatalogAction!deleteNodeAction.do";
	
	//保存合同分类
	var link15 = "${contextPath}/jteap/gcht/htsp/HeTongCatalogAction!saveOrUpdateCatalogAction.do";
	
	//【部门组织】树
	var link16 = "${contextPath}/jteap/system/group/GroupAction!showTreeAction.do";
	
	// 查看终结
	var link17="${contextPath}/jteap/gcht/htsp/HeTongShenPiAction!showYzjHeTongAction.do";
	
	// 选择已立项、合同审批为'合同生效'的项目
	var link18="${contextPath}/jteap/gcht/htsp/HeTongShenPiAction!chooseXmAction.do";
	
	
	//显示费用性质分类树
	var link19 = "${contextPath}/jteap/gcht/htsp/HeTongFyxzAction!showTreeAction.do";
	
	//保存费用性质分类
	var link20 = "${contextPath}/jteap/gcht/htsp/HeTongFyxzAction!saveOrUpdateCatalogAction.do";
	
	//删除费用性质分类
	var link21 = "${contextPath}/jteap/gcht/htsp/HeTongFyxzAction!deleteNodeAction.do";
	
	var link22 = "${contextPath}/jteap/gcht/htsp/HeTongFyxzAction!isZthAction.do";
	
</script>