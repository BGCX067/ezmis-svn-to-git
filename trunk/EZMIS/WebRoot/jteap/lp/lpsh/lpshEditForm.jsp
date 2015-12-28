<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>两票审核信息</title>
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
						两票审核信息
					</div>
				    <table width="100%" height="80%" align="center"class="LabelBodyTb">
				    	<tr>
				    		<td class="POPtab-Title">票号</td>
				    		<td class="POPtabTbEntry2"><div id="divPh"></div></td>
				    		<td class="POPtab-Title">票种类</td>
				    		<td class="POPtabTbEntry2"><div id="divPmc"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">开始时间</td>
				    		<td class="POPtabTbEntry2"><div id="divJhkssj"></div></td>
				    		<td class="POPtab-Title">结束时间</td>
				    		<td class="POPtabTbEntry2"><div id="divJhjssj"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">审核部门</td>
				    		<td class="POPtabTbEntry2"><div id="divShbm"></div></td>
				    		<td class="POPtab-Title">审核时间</td>
				    		<td class="POPtabTbEntry2"><div id="divShsj"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">审核人</td>
				    		<td class="POPtabTbEntry2"><div id="divShr"></div></td>
				    		<td class="POPtab-Title">票状态</td>
				    		<td class="POPtabTbEntry2"><div id="divShzt"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">批注</td>
				    		<td class="POPtabTbEntry2" colspan="3"><div id="divPz"></div></td>
				    	</tr>
				    	<tr style="display:none">
				    		<td><div id="pzid"></div></td>
				    	</tr>
				    	<tr style="display:none">
				    		<td><div id="divTablename"></div></td>
				    	</tr>
				    </table>
				    <div class="pop-but">
						<div class="pop-butMain">
							<input type="button" name="save" value=" 保 存 " onclick="save()" class="pop-but01">
			    			<input type="button" name="close" value=" 关 闭 " onclick="window.close();" class="pop-but01">
						</div>
					</div>	
    			</div>
    		</div>
    	</div>
   	 <!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/ComboTree.js"></script>
	<script type="text/javascript" src="script/lpshEditForm.js" charset="UTF-8"></script>
	
	<!-- 入口程序 -->
    <script type="text/javascript">
    	var arg = window.dialogArguments;
		Ext.onReady(function(){
			Ext.QuickTips.init();
		});
    </script>
    <!-- 加载脚本库  结束 -->
  </body>
</html>
