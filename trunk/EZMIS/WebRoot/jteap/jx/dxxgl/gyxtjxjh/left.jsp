<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.jx.dxxgl.manager.JxsbtzManager"%>
<%@page import="com.jteap.jx.dxxgl.model.Jxsbtz"%>
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
		String zyId = request.getParameter("sszy");
		JxsbtzManager jxsbtzManager = (JxsbtzManager)SpringContextUtil.getBean("jxsbtzManager");
		List<Jxsbtz> lstSb = jxsbtzManager.findBySszy(zyId);
	%>
	
	<table cellspacing="1" width="100%">
		<%
			for(Jxsbtz jxsbtz : lstSb){
		%>
		<tr>
			<td align="center" nowrap="nowrap" width="30" height="20">
				<%=jxsbtz.getXmxh()==null?"":jxsbtz.getXmxh() %>
			</td>
			<td nowrap="nowrap" width="200" height="20">
				&nbsp;&nbsp;&nbsp;<%=jxsbtz.getSbmc() %>
			</td>
		</tr>	
		<%
			}
		 %>
	</table>
	  	
  </body>
</html>
