<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>
	<link rel="stylesheet" href="../../index.css" type="text/css"></link>
	<link rel="stylesheet" type="text/css" href="${contextPath}/script/ext-extend/form/Datetime/datetime.css"></link>
	<!-- 数据字典 -->
	<jteap:dict catalog="zbzb"></jteap:dict>
  </head>
 
  <body scroll="no" id="index">
	
    <div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						厂用电统计(600MW)
					</div>
					
				    <table align="center" height="74.5%" width="97.5%" class="LabelBodyTb">
				    	<tr>
				    		<td class="POPtab-Title">抄表人</td>
				    		<td class="POPtabTbEntry2"><div id="divcbr"></div></td>
				    		<td class="POPtab-Title">抄表时间</td>
				    		<td class="POPtabTbEntry2"><div id="divcbsj"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">值别</td>
				    		<td class="POPtabTbEntry2"><div id="divzb"></div></td>
				    		<td class="POPtab-Title">#02启备变 表码</td>
				    		<td class="POPtabTbEntry2"><div id="divqbb_2"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">#3发电机有功 表码</td>
				    		<td class="POPtabTbEntry2"><div id="divfdjyg_3"></div></td>
				    		<td class="POPtab-Title">#3高厂变 表码</td>
				    		<td class="POPtabTbEntry2"><div id="divgcb_3"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">#4发电机有功 表码</td>
				    		<td class="POPtabTbEntry2"><div id="divfdjyg_4"></div></td>
				    		<td class="POPtab-Title">#4高厂变 表码</td>
				    		<td class="POPtabTbEntry2"><div id="divgcb_4"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">#3励磁变 表码</td>
				    		<td class="POPtabTbEntry2"><div id="divlcb_3"></div></td>
				    		<td class="POPtab-Title">#4励磁变 表码</td>
				    		<td class="POPtabTbEntry2"><div id="divlcb_4"></div></td>
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
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/Datetime/Datetime.js"></script>
	
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
