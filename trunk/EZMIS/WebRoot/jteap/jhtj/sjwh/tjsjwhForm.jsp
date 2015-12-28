<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
  </head>
  	<%@ include file="/inc/ext-all.jsp" %>
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelPanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelValuePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/TitlePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/ComboTree.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNodeUI.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNode.js" charset="UTF-8"></script>
	<SCRIPT language="JavaScript" src="${contextPath}/script/xml.js"></SCRIPT>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/TitlePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/upload/UploadDialog.js" charset="UTF-8"></script>
  <script type="text/javascript" src="${contextPath}/script/lightbox.js"></script>
  <script language="javascript" type="text/javascript" src="${contextPath}/component/My97DatePicker/WdatePicker.js"></script>
  <script language="javascript">
  		//初始化参数
  		var keyData='${requestScope.keyAll}';
  		var fieldAll='${requestScope.fieldAll}';
  		var fieldValues='${requestScope.fieldValues}';
  		var kid='${requestScope.kid}';
  		var flid='${requestScope.flid}';
  		var isRoot='${requestScope.isRoot}';
  </script>
  <script type="text/javascript" src="script/tjsjwhForm.js" charset="UTF-8"></script>	
  <body onload="cteateKeyList();cteateFieldList();">
	<dl id="idBox" style="display:none" class="lightbox">
	  <dt id="idBoxHead"><b><center>正在取数中,请稍后....</center></b> </dt>
	  <dd style="text-align:center;">
	  		<img src="${contextPath}/jteap/jhtj/bbsj/icon/process.gif"/>
	  </dd>
	</dl>
	 <iframe   style="display:none"   id="ifAlert"   ></iframe>


<table style='display:none' width='100%'>
		<tr>
			<td width='15%'>编号:</td>
			<td align='left'> 
			</td>
		</tr>		
		<tr><td colspan=2><hr/></td></tr>	
	</table>
	<!-- Form开始 -->

<div><!--  设置弹出页面大小 --> <!--  start of 标题区域 -->
<table width="100%" border="1" align="center" cellpadding="1"
	cellspacing="0" class="TitleBGDiv">
	<tr>
		<td>
		<table width="99%" align="center" cellspacing="0" class="TitleBorder">
			<tr>
				<td height="25" align="center" class="TitleTable">数据信息维护</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<!--  end of 标题区域 --> <!--  start of 内容区域 -->
<div class="MainDiv"><!--  start of 按钮区域 -->

<!--  end of 按钮区域 --> <!--  start of 一分栏区域 -->
<table width="99%" border="0" cellpadding="0" cellspacing="0"
	class="FBorder">
	<tr>
		<td class="ButtonDiv" align="left">
		<div class="ButtonDivLeft">
		 	<table>
		   	<tr>
		   	  <td nowrap="nowrap" id="JZTd">
		   	  	
			  </td>
			  <td nowrap="nowrap" id="NIANTd">
			  	
			  </td>
			  <td nowrap="nowrap" id="YUETd">
			  	
			  </td>
			  <td nowrap="nowrap" id="RITd">
			  	
			  </td>
			 </tr>
		   </table>
		</div>
		</td>
		<td class="ButtonDiv" align="left">
			<div class="ButtonDivRight">
				<font color="red">手工输入*红色</font>&nbsp;
				<font color="#000000">其它*黑色</font>&nbsp;
			</div>
		</td>
	</tr>
</table>

<form action="/tjSjwhAction.do?action=saveOrUpdate" onsubmit="">
<table width="99%" border="0" align="center" cellpadding="1"
	cellspacing="1" class="FBorder">
	<tr>
		<td bgcolor="">
			<div style="display:none;" id="hidDIV">
				<!-- 动态生成条件字段 -->
			</div>
			<input type="hidden" name="kidIn">
			<input type="hidden" name="updateData">
			<div align="center" style="overflow-x:auto;overflow-y:auto; height:440;width:100%;" id="div6" name="div6" class="">
				<!-- 动态生成普通字段 -->
			</div>
		</td>
	</tr>
	<tr >
		<td>
		</td>
	</tr>
</table>
<table width="99.5%" border="0" cellpadding="0" cellspacing="0"
	class="FBorder" align="center">
	<tr>
		<td class="GridCellL">
		<div class="ButtonDivRight">
			<Input type="button" value="取数"  id="qsButton" class="button01" onclick="getDataOrComputeAll(1);"/>
			<Input type="button" value="计算"  id="jsButton" class="button01" onclick="getDataOrComputeAll(2);"/>
			<Input type="button" value="保存"  id="saveButton" class="button01" onclick="saveOrUpdateData();"/>
			<Input type="button" value="关闭" class="button01" onclick="window.close();"/>
		</div>
		</td>
	</tr>
</table>
</form>
   	 
   	 
<div  class="help-tooltip" id="showmes" style="display:none"> 
   <table border=0 height=100% width=100%> 
       <tr> 
           <td align='middle' width=35%> 
               <img src='${contextPath}/jteap/jhtj/sjwh/icon/loading.gif' border='0' WIDTH='17' HEIGHT='17'> 
           </td> 
           <td valign='middle'> 
              	正在取数中,请稍后....
           </td> 
       </tr> 
   </table> 
</div>

    	 
  </body>
</html>
