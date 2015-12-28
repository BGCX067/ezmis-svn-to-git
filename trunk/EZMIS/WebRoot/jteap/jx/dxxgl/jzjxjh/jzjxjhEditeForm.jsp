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
 
  <jteap:dict catalog="JXJZ,JXXZ"></jteap:dict>
  <body scroll="no" id="index" onload="load()">
   	   <div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						机组检修计划信息
					</div>
					<form id="form" action="${contextPath}/jteap/jx/dxxgl/JzjxjhAction!saveOrUpdateAction.do" target="ftarget" method="post" enctype="multipart/form-data">
					    <table width="97%" height="76%" align="center"class="LabelBodyTb">
					    	<tr>
					    		<td class="POPtab-Title">计划名称</td>
					    		<td class="POPtabTbEntry2"><div id="divJhmc"></div></td>
					    		<td class="POPtab-Title">检修性质</td>
					    		<td class="POPtabTbEntry2"><div id="divJxxz"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">机组名称</td>
					    		<td class="POPtabTbEntry2"><div id="divJz"></div></td>
					    		<td class="POPtab-Title">起始日期</td>
					    		<td class="POPtabTbEntry2"><div id="divQsrq"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">结束日期</td>
					    		<td class="POPtabTbEntry2"><div id="divJsrq"></div></td>
					    		<td class="POPtab-Title">人工费用</td>
					    		<td class="POPtabTbEntry2"><div id="divRgfy" style="float:left"></div><font size='2pt'>万</font></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">材料费用</td>
					    		<td class="POPtabTbEntry2"><div id="divClfy" style="float:left"></div><font size='2pt'>万</font></td>
					    		<td class="POPtab-Title">费用合计</td>
					    		<td class="POPtabTbEntry2"><div id="divFyhj" style="float:left"></div><font size='2pt'>万</font></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">内容概要</td>
					    		<td class="POPtabTbEntry2" colspan="3"><div id="divNrgy"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">检修任务书</td>
					    		<td class="POPtabTbEntry2" colspan="2"><div id="divJxrwsMc"></div></td>
					    		<td class="POPtabTbEntry2"><div id="divJxrws" style="float:left"></div><font size='2pt' color="red">*必须上传</font></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">检修项目</td>
					    		<td class="POPtabTbEntry2" colspan="2"><div id="divJxxmMc"></div></td>
					    		<td class="POPtabTbEntry2"><div id="divJxxm" style="float:left"></div><font size='2pt' color="red">*必须上传</font></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">检修技术协议</td>
					    		<td class="POPtabTbEntry2" colspan="2"><div id="divJxjsxyMc"></div></td>
					    		<td class="POPtabTbEntry2"><div id="divJxjsxy" style="float:left"></div><font size='2pt' color="red">*必须上传</font></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">其他附件1</td>
					    		<td class="POPtabTbEntry2" colspan="2"><div id="divQtfjMc1"></div></td>
					    		<td class="POPtabTbEntry2"><div id="divQtfj1"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">其他附件2</td>
					    		<td class="POPtabTbEntry2" colspan="2"><div id="divQtfjMc2"></div></td>
					    		<td class="POPtabTbEntry2"><div id="divQtfj2"></div></td>
					    	</tr>
					    	<tr style="display:none">
					    		<td><div id="jhid"></div></td>
					    	</tr>
					    </table>
				    </form>
				    <iframe name="ftarget" style="display:none"></iframe>
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
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/upload/UploadDialog.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/jzjxjhEditeForm.js" charset="UTF-8"></script>
	
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
