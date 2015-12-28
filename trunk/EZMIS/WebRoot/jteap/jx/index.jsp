<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.core.support.SystemConfig"%>
<%@page import="com.jteap.core.utils.StringUtil"%>

<html>
  <head>
	<meta http-equiv="x-ua-compatible" content="ie=7" />
	<title>运行管理子系统</title>
	<%
		String moduleId = (String)request.getParameter("moduleId");
	%>
	<link rel="stylesheet" href="index.css" type="text/css"></link>
	<script>
	<%
		String isPop = SystemConfig.getProperty("JTEAP_SYSTEM_POPUP_WINDOW");
		if(StringUtil.isEmpty(isPop) || (!isPop.equals("true") && !isPop.equals("false"))){
			isPop = "true";
		}
	%>
	var isPop = <%=isPop%>;
	function windowopen(){
		var moduleId = '<%=moduleId%>';
		var target="index2.jsp?moduleId="+moduleId;
		if(isPop){
			newwindow=window.open("","","left=0,top=0,toolbar=0,menubar=0,resizable=no,location=no, status=no");
			if (document.all){
			 try{  
			 
			  newwindow.resizeTo(screen.width,screen.height-28);
			  
			  }catch(e){}
			
			}
			newwindow.location=target
		}else{
			window.location = target;
		}
	}
		
		function CloseWin()
		{
			if(!isPop)
				return;
		  var ua=navigator.userAgent
		  var ie=navigator.appName=="Microsoft Internet Explorer"?true:false
		  if(ie){
		    var IEversion=parseFloat(ua.substring(ua.indexOf("MSIE ")+5,ua.indexOf(";",ua.indexOf("MSIE "))))
		  if(IEversion< 5.5){
		   var str = '<object id=noTipClose classid="clsid:ADB880A6-D8FF-11CF-9377-00AA003B7A11">'
		   str += '<param name="Command" value="Close"></object>';
		   document.body.insertAdjacentHTML("beforeEnd", str);
		   document.all.noTipClose.Click();
		   }else{
		     window.opener =null;
		     window.open('','_self','');
		     window.close();
		   }
		  }
		  else{
		    window.close();
		  }
		}
			
	</script>
  </head>
	
  <body onload="windowopen();CloseWin();">
	
  </body>
</html>
