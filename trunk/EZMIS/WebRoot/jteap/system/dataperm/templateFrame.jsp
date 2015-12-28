<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
  <script>
	var pWin=window.dialogArguments.opener;
	var windowTitle=window.dialogArguments.title;
	var windowUrl=window.dialogArguments.url;
	
	function initWin(){		
	    //alert(windowUrl);
		document.all.formFrame.src=windowUrl;	
	}
	</script>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
  </head>
  
  
  
  <body style='OVERFLOW-Y: hidden; OVERFLOW-X: hidden;' onload="initWin();" leftmargin="0" rightmargin="0" bottommargin="0" topmargin="0">
  
  	<IFRAME name="formFrame" scrolling="no" src="#" style="width:100%;height:100%" frameborder="0"></IFRAME>
  	<IFRAME name="next" scrolling="no" src="#" style="display:none;width:100%;height:100%" frameborder="0"></IFRAME>
  	<IFRAME name="next2" scrolling="no" src="#" style="display:none;width:100%;height:100%" frameborder="0"></IFRAME>
  </body>
</html>
<script>
document.title=windowTitle;
</script>