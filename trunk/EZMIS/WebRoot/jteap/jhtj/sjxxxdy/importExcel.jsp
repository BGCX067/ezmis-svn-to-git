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
  	<script type="text/javascript">
		function importExcel(){
			var upload=$("upload").value;
			if(upload!=""){
				$("ulButton").disabled=true;
				uploadForm.submit();
				window.returnValue = "true";
				window.close();
			}
		}
	 </script>
	 
  </head>
  <body>
	<form name="uploadForm"  action="${contextPath}/jteap/jhtj/sjxxxdy/importExcelAction!importAction.do?kid=${param.kid}" enctype="multipart/form-data" method="post">
		<div  id="accDiv">
			<table width="98%" border="0" align="center" cellpadding="0"
				cellspacing="0">
				<tr>
					<td width="30%">页号</td>
					<td width="70%">
						<input type="text" name="sheetNum" style="width:55%">
						<font color="red">(数字)</font></td>
				</tr>
				<tr>
					<td width="30%">开始行号</td>
					<td width="70%">
						<input type="text" name="row" style="width:55%">
						<font color="red">(数字)</font></td>
				</tr>
				<tr>
					<td width="30%">数据项编码列号</td>
					<td width="70%">
						<input type="text" name="name" style="width:55%">
						<font color="red">(数字)</font></td>
				</tr>
				<tr>
					<td width="30%">数据项名称列号</td>
					<td width="70%">
						<input type="text" name="cname" style="width:55%">
						<font color="red">(数字)</font></td>
				</tr>
				<tr>
					<td width="30%">单位列号</td>
					<td width="70%">
						<input type="text" name="dw" style="width:55%">
						<font color="red">(数字)</font></td>
				</tr>
				<tr>
					<td width="30%">数据分组列号</td>
					<td width="70%">
					<input type="text" name="sjfz" style="width:55%">
					<font color="red">(数字)</font></td>
				</tr>
				<tr>
					<td width="30%">计算公式列号</td>
					<td width="70%">
						<input type="text" name="cexp" style="width:55%">
						<font color="red">(数字)</font>
					</td>
				</tr>
				<tr>
					<td width="30%">计算顺序列号</td>
					<td width="70%">
						<input type="text" name="corder" style="width:55%">
						<font color="red">(数字)</font>
					</td>
				</tr>
				<tr>
					<td width="30%">文件</td>
					<td width="70%">
						<input type="file" name="upload">
					</td>
				</tr>
				<tr>
					<td width="30%" colspan="2"><input type="submit" name="ulButton" value="导入" style='height:18px;' onclick="importExcel();"></td>
				</tr>
			</table>
		</div>
	</form>
  </body>
</html>
