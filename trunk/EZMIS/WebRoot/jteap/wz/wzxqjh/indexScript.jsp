<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	
	//需求计划处理列表
	var link1="${contextPath}/jteap/wz/xqjh/XqjhDetailAction!showXqjhDetailListAction.do";
	
	//数量分配情况(需求计划内容)
	var link2="${contextPath}/jteap/wz/xqjh/XqjhAction!showXqjhListAction.do";
	
	//数量分配情况(需求计划明细内容)
	var link3="${contextPath}/jteap/wz/xqjh/XqjhDetailAction!showXqjhDetailForSlfpListAction.do";
	
	//库存量调整
	var link4="${contextPath}/jteap/wz/xqjh/XqjhDetailAction!KclSettingAction.do";
	
	//获取指定流程实例
	var link5="${contextPath}/jteap/wz/xqjh/XqjhDetailAction!showProcessinstance.do";
	
	//需求计划生成采购计划(初始化列表)
	var link6="${contextPath}/jteap/wz/xqjh/XqjhDetailAction!showXqjhSccgjhListAction.do";
	
	//生成采购计划
	var link7="${contextPath}/jteap/wz/xqjh/XqjhDetailAction!sccgjhAction.do";
	
	// model模式弹出页面
	var link9 = "${contextPath}/jteap/ModuleWindowForm.jsp";
	
	// 查看签收页面
	var link10 = "${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showViewAction.do";

	//回退
	var link11 = "${contextPath}/jteap/wz/xqjh/XqjhDetailAction!RollbackAction.do";
	
</script>