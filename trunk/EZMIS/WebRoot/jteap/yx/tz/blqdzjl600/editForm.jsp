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
						避雷器动作记录(600MW)
					</div>
					
				    <table align="center" height="86.9%" width="97.5%" class="LabelBodyTb">
				    	<tr>
				    		<td class="POPtab-Title">检查人</td>
				    		<td class="POPtabTbEntry2"><div id="divcbr"></div></td>
				    		<td class="POPtab-Title">检查时间</td>
				    		<td class="POPtabTbEntry2"><div id="divcbsj"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">#3主变高压侧A相</td>
				    		<td class="POPtabTbEntry2"><div id="divzbgycA3"></div></td>
				    		<td class="POPtab-Title">#3主变高压侧B相</td>
				    		<td class="POPtabTbEntry2"><div id="divzbgycB3"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">#3主变高压侧C相</td>
				    		<td class="POPtabTbEntry2"><div id="divzbgycC3"></div></td>
				    		<td class="POPtab-Title">#3主变高压侧中性点</td>
				    		<td class="POPtabTbEntry2"><div id="divzbgyczx3"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">#4主变高压侧A相</td>
				    		<td class="POPtabTbEntry2"><div id="divzbgycA4"></div></td>
				    		<td class="POPtab-Title">#4主变高压侧B相</td>
				    		<td class="POPtabTbEntry2"><div id="divzbgycB4"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">#4主变高压侧C相</td>
				    		<td class="POPtabTbEntry2"><div id="divzbgycC4"></div></td>
				    		<td class="POPtab-Title">#4主变高压侧中性点</td>
				    		<td class="POPtabTbEntry2"><div id="divzbgyczx4"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">#02启备变高压侧A相</td>
				    		<td class="POPtabTbEntry2"><div id="divqbbgycA02"></div></td>
				    		<td class="POPtab-Title">#02启备变高压侧B相</td>
				    		<td class="POPtabTbEntry2"><div id="divqbbgycB02"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">#02启备变高压侧C相</td>
				    		<td class="POPtabTbEntry2"><div id="divqbbgycC02"></div></td>
				    		<td class="POPtab-Title">220KV #4母线A相</td>
				    		<td class="POPtabTbEntry2"><div id="divmxA4"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">220KV #4母线B相</td>
				    		<td class="POPtabTbEntry2"><div id="divmxB4"></div></td>
				    		<td class="POPtab-Title">220KV #4母线C相</td>
				    		<td class="POPtabTbEntry2"><div id="divmxC4"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">220KV #5母线A相</td>
				    		<td class="POPtabTbEntry2"><div id="divmxA5"></div></td>
				    		<td class="POPtab-Title">220KV #5母线B相</td>
				    		<td class="POPtabTbEntry2"><div id="divmxB5"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">220KV #5母线C相</td>
				    		<td class="POPtabTbEntry2"><div id="divmxC5"></div></td>
				    		<td class="POPtab-Title">220KV 鄂光线A相</td>
				    		<td class="POPtabTbEntry2"><div id="divegxA"></div></td>
				    	</tr>
			    		<tr>
				    		<td class="POPtab-Title">220KV 鄂光线B相</td>
				    		<td class="POPtabTbEntry2"><div id="divegxB"></div></td>
				    		<td class="POPtab-Title">220KV 鄂光线C相</td>
				    		<td class="POPtabTbEntry2"><div id="divegxC"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">220KV 鄂凤线A相</td>
				    		<td class="POPtabTbEntry2"><div id="divefxA"></div></td>
				    		<td class="POPtab-Title">220KV 鄂凤线B相</td>
				    		<td class="POPtabTbEntry2"><div id="divefxB"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">220KV 鄂凤线C相</td>
				    		<td class="POPtabTbEntry2"><div id="divefxC"></div></td>
				    		<td class="POPtab-Title">220KV 杜山一回A相</td>
				    		<td class="POPtabTbEntry2"><div id="divtsyhA"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">220KV 杜山一回B相</td>
				    		<td class="POPtabTbEntry2"><div id="divtsyhB"></div></td>
				    		<td class="POPtab-Title">220KV 杜山一回C相</td>
				    		<td class="POPtabTbEntry2"><div id="divtsyhC"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">220KV 杜山二回A相</td>
				    		<td class="POPtabTbEntry2"><div id="divtsehA"></div></td>
				    		<td class="POPtab-Title">220KV 杜山二回B相</td>
				    		<td class="POPtabTbEntry2"><div id="divtsehB"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">220KV 杜山二回C相</td>
				    		<td class="POPtabTbEntry2" colspan="3"><div id="divtsehC"></div></td>
				    	</tr>
				    </table>
					
					<%
						String showSave = "";
						String showJx = "";
						if(request.getParameter("modi") != null){
							showJx = "none;";
						}
						
						String query = request.getParameter("query");
						if(query != null){
							showSave = "none;";
							showJx = "none;";
						}
					 %>
					
				    <div class="pop-but">
						<div class="pop-butMain">
							<input type="button" value=" 保 存 " style="display: <%=showSave%>" onclick="save()" class="pop-but01">
							<input type="button" value=" 保存继续 " style="display: <%=showJx%>" onclick="save('jx')" class="pop-but01">
			    			<input type="button" value=" 关 闭 " onclick="window.close();" class="pop-but01">
						</div>
					</div>	
    			</div>
    		</div>
    	</div>
    
    <script type="text/javascript">
    	var query = "<%=query%>";
    	var isReadOnly = false;
    	if(query == "q"){
    		isReadOnly = true;
    	}
    </script>
    
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
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>
		
	<script type="text/javascript" src="script/editForm.js" charset="UTF-8"></script>
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
