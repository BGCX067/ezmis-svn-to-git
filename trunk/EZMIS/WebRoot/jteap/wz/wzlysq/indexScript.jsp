<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	//全部领用申请
	var link="${contextPath}/jteap/wz/wzlysq/wzlysqAction!showAllListAction.do";
	//草稿箱grid
	var link1="${contextPath}/jteap/wz/wzlysq/wzlysqAction!showCgxAction.do";

	// 查看当前登录人待处理的领用申请
	var link2="${contextPath}/jteap/wz/wzlysq/wzlysqAction!showDclAction.do";

	// 查看已处理领用申请
	var link3="${contextPath}/jteap/wz/wzlysq/wzlysqAction!showYclAction.do";

	// 查看作废的领用申请
	var link4="${contextPath}/jteap/wz/wzlysq/wzlysqAction!showZfAction.do";

	
	//添加与修改
	var link6="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_YLYSQFM";

	// 导出领用申请单
	//var link9="${contextPath}/jteap/wz/xqjhsq/XqjhsqAction!exportXqjhsqAction.do";
	
	
    /*****************流程相关查询*******************/
    // 释放签收
	var link5="${contextPath}/jteap/wfengine/tasktodo/TaskToDoAction!releaseSignInAction.do";
	
	// 作废需求计划申请单
	var link7="${contextPath}/jteap/wz/wzlysq/wzlysqAction!cancelWzlyAction.do";
	
	// 查看流程
	var link8="${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showUpdateAction.do";
	
	// model模式弹出页面
	var link9 = "${contextPath}/jteap/ModuleWindowForm.jsp";
	
	// 查看签收页面
	var link10 = "${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showViewAction.do";
    
    // 撤销
	var link12="${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!cancelNodeAction.do";
    
    //选择领用申请
    //var link13="${contextPath}/jteap/wz/cgjhgl/CgjhglAction!showListAction.do?showType=1";
    var link13="${contextPath}/jteap/wz/wzlysq/XqjhSelectAction!showXQJHListAction.do";
    
    //领用申请明细
    var link14="${contextPath}/jteap/wz/wzlysq/XqjhSelectAction!showXQJHMXListAction.do";
    /************************************************/
    
    var link15 = "${contextPath}/jteap/wz/wzlb/WzlbAction!showTreeAction.do";
	
	var link16 = "${contextPath}/jteap/wz/wzda/WzdaAction!showListAction.do";
	
	// 计划员用户
	var link17="${contextPath}/jteap/wz/xqjhsq/XqjhsqDetailAction!showPersons.do?type=0";
	
</script>