<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	// 查看当前登录人的待审批项目
	var link1="${contextPath}/jteap/gcht/gcxmgl/GclxAction!showDspHeTongAction.do";

	// 查看已审批项目
	var link2="${contextPath}/jteap/gcht/gcxmgl/GclxAction!showYspHeTongAction.do";
	
	// 查看草稿箱
	var link3="${contextPath}/jteap/gcht/gcxmgl/GclxAction!showCgxHeTongAction.do";
	
	// 释放签收
	var link4="${contextPath}/jteap/wfengine/tasktodo/TaskToDoAction!releaseSignInAction.do";
	
	// 查看全厂
	var link5="${contextPath}/jteap/gcht/gcxmgl/GclxAction!showQcHeTongAction.do";
	
	// 查看作废
	var link6="${contextPath}/jteap/gcht/gcxmgl/GclxAction!showYzfHeTongAction.do";
	
	// 作废项目审批
	var link7="${contextPath}/jteap/gcht/gcxmgl/GclxAction!cancelHeTongAction.do";
	
	// 查看流程
	var link8="${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showUpdateAction.do";
	
	//删除项目表单数据
	var link9 = "${contextPath}/jteap/form/eform/EFormAction!delEFormRecAction.do";
	
	// 查看签收页面
	var link10 = "${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showViewAction.do";
	
	// 撤销
	var link12="${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!cancelNodeAction.do";
	
	//显示项目分类树
	var link13 = "${contextPath}/jteap/gcht/htsp/HeTongCatalogAction!showTreeAction.do";
	
	//删除项目分类
	var link14 = "${contextPath}/jteap/gcht/htsp/HeTongCatalogAction!deleteNodeAction.do";
	
	//保存项目分类
	var link15 = "${contextPath}/jteap/gcht/htsp/HeTongCatalogAction!saveOrUpdateCatalogAction.do";
	
	//【部门组织】树
	var link16 = "${contextPath}/jteap/system/group/GroupAction!showTreeAction.do";
	
	// 查看终结
	var link17="${contextPath}/jteap/gcht/gcxmgl/GclxAction!showYzjHeTongAction.do";
</script>