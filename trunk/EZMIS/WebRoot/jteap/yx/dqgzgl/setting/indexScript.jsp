<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>

<!-- 数据字典 -->
<jteap:dict catalog="dqgzgl,dqgzzy"></jteap:dict>

<script>
	
	//显示定期工作分类树
	var link1 = "${contextPath}/jteap/yx/dqgzgl/DqgzCatalogAction!showTreeAction.do";
	
	//删除定期工作分类
	var link2 = "${contextPath}/jteap/yx/dqgzgl/DqgzCatalogAction!deleteNodeAction.do";
	
	//保存定期工作分类
	var link3 = "${contextPath}/jteap/yx/dqgzgl/DqgzCatalogAction!saveOrUpdateCatalogAction.do";
	
	//获取所有定期工作分类 list
	var link11 = "${contextPath}/jteap/yx/dqgzgl/DqgzCatalogAction!findAllCatalogAction.do";
	
	//显示定期工作设置列表
	var link4 = "${contextPath}/jteap/yx/dqgzgl/DqgzSetAction!showListAction.do";
	
	//删除定期工作设置
	var link5 = "${contextPath}/jteap/yx/dqgzgl/DqgzSetAction!removeAction.do";
	
	//保存或修改定期工作设置
	var link6 = "${contextPath}/jteap/yx/dqgzgl/DqgzSetAction!saveOrUpdateAction.do";
	
	//修改定期工作设置
	var link9 = "${contextPath}/jteap/yx/dqgzgl/DqgzSetAction!showUpdateAction.do";
	
	//手工发送
	var link10 = "${contextPath}/jteap/yx/dqgzgl/DqgzSetAction!hanldSendAction.do";
	
	//【部门组织】树
	var link7 = "${contextPath}/jteap/system/group/GroupAction!showTreeAction.do";
	
	//【角色】树
	var link8 = "${contextPath}/jteap/system/role/RoleAction!showRoleTreeAction.do";
	
</script>