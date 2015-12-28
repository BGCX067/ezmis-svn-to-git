<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>
  </head>
  
  <body scroll="no" id="index">
 
	 <!-- 加载等待图标 开始 -->
	<div id="loading-mask" style=""></div>
	<div id="loading">
	  <div class="loading-indicator">
	  	<img src="${contextPath}/resources/extanim32.gif" width="32" height="32" style="margin-right:8px;" align="absmiddle"/>Loading...
	  </div>
	</div>
   	 <!-- 加载等待图标 结束 -->
  	<!-- 数据字典 -->
  	<jteap:dict catalog="SCFX_XZ,jizu"></jteap:dict>
  	
   	<!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/SearchPanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>
	
	<script type="text/javascript">
		var formSn = "<%=request.getParameter("formSn")%>";
		//当前接班时间
		var nowDate = new Date();
		//当前日期
		var nowYmd = nowDate.format('Y-m-d');
		var nowYmdHi = nowDate.format('Y-m-d H:i');
		//现在 时、分、秒
		var nowHms = nowDate.format('H:i:s');
		
		var Nowdate=new Date(); 
        var monthFirstDay=new Date(Nowdate.getYear(),Nowdate.getMonth(),1);
        var MonthFirstDay=monthFirstDay.format('Y-m-d');

	</script>
  	
	<script type="text/javascript" src="script/SearchPanel.js" charset="UTF-8"></script>	
	<script type="text/javascript" src="script/RightGrid.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/index.js" charset="UTF-8"></script>
	
	<!-- 入口程序 -->
    <script type="text/javascript">
		Ext.onReady(function(){			
			Ext.QuickTips.init();
			var viewport=new Ext.Viewport({
				layout:'border',
				items:[lyCenter,lyNorth]
			});
		  	//程序加载完成后撤除加载图片
		    setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250);
		    searchPanel.collapse(false);
			searchPanel.expand(true);
		});	
    </script>
    <!-- 加载脚本库  结束 -->
    
    <!-- 页面内容 to do something in here -->

  </body>
</html>
