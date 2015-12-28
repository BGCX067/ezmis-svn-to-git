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
	var link4="${contextPath}/jteap/wz/yhdmx/YhdmxAction!showListAction.do";
	
	//删除
	var link5="${contextPath}/jteap/wz/yhdmx/YhdmxAction!removeAction.do";
	
	//添加修改功能
	var link6="${contextPath}/jteap/wz/yhdmx/YhdmxForm.jsp";
	
	var link8="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_YYHD_VIEW";
	
	var link10="${contextPath}/jteap/wz/ckgl/CkglAction!showListAction.do";
</script>