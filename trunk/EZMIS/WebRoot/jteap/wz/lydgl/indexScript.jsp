<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	//分类树
	var link1="${contextPath}/jteap/wz/lydgl/LydglCatalogAction!showTreeAction.do";
	
	//领料单明细列表
	var link2="${contextPath}/jteap/wz/lydgl/LydmxAction!showListAction.do";
	//var link2="${contextPath}/jteap/wz/lydgl/LydmxAction!showListBySqlAction.do";
	
	//保存分类
	var link3="${contextPath}/jteap/wz/lydgl/LydglCatalogAction!saveOrUpdateCatalogAction.do";
	
	
	//(新系统领用单数据)列表
	//var link4="${contextPath}/jteap/wz/lydgl/LydglAction!showListAction.do";
	var link4="${contextPath}/jteap/wz/lydgl/LydglAction!showListBySqlAction.do";
	//删除领料单
	var link5="${contextPath}/jteap/wz/lydgl/LydglAction!removeAction.do";
	
	//删除领料单明细
	var link15="${contextPath}/jteap/wz/lydgl/LydmxAction!removeAction.do";
	//删除领料单明细
	var link16="${contextPath}/jteap/wz/lydgl/LydmxAction!rollBackAction.do";
	
	//添加修改功能
	//var link6="${contextPath}/jteap/wz/lydgl/LydglForm.jsp";
	var link6 = "${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_YLYD";
	//领用单生效
	var link7="${contextPath}/jteap/wz/lydgl/LydglAction!enableLydgl.do";
	
	//领料单列表
	var link8="${contextPath}/jteap/wz/lydgl/LydglAction!showSelectListBySqlAction.do";
	
	//(老系统领用单数据)列表
	var link9 = "${contextPath}/jteap/wz/lydgl/LydglAction!showHistoryListBySqlAction.do";
</script>