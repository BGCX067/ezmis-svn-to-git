<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>

	<link rel="stylesheet" href="${contextPath}/jteap/jhtj/sjdbfx/index.css" type="text/css"></link>	
  </head>
 
  <body scroll="no" onload="intiDate();">
  <div style="display:none">
	  <form action="${contextPath}/jteap/jhtj/sjdbfx/sjdbfxAction!showIndexAction.do" name="backForm">
	  		<input type="text" name="kid" value="${requestScope.kid}">
	  		<input type="text" name="isJz" value="${requestScope.isJz}">
	  		<input type="text" name="flflag" value="${requestScope.flflag}">
	  		<input type="text" name="item" value="${requestScope.item}">
	  </form>
  </div>
	 <!-- 加载等待图标 开始 -->
	<div id="loading-mask" style=""></div>
	<div id="loading">
	  <div class="loading-indicator">
	  	<img src="${contextPath}/resources/extanim32.gif" width="32" height="32" style="margin-right:8px;" align="absmiddle"/>Loading...
	  </div>
	</div>
   	 <!-- 加载等待图标 结束 -->

   	 <!-- 加载脚本库  开始 -->
   	 <%@ include file="/inc/ext-all.jsp" %>
<script type="text/javascript" src="${contextPath}/script/ext-extend/form/SearchPanel.js"></script>
	<script type="text/javascript" src="${contextPath}/component/chart/js/xml.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/component/chart/Charts/FusionCharts.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/jteap/jhtj/sjdbfx/script/SearchPanel.js"></script>
	<script type="text/javascript" src="${contextPath}/jteap/jhtj/sjdbfx/script/sjdbfxChart.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/jteap/jhtj/sjdbfx/script/sjdbfxOperation.js" charset="UTF-8"></script>
	<script language="javascript">
  		//初始化参数
  		var kid='${requestScope.kid}';
  		var flflag='${requestScope.flflag}';
  		var isJz='${requestScope.isJz}';
  		var item='${requestScope.item}';
  	</script>
	<!-- 入口程序 -->
    <script type="text/javascript">
		//用户查询面板								
		var searchPanel=new SearchPanel({searchDefaultFs:"",searchAllFs:""});
		Ext.onReady(function(){			
			Ext.QuickTips.init();
			
			var sjdbfxChart = new SjdbfxChart({},{});
			sjdbfxChart.render('center');
			searchPanel.collapse(true);
			searchPanel.expand(true);
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
