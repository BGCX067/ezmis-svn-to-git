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
  	
  	<!-- 数据字典 --> 
  		<!-- 值班班次时间定义、值班班次 -->
 	<jteap:dict catalog="zbbc_sj_dy,zbbc,zbzb"></jteap:dict>
  	
	 <!-- 加载等待图标 开始 -->
	<div id="loading-mask" style=""></div>
	<div id="loading">
	  <div class="loading-indicator">
	  	<img src="${contextPath}/resources/extanim32.gif" width="32" height="32" style="margin-right:8px;" align="absmiddle"/>Loading...
	  </div>
	</div>
   	 <!-- 加载等待图标 结束 -->
   	 
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
		//现在班次
		var nowBc = "";
		
	/** 根据 数据字典初始化 */
		//夜班开始、结束时间
		var yeB = $dictValue("zbbc_sj_dy","ye_beginTime");
		var yeE = $dictValue("zbbc_sj_dy","ye_endTime");
		//白班开始、结束时间
		var baiB = $dictValue("zbbc_sj_dy","bai_beginTime");
		var baiE = $dictValue("zbbc_sj_dy","bai_endTime");
		//中班开始、结束时间
		var zhongB = $dictValue("zbbc_sj_dy","zhong_beginTime");
		var zhongE = $dictValue("zbbc_sj_dy","zhong_endTime");
		
		//获取初始交班班次
		 if(nowHms >= yeB && nowHms <= yeE){
			nowBc = "夜班";
		}else if(nowHms >= baiB && nowHms <= baiE){
			nowBc = "白班";
		}else if(nowHms >= zhongB && nowHms <= zhongE){
			nowBc = "中班";
		}
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
