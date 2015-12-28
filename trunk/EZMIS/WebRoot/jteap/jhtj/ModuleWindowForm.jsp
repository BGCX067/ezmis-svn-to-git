<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<script>
	var pWin=window.dialogArguments.opener;
	var windowTitle=window.dialogArguments.title;
	var windowUrl=window.dialogArguments.url;
	function initWin(){		
		document.all.formFrame.src=windowUrl;		
	}
	</script>
	<TITLE>
	
	</TITLE>
  </head>
  
  <body style='OVERFLOW-Y: hidden; OVERFLOW-X: hidden;' onload="initWin();" leftmargin="0" rightmargin="0" bottommargin="0" topmargin="0">
  
  	<IFRAME name="formFrame" scrolling="no" src="#" style="width:100%;height:100%" frameborder="0"></IFRAME>
  </body>
</html>
<script>
document.title=windowTitle;
</script>