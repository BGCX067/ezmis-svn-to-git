<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>

<html>
  <head>
    <title>电厂应用系统</title>
  	<style type="text/css">
  		a{
  			color: #000;
			cursor: hand;
			text-decoration: none;
			font-size: 12px;
  		}
  		a:HOVER {
			color: #F60;
		}
		img {
			border: none;
			vertical-align: middle;
		}
  	</style>
  </head>
  
  <body>
	
	<table height="100%" width="100%">
	 <tr>
	    <td>
	    	<a href="http://10.229.41.55/sis/main.do" target="_blank">
	    		<img src="${contextPath}/jteap/images/sysico-5.png" width="17" height="17"/>&nbsp;&nbsp;&nbsp;&nbsp;
	    		SIS系统
	    	</a>
	    </td>
	    <td>
	    	<a href="${pageContext.request.contextPath}/jteap/system/doclib/index.jsp?catalogCode=sysm" target="_blank">
		    	<img src="${contextPath}/jteap/images/sysico-2.png" width="17" height="17" />&nbsp;&nbsp;&nbsp;&nbsp;
	    		使用说明
	    	</a>
	    </td>
	  </tr>
	  <tr>
	    <td>
	    	<a href="http://10.229.41.8:8033/" target="_blank">
		    	<img src="${contextPath}/jteap/images/sysico-1.png" width="17" height="17" />&nbsp;&nbsp;&nbsp;&nbsp;
	    		生产技术部
	    	</a>
	    </td>
	    <td>
	    	<a href="http://10.229.41.8:8088/" target="_blank">
		    	<img src="${contextPath}/jteap/images/sysico-4.png" width="17" height="17" />&nbsp;&nbsp;&nbsp;&nbsp;
	    		财务部
	    	</a>
	    </td>
	  </tr>
	  <tr>
	    <td>
	    	<a href="http://10.229.41.30/ezdc/newindex.htm" target="_blank">
		    	<img src="${contextPath}/jteap/images/sysico-3.png" width="17" height="17" />&nbsp;&nbsp;&nbsp;&nbsp;
	    		燃料管理系统
	    	</a>
	    </td>
	    <td>
	    	<a href="http://10.229.41.21/SBXDJ/Login.aspx?Url=%2fsbxdj%2fdefault1.aspx" target="_blank">
		    	<img src="${contextPath}/jteap/images/sysico-6.png" width="17" height="17" />&nbsp;&nbsp;&nbsp;&nbsp;
	    		设备巡点检
	    	</a>
	    </td>
	  </tr>
	  <tr>
	    <td>
	    	<a href="http://10.229.41.10:8081/" target="_blank">
		    	<img src="${contextPath}/jteap/images/sysico-7.png" width="17" height="17" />&nbsp;&nbsp;&nbsp;&nbsp;
	    		实时查询系统
	    	</a>
	    </td>
	    <td>
	    	<a href="ftp://10.229.41.8/service" target="_blank">
		    	<img src="${contextPath}/jteap/images/sysico-8.png" width="17" height="17" />&nbsp;&nbsp;&nbsp;&nbsp;
	    		文件服务
	    	</a>
	    </td>
	  </tr>
	</table>
	
  </body>
</html>
