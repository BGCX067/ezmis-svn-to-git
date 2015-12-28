<%@ page language="java" pageEncoding="UTF-8"%>
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
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/ExTreeEditor.js"></script>
   	<script type="text/javascript" src="${contextPath}/script/ext-extend/ConfirmTextField.js"></script>	 
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelPanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelValuePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/TitlePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/UniqueTextField.js" charset="UTF-8"></script>	
	
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/SearchPanel.js"></script>
	<script type="text/javascript" src="script/SearchPanel.js" charset="UTF-8"></script>	
	<script type="text/javascript" src="script/DictCatalogTree.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/CatalogEditForm.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/DictEditForm.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/DictGrid.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/DetailForm.js" charset="UTF-8"></script>
	<script>
	//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
    var searchAllFs="键#key#textField,值#value#textField".split(",");
   	//查询面板中默认显示的条件，格式同上
   	var searchDefaultFs="键#key#textField,值#value#textField".split(",");
	</script>
	<script type="text/javascript" src="script/index.js" charset="UTF-8"></script>
	<!-- 入口程序 -->
    <script type="text/javascript">
    	
		var dictCatalogTree; //字典类型树
		Ext.onReady(function(){
			Ext.QuickTips.init();
			
			dictCatalogTree=new DictCatalogTree();
		
			var viewport=new Ext.Viewport({
				layout:'border',
				items:[dictCatalogTree,lyCenter,lyNorth]
			});
			//初始化的时候隐藏查询面板
			searchPanel.collapse(false);
			
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
