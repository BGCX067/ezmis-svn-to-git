<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	//分类树
	var link1="${contextPath}/jteap/wz/lydgl/LydmxCatalogAction!showTreeAction.do";
	
	//删除分类
	var link2="${contextPath}/jteap/wz/lydgl/LydmxCatalogAction!deleteNodeAction.do";
	
	//保存分类
	var link3="${contextPath}/jteap/wz/lydgl/LydmxCatalogAction!saveOrUpdateCatalogAction.do";
	
	
	//列表
	var link4="${contextPath}/jteap/wz/lydgl/LydmxAction!showListAction.do";
	
	//删除
	var link5="${contextPath}/jteap/wz/lydgl/LydmxAction!removeAction.do";
	
	//添加修改功能
	var link6="${contextPath}/jteap/wz/lydgl/LydmxForm.jsp";
	
</script>