<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.yx.runlog.manager.LogsTableInfoManager"%>
<%@page import="com.jteap.yx.runlog.model.LogsTableInfo"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.yx.runlog.manager.PhysicLogsManager"%>
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
 
  <body scroll="no" id="index">
	
	<%
		//表Id
		String tableId = request.getParameter("tableId");
		//值班班次
		String zbbc = request.getParameter("zbbc");
		//获取该表信息
		LogsTableInfoManager tableInfoManager = (LogsTableInfoManager)SpringContextUtil.getBean("logsTableInfoManager");
		LogsTableInfo tableInfo = tableInfoManager.get(tableId);
		
		PhysicLogsManager physicLogsManager = (PhysicLogsManager)SpringContextUtil.getBean("physicLogsManager");
		//根据值班班次、采样点  获取 当前班次的采样List、当前班次开始、结束时间
		Map<String,Object> map = physicLogsManager.findCaiYangMap(tableInfo.getCaiyangdian(),zbbc);
		List<String> caiyangList = (List<String> )map.get("caiyangList");
	 %>
	
	<table border="0" cellspacing="1" bgcolor="#e1e1e1" height="100%">
		<tr>
			<td align="center" nowrap="nowrap" width="100px">
				额定值
			</td>
			<%
				for(int i=0; i<caiyangList.size(); i++){
			%>
				<td align="center" nowrap="nowrap" width="100">
					<%=caiyangList.get(i) + ":00"%>
				</td>
			<%		
				}
			 %>
			<td align="center" nowrap="nowrap" width="100px"> 班累计</td>
			<td align="center" nowrap="nowrap" width="100px"> 班平均</td>				
			<td align="center" nowrap="nowrap" width="100px"> 全天累计</td>
			<td align="center" nowrap="nowrap" width="100px"> 全天平均</td>				
			<td align="center" nowrap="nowrap" width="100px"> 月累计</td>
			<td align="center" nowrap="nowrap" width="100px"> 月平均</td>
		</tr>
	</table>
			
  </body>
</html>
