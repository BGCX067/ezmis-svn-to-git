<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
	<head>
		<title>设备评级构成</title>
		<%@ include file="/inc/meta.jsp" %>
		<%@ include file="indexScript.jsp" %>
		<%@ include file="/inc/ext-all.jsp" %>
		<script language="JavaScript" src="${contextPath}/jteap/sb/sbpjgl/pjgc/script/xml.js"></script>
		<script language="JavaScript" src="${contextPath}/jteap/sb/sbpjgl/pjgc/script/FusionCharts.js" charset="UTF-8"></script>
		<%
			String chartData = (String) request.getAttribute("chartData");
			if (chartData == null) {
				chartData = "";
			}
		%>
		<script type="text/javascript">
		function changeSize(){
	   		try{		
				var height = document.body.offsetHeight;
				var width=document.body.offsetWidth;	
				var ver=navigator.appVersion;			
				if(ver.indexOf("MSIE 7")>-1){			
					//document.all.tableContainer.style.height=height-80;
					$("div6").style.height=height-530;
					$("div6").style.width=width-25;
				}else{
					//document.all.tableContainer.style.height=height-120;
					$("div6").style.height=height-460;	
					$("div6").style.width=width-23;	
				}
				}catch(e){}
	   	 }
		
		function showSbgc(){
			//提交数据
		 	Ext.Ajax.request({
		 		url:link7+"?queryParams="+window.parent.queryParams,
		 		success:function(ajax){
		 			var responseText=ajax.responseText;	
		 			var responseObject=Ext.util.JSON.decode(responseText);
		 			if(responseObject.success){
		 				var chartData = responseObject.chartData.trim();
		 				var oXML=XMLUtil.load_xml(chartData);
						initData('${contextPath}/jteap/sb/sbpjgl/pjgc/Charts',oXML.xml);
		 			}else{
		 				alert(responseObject.msg);
		 			}
		 		},
		 		failure:function(){
		 			alert("提交失败");
		 		},
		 		method:'POST'
		 	})
		}
	 </script>
	</head>

	<body id="myBody" onload="showSbgc();changeSize();">
		<div id="chartDiv" align="cetner"></div>
	</body>
</html>
<script type="text/javascript">
	var flashWidth = document.body.offsetWidth;
	var flashHeight = document.body.offsetHeight;
	/**flash窗体自适应大小**/
	window.onresize=function(){
		flashWidth = document.body.offsetWidth;
		flashHeight = document.body.offsetHeight;
		window.location.reload();
	}
   	/**初始化**/
	function initData(path,dataXML,container,fieldFilter,initData){
		//图标
		var myChart1=new FusionCharts("${contextPath}/jteap/sb/sbpjgl/pjgc/Charts/Pie3D.swf",container, flashWidth, flashHeight);
	    myChart1.setDataXML(dataXML);       
	    myChart1.render("chartDiv"); 
	    myChart1.setTransparent(true);
	}
</script>
