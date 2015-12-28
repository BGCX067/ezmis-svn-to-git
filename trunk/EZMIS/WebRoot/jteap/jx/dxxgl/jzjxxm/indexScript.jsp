<!-- 当前模块所有具有权限的操作 -->
<jteap:operations />
<script>
    // 机组检修计划树
	var link1="${contextPath}/jteap/jx/dxxgl/JzjxjhAction!showJzjxjhTreeAction.do";
	
	// 检修项目显示列表
	var link2="${contextPath}/jteap/jx/dxxgl/JzjxxmAction!showListAction.do";
	
	// 显示详细信息
	var link3="${contextPath}/jteap/jx/dxxgl/JzjxxmAction!showUpdateAction.do";
	
	// 保存详细信息
	var link4="${contextPath}/jteap/jx/dxxgl/JzjxxmAction!saveOrUpdateAction.do";
	
	// 机组检修项目编辑窗口
	var link5="${contextPath}/jteap/jx/dxxgl/jzjxxm/jzjxxmEditeForm.jsp";
	
	// 删除检修项目
	var link6="${contextPath}/jteap/jx/dxxgl/JzjxxmAction!removeAction.do";
	
	// 导出Excel
	var link7="${contextPath}/jteap/jx/dxxgl/JzjxxmAction!exportExcelAction.do";

	// 机组检修项目查看窗口
	var link8="${contextPath}/jteap/jx/dxxgl/jzjxxm/jzjxxmViewForm.jsp";
</script>