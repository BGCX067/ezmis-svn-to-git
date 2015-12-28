<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>机组检修项目详细信息</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<link rel="stylesheet" type="text/css" href="${contextPath}/script/ext-extend/form/Datetime/datetime.css"></link>
	<link rel="stylesheet" href="../../index.css" type="text/css"></link>
  </head>
 
  <body scroll="no" id="index" onload="load()">
   	   <div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						备品备件信息编辑
					</div>
					<form id="form" action="${contextPath}/jteap/jx/bpbjgl/BpbjxxAction!saveOrUpdateAction.do" target="ftarget" method="post">
					    <table width="97%" height="78%" align="center"class="LabelBodyTb">
					    	<tr>
					    		<td class="POPtab-Title">设备名称</td>
					    		<td class="POPtabTbEntry2"><div id="divSbmc" style="float:left"></div><input type="button" value="选择" onclick="showSbmc()" class="pop-but01"></td>
					    		<td class="POPtab-Title">型式及规格</td>
					    		<td class="POPtabTbEntry2"><div id="divXsjgg"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">单位</td>
					    		<td class="POPtabTbEntry2"><div id="divDw"></div></td>
					    		<td class="POPtab-Title">额定数量</td>
					    		<td class="POPtabTbEntry2"><div id="divEdsl"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">实际数量</td>
					    		<td class="POPtabTbEntry2"><div id="divSjsl"></div></td>
					    		<td class="POPtab-Title">预警数量</td>
					    		<td class="POPtabTbEntry2"><div id="divYjsl"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">备注说明</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divRemark"></div></td>
					    	</tr>
					    	<tr style="display:none">
					    		<td><div id="zyflId"></div></td>
					    	</tr>
					    	<tr style="display:none">
					    		<td><div id="id"></div></td>
					    	</tr>
					    	<tr style="display:none">
					    		<td><div id="divSbbm"></div></td>
					    	</tr>
					    </table>
				    </form>
				    <div class="pop-but">
						<div class="pop-butMain">
							<input type="button" value=" 保 存 " onclick="save()" class="pop-but01">
			    			<input type="button" value=" 关 闭 " onclick="window.close();" class="pop-but01">
						</div>
					</div>	
				    <iframe name="ftarget" style="display:none"></iframe>
    			</div>
    		</div>
    	</div>
   	 <!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>
	<script type="text/javascript" src="script/bpbjxxEditeForm.js" charset="UTF-8"></script>
	
	<!-- 入口程序 -->
    <script type="text/javascript">
    	var id = '${param.id}';
    	var zyflId = '${param.zyflId}';
    	hdnId.setValue(id);
    	hdnNodeId.setValue(zyflId);
		Ext.onReady(function(){
			Ext.QuickTips.init();
		});	
    </script>
    <!-- 加载脚本库  结束 -->
    
    <!-- 页面内容 to do something in here -->

  </body>
</html>
