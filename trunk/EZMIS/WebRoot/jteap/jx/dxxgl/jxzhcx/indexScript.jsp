<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
    // 查看机组检修计划列表
	var link1="${contextPath}/jteap/jx/dxxgl/JzjxjhAction!showListAction.do";
	
	// 查看检修计划窗口
	var link2="${contextPath}/jteap/jx/dxxgl/jzjxjh/jzjxjhViewForm.jsp";

	// 机组检修项目列表
	var link3="${contextPath}/jteap/jx/dxxgl/JzjxxmAction!showListAction.do";

	// 查看检修项目窗口
	var link4="${contextPath}/jteap/jx/dxxgl/jxzhcx/jzjxxmGrid.jsp";

	// 显示检修总结报告详细信息
	var link5="${contextPath}/jteap/jx/dxxgl/JxzjbgAction!showUpdateAction.do";

	// 下载报告附件
	var link6="${contextPath}/jteap/jx/dxxgl/JxzjbgAction!downloadFileAction.do";
	
	// 显示检修总结报告窗口
	var link7="${contextPath}/jteap/jx/dxxgl/jxzhcx/jxzjbgViewForm.jsp";
</script>