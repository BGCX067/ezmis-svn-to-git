<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	// 显示列表
	var link1="${contextPath}/jteap/yx/tz/DqgzpAction!showListAction.do";
	
	// 新建/修改
	var link2="${contextPath}/jteap/yx/tz/dqgzp/dqgzpEditeForm.jsp";
	
	// 查看详细信息
	var link3="${contextPath}/jteap/yx/tz/dqgzp/dqgzpViewForm.jsp";
	
	// 删除
	var link4="${contextPath}/jteap/yx/tz/DqgzpAction!removeAction.do";
	
	// 详细信息初始化
	var link5="${contextPath}/jteap/yx/tz/DqgzpAction!showUpdateAction.do";
	
	// 详细信息保存
	var link6="${contextPath}/jteap/yx/tz/DqgzpAction!saveOrUpdateAction.do";
	
	// 角色对应人员下拉框
	var link7="${contextPath}/jteap/yx/tz/DqgzpAction!getGxRoleListAction.do"
	
	// 能完全修改记录的页面
	var link8="${contextPath}/jteap/yx/tz/dqgzp/dqgzpEditeAllForm.jsp";
</script>