<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	// 查看已批准的物资申请
	var link1="${contextPath}/jteap/wz/xqjhsq/XqjhsqAction!showDsxXqjhAction.do";

	// 查看物资申请明细
	var link2="${contextPath}/jteap/wz/xqjhsq/XqjhsqDetailAction!showSxListAction.do";

	// 计划员用户
	var link3="${contextPath}/jteap/wz/xqjhsq/XqjhsqDetailAction!showPersons.do?type=0";


	// 物资需求计划生效
	var link4="${contextPath}/jteap/wz/xqjhsq/XqjhsqDetailAction!doSxXqjhsq.do";
	
	// 回退至主管
	var link5="${contextPath}/jteap/wz/xqjhsq/XqjhsqDetailAction!doCallBackToFp.do";
	
	//添加与修改
	var link6="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_XQJHSQ";


	//新物资处理
	var link7="${contextPath}/jteap/wz/xqjhsq/XqjhsqDetailAction!doHandleWz.do";
	

	// 导出需求计划申请单
	var link9="${contextPath}/jteap/wz/xqjhsq/XqjhsqDetailAction!exportXqjhsqSxDetailAction.do";
	
	//更新需求计划申请生效明细数据到数据库
	var link13 = "${contextPath}/jteap/wz/xqjhsq/XqjhsqDetailAction!updateXqjhsqSxDetailDataAction.do";
	
	//获取指定流程实例
	var link15="${contextPath}/jteap/wz/xqjh/XqjhDetailAction!showProcessinstance.do";
	
	// model模式弹出页面
	var link16 = "${contextPath}/jteap/ModuleWindowForm.jsp";
	
	// 查看签收页面
	var link17 = "${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showViewAction.do";
	

	
</script>