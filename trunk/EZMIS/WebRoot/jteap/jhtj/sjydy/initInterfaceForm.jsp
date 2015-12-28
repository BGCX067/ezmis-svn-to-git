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
  
  <script language="javascript" type="text/javascript" src="${contextPath}/component/My97DatePicker/WdatePicker.js"></script>
  <script language="javascript">
			var allData=new Array();
			var k=0;
			function init(){
				var initValue="${param.initValue}";//初始化数据
				if(initValue==""){
					alert("初始化接口失败!");
				}else{
					//具体显示的字段
					var datas=initValue.split("!");
					for(var i=0;i<datas.length;i++){
						var items=datas[i].split(",");
						allData[k]=items[0];
						k++;
						$(items[0]+"Tr").style.display="block";
						var result="";
						//年和月放在一起
						if(items[0]!="SRQ"&&items[0]!="ERQ"){
							result="<SELECT NAME='"+items[0]+"' style='width:50%' onchange='setDay(this);'>";
						}
						
						//是机组就特别处理
						if(items[0]=="JZ"){
							var jzs=items[1];
							var jzArray=jzs.split("-");
							for(var j=0;j<jzArray.length;j++){
								if(j==0){
									result=result+"<OPTION VALUE="+jzArray[j]+" selected>"+jzArray[j]+"</OPTION>";
								}else{
									result=result+"<OPTION VALUE="+jzArray[j]+">"+jzArray[j]+"</OPTION>";
								}
							}
						}else{
							if(items[0]!="SRQ"&&items[0]!="ERQ"){
								var start=parseInt(items[1]);
								var end=parseInt(items[2]);
								for(var j=start;j<=end;j++){
									result=result+"<OPTION VALUE="+j+">"+j+"</OPTION>";
								}
							}
						}
						
						if(items[0]!="SRQ"&&items[0]!="ERQ"){
							result=result+"</SELECT>";
							$(items[0]+"Td2").innerHTML=result;
							if(items[0]!="JZ"){
								//默认值
								$(items[0]).value=items[3];
							}
						}
					}
				}
			}
			
			/**是否是年或者月
			**/
			function isYearOrMonth(id){
				if(id=="NIAN"||id=="YUE"||id=="SNIAN"||id=="SYUE"||id=="ENIAN"||id=="EYUE"){
					return true;
				}else{
					return false;
				}
			}
			
			/**改变年或者月的时候更新日
			**/
			function setDay(obj){
				var link="${contextPath}/jteap/jhtj/sjwh/tjSjwhAction!findLastDayAction.do"; 
				if(obj.name=="NIAN" || obj.name=="YUE"){
					if($("NIAN")!=null&&$("YUE")!=null){
						var param={};
						param.curDate=$("NIAN").value+"-"+$("YUE").value+"-1";
						AjaxRequest_Sync(link,param,function(lastDateRes){
							var lastDateText = lastDateRes.responseText;
							var lastDateObj = lastDateText.evalJSON();
							if(lastDateObj.success){
								var lastDate=lastDateObj.day;
								if(lastDate!=""){
									var end=parseInt(lastDate);
									var result="<SELECT NAME='RI' style='width:50%'>";
									for(var i=1;i<=end;i++){
										result=result+"<OPTION VALUE="+i+">"+i+"</OPTION>";
									}
									result=result+"</SELECT>";
									$("RITd2").innerHTML=result;
								}else{
									alert("初始日期失败!");
								}
							}
						});
					}
				}
			}
			
			
			/**提交
			**/
			function submitForm(){
				var result="";
				for(var i=0;i<allData.length;i++){
					result=result+allData[i]+","+$(allData[i]).value+"!";
				}
				if(result!=""){
					result=result.substring(0,result.length-1);
				}else{
					result=undefined;
				}
				window.parent.returnValue=result;
				window.close();
				
			}
			
		</script>
  <body onload="init();">
   	 <form action="/tjSjwhAction.do?action=saveOrUpdate" onsubmit="">


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
				<td height="25" align="center" class="TitleTable">初始化日期</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<!--  end of 标题区域 --> <!--  start of 内容区域 -->
