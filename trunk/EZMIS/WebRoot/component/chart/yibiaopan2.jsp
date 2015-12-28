<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>

<%
	String txChart=(String)request.getAttribute("txtChart");
	
	System.out.println(txChart);
	
	if(txChart==null){
		txChart="";
	}
%>

<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>

	<link rel="stylesheet" href="index.css" type="text/css"></link>	
  </head>
 
  <body scroll="no">
	 <!-- 加载等待图标 开始 -->
	<div id="loading-mask" style=""></div>
	<div id="loading">
	  <div class="loading-indicator">
	  	<img src="${contextPath}/resources/extanim32.gif" width="32" height="32" style="margin-right:8px;" align="absmiddle"/>Loading...
	  </div>
	</div>
   	 <!-- 加载等待图标 结束 -->

   	 <!-- 加载脚本库  开始 -->
   	 <%@ include file="/inc/ext-all.jsp" %>

	<script type="text/javascript" src="${contextPath}/component/chart/js/xml.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/component/chart/ChartsWidgets/FusionCharts.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/component/chart/js/yibiaopan2.js" charset="UTF-8"></script>
	
<script type="text/javascript">
	
	function changeSize(){
   		try{		
			var height = document.body.offsetHeight;
			var width=document.body.offsetWidth;	
			var ver=navigator.appVersion;			
			if(ver.indexOf("MSIE 7")>-1){			
				//document.all.tableContainer.style.height=height-80;
				$("tableContainer").style.height=height-480;
				$("tableContainer").style.width=width-25;
			}else{
				//document.all.tableContainer.style.height=height-120;
				$("tableContainer").style.height=height-460;	
				$("tableContainer").style.width=width-23;	
			}
			}catch(e){}
   	 }
    window.onresize=changeSize;
	

</script>

	<!-- 入口程序 -->
   		<script type="text/javascript">

		Ext.onReady(function(){
			Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
			Ext.QuickTips.init();
			
			var oXML=XMLUtil.load_xml("<%=txChart.trim()%>");
			
			var yibiaopan2 = new Yibiaopan2({},{dataXml:oXML.xml});
			
			if(Ext.get('loading')){
				setTimeout(function(){
					Ext.get('loading').remove();
			        Ext.get('loading-mask').fadeOut({remove:true});
			    }, 250);
			}
		})
		</script>
    <!-- 加载脚本库  结束 -->
    

  </body>
</html>
