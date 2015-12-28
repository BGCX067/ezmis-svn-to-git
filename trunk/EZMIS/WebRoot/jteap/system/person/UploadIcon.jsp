<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp"  %>
	<title>JTEAP 2.0</title>

	<script type="text/javascript" src="${contextPath}/script/prototype.js"></script>
	<script type="text/javascript" src="${contextPath}/script/common.js"></script>
	<script type="text/javascript" src="script/MoreProperties.js" charset="UTF-8"></script>	
  	<script type="text/javascript">
	  	var personid="${param.personid}";
	  	var readOnly="${param.readOnly}";
	 </script>
  </head>
  <body onload="onlaodIcon();">
  <s:form action="PersonAction!saveIconAction.do" target="_self" enctype="multipart/form-data" method="post" name="form1" theme="simple">
		<div id="accDiv" style="width:240px;">
			<img src='icon/noimg.gif' id="icon" width='150' height="130">
			<input type="file" name="upload" style="width:180px;">
			<input type="button" name="uploadButton" value="上传" style='height:18px;' onclick="uploadIcon();">
		</div>
  </s:form>	
	
	<script type="text/javascript">
		
	</script>
  </body>
</html>
