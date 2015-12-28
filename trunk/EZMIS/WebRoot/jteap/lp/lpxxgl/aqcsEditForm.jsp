<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>安全措施</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<link rel="stylesheet" href="../index.css" type="text/css"></link>
  </head>
 
  <jteap:dict catalog="PFL"></jteap:dict>
  <body scroll="no" id="index" onload="load()">
   	   <div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						安全措施基本信息
					</div>
				    <table width="100%" height="20%" align="center"class="LabelBodyTb">
				    	<tr>
				    		<td class="POPtab-Title">所属分类</td>
				    		<td class="POPtabTbEntry2"><div id="divSsfl"></div></td>
				    		<td class="POPtab-Title">措施名称</td>
				    		<td class="POPtabTbEntry2"><div id="divCsmc"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">措施内容</td>
				    		<td class="POPtabTbEntry2" colspan="3"><div id="divCsnr"></div></td>
				    	</tr>
				    	<tr style="display:none">
				    		<td><div id="csid"></div></td>
				    	</tr>
				    	<tr style="display:none">
				    		<td><div id="flid"></div></td>
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
	<script type="text/javascript" src="script/aqcsEditForm.js" charset="UTF-8"></script>
	
	<!-- 入口程序 -->
    <script type="text/javascript">
    	var id = '${param.id}';
    	var flmc = '${param.flmc}';
    	var flid = '${param.flid}';
    	hdnId.setValue(id);
    	hdnFlid.setValue(flid);
		Ext.onReady(function(){
			Ext.QuickTips.init();
		});
    </script>
    <!-- 加载脚本库  结束 -->
  </body>
</html>
