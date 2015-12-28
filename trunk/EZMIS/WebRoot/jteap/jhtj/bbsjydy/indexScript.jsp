 
<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>

<jteap:operations/>
<script>	
	var link1="${contextPath}/jteap/jhtj/bbsjydy/bbIOAction!showTreeAction.do";
	
	var link2="${contextPath}/jteap/jhtj/bbsjydy/bbIOAction!deleteNodeAction.do";
	
	var link3="${contextPath}/jteap/jhtj/bbsjydy/bbIOAction!saveUpdateAction.do";

	var link4="${contextPath}/jteap/jhtj/bbsjydy/bbIOAction!showListAction.do";
	
	var link5="${contextPath}/jteap/jhtj/bbsjydy/bbIOAction!removeAction.do";
	
	var link6="${contextPath}/jteap/jhtj/bbsjydy/bbzcForm.jsp";
	
	var link7="${contextPath}/jteap/jhtj/bbsjydy/bbIOAction!showUpdateAction.do";

	var link8="${contextPath}/jteap/jhtj/bbsjydy/bbSjzdAction!showListAction.do";
	//增加的模板
	var link9="${contextPath}/jteap/jhtj/bbsjydy/templateFrame.jsp";
	//选表和字段的页面
	var link10="${contextPath}/jteap/jhtj/bbsjydy/dataDefineForm.jsp";
	//寻找所有的表
	var link11="${contextPath}/jteap/jhtj/bbsjydy/bbIOAction!getTablbsAction.do";
	//寻找所有的字段
	var link12="${contextPath}/jteap/jhtj/bbsjydy/bbIOAction!findDefColumnInfoListByTableAction.do";

	//动态创建列
	var link13="${contextPath}/jteap/jhtj/bbsjydy/bbIOAction!findDynaColumnsAction.do";
	var link14="${contextPath}/jteap/jhtj/bbsjydy/bbIOAction!findDynaDataAction.do";
	//给接口初始化
	var link15="${contextPath}/jteap/jhtj/sjydy/tjAppIOAction!createInterfaceBySqlAction.do";
	//接口展示页面
	var link16="${contextPath}/jteap/jhtj/sjydy/initInterfaceForm.jsp";
	//根据具体的值替换接口
	var link17="${contextPath}/jteap/jhtj/sjydy/tjAppIOAction!replaceInterfaceSqlAction.do";
	//sql语句是否合法
	var link18="${contextPath}/jteap/jhtj/sjydy/tjAppIOAction!isRightSqlAction.do";
	
</script>