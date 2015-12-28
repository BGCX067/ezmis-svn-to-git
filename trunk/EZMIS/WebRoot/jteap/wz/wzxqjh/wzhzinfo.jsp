<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
 	<%@ include file="/inc/meta.jsp" %>
	<title>JTEAP 2.0</title>
	<%
		String selectedObj = (String)request.getParameter("selectedObj");
		String total = (String)request.getParameter("total");
		String totalSL = (String)request.getParameter("totalSL");
		Double moneyTotal = Double.valueOf(total);
		Double pzslTotal = Double.valueOf(totalSL);
		String selectedArray[] = selectedObj.split(",");
		int objSize = selectedArray.length/6;
	%>
	<style>
		table#border{   
			border-top:#000 1px solid;   
			border-left:#000 1px solid;   
		}   
		table#border td{   
			border-bottom:#000 1px solid;   
			border-right:#000 1px solid;   
		}  
		h1{
			font-family:楷体, "Times New Roman";
		}
		table{
			font-size:13px;
			font-weight:lighter;
			font-family:宋体, "Times New Roman";
		}
		.pop-but01 {
			BORDER-RIGHT: #ceb80d 1px solid;
			BORDER-TOP: #ceb80d 1px solid;
			FONT-SIZE: 12px;
			FILTER: progid:DXImageTransform.Microsoft.Gradient(GradientType=0, StartColorStr=#ffffff, EndColorStr=#f3dc41); 								    BORDER-LEFT: #ceb80d 1px solid;
			CURSOR: hand;
			COLOR: #000000;
			BORDER-BOTTOM: #ceb80d 1px solid;
			margin: 0px 2px 0px 2px;
			text-decoration: none;
			padding: 1px 0px 0px 0px;
			height: 23px;
			width: auto;
		}
	</style>
	<script type="text/javascript">
		function exportExcel() {
			var xqjhMxIdArray = new Array();
			var selectedObj = '<%=selectedObj%>';
			var selectedObjArray = new Array();
			selectedObjArray = selectedObj.split(",");
			for(var i = 1; i <= selectedObjArray.length/5; i++){
				xqjhMxIdArray.push(selectedObjArray[6*i-6]);
			}
			var path = contextPath + "/jteap/wz/xqjh/XqjhDetailAction!exportXqjhDetailHzAction.do?xqjhMxIdArray="+xqjhMxIdArray;
			window.open(path);
		}
	</script>
  </head>

	<body>
		<center><h1>需求计划物资汇总</h1></center>
		<div style="text-align:right">
    		<input type="button" id="exprotExcel" value="Excel导出" onclick="exportExcel()" class="pop-but01" />
		</div>
		<br/>
		<table id="border" border="0" cellspacing="0" width="100%" align="center">  
			<tr>
				<td width="20%" align="center">
					单据编号
				</td>
				<td width="30%" align="center">
					物资名称规格
				</td>
				<td width="10%" align="center">
					计量单位
				</td>
				<td width="20%" align="center">
					批准数量
				</td>
				<td width="20%" align="center">
					金额
				</td>
			</tr>
			<%
				double factor = Math.pow(10, 2); 
				for(int i = 1; i <= objSize; i++){
					
			%>
			<tr>
					<td>
						<%=selectedArray[6*i-5] %>
					</td>
					<td>
						<%=selectedArray[6*i-4] %>
					</td>
					<td align="center">
						<%=selectedArray[6*i-3] %>
					</td>
					<td align="right">
						<%=new Double(Math.floor((Double.valueOf(selectedArray[6*i-2]) * factor + 0.5) / factor)) %>
					</td>
					<td align="right">
						<%=new Double(Math.floor((Double.valueOf(selectedArray[6*i-1]) * factor + 0.5) / factor)) %>
					</td>
			</tr>
			<%
			}
			%>
		</table>
		<table border="0" align="center" width="100%">
			<tr>
				<td width="50%">
				</td>
				<td width="10%" align="center">
					合计:
				</td>
				<td width="20%" align="right">
					<%=new Double(Math.floor((Double.valueOf(pzslTotal) * factor + 0.5) / factor)) %>
				</td>
				<td width="20%" align="right">
					<%=new Double(Math.floor((Double.valueOf(moneyTotal) * factor + 0.5) / factor)) %>
				</td>
			</tr>
		</table>
	</body>
</html>

