<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp"%>
<html>
	<head>
		<%@ include file="/inc/meta.jsp"%>
		<title>JTEAP 2.0</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
		<script type="text/javascript" src="${contextPath}/script/prototype.js"></script>
		
		<script type="text/javascript">

		
			function saveWf(){
				addFlow.editor.save();
				window.close() ;
			}
			
			function cancelWf(){
				window.close() ;
			}
			
			function onloadSubmit(){
				$("myForm").submit();
			}
		</script>
		<style>
			.formTitle{
				height:50px;
				text-align: center;
				font-weight: bold;
				padding-top:5px;
				font-size: 15pt;
			}
			.bottomDiv{
				text-align: right;
				padding:10 10 0 0;
			}
			body,td{
				font-size: 10pt;
			}
			td{
				padding-left: 10px;
			}
			
		</style>
	</head>

	<body onload="onloadSubmit();">
		<center>
			<div style='color: #114581;font:bolder 20px 仿宋;padding-top:15px;height:50px;border:solid 1px #EFEFEF;margin:2px 2px 2px 2px'>流程跟踪</div>
		</center>
		<form target="iFrameWFT" method="post" name="myForm" action="${contextPath}/component/processdef/workflowviewer.jsp">
			<input name="flowConfigId" value="${param.flowConfigId}" type="hidden">
			<input name="currentNode" value="${currentNode}" type="hidden">
			
		</form>
		<div style = "border:solid 1px #EFEFEF;margin:2px 2px 2px 2px">
			<iframe name="iFrameWFT" scrolling="no"
				width="800" height="550" id="addFlow" frameborder="no"></iframe>
		</div>
		<br/>
		<div class="bottomDiv">
			<button onclick="window.close()">退出</button>
		</div>



	</body>
</html>
