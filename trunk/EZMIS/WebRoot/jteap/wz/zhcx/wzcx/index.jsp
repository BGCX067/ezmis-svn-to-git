<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<link rel="stylesheet" href="${contextPath}/script/ext-extend/treegrid/css/TreeGrid.css" type="text/css"></link>
	<link rel="stylesheet" href="${contextPath}/script/ext-extend/treegrid/css/TreeGridLevels.css" type="text/css"></link>
	<%
		String catalogMark=request.getParameter("catalogMark"); 
	%>
  </head>
 
  <body scroll="no">
  	<script>
	  	var formSn = "TB_WZ_SWZLB";
  	</script>
	 <!-- 加载等待图标 开始 -->
	<div id="loading-mask" style=""></div>
	<div id="loading">
	  <div class="loading-indicator">
	  	<img src="${contextPath}/resources/extanim32.gif" width="32" height="32" style="margin-right:8px;" align="absmiddle"/>Loading...
	  </div>
	</div>
   	 <!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>	
   	<script type='text/javascript' src='${contextPath}/script/ext-extend/treegrid/TreeGrid.js'></script>
   	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/SearchPanel.js"></script>
   	<script type="text/javascript" src="script/RightGrid.js" charset="UTF-8"></script>
   	
	<script type="text/javascript" src="script/SearchPanel.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/RightPanel.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/UpdateWzdaForm.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/LeftTree.js" charset="UTF-8"></script>		
	<script type="text/javascript" src="script/index.js" charset="UTF-8"></script>
	<!-- 入口程序 -->
    <script type="text/javascript">
    	
    	var gTree;		//组织树

    	Ext.onReady(function(){			
			Ext.QuickTips.init();
			var viewport=new Ext.Viewport({
				layout:'border',
				items:[lyCenter,lyNorth]//leftTree,
			});
			
		  	//程序加载完成后撤除加载图片
		    setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250);
		    searchPanel.expand(false);
		});	
		
		
    </script>
    <!-- 加载脚本库  结束 -->
    
    <!-- 页面内容 to do something in here -->

  </body>
</html>
