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
	<script type="text/javascript" src="script/ProjSelect1.js" charset="UTF-8"></script>
	<!-- 入口程序 -->
    <script type="text/javascript">
    	var groupTree = new GroupTree() ;
    	//判断本页面为单选还是多选的参数onlyOne
   		onlyOne = false ;
   		if(window.dialogArguments != null){
	   		if(window.dialogArguments.onlyOne == "true"){
	   			onlyOne = true  ;
	   		} 
   		}
		Ext.onReady(function(){			
			Ext.QuickTips.init();
			groupTree.on('dblclick',function(node,event){
							
							//this.result.id = node.id;
    						//this.result.text = node.text;
							//window.returnValue = this.result;
							//oWindow.close();
							//window.close();
						});
			var oWindow = new Ext.Window({
			    id:'oWindow',
				width : 300,
	//			height : 500,
				title : '项目选择' ,
				closable : false ,
				//layout : 'border' ,
				buttonAlign : 'center' ,
				draggable : false ,
				buttons : [
					{
						xtype : 'button' ,
						text : '确定' ,
						handler : function() {
						    var node = groupTree.getSelectionModel().getSelectedNode();
							if(!node.parentNode || node.parentNode.attributes.text=='工程类别' ){
								alert('请选择项目');
								return ;
							}
							if(!node.leaf){
								alert('只能选择叶子节点');
								return ;
							}
							window.returnValue=node.attributes.text+'|'+node.attributes.projcatName;
							oWindow.close();
							window.close();
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
					groupTree
				]
			}) ;
			oWindow.show() ;
			
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
