<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.yx.jjb.manager.JiaoJieBanManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.yx.jjb.model.JiaoJieBan"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<link rel="stylesheet" type="text/css" href="${contextPath}/script/ext-extend/form/Datetime/datetime.css"></link>
  </head>
 
  <body scroll="no" onload="setUserloginName();">
  	
  	<!-- 数据字典 -->
  		<!-- 值班时间定义,值班班次 -->
  	<jteap:dict catalog="zbbc_sj_dy,zbbc"></jteap:dict>
  	
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
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/UniqueTextField.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelPanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/Datetime/Datetime.js"></script>
	
	<%
		//岗位类别
		String gwlb = request.getParameter("gwlb");
		String fl = request.getParameter("fl");
		String formSn = request.getParameter("formSn");
		String jizhangtype = request.getParameter("jizhangtype");
		JiaoJieBanManager jiaoJieBanManager = (JiaoJieBanManager)SpringContextUtil.getBean("jiaoJieBanManager");
		//获取第一个交接班信息
		JiaoJieBan firstJiaoJieBan = jiaoJieBanManager.findFirstOrLast("first",gwlb);
		//获取最后一个交接班信息
		JiaoJieBan lastJiaoJieBan = jiaoJieBanManager.findFirstOrLast("last",gwlb);
	 %>
	 <script type="text/javascript">
	 	var flag = false;
	 	var username = '';
	 	var flagUser = '';
	 	var flagGwlb = '';
		var gwlb = "<%=gwlb%>";
		var superGwlb = gwlb;
		var fl = "<%=fl%>";
		var formSn = "<%=formSn%>";
		var jizhangtype = "<%=jizhangtype%>";
		if(gwlb == "电气"){
			gwlb = fl;
		}
		
	 	var zbzb = "";
	 	var lastJjbsj = "";
	 	var lastJieBanbc = "";
	 	var firstJjbsj = "";
	 	var firstJieBanbc = "";
	 	
		var nowDate = new Date();
		var nowYmd = nowDate.format('Y-m-d');
		var nowHms = nowDate.format('H:i:s');
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
		
		 if(nowHms >= yeB && nowHms <= yeE){
			nowBc = "夜班";
		}else if(nowHms >= baiB && nowHms <= baiE){
			nowBc = "白班";
		}else if(nowHms >= zhongB && nowHms <= zhongE){
			nowBc = "中班";
		}
		
		//初始化交接班时间、班次
		lastJjbsj = nowYmd;
		firstJjbsj = nowYmd;
		lastJieBanbc = nowBc;
		firstJieBanbc = nowBc;
 	/** end */
 		
		<%
		if(lastJiaoJieBan != null){
		%>
		 	//最后一个交接班时间
		 	lastJjbsj = "<%=lastJiaoJieBan.getJjbsj()%>";
		 	//最后一个交接班的 接班班次
		 	lastJieBanbc = "<%=lastJiaoJieBan.getJiebanbc()%>";
		 	//值班值别
		 	zbzb = "<%=lastJiaoJieBan.getJiebanzb()%>";
		 	
		 	//第一个交接班时间
		 	firstJjbsj = "<%=firstJiaoJieBan.getJjbsj()%>";
		 	//第一个交接班的 接班班次
		 	firstJieBanbc = "<%=firstJiaoJieBan.getJiebanbc()%>";
	 	<%
	 	}
	 	%>
	 </script>
	
	<script type="text/javascript" src="script/RightGrid.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/tzGrid.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/shenYueForm.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/lockForm.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/index.js" charset="UTF-8"></script>
	<!-- 入口程序 -->
    <script type="text/javascript">
		Ext.onReady(function(){			
			Ext.QuickTips.init();
			var viewport=new Ext.Viewport({
				layout:'border',
				items:[lyCenter,lySouth]
			});
		  	//程序加载完成后撤除加载图片
		    setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250);
		});	
    </script>
    <!-- 加载脚本库  结束 -->
    
  </body>
</html>
