<%--
  Author: Calvin
--%>
<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String strFaceStyle = (String)request.getSession().getAttribute("faceStyle");

%>


<html>
<head>
    <title>出错页面</title>
	<LINK rel="stylesheet" href="index.css" type="text/css">
</head>

<body>

<div id="content">
    <%

        if (exception != null) //from JSP
        {
            //Exception from JSP didn't log yet ,should log it here.
            String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
            LogFactory.getLog(requestUri).error(exception.getMessage(), exception);
        }
        else if (request.getAttribute("exception") != null) //from Spring
        {
            exception = (Exception) request.getAttribute("exception");
        }
    %>
<!-- 
    <h3>系统运行期错误: <br><%=exception.getMessage()%></h3>
-->
    <h3>您没有查看当前信息的权限，请与管理员联系。</h3>
    <br>
     <script language="javascript">
        function showDetail()
        {
            if (detail_error_msg.style.display == 'none')
                detail_error_msg.style.display = '';
            else
                detail_error_msg.style.display = 'none';
        }
    </script>
    <button onclick="history.back();" class="buttonbt">返回</button>
    <br>
<!-- 
    <p><a href="javascript:showDetail();" onclick="">开发人员点击此处获取详细错误信息</a></p>
-->
    <div id="detail_error_msg" style="display:none">
        <pre><%exception.printStackTrace(new java.io.PrintWriter(out));%></pre>
    </div>
</div>
</body>
</html>