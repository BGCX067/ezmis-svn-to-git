<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.wfengine.workflow.manager.FlowConfigManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.wfengine.workflow.model.FlowConfig"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
  </head>
 
 <%
 	String tableName = request.getParameter("formSn");
 	String flowName = "";
 	
 	if("TB_HT_KGQZ".equals(tableName)){
 		flowName = "开工签证";
 	}
 	
 	FlowConfigManager flowConfigManager = (FlowConfigManager) SpringContextUtil.getBean("flowConfigManager");
 	FlowConfig flowConfig = flowConfigManager.getNewFlowConfigByName(flowName);
 	String flowConfigId = "";
 	if (flowConfig != null) {
 		flowConfigId = flowConfig.getId();
 	}
 %>
 <script type="text/javascript">
 		var tableName = "<%=tableName%>";
 		var flowName = "<%=flowName%>";
 		var flowConfigId = "<%=flowConfigId%>";
 		var sn = "";
 </script>
  <body scroll="no" id="index">
	 <!-- 加载等待图标 开始 -->
	<div id="loading-mask" style=""></div>
	<div id="loading">
	  <div class="loading-indicator">
	  	<img src="${contextPath}/resources/extanim32.gif" width="32" height="32" style="margin-right:8px;" align="absmiddle"/>Loading...
	  </div>
	</div>
   	 <!-- 加载等待图标 结束 -->
   	 <script type="text/javascript" src="${contextPath}/script/prototype.js"></script>
   	 
   	 <!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/SearchPanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/ComboTree.js"></script>
	<script type="text/javascript" src="${contextPath}/jteap/system/person/script/GroupNodeUI.js"></script>
	
	<script type="text/javascript" src="script/RightGrid.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/LeftTree.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/SearchPanel.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/index.js" charset="UTF-8"></script>
	
	<!-- 入口程序 -->
    <script type="text/javascript">
		Ext.onReady(function(){			
			Ext.QuickTips.init();
			
			var viewport=new Ext.Viewport({
				layout:'border',
				items:[lyCenter,leftTree,lyNorth]
			});
			
		  	//程序加载完成后撤除加载图片
		    setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250);
		    //默认选中待审批节点
		    leftTree.getRootNode().childNodes[0].select();
		    //隐藏查询面板
		    searchPanel.collapse(false);
		});	
    </script>
    <!-- 加载脚本库  结束 -->
  </body>
</html>
