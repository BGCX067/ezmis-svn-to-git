<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>表格数据</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<link rel="stylesheet" type="text/css" href="${contextPath}/script/ext-extend/form/Datetime/datetime.css"></link>
	<link rel="stylesheet" href="${contextPath}/jteap/jhtj/index.css" type="text/css"></link>
  	<script language="javascript" type="text/javascript" src="${contextPath}/component/My97DatePicker/WdatePicker.js"></script>
  	<script type="text/javascript" src="${contextPath}/component/chart/js/xml.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/component/chart/Charts/FusionCharts.js" charset="UTF-8"></script>
  	
  </head>
 
  <body scroll="no" id="index" onload="load()">
   <!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/Datetime/Datetime.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/ExTreeEditor.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/ConfirmTextField.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/SearchPanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNodeUI.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNode.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNodeLoader.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxAsyncTreeNode.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/ColumnNodeUI.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/TitlePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>
	<script type="text/javascript" src="script/RightGrid.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/dataTable.js" charset="UTF-8"></script>
	<!-- 入口程序 -->
    <script type="text/javascript">
    	var id="${param.id}";
		Ext.onReady(function(){
			Ext.QuickTips.init();
			
			
			//rightGrid.getStore().reload();
		  	//程序加载完成后撤除加载图片
		    setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250);
		});	
    </script>
    <!-- 加载脚本库  结束 -->
  
  <!-- 加载等待图标 开始 -->
	<div id="loading-mask" style=""></div>
	<div id="loading">
	  <div class="loading-indicator">
	  	<img src="${contextPath}/resources/extanim32.gif" width="32" height="32" style="margin-right:8px;" align="absmiddle"/>Loading...
	  </div>
	</div>
   	 <!-- 加载等待图标 结束 -->
  
   	   <div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						<div class="pop-butMain">
							<input type="radio" name="type" value="PZ" checked="checked" onclick="selectType(this);">正向有功
							<input type="radio" name="type" value="PF" onclick="selectType(this);">反向有功
							<input type="radio" name="type" value="QZ" onclick="selectType(this);">正向无功
							<input type="radio" name="type" value="QF" onclick="selectType(this);">反向无功
			    			<input type='text' name='curDate' onchange="" class='Wdate' onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',readOnly:true})">
			    			<input type="button" value="查询" onclick="reFindData();" class="pop-but01">
							<input type="button" value="上一天" onclick="submitTopDay()" class="pop-but01">
			    			<input type="button" value="下一天" onclick="submitNextDay();" class="pop-but01">
						</div>
					</div>
				    <table width="100%" height="70%" align="center"class="LabelBodyTb">
				    	<tr>
				    		<td class="POPtab-Title" width="25%">&nbsp;</td>
				    		<td class="POPtab-Title">表码读数</td>
				    		<td class="POPtab-Title">本日累计电量</td>
				    		<td class="POPtab-Title">本月累计电量</td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">正向有功</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" id="PZ"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" id="BRPZ"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" id="BYPZ"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">反向有功</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" id="PF"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" id="BRPF"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" id="BYPF"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">正向无功</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" id="QZ"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" id="BRQZ"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" id="BYQZ"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">反向无功</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" id="QF"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" id="BRQF"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" id="BYQF"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">有功累计</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" id="BRYG"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" id="BYYG"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">无功累计</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" id="BRWG"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" id="BYWG"></td>
				    	</tr>
				    	<tr>
				    	   <td colspan="4">
				    	   		<div id="flashcontent"></div>
				    	   </td>
				    	</tr>
				    	<tr>
				    	   <td colspan="4">
				    	   		<div id="gridcontent"></div>
				    	   </td>
				    	</tr>
				    </table>	
				    <iframe name="ftarget" style="display:none"></iframe>
    			</div>
    		</div>
    	</div>
   	
    
    <!-- 页面内容 to do something in here -->

  </body>
</html>
