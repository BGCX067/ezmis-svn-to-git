 
<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>

<!-- å½åæ¨¡åææå·ææéçæä½ -->
<jteap:operations/>
<script>	
	var link1="${contextPath}/jteap/jhtj/sjydy/tjAppIOAction!showTreeAction.do";
	
	var link2="${contextPath}/jteap/jhtj/sjydy/tjAppIOAction!deleteNodeAction.do";
	
	var link3="${contextPath}/jteap/jhtj/sjydy/tjAppIOAction!saveUpdateAction.do";

	var link4="${contextPath}/jteap/jhtj/sjydy/tjAppSjzdAction!showListAction.do";
	var link5="${contextPath}/jteap/jhtj/sjydy/tjAppSjzdAction!removeAction.do";
	
	var link6="${contextPath}/jteap/jhtj/sjydy/dataDefineForm.jsp";
	
	var link7="${contextPath}/jteap/jhtj/sjydy/templateFrame.jsp";
	//得到所有的表
	var link8="${contextPath}/jteap/jhtj/sjydy/tjAppIOAction!getTablbsBySistemAction.do";
	//得到所有的字段
	var link9="${contextPath}/jteap/jhtj/sjydy/tjAppIOAction!getAllFieldInfoInTableAction.do";
	//sql语句是否合法
	var link10="${contextPath}/jteap/jhtj/sjydy/tjAppIOAction!isRightSqlAction.do";
	//给接口初始化
	var link11="${contextPath}/jteap/jhtj/sjydy/tjAppIOAction!createInterfaceBySqlAction.do";
	//接口展示页面
	var link12="${contextPath}/jteap/jhtj/sjydy/initInterfaceForm.jsp";
	//根据具体的值替换接口
	var link13="${contextPath}/jteap/jhtj/sjydy/tjAppIOAction!replaceInterfaceSqlAction.do";
	//动态创建列
	var link14="${contextPath}/jteap/jhtj/sjydy/tjAppIOAction!findDynaColumnsAction.do";
	var link15="${contextPath}/jteap/jhtj/sjydy/tjAppIOAction!findDynaDataAction.do";
	
	var link16="${contextPath}/jteap/jhtj/sjydy/tjAppIOAction!showUpdateAction.do";
	
	var link17="${contextPath}/jteap/jhtj/sjydy/tjAppSjzdForm.jsp";
	
	var link18="${contextPath}/jteap/jhtj/sjydy/tjAppSjzdAction!showUpdateAction.do";
	
	var link19="${contextPath}/jteap/jhtj/sjydy/tjAppSjzdAction!saveUpdateAction.do";
	
	var link20="${contextPath}/jteap/jhtj/ljydy/appSystemConnForm.jsp";
	//var link7="${contextPath}/jteap/jhtj/sjydy/appSystemConnAction!validateAppSystemConnNameUniqueAction.do";
	///var link8="${contextPath}/jteap/jhtj/sjydy/appSystemConnAction!validateDataBaseConn.do";
	//var link9="${contextPath}/jteap/jhtj/sjydy/appSystemConnAction!showUpdateAction.do";
	//var link10="${contextPath}/jteap/jhtj/sjydy/dataDefineForm.jsp";
	
	//var link12="${contextPath}/jteap/jhtj/sjydy/appSystemConnAction!getAllFieldInfoInTableAction.do";
	//var link13="${contextPath}/jteap/jhtj/sjydy/appSystemConnAction!saveDataDefineAction.do";
	//var link14="${contextPath}/jteap/jhtj/sjydy/appSystemFieldAction!deleteNodeAction.do";
</script>