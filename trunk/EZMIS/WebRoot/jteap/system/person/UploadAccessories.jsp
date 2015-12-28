<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp"  %>
	<title>JTEAP 2.0</title>

	<link rel="stylesheet" href="MoreProperties.css" type="text/css"></link>
	<script type="text/javascript" src="${contextPath}/script/prototype.js"></script>
	<script type="text/javascript" src="${contextPath}/script/common.js"></script>
	<script type="text/javascript" src="script/MoreProperties.js" charset="UTF-8"></script>	
  	<script type="text/javascript">
	  	var personid="${param.personid}";
	  	var readOnly="${param.readOnly}";
	 </script>
  </head>
  <body onload="loadAccInfo();">
	<form name="uploadForm" action="${contextPath}/jteap/system/person/PersonAction!saveAccessoriesAction.do?personid=${param.personid}" enctype="multipart/form-data" method="post" target="accIframe">
		<div id="accDiv">
			<table width='720' cellpadding="0" cellspacing="0">
				<tr>
					<td colspan="5"><input type="file" name="upload"><input type="button" name="ulButton" value="上传" style='height:18px;' onclick="uploadAcc();"></td>
				</tr>
			</table>
		</div>
	</form>	
  </body>
</html>
