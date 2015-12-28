<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp"  %>
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

   	 <!-- 加载脚本库  开始 -->
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
	
	<script type="text/javascript" src="script/GroupNodeUI.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/GroupTree.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/SearchPanel.js" charset="UTF-8"></script>
	
	<script type="text/javascript" src="script/PersonGrid.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/EditForm.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/EditPresonForm.js" charset="UTF-8"></script>
	
	<script type="text/javascript" src="script/GroupAdminGrid.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/GroupEditForm.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/DetailForm.js" charset="UTF-8"></script>
	
	<script type="text/javascript" src="script/DatapermTree.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/DatapemOperation.js" charset="UTF-8"></script>
	<script>
	    var searchAllFs="出生年月#birthday#dateField,工号#userLoginName#textField,登录名#userLoginName2#textField,中文名#userName#textField,性别#sex#textField,联系电话#tel#textField".split(",");
    	var searchDefaultFs="中文名#userName#textField,登录名#userLoginName2#textField,工号#userLoginName#textField".split(",");
	</script>
	<script type="text/javascript" src="script/index.js" charset="UTF-8"></script>
	
	<!-- 入口程序 -->
    <script type="text/javascript">
    	
    	var groupTree;		//组织树

		Ext.onReady(function(){
			Ext.QuickTips.init();
			
			groupTree=new GroupTree();
			
			var viewport=new Ext.Viewport({
				layout:'border',
				items:[groupTree,lyCenter,lyNorth]
			});
			//初始化的时候隐藏查询面板
			searchPanel.collapse(false);
			
		  	//程序加载完成后撤除加载图片
		    setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250);
		    
		    /**
		    *将人员Grid中的数据拖放到组织树中，实现拖放移动人员动作
		
		
		    var drop = new Ext.dd.DropTarget(groupTree.dragZone, {
			    ddGroup : 'GridDD',
			    notifyDrop : function(dd, e, data){
			    	var persons=data.selections;
			    	for(var i=0;i<persons.length;i++){
			    		alert(persons[i].id);
			    	}
			    }
			});
		    */  
		     
		});	
		 
		
		
    </script>
    <!-- 加载脚本库  结束 -->
    
    <!-- 主面板 -->
    <div id="center">
    	center
    </div>

  </body>
</html>
