
<%@page import="com.jteap.core.support.SystemConfig"%>
<%@page import="java.util.Date"%>
<!-- 
描述:
在此可做的事情有很多，包括页面清理缓存，HTML元数据统一配置等 
每个页面均导入该文件 
 --> 
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<!-- 下面一行会改变滚动条按钮样式 -->    
<!-- <meta http-equiv="MSThemeCompatible" Content="no"> -->
<meta http-equiv="keywords" content="JREAP,JTEAP,TECHHERO">
<meta http-equiv="description" content="JTEAP">
<meta http-equiv="Content-Type" Content="text/html; Charset=UTF-8">
<link rel="stylesheet" href="${contextPath}/resources/css/ext-all.css" type="text/css"></link>
<link rel="stylesheet" href="${contextPath}/resources/css/portal.css" type="text/css"></link>
<!-- 下面一行是extjs中文补丁，用于在FireFox中正常显示中文字体 -->
<link rel="stylesheet" href="${contextPath}/resources/css/ext-patch-ex.css" type="text/css"></link>
<link rel="stylesheet" href="${contextPath}/resources/css/jteap-<%=SystemConfig.getProperty("JTEAP_SYSTEM_STYLE","") %>.css" type="text/css"></link>
<%
   	 Date dt = new Date();
   	 long time =dt.getTime();
%>
<script>
	var imgPathPrefix="${contextPath}/";
	var contextPath="${contextPath}";
	var serverDt = new Date(<%=time%>);
</script>

<title><%=SystemConfig.getProperty("JTEAP_SYSTEM_NAME") %></title>