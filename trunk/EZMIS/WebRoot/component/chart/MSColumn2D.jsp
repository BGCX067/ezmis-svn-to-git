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
	
	String txChart=(String)request.getAttribute("txtChart");
	String initTable = (String)request.getAttribute("initTable");
	String year = (String)request.getAttribute("year");
	
	System.out.println(txChart);
	System.out.println(initTable);
	System.out.println(year);
	
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
	<script type="text/javascript" src="${contextPath}/component/chart/Charts/FusionCharts.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/component/chart/js/MSColumn2D.js" charset="UTF-8"></script>
	
<script type="text/javascript">
	function GetResult(str){
	
	/*--------------- GetResult(str) -----------------
	
	* 功能:通过XMLHTTP发送请求,返回结果.
	
	* 实例:GetResult(document.all.userid.value);
	
	*--------------- GetResult(str) ----------------- */
	
	    var oBao = new ActiveXObject("Microsoft.XMLHTTP");
	
	    oBao.open("POST",link2 + "?year="+str,false);
	
	    oBao.send();
	
	    //服务器端处理返回的是经过escape编码的字符串.
	
	    //通过XMLHTTP返回数据,开始构建Select.
	
	    BuildSel(unescape(oBao.responseText),document.all.sel2)
	
	} 
	
	function BuildSel(str,sel){	
		var arrstr = new Array();
	    arrstr = str.split("|");
	    var oXML=XMLUtil.load_xml(arrstr[0]);
		var table = arrstr[1];
		
		var chart1 = new FusionCharts(contextPath+"/component/chart/Charts/MSColumn2D.swf", "chart1Id", document.body.offsetWidth-10, document.body.offsetHeight-200);
		chart1.setDataXML(oXML.xml);
		chart1.setTransparent(true);
		chart1.render("flashcontent");
		
		Ext.getDom("tablecontent").innerHTML = table;
	}


	
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
			var table = "<%=initTable.trim()%>";
			var years = "<%=year.trim()%>".split(",");
			
			var mscolumn2d = new MSColumn2D({},{dataXml:oXML.xml,table:table,years:years});
			mscolumn2d.render('center');
			
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
    <div id="center">

    </div>

  </body>
</html>
