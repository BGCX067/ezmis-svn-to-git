<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.index.manager.ScZhxxManager"%>
<%@page import="java.text.DecimalFormat"%>
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
		.ys{
			color: #EF6421;
		}
	</style>
  </head>
 
  <body id="index" scroll="no">
	
	<%
		ScZhxxManager scZhxxManager = (ScZhxxManager)SpringContextUtil.getBean("scZhxxManager");
		Map<String, Double> map = scZhxxManager.findScZhxxMap();
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		
		double scer = 0d;
		double se = 0d;
		double cc = 0d;
		double scr = 0d;
		double ycydl = 0d;
		double yswdl = 0d;
		double yhml = 0d;
		double ygdbmh = 0d;
		if(map != null){
			scer = map.get("scer");
			se = map.get("se");
			cc = map.get("cc");
			scr = map.get("scr");
			ycydl = map.get("ycydl");
			yswdl = map.get("yswdl");
			yhml = map.get("yhml");
			ygdbmh = map.get("ygdbmh");
		}
	 %>
	
	<table align="center" width="96%" height="100%">
		<tr>
			<td>
				日综合厂用电率：
			</td>
			<td>&nbsp;</td>
			<td>
				<font class="ys">
					<%=decimalFormat.format(scer) %>
				</font>
			</td>
			<td>
				%
			</td>
		</tr>
		<tr>
			<td>
				月度综合厂用电率：
			</td>
			<td>&nbsp;</td>
			<td>
				<font class="ys">
					<%=decimalFormat.format(ycydl) %>
				</font>
			</td>
			<td>
				%
			</td>
		</tr>
		<tr>
			<td colspan="4"><hr></td>
		</tr>
		<tr>
			<td>
				日上网电量：
			</td>
			<td>&nbsp;</td>
			<td>
				<font class="ys">
					<%=decimalFormat.format(se) %>
				</font>
			</td>
			<td>
				万kWh
			</td>
		</tr>
		<tr>
			<td>
				月度累计上网电量：
			</td>
			<td>&nbsp;</td>
			<td>
				<font class="ys">
					<%=decimalFormat.format(yswdl) %>
				</font>
			</td>
			<td>
				万kWh
			</td>
		</tr>
		<tr>
			<td colspan="4"><hr></td>
		</tr>
		<tr>
			<td>
				日耗煤量：
			</td>
			<td>&nbsp;</td>
			<td>
				<font class="ys">
					<%=decimalFormat.format(cc) %>
				</font>
			</td>
			<td>
				T
			</td>
		</tr>
		<tr>
			<td>
				月度累计耗煤量：
			</td>
			<td>&nbsp;</td>
			<td>
				<font class="ys">
					<%=decimalFormat.format(yhml) %>
				</font>
			</td>
			<td>
				T
			</td>
		</tr>
		<tr>
			<td colspan="4"><hr></td>
		</tr>
		<tr>
			<td>
				日供电标煤耗：
			</td>
			<td>&nbsp;</td>
			<td>
				<font class="ys">
					<%=decimalFormat.format(scr) %>
				</font>
			</td>
			<td>
				g/kWh
			</td>
		</tr>
		<tr>
			<td>
				月度供电标煤耗：
			</td>
			<td>&nbsp;</td>
			<td>
				<font class="ys">
					<%=decimalFormat.format(ygdbmh) %>
				</font>
			</td>
			<td>
				g/kWh
			</td>
		</tr>
	</table>
	
  </body>
</html>
