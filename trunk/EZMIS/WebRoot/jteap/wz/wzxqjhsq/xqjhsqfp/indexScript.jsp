<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	// 查看待分配的物资申请
	var link1="${contextPath}/jteap/wz/xqjhsq/XqjhsqAction!showDfpXqjhAction.do";

	// 查看物资申请明细
	var link2="${contextPath}/jteap/wz/xqjhsq/XqjhsqDetailAction!showFpListAction.do";

	// 计划员用户
	var link3="${contextPath}/jteap/wz/xqjhsq/XqjhsqDetailAction!showPersons.do?type=0";


	// 处理物资申请分配
	var link4="${contextPath}/jteap/wz/xqjhsq/XqjhsqDetailAction!doFpXqjhsq.do";
	
	
	//添加与修改
	var link6="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_XQJHSQ";

	// 导出需求计划申请单
	var link9="${contextPath}/jteap/wz/xqjhsq/XqjhsqAction!exportXqjhsqAction.do";

	//更新需求计划申请明细数据到数据库
	var link11 = "${contextPath}/jteap/wz/xqjhsq/XqjhsqDetailAction!updateDataAction.do";
	
</script>