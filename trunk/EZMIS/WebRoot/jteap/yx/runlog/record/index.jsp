<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.yx.runlog.manager.LogsTableInfoManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.yx.runlog.model.LogsTableInfo"%>
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
	<jteap:dict catalog="zbbc_sj_dy"></jteap:dict>
		
		<%
			LogsTableInfoManager tableInfoManager = (LogsTableInfoManager) SpringContextUtil.getBean("logsTableInfoManager");
			
			DictManager dictManager = (DictManager)SpringContextUtil.getBean("dictManager");
			List<Dict> dictList = (List<Dict>)dictManager.findDictByUniqueCatalogName("zbbc");
						
			//表编号
			String tableCode = request.getParameter("tableCode");
			//表对象
			LogsTableInfo tableInfo = tableInfoManager.findBy("tableCode",tableCode).get(0);
			if(tableInfo == null){
				out.print("<h3>运行日志配置 - 表定义被删除,请联系管理员...<h3>");
				return;
			}
			//表Id
			String tableId = tableInfo.getId();
		 %>
		
		<table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%">
			<tr>
				<td width="100%" align="center" valign="top">
					<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
						<tr>
							<td height="20px" style="font-size: 12px;">
								<!-- 工具条 -->
								<div style="background-color: EEF6FB; border: 1px solid #009ee8;">
									
									&nbsp;&nbsp;&nbsp; 时间：
									<input id="dtTime" type="text" onClick="WdatePicker()" onchange="zbsjChange()"
										style="width: 80" readonly="readonly">
									<img onclick="WdatePicker({el:'dtTime'})"
										src="${contextPath}/component/My97DatePicker/skin/datePicker.gif"
										width="16" height="22" align="absmiddle">
									&nbsp;&nbsp;&nbsp;
									<input type="button" value="查询" onclick="query()">
									&nbsp;&nbsp;&nbsp; 班次：
									<select id="comBc" onchange="zbbcChange()">
										<%
											for(Dict dict : dictList){
										%>
											<option><%=dict.getValue() %></option>	
										<% 	
											}
										 %>
										
									</select>
									&nbsp;
									<input type="button" value="上一班" onclick="shangYiBan()">
									&nbsp;
									<input type="button" value="下一班" onclick="xiaYiBan()">
									&nbsp;&nbsp;&nbsp;
									<input id="btnQushu" type="button" value="取数" onclick="getData()">
									&nbsp;
									<input id="btnSave" type="button" value="保存" onclick="save()">
									&nbsp;
									<input type="button" value="导出" onclick="exportData()">
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
												<tr style="color:blue;" height="15">
													<td style="background:#efefef;" align="center" valign="middle" nowrap bgcolor="#efefef" width="22%">&nbsp;记录项目&nbsp;</td>
													<td bgcolor="#FFFFFF">
														<table border="0" cellpadding="0" cellspacing="0" width="100%">
															<tr>
																<td>		
																	<iframe id="topIF" src="" width="100%"  height="20" frameborder=0 scrolling="no">&nbsp;</iframe>
																</td> 
																<td width="19">&nbsp;</td>
															</tr>
														</table>	
													</td>
													
												</tr>
												<tr>
													<td bgcolor="#FFFFFF" valign="top">
														<iframe id="leftIF" height="100%" src="left.jsp?tableId=<%=tableId%>" width="100%" frameborder=0 scrolling="no"></iframe>
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
			//取得班次时间数据字典
			var dict_zbbc_sj = $dictList("zbbc_sj_dy");
			
		 	//当前时间
			var nowDate = new Date();
			//当前日期
			var nowYmd = nowDate.format('Y-m-d');
			//现在 时、分、秒
			var nowHms = nowDate.format('H:i:s');
			//现在班次
			var nowBc = "";
			
			//夜班开始、结束时间
			var yeB = $dictValue("zbbc_sj_dy","ye_beginTime");
			var yeE = $dictValue("zbbc_sj_dy","ye_endTime");
			//白班开始、结束时间
			var baiB = $dictValue("zbbc_sj_dy","bai_beginTime");
			var baiE = $dictValue("zbbc_sj_dy","bai_endTime");
			//中班开始、结束时间
			var zhongB = $dictValue("zbbc_sj_dy","zhong_beginTime");
			var zhongE = $dictValue("zbbc_sj_dy","zhong_endTime");
			
			/** 交接班信息初始化 */
			if(nowHms >= yeB && nowHms <= yeE){
				nowBc = "夜班";
				comBc.selectedIndex = 0;
			}else if(nowHms >= baiB && nowHms <= baiE){
				nowBc = "白班";
				comBc.selectedIndex = 1;
			}else if(nowHms >= zhongB && nowHms <= zhongE){
				nowBc = "中班";
				comBc.selectedIndex = 2;
			}
		 	dtTime.value = nowYmd;
		</script>
		
		<script type="text/javascript">
			//初始化 top,main iframe
			function initIF(){
				//设置 topIF参数
				window.document.getElementById("topIF").src = "top.jsp?tableId=<%=tableId%>&zbbc=" + encodeURIComponent(comBc.options[comBc.selectedIndex].innerText);
		    	//设置 mainIF参数
				window.document.getElementById("mainIF").src = "main.jsp?tableId=<%=tableId%>&zbsj=" + dtTime.value + "&zbbc=" + encodeURIComponent(comBc.options[comBc.selectedIndex].innerText);
			}
			initIF();
			
			//值班时间的值改变时
			function zbsjChange(){
				var selectDate = dtTime.value;
				initIF();
			}
			//值班班次的值改变时
			function zbbcChange(){
				initIF();
			}			
			//查询按钮
			function query(){
				initIF();
			}
			
			//上一班
			function shangYiBan(){
				//所选班次
				var selectBc = comBc.options[comBc.selectedIndex].innerText;
			    //当前时间
			    var selectDate = dtTime.value;
			    //上一班时间
			    var leftTime = getDataByDay(selectDate,-1);
				
				if(selectBc == '夜班'){
			     	comBc.selectedIndex = 2;
			        dtTime.value = leftTime;
			    }else if(selectBc == '白班'){
			    	comBc.selectedIndex = 0;
			    }else if(selectBc == '中班'){
			    	comBc.selectedIndex = 1;
			    }
				initIF();
			}
			
			//下一班
			function xiaYiBan(){
				//所选班次
				var selectBc = comBc.options[comBc.selectedIndex].innerText;
			    //当前时间
			    var nowDate = dtTime.value;
			    //下一班时间
			    var rightTime = getDataByDay(nowDate,1);
			    
				if(selectBc == '夜班'){
			     	comBc.selectedIndex = 1;
			    }else if(selectBc == '白班'){
			        comBc.selectedIndex = 2;
			    }else if(selectBc == '中班'){
			        comBc.selectedIndex = 0;
			        dtTime.value = rightTime;
		    	}
				initIF();
			}
			
			var mask = null;
			var timer = null;
			
			//取数动作
			function doGetData(){
				window.clearInterval(timer);
				mainIF.getData(mask);
			}
			
			//取数
			function getData(){
				mask = showExtMask("正在取数,请稍后...");
				timer = window.setInterval("doGetData()",100);
			}
			
			//保存动作
			function doSave(){
				window.clearInterval(timer);
				mainIF.save(mask);
			}
			
			//保存
			function save(){
				var bcValue = "";
				var bcIndex = comBc.selectedIndex;
				if(bcIndex == 0){
					bcValue = "夜班";
				}else if(bcIndex == 1){
					bcValue = "白班";
				}else if(bcIndex == 2){
					bcValue = "中班";
				}
				
				mask = showExtMask("正在保存,请稍后...");
				timer = window.setInterval("doSave()",100);
			}
			
			//导出动作
			function doExportData(){
				window.clearInterval(timer);
				mainIF.exportData(mask);
			}
			//导出
			function exportData(){
				mask = showExtMask("正在导出,请稍后...");
				timer = window.setInterval("doExportData()",100);
			}
	   	</script>

	</body>
</html>
