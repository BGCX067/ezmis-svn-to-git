<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>机组检修计划详细信息</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<link rel="stylesheet" type="text/css" href="${contextPath}/script/ext-extend/form/Datetime/datetime.css"></link>
	<link rel="stylesheet" href="../../index.css" type="text/css"></link>
  </head>
 
  <body scroll="no" id="index" onload="load()">
   	   <div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						机组检修计划信息查看
					</div>
					    <table width="97%" height="76%" align="center"class="LabelBodyTb">
					    	<tr>
					    		<td class="POPtab-Title">报告名称</td>
					    		<td class="POPtabTbEntry2"><div id="divBgmc"></div></td>
					    		<td class="POPtab-Title">检修性质</td>
					    		<td class="POPtabTbEntry2"><div id="divJxxz"></div></td>
					    		<td class="POPtab-Title">所属机组</td>
					    		<td class="POPtabTbEntry2"><div id="divSsjz"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">负责人员</td>
					    		<td class="POPtabTbEntry2" colspan="5"><div id="divFzry"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">起始日期</td>
					    		<td class="POPtabTbEntry2"><div id="divQsrq"></div></td>
					    		<td class="POPtab-Title">终止日期</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divJsrq"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">报告摘要</td>
					    		<td class="POPtabTbEntry2" colspan="5"><div id="divBgzy"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">验收意见</td>
					    		<td class="POPtabTbEntry2" colspan="5"><div id="divYsyj"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">验收日期</td>
					    		<td class="POPtabTbEntry2"><div id="divYsrq"></div></td>
					    		<td class="POPtab-Title">验收部门</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divYsbm"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">存在问题</td>
					    		<td class="POPtabTbEntry2" colspan="5"><div id="divCzwt"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">备注说明</td>
					    		<td class="POPtabTbEntry2" colspan="5"><div id="divBzsm"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">报告附件</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divBgfjMc"></div></td>
					    		<td class="POPtabTbEntry2" colspan="2"><div id="divBgfj"></div></td>
					    	</tr>
					    	<tr style="display:none">
					    		<td><div id="bgid"></div></td>
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
	<script type="text/javascript" src="script/jxzjbgViewForm.js" charset="UTF-8"></script>
	
	<!-- 入口程序 -->
    <script type="text/javascript">
    	var id = '${param.id}';
    	var jhId = '${param.jhId}';
    	hdnId.setValue(id);
		Ext.onReady(function(){
			Ext.QuickTips.init();
		});	
    </script>
    <!-- 加载脚本库  结束 -->
    
    <!-- 页面内容 to do something in here -->

  </body>
</html>
