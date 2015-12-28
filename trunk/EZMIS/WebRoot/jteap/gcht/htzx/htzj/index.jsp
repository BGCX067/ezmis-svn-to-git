<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
  </head>
 
 <%
 	//合同类型,物资(1)、燃料合同(2).
 	String tableName = request.getParameter("formSn");
 	String childTableName = "";
 	String flowName = "";
 	String flbm = "";
 	String flmc = "";
 	
 	if("TB_HT_WZHT".equals(tableName)){
 		flowName = "物资合同付款审批";
 		flmc = "物资合同";
 		flbm = "WZ";
 		childTableName = "TB_HT_WZHTFK";
 	}else if("TB_HT_GCHT".equals(tableName)){
 		flowName = "工程合同付款审批";
 		flmc = "工程合同";
 		flbm = "GC";
 		childTableName = "TB_HT_GCHTFK";
 	}else if("TB_HT_RLHT".equals(tableName)){
 		flowName = "燃料合同付款审批";
 		flmc = "燃料合同";
 		flbm = "RL";
 		childTableName = "TB_HT_RLHTFK";
 	}else if("TB_HT_CWHT".equals(tableName)){
 		flowName = "财务合同付款审批";
 		flmc = "财务合同";
 		flbm = "CW";
 		childTableName = "TB_HT_CWHTFK";
 	}
 %>
 <script type="text/javascript">
 		var tableName = "<%=tableName%>";
 		var flowName = "<%=flowName%>";
 		var flbm = "<%=flbm%>";
 		var flmc = "<%=flmc%>";
 		var childTableName = "<%=childTableName%>";
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
		    //隐藏查询面板
		    searchPanel.collapse(false);
		    //默认选中根节点
		    leftTree.getRootNode().select();
		});	
    </script>
    <!-- 加载脚本库  结束 -->
  </body>
</html>
