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
	
	<%
		//合同分类编码
		String flbm = request.getParameter("flbm");
	 %>
	 <script type="text/javascript">
	 	var flbm = "<%=flbm%>";
	 	//分类名称
	 	var flmc = "";
	 	var tableName = "";
	 	
	 	if(flbm == "WZ"){
			flmc = "物资合同";
			tableName = "tb_ht_wzht";	
	 	}else if(flbm == "RL"){
			flmc = "燃料合同";		
			tableName = "tb_ht_rlht"; 	
	 	}else if(flbm == "GC"){
			flmc = "工程合同";	
			tableName = "tb_ht_gcht";	 	
	 	}else if(flbm == "YB"){
	 		flmc = "一般立项";
	 		tableName = "tb_ht_yblxd";
	 	}else if(flbm == "WT"){
	 		flmc = "委托立项";
	 		tableName = "tb_ht_wtd";
	 	}else if(flbm == "CW"){
	 		flmc = "财务合同";
	 		tableName = "tb_ht_cwht";
	 	}
	 </script>
			
   	<!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>
		
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/ExTreeEditor.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/ConfirmTextField.js"></script>
	
	<script type="text/javascript" src="script/htCatalogTree.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/htCatalogIndex.js" charset="UTF-8"></script>
	
	<!-- 入口程序 -->
    <script type="text/javascript">
		Ext.onReady(function(){		
			Ext.QuickTips.init();
			//布局为上下两个部分
			var viewport = new Ext.Viewport({
				layout:'border',
				items:[leftTree]
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
