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
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>
  	<script language="javascript" type="text/javascript" src="${contextPath}/component/My97DatePicker/WdatePicker.js"></script>
  	<style type="text/css">
		.lblText{
			text-align: center;
			background: #D2E0F2;
			font-size: 14px;
			font-weight: bold;
			color: #0065B3;
		}  		
  	</style>
  </head>
 <script type="text/javascript" src="script/dnbBbForm.js" charset="UTF-8"></script>
 <%@ include file="/inc/ext-all.jsp" %>
 
  <body id="index" onload="loadData();">
   	   <div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
				    <div id="div6" style="overflow-x:auto;overflow-y:auto;">
				    <table width="98.5%" align="center" class="LabelBodyTb">
				    	<tr>
				    		<td colspan="9" height="20" align="center" style="background-color: #0065b3;color: #FFF;">湖北能源集团鄂州发电公司电能量日报表
				    		</td>
				    	</tr>
				    	<tr>
				    		<td colspan="9" height="20" align="right" style="background-color: #0065b3;color: #FFF;">
				    			<input type='text' name='curDate' onchange="" class='Wdate' onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',readOnly:true})">
				    			<input type="button" value="查询" onclick="reFindData();" class="pop-but01">
								<input type="button" value="上一天" onclick="submitTopDay()" class="pop-but01">
			    				<input type="button" value="下一天" onclick="submitNextDay();" class="pop-but01">
			    				<input type="button" value="导出Excel" onclick="exportDnbsExcel();" class="pop-but01">
			    				<input type="button" value="换表处理" onclick="change();" class="pop-but01">
				    		</td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" colspan="2" nowrap="nowrap" >计算开始时间</td>
				    		<td class="POPtab-Title" colspan="2" nowrap="nowrap">00:00</td>
				    		<td class="POPtab-Title" nowrap="nowrap">计算结束时间</td>
				    		<td class="POPtab-Title" colspan="2" nowrap="nowrap">24:00</td>
				    		<td class="POPtab-Title" nowrap="nowrap">&nbsp;</td>
				    		<td class="POPtab-Title" nowrap="nowrap">单位:kWh/kvarh</td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">&nbsp;</td>
				    		<td class="POPtab-Title" width="10%">&nbsp;</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">时刻</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%">正向有功总<br>(表码二次值)</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%">反向有功总<br>(表码二次值)</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%">正向无功总<br>(表码二次值)</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%">反向无功总<br>(表码二次值)</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">时间段</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%">有功增量总<br>(计算一次值)</td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">1</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">E01鄂左线主表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-19-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-19-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-19-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-19-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="TOTAL-19-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-19"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-19-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-19-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-19-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-19-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="TOTAL-19-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-19"><input type="hidden" name="CT-19"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-19-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-19-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-19-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-19-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="TOTAL-19-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-19"><input type="hidden" name="PT-19"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-19-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-19-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-19-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-19-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="TOTAL-19-24"></td>
				    	</tr>
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">2</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">E01鄂左线辅表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-25-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-25-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-25-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-25-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="TOTAL-25-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-25"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-25-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-25-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-25-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-25-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="TOTAL-25-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-25"><input type="hidden" name="CT-25"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-25-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-25-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-25-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-25-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="TOTAL-25-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-25"><input type="hidden" name="PT-25"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-25-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-25-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-25-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-25-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="TOTAL-25-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">3</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">E03花鄂线主表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-20-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-20-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-20-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-20-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="TOTAL-20-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-20"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-20-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-20-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-20-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-20-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="TOTAL-20-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-20"><input type="hidden" name="CT-20"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-20-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-20-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-20-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-20-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="TOTAL-20-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-20"><input type="hidden" name="PT-20"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-20-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-20-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-20-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-20-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="TOTAL-20-24"></td>
				    	</tr>
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">4</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">E03花鄂线辅表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-26-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-26-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-26-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-26-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="TOTAL-26-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-26"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-26-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-26-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-26-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-26-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-26-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-26"><input type="hidden" name="CT-26"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-26-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-26-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-26-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-26-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-26-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-26"><input type="hidden" name="PT-26"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-26-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-26-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-26-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-26-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-26-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">5</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">E05鄂下线主表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-15-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-15-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-15-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-15-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-15-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-15"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-15-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-15-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-15-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-15-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-15-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-15"><input type="hidden" name="CT-15"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-15-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-15-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-15-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-15-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-15-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-15"><input type="hidden" name="PT-15"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-15-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-15-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-15-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-15-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-15-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">6</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">E05鄂下线辅表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-30-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-30-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-30-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-30-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-30-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-30"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-30-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-30-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-30-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-30-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-30-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-30"><input type="hidden" name="CT-30"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-30-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-30-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-30-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-30-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-30-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-30"><input type="hidden" name="PT-30"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-30-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-30-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-30-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-30-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-30-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">7</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">E08鄂郎线主表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-16-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-16-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-16-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-16-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-16-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-16-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-16-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-16-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-16-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-16-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-16"><input type="hidden" name="CT-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-16-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-16-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-16-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-16-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-16-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-16"><input type="hidden" name="PT-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-16-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-16-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-16-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-16-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-16-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">8</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">E08鄂郎线辅表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-28-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-28-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-28-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-28-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-28-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-28"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-28-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-28-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-28-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-28-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-28-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-28"><input type="hidden" name="CT-28"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-28-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-28-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-28-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-28-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-28-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-28"><input type="hidden" name="PT-28"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-28-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-28-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-28-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-28-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-28-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">9</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">E10鄂华线主表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-17-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-17-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-17-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-17-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-17-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-17"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-17-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-17-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-17-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-17-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-17-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-17"><input type="hidden" name="CT-17"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-17-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-17-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-17-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-17-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-17-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-17"><input type="hidden" name="PT-17"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-17-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-17-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-17-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-17-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-17-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">10</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">E10鄂华线辅表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-29-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-29-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-29-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-29-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-29-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-29"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-29-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-29-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-29-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-29-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-29-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-29"><input type="hidden" name="CT-29"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-29-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-29-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-29-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-29-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-29-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-29"><input type="hidden" name="PT-29"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-29-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-29-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-29-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-29-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-29-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">11</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">E12#02启备变主表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-18-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-18-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-18-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-18-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-18-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-18"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-18-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-18-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-18-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-18-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-18-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-18"><input type="hidden" name="CT-18"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-18-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-18-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-18-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-18-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-18-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-18"><input type="hidden" name="PT-18"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-18-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-18-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-18-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-18-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-18-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">12</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">E13华容II回线主表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-21-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-21-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-21-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-21-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-21-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-21"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-21-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-21-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-21-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-21-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-21-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-21"><input type="hidden" name="CT-21"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-21-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-21-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-21-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-21-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-21-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-21"><input type="hidden" name="PT-21"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-21-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-21-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-21-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-21-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-21-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">13</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">E13华容II回线辅表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-22-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-22-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-22-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-22-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-22-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-22"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-22-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-22-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-22-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-22-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-22-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-22"><input type="hidden" name="CT-22"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-22-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-22-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-22-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-22-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-22-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-22"><input type="hidden" name="PT-22"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-22-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-22-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-22-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-22-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-22-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">14</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">#1发电机主表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-23-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-23-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-23-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-23-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-23-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-23"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-23-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-23-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-23-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-23-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-23-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-23"><input type="hidden" name="CT-23"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-23-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-23-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-23-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-23-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-23-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-23"><input type="hidden" name="PT-23"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-23-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-23-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-23-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-23-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-23-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">15</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">#2发电机主表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-24-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-24-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-24-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-24-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-24-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-24-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-24-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-24-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-24-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-24-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-24"><input type="hidden" name="CT-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-24-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-24-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-24-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-24-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-24-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-24"><input type="hidden" name="PT-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-24-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-24-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-24-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-24-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-24-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">16</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">#2高厂变</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-31-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-31-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-31-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-31-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-31-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-31"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-31-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-31-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-31-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-31-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-31-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-31"><input type="hidden" name="CT-31"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-31-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-31-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-31-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-31-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-31-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-31"><input type="hidden" name="PT-31"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-31-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-31-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-31-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-31-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-31-24"></td>
				    	</tr>
				    	
				    	
				    	<tr>
				    		<td class="POPtabTbEntry2" colspan="9" style="background-color: #0065b3;color: #FFF;" height="20"></td>
				    	</tr>
				    	
				    	<!-- 第二张表格开始 -->
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">1</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">E202鄂凤线主表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-3-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-3-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-3-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-3-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-3-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-3"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-3-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-3-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-3-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-3-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-3-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-3"><input type="hidden" name="CT-3"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-3-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-3-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-3-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-3-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-3-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-3"><input type="hidden" name="PT-3"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-3-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-3-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-3-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-3-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-3-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">2</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">E202鄂凤线辅表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-4-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-4-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-4-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-4-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-4-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-4"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-4-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-4-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-4-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-4-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-4-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-4"><input type="hidden" name="CT-4"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-4-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-4-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-4-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-4-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-4-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-4"><input type="hidden" name="PT-4"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-4-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-4-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-4-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-4-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-4-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">3</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">E201鄂光线主表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-5-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-5-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-5-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-5-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-5-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-5"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-5-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-5-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-5-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-5-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-5-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-5"><input type="hidden" name="CT-5"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-5-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-5-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-5-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-5-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-5-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-5"><input type="hidden" name="PT-5"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-5-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-5-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-5-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-5-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-5-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">4</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">E201鄂光线辅表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-6-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-6-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-6-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-6-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-6-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-6"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-6-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-6-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-6-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-6-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-6-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-6"><input type="hidden" name="CT-6"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-6-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-6-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-6-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-6-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-6-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-6"><input type="hidden" name="PT-6"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-6-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-6-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-6-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-6-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-6-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">5</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">E206#02启备变主表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-11-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-11-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-11-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-11-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-11-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-11"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-11-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-11-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-11-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-11-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-11-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-11"><input type="hidden" name="CT-11"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-11-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-11-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-11-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-11-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-11-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-11"><input type="hidden" name="PT-11"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-11-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-11-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-11-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-11-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-11-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">6</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">E206#02启备变辅表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-12-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-12-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-12-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-12-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-12-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-12"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-12-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-12-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-12-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-12-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-12-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-12"><input type="hidden" name="CT-12"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-12-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-12-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-12-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-12-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-12-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-12"><input type="hidden" name="PT-12"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-12-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-12-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-12-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-12-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-12-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">7</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">E208鄂蒲一回线主表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-7-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-7-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-7-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-7-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-7-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-7"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-7-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-7-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-7-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-7-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-7-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-7"><input type="hidden" name="CT-7"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-7-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-7-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-7-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-7-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-7-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-7"><input type="hidden" name="PT-7"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-7-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-7-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-7-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-7-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-7-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">8</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">E208鄂蒲一回线辅表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-8-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-8-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-8-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-8-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-8-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-8"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-8-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-8-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-8-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-8-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-8-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-8"><input type="hidden" name="CT-8"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-8-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-8-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-8-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-8-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-8-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-8"><input type="hidden" name="PT-8"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-8-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-8-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-8-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-8-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-8-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">9</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">E204鄂蒲二回线主表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-9-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-9-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-9-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-9-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-9-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-9"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-9-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-9-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-9-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-9-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-9-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-9"><input type="hidden" name="CT-9"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-9-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-9-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-9-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-9-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-9-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-9"><input type="hidden" name="PT-9"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-9-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-9-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-9-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-9-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-9-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">10</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">E204鄂蒲二回线辅表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-10-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-10-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-10-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-10-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-10-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-10"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-10-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-10-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-10-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-10-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-10-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-10"><input type="hidden" name="CT-10"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-10-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-10-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-10-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-10-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-10-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-10"><input type="hidden" name="PT-10"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-10-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-10-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-10-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-10-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-10-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">11</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">#3主变主表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-13-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-13-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-13-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-13-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-13-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-13"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-13-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-13-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-13-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-13-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-13-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-13"><input type="hidden" name="CT-13"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-13-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-13-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-13-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-13-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-13-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-13"><input type="hidden" name="PT-13"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-13-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-13-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-13-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-13-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-13-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">12</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">#4主变主表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-14-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-14-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-14-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-14-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-14-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-14"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-14-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-14-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-14-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-14-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-14-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-14"><input type="hidden" name="CT-14"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-14-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-14-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-14-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-14-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-14-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-14"><input type="hidden" name="PT-14"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-14-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-14-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-14-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-14-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-14-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">13</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">#3发电机主表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-1-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-1-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-1-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-1-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-1-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-1"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-1-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-1-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-1-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-1-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-1-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-1"><input type="hidden" name="CT-1"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-1-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-1-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-1-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-1-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-1-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-1"><input type="hidden" name="PT-1"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-1-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-1-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-1-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-1-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-1-24"></td>
				    	</tr>
				    	
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">14</td>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%">#4发电机主表</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-2-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-2-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-2-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-2-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-8</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-2-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="ELECBH-2"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-2-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-2-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-2-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-2-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">8-16</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-2-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="CTS-2"><input type="hidden" name="CT-2"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PZ-2-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="PF-2-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QZ-2-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="QF-2-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-2-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" width="10%"><input type="text" class="lblText" readonly="readonly" name="PTS-2"><input type="hidden" name="PT-2"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PZ-2-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="PF-2-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QZ-2-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="QF-2-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">0-24</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%" id=""><input type="text" style="width:80px;" name="TOTAL-2-24"></td>
				    	</tr>
				    	
				    	
				    	<tr>
				    		<td class="POPtabTbEntry2" style="background-color: #0065b3;color: #FFF;" colspan="9" height="20"></td>
				    	</tr>
				    	
				    	
				    	<!-- 第三个表格开始 -->
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap">&nbsp;</td>
				    		<td class="POPtab-Title" nowrap="nowrap">&nbsp;</td>
				    		<td class="POPtab-Title" style="text-align: center;" nowrap="nowrap">&nbsp;</td>
				    		<td class="POPtab-Title" style="text-align: center;" colspan="4">平均负荷(kW)</td>
				    		<td class="POPtab-Title" style="text-align: center;" colspan="2" nowrap="nowrap">综合厂用电率</td>
				    	</tr>
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" nowrap="nowrap" colspan="2">综合厂用电率</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%">1号机</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%">2号机</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%">3号机</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%">4号机</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">一期</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%">二期</td>
				    	</tr>
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">1</td>
				    		<td class="POPtab-Title" nowrap="nowrap" rowspan="4">当日均</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点-08点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="JZ-23-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="JZ-24-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="JZ-1-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="JZ-2-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap"><input type="text" style="width:80px;" name="JZ-1Q-00"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="JZ-2Q-00"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">08点-16点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="JZ-23-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="JZ-24-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="JZ-1-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="JZ-2-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap"><input type="text" style="width:80px;" name="JZ-1Q-08"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="JZ-2Q-08"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">16点-24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="JZ-23-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="JZ-24-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="JZ-1-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="JZ-2-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap"><input type="text" style="width:80px;" name="JZ-1Q-16"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="JZ-2Q-16"></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap">00点-24点</td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="JZ-23-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="JZ-24-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="JZ-1-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="JZ-2-24"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap"><input type="text" style="width:80px;" name="JZ-1Q-TOTLE"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="JZ-2Q-TOTLE"></td>
				    	</tr>
				    	
				    	
				    	
				    	
				    	<tr>
				    		<td class="POPtab-Title" rowspan="4" nowrap="nowrap">2</td>
				    		<td class="POPtab-Title" nowrap="nowrap" colspan="2"><input type="text" class="lblText" readonly="readonly"  name="txtDyj" value="当月均(01日至日)"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="JZ1-CYJ"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="JZ2-CYJ"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="JZ3-CYJ"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="JZ4-CYJ"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" nowrap="nowrap"><input type="text" style="width:80px;" name="1Q-CYJ"></td>
				    		<td class="POPtabTbEntry2" style="text-align: center;" width="15%"><input type="text" style="width:80px;" name="2Q-CYJ"></td>
				    	</tr>
				    	
				    </table>	
				    </div>
    			</div>
    		</div>
    	</div>
  </body>
</html>
