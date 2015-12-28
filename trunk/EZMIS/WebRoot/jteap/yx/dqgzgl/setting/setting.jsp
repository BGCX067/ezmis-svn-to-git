<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>
	<link rel="stylesheet" href="../../index.css" type="text/css"></link>
  </head>
 
  <body scroll="no" id="index">
	
    <div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						定期工作设置
					</div>
					
				    <table align="center" height="80%" width="97.5%" class="LabelBodyTb">
				    	<tr>
				    		<td class="POPtab-Title">负责部门</td>
				    		<td class="POPtabTbEntry2"><div id="divFzbm"></div></td>
				    		<td class="POPtab-Title">负责岗位</td>
				    		<td class="POPtabTbEntry2"><div id="divFzgw"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">工作规律</td>
				    		<td class="POPtabTbEntry2"><div id="divGzgl"></div></td>
				    		<td class="POPtab-Title">工作专业</td>
				    		<td class="POPtabTbEntry2"><div id="divDqgzzy"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">班次</td>
				    		<td colspan="3" class="POPtabTbEntry2">
				    			&nbsp;&nbsp; 夜班
				    			<input id="ckYeB" type="checkbox">
								&nbsp; 白班
								<input id="ckBaiB" type="checkbox">
								&nbsp; 中班
								<input id="ckZhongB" type="checkbox">
				    		</td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">实验及切换项目</td>
				    		<td colspan="3" class="POPtabTbEntry2">
				    			<div id="divDqgznr"></div>
				    		</td>
				    	</tr>
				    </table>
					
					<%
						String showJx = "";
						if(request.getParameter("modi") != null){
							showJx = "none;";
						}
					 %>
					
				    <div class="pop-but">
						<div class="pop-butMain">
							<input type="button" value=" 保 存 " onclick="save()" class="pop-but01">
							<input type="button" value=" 保存继续 " style="display: <%=showJx%>" onclick="save('jx')" class="pop-but01">
			    			<input type="button" value=" 关 闭 " onclick="window.close();" class="pop-but01">
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
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/ComboTree.js"></script>
	<script type="text/javascript" src="${contextPath}/jteap/system/person/script/GroupNodeUI.js"></script>
	<script type="text/javascript" src="${contextPath}/jteap/system/role/script/RoleNodeUI.js"></script>
	
	<script type="text/javascript" src="script/settingForm.js" charset="UTF-8"></script>
	<!-- 加载脚本库  结束 -->
	
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
    
  </body>
</html>
