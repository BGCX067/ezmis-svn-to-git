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
 
  <jteap:dict catalog="JXXM_SSZY"></jteap:dict>
  <body scroll="no" id="index" onload="load()">
   	   <div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						机组检修项目信息
					</div>
					<form id="form" action="${contextPath}/jteap/jx/dxxgl/JzjxxmAction!saveOrUpdateAction.do" target="ftarget" method="post">
					    <table width="97%" height="50%" align="center"class="LabelBodyTb">
					    	<tr>
					    		<td class="POPtab-Title">所属专业</td>
					    		<td class="POPtabTbEntry2"><div id="divSszy"></div></td>
					    		<td class="POPtab-Title">记录时间</td>
					    		<td class="POPtabTbEntry2"><div id="divJlsj"></div></td>
					    		
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">项目名称</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divXmmc"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">记录人</td>
					    		<td class="POPtabTbEntry2"><div id="divJlr"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">备注说明</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divRemark"></div></td>
					    	</tr>
					    	<tr style="display:none">
					    		<td><div id="jxxmid"></div></td>
					    	</tr>
					    	<tr style="display:none">
					    		<td><div id="jhId"></div></td>
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
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/Datetime/Datetime.js"></script>
	<script type="text/javascript" src="script/jzjxxmEditeForm.js" charset="UTF-8"></script>
	
	<!-- 入口程序 -->
    <script type="text/javascript">
    	var id = '${param.id}';
    	var jhId = '${param.jhId}';
    	hdnId.setValue(id);
    	hdnNodeId.setValue(jhId);
		Ext.onReady(function(){
			Ext.QuickTips.init();
		});	
    </script>
    <!-- 加载脚本库  结束 -->
    
    <!-- 页面内容 to do something in here -->

  </body>
</html>
