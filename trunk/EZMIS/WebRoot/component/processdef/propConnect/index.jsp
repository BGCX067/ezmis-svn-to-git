<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<c:set var="contextPath" scope="page"
	value="${pageContext.request.contextPath}" />
<%@ include file="/inc/import.jsp" %>
<html>
<head>
	<%@ include file="/inc/meta.jsp" %>
	<title>路由属性</title>
	 <link rel="stylesheet" href="${contextPath}/resources/css/ext-all.css" type="text/css"></link>
	 <link rel="stylesheet" href="index.css" type="text/css"></link>
	 <link rel="stylesheet" href="${contextPath}/script/ext-extend/MultiselectItemSelector/Multiselect.css" type="text/css"></link>
	 <!-- 加载脚本库  开始 -->
	
	<%@ include file="/inc/ext-all.jsp" %>

	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelPanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelValuePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/TitlePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/MultiselectItemSelector/Multiselect.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/MultiselectItemSelector/DDView.js" charset="UTF-8"></script>
	<script>
	
	//由于该js文件放在最前面，所以在此文件中定义model cell 及其相关方法
	var model = window.dialogArguments.mxModel;
	var cell = window.dialogArguments.mxCell;
	var rootCell = window.dialogArguments.root;
	
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
	</script>
	<script type="text/javascript" src="script/index.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/EditConnectFormWindow.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/ConnConditionPanel.js" charset="UTF-8"></script>	
	
	
	
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
	<!-- 加载mxGraph的js  end-->
	
	
		<title></title>

		
</head>
<body style="padding: 0px 0px 0px 0px">
 <!-- 加载等待图标 开始 -->
	<div id="loading-mask" style=""></div>
	<div id="loading">
	  <div class="loading-indicator">
	  	<img src="${contextPath}/resources/extanim32.gif" width="32" height="32" style="margin-right:8px;" align="absmiddle"/>Loading...
	  </div>
	</div>
   	 <!-- 加载等待图标 结束 -->
   	 <script type="text/javascript">
			Ext.onReady(function() {
				Ext.QuickTips.init();
				//editConnectFormWindow.show()
				/*
				var connConditionPanel = new ConnConditionPanel() ;
				var conditionMakerPanel = new ConditionMakerPanel() ;
				var window =  new Ext.Window(
					{
						width : 500 ,
						height : 400 ,
						items:[conditionMakerPanel]
					}
				)
				window.show()
				*/
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
			})
		</script>
</body>
</html>