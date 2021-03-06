<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<link rel="stylesheet" href="${contextPath}/script/ext-extend/MultiselectItemSelector/Multiselect.css" type="text/css"></link>
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
	
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/ExTreeEditor.js"></script>
   	<script type="text/javascript" src="${contextPath}/script/ext-extend/ConfirmTextField.js"></script>	 
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelPanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelValuePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/TitlePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/ComboTree.js"></script>	
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/ComboGrid.js"></script>	
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/UniqueTextField.js" charset="UTF-8"></script>	
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/SearchPanel.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNodeUI.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNodeUI.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNode.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNodeLoader.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxAsyncTreeNode.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/grid/NewRecEditGrid.js" charset="UTF-8"></script>
	
	
	<script type="text/javascript" src="${contextPath}/script/ext-extend/MultiselectItemSelector/Multiselect.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/MultiselectItemSelector/DDView.js" charset="UTF-8"></script>
	
	
	
	<script type="text/javascript" src="${contextPath}/jteap/system/role/script/RoleNodeUI.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/jteap/system/role/script/RoleTree.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/GroupNodeUI.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/PersonSelect.js" charset="UTF-8"></script>
	<!-- 入口程序 -->
    <script type="text/javascript">
    	//判断本页面为单选还是多选的参数onlyOne
   		onlyOne = false ;
   		if(window.dialogArguments != null){
	   		if(window.dialogArguments.onlyOne == "true"){
	   			onlyOne = true  ;
	   		} 
   		}
		Ext.onReady(function(){			
			Ext.QuickTips.init();
			roleGroupTreeTabPanel = new RoleGroupTreeTabPanel() ;
			//中间的SearchPanel
			//用户查询面板								
	//		var searchAllFs="用户名#userLoginName#textField,昵称#userName#textField,性别#sex#textField".split(",");
	//		var searchDefaultFs="用户名#userLoginName#textField,昵称#userName#textField,性别#sex#textField".split(",");
	//		var searchPanel=new CenterSearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs,region:'north',labelWidth : 45,txtWidth: 80 ,width: 525});
			centerPersonGrid = new CenterPersonGrid() ;
			centerPersonGrid.region = 'center' ;
			centerPersonGrid.getStore().load() ;
			var oWindow = new Ext.Window({
			    id:'oWindow',
				width : 645,
				height : 500 ,
				title : '人员选择' ,
				closable : false ,
				layout : 'border' ,
				buttonAlign : 'center' ,
				draggable : false ,
				buttons : [
					{
						xtype : 'button' ,
						text : '确定' ,
						handler : function() {
						    var result={};
							var obj = centerPersonGrid.getSelections()[0];
							if(obj) {
							   if(obj.data.id)  result.loginName = obj.data.id;
							   	  else result.loginName = obj.data['person.id'];
							   if(obj.data.userName) result.name = obj.data.userName;
							      else result.name = obj.data['person.userName'];
							}else result=null;
							window.returnValue = result ;
							oWindow.close() ;
							window.close() ;
						}
					},
					{
						xtype : 'button' ,
						text : '取消' ,
						handler : function() {
							oWindow.close() ;
							window.close() ;
						}
					}
				],
				items : [
					roleGroupTreeTabPanel,
					{
						xtype:'panel',
						layout:'border' ,
						border : false ,
						bodyBorder : false ,
						region : 'center' ,
						items : [centerPersonGrid]
					}
				//	,rightListBox
				]
			}) ;
			oWindow.show() ;
			
			//初始化的时候隐藏查询面板
		//	searchPanel.collapse(false);
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
