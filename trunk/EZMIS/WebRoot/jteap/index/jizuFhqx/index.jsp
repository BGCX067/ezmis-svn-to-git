<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.index.manager.JizuFhqxManager"%>
<%@ include file="/inc/import.jsp" %>

<% 
	response.addHeader("pragma", "no-cache");
	response.addHeader("cache-control", "no-cache");
	response.addDateHeader("expries", 0);
	
	String jizu = "3";
	String isMax = "0";
	String imgTitleScr = "images/ico-maximize.gif";
	
	if(request.getParameter("jizu") != null){
		jizu = request.getParameter("jizu");
	}
	if(request.getParameter("isMax") != null){
		isMax = request.getParameter("isMax");
	}
	if("1".equals(isMax)){
		imgTitleScr = "images/ico-minimize.gif";
	}
	
	JizuFhqxManager jizuFhqxManager = (JizuFhqxManager)SpringContextUtil.getBean("jizuFhqxManager");
	Map<String, Object> map = jizuFhqxManager.findByCdDesc(jizu);
	
	String txChart = (String)map.get("chartData");
	if(txChart==null){
		txChart="";
	}
%>

<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<title>JTEAP 2.0</title>

	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<style type="text/css">
		table {
			font-size: 12px;
		}
	</style>
  </head>
 
  <body scroll="no" id="index">

   	 <!-- 加载脚本库  开始 -->
   	 <%@ include file="/inc/ext-all.jsp" %>

	<script type="text/javascript" src="${contextPath}/component/chart/js/xml.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/component/chart/Charts/FusionCharts.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/component/chart/js/MSLine.js" charset="UTF-8"></script>
	
	<!-- 入口程序 -->
   		<script type="text/javascript">

		Ext.onReady(function(){
			Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
			Ext.QuickTips.init();
			
			var oXML=XMLUtil.load_xml('<%=txChart.trim()%>');
			
			var chart1 = new FusionCharts(contextPath+"/component/chart/Charts/MSLine.swf", "chart1Id", document.body.offsetWidth-5, document.body.offsetHeight-20);
			chart1.setDataXML(oXML.xml);
			chart1.setTransparent(true);
			chart1.render("divCenter");
		})
		</script>
    <!-- 加载脚本库  结束 -->
    
    <script type="text/javascript">
	   	//页面每小时刷新一次
	   	setTimeout("self.location.reload();",3600000);
   		
   		//机组号
   		var jizuNum = "<%=jizu%>";
		//是否最大化
		var isMax = "<%=isMax%>";
		
		var curWith = window.document.body.clientWidth;
		var curHeigth = window.document.body.clientHeight;
		
   		//最大化最小化rightFrame 
		function toMaxRightFrameWindow(){
			if("0" == isMax){
				//最大化
				window.moveTo(2.5,2.5);
				window.resizeTo(curWith*2,curHeigth*2);
				isMax = 1;
				
				window.location.replace("index.jsp?jizu=<%=jizu%>&isMax="+isMax);
			}else{	
				/**		
				//当前IF移动像素值x,y
				var curX = 2.5;
				var curY = 2.5;
				
				if("2" == jizuNum){
					curX = curWith/2 + 5;
				}else if("3" == jizuNum){
					curY = curHeigth/2 + 5;
				}else if("4" == jizuNum){
					curX = curWith/2 + 5;
					curY = curHeigth/2 + 5;
				}
				
				//最小化
				window.moveTo(curX,curY);
				window.resizeTo(curWith/2,curHeigth/2);
				isMax = 0;
				*/
				
				window.parent.location.reload();
			}
		}
    </script>
    
	<table align="center" cellspacing="0">
		<tr style="background-color: lightblue;" onclick="toMaxRightFrameWindow()">
			<td>
				<table width="100%" cellpadding="0" cellspacing="0">
					<tr>
						<td width="33%">&nbsp;</td>
						<td width="34%" align="center">
							#<%=jizu %>机组负荷
						</td>
						<td width="33%" align="right">
							<img id="imgTitle" src="<%=imgTitleScr %>" width="16" height="16" align="middle" alt="缩放" />	
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<div id="divCenter"></div>
			</td>
		</tr>
	</table>
  </body>
</html>
