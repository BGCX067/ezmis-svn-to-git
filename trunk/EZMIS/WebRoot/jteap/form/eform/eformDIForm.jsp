<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.core.utils.StringUtil"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.form.dbdef.manager.DefColumnInfoManager"%>
<%@page import="com.jteap.form.dbdef.model.DefTableInfo"%>
<%@page import="com.jteap.form.dbdef.manager.DefTableInfoManager"%>
<%@page import="com.jteap.core.utils.JSONUtil"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<title>数据项设置</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<style>
	</style>
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
   	 
   	 
   	 <!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>
	<script>

	
	<%
		String defTableId = request.getParameter("defTableId");
		DefTableInfoManager defTableInfoManager = (DefTableInfoManager)SpringContextUtil.getBean("defTableInfoManager");
		DefTableInfo defTableInfo = defTableInfoManager.get(defTableId);
		String columnList = JSONUtil.listToJson(defTableInfo.getColumns(),new String[]{"id","columncode","columnname","columntype","columnlength","defaultvalue","allownull","pk","columnprec","columnorder","comm"});
	%>
	
	eval('var defColumns={data:<%=columnList%>}');
	
	//表定义schema节点
	var link1="${contextPath}/jteap/form/dbdef/DefTableInfoAction!showTreeSchemaAction.do";
	//请求表定义树形结构
	var link2="${contextPath}/jteap/form/dbdef/DefTableInfoAction!showTreeAction.do";
	//请求数据表的列信息
	var link3 ="${contextPath}/jteap/form/dbdef/DefColumnInfoAction!findColumnListByTableIdAction.do";
	</script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelPanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelValuePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/TitlePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/hzToPy.js" charset="UTF-8"></script>
	
	<script type="text/javascript" src="script/ExPropertyGrid.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/eformDIForm.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/eformDIFormEditorProperty.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/eformDIFormEditorPropertyMany.js" charset="UTF-8"></script>
	<!-- 入口程序 -->
    <script type="text/javascript">
    	
		Ext.onReady(function(){			
			Ext.QuickTips.init();
			var viewport=new Ext.Viewport({
				layout:'border',
				items:[titlePanel,lyCenter]
			});
		
			onload();
			
		  	//程序加载完成后撤除加载图片
		    setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250);
		});	
    </script>
    <!-- 加载脚本库  结束 -->
    
    <!-- 页面内容 to do something in here -->
	
  </body>
</html>
