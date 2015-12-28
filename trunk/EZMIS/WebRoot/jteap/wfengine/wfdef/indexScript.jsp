<jteap:operations/>
<script>
	//显示流程分类树
	var link1="${contextPath}/jteap/wfengine/workflow/FlowCatalogAction!showTreeAction.do";
	
	//删除分类
	var link2="${contextPath}/jteap/wfengine/workflow/FlowCatalogAction!deleteNodeAction.do";
	
	//保存分类
	var link3="${contextPath}/jteap/wfengine/workflow/FlowCatalogAction!saveOrUpdateCatalogAction.do";
	
	//显示流程列表
	var link4="${contextPath}/jteap/wfengine/workflow/FlowConfigAction!showListAction.do";
	

	//发布流程配置
	var link7="${contextPath}/jteap/wfengine/workflow/WorkflowAction!deployWorkFlowAction.do";

	//删除流程列表
	var link5="${contextPath}/jteap/wfengine/workflow/FlowConfigAction!removeAction.do";
	

	//导出流程
	var link6 = "${contextPath}/jteap/wfengine/workflow/FlowConfigAction!exportFlowConfigXmlAction.do" ;
	
	
	//版本回退
	var link8="${contextPath}/jteap/wfengine/workflow/FlowConfigAction!moveBackAction.do";
	
	//验证工作流
	var link9="${contextPath}/jteap/wfengine/workflow/FlowConfigAction!validateFlowAction.do";
	
	//复制流程
	var link10="${contextPath}/jteap/wfengine/workflow/FlowConfigAction!copyFlowAction.do";
</script>
