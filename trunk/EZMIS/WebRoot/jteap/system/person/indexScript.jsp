<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>
 
<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>

<%
//为了实现分级权限
PersonManager personManager=(PersonManager)SpringContextUtil.getBean("personManager");
//isRoot & curPersonId is defined in import.jsp
String adminGroupIds=isRoot?"":personManager.findAdminGroupIdsOfThePerson(curPersonId);

%>
<script>
	var adminGroupIds="<%=adminGroupIds%>";
	//请求人员列表数据
	var link1="${contextPath}/jteap/system/person/P2GAction!showListAction.do";
	
	//请求组织树形结构
	var link2="${contextPath}/jteap/system/group/GroupAction!showTreeAction.do";
	
	//简单添加组织
	var link3="${contextPath}/jteap/system/group/GroupAction!simpleCreateGroupAction.do";
	
	//移动组织节点
	var link4="${contextPath}/jteap/system/group/GroupAction!simpleDragMoveNodeAction.do";
	
	//删除组织
	var link5="${contextPath}//jteap/system/group/GroupAction!deleteNodeAction.do";
	
	//修改组织，加载数据
	var link6="${contextPath}/jteap/system/group/GroupAction!showUpdateAction.do";
	
	//保存修改组织数据
	var link7="${contextPath}/jteap/system/group/GroupAction!saveUpdateAction.do";
	
	//取得组织管理人员
	var link8="${contextPath}/jteap/system/group/GroupAction!getAdminPersonAction.do";
	
	//新增管理员
	var link9="${contextPath}/jteap/system/person/P2GAction!addAdminToGroupAction.do";
	
	//废除管理员
	var link10="${contextPath}/jteap/system/person/P2GAction!removeAdminFromGroupAction.do";
	
	//游离用户列表
	var link11="${contextPath}/jteap/system/person/PersonAction!showDissciationPersonListAction.do";
	//取得所有用户列表的时候
	var link12="${contextPath}/jteap/system/person/PersonAction!showListAction.do";
	
	//复制人员到指定组织
	var link13="${contextPath}/jteap/system/person/P2GAction!copyPersonsToTheGroupAction.do";
	//移动人员到指定组织
	var link14="${contextPath}/jteap/system/person/P2GAction!movePersonsToTheGroupAction.do";
	//为组织创建人员
    var link15="${contextPath}/jteap/system/person/P2GAction!addPersonToTheGroupAction.do";
    //请求角色树
    var link16="${contextPath}/jteap/system/role/RoleAction!showRoleTreeForCheckAction.do";
    //修改人员
    var link17="${contextPath}/jteap/system/person/PersonAction!showModifyPersonAction.do";
    //请求选中了的角色树
    var link18="${contextPath}/jteap/system/role/RoleAction!showRloeTreeForIsCheckAction.do";
    //请求选中的组织树
    var link19="${contextPath}/jteap/system/group/GroupAction!showGroupTreeForIsCheckAction.do";
    //更新组织人员
    var link20="${contextPath}/jteap/system/person/P2GAction!updatePersonAction.do";
    //删除组织人员
    var link21="${contextPath}/jteap/system/person/P2GAction!batchDelPersonToTheGroupAction.do";
    //移去用户
    var link22="${contextPath}/jteap/system/person/P2GAction!removePersonFromGroupAction.do";
	//人员详细信息中我的组织
	var link23="${contextPath}/jteap/system/person/PersonAction!showDetailPersonAction.do";
	//初始化用户密码
	var link24="${contextPath}/jteap/system/person/PersonAction!initPersonPassWordAction.do";
	//用户属性配置
	var link25="${contextPath}/jteap/config/person-config.xml";
	//用户属性配置
	var link26="${contextPath}/jteap/system/person/PersonAction!lockPersonAction.do";
	//用户解锁
	var link27="${contextPath}/jteap/system/person/PersonAction!moveLockPersonAction.do";
	//验证用户名称是否已经存在
	var link28="${contextPath}/jteap/system/person/PersonAction!validateUserNameUniqueAction.do";
	//批量设定管理员
	 var link29="${contextPath}/jteap/system/person/P2GAction!setupAdminForGroupAction.do";
	 //批量设定角色
	 var link30="${contextPath}/jteap/system/person/P2RoleAction!setupRoleForPersonAction.do";
	 
	 //显示所有资源
	 var link31="${contextPath}/jteap/system/resource/ResourceAction!showAllResourcesForCheck.do";
	 //为用户赋权
	 var link32="${contextPath}/jteap/system/person/PersonAction!setupResourceOfThePersonAction.do";
	 //清除用户权限
	 var link33="${contextPath}/jteap/system/person/PersonAction!clearResourceOfThePersonAction.do";
	 
	 //显示权限树
	 var link34="${contextPath}/jteap/system/dataperm/DatapermCatalogAction!showTreeAndCheckedAction.do";
	 
	 var link35="${contextPath}/jteap/system/dataperm/DatapermAction!saveDatapermAndPersonAction.do";
	 //显示更多属性页面
	 var link36="${contextPath}/jteap/system/person/MoreProperties.jsp";
	 //初始化附件信息
	 var link37="${contextPath}/jteap/system/person/PersonAction!showAccessoriesInfo.do";
	 //保存更多属性信息
	 var link38="${contextPath}/jteap/system/person/PersonAction!saveOrUpdateAction.do";
	 //显示图像
	 var link39="${contextPath}/jteap/system/person/PersonAction!showPersonIconAction.do";
	 //判断图像是否存在
	 var link40="${contextPath}/jteap/system/person/PersonAction!isExistIconAction.do";
	 //初始化页面信息
	 var link41="${contextPath}/jteap/system/person/PersonAction!initPersonAccessoriesAction.do";
	 //下载附件
	 var link42="${contextPath}/jteap/system/person/PersonAction!downAccessoriesAction.do";
	 //删除附件
	 var link43="${contextPath}/jteap/system/person/PersonAction!deleteAccessoriesAction.do";
	 
	 // 验证工号唯一性
	 var link44="${contextPath}/jteap/system/person/PersonAction!validateUserLoginName2Action.do";
	 
	 // 验证组织唯一性
	 var link45="${contextPath}/jteap/system/group/GroupAction!validateGroupSnUniqueAction.do";
	 //人员选择界面
	 var link46="${contextPath}/jteap/system/person/personSelect.jsp";
</script>