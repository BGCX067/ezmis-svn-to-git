<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>
	<link rel="stylesheet" href="../../index.css" type="text/css"></link>
	<script type="text/javascript" src="${contextPath}/script/date.js" charset="UTF-8"></script>
	<%
		String sbpjCatalogId = (String)request.getParameter("sbpjCatalogId");
		String type = (String)request.getParameter("type");
	 %>
  </head>

	<body scroll="no" id="index">
		<div class="pop-out">
		<div class="pop-in">
			<div class="pop-main">
				<div class="pop-title">
					设备评级信息
				</div>
				
			    <table align="center" height="120" width="97.5%" class="LabelBodyTb">
			    	<tr>
			    		<td class="POPtab-Title">评级分类</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divPjfl"></div>
			    		</td>
			    		<td class="POPtab-Title">设备名称</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divSbmc"></div>
			    		</td>
			    		<td class="POPtab-Title">设备规格</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divSbgg"></div>
			    		</td>
			    	</tr>
			    	<tr>
			    		<td class="POPtab-Title">上次评级日期</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divScpjrq"></div>
			    		</td>
			    		<td class="POPtab-Title">上次评级级别</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divScpjjb"></div>
			    		</td>
			    		<td class="POPtab-Title">上次评级人</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divScpjr"></div>
			    		</td>
			    	</tr>
			    	<tr>
			    		<td class="POPtab-Title">本次评级日期</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divBcpjrq"></div>
			    		</td>
			    		<td class="POPtab-Title">本次评级级别</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divBcpjjb"></div>
			    		</td>
			    		<td class="POPtab-Title">本次评级人</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divBcpjr"></div>
			    		</td>
			    	</tr>
			    	<tr>
			    		<td class="POPtab-Title">备注</td>
			    		<td class="POPtabTbEntry2" colspan="5">
			    			<div id="divRemark"></div>
			    		</td>
			    	</tr>
			    </table>
				
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
		var sbpjCatalogId = '<%=sbpjCatalogId%>';
		var type = '<%=type%>';
		if(type == "show"){
			$('save').style.display = 'none';
		}
	</script>
	<script type="text/javascript" src="script/pjxxinfo.js" charset="UTF-8"></script>
	
		
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
