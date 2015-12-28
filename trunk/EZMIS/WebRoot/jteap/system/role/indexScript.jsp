<jteap:operations/>
<script>
	
	//移动角色节点
	var link1="${contextPath}/jteap/system/role/RoleAction!simpleDragMoveNodeAction.do";
	
	//请求角色树形结构
	var link2="${contextPath}/jteap/system/role/RoleAction!showRoleTreeAction.do";
	//添加角色
	var link3="${contextPath}/jteap/system/role/RoleAction!simpleCreateRoleAction.do";
	
	//删除角色
	var link5="${contextPath}/jteap/system/role/RoleAction!deleteNodeAction.do";
	//修改角色
	var link6="${contextPath}/jteap/system/role/RoleAction!showUpdateAction.do";
	
	//保存修改角色数据
	var link7="${contextPath}/jteap/system/role/RoleAction!saveUpdateAction.do";
	
	//父角色可被继承的资源树
	//var link8="${contextPath}/jteap/system/resource/ResourceAction!showResoucesAction.do?roleId=";
	//给角色指定资源
	//var link9="${contextPath}/jteap/system/resource/ModuleAction!showTreeAction.do";
	
	//显示指定角色的详细内容
	var link10="${contextPath}/jteap/system/role/RoleAction!showRoleDetailAction.do";
	
	 //显示角色指定资源
	 var link11="${contextPath}/jteap/system/resource/ResourceAction!showResoucesDetailAction.do?roleId=";
	 
	 //显示指定角色所拥有所有资源的树形数据
	 var link12="${contextPath}/jteap/system/role/R2RAction!showRoleResourceAction.do";
	 
	 //显示所有资源用于勾选
	 var link13="${contextPath}/jteap/system/resource/ResourceAction!showAllResourcesForCheck.do";
	 
	 //为角色指定资源
	 var link14="${contextPath}/jteap/system/role/R2RAction!indicatResourceForRoleAction.do";
	 
	 //设定资源的可传播性
	 var link15="${contextPath}/jteap/system/role/R2RAction!setR2RCommunicableAction.do";
	 
	 //可传播的资源
	 var link16="${contextPath}/jteap/system/role/R2RAction!showRoleCommunicableResourceAction.do";
	 
	 //为角色继承资源
	 var link17="${contextPath}/jteap/system/role/R2RAction!extendResourceForRoleAction.do";
	  //取得所有用户列表的时候
	var link18="${contextPath}/jteap/system/person/P2RoleAction!showListAction.do";
	
	 //显示权限树
	 var link19="${contextPath}/jteap/system/dataperm/DatapermCatalogAction!showTreeAndCheckedRoleAction.do";
	 var link20="${contextPath}/jteap/system/dataperm/DatapermAction!saveDatapermAndRoleAction.do";
	 
	//人员详细信息中我的组织
	var link23="${contextPath}/jteap/system/person/PersonAction!showDetailPersonAction.do";
	
	// 显示人员选择窗口
	var link24="${contextPath}/jteap/system/person/personSelect.jsp";
	
	// 角色添加用户
	var link25="${contextPath}/jteap/system/person/P2RoleAction!addPerson2RoleAction.do";
	
	// 角色移除用户
	var link26="${contextPath}/jteap/system/person/P2RoleAction!removePersonFromRoleAction.do"
	
	//用户属性配置
	var link27="${contextPath}/jteap/config/person-config.xml";
	
	//请求组织树形结构
	var link28="${contextPath}/jteap/system/group/GroupAction!showTreeAction.do";
	
	//请求角色树
    var link29="${contextPath}/jteap/system/role/RoleAction!showRoleTreeForCheckAction.do";
    
	//验证用户名称是否已经存在
	var link30="${contextPath}/jteap/system/person/PersonAction!validateUserNameUniqueAction.do";
	
	//为组织创建人员
    var link31="${contextPath}/jteap/system/person/P2GAction!addPersonToTheGroupAction.do";
    // 验证工号唯一性
	var link32="${contextPath}/jteap/system/person/PersonAction!validateUserLoginName2Action.do";
	
	// 验证角色唯一性
	var link33="${contextPath}/jteap/system/role/RoleAction!validateRoleSnUniqueAction.do";
</script>