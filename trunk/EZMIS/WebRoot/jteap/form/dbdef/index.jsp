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
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/TitlePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelPanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelValuePanel.js"></script>
   	
   	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNodeUI.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNode.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNodeLoader.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxAsyncTreeNode.js" charset="UTF-8"></script>
   	<script type="text/javascript" src="${contextPath}/script/ext-extend/SearchField.js"></script>	
   	<script type="text/javascript" src="${contextPath}/script/ext-extend/ConfirmTextField.js" charset="UTF-8"></script>
   	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/ExTreeEditor.js" charset="UTF-8"></script>
   	
   	<script type="text/javascript" src="script/EditColumnForm.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/EditTableForm.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/LeftTree.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/ColumnGrid.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/SearchPanel.js" charset="UTF-8"></script>
	
	<script>
		//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
	    var searchAllFs="字段名称_columncode_textField,中文名称_columnname_textField".split(",");
    	//查询面板中默认显示的条件，格式同上
    	var searchDefaultFs="字段名称_columncode_textField,中文名称_columnname_textField".split(",");
	</script>
	
	
	<script type="text/javascript" src="script/index.js" charset="UTF-8"></script>
	

	
	<!-- 入口程序 -->
    <script type="text/javascript">
    	
    	var gTree;		//组织树

		Ext.onReady(function(){			
			Ext.QuickTips.init();
			
			var viewport=new Ext.Viewport({
				layout:'border',
				items:[leftTree,lyCenter,lyNorth]
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
