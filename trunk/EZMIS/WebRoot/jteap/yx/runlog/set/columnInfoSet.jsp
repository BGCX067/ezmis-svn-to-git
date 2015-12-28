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
					指标信息设置
				</div>
				
			    <table align="center" height="150" width="97.5%" class="LabelBodyTb">
			    	<tr>
			    		<td class="POPtab-Title">中文名称</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divColumnName"></div>
			    		</td>
			    		<td class="POPtab-Title">英文名称</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divColumnCode"></div>
			    		</td>
			    	</tr>
			    	<tr>
			    		<td class="POPtab-Title">排序号</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divSortno"></div>
			    		</td>
			    		<td class="POPtab-Title">额定值</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divEdingzhi"></div>
			    		</td>
			    	</tr>
			    	<tr>
			    		<td class="POPtab-Title">计量单位</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divJiliangdanwei"></div>
			    		</td>
			    		<td class="POPtab-Title">取数机组</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divJizu"></div>
			    		</td>
			    	</tr>
			    	<tr>
			    		<td class="POPtab-Title">取数表编码</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divDataTableCode"></div>
			    		</td>
			    		<td class="POPtab-Title">测点编码</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divSisCedianbianma"></div>
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
	
	<script type="text/javascript" src="script/columnInfoSet.js" charset="UTF-8"></script>
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
