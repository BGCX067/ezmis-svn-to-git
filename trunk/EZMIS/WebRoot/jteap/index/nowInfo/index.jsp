<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.index.manager.NowInfoManager"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<style type="text/css">
		table{
			font-size: 12px;
		}
		.xhxJc{
			font-weight: bold;
		}
		.jc{
			font-weight: bold;
		}
		.ys{
			color: #EF6421;
		}
	</style>
  </head>
 
  <body id="index" scroll="no">
	
	<%
		NowInfoManager nowInfoManager = (NowInfoManager)SpringContextUtil.getBean("nowInfoManager");
		Map<String, Object> map = nowInfoManager.findNowInfoMap();
		
		String qczFh = "0.00";
		String jizuFh1 = "0.00";
		String jizuFh2 = "0.00";
		String jizuFh3 = "0.00";
		String jizuFh4 = "0.00";
		String status1 = "连续停运";
		String status2 = "连续停运";
		String status3 = "连续停运";
		String status4 = "连续停运";
		Integer runDay1 = 1;
		Integer runDay2 = 1;
		Integer runDay3 = 1;
		Integer runDay4 = 1;
		String colorStr1 = "style='color:#EF6421;'";
		String colorStr2 = "style='color:#EF6421;'";
		String colorStr3 = "style='color:#EF6421;'";
		String colorStr4 = "style='color:#EF6421;'";
		
		if(map.get("qczFh") != null){
			qczFh = (String)map.get("qczFh");
		}
		if(map.get("jizuFh1") != null){
			jizuFh1 = (String)map.get("jizuFh1");
		}
		if(map.get("jizuFh2") != null){
			jizuFh2 = (String)map.get("jizuFh2");
		}
		if(map.get("jizuFh3") != null){
			jizuFh3 = (String)map.get("jizuFh3");
		}
		if(map.get("jizuFh4") != null){
			jizuFh4 = (String)map.get("jizuFh4");
		}
		if(map.get("status1") != null){
			status1 = (String)map.get("status1");
			if("连续运行".equals(status1)){
				colorStr1 = "style='color:green;'";
			}
		}
		if(map.get("status2") != null){
			status2 = (String)map.get("status2");
			if("连续运行".equals(status2)){
				colorStr2 = "style='color:green;'";
			}
		}
		if(map.get("status3") != null){
			status3 = (String)map.get("status3");
			if("连续运行".equals(status3)){
				colorStr3 = "style='color:green;'";
			}
		}
		if(map.get("status4") != null){
			status4 = (String)map.get("status4");
			if("连续运行".equals(status4)){
				colorStr4 = "style='color:green;'";
			}
		}
		if(map.get("runDay1") != null){
			runDay1 = (Integer)map.get("runDay1");
		}
		if(map.get("runDay2") != null){
			runDay2 = (Integer)map.get("runDay2");
		}
		if(map.get("runDay3") != null){
			runDay3 = (Integer)map.get("runDay3");
		}
		if(map.get("runDay4") != null){
			runDay4 = (Integer)map.get("runDay4");
		}
	 %>
	
	<script type="text/javascript">
		//页面每5分钟刷新一次
    	setTimeout("self.location.reload();",300000);
	</script>
	
	<table align="center" width="96%" height="100%" cellpadding="10">
		<tr class="ys">
			<td colspan="2">
				全厂总负荷：
			</td>
			<td align="right">
				<font class="xhxJc">
					<%=qczFh %>
				</font>&nbsp;&nbsp;MW
			</td>
		</tr>
		<tr class="ys">
			<td colspan="2">
				#1机组实时负荷：
			</td>
			<td align="right">
				<font class="xhxJc">
					<%=jizuFh1 %>
				</font>&nbsp;&nbsp;MW
			</td>
		</tr>
		<tr class="ys">
			<td colspan="2">
				#2机组实时负荷：
			</td>
			<td align="right">
				<font class="xhxJc">
					<%=jizuFh2 %>
				</font>&nbsp;&nbsp;MW
			</td>
		</tr>
		<tr class="ys">
			<td colspan="2">
				#3机组实时负荷：
			</td>
			<td align="right">
				<font class="xhxJc">
					<%=jizuFh3 %>
				</font>&nbsp;&nbsp;MW
			</td>
		</tr>
		<tr class="ys">
			<td colspan="2">
				#4机组实时负荷：
			</td>
			<td align="right">
				<font class="xhxJc">
					<%=jizuFh4 %>
				</font>&nbsp;&nbsp;MW
			</td>
		</tr>
		<tr style="color: #9CD7F7;">
			<td colspan="3">
				<hr>	
			</td>
		</tr>
		<tr>
			<td>#1机组：</td>
			<td>
				<font <%=colorStr1 %> ><%=status1 %></font>
			</td>
			<td align="right">
				<font <%=colorStr1 %> >
					<font class="jc"><%=runDay1 %></font>&nbsp;&nbsp;天
				</font>
			</td>
		</tr>
		<tr>
			<td>#2机组：</td>
			<td>
				<font <%=colorStr2 %> ><%=status2 %></font>
			</td>
			<td align="right">
				<font <%=colorStr2 %> >
					<font class="jc"><%=runDay2 %></font>&nbsp;&nbsp;天
				</font>
			</td>
		</tr>
		<tr>
			<td>#3机组：</td>
			<td>
				<font <%=colorStr3 %> ><%=status3 %></font>
			</td>
			<td align="right">
				<font <%=colorStr3 %> >
					<font class="jc"><%=runDay3 %></font>&nbsp;&nbsp;天
				</font>
			</td>
		</tr>
		<tr>
			<td>#4机组：</td>
			<td>
				<font <%=colorStr4 %> ><%=status4 %></font>
			</td>
			<td align="right">
				<font <%=colorStr4 %> >
					<font class="jc"><%=runDay4 %></font>&nbsp;&nbsp;天
				</font>
			</td>
		</tr>
	</table>
	
  </body>
</html>
