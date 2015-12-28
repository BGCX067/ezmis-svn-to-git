<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>两票基本信息</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<link rel="stylesheet" type="text/css" href="${contextPath}/script/ext-extend/form/Datetime/datetime.css"></link>
	<link rel="stylesheet" href="../index.css" type="text/css"></link>
  </head>
 
  <jteap:dict catalog="PFL"></jteap:dict>
  <body scroll="no" id="index" onload="load()">
   	   <div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						两票基本信息
					</div>
				    <table width="100%" height="80%" align="center"class="LabelBodyTb">
				    	<tr>
				    		<td class="POPtab-Title">票分类</td>
				    		<td class="POPtabTbEntry2"><div id="divPfl"></div></td>
				    		<td class="POPtab-Title">票名称</td>
				    		<td class="POPtabTbEntry2"><div id="divPmc"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">创建部门</td>
				    		<td class="POPtabTbEntry2"><div id="divCjbm"></div></td>
				    		<td class="POPtab-Title">创建人</td>
				    		<td class="POPtabTbEntry2"><div id="divCjr"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">创建时间</td>
				    		<td class="POPtabTbEntry2"><div id="divCjsj"></div></td>
				    		<td class="POPtab-Title">标准票</td>
				    		<td class="POPtabTbEntry2"><div id="divIsBzp"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">票状态</td>
				    		<td class="POPtabTbEntry2"><div id="divPzt"></div></td>
				    		<td class="POPtab-Title">内码</td>
				    		<td class="POPtabTbEntry2"><div id="divNm"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">备注说明</td>
				    		<td class="POPtabTbEntry2" colspan="3"><div id="divBzsm"></div></td>
				    	</tr>
				    	<tr style="display:none">
				    		<td><div id="pzid"></div></td>
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
	<script type="text/javascript" src="script/pkViewForm.js" charset="UTF-8"></script>
	
	<!-- 入口程序 -->
    <script type="text/javascript">
    	var id = '${param.id}';
    	hdnId.setValue(id);
		Ext.onReady(function(){
			Ext.QuickTips.init();
		});
    </script>
    <!-- 加载脚本库  结束 -->
  </body>
</html>
