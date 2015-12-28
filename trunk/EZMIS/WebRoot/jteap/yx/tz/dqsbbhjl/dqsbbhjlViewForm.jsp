<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>电气设备保护加用、停用记录</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<link rel="stylesheet" href="../../index.css" type="text/css"></link>
  </head>
 
  <jteap:dict catalog="jizu"></jteap:dict>
  <body scroll="no" id="index" onload="load()">
   	   <div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						电气设备保护加用、停用记录
					</div>
					    <table width="98%" height="70%" align="center"class="LabelBodyTb">
					    	<tr style="display:none">
					    		<td><div id="id"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title" width="130">机组</td>
					    		<td class="POPtabTbEntry2"><div id="divjz"></div></td>
					    		<td class="POPtab-Title" width="130">保护名称</td>
					    		<td class="POPtabTbEntry2"><div id="divbhmc"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">停用时间</td>
					    		<td class="POPtabTbEntry2"><div id="divtysj"></div></td>
					    		<td class="POPtab-Title">停用执行人</td>
					    		<td class="POPtabTbEntry2"><div id="divtyzxr"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">停用值班员</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divtyzby"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">停用原因</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divtyyy"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">加用时间</td>
					    		<td class="POPtabTbEntry2"><div id="divjysj"></div></td>
					    		<td class="POPtab-Title">加用执行人</td>
					    		<td class="POPtabTbEntry2"><div id="divjyzxr"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">加用值班员</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divjyzby"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">加用原因</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divjyyy"></div></td>
					    	</tr>
					    </table>
				    <div class="pop-but">
						<div class="pop-butMain">
			    			<input type="button" value=" 关 闭 " onclick="window.close();" class="pop-but01">
						</div>
					</div>	
    			</div>
    		</div>
    	</div>
   	 <!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>
	<script type="text/javascript" src="script/dqsbbhjlViewForm.js" charset="UTF-8"></script>
	
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
