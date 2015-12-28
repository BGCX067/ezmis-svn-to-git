 
<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>

<jteap:operations/>
<script>	
	var link1="${contextPath}/jteap/jhtj/sjwh/tjSjwhAction!showTreeAction.do";
	
	var link2="${contextPath}/jteap/jhtj/sjwh/tjSjwhAction!deleteNodeAction.do";
	
	var link3="${contextPath}/jteap/jhtj/sjwh/tjSjwhAction!saveUpdateAction.do";

	var link4="${contextPath}/jteap/jhtj/sjwh/tjSjwhAction!showListAction.do";
	var link5="${contextPath}/jteap/jhtj/sjwh/tjSjwhAction!removeAction.do";
	
	var link6="${contextPath}/jteap/jhtj/sjwh/dataDefineForm.jsp";
	
	var link7="${contextPath}/jteap/jhtj/sjwh/templateFrame.jsp";
	
	//动态创建列
	var link8="${contextPath}/jteap/jhtj/sjwh/tjSjwhAction!findDynaColumnsAction.do";
	var link9="${contextPath}/jteap/jhtj/sjwh/tjSjwhAction!findDynaDataAction.do";
	//查看该模块是否能操作
	var link10="${contextPath}/jteap/jhtj/sjwh/tjSjwhAction!findOperateStateAction.do";
	//查看该数据表中是否有数据
	var link11="${contextPath}/jteap/jhtj/sjwh/tjSjwhAction!isExistDataAction.do";
	//显示选择条件的界面
	var link12="${contextPath}/jteap/jhtj/sjwh/tjSjwhAction!showSelectDatePageAction.do";
	//有数据的时候请求最新的日期
	var link13="${contextPath}/jteap/jhtj/sjwh/tjSjwhAction!findInitDataAction.do";
	//显示增加页面
	var link14="${contextPath}/jteap/jhtj/sjwh/tjSjwhAction!showAddAction.do";
	//更新操作状态
	var link15="${contextPath}/jteap/jhtj/sjwh/tjSjwhAction!updateOperateStateAction.do";
	//根据年和月来改变天数
	var link16="${contextPath}/jteap/jhtj/sjwh/tjSjwhAction!findLastDayAction.do";
	//取数
	var link17="${contextPath}/jteap/jhtj/sjwh/tjSjwhAction!getDataAllAction.do";
	//计算
	var link18="${contextPath}/jteap/jhtj/sjwh/tjSjwhAction!computeDataAction.do";
	//保存
	var link19="${contextPath}/jteap/jhtj/sjwh/tjSjwhAction!saveOrUpdateAction.do";
	var link20="${contextPath}/jteap/jhtj/sjwh/tjSjwhAction!dynaAddSearPanelAction.do";
	
</script>