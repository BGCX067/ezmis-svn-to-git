<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.yx.runlog.manager.LogsColumnInfoManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.yx.runlog.model.LogsColumnInfo"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<link rel="stylesheet" href="index.css" type="text/css"></link>
	<style>
		TABLE {
			font-size: 12px;
			color: 043A7A;
			background-color: #E3E3E3;
		}
		TD {
			background: #efefef;
		}
	</style>
  </head>
 
  <body id="index">
	
	<%
		//表Id
		String tableId = request.getParameter("tableId");
		LogsColumnInfoManager columnInfoManager = (LogsColumnInfoManager)SpringContextUtil.getBean("logsColumnInfoManager");
		List<LogsColumnInfo> columnInfoList = columnInfoManager.findByTableId(tableId);
	%>
	
	<table cellspacing="1" width="100%">
		<%
			for(LogsColumnInfo columnInfo : columnInfoList){
		%>
		<tr>
			<td nowrap="nowrap" width="200" height="20">
				&nbsp;&nbsp;&nbsp;<%=columnInfo.getColumnName() %>
			</td>
		</tr>	
		<%
			}
		 %>
		 <tr nowrap="nowrap" width="200">
		 	<td>&nbsp;</td>
		 </tr>
	</table>
	  	
  </body>
</html>
