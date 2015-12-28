<script>
	//注销链接
	var link1="${contextPath}/LogoutAction.do";
	//验证当前密码是否正确
	var link2="${contextPath}/jteap/system/person/PersonAction!validateNameAndPasswordAction.do?userLoginName="+curPersonLoginName;
	//修改密码
	var link3="${contextPath}/jteap/system/person/PersonAction!changePasswordAction.do?userLoginName="+curPersonLoginName;
	
	//请求模块树形结构
	var link4="${contextPath}/jteap/system/resource/ModuleAction!showFunctionTreeAction.do";

	//修改当前用户信息
	var link5="${contextPath}/jteap/system/person/PersonAction!showModifyCurrPersonAction.do";
	
	//更新当前人员信息
    var link6="${contextPath}/jteap/system/person/PersonAction!updateCurrPersonAction.do";
    
    //用户属性配置
	var link7="${contextPath}/jteap/config/person-config.xml";
	
	//请求个性化配置，加载个人配置信息
	var loadPerLink = "${contextPath}/jteap/index/PreferenceAction!loadPreference.do";
	
	// 单点登录处理
	var link8="${contextPath}/jteap/sso/SsoSessionAction!ssoLogin.do";
	
	// LDAP同步
	var link9="${contextPath}/jteap/ldap/LdapAction!LdapSynchronizationAction.do";
	
	// 清除二级缓存
	var link10="${contextPath}/jteap/system/SystemFuncAction!clearHibernateL2CacheAction.do";
	
	// 获得快速链接
	var link11="${contextPath}/jteap/system/resource/ModuleAction!getQuickLinkAction.do";

	// 添加快速链接
	var link12="${contextPath}/jteap/system/resource/ResourcesUsersAction!setupQuickLinkAction.do";
	
	//初始化功能树所有父节点
	var link13 ="${contextPath}/jteap/system/resource/ModuleAction!showTreeParentNodeAction.do";
	
	//跳转具体子系统页面
	//var link14 ="${contextPath}/jteap/system/resource/ModuleAction!showModuleIndexAction.do?moduleId="+moduleId;
	
	//保存个人的页面布局配置
	var link15 = "${contextPath}/jteap/index/PreferenceAction!savePreferenceAction.do";
	
</script>


