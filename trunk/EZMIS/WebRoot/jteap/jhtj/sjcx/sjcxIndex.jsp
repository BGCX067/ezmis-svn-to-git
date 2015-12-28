<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp"%>
<html>
	<head>
		<%@ include file="/inc/meta.jsp"%>
		<%@ include file="indexScript.jsp"%>
		<title>JTEAP 2.0</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
	</head>
	  	<%@ include file="/inc/ext-all.jsp" %>	
	<script language="javascript">
  		//初始化参数
  		var fieldAll='${requestScope.fieldAll}';
  		var kid='${requestScope.kid}';
  		var id='${requestScope.id}';
  		var flflag='${requestScope.flflag}';
  	</script>
  	<script type="text/javascript" src="script/sjcxIndex.js" charset="UTF-8"></script>
	<body style="margin-left: 0; margin-top: 0" onload="cteateFieldList();">
		<form action="${contextPath}/jteap/jhtj/sjcx/sjcxAction!showFindPageAction.do" method="post" target="excelFrame">
			<input type="hidden" name="id" value="${requestScope.id}">
			<input type="hidden" name="kid" value="${requestScope.kid}">
			<input type="hidden" name="flflag" value="${requestScope.flflag}">
			<input type="hidden" name="fields">
			<div id="div6" style="overflow: auto;">
				<div class="arrowlistmenu">
					<div style="float: right">
						<Input type="button" value="查询" class="btn02"
							onclick="subForm();" />
					</div>
					<div id="dataDiv"></div>
				</div>
			</div>
		</form>
	</body>
</html>
