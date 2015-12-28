<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>

<!-- 数据字典 -->
<jteap:dict catalog="dqgzgl,dqgzzy,dqgzfl"></jteap:dict>

<script>
	
	//显示定期工作统计报表
	var link1 = "${contextPath}/jteap/yx/dqgzgl/DqgzStatisAction!yueTongJiAction.do";
	
	//显示相关专业详细定期工作统计报表
	var link2 = "${contextPath}/jteap/yx/dqgzgl/DqgzHanleAction!showListAction.do";
	
	//【部门组织】树
	var link7 = "${contextPath}/jteap/system/group/GroupAction!showTreeAction.do";
	
	//【角色】树
	var link8 = "${contextPath}/jteap/system/role/RoleAction!showRoleTreeAction.do";
	
</script>