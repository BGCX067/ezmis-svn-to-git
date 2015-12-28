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
		<div class="pop-out">
		<div class="pop-in">
			<div class="pop-main">
				<div class="pop-title">
					设备名称选择
				</div>
				
			    <table align="center" height="120" width="97.5%" class="LabelBodyTb">
			    	<tr>
			    		<td class="POPtabTbEntry2">
			    			<div id="divLeftTree"></div>
			    		</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divRightGrid"></div>
			    		</td>
			    	</tr>
			    </table>
				
			    <div class="pop-but">
					<div class="pop-butMain">
						<input type="button" id="save" value=" 保 存 " onclick="btnYxtz_Click();" class="pop-but01">
		    			<input type="button" id="colse" value=" 关 闭 " onclick="window.close();" class="pop-but01">
					</div>
				</div>	
   			</div>
   		</div>
   	</div>
   	
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
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/Datetime/Datetime.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/ExTreeEditor.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/ConfirmTextField.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/SearchPanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/ComboTree.js"></script>
	<script type="text/javascript" src="${contextPath}/jteap/system/person/script/GroupNodeUI.js"></script>
	<script type="text/javascript" src="${contextPath}/jteap/system/role/script/RoleNodeUI.js"></script>
	<script type="text/javascript" src="script/LeftTree.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/RightGrid.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/index.js" charset="UTF-8"></script>

	<!-- 入口程序 -->
    <script type="text/javascript">
		Ext.onReady(function(){		
			Ext.QuickTips.init();
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
