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
						厂用电统计(300MW)
					</div>
					
				    <table align="center" height="85.5%" width="97.5%" class="LabelBodyTb">
				    	<tr>
				    		<td class="POPtab-Title">抄表人</td>
				    		<td class="POPtabTbEntry2"><div id="divcbr"></div></td>
				    		<td class="POPtab-Title">抄表时间</td>
				    		<td class="POPtabTbEntry2"><div id="divcbsj"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">值别</td>
				    		<td class="POPtabTbEntry2"><div id="divzb"></div></td>
				    		<td class="POPtab-Title">填写人</td>
				    		<td class="POPtabTbEntry2"><div id="divzb"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">#01主变(峰值) 表码</td>
				    		<td class="POPtabTbEntry2"><div id="divzbf_1"></div></td>
				    		<td class="POPtab-Title">#02主变(峰值) 表码</td>
				    		<td class="POPtabTbEntry2"><div id="divzbf_2"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">#01主变(平值) 表码</td>
				    		<td class="POPtabTbEntry2"><div id="divzbp_1"></div></td>
				    		<td class="POPtab-Title">#02主变(平值) 表码</td>
				    		<td class="POPtabTbEntry2"><div id="divzbp_2"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">#01主变(谷值) 表码</td>
				    		<td class="POPtabTbEntry2"><div id="divzbg_1"></div></td>
				    		<td class="POPtab-Title">#02主变(谷值) 表码</td>
				    		<td class="POPtabTbEntry2"><div id="divzbg_2"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">启备变E06(峰) 表码</td>
				    		<td class="POPtabTbEntry2"><div id="divqbbf"></div></td>
				    		<td class="POPtab-Title">#1高厂变 表码</td>
				    		<td class="POPtabTbEntry2"><div id="divgcb_1"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">启备变E06(平) 表码</td>
				    		<td class="POPtabTbEntry2"><div id="divqbbp"></div></td>
				    		<td class="POPtab-Title">#2高厂变 表码</td>
				    		<td class="POPtabTbEntry2"><div id="divgcb_2"></div></td>
				    		
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">启备变E06(谷) 表码</td>
				    		<td class="POPtabTbEntry2"><div id="divqbbg"></div></td>
				    		<td class="POPtab-Title">#1尾水发电 表码</td>
				    		<td class="POPtabTbEntry2"><div id="divwsfd_1"></div></td>
				    		
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">集控启备变 表码</td>
				    		<td class="POPtabTbEntry2"><div id="divjkqbb"></div></td>
				    		<td class="POPtab-Title">#2尾水发电 表码</td>
				    		<td class="POPtabTbEntry2"><div id="divwsfd_2"></div></td>
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
