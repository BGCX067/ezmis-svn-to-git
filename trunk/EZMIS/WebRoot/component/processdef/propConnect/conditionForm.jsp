<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<c:set var="contextPath" scope="page"
	value="${pageContext.request.contextPath}" />
<%@ include file="/inc/import.jsp" %>
<html>
<head>
	<%@ include file="/inc/meta.jsp" %>
	<title>条件生成器</title>
	 <link rel="stylesheet" href="${contextPath}/resources/css/ext-all.css" type="text/css"></link>
	 <link rel="stylesheet" href="${contextPath}/script/ext-extend/MultiselectItemSelector/Multiselect.css" type="text/css"></link>
 	
	 <!-- 加载脚本库  开始 -->
	 <!-- Ext 主库 -->
	 <script type="text/javascript" src="${contextPath}/script/adapter/ext/ext-base.js"></script>
	 <script type="text/javascript" src="${contextPath}/script/ext-all.js"></script> 	
	 	
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelPanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelValuePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/TitlePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/MultiselectItemSelector/Multiselect.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/MultiselectItemSelector/DDView.js" charset="UTF-8"></script>
	
	<script type="text/javascript" src="${contextPath}/script/common.js"></script>
	 
	 
	
	
	<script type="text/javascript" src="script/conditionForm.js" charset="UTF-8"></script>	 
		<title></title>

		<script type="text/javascript">
			Ext.onReady(function() {
				Ext.QuickTips.init();
				
				/*
				var conditionMakerPanel = new ConditionMakerPanel();
				var conditionMakerWindow = new Ext.Window(
					{
						width : 500 ,
						height : 400,
						closable:false ,
						title:'条件生成器',
						draggable : false , 
						items:[conditionMakerPanel]
					}
				)
				*/	
			var viewport=new Ext.Viewport({
				layout:'border',
				items:[titlePanel,lyCenter]
			});
			
			onload();
			
				
				//conditionMakerWindow.show();
			})
		</script>
</head>
<body style="padding: 0px 0px 0px 0px">
</body>
</html>