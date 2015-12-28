<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<link rel="stylesheet" href="${contextPath}/script/ext-extend/form/Datetime/datetime.css" type="text/css"></link>	
</head>
 
  <body scroll="yes">
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
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/ExTreeEditor.js"></script>
	<script type="text/javascript" src="${contextPath}/component/FCKeditor/fckeditor.js" charset="UTF-8"></script>
	
	<script type="text/javascript" src="${contextPath}/script/ext-extend/ConfirmTextField.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelPanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelValuePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/TitlePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/ComboTree.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/UniqueTextField.js" charset="UTF-8"></script>	
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNodeUI.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNode.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/grid/NewRecEditGrid.js" charset="UTF-8"></script>
	
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelPanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelValuePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/TitlePanel.js"></script>
	 <script type="text/javascript" src="${contextPath}/script/ext-extend/form/upload/UploadDialog.js" charset="UTF-8"></script>
   
    <script type="text/javascript"src="${contextPath}/script/ext-extend/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="${contextPath}/script/ext-extend/swfupload/simpledemo/js/swfupload.queue.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/swfupload/simpledemo/js/fileprogress.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/swfupload/simpledemo/js/handlers.js"></script>
	
    <script type="text/javascript" src="${contextPath}/script/ext-extend/form/Datetime/Datetime.js"></script>
	<script type="text/javascript" src="script/DoclibAddForm.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/AttachGrid.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/RightGrid.js" charset="UTF-8"></script>
	<!-- 入口程序 -->
    <script type="text/javascript">
    	
		Ext.onReady(function(){			
			Ext.QuickTips.init();
			//to do in the program
			var doclibAddForm = new DoclibAddWindow('${param.catalogId}');
			var titlePanel=new Ext.app.TitlePanel({caption:'添加文档信息',border:false,region:'north'});
			
			var lyCenter = {
				id : 'center-panel',
				region : 'center',
				border : true,
				
				items:[doclibAddForm]
			}
	
			var viewport=new Ext.Viewport({
				layout:'border',	
				items:[titlePanel,lyCenter]
			});
			
			doclibAddForm.show();
			doclibAddForm.loadData();
			doclibAddForm.loadDoclibLevel();
			
						
		  	//程序加载完成后撤除加载图片
		    setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250);
		  
		});	
    </script>
    <!-- 加载脚本库  结束 -->
        <!-- 加载脚本库  结束 -->
    <div id="hideDiv">
    	<!--  <input type="file" id="fileSelector" onchange="fileSelectorChanged(this.value);"/>-->
    </div>
    <!-- 页面内容 to do something in here -->
  </body>
</html>
