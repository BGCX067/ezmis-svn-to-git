 <%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.wfengine.workflow.manager.FlowConfigManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.wfengine.workflow.model.FlowConfig"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
  </head>
 
  <body scroll="no">
	 <!-- 加载等待图标 开始 -->
	<div id="loading-mask" style=""></div>
	<div id="loading">
	  <div class="loading-indicator">
	  	<img src="${contextPath}/resources/extanim32.gif" width="32" height="32" style="margin-right:8px;" align="absmiddle"/>Loading...
	  </div>
	</div>
   	 <!-- 加载等待图标 结束 -->
   	 
   	 
   	 <!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/Datetime/Datetime.js"></script>
	<!--script type="text/javascript" src="${contextPath}/script/ext-extend/tree/ExTreeEditor.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/ConfirmTextField.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/SearchPanel.js"></script -->
	
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>
	<script type="text/javascript" src="script/xqjhsqGrid.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/mxGrid.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/index.js" charset="UTF-8"></script>
	
	<!-- 入口程序 -->
    <script type="text/javascript">
    	
		Ext.onReady(function(){			
			Ext.QuickTips.init();
			//to do in the program
			
			var viewport=new Ext.Viewport({
				layout:'border',
				items:[lyNorth,lyCenter,lySouth]
			});
			
		
		  	//程序加载完成后撤除加载图片
		    setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250); 
		});	
    </script>
    <!-- 加载脚本库  结束 -->

  </body>
</html>
