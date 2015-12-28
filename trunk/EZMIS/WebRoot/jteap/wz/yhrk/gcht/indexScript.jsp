<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	//分类树
	var link1="${contextPath}/jteap/wz/yhdmx/YhdmxCatalogAction!showTreeAction.do";
	
	//仓库保管员列表
	var link2="${contextPath}/jteap/wz/cgjhmx/CgjhmxAction!showPersons.do?type=2";
	
	//保存分类
	var link3="${contextPath}/jteap/wz/yhdmx/YhdmxCatalogAction!saveOrUpdateCatalogAction.do";
	
	//列表
	var link4="${contextPath}/jteap/wz/yhdgl/WzhtAction!showListAction.do";
	
	//删除
	var link5="${contextPath}/jteap/wz/yhdmx/YhdmxAction!removeAction.do";
	
	//采购计划单打印
	var link9="${contextPath}/jteap/wz/yhdmx/YhdglAction!printCgjhglAction.do";	
	
	//添加修改功能
	//var link6="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_YYHD_RK_VIEW";
	var link6="${contextPath}/jteap/wz/wzhhcx/index.jsp";
	
	//打印
	var link7="${contextPath}/jteap/wz/yhdmx/YhdmxAction!yhdmxPrintAction.do";
</script>