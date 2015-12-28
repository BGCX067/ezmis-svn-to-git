<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	 <link rel="stylesheet" href="${contextPath}/script/ext-extend/MultiselectItemSelector/Multiselect.css" type="text/css"></link>
	<style>
	#bbar{
	 text-align: right;
	 padding:5px 5px 5px 5px;
	 
	}
	</style>
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
	 <script type="text/javascript" src="${contextPath}/script/ext-extend/MultiselectItemSelector/Multiselect.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/MultiselectItemSelector/DDView.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/excelFormDsDef.js" charset="UTF-8"></script>
	
	<!-- 入口程序 -->
    <script type="text/javascript">
    	
		Ext.onReady(function(){			
			Ext.QuickTips.init();
			
			var viewport=new Ext.Viewport({
				layout:'border',
				items:[lyCenter,lyLeft,lySouth]
			});
			
			onload();
			
		  	//程序加载完成后撤除加载图片
		    setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250);
		});	
    </script>
    <!-- 加载脚本库  结束 -->
    
    <!-- 页面内容 to do something in here -->
	<div id="bbar">
		<button>保存</button><button>取消</button>
	</div>
  </body>
</html>
