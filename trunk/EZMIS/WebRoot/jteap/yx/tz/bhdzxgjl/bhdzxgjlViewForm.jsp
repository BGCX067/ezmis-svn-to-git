<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>保护定值修改记录</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<link rel="stylesheet" href="../../index.css" type="text/css"></link>
  </head>
 
  <body scroll="yes" id="index" onload="load()">
   	   <div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						保护定值修改记录
					</div>
					    <table width="90%" height="65%" align="center" class="LabelBodyTb">
					    	<tr style="display:none">
					    		<td><div id="id"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">设备名称</td>
					    		<td class="POPtabTbEntry2" colspan="2"><div id="divbhmc"></div></td>
					    		<td class="POPtab-Title">更改时间</td>
					    		<td class="POPtabTbEntry2" colspan="2"><div id="divggsj"></div></td>
					    	</tr>
					    	<tr>
					    		<td colspan="6">
						    	<table width="50%" align="left" class="LabelBodyTb">
							    	<tr>
							    		<td class="POPtab-Title">序号</td>
							    		<td class="POPtab-Title">保护名称(改前)</td>
							    		<td class="POPtab-Title">整定值（改前）</td>
							    		<td class="POPtab-Title">保护名称（改后）</td>
							    		<td class="POPtab-Title">整定值（改后）</td>
							    	</tr>
							    	<tr>
							    		<td class="POPtab-Title">1</td>
							    		<td class="POPtabTbEntry2"><div id="divbhmcgq1"></div></td>
							    		<td class="POPtabTbEntry2"><div id="divzdzgq1"></div></td>
							    		<td class="POPtabTbEntry2"><div id="divbhmcgh1"></div></td>
							    		<td class="POPtabTbEntry2"><div id="divzdzgh1"></div></td>
							    	</tr>
							    	<tr>
							    		<td class="POPtab-Title">2</td>
							    		<td class="POPtabTbEntry2"><div id="divbhmcgq2"></div></td>
							    		<td class="POPtabTbEntry2"><div id="divzdzgq2"></div></td>
							    		<td class="POPtabTbEntry2"><div id="divbhmcgh2"></div></td>
							    		<td class="POPtabTbEntry2"><div id="divzdzgh2"></div></td>
							    	</tr>
							    	<tr>
							    		<td class="POPtab-Title">3</td>
							    		<td class="POPtabTbEntry2"><div id="divbhmcgq3"></div></td>
							    		<td class="POPtabTbEntry2"><div id="divzdzgq3"></div></td>
							    		<td class="POPtabTbEntry2"><div id="divbhmcgh3"></div></td>
							    		<td class="POPtabTbEntry2"><div id="divzdzgh3"></div></td>
							    	</tr>
							    	<tr>
							    		<td class="POPtab-Title">4</td>
							    		<td class="POPtabTbEntry2"><div id="divbhmcgq4"></div></td>
							    		<td class="POPtabTbEntry2"><div id="divzdzgq4"></div></td>
							    		<td class="POPtabTbEntry2"><div id="divbhmcgh4"></div></td>
							    		<td class="POPtabTbEntry2"><div id="divzdzgh4"></div></td>
							    	</tr>
							    	<tr>
							    		<td class="POPtab-Title">5</td>
							    		<td class="POPtabTbEntry2"><div id="divbhmcgq5"></div></td>
							    		<td class="POPtabTbEntry2"><div id="divzdzgq5"></div></td>
							    		<td class="POPtabTbEntry2"><div id="divbhmcgh5"></div></td>
							    		<td class="POPtabTbEntry2"><div id="divzdzgh5"></div></td>
							    	</tr>
						    	</table>
						    	<td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">更改原因及定值单编号</td>
					    		<td class="POPtabTbEntry2" colspan="5"><div id="divggyy"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">发令人</td>
					    		<td class="POPtabTbEntry2"><div id="divggflr"></div></td>
					    		<td class="POPtab-Title">执行人</td>
					    		<td class="POPtabTbEntry2"><div id="divggzhr"></div></td>
					    		<td class="POPtab-Title">运行检查</td>
					    		<td class="POPtabTbEntry2"><div id="divggyhjc"></div></td>
					    	</tr>
					    	<tr>
						    	<td colspan="6">
							    	<table width="50%" align="left" class="LabelBodyTb">
								    	<tr>
								    		<td class="POPtab-Title">序号</td>
								    		<td class="POPtab-Title">保护名称(恢复前)</td>
								    		<td class="POPtab-Title">整定值（恢复前）</td>
								    		<td class="POPtab-Title">保护名称（恢复后）</td>
								    		<td class="POPtab-Title">整定值（恢复后）</td>
								    	</tr>
								    	<tr>
								    		<td class="POPtab-Title">1</td>
								    		<td class="POPtabTbEntry2"><div id="divbhmchfq1"></div></td>
								    		<td class="POPtabTbEntry2"><div id="divzdzhfq1"></div></td>
								    		<td class="POPtabTbEntry2"><div id="divbhmchfh1"></div></td>
								    		<td class="POPtabTbEntry2"><div id="divzdzhfh1"></div></td>
								    	</tr>
								    	<tr>
								    		<td class="POPtab-Title">2</td>
								    		<td class="POPtabTbEntry2"><div id="divbhmchfq2"></div></td>
								    		<td class="POPtabTbEntry2"><div id="divzdzhfq2"></div></td>
								    		<td class="POPtabTbEntry2"><div id="divbhmchfh2"></div></td>
								    		<td class="POPtabTbEntry2"><div id="divzdzhfh2"></div></td>
								    	</tr>
								    	<tr>
								    		<td class="POPtab-Title">3</td>
								    		<td class="POPtabTbEntry2"><div id="divbhmchfq3"></div></td>
								    		<td class="POPtabTbEntry2"><div id="divzdzhfq3"></div></td>
								    		<td class="POPtabTbEntry2"><div id="divbhmchfh3"></div></td>
								    		<td class="POPtabTbEntry2"><div id="divzdzhfh3"></div></td>
								    	</tr>
								    	<tr>
								    		<td class="POPtab-Title">4</td>
								    		<td class="POPtabTbEntry2"><div id="divbhmchfq4"></div></td>
								    		<td class="POPtabTbEntry2"><div id="divzdzhfq4"></div></td>
								    		<td class="POPtabTbEntry2"><div id="divbhmchfh4"></div></td>
								    		<td class="POPtabTbEntry2"><div id="divzdzhfh4"></div></td>
								    	</tr>
								    	<tr>
								    		<td class="POPtab-Title">5</td>
								    		<td class="POPtabTbEntry2"><div id="divbhmchfq5"></div></td>
								    		<td class="POPtabTbEntry2"><div id="divzdzhfq5"></div></td>
								    		<td class="POPtabTbEntry2"><div id="divbhmchfh5"></div></td>
								    		<td class="POPtabTbEntry2"><div id="divzdzhfh5"></div></td>
								    	</tr>
							    	</table>
						    	</td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">恢复原因及定值单编号</td>
					    		<td class="POPtabTbEntry2" colspan="5"><div id="divhfyy"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">发令人</td>
					    		<td class="POPtabTbEntry2"><div id="divhfflr"></div></td>
					    		<td class="POPtab-Title">执行人</td>
					    		<td class="POPtabTbEntry2"><div id="divhfzhr"></div></td>
					    		<td class="POPtab-Title">运行检查</td>
					    		<td class="POPtabTbEntry2"><div id="divhfyhjc"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">备注</td>
					    		<td class="POPtabTbEntry2" colspan="2"><div id="divbz"></div></td>
					    		<td class="POPtab-Title">恢复时间</td>
					    		<td class="POPtabTbEntry2" colspan="2"><div id="divhfsj"></div></td>
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
	<script type="text/javascript" src="script/bhdzxgjlViewForm.js" charset="UTF-8"></script>
	
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
