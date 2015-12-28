<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	//验货单查询页面
	var link1="${contextPath}/jteap/wz/yhdmx/index.jsp";
	
	//验货单明细表
	var link2="${contextPath}/jteap/wz/yhdmx/YhdmxAction!showListAction.do?zt=0";
	
	//保存分类
	var link3="${contextPath}/jteap/wz/yhdgl/YhdglCatalogAction!saveOrUpdateCatalogAction.do";
	
	//列表
	var link4="${contextPath}/jteap/wz/yhdgl/YhdglAction!showListAction.do?zt=0&limit=50";
	
	//删除
	var link5="${contextPath}/jteap/wz/yhdgl/YhdglAction!removeAction.do";
	var link15="${contextPath}/jteap/wz/yhdmx/YhdmxAction!removesAction.do";
	//添加
	var link6="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_YHD";
	
	//验货单生效
	var link7="${contextPath}/jteap/wz/yhdgl/YhdglAction!enableYhdAction.do";
	
	//修改
	var link8="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_YHDMOD";
	
	//自由入库验货单列表（用于补料计划申请选择）
	var link9="${contextPath}/jteap/wz/yhdgl/YhdglSelectAction!showListAction.do";
	
	//自由入库验货单明细列表（用于补料计划申请选择,ZT【0】表示未生效，【2】表示待验收已生效，【1】表示正式入库生效）
	var link10="${contextPath}/jteap/wz/yhdmx/YhdmxSelectAction!showListAction.do?zt=2";
	//删除自由入库
	var link11 = "${contextPath}/jteap/wz/yhdgl/YhdglAddEFormAction!delZyrkAction.do";
	
</script>