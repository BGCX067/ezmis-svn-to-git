<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>
	<link rel="stylesheet" href="../../index.css" type="text/css"></link>
	<%
		String sbtzCatalogId = (String)request.getParameter("sbtzCatalogId");
		String type = (String)request.getParameter("type");
		String sblx = (String)request.getParameter("sblx");
	 %>
  </head>

	<body scroll="no" id="index">
		<div class="pop-out">
		<div class="pop-in">
			<div class="pop-main">
				<div class="pop-title">
					<%
						if("2".equals(sblx)){
					%>
						设备运行台帐信息
					<%
						}else{
					 %>
					 	设备基础台帐信息
					<%
						}
					 %>
				</div>
				
			    <table align="center" height="150" width="97.5%" class="LabelBodyTb">
			    	<tr>
			    		<td class="POPtab-Title">KKS码</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divKKS"></div>
			    		</td>
			    		<td class="POPtab-Title">设备编码</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divSbbm"></div>
			    		</td>
			    	</tr>
			    	<tr>
			    		<td class="POPtab-Title">仪表名称</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divYbmc"></div>
			    		</td>
			    		<td class="POPtab-Title">型式及规范</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divXsjgf"></div>
			    		</td>
			    	</tr>
			    	<tr>
			    		<td class="POPtab-Title">单位</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divDw"></div>
			    		</td>
			    		<td class="POPtab-Title">数量</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divSl"></div>
			    		</td>
			    	</tr>
			    	<tr>
			    		<td class="POPtab-Title">安装地点</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divAzdd"></div>
			    		</td>
			    		<td class="POPtab-Title">系统图号</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divXtth"></div>
			    		</td>
			    	</tr>
			    	<tr>
			    		<td class="POPtab-Title">用途</td>
			    		<td class="POPtabTbEntry2" colspan="3">
			    			<div id="divYt"></div>
			    		</td>
			    	</tr>
			    	<tr>
			    		<td class="POPtab-Title">备注</td>
			    		<td class="POPtabTbEntry2" colspan="3">
			    			<div id="divRemark"></div>
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
						<input type="button" id="save" value=" 保 存 " onclick="save()" class="pop-but01">
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
	<script type="text/javascript">
		var sbtzCatalogId = '<%=sbtzCatalogId%>';
		var type = '<%=type%>';
		if(type == "show"){
			$('save').style.display = 'none';
		}
	</script>
	<script type="text/javascript" src="script/jctzinfo.js" charset="UTF-8"></script>
		
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
