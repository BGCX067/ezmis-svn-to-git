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
	var link4="${contextPath}/jteap/wz/lydgl/LydmxAction!showListAction.do";
	
	//删除
	var link5="${contextPath}/jteap/wz/cgjhmx/CgjhmxAction!removeAction.do";
	
	//添加修改功能
	var link6 = "${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_YLYD_VIEW";
	
	//列表
	var link7="${contextPath}/jteap/wz/lydgl/LydmxAction!exportSelectedExcelAction.do";
	
	//仓库列表
	var link10="${contextPath}/jteap/wz/ckgl/CkglAction!showListAction.do";
	
	//保管员
	var link12="${contextPath}/jteap/wz/cgjhmx/CgjhmxAction!showPersons.do?type=2";
	
	//组织结构树
	var link13="${contextPath}/jteap/wz/xqjhsq/GroupExtendForWzAction!showWzSqbmTreeAction.do";
	var link14 = "${contextPath}/jteap/wz/wzhhcx/index.jsp";
	var link15 = "${contextPath}/jteap/wz/lydgl/LydmxAction!updateGcxmAction.do";
</script>