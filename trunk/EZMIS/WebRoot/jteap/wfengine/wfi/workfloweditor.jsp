<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<c:set var="contextPath" scope="page"
	value="${pageContext.request.contextPath}" />
<html>
	<head>
		<!-- 
		<script type="text/javascript" src="${contextPath}/script/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="${contextPath}/script/ext-all.js"></script> 		
		<script type="text/javascript" src="${contextPath}/script/build/locale/ext-lang-zh_CN.js"></script>
		<script type="text/javascript" src="${contextPath}/script/common.js"></script>
		 -->
		
		<title>WorkflowEditor</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<style type="text/css" media="screen">
				div.base {
			
					overflow: hidden;
					white-space: nowrap;
					font-family: Arial;
					font-size: 8pt;
				}
				div.base#graph {
					border-style: solid;
					border-color: #F2F2F2;
					border-width: 1px;
					background: url('images/grid.gif');
				}			
		</style>
		<script type="text/javascript">
			mxBasePath = '${contextPath}/component/processdef/';
			//环境路径
			var CONTEXT_PATH='${contextPath}';
		</script>

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
		<script type="text/javascript"
			src="${contextPath}/component/processdef/js/mxApplication.js"></script>
		<script type="text/javascript">
			mxConstants.DEFAULT_HOTSPOT = 1;
		</script>
	</head>
	<body  style="padding: 0px 0px 0px 0px"
		onload="new mxApplication('${contextPath}/component/processdef/config/workfloweditor.xml');">
		<!-- onbeforeunload="javascript: event.returnValue = mxResources.get('changesLost');" -->
			<table id="splash" width="100%" height="100%"
				style="background: white; position: absolute; top: 0px; left: 0px; z-index: 4;">
				<tr>
					<td align="center" valign="middle">
						<img src="images/loading.gif">
					</td>
				</tr>
			</table>
			<div id="graph" class="base" style="position: absolute;margin: 2px 0px 0px 2px">
				<!-- Graph Here -->
			</div>
			<div id="status" class="base" align="right">
				<!-- Status Here -->
			</div>
			<script type="text/javascript">
				document.getElementById("graph").style.height=600;
				document.getElementById("graph").style.width=790;
			</script>
	</body>
</html>
