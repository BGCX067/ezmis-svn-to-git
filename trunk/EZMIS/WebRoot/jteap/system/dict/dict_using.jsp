<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<jteap:dict catalog="2222,bbbbb"></jteap:dict>
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

	
	<!-- 入口程序 -->
    <script type="text/javascript">
var dict_1=$dictList("bbbbb");	
		var dictCatalogTree; //字典类型树
		Ext.onReady(function(){
			Ext.QuickTips.init();

var ds = new Ext.data.Store( {
	data: {rows:dict_1},
	reader : new Ext.data.JsonReader( {
		root : 'rows',
		id : 'id'
	}, ['key', 'value', 'id'])
});

			var sss=new Ext.form.ComboBox({
				store:ds,
				valueField :"value",
				displayField: "key",
				mode: 'local',
				forceSelection: true,
				renderTo :'comboDiv',
				triggerAction: 'all',
				blankText:'请选择性别',
				emptyText:'请选择性别',
				hiddenName:'userSex',
				editable: false,
				allowBlank:true,
				id:'sex',
				fieldLabel: '性别',
				listeners:{
					beforeselect :function(combo,record,idx){
						alert(record.json.value);
					}
				},
				name: 'education'
			});
			
		  	//程序加载完成后撤除加载图片
		    setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250);
		});	
    </script>
    <!-- 加载脚本库  结束 -->
    
    <!-- 页面内容 to do something in here -->
	<div id="comboDiv"></div>
  </body>
</html>
