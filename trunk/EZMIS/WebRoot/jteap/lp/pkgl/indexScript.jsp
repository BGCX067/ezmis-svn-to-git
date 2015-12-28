<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	// 票库管理显示列表
	var link1="${contextPath}/jteap/lp/pkgl/PkglAction!showListAction.do";
	
	// 创建部门树
	var link2="${contextPath}/jteap/lp/pkgl/GroupExtendForLpAction!showTreeAction.do";
	
	// 读取两票详细信息
	var link3="${contextPath}/jteap/lp/pkgl/PkglAction!showUpdateAction.do";
	
	// 保存两票详细信息
	var link4="${contextPath}/jteap/lp/pkgl/PkglAction!saveOrUpdateAction.do";
	
	// 两票信息编辑窗口
	var link5="${contextPath}/jteap/lp/pkgl/pkEditForm.jsp";
	
	// 显示关联两票表单选择树
	var link6="${contextPath}/jteap/lp/pkgl/FlowCatalogExtendForLpAction!showCatalogAndItemTreeAction.do";
	
	// 两票信息查看窗口
	var link7="${contextPath}/jteap/lp/pkgl/pkViewForm.jsp";
	
	// 删除计划
	var link8="${contextPath}/jteap/lp/pkgl/PkglAction!removeAction.do";
	
	// 生成标准票
	var link9="${contextPath}/jteap/lp/pkgl/PkglAction!makeBzpAction.do";
	
	// 验证两票内码
	var link10="${contextPath}/jteap/lp/pkgl/PkglAction!validateLpNmAction.do";
</script>
