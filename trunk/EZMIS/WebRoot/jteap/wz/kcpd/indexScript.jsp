<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<jteap:dict catalog="CGJHMX,CGFX"></jteap:dict>
<script>
	//采购计划查询页面
	var link1="${contextPath}/jteap/wz/cgjhmx/index.jsp";
	
	//请求人员列表数据
	var link1="${contextPath}/jteap/system/person/P2GAction!showListAction.do";
	
	//请求组织树形结构
	var link2="${contextPath}/jteap/system/group/GroupAction!showTreeAction.do";
	
	//采购员列表
	//var link3="${contextPath}/jteap/wz/cgjhmx/CgjhmxAction!showPersons.do?type=1";
	
	//列表
	var link4="${contextPath}/jteap/wz/kcpd/PddAction!showListAction.do?zt=0";
	
	//删除
	var link5="${contextPath}/jteap/wz/kcpd/PddAction!removeAction.do";
	
	//添加
	var link6="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_YPDD";
	
	//盘点出库生效
	var link7="${contextPath}/jteap/wz/kcpd/PddAction!enablePdd.do";
	
	//盘点入库生效
	//var link7="${contextPath}/jteap/wz/kcpd/PddAction!enableDR.do";
	
	//采购物资汇总
	//var link8="${contextPath}/jteap/wz/cgjhmx/CgjhmxAction!sumWz.do";	
	
	//采购计划单打印
	//var link9="${contextPath}/jteap/wz/cgjhgl/CgjhglAction!printCgjhgl.do";	
	
	//查询以生效的采购单（新建验货单使用）showType 以生效:1  未生效:0
	//var link10="${contextPath}/jteap/wz/cgjhgl/CgjhglAction!showListAction.do?showType=1";
	
	//游离用户列表
	//var link11="${contextPath}/jteap/system/person/PersonAction!showDissciationPersonListAction.do";
	//取得所有用户列表的时候
	//var link12="${contextPath}/jteap/system/person/PersonAction!showListAction.do";
	
	//修改
	var link13="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_YPDD";
	
	//采购计划明细列表
	var link14="${contextPath}/jteap/wz/kcpd/PddMxAction!showListAction.do";
</script>