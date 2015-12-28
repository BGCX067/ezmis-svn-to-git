<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>保护及自动装置动作记录</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<link rel="stylesheet" type="text/css" href="${contextPath}/script/ext-extend/form/Datetime/datetime.css"></link>
	<link rel="stylesheet" href="../../index.css" type="text/css"></link>
  </head>
 
  <body scroll="no" id="index" onload="load()">
   	   <div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						保护及自动装置动作记录
					</div>
					    <table width="98%" height="50%" align="center"class="LabelBodyTb">
					    	<tr style="display:none">
					    		<td><div id="id"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title" width="130">设备名称</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divsbmc"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">动作时间</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divdzsj"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">保护名称及吊牌</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divbhmc"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">光字牌信号</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divgzpxh"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title" width="130">检查人</td>
					    		<td class="POPtabTbEntry2"><div id="divjcr"></div></td>
					    		<td class="POPtab-Title" width="130">复归人</td>
					    		<td class="POPtabTbEntry2"><div id="divfgr"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">备注</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divbz"></div></td>
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
	<script type="text/javascript" src="script/bhzddzjlEditeForm.js" charset="UTF-8"></script>
	
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
