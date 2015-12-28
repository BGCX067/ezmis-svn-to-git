<jteap:operations></jteap:operations>
<jteap:dict catalog="2222,bbbbb"></jteap:dict>
<script>
	//请求字典列表数据
	var link1="${contextPath}/jteap/system/dict/DictAction!showListAction.do";
	
	//请求字典类型树形结构
	var link2="${contextPath}/jteap/system/dict/DictCatalogAction!showTreeAction.do";
	
	//添加字典类型
	var link3="${contextPath}/jteap/system/dict/DictCatalogAction!createDictCatalogAction.do";
	
	//移动字典类型节点
	var link4="${contextPath}/jteap/system/dict/DictCatalogAction!simpleDragMoveNodeAction.do";
	
	//删除字典类型
	var link5="${contextPath}/jteap/system/dict/DictCatalogAction!deleteNodeAction.do";
	
	//修改字典类型，加载数据
	var link6="${contextPath}/jteap/system/dict/DictCatalogAction!showUpdateAction.do";
	
	//保存修改字典类型数据
	var link7="${contextPath}/jteap/system/dict/DictCatalogAction!saveUpdateAction.do";

	//添加数据字典
	var link8="${contextPath}/jteap/system/dict/DictAction!saveUpdateAction.do";
	
	//修改字典，加载数据
	var link9="${contextPath}/jteap/system/dict/DictAction!showUpdateAction.do";
	
	//删除字典
	var link10="${contextPath}/jteap/system/dict/DictAction!removeAction.do"

	//复制人员到指定组织
	var link11="${contextPath}/jteap/system/dict/DictAction!copyDictsToTheCatalogAction.do";
	
	//移动人员到指定组织
	var link12="${contextPath}/jteap/system/dict/DictAction!moveDictsToTheCatalogAction.do";
	//验证数据字典类型唯一性
	var link13="${contextPath}/jteap/system/dict/DictCatalogAction!validateCatalogUniqueAction.do";
</script>
