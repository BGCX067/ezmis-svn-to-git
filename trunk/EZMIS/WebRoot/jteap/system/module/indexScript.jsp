
<jteap:operations></jteap:operations>
<script>
	
	//请求模块树形结构
	var link1="${contextPath}/jteap/system/resource/ModuleAction!showTreeAction.do";
	
	//移动模块节点
	var link2="${contextPath}/jteap/system/resource/ModuleAction!simpleDragMoveNodeAction.do";
	
	//删除模块
	var link3="${contextPath}/jteap/system/resource/ModuleAction!deleteNodeAction.do";
	
	//修改模块，加载数据
	var link4="${contextPath}/jteap/system/resource/ModuleAction!showUpdateAction.do";
	
	//保存修改模块数据
	var link5="${contextPath}/jteap/system/resource/ModuleAction!saveUpdateAction.do";
	
	//资源列表
	var link6="${contextPath}/jteap/system/resource/ModuleAction!getModuleDetailAction.do";
	
	//操作列表
	var link7="${contextPath}/jteap/system/resource/OperationAction!showOperationListAction.do";
	
	//添加资源
	var link8="${contextPath}/jteap/system/resource/ResourceAction!addModuleAction.do";
	
	//模块配置文件
	var link9="${contextPath}/jteap/config/modules-config.xml";
	
	//创建模块
	var link11="${contextPath}/jteap/system/resource/ModuleAction!saveUpdateAction.do";
	
    //模块树形结构,带权限
    var link12="${contextPath}/jteap/system/resource/ModuleAction!showFunctionTreeAction.do";
    
    //系统模块名称个性化保存
    var link13="${contextPath}/jteap/system/resource/ResourcesUsersAction!updateSysModuleAction.do";
    
    // 自定义模块保存
    var link14="${contextPath}/jteap/system/resource/ModuleAction!saveUpdateIndividualAction.do"
    
    // 恢复系统默认值
    var link15="${contextPath}/jteap/system/resource/ResourcesUsersAction!resetAction.do";
    
    // 显示带checkbox的模块
    var link16="${contextPath}/jteap/system/resource/ModuleAction!showTreeForCheckAction.do";
    
    // 批量添加快速链接
    var link17="${contextPath}/jteap/system/resource/ResourcesUsersAction!setupQuickLinkAction.do";
    
    // 批量添加文档链接
    var link18="${contextPath}/jteap/system/resource/ResourcesDocsAction!setupDocsLinkAction.do";

    // 显示带checkbox的文档树
    var link19="${contextPath}/jteap/doclib/DoclibCatalogAction!showTreeForCheckAction.do";
</script>