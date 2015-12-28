<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<script type="text/javascript">
<!--
var ops=[];
//-->
</script>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<link rel="stylesheet" href="${contextPath}/script/ext-extend/treegrid/css/TreeGrid.css" type="text/css"></link>
	<link rel="stylesheet" href="${contextPath}/script/ext-extend/treegrid/css/TreeGridLevels.css" type="text/css"></link>
	<script type="text/javascript">
		var windowClose = true;
	</script>
  </head>
 
  <body>    
	 <!-- 加载等待图标 开始 -->
	<div id="loading-mask" style=""></div>
	<div id="loading">
	  <div class="loading-indicator">
	  	<img src="${contextPath}/resources/extanim32.gif" width="32" height="32" style="margin-right:8px;" align="absmiddle"/>Loading...
	  </div>
	</div>
   	 <!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/TitlePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelPanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelValuePanel.js"></script>
	
   	<script type="text/javascript" src="${contextPath}/script/ext-extend/SearchField.js"></script>	
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/ComboTree.js"></script>
   	<script type='text/javascript' src='${contextPath}/script/ext-extend/treegrid/TreeGrid.js'></script>
   	<script type="text/javascript" src="script/kwTreeGrid.js" charset="UTF-8"></script>
	
	<script type="text/javascript" src="script/LeftTree.js" charset="UTF-8"></script>	
	<script type="text/javascript" src="script/gcxmTreeIndex.js" charset="UTF-8"></script>
	
	<!-- 入口程序 -->
    <script type="text/javascript">
    	
    	var gTree;		//组织树

    	Ext.onReady(function(){			
			Ext.QuickTips.init();
			var viewport=new Ext.Viewport({
				layout:'border',
				items:[leftTree,lyCenter]
			});
			
		  	//程序加载完成后撤除加载图片
		    setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250);
		});	
		
		
    </script>
    <!-- 加载脚本库  结束 -->
    
    <!-- 页面内容 to do something in here -->
 
  </body>
</html>
