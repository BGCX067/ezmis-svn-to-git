<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>
	 <link rel="stylesheet" href="${contextPath}/resources/css/ext-all.css" type="text/css"></link>
	 <link rel="stylesheet" href="${contextPath}/script/ext-extend/MultiselectItemSelector/Multiselect.css" type="text/css"></link>	
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
	
	 <!-- Ext 扩展库 -->
	 <script type="text/javascript" src="${contextPath}/script/ext-extend/tree/ExTreeEditor.js"></script>
   	<script type="text/javascript" src="${contextPath}/script/ext-extend/ConfirmTextField.js"></script>	 
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelPanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelValuePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/TitlePanel.js"></script>
	
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNodeUI.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNode.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNodeLoader.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxAsyncTreeNode.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/ColumnNodeUI.js" charset="UTF-8"></script>
	<!-- Ext 扩展库 --> 
	 
	 
	<script type="text/javascript" src="${contextPath}/script/ext-extend/MultiselectItemSelector/Multiselect.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/MultiselectItemSelector/DDView.js" charset="UTF-8"></script>
	
	<script>
	
	//由于该js文件放在最前面，所以在此文件中定义model cell 及其相关方法
	model = window.dialogArguments.mxModel;
	cell = window.dialogArguments.mxCell;
	rootCell = window.dialogArguments.root;
	
	var getLabelValue= function(nodeName,objCell){
		if(objCell==null) {
			objCell = cell ;
		}
		var attrs = objCell.value.attributes;
		for (var i = 0;i < attrs.length; i++) {
			if(attrs[i].nodeName == nodeName){
				return  attrs[i].nodeValue;
			}
		}
	}
	
	var getRootLabelValue = function(nodeName) {
		var attrs = model.root.value.attributes;
		for (var i = 0; i < attrs.length; i++) {
			if(attrs[i].nodeName == nodeName){
				return  attrs[i].nodeValue;
			} 
		}
	}
	</script>
	
	
	<script type="text/javascript" src="script/VariableSelect.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/TaskVariableGrid.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/SelProcessorRuleWindow.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/SelCalculateWindow.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/TaskOperateGrid.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/TaskPowerPanel.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/TaskEventGrid.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/EditTaskFormWindow.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/index.js" charset="UTF-8"></script>
	
	
	<!-- 加载mxGraph的js  begin-->
	<script type="text/javascript">
		if (typeof(mxClient) == 'undefined')
		{
			if (navigator.appName.toUpperCase() == 'MICROSOFT INTERNET EXPLORER')
			{
				document.write('<script src="${contextPath}/component/processdef/js/mxclient.js" type="text/javascript"></'+'script>');
			}else{
				var IS_FF2  = navigator.userAgent.indexOf('Firefox/2') >= 0 || navigator.userAgent.indexOf('Iceweasel/2') >= 0;
				var IS_FF3  =  navigator.userAgent.indexOf('Firefox/3') >= 0 || navigator.userAgent.indexOf('Iceweasel/3') >= 0;
				var jsSrc = "";
				if(IS_FF2 ||  IS_FF3){
					jsSrc = "${contextPath}/component/processdef/js/mxclient_fire.js";
				}else{
					jsSrc = "${contextPath}/component/processdef/js/mxclient.js";
				}

				var script = document.createElement('script');	
				script.setAttribute('type', 'text/javascript');
				script.setAttribute('src', jsSrc);
	
				var head = document.getElementsByTagName('head')[0];
		   		head.appendChild(script);
		   	}
		}
	</script>
	<script type="text/javascript" src="${contextPath}/component/processdef/js/mxApplication.js"></script>
	<!-- 加载mxGraph的js end -->
	
	
	<!-- 入口程序 -->
    <script type="text/javascript">
    	
		Ext.onReady(function(){			
			Ext.QuickTips.init();
			//to do in the program
			var viewport=new Ext.Viewport({
				layout:'border',
				items:[titlePanel,lyCenter]
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

  </body>
</html>