<div class="MainDiv"><!--  start of 按钮区域 -->

<!--  end of 按钮区域 --> <!--  start of 一分栏区域 -->
<table width="98%" border="0" align="center" cellpadding="0"
	cellspacing="0" class="FBorder">
	<tr class="GridCellJ" id="NIANTr" style="display:none">
		<td width="30%" class="GridCellL" id="NIANTd1">年</td>
		<td width="70%" class="GridCellL2" id="NIANTd2">	
		</td>
	</tr>

	<tr class="GridCellJ" id="YUETr" style="display:none">
		<td width="30%" class="GridCellL" id="YUETd1">月</td>
		<td width="70%" class="GridCellL2" id="YUETd2">
			
		</td>
	</tr>

	
	<tr class="GridCellJ" id="RITr" style="display:none">
		<td width="30%" class="GridCellL" id="RITd1">日</td>
		<td width="70%" class="GridCellL2" id="RITd2">
			<input name="RI" type="text"  maxlength="20" width="50%"  class="Input02">
		</td>
	</tr>
	
	<tr class="GridCellJ" id="SNIANTr" style="display:none">
		<td width="30%" class="GridCellL" id="SNIANTd1">起始年</td>
		<td width="70%" class="GridCellL2" id="SNIANTd2">
		</td>
	</tr>
	
	<tr class="GridCellJ" id="SYUETr" style="display:none">
		<td width="30%" class="GridCellL" id="SYUETd1">起始月</td>
		<td width="70%" class="GridCellL2" id="SYUETd2">
		</td>
	</tr>
	
	<tr class="GridCellJ" id="SRQTr" style="display:none">
		<td width="30%" class="GridCellL" id="SRQTd1">起始日期</td>
		<td width="70%" class="GridCellL2" id="SRQTd2">
			<div id="conditionDiv1">
				 <input type="text" value="${condition_startDt}" width="80px" name="SRQ" class="Wdate" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" maxlength="20">
			</div>
		</td>
	</tr>
	
	<tr class="GridCellJ" id="ENIANTr" style="display:none">
		<td width="30%" class="GridCellL" id="ENIANTd1">终止年</td>
		<td width="70%" class="GridCellL2" id="ENIANTd2">
		</td>
	</tr>
	
	<tr class="GridCellJ" id="EYUETr" style="display:none">
		<td width="30%" class="GridCellL" id="EYUETd1">终止月</td>
		<td width="70%" class="GridCellL2" id="EYUETd2">
		</td>
	</tr>
	
	<tr class="GridCellJ" id="ERQTr" style="display:none">
		<td width="30%" class="GridCellL" id="ERQTd1">终止日期</td>
		<td width="70%" class="GridCellL2" id="ERQTd2">
			<div id="conditionDiv2">
				<input type="text"  name="ERQ" value="${condition_endDt}" class="Wdate" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" maxlength="20"/>
		    </div>
		</td>
	</tr>
	
	<tr class="GridCellJ" id="JZTr" style="display:none">
		<td width="30%" class="GridCellL" id="JZTd1">机组</td>
		<td width="70%" class="GridCellL2" id="JZTd2">
		</td>
	</tr>
</table>
</div>
<!--  end of 一分栏区域 --> <!--  start of 底部按钮区域 -->
<table width="98%" border="0" cellpadding="0" cellspacing="0"
	class="FBorder">
	<tr>
		<td class="GridCellL">
		<div class="ButtonDivRight">
			<Input type="button" value="确认"  id="saveButton" class="button01" onclick="submitForm();"/>
			<Input type="button" value="关闭" class="button01" onclick="window.close();"/></div>
		</td>
	</tr>
</table>
<!--  end of 底部按钮区域 -->
<!--  end of 内容区域 --></div>
	
	
</form>
   	 
   	 
   	 
  </body>
</html>
