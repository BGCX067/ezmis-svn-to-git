<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
  </head>
 
  <body scroll="yes" onload="intiDate();">
  <div style="display:none">
	  <form action="${contextPath}/jteap/jhtj/sjcx/sjcxAction!showIndexAction.do" name="backForm">
	  		<input type="text" name="kid" value="${requestScope.kid}">
	  		<input type="text" name="id" value="${requestScope.id}">
	  		<input type="text" name="flflag" value="${requestScope.flflag}">
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
   	 
   	 
   	 <!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelPanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelValuePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/TitlePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/ComboTree.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNodeUI.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNode.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/SearchPanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/TitlePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/upload/UploadDialog.js" charset="UTF-8"></script>
   <script type="text/javascript" src="script/SearchPanel.js" charset="UTF-8"></script>	     
	<script type="text/javascript" src="script/sjcxGrid.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/sjcxOperation.js" charset="UTF-8"></script>
	<script language="javascript">
  		//初始化参数
  		var kid='${requestScope.kid}';
  		var id='${requestScope.id}';
  		var flflag='${requestScope.flflag}';
  		var isJz='${requestScope.isJz}';
  		var fields='${requestScope.fields}';
  		var keys='${requestScope.keys}';
  	</script>
	<!-- 入口程序 -->
    <script type="text/javascript">
    	var sjcxGrid = new SjcxGrid();
		//用户查询面板								
		var searchPanel=new SearchPanel({searchDefaultFs:"",searchAllFs:""});
		Ext.onReady(function(){			
			Ext.QuickTips.init();
			var lyCenter={
				layout:'border',
				id:'center-panel',
				region:'center',
				minSize: 175,
				maxSize: 400,
				border:false,
				margins:'0 0 0 0',
				items:[searchPanel,sjcxGrid]
			}
			var viewport=new Ext.Viewport({
				layout:'border',	
				items:[lyCenter]
			});
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
