<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<jteap:dict catalog="RKRZLB"></jteap:dict>
<script>
	//分类树
	var link1="${contextPath}/jteap/wz/crkrzgl/CrkrzmxCatalogAction!showTreeAction.do";
	
	//
	var link2="${contextPath}/jteap/wz/cgjhmx/CgjhmxAction!showPersons.do";
	
	//列表
	var link3="${contextPath}/jteap/wz/ckgl/CkglAction!showListAction.do";
	
	//列表
	var link4="${contextPath}/jteap/wz/crkrzgl/CrkrzglAction!showListAction.do?queryParamsSql=obj.crkqf='1'";
	
	//删除
	var link5="${contextPath}/jteap/wz/crkrzgl/CrkrzmxAction!removeAction.do";
	
	//添加修改功能
	var link6="${contextPath}/jteap/wz/crkrzgl/CrkrzmxForm.jsp";
	
	//入库明细
	var link15="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_YCRKRZ_VIEW";
	
	//物资
	var link16 = "${contextPath}/jteap/wz/wzda/WzdaAction!showListAction.do";
	
	//入库
	var link17="${contextPath}/jteap/wz/crkrzgl/CrkrzmxAction!showListAction.do";
	
	//分配
	var link18="${contextPath}/jteap/wz/wzlysq/XqjhSelectAction!showXQJHMXFPListAction.do";
	
	//需求
	var link19="${contextPath}/jteap/wz/wzlysq/XqjhSelectAction!showXQJHMXAllListAction.do";
	
	//采购
	var link20="${contextPath}/jteap/wz/cgjhmx/CgjhmxAction!showListAction.do";
	
	//采购明细
	var link21="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_YCGJH_VIEW";
	
	//验货入库
	var link22="${contextPath}/jteap/wz/yhdmx/YhdmxAction!showListAction.do";
	
	//验货入库明细
	var link23="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_YYHD_RK_VIEW";
	
	//领用单
	var link24="${contextPath}/jteap/wz/lydgl/LydmxAction!showListAction.do";
	
	//领用单明细
	var link25="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_YLYD_VIEW";
	
	//调入单
	var link26="${contextPath}/jteap/wz/dbdgl/DbdMxAction!showListAction.do";
	
	//调入单明细
	var link27="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_YDBD_RK_VIEW";
	
	//退料
	var link28="${contextPath}/jteap/wz/tldgl/TldMxAction!showListAction.do";
	
	//退料明细
	var link29="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_YTLD_VIEW";
	
	//销售
	var link30="${contextPath}/jteap/wz/wzxsd/WzxsdMxAction!showListAction.do";
	
	//销售明细
	var link31="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_YWZXSD_VIEW";
	
	//获取指定流程实例
	var link32="${contextPath}/jteap/wz/xqjhsq/XqjhsqxxAction!showProcessinstance.do";
	
	// model模式弹出页面
	var link33 = "${contextPath}/jteap/ModuleWindowForm.jsp";
	
	// 查看签收页面
	var link34 = "${contextPath}/jteap/wfengine/wfi/WorkFlowInstanceAction!showViewAction.do";
</script>