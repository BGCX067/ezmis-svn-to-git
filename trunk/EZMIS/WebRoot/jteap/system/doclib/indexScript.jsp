<%@page import="com.jteap.core.utils.StringUtil"%>

<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	//显示自定义表单分类树
	var link1="${contextPath}/jteap/doclib/DoclibCatalogAction!showTreeAction.do";
	if(${catalogid} != 'false') {
		link1="${contextPath}/jteap/doclib/DoclibCatalogAction!showTreeAction.do?catalogId="+${catalogid};
	}

	//删除分类
	var link2="${contextPath}/jteap/doclib/DoclibCatalogAction!deleteNodeAction.do";
	
	//保存分类
	var link3="${contextPath}/jteap/doclib/DoclibCatalogAction!saveOrUpdateCatalogAction.do";
	
	//显示自定义文档列表
	var link4="${contextPath}/jteap/doclib/DoclibAction!findDynaDataAction.do";
	
	//删除文档
	var link5="${contextPath}/jteap/doclib/DoclibAction!removeAction.do";
	
	//修改文档分类，加载数据
	var link6="${contextPath}/jteap/doclib/DoclibCatalogAction!showUpdateAction.do";
	
	//显示需要修改的扩展字段列表
	var link7="${contextPath}/jteap/doclib/DoclibCatalogAction!showUpdataCatalogFiledAction.do";
	
	//创建扩展字段                               
	var link8="${contextPath}/jteap/doclib/DoclibCatalogAction!saveUpdateAction.do";
	
	//删除扩展字段
	var link9="${contextPath}/jteap/doclib/DoclibCatalogFieldAction!removeAction.do";
	
	//创建文档
	var link10="${contextPath}/jteap/doclib/DoclibAction!saveUpdataAction.do";
	
	//显示扩展字段列表                                                      
	var link11="${contextPath}/jteap/doclib/DoclibAction!getDoclibFieldPropertiesAction.do";
	
	//初始化添加文档数据
	var link12="${contextPath}/jteap/doclib/DoclibAttachAction!showUpdateAction.do"
	
	//加载文档附件列表
	var link13="${contextPath}/jteap/doclib/DoclibAction!showUpdataAttachFileAction.do"
	
	var link14="${contextPath}/jteap/doclib/DoclibCatalogAction!checkCatalogCodeAction.do";
	
	//得到静态页面的地址
	var link15="${contextPath}/jteap/doclib/DoclibAction!getUrlAction.do";
	
	
	var link16="${contextPath}/jteap/doclib/DoclibCatalogAction!simpleDragMoveNodeAction.do";
</script>