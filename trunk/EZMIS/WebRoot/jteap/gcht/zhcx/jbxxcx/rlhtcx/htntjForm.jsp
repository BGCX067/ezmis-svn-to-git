<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>合同统计</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<link rel="stylesheet" href="../../../index.css" type="text/css"></link>
  </head>
 
  <body scroll="no" id="index"">
   	   <div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						合同统计
					</div>
					    <table width="97%" height="40%" align="center" class="LabelBodyTb">
					    	<tr>
					    		<td class="POPtab-Title">查询年份</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divNf"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">合同总份数</td>
					    		<td class="POPtabTbEntry2"><div id="divHtZfs"></div></td>
					    		<td class="POPtab-Title">在审批合同数</td>
					    		<td class="POPtabTbEntry2"><div id="divZspHt"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">在执行份数</td>
					    		<td class="POPtabTbEntry2"><div id="divZzxHt"></div></td>
					    		<td class="POPtab-Title">终结份数</td>
					    		<td class="POPtabTbEntry2"><div id="divZjHt"></div></td>
					    	</tr>
					    </table>
				    <div class="pop-but">
						<div class="pop-butMain">
							<input type="button" value=" 统 计 " onclick="query()" class="pop-but01">
			    			<input type="button" value=" 关 闭 " onclick="window.close();" class="pop-but01">
						</div>
					</div>	
    			</div>
    		</div>
    	</div>
   	 <!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>
	<script type="text/javascript" src="script/htntjForm.js" charset="UTF-8"></script>
	
	<!-- 入口程序 -->
    <script type="text/javascript">
		Ext.onReady(function(){
			Ext.QuickTips.init();
		});	
    </script>
    <!-- 加载脚本库  结束 -->
    
  </body>
</html>
