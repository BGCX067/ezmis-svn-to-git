<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	//全部需求计划
	var link="${contextPath}/jteap/wz/xqjhsq/XqjhsqAction!showAllListAction.do";
	//草稿箱grid
	var link1="${contextPath}/jteap/wz/xqjhsq/XqjhsqAction!showCgxXqjhAction.do";

	// 查看当前登录人待处理的需求计划
	var link2="${contextPath}/jteap/wz/xqjhsq/XqjhsqAction!showDclXqjhAction.do";

	// 查看已处理需求计划
	var link3="${contextPath}/jteap/wz/xqjhsq/XqjhsqAction!showYclXqjhAction.do";

	// 查看作废的需求计划
	var link4="${contextPath}/jteap/wz/xqjhsq/XqjhsqAction!showZfXqjhAction.do";

	
	//添加与修改
	var link6="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_XQJHSQ";

	// 导出需求计划申请单
	//var link9="${contextPath}/jteap/wz/xqjhsq/XqjhsqAction!exportXqjhsqAction.do";
	
	
    /*****************流程相关查询*******************/
    // 释放签收
	var link5="${contextPath}/jteap/wfengine/tasktodo/TaskToDoAction!releaseSignInAction.do";
	
	// 作废需求计划申请单
	var link7="${contextPath}/jteap/wz/xqjhsq/XqjhsqAction!cancelXqjhsqAction.do";
	
	// 查看流程
	var link8="${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showUpdateAction.do";
	
	// model模式弹出页面
	var link9 = "${contextPath}/jteap/ModuleWindowForm.jsp";
	
	// 查看签收页面
	var link10 = "${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showViewAction.do";
    
    // 撤销
	var link12="${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!cancelNodeAction.do";
    
    
    /************************************************/
    
    
    
	
	
	

	
</script>