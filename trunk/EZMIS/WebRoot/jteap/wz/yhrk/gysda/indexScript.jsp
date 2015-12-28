<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	//分类树
	var link1="${contextPath}/jteap/wz/gysda/GysdaCatalogAction!showTreeAction.do";
	
	//删除分类
	var link2="${contextPath}/jteap/wz/gysda/GysdaCatalogAction!deleteNodeAction.do";
	
	//保存分类
	var link3="${contextPath}/jteap/wz/gysda/GysdaCatalogAction!saveOrUpdateCatalogAction.do";
	
	
	//列表
	var link4="${contextPath}/jteap/wz/gysda/GysdaAction!showListAction.do";
	
	//删除
	var link5="${contextPath}/jteap/wz/gysda/GysdaAction!removeAction.do";
	
	//添加与修改
	var link6="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_SGYSDA";
	
	//供应商供应额统计
	var link7="${contextPath}/jteap/wz/gysda/GysdaAction!getMoneySum.do";
	
</script>