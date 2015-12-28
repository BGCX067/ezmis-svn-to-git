<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>220KV开关分合闸记录</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<link rel="stylesheet" type="text/css" href="${contextPath}/script/ext-extend/form/Datetime/datetime.css"></link>
	<link rel="stylesheet" href="../../index.css" type="text/css"></link>
  </head>
 
  <jteap:dict catalog="YX_JZLX,YX_300_KGMC,YX_600_KGMC"></jteap:dict>
  <body scroll="no" id="index" onload="load()">
   	   <div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						220KV开关分合闸记录
					</div>
					    <table width="98%" height="70%" align="center"class="LabelBodyTb">
					    	<tr style="display:none">
					    		<td><div id="id"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title" width="130">机组类型</td>
					    		<td class="POPtabTbEntry2"><div id="divjzlx"></div></td>
					    		<td class="POPtab-Title" width="130">开关名称</td>
					    		<td class="POPtabTbEntry2"><div id="divkgmc"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">合闸时间</td>
					    		<td class="POPtabTbEntry2"><div id="divhzsj"></div></td>
					    		<td class="POPtab-Title">合闸记录人</td>
					    		<td class="POPtabTbEntry2"><div id="divhzjlr"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">合闸原因</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divhzyy"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">分闸时间</td>
					    		<td class="POPtabTbEntry2"><div id="divfzsj"></div></td>
					    		<td class="POPtab-Title">分闸记录人</td>
					    		<td class="POPtabTbEntry2"><div id="divfzjlr"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">分闸原因</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divfzyy"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">运行时间</td>
					    		<td class="POPtabTbEntry2"><div id="divyxsj"></div></td>
					    		<td class="POPtab-Title">动作次数</td>
					    		<td class="POPtabTbEntry2"><div id="divdzcs"></div></td>
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
	<script type="text/javascript" src="script/kgfhzjlEditeForm.js" charset="UTF-8"></script>
	
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
