<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.yx.runlog.manager.PhysicLogsManager"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<link rel="stylesheet" href="index.css" type="text/css"></link>
	<style>
		TABLE {
			font-size: 12px;
			color: 043A7A;
			background-color: #E3E3E3;
		}
		TD {
			background-color: white;
		}
		.ValueInput{
			border: 1px solid #FFFFFF;
			width: 100px;
			height: 18px;
			text-align: right;
		}
		.ValueInput2{
			border: 1px solid #FFFFFF;
			width: 100px;
			height: 18px;
			text-align: right;
			background-color: #F5DEB3;
		}
	</style>
  </head>
 
  <body id="index">
	
	<!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>
	<script type="text/javascript" src="script/main.js"></script>	
    <!-- 加载脚本库  结束 -->
  	
    <script type="text/javascript">
    	var row_ids = new Array();
    	var col_ids = new Array();
    </script>
	
	<%
		//表Id
		String tableId = request.getParameter("tableId");
		//值班时间
		String zbsj = request.getParameter("zbsj");
		//值班班次
		String zbbc = request.getParameter("zbbc");
		
		PhysicLogsManager physicLogsManager = (PhysicLogsManager)SpringContextUtil.getBean("physicLogsManager");
	%>
	
	<table id="myTable" cellspacing="1">
		<%
		List<Map<String, Object>> list = physicLogsManager.findIntiData(tableId,zbsj,zbbc);
		for(Map<String, Object> rowMap : list){
			//字段ID
			String columnId = (String)rowMap.get("columnId");
			//额定值
			String edingzhi = (String)rowMap.get("edingzhi");
		%>
		
		<script type="text/javascript">
			row_ids.push("<%=columnId%>");
		</script>
		
		<tr id="<%=columnId%>">
			<%
				for(Map.Entry<String, Object> entry : rowMap.entrySet()){
					String readonly = "";
					if(entry.getKey().equals("sumBan")){
						readonly = "readonly='readonly'";
					}else if(entry.getKey().equals("avgBan")){
						readonly = "readonly='readonly'";
					}else if(entry.getKey().equals("sumDay")){
						readonly = "readonly='readonly'";
					}else if(entry.getKey().equals("avgDay")){
						readonly = "readonly='readonly'";
					}else if(entry.getKey().equals("sumMonth")){
						readonly = "readonly='readonly'";
					}else if(entry.getKey().equals("avgMonth")){
						readonly = "readonly='readonly'";
					}
					if(entry.getKey().equals("id")){
			%>
			<!-- 额定值 -->
			<td align="center" nowrap="nowrap" width="100" height="20">
				<%=edingzhi %>
				<input type="text" id="<%=entry.getKey()+"_"+columnId%>" value="<%=entry.getValue()%>" style="display: none;">
			</td>
			<%		
					}else if(!entry.getKey().equals("columnId") && !entry.getKey().equals("edingzhi")){
			 %>	
			
			<script type="text/javascript">
				col_ids.push("<%=entry.getKey()%>");
			</script>
			
			<td align="right" nowrap="nowrap" width="100">
				<input type="text" id="<%=entry.getKey()+"_"+columnId%>" value="<%=entry.getValue()%>" class="ValueInput" onKeyPress="regInput(/^(-)?\d*\.?\d{0,4}$/)" onfocus="tdInputFocus('<%=entry.getKey()+"_"+columnId%>')" onblur="tdInputBlur('<%=entry.getKey()+"_"+columnId%>')" <%=readonly%> >
			</td>
			<%		
					}
				}
			 %>
		</tr>
		<%
		}
		 %>
	</table>
	
	<form name="myForm" method="post" action="${contextPath}/jteap/yx/runlog/web/PhysicLogsAction!exportExcel.do">
		<input name="json" type="hidden">
		<input name="tableId" type="hidden">
		<input name="zbbc" type="hidden">
	</form>
	
	<script type="text/javascript">
		
		//设置iframe滚动条级联
		window.attachEvent("onscroll",function(){
	    	parent.topIF.document.body.scrollLeft = document.body.scrollLeft;
	    	parent.leftIF.document.body.scrollTop = document.body.scrollTop;
		});
		
		//设置输入项样式
		function tdInputFocus(id){
			document.getElementById(id).className = "ValueInput2";
			document.getElementById(id).select();
		}
		function tdInputBlur(id){
			document.getElementById(id).className = "ValueInput";
		}
		
		//上下左右键 移动表的选中项
		function document.onkeydown(){
			if (event.keyCode!=13&&event.keyCode!=37&&event.keyCode!=38&&event.keyCode!=39&&event.keyCode!=40) return;
				
				var el_input = window.event.srcElement;
				var el_input_id = el_input.id;
				
				//列id 如 VALUE_0
				var el_col_id = el_input_id.split("_")[0];
				//行id 如 40288abe29afde1e0129b0a6c7880002
				var el_tr_id = el_input_id.split("_")[1];
				
				var el_tr = document.getElementById(el_tr_id);
												
				//向下移动一格
				if((event.keyCode==13||event.keyCode==40)){
					for(var i=0; i<row_ids.length; i++){
						if(el_tr_id == row_ids[i]){
							var active_row_id = "";
							if(row_ids[i+1] != null){
								active_row_id = row_ids[i+1];
							}else{
								active_row_id = row_ids[0];
							}
							if(active_row_id != ""){
								var active_input_id = el_col_id + "_" + active_row_id;
								document.getElementById(active_input_id).focus();
							}
						}
					}					
				}
				//向上移动一格
				if((event.keyCode==38)){
					for(var i=0; i<row_ids.length; i++){
						if(el_tr_id == row_ids[i]){
							var active_row_id = "";
							if(row_ids[i-1] != null){
								active_row_id = row_ids[i-1];
							}else{
								active_row_id = row_ids[row_ids.length-1];
							}
							if(active_row_id != ""){
								var active_input_id = el_col_id + "_" + active_row_id;
								document.getElementById(active_input_id).focus();
							}
						}
					}			
				}
				//向左移动一格
				if((event.keyCode==37)){
					//if(!event.ctrlKey){
					//	return;
					//}
					for(var i=0; i<col_ids.length; i++){
						if(el_col_id == col_ids[i]){
							var active_col_id = "";
							if(col_ids[i-1] != null){
								active_col_id = col_ids[i-1];
							}else{
								active_col_id = col_ids[col_ids.length-1];
							}
							if(active_col_id != ""){
								var active_input_id = active_col_id + "_" + el_tr_id;
								document.getElementById(active_input_id).focus();
							}
						}
					}							
				}
				//向右移动一格
				if((event.keyCode==39)){
					for(var i=0; i<col_ids.length; i++){
						if(el_col_id == col_ids[i]){
							var active_col_id = "";
							if(col_ids[i+1] != null){
								active_col_id = col_ids[i+1];
							}else{
								active_col_id = col_ids[0];
							}
							if(active_col_id != ""){
								var active_input_id = active_col_id + "_" + el_tr_id;
								document.getElementById(active_input_id).focus();
							}
						}
					}				
				}
		}
		
		//单元格验证
		function regInput(reg){
			var srcElem	= event.srcElement;
			var oSel = document.selection.createRange();
			var srcRange = srcElem.createTextRange();
			oSel.setEndPoint("StartToStart", srcRange);
			var num = oSel.text + String.fromCharCode(event.keyCode) + srcRange.text.substr(oSel.text.length);
			event.returnValue = reg.test(num);
		}
		
		//取数
		function getData(mask){
			logQushu("<%=tableId%>","<%=zbsj%>","<%=zbbc%>",mask);
		}
		
		//保存
		function save(mask){
			var all_params = [];
			var tableId = "<%=tableId%>";
			var zbbc = parent.comBc.options[parent.comBc.selectedIndex].innerText;
			var zbsj = parent.dtTime.value;
			
			for(var index=0; index<row_ids.length; index++){
				var params = {};
				params.columnId = row_ids[index];
								
				var tr = document.getElementById(row_ids[index]);
				var td_array = tr.childNodes;
				for(var i=0; i<td_array.length; i++){
					var td_items = td_array[i].childNodes;
					for(var j=0; j<td_items.length; j++){
						if(td_items[j].type == "text" && td_items[j].readOnly == false){
							var pid = td_items[j].id;
							var pvalue = td_items[j].value;
							
							eval("params."+pid+"=pvalue");
						}
					}
				}
				all_params.push(params);
			}
			
			// 保存全部日志记录
			saveMain(all_params,tableId,zbbc,zbsj,mask);
		}
		
		//导出
		function exportData(mask){
			var all_params = [];
			var tableId = "<%=tableId%>";
			var zbbc = parent.comBc.options[parent.comBc.selectedIndex].innerText;
			
			for(var index=0; index<row_ids.length; index++){
				var params = {};
				params.columnId = row_ids[index];
								
				var tr = document.getElementById(row_ids[index]);
				var td_array = tr.childNodes;
				for(var i=0; i<td_array.length; i++){
					var td_items = td_array[i].childNodes;
					for(var j=0; j<td_items.length; j++){
						if(td_items[j].type == "text"){
							var pid = td_items[j].id;
							var pvalue = td_items[j].value;
							
							eval("params."+pid+"=pvalue");
						}
					}
				}
				all_params.push(params);
			}
			var json = Ext.util.JSON.encode(all_params);
			myForm.json.value = json;
			myForm.tableId.value = tableId;
			myForm.zbbc.value = zbbc;
			
			//导出全部日志记录
			myForm.submit();
			
			mask.hide();
		}
	</script>
	  	
  </body>
</html>
