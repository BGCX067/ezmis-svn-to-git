
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
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
   	 
   	 
   	 <!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/datepicker/DateField.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/datepicker/DatePicker.js"></script>
	
	<!-- 入口程序 -->
    <script type="text/javascript">
    	
		Ext.onReady(function(){			
			Ext.QuickTips.init();
			//to do in the program
			var dtSelector  = new Ext.app.DateField({  
						id : 'dateSection',
						fieldLabel : '选择指标月份',
						format : 'Y-m',
						renderTo:'yyyyMM',
						name : 'dateSection',
						allowBlank : false,
						readOnly :true,
						anchor : '90%'
					});
					
					//选择年
		var dtSelector2 = new Ext.app.DateField({  
						id : 'dateSection2',
						fieldLabel : '选择指标年份',
						format : 'Y',
						name : 'dateSection2',
						renderTo:'yyyy',
						allowBlank : false,
						readOnly :true,
						anchor : '90%'
					});
var dtSelector2 = new Ext.form.DateField({  
						id : 'dateSection3',
						fieldLabel : '选择指标年份',
						name : 'dateSection3',
						renderTo:'zzz',
						allowBlank : false,
						readOnly :true,
						anchor : '90%'
					});					
		  	//程序加载完成后撤除加载图片
		    setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250);
		    
		 //  初始化的时候隐藏查询面板
		 //  searchPanel.collapse(false);
		});	
    </script>
    <!-- 加载脚本库  结束 -->
    <div id="yyyyMM">
    //选择年月
		
    </div>
     <div id="yyyy">
    //选择年
		
    </div>
     <div id="zzz">
    //选择年月日
		
    </div>
    <!-- 页面内容 to do something in here -->

  </body>
</html>
