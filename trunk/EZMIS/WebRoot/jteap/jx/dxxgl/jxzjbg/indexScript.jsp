<!-- 当前模块所有具有权限的操作 -->
<jteap:operations />
<script>
    // 机组检修计划树
	var link1="${contextPath}/jteap/jx/dxxgl/JzjxjhAction!showJzjxjhTreeAction.do";
	
	// 显示检修总结报告详细信息
	var link2="${contextPath}/jteap/jx/dxxgl/JxzjbgAction!showUpdateAction.do";
	
	// 保存表单
	var link3="${contextPath}/jteap/jx/dxxgl/JxzjbgAction!saveOrUpdateAction.do";
	
	// 下载地址
	var link4="${contextPath}/jteap/jx/dxxgl/JxzjbgAction!downloadFileAction.do";
	
	// 删除报告
	var link5="${contextPath}/jteap/jx/dxxgl/JxzjbgAction!removeAction.do";
	
	// 获得检修计划信息
	var link6="${contextPath}/jteap/jx/dxxgl/JzjxjhAction!showUpdateAction.do";
</script>