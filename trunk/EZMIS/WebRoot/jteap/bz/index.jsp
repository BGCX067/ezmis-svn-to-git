<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp"%>
<html>
<head>
<%@ include file="/inc/meta.jsp"%>
<meta http-equiv="x-ua-compatible" content="ie=7" />
<%@ include file="indexScript.jsp"%>
<%
			String moduleId = (String) request.getParameter("moduleId");
			String bs = request.getParameter("bs");
		%>
<title>运行管理子系统首页</title>
<link rel="stylesheet" href="${contextPath}/jteap/bz/index.css" type="text/css"></link>
<%@ include file="/inc/ext-all.jsp"%>
<script type="text/javascript">
			function goToPage(moduleId,childId,classId){
				parent.window.document.getElementById("portal").src = "index.jsp";
				parent.window.document.getElementById(0).className = "";
			}
			function showJHMoudle(docid){
				var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn=TB_FORM_NGZJH&docid="+docid+"&st=02";
				showIFModule(url,"班组管理子系统","true",530,500,{},null,null,null,false,"auto");
			}
		</script>
</head>

<body>
<table width="100%" style="height: 100%" border="0" cellspacing="0"
	cellpadding="0">
	<tr>
		<td width="50%">

		<table width="100%" style="height: 100%" border="0" cellspacing="0"
			cellpadding="0">
			<tr>
				<td valign="top" style="padding:3px">
				<table width='100%' border='0' cellspacing='0' cellpadding='0'
					class='mod-TAB'  height="99%">
					<tr>
						<td class='mod-top-left'>&nbsp;</td>
						<td class='mod-top-center'>&nbsp;</td>
						<td class='mod-top-right'>&nbsp;</td>
					</tr>
					<tr>
						<td class='mod-left'>&nbsp;</td>
						<td class='mod-main'>
						<table width='100%' border='0' cellspacing='0' cellpadding='0'
							class='mod-TAB-title'>
							<tr>
								<td class='mod-collapse'>&nbsp;</td>
								<td class='mod-title'>
								${bzInfo.BZM}
								</td>
							</tr>
						</table>
						<div class="mod-content" style="height: 88%">
							${bzInfo.BZJBXX}
						</div>
						 <div class="YieldTxt3" style="padding-top:3px">
						 	◎本班安全生产天数: 
						 	<span class="YieldTxt4">
							<script>
							var aqscqsrq = "${bzInfo.AQSCQSRQ}";
							if(aqscqsrq!=''){
								aqscqsrq = aqscqsrq.split(" ")[0];
		    					var now = Ext.util.Format.date(new Date(),"Y-m-d");
		    					var  arrbeginDate,  Date1,  Date2, arrendDate,  iDays  
							    arrbeginDate=  aqscqsrq.split("-")  
							    Date1=  new  Date(arrbeginDate[1]  +  '-'  +  arrbeginDate[2]  +  '-'  +  arrbeginDate[0])    //转换为2007-8-10格式
							    arrendDate=  now.split("-")  
							    Date2=  new  Date(arrendDate[1]  +  '-'  +  arrendDate[2]  +  '-'  +  arrendDate[0])  
							    iDays  =  parseInt(Math.abs(Date1-  Date2)  /  1000  /  60  /  60  /24)    //转换为天数 
							    document.write(iDays);
							}
							</script>
						</span>天</div>								
						</td>
						<td class="mod-right">&nbsp;</td>
					</tr>
					<tr>
						<td class="mod-bottom-left">&nbsp;</td>
						<td class="mod-bottom-center">&nbsp;</td>
						<td class="mod-bottom-right">&nbsp;</td>
					</tr>
				</table>
		</td>
	</tr>
</table>
</td>
<td>
<table width="100%" style="height: 100%" border="0" cellspacing="0"
	cellpadding="0">
	<tr>
		<td style="padding:3px">
		<table width='100%' border='0' cellspacing='0' cellpadding='0'
					class='mod-TAB' height="49%">
					<tr>
						<td class='mod-top-left'>&nbsp;</td>
						<td class='mod-top-center'>&nbsp;</td>
						<td class='mod-top-right'>&nbsp;</td>
					</tr>
					<tr>
						<td class='mod-left'>&nbsp;</td>
						<td class='mod-main'>
						<table width='100%' border='0' cellspacing='0' cellpadding='0'
							class='mod-TAB-title'>
							<tr>
								<td class='mod-collapse'>&nbsp;</td>
								<td class='mod-title'>本年工作计划</td>
							</tr>
						</table>
						<div class="mod-content">
							<!-- <div id="ContentLink"></div>  -->
							<div>
								<a
									href="javascript:void(0);showJHMoudle('${jh.ID}')"
									title="${jh.ts}">${jh.JHNR}</a>
							</div>
						</div>
						</td>
						<td class="mod-right">&nbsp;</td>
					</tr>
					<tr>
						<td class="mod-bottom-left">&nbsp;</td>
						<td class="mod-bottom-center">&nbsp;</td>
						<td class="mod-bottom-right">&nbsp;</td>
					</tr>
				</table>
				<p style="LINE-HEIGHT:7px">&nbsp;</p>
				
				<table width='100%' border='0' cellspacing='0' cellpadding='0'
					class='mod-TAB' height="49%">
					<tr>
						<td class='mod-top-left'>&nbsp;</td>
						<td class='mod-top-center'>&nbsp;</td>
						<td class='mod-top-right'>&nbsp;</td>
					</tr>
					<tr>
						<td class='mod-left'>&nbsp;</td>
						<td class='mod-main'>
						<table width='100%' border='0' cellspacing='0' cellpadding='0'
							class='mod-TAB-title'>
							<tr>
								<td class='mod-collapse'>&nbsp;</td>
								<td class='mod-title'>本班亮点</td>
							</tr>
						</table>
						<div class="mod-content">
							${bzInfo.BZLD}
						</div>
						</td>
						<td class="mod-right">&nbsp;</td>
					</tr>
					<tr>
						<td class="mod-bottom-left">&nbsp;</td>
						<td class="mod-bottom-center">&nbsp;</td>
						<td class="mod-bottom-right">&nbsp;</td>
					</tr>
				</table>
		</td>
	</tr>
</table>
</td>
</tr>
</table>
</td>
</tr>
</table>
</body>
</html>
