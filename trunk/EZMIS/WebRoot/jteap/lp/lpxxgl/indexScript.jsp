<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	//分类树
	var link1="${contextPath}/jteap/lp/lpxxgl/AqcsCatalogAction!showTreeAction.do";
	
	//删除分类
	var link2="${contextPath}/jteap/lp/lpxxgl/AqcsCatalogAction!deleteNodeAction.do";
	
	//保存分类
	var link3="${contextPath}/jteap/lp/lpxxgl/AqcsCatalogAction!simpleCreateCatalogAction.do";
	
	
	//列表
	var link4="${contextPath}/jteap/lp/lpxxgl/AqcsAction!showListAction.do";
	
	//删除
	var link5="${contextPath}/jteap/lp/lpxxgl/AqcsAction!removeAction.do";
	
	// 显示安全措施详细信息页面
	var link6="${contextPath}/jteap/lp/lpxxgl/aqcsEditForm.jsp";
	
	// 安全措施详细信息显示
	var link7="${contextPath}/jteap/lp/lpxxgl/AqcsAction!showUpdateAction.do";
	
	// 保存或修改功能
	var link8="${contextPath}/jteap/lp/lpxxgl/AqcsAction!saveOrUpdateAction.do";
	
	// 显示安全措施详细信息查看页面
	var link9="${contextPath}/jteap/lp/lpxxgl/aqcsViewForm.jsp";
	
</script>