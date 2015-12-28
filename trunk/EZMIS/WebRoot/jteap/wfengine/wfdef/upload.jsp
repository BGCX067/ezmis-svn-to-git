<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  <body>
	<!-- 加载等待图标 开始 -->
	<div id="loading-mask" style=""></div>
	<div id="loading">
	  <div class="loading-indicator">
	  	<img src="${contextPath}/resources/extanim32.gif" width="32" height="32" style="margin-right:8px;" align="absmiddle"/>Loading...
	  </div>
	</div>
	
	<!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>
  	
    <div id="upDiv" style="position: absolute;top: 32;left: 32;display: none;">
  	<form name="uploadform" method="POST" action="${contextPath}/jteap/wfengine/workflow/FlowConfigAction!importFlowConfigFromXmlAction.do" ENCTYPE="multipart/form-data">
  		<table>
  			<tr>
  				<td align="center">
			     	<input name="xmlFile" type="file">
  				</td>
  			</tr>
  			<tr>
  				<td align="center">
  					<input type="button" value="开始上传" onclick="btnSub()">
  				</td>
  			</tr>
  		</table>
  	</form>
  	</div>
  	
  	<!-- 加载等待图标 结束 -->
	<script type="text/javascript">
		Ext.onReady(function(){
			Ext.QuickTips.init();
		  	//程序加载完成后撤除加载图片
		    setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		        upDiv.style.display = "block";
		    }, 250);
		});	
		
		function btnSub(){
			if(/.*\.xml$/.test(document.getElementById("xmlFile").value)){
				uploadform.submit();
			}else{
				alert("请选择正确的流程文件格式");
			}
			
		}
	</script>
	
  </body>
</html>
