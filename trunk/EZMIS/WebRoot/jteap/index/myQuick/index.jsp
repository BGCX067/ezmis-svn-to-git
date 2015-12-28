<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.index.manager.MyQuickManager"%>
<%@page import="com.jteap.index.model.MyQuick"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
  </head>
 
  <body id="index" scroll="no">
  	
  	<%@ include file="/inc/ext-all.jsp" %>
	<script type="text/javascript">
  		function setMyQuick(){
	  		var url = contextPath + '/jteap/index/myQuick/setMyQuickCkTree.jsp';
			showIFModule(url,"设置我的快捷","true",317,400,{});
			window.location.reload();
  		}
  	</script>
  	
  	<%
  		//获取当前登录用户的快捷配置
  		MyQuickManager myQuickManager = (MyQuickManager)SpringContextUtil.getBean("myQuickManager");
  		MyQuick myQuick = myQuickManager.findUniqueBy("personId",curPersonId);
  		if(myQuick == null || myQuick.getModuleIds() == null){
  			out.print("<a href='javascript:void(0);' style='color:red;font-size:12px;font-weight:bold;' onclick='setMyQuick()'>您未设置快捷,点击设置...</a>");
  			return;
  		}
  	 %>
  	
	 <!-- 加载等待图标 开始 -->
	<div id="loading-mask" style=""></div>
	<div id="loading">
	  <div class="loading-indicator">
	  	<img src="${contextPath}/resources/extanim32.gif" width="32" height="32" style="margin-right:8px;" align="absmiddle"/>Loading...
	  </div>
	</div>
   	 <!-- 加载等待图标 结束 -->
   	 
   	 <!-- 加载脚本库  开始  -->
	<script type="text/javascript" src="script/index.js" charset="UTF-8"></script>
	<!-- 加载脚本库  结束 -->
	
	<!-- 入口程序 -->
    <script type="text/javascript">
		Ext.onReady(function(){			
			//布局为上下两个部分
			var viewport = new Ext.Viewport({
				layout:'border',
				items:[lyCenter]
			});
		  	//程序加载完成后撤除加载图片
		    setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250);
		});	
	</script>	
	
  </body>
</html>
