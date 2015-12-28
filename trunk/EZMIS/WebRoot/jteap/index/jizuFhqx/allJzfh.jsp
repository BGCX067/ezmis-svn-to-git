<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>

<html>
  <head>
 	<%@ include file="/inc/meta.jsp" %>
    <title>机组负荷曲线</title>
  </head>
  
  <body>
  	
  	<table width="100%" height="100%" align="center">
  		<tr>
  			<td>
			  	<IFRAME id='jz1IF' name='portal' scrolling='no' src="index.jsp?jizu=1" style='width: 100%; height: 100%' frameborder='no'></IFRAME>
  			</td>
  			<td>
			  	<IFRAME id='jz2IF' name='portal' scrolling='no' src="index.jsp?jizu=2" style='width: 100%; height: 100%' frameborder='no'></IFRAME>
  			</td>
  		</tr>
  		<tr>
  			<td>
			  	<IFRAME id='jz3IF' name='portal' scrolling='no' src="index.jsp?jizu=3" style='width: 100%; height: 100%' frameborder='no'></IFRAME>
  			</td>
  			<td>
			  	<IFRAME id='jz4IF' name='portal' scrolling='no' src="index.jsp?jizu=4" style='width: 100%; height: 100%' frameborder='no'></IFRAME>
  			</td>
  		</tr>
  	</table>
  	
  </body>
</html>
