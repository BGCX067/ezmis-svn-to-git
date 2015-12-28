<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<jteap:dict catalog="CGJHMX,CGFX"></jteap:dict>
<script>
	//采购计划查询页面
	var link1="${contextPath}/jteap/wz/cgjhmx/index.jsp";
	
	//采购计划明细列表
	var link2="${contextPath}/jteap/wz/cgjhmx/CgjhmxAction!showListAction.do";
	
	//采购员列表
	var link3="${contextPath}/jteap/wz/cgjhmx/CgjhmxAction!showPersons.do?type=1";
	
	//列表
	var link4="${contextPath}/jteap/wz/cgjhgl/CgjhglAction!showListAction.do";
	
	//删除
	var link5="${contextPath}/jteap/wz/cgjhgl/CgjhglAction!removeAction.do";
	
	//添加
	var link6="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_YCGJH";
	
	//采购计划生效
	var link7="${contextPath}/jteap/wz/cgjhgl/CgjhglAction!enableCgjh.do";
	
	//采购物资汇总
	var link8="${contextPath}/jteap/wz/cgjhmx/CgjhmxAction!sumWz.do";	
	
	//采购计划单打印
	var link9="${contextPath}/jteap/wz/cgjhgl/CgjhglAction!printCgjhgl.do";	
	
	//查询以生效的采购单（新建验货单使用）showType 以生效:1  未生效:0
	var link10="${contextPath}/jteap/wz/cgjhgl/CgjhglAction!showListAction.do?showType=1";
	
	//修改
	var link11="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_YCGJHMOD";
	
	//删除明细
	var link15="${contextPath}/jteap/wz/cgjhmx/CgjhmxAction!removeAction.do";
	//
	var link12="${contextPath}/jteap/wz/xqjhsq/XqjhsqAction!findXqjhsqAction.do";
	
	var link13="${contextPath}/jteap/wz/xqjhsq/XqjhsqDetailAction!findXqjhsqDetailByXqjhsqIdAction.do";
	
	var link14="${contextPath}/jteap/wz/xqjhsq/XqjhsqDetailAction!updateXqjhsqDetailDysztAction.do";
</script>