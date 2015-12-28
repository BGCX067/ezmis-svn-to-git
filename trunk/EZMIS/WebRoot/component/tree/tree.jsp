<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="${contextPath}/resources/css/ext-all.css" type="text/css"></link>
	<link rel="stylesheet" href="${contextPath}/resources/style01.css" type="text/css"></link>
	<style>
	.x-paged-tree-node-a:link .x-paged-tree-node-a:visited,{
		color:'red';
	}
	
	</style>
  </head>
  <body scroll="no">
	 <!-- 加载等待图标 开始 -->    
	<div id="loading-mask" style=""></div>
	<div id="loading">
	  <div class="loading-indicator">
	  	<img src="../../resources/extanim32.gif" width="32" height="32" style="margin-right:8px;" align="absmiddle"/>Loading...
	  </div>
	</div>
   	 <!-- 加载等待图标 结束 -->
   	 
   	 
   	 <!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>

	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNodeUI.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNode.js" charset="UTF-8"></script>
	
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/PagedTreeNode.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/PagedAsyncTreeNode.js" charset="UTF-8"></script>

	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/PagedTreeNodeUI.js" charset="UTF-8"></script>
<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/PagedTreeNodeLoader.js" charset="UTF-8"></script>
	
	<script type="text/javascript" src="tree.js" charset="UTF-8"></script>

	<!-- 入口程序 -->
    <script type="text/javascript">
    	var tree=null; 
    	var tree2=null; 
    	var tree3=null; 
		Ext.onReady(function(){
			Ext.QuickTips.init();
			var tree2=createTree2();
			var tree3=createTree3();
			tree2.render();
			tree3.render();
			//程序加载完成后撤除加载图片
		    setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250);
		});	
		
		
    </script>
    <!-- 加载脚本库  结束 -->
    <div id="tree-div2" style="overflow:auto; float:left;height:400px;width:250px;border:1px solid #c3daf9;">

	</div>
    <div id="tree-div3" style="overflow:auto; float:left;height:400px;width:250px;border:1px solid #c3daf9;">

	</div>
    <!-- 页面内容 to do something in here -->
	
  </body>
</html>
