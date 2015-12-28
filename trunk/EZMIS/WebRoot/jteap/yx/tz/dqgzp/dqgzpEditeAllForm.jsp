<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>电气工作票</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<link rel="stylesheet" type="text/css" href="${contextPath}/script/ext-extend/form/Datetime/datetime.css"></link>
	<link rel="stylesheet" href="../../index.css" type="text/css"></link>
  </head>
 
  <jteap:dict catalog="YX_GZPZT"></jteap:dict>
  <script type="text/javascript">
		var isTotalModi = "<%=request.getParameter("isTotalModi")%>";
  </script>
  <body scroll="no" id="index" onload="load()">
   	   <div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						电气工作票
					</div>
					    <table width="98%" height="55%" align="center"class="LabelBodyTb">
					    	<tr style="display:none">
					    		<td><div id="divid"></div></td>
					    	</tr>
					    	<tr style="display:none">
					    		<td><div id="divpzzzmc"></div></td>
					    	</tr>
					    	<tr style="display:none">
					    		<td><div id="divxkrmc"></div></td>
					    	</tr>
					    	<tr style="display:none">
					    		<td><div id="divzjrmc"></div></td>
					    	</tr>
					    	<tr style="display:none">
					    		<td><div id="divpzyqzzmc"></div></td>
					    	</tr>
					    	<tr style="display:none">
					    		<td><div id="divzfrmc"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title" width="140">工作票编号</td>
					    		<td class="POPtabTbEntry2"><div id="divgzpbh"></div></td>
					    		<td class="POPtab-Title" width="140">工作票状态</td>
					    		<td class="POPtabTbEntry2"><div id="divgzpzt"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">地点及任务</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divddjrw"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">工作负责人</td>
					    		<td class="POPtabTbEntry2"><div id="divgzfzr"></div></td>
					    		<td class="POPtab-Title">工作票登记人</td>
					    		<td class="POPtabTbEntry2"><div id="divgzpdjr"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">工作班人员</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divgzbry"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">收票时间</td>
					    		<td class="POPtabTbEntry2"><div id="divspsj"></div></td>
					    		<td class="POPtab-Title">收票人</td>
					    		<td class="POPtabTbEntry2"><div id="divspr"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">批准工作期限</td>
					    		<td class="POPtabTbEntry2"><div id="divpzgzqx"></div></td>
					    		<td class="POPtab-Title">批准值长</td>
					    		<td class="POPtabTbEntry2"><div id="divpzzz"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">许可开工时间</td>
					    		<td class="POPtabTbEntry2"><div id="divxksj"></div></td>
					    		<td class="POPtab-Title">许可人</td>
					    		<td class="POPtabTbEntry2"><div id="divxkr"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">延期期限</td>
					    		<td class="POPtabTbEntry2"><div id="divyqsj"></div></td>
					    		<td class="POPtab-Title">批准延期值长</td>
					    		<td class="POPtabTbEntry2"><div id="divpzyqzz"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">办理延期手续时间</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divyqsxsj"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">终结时间</td>
					    		<td class="POPtabTbEntry2"><div id="divzjsj"></div></td>
					    		<td class="POPtab-Title">终结人</td>
					    		<td class="POPtabTbEntry2"><div id="divgzpzjr"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">作废时间</td>
					    		<td class="POPtabTbEntry2"><div id="divzfsj"></div></td>
					    		<td class="POPtab-Title">作废人</td>
					    		<td class="POPtabTbEntry2"><div id="divgzpzfr"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">作废原因</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divzfyy"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">终结交待</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divzjjd"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">终结检查情况</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divzjjcqk"></div></td>
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
	<script type="text/javascript" src="script/dqgzpEditeAllForm.js" charset="UTF-8"></script>
	
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
