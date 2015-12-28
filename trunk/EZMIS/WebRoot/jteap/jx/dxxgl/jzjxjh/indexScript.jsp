<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
    // 查看机组检修计划列表
	var link1="${contextPath}/jteap/jx/dxxgl/JzjxjhAction!showListAction.do";
	
	// 机组检修计划编辑窗口
	var link2="${contextPath}/jteap/jx/dxxgl/jzjxjh/jzjxjhEditeForm.jsp";
	
	// 修改编辑窗口，获得数据记录
	var link3="${contextPath}/jteap/jx/dxxgl/jzjxjh/JzjxjhAction!showUpdateAction.do";
	
	// 提交表单
	var link4="${contextPath}/jteap/jx/dxxgl/JzjxjhAction!saveOrUpdateAction.do";
	
	// 查看窗口
	var link5="${contextPath}/jteap/jx/dxxgl/jzjxjh/jzjxjhViewForm.jsp";
	
	// 下载地址
	var link6="${contextPath}/jteap/jx/dxxgl/JzjxjhAction!downloadFileAction.do";
	
	// 删除计划
	var link7="${contextPath}/jteap/jx/dxxgl/JzjxjhAction!removeAction.do";
</script>