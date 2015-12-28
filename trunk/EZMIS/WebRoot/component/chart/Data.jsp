<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>

<% String sBasePath = request.getContextPath();
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String strFaceStyle = (String) request.getSession().getAttribute(
			"faceStyle");
	response.addHeader("pragma", "no-cache");
	response.addHeader("cache-control", "no-cache");
	response.addDateHeader("expries", 0);
	
	String txChart1=(String)request.getAttribute("txtChart1");
	String txChart2=(String)request.getAttribute("txtChart2");
	String txChart3=(String)request.getAttribute("txtChart3");
	String initTable=(String)request.getAttribute("initTable");
	String title=(String)request.getAttribute("title");
	
	System.out.println(txChart1);
	System.out.println(txChart2);
	System.out.println(txChart3);
	System.out.println(initTable);
	System.out.println(title);

%>

<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>

	<link rel="stylesheet" href="index.css" type="text/css"></link>
	<script>
	window.ActionA = function(obj){
		GetResultA(obj);
	}

	window.ActionB = function(obj1,obj2){
		GetResultB(obj1,obj2);
	}
	
	window.ActionC = function(obj1,obj2,obj3){
		GetResultC(obj1,obj2,obj3);
	}
	
	function GetResultA(str){
	    var oBao = new ActiveXObject("Microsoft.XMLHTTP");
	    oBao.open("POST",link7 + "?year="+str,false);
	    oBao.send();
	    HandleA(unescape(oBao.responseText))
	} 
	
	function HandleA(str){	
		var arrstr = new Array();
	    arrstr = str.split("|");
	    
	    var oXML1=XMLUtil.load_xml(arrstr[0]);
	    var oXML2=XMLUtil.load_xml(arrstr[1]);
		var table = arrstr[2];
		var title = arrstr[3];

		var chart1 = new FusionCharts(contextPath + "/component/chart/Charts/MSCombiDY2D.swf", "chart1Id", document.body.offsetWidth / 2 - 15, document.body.offsetHeight / 2 - 15);
		chart1.setDataXML(oXML1.xml);
		chart1.setTransparent(true);
		chart1.render("two");
		
		var chart2 = new FusionCharts(contextPath + "/component/chart/Charts/MSCombiDY2D.swf", "chart1Id", document.body.offsetWidth / 2 - 15, document.body.offsetHeight / 2 - 15);
		chart2.setDataXML(oXML2.xml);
		chart2.setTransparent(true);
		chart2.render("three");

		Ext.getCmp('grid-panel').getStore().loadData(eval(table));
		Ext.getCmp('grid-panel').setTitle(title);
	}
	
	function GetResultB(str1,str2){
	    var oBao = new ActiveXObject("Microsoft.XMLHTTP");
	    oBao.open("POST",link8 + "?year="+str1 + "&month=" + str2,false);
	    oBao.send();
	    HandleB(unescape(oBao.responseText))
	} 
	
	function HandleB(str){	
		var arrstr = new Array();
	    arrstr = str.split("|");
	    
	    var oXML1=XMLUtil.load_xml(arrstr[0]);
		var table = arrstr[1];
		var title = arrstr[2];

		var chart1 = new FusionCharts(contextPath + "/component/chart/Charts/MSCombiDY2D.swf", "chart1Id", document.body.offsetWidth / 2, document.body.offsetHeight / 2 - 15);
		chart1.setDataXML(oXML1.xml);
		chart1.setTransparent(true);
		chart1.render("three");
		
		Ext.getCmp('grid-panel').getStore().loadData(eval(table));
		Ext.getCmp('grid-panel').setTitle(title);
	}
	
	function GetResultC(str1,str2,str3){
	    var oBao = new ActiveXObject("Microsoft.XMLHTTP");
	    oBao.open("POST",link9 + "?year="+str1+"&month="+str2+"&day="+str3,false);
	    oBao.send();
	    HandleC(unescape(oBao.responseText))
	} 
	
	function HandleC(str){	
		var arrstr = str.split('|');
		var table = arrstr[0];
		var title = arrstr[1];
		Ext.getCmp('grid-panel').getStore().loadData(eval(table));
		Ext.getCmp('grid-panel').setTitle(title);
	}
	
	</script>
	
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
	<script type="text/javascript" src="${contextPath}/component/chart/Charts/FusionCharts.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/component/chart/js/Data.js" charset="UTF-8"></script>
	
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

			var oXML1="<%=txChart1.trim()%>";
			var oXML2="<%=txChart2.trim()%>";
			var oXML3="<%=txChart3.trim()%>";
			var table = "<%=initTable%>";
			var title = "<%=title%>";

			var data = new Data({},{dataXml1:oXML1,dataXml2:oXML2,dataXml3:oXML3,table:table,title:title});
			data.render('center');
			
			if(Ext.get('loading')){
				setTimeout(function(){
					Ext.get('loading').remove();
			        Ext.get('loading-mask').fadeOut({remove:true});
			    }, 250);
			}
		})
		</script>
    <!-- 加载脚本库  结束 -->
    
    <!-- 主面板 -->
    <div id="center"></div>

  </body>
</html>
