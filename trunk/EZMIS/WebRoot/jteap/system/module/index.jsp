<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>

	<link rel="stylesheet" href="index.css" type="text/css"></link>
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
   	
	<!-- 加载脚本库  开始 -->
	<%@ include file="/inc/ext-all.jsp" %>
	
   	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/ExTreeEditor.js"></script>
   	<script type="text/javascript" src="${contextPath}/script/ext-extend/ConfirmTextField.js"></script>	 
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelPanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelValuePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/TitlePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/ComboTree.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/UniqueTextField.js" charset="UTF-8"></script>	
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNodeUI.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNode.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/grid/NewRecEditGrid.js" charset="UTF-8"></script>
    
	<script type="text/javascript" src="script/ModuleTree.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/ModuleDetailForm.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/OperationGrid.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/ModuleEditForm.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/index.js" charset="UTF-8"></script>
	<!-- 入口程序 -->
    <script type="text/javascript">
    	Ext.BLANK_IMAGE_URL='${contextPath}/resources/s.gif';
    	var moduleTree;		//组织树

		Ext.onReady(function(){
			Ext.QuickTips.init();
			moduleTree=new ModuleTree();
			var viewport=new Ext.Viewport({
				layout:'border',
				items:[moduleTree,lyCenter,lyNorth]
			});
			
		  	//程序加载完成后撤除加载图片
		    setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250);
		    
		});	
    </script>
    <!-- 加载脚本库  结束 -->
    
    <!-- 主面板 -->
    <div id="center">
    	
    </div>

  </body>
</html>
