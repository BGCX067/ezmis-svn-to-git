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
	var link4="${contextPath}/jteap/wz/cgjhmx/CgjhmxAction!showListAction.do";
	
	//删除
	var link5="${contextPath}/jteap/wz/cgjhmx/CgjhmxAction!removeAction.do";
	
	//添加修改功能
	var link6="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_YCGJH_VIEW";
	
	var link8="${contextPath}/jteap/wz/cgjhmx/CgjhmxAction!sumWzAction.do";	
	
	//组织结构树
	var link9="${contextPath}/jteap/wz/xqjhsq/GroupExtendForWzAction!showWzSqbmTreeAction.do";
</script>