<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>待办</title>
    <style type="text/css">
    	font {
			font-size: 12px;
			color: white;
		}
		body {
			margin: 0px;
		}
    </style>
  </head>
  
  <body>
	
	<table width="100%" height="100%">
		<tr bgcolor="cornflowerblue">
			<td>
				<img src="${contextPath}/jteap/images/deals.gif">
				<font>待办事项</font>
			</td>
		</tr>
		<tr>
			<td>
				<iframe id='dealsIF' src='${contextPath}/jteap/index/deals/index.jsp' width='100%'  height='115' frameborder='0' scrolling='yes'>&nbsp;</iframe>
			</td>
		</tr>
		<tr bgcolor="cornflowerblue">
			<td>
				<img src="${contextPath}/jteap/images/downDeals.gif">
				<font>已办事项</font>
			</td>
		</tr>
		<tr>
			<td>
				<iframe id='downDealsIF' src='${contextPath}/jteap/index/downDeals/index.jsp' width='100%'  height='115' frameborder='0' scrolling='yes'>&nbsp;</iframe>
			</td>
		</tr>
	</table>
	
  </body>
</html>
