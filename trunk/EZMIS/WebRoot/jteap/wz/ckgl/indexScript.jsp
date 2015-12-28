<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	//分类树
	var link1="${contextPath}/jteap/wz/ckgl/CkglCatalogAction!showTreeAction.do";
	
	//删除分类
	var link2="${contextPath}/jteap/wz/ckgl/CkglCatalogAction!deleteNodeAction.do";
	
	//保存分类
	var link3="${contextPath}/jteap/wz/ckgl/CkglCatalogAction!saveOrUpdateCatalogAction.do";
	
	
	//列表
	var link4="${contextPath}/jteap/wz/ckgl/CkglAction!showListAction.do";
	
	//删除
	var link5="${contextPath}/jteap/wz/ckgl/CkglAction!removeAction.do";
	
	//添加与修改
	var link6="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_CKGL";
	
</script>