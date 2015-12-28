
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
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
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/muiltselect/MultiSelectField.js"></script>
	<script type="text/javascript" src="demo.js" charset="UTF-8"></script>
	<!-- 入口程序 -->
    <script type="text/javascript">
    	
    	var menu1 = null;
		Ext.onReady(function(){			
			Ext.QuickTips.init();
			//to do in the program
		
			menu1 = new Ext.example.StateMultiSelect({
				renderTo: 'menu',
				width:200,
				containerHeight: 200,
				containerWidth: 200
			}); 	
			
			
		  	//程序加载完成后撤除加载图片
		    setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250);
		    
		 //  初始化的时候隐藏查询面板
		 //  searchPanel.collapse(false);
		});	
    </script>
    <!-- 加载脚本库  结束 -->
    
    <!-- 页面内容 to do something in here -->
<div id="menu" style="text-align:left;"><input type="button" value="Show Selected" onclick="alert('Currently Selected States: ' + menu1.getValue() );">
<div id="grid" style="text-align:left;">
  </body>
</html>
	