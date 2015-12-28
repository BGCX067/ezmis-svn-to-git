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
	var link4="${contextPath}/jteap/wz/kcpd/PddMxAction!showListAction.do?zt=1";
	
	//删除
	var link5="${contextPath}/jteap/wz/cgjhmx/CgjhmxAction!removeAction.do";
	
	var link6="${contextPath}/jteap/wz/wzhhcx/index.jsp";
	
	//仓库列表
	var link10="${contextPath}/jteap/wz/ckgl/CkglAction!showListAction.do";
	
</script>