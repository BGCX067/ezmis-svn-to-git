<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp"%>
<html>
	<head>
		<%@ include file="/inc/meta.jsp"%>
	
		<title>JTEAP 2.0</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
		<script type="text/javascript" src="${contextPath}/script/adapter/prototype/prototype.js"></script>
		<style>
			.bottomDiv{
				text-align: right;
				padding: 10 10 10 10;
			}
		</style>
		<script>
			function SaveEForm(){
				var topic=eformFrame.frames[2];	//design/topic
				var msg=topic.DesignDjSave();
				if(msg==""){
					window.returnValue="true";
					topic.blnChange=false;
					window.close();
				}
			}
		</script>
	</head>

	<body>
		<iframe src="${contextPath}/jteap/form/cform/fceform/design/design.htm?djsn=${param.djsn}&catalogId=${param.catalogId}"
			width="100%" height="550" id="eformFrame" frameborder="0"></iframe>
		<br/>
		<div class="bottomDiv">
			<button onclick="SaveEForm();">保存</button>
			<button onclick="window.close();">取消</button>
		</div>



	</body>
</html>
