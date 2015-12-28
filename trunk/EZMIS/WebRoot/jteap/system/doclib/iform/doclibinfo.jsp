<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>
    <title>文档详细信息</title>
	
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="this is my page">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    
    <!--<link rel="stylesheet" type="text/css" href="./styles.css">-->
	
  </head>
  
  
  <body>
  	<table align="center" width="100%">
		<tr>
			<td colspan="6" height="40">
				<center>
					<font size="4">文档详细信息</font>
				</center>
			</td>
		</tr>
		<tr>
			<td colspan="6" height="20">
			</td>
		</tr>
		<tr>
			<td>
				标题
			</td>
			<td>
				<input type="text" id="title" name="title" value="${doclib.title}"
					size="10" />
			</td>
			<td>
				创建人
			</td>
			<td>
				<input type="text" id="creator" name="creator"
					value="${doclib.creator}" size="10" />
			</td>
			<td>
				创建时间
			</td>
			<td>
				<input type="text" id="createdt" name="createdt" value="${createdt}"
					size="15" />
			</td>
			<td colspan="2" height="20">
			</td>
		</tr>
		<tr>
			<td colspan="6" height="40">
			</td>
		</tr>
		<tr>
			<td colspan="6" width="100%">
				扩展字段<input type="text" id="doclibfv" name="doclibfv" value="1234">
			</td>
		</tr>
		<tr>
			<td colspan="6" height="40">
			</td>
		</tr>
		<tr>
			<td colspan="6" width="100%">
				<iframe src="./iform/attach.jsp" width="100%" height="100%"
					id="attach" scrolling=yes></iframe>
			</td>
		</tr>
		<tr>
			<td colspan="6" height="40">
			</td>
		</tr>
		<tr>
			<td align="center" colspan="6">
				<input type="submit" id="submit" name="submit" value="提交" />
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" id="cancel" name="cancel" value="取消"
					onclick="javascript:window.close();" />
			</td>
		</tr>
	</table>
  </body>
</html>
