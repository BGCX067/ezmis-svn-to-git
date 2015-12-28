<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.jteap.sb.sbpjgl.model.Sbpj"%>
<%@ page import="com.jteap.sb.sbpjgl.manager.SbpjManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<%
		SbpjManager sbpjManager = (SbpjManager) SpringContextUtil.getBean("sbpjManager");
		String total = String.valueOf(sbpjManager.getTotal());
		String firstTotal = String.valueOf(sbpjManager.getTotal("一类"));
		String secondTotal = String.valueOf(sbpjManager.getTotal("二类"));
		String thirdTotal = String.valueOf(sbpjManager.getTotal("三类"));
		String wdTotal = String.valueOf(sbpjManager.getTotal("未定"));
		String firstRatio = String.valueOf(sbpjManager.getRatio("一类"));
		String secondRatio = String.valueOf(sbpjManager.getRatio("二类"));
		String thirdRatio = String.valueOf(sbpjManager.getRatio("三类"));
		String wdRatio = String.valueOf(sbpjManager.getRatio("未定"));
	%>
  </head>
 
  <body scroll="no" id="index">
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
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/ExTreeEditor.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/ConfirmTextField.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/SearchPanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/ComboTree.js"></script>
	<script type="text/javascript" src="${contextPath}/jteap/system/person/script/GroupNodeUI.js"></script>
	<script type="text/javascript" src="${contextPath}/jteap/system/role/script/RoleNodeUI.js"></script>
	<script type="text/javascript" src="script/Ext.ux.grid.GridSummary.js"></script>
	
	<script type="text/javascript">
		var total = "<%=total%>";
		var firstTotal="<%=firstTotal%>";
		var secondTotal="<%=secondTotal%>";
		var thirdTotal="<%=thirdTotal%>";
		var wdTotal="<%=wdTotal%>";
		var firstRatio = "<%=firstRatio%>";
		var secondRatio = "<%=secondRatio%>";
		var thirdRatio = "<%=thirdRatio%>";
		var wdRatio = "<%=wdRatio%>";
	</script>
	<script type="text/javascript" src="script/LeftTree.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/RightGrid.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/SearchPanel.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/index.js" charset="UTF-8"></script>
	<!-- 入口程序 -->
    <script type="text/javascript">
    	var leftTree = new LeftTree();
		Ext.onReady(function(){		
			Ext.QuickTips.init();
			var viewport=new Ext.Viewport({
				layout:'border',
				items:[lyCenter]
			});
			
		  	//程序加载完成后撤除加载图片
		    setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250);
		    searchPanel.collapse(false);
			searchPanel.expand(true);
		});	
    </script>
    <!-- 加载脚本库  结束 -->
    
    <!-- 页面内容 to do something in here -->

  </body>
</html>
