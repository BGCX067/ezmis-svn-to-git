<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<jteap:dict catalog="CGJHMX,CGFX"></jteap:dict>
<script>
	//分类树
	var link1="${contextPath}/jteap/wz/cgjhmx/CgjhmxCatalogAction!showTreeAction.do";
	
	//计划员列表
	var link2="${contextPath}/jteap/wz/cgjhmx/CgjhmxAction!showPersons.do?type=0";
	
	//采购员列表
	var link3="${contextPath}/jteap/wz/cgjhmx/CgjhmxAction!showPersons.do?type=1";
	
	//列表
	var link4="${contextPath}/jteap/wz/fjcldgl/FjcldAction!showListAction.do?zt=1"
	
	//删除
	var link5="${contextPath}/jteap/wz/cgjhmx/CgjhmxAction!removeAction.do";
	
	//添加修改功能
	var link6="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_YFJWZCLD_VIEW";
	
</script>