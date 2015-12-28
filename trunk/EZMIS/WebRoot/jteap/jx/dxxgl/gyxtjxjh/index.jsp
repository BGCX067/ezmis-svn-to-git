<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.jteap.system.dict.manager.DictManager"%>
<%@page import="com.jteap.system.dict.model.Dict"%>

<%@ include file="/inc/import.jsp"%>
<html>
	<head>
		<%@ include file="/inc/meta.jsp"%>
		<title>JTEAP 2.0</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
		<style>
			TABLE {
				font-size: 12px;
				color: 043A7A;
			}
		</style>
	</head>

	<body scroll="no" id="index">
	
	<!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>
	<script language="javascript" type="text/javascript" src="${contextPath}/component/My97DatePicker/WdatePicker.js"></script>
    <!-- 加载脚本库  结束 -->

	<!-- 数据字典 -->
		<!-- 班次时间定义 -->
	<jteap:dict catalog="SSZY"></jteap:dict>
		<%
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
			String nowYear = dateFormat.format(new Date());

			DictManager dictManager = (DictManager)SpringContextUtil.getBean("dictManager");
			List<Dict> dictList = (List<Dict>)dictManager.findDictByUniqueCatalogName("SSZY");
		 %>
		
		<table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%">
			<tr>
				<td width="100%" align="center" valign="top">
					<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
						<tr>
							<td height="20px" style="font-size: 12px;">
								<!-- 工具条 -->
								<div style="background-color: EEF6FB; border: 1px solid #009ee8;">
									&nbsp;&nbsp;&nbsp; 专业：
									<select id="comZy">
										<%
											for(Dict dict : dictList){
										%>
											<option value="<%=dict.getValue() %>"><%=dict.getKey() %></option>	
										<% 	
											}
										 %>
									</select>
									&nbsp;&nbsp;&nbsp; 年份：
									<input id="dtTime" type="text" onClick="WdatePicker({el:'dtTime',readOnly:true,isShowToday:false,isShowClear:true,dateFmt:'yyyy'})"
										style="width:50" readonly="readonly" value=<%=nowYear %> onchange="jhnfChange()">
									<img onclick="WdatePicker({el:'dtTime',readOnly:true,isShowToday:false,isShowClear:true,dateFmt:'yyyy'})"
										src="${contextPath}/component/My97DatePicker/skin/datePicker.gif"
										width="16" height="22" align="absmiddle">
									&nbsp;&nbsp;&nbsp;
									<input id="btnQuery" type="button" value="查询" onclick="query()">
									&nbsp;
									<input id="btnBj" type="button" value="标记" onclick="biaoji()">
									&nbsp;
									<input id="btnJl" type="button" value="记录" onclick="jilu()">
									&nbsp;
									<input id="btnSave" type="button" value="保存" onclick="save()">
									&nbsp;
									<input id="btnExport" type="button" value="导出" onclick="exportData()">
									(导出前请确保数据已保存)
								</div>
								<!--  -->
							</td>
						</tr>
						<tr> 
							<td align="left" height="100%"> 
		
								<table align="left" border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
									<tr>
										<td>&nbsp;</td>
										<td valign="top">
											<!--报表开始-->
											<table width="100%" border="0" cellspacing="1" bgcolor="#e1e1e1" height="100%">
												<tr style="color:blue;" height="40">
													<td style="background:#efefef;" align="center" valign="middle" nowrap bgcolor="#efefef" width="22%">&nbsp;设备名称&nbsp;</td>
													<td bgcolor="#FFFFFF">
														<table border="0" cellpadding="0" cellspacing="0" width="100%">
															<tr>
																<td>		
																	<iframe id="topIF" src="top.jsp" width="100%"  height="40" frameborder=0 scrolling="no">&nbsp;</iframe>
																</td> 
																<td width="19">&nbsp;</td>
															</tr>
														</table>	
													</td>
												</tr>
												<tr>
													<td bgcolor="#FFFFFF" valign="top">
														<iframe id="leftIF" height="100%" src="left.jsp?sszy=gl&year=<%=nowYear%>" width="100%" frameborder=0 scrolling="no"></iframe>
													</td>
													<td bgcolor="#FFFFFF">
														<iframe id="mainIF" height="100%" src="" width="100%" frameborder=0 scrolling="auto" ></iframe>
													</td>
												</tr>
											</table>					
											<!--报表结束-->
										</td>
										<td></td> 
									</tr>
								</table>
							</td>  
						</tr>
					</table>
				</td> 
			</tr>
		</table>
		
		<script type="text/javascript">
			//初始化 top,main iframe
			function initIF(){
				//设置 leftIF参数
				window.document.getElementById("leftIF").src = "left.jsp?sszy="+comZy.options[comZy.selectedIndex].value+"&year="+dtTime.value;
		    	//设置 mainIF参数
				window.document.getElementById("mainIF").src = "main.jsp?sszy="+comZy.options[comZy.selectedIndex].value+"&year="+dtTime.value;
			}
			initIF();
			
			// 计划年份改变时
			function jhnfChange() {
				if (dtTime.value != <%=nowYear%>) {
					btnBj.disabled = true;
					btnJl.disabled = true;
					btnSave.disabled = true;
					btnExport.disabled = true;
				} else {
					btnBj.disabled = false;
					btnJl.disabled = false;
					btnSave.disabled = false;
					btnExport.disabled = false;
				}
			}
			
			//查询按钮
			function query(){
				if (dtTime.value > <%=nowYear%>) {
					alert("无今年以后的计划，无法查询");
					return;
				}
				initIF();
			}
			
			var mask = null;
			var timer = null;
			
			//保存动作
			function doSave(){
				window.clearInterval(timer);
				mainIF.save(mask);
			}
			
			//保存
			function save(){
				mask = showExtMask("正在保存,请稍候...");
				timer = window.setInterval("doSave()",100);
			}
			
			// 标记
			function biaoji() {
				mainIF.biaoji();
			}
			
			// 记录
			function jilu() {
				mainIF.jilu();
			}
			
			//导出动作
			function doExportData(){
				window.clearInterval(timer);
				mainIF.exportData(mask);
			}
			
			//导出
			function exportData(){
				mask = showExtMask("正在导出,请稍候...");
				timer = window.setInterval("doExportData()",100);
			}
	   	</script>

	</body>
</html>
