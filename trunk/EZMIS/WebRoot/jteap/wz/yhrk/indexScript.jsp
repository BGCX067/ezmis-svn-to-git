<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	//验货单查询页面
	var link1="${contextPath}/jteap/wz/yhdmx/index.jsp";
	
	//验货单明细表
	var link2="${contextPath}/jteap/wz/yhdmx/YhdmxAction!showListAction.do?zt=2&lists=yhdmx";
	
	//保存分类
	var link3="${contextPath}/jteap/wz/yhdgl/YhdglCatalogAction!saveOrUpdateCatalogAction.do";
	
	//列表
	var link4="${contextPath}/jteap/wz/yhdgl/YhdglAction!showListAction.do?zt=2&limit=50&lists=yhd";
	
	//删除
	var link5="${contextPath}/jteap/wz/yhdgl/YhdglAction!removeAction.do";
	//删除
	var link15="${contextPath}/jteap/wz/yhdmx/YhdmxAction!removeAction.do";
	
	//添加修改功能
	var link6="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_YYHD";
	
	//验货单生效
	var link7="${contextPath}/jteap/wz/yhdgl/YhdglAction!enableRKAction.do";
	
	var link8="${contextPath}/jteap/wz/yhdgl/YhdglAction!showListAction.do?zt=2&limit=50";
	
</script>