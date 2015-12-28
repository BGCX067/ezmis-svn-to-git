<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>接地线拆、装记录</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<link rel="stylesheet" type="text/css" href="${contextPath}/script/ext-extend/form/Datetime/datetime.css"></link>
	<link rel="stylesheet" href="../../index.css" type="text/css"></link>
  </head>
 
  <jteap:dict catalog="jizu"></jteap:dict>
  <body scroll="no" id="index" onload="load()">
   	   <div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						接地线拆、装记录
					</div>
					    <table width="98%" height="30%" align="center"class="LabelBodyTb">
					    	<tr style="display:none">
					    		<td><div id="divid"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title" width="130">接地线编号</td>
					    		<td class="POPtabTbEntry2"><div id="divjdxbh"></div></td>
					    		<td class="POPtab-Title" width="130">机组</td>
					    		<td class="POPtabTbEntry2"><div id="divjz"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">装设地点</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divzsdd"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">装设人</td>
					    		<td class="POPtabTbEntry2"><div id="divzsr"></div></td>
					    		<td class="POPtab-Title">拆除人</td>
					    		<td class="POPtabTbEntry2"><div id="divccr"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">装设时间</td>
					    		<td class="POPtabTbEntry2"><div id="divzssj"></div></td>
					    		<td class="POPtab-Title">拆除时间</td>
					    		<td class="POPtabTbEntry2"><div id="divccsj"></div></td>
					    		
					    	</tr>
					    </table>
				    <div class="pop-but">
						<div class="pop-butMain">
							<input type="button" value=" 保 存 " onclick="save()" class="pop-but01">
			    			<input type="button" value=" 关 闭 " onclick="window.close();" class="pop-but01">
						</div>
					</div>	
    			</div>
    		</div>
    	</div>
   	 <!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/Datetime/Datetime.js"></script>
	<script type="text/javascript" src="script/jdxjlEditeForm.js" charset="UTF-8"></script>
	
	<!-- 入口程序 -->
    <script type="text/javascript">
    	var id = '${param.id}';
    	hdnId.setValue(id);
		Ext.onReady(function(){
			Ext.QuickTips.init();
		});	
    </script>
    <!-- 加载脚本库  结束 -->
    
    <!-- 页面内容 to do something in here -->

  </body>
</html>
