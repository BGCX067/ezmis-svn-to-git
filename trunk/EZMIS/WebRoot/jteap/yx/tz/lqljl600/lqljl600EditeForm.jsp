<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>漏氢量记录(300MW)</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<link rel="stylesheet" type="text/css" href="${contextPath}/script/ext-extend/form/Datetime/datetime.css"></link>
	<link rel="stylesheet" href="../../index.css" type="text/css"></link>
  </head>
 
  <body scroll="no" id="index" onload="load()">
   	   <div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						漏氢量记录(300MW)
					</div>
					    <table width="98%" height="70%" align="center"class="LabelBodyTb">
					    	<tr style="display:none">
					    		<td><div id="id"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title" width="130">开始时间</td>
					    		<td class="POPtabTbEntry2"><div id="divkssj"></div></td>
					    		<td class="POPtab-Title" width="130">开始氢压P1</td>
					    		<td class="POPtabTbEntry2"><div id="divksqyp1" style="float:left"></div><font size='2pt'>MPa</font></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">开始冷氢温度t1</td>
					    		<td class="POPtabTbEntry2"><div id="divqwxt1" style="float:left"></div><font size='2pt'>℃</font></td>
					    		<td class="POPtab-Title">开始冷氢温度t2</td>
					    		<td class="POPtabTbEntry2"><div id="divqwxt2" style="float:left"></div><font size='2pt'>℃</font></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">结束热氢温度T1</td>
					    		<td class="POPtabTbEntry2"><div id="divqwdt1" style="float:left"></div><font size='2pt'>℃</font></td>
					    		<td class="POPtab-Title">结束时间</td>
					    		<td class="POPtabTbEntry2"><div id="divjssj"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">结束氢压P2</td>
					    		<td class="POPtabTbEntry2"><div id="divjsqyp2" style="float:left"></div><font size='2pt'>MPa</font></td>
					    		<td class="POPtab-Title">结束热氢温度T2</td>
					    		<td class="POPtabTbEntry2"><div id="divqwdt2" style="float:left"></div><font size='2pt'>℃</font></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">运行时间H</td>
					    		<td class="POPtabTbEntry2"><div id="divyxsj" style="float:left"></div><font size='2pt'>h</font></td>
					    		<td class="POPtab-Title">漏氢量</td>
					    		<td class="POPtabTbEntry2"><div id="divlql" style="float:left"></div><font size='2pt'>m³/d</font></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">漏氢率</td>
					    		<td class="POPtabTbEntry2"><div id="divlqlv" style="float:left"></div><font size='2pt'>%/d</font></td>
					    		<td class="POPtab-Title">填写人1</td>
					    		<td class="POPtabTbEntry2"><div id="divtxr1"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">填写人2</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divtxr2"></div></td>
					    	</tr>
					    </table>
				    <div class="pop-but">
						<div class="pop-butMain">
							<input type="button" value="计算漏氢量" onclick="calLql()" class="pop-but01">
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
	<script type="text/javascript" src="script/lqljl600EditeForm.js" charset="UTF-8"></script>
	
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
