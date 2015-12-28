<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE   html   PUBLIC   "-//W3C//DTD   XHTML   1.0   Transitional//EN "   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd ">
<%@ include file="/inc/import.jsp"%>
<html>
	<head>
		<%@ include file="/inc/meta.jsp"%>
		<%@ include file="indexScript.jsp" %>
		<title>JTEAP 2.0</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
		<%@ include file="/inc/ext-all.jsp" %>
		<script type="text/javascript" src="${contextPath}/script/common.js"></script>
		<script type="text/javascript" src="${contextPath}/script/prototype.js"></script>
		<link rel="stylesheet" href="index.css" type="text/css"></link>	
		<link rel="stylesheet" type="text/css" href="${contextPath}/script/ext-extend/form/Datetime/datetime.css"></link>
		<link rel=stylesheet type = "text/css" href=css/style.css>
		<script type="text/javascript" src="${contextPath}/script/date.js"></script>
	  	<script language="javascript" type="text/javascript" src="${contextPath}/component/My97DatePicker/WdatePicker.js"></script>
		<script>
			//var oJson = window.dialogArguments;
			var bblx = "<%=request.getParameter("bblx")%>";
			function closeExcelForm(){
				TANGER_OCX_OnDocumentClosed();
				//window.close();
			}
		
		</script>
		<script type="text/javascript">
			function getCookie(name){
			    var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
			    if(arr != null) return unescape(arr[2]);
			        return null;
			}
			function setButValue(str){
				document.getElementById("but").value = str;
			}
		</script>
		<script type="text/javascript" src="script/bbzzForm.js"></script>
		<style>
		</style>
		<style>
			.bottomDiv{
				text-align: right;
				padding: 10 10 10 10;
			}
		</style>
		
	</head>

	<body onload="onload();">
		<div id="mainBar" style="margin:2px 2px 2px 2px;height:30px;padding:10 10 10 10;background-color:#c3daf9;border: solid 1px #385F5E;width:100%">
			<input type='text' name='curDate' id='curDate' onchange="" class='Wdate' onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-M-dd',readOnly:true})">
   			<input type="button" value="查询" onclick="reFindData();" class="pop-but01">
			<input type="button" value="上一天" onclick="submitTopDay()" class="pop-but01">
			<input type="button" value="下一天" onclick="submitNextDay();" class="pop-but01">
		</div>
		<div align="center">
			<table border=0 cellpadding=0 cellspacing=0 width=95% height=90% style='border-collapse:
			 collapse;table-layout:fixed;width:95%;vertical-align:center'  >
			<col class=xl68 width=107 style='mso-width-source:userset;mso-width-alt:3424;
			 width:80pt'>
			 <col class=xl68 width=82 style='mso-width-source:userset;mso-width-alt:2624;
			 width:62pt'>
			 <col class=xl68 width=80 style='mso-width-source:userset;mso-width-alt:2560;
			 width:60pt'>
			 <col class=xl68 width=82 style='mso-width-source:userset;mso-width-alt:2624;
			 width:62pt'>
			 <col class=xl68 width=95 style='mso-width-source:userset;mso-width-alt:3040;
			 width:71pt'>
			 <col class=xl68 width=79 style='mso-width-source:userset;mso-width-alt:2528;
			 width:59pt'>
			 <col class=xl68 width=75 style='mso-width-source:userset;mso-width-alt:2400;
			 width:56pt'>
			 <col class=xl68 width=65 style='mso-width-source:userset;mso-width-alt:2080;
			 width:49pt'>
			 <col class=xl68 width=66 style='mso-width-source:userset;mso-width-alt:2112;
			 width:50pt'>
			 <col class=xl68 width=91 style='mso-width-source:userset;mso-width-alt:2912;
			 width:68pt'>
			 <col class=xl68 width=88 style='mso-width-source:userset;mso-width-alt:2816;
			 width:66pt'>
			 <col class=xl68 width=53 style='mso-width-source:userset;mso-width-alt:1696;
			 width:40pt'>
			 <col class=xl68 width=59 style='mso-width-source:userset;mso-width-alt:1888;
			 width:44pt'>
			 <col class=xl68 width=60 style='mso-width-source:userset;mso-width-alt:1920;
			 width:45pt'>
			 <tr height=58 style='mso-height-source:userset;height:43.5pt'>
			  <td colspan=14 height=58 class=xl115 width=1082 style='height:43.5pt;
			  width:812pt'>湖<font class="font7">北</font><font class="font7">能</font><font
			  class="font7">源</font><font class="font7">集</font><font class="font7">团</font><font
			  class="font7">鄂</font><font class="font7">州</font><font class="font7">发</font><font
			  class="font7">电</font><font class="font7">有</font><font class="font7">限</font><font
			  class="font7">公</font><font class="font7">司</font><font class="font7">技</font><font
			  class="font7">术</font><font class="font7">经</font><font class="font7">济</font><font
			  class="font7">指</font><font class="font7">标</font><font class="font7">日</font><font
			  class="font7">报</font></td>
			 </tr>
			 <tr height=42 style='height:31.5pt'>
			  <td height=42 colspan=9 class=xl72 style='height:31.5pt;mso-ignore:colspan'></td>
			  <td class=xl73></td>
			  <td colspan=4 class=xl114><center id='RQ.NY'></center></td>
			 </tr>
			 <tr height=19 style='height:14.25pt'>
			  <td height=19 class=xl74 style='height:14.25pt'>年发电量计划:</td>
			  <td colspan=2 class=xl116 style='border-right:.5pt solid black;border-left:
			  none'><center id='EQJHSJ.NGE'></center></td>
			  <td class=xl75 style='border-left:none'>万千瓦时</td>
			  <td colspan=2 class=xl80 style='border-right:.5pt solid black;border-left:
			  none'>截止本日年发电量累计:</td>
			  <td colspan=2 class=xl118 style='border-right:.5pt solid black;border-left:
			  none'><center id='ZHCX.NGE'></center></td>
			  <td colspan=2 class=xl80 style='border-right:.5pt solid black;border-left:
			  none'>万千瓦时</td>
			  <td colspan=2 class=xl80 style='border-right:.5pt solid black;border-left:
			  none'>完成年发电量计划：</td>
			  <td colspan=2 class=xl120 style='border-right:.5pt solid black;border-left:
			  none'><center id='ZHCX.NGESCALE'></center></td>
			 </tr>
			 <tr height=19 style='height:14.25pt'>
			  <td height=19 class=xl74 style='height:14.25pt;border-top:none'>年上网电量计划:</td>
			  <td colspan=2 class=xl116 style='border-right:.5pt solid black;border-left:
			  none'><center id='EQJHSJ.NSE'></center></td>
			  <td class=xl75 style='border-top:none;border-left:none'>万千瓦时</td>
			  <td colspan=2 class=xl80 style='border-right:.5pt solid black;border-left:
			  none'>截止本日年上网电量累计:</td>
			  <td colspan=2 class=xl118 style='border-right:.5pt solid black;border-left:
			  none'><center id='ZHCX.NSE'></center></td>
			  <td colspan=2 class=xl80 style='border-right:.5pt solid black;border-left:
			  none'>万千瓦时</td>
			  <td colspan=2 class=xl80 style='border-right:.5pt solid black;border-left:
			  none'>完成年上网电量计划：</td>
			  <td colspan=2 class=xl120 style='border-right:.5pt solid black;border-left:
			  none'><center id='ZHCX.NSESCALE'></center></td>
			 </tr>
			 <tr height=19 style='height:14.25pt'>
			  <td height=19 class=xl74 style='height:14.25pt;border-top:none'>月发电量计划:</td>
			  <td colspan=2 class=xl116 style='border-right:.5pt solid black;border-left:
			  none'><center id='EQJHSJ.YGE'></center></td>
			  <td class=xl75 style='border-top:none;border-left:none'>万千瓦时</td>
			  <td colspan=2 class=xl80 style='border-right:.5pt solid black;border-left:
			  none'>截止本日月发电量累计:</td>
			  <td colspan=2 class=xl118 style='border-right:.5pt solid black;border-left:
			  none'><center id='ZHCX.YGE'></center></td>
			  <td colspan=2 class=xl80 style='border-right:.5pt solid black;border-left:
			  none'>万千瓦时</td>
			  <td colspan=2 class=xl80 style='border-right:.5pt solid black;border-left:
			  none'>完成月发电计划：</td>
			  <td colspan=2 class=xl120 style='border-right:.5pt solid black;border-left:
			  none'><center id='ZHCX.YGESCALE'></center></td>
			 </tr>
			 <tr class=xl68 height=3 style='mso-height-source:userset;height:2.25pt'>
			  <td height=3 class=xl90 style='height:2.25pt;border-top:none'>　</td>
			  <td class=xl91 style='border-top:none'>　</td>
			  <td class=xl92 style='border-top:none'>　</td>
			  <td class=xl92 style='border-top:none'>　</td>
			  <td class=xl93 style='border-top:none'>　</td>
			  <td class=xl93 style='border-top:none'>　</td>
			  <td class=xl93 style='border-top:none'>　</td>
			  <td class=xl91 style='border-top:none'>　</td>
			  <td class=xl94 style='border-top:none'>　</td>
			  <td class=xl94 style='border-top:none'>　</td>
			  <td class=xl95 style='border-top:none'>　</td>
			  <td class=xl95 style='border-top:none'>　</td>
			  <td class=xl94 style='border-top:none'>　</td>
			  <td class=xl94 style='border-top:none'>　</td>
			 </tr>
			 <tr height=78 style='mso-height-source:userset;height:58.5pt'>
			  <td height=78 class=xl78 style='height:58.5pt;border-top:none'>项<font
			  class="font12"><span style='mso-spacerun:yes'>&nbsp; </span></font><font
			  class="font13">目</font></td>
			  <td colspan=3 class=xl82 style='border-right:.5pt solid black;border-left:
			  none'>发电量（万千瓦时）</td>
			  <td class=xl79 width=95 style='border-top:none;border-left:none;width:71pt'>上网电量<br>
			    （万千瓦时）</td>
			  <td colspan=3 class=xl85 width=219 style='border-right:.5pt solid black;
			  border-left:none;width:164pt'>生产厂用电量（万千瓦时）</td>
			  <td class=xl76 align=center width=66 style='border-top:none;border-left:none;
			  width:50pt'>生产厂用电率（%）</td>
			  <td class=xl76 align=center width=91 style='border-top:none;border-left:none;
			  width:68pt'>综合厂用电量（万千瓦时）</td>
			  <td colspan=2 class=xl88 width=141 style='border-right:.5pt solid black;
			  border-left:none;width:106pt'><span
			  style='mso-spacerun:yes'>&nbsp;</span>二期启备变电量（万千瓦时）已包含在上网电量中</td>
			  <td colspan=2 class=xl82 style='border-right:.5pt solid black;border-left:
			  none'>综合厂用电率（<font class="font12">%</font><font class="font13">）</font></td>
			 </tr>
			 <tr height=19 style='mso-height-source:userset;height:14.25pt'>
			  <td height=19 class=xl78 style='height:14.25pt;border-top:none'>　</td>
			  <td class=xl78 style='border-top:none;border-left:none'>二期</td>
			  <td class=xl75 style='border-top:none;border-left:none'>#3机组</td>
			  <td class=xl75 style='border-top:none;border-left:none'>#4机组</td>
			  <td class=xl78 style='border-top:none;border-left:none'>二期</td>
			  <td class=xl78 style='border-top:none;border-left:none'>二期</td>
			  <td class=xl75 style='border-top:none;border-left:none'>#3机组</td>
			  <td class=xl75 style='border-top:none;border-left:none'>#4机组</td>
			  <td colspan=2 class=xl85 width=157 style='border-right:.5pt solid black;
			  border-left:none;width:118pt'>　</td>
			  <td colspan=2 class=xl88 width=141 style='border-right:.5pt solid black;
			  border-left:none;width:106pt'>　</td>
			  <td colspan=2 class=xl82 style='border-right:.5pt solid black;border-left:
			  none'>　</td>
			 </tr>
			 <tr height=19 style='height:14.25pt'>
			  <td height=19 class=xl78 style='height:14.25pt;border-top:none'>本日</td>
			  <td class=xl104 style='border-top:none;border-left:none'><center id='EQRSJ.GE'></center> </td>
			  <td class=xl104 style='border-top:none;border-left:none'><center id='U3JZSJ.GE'></center> </td>
			  <td class=xl104 style='border-top:none;border-left:none'><center id='U4JZSJ.GE'></center> </td>
			  <td class=xl104 style='border-top:none;border-left:none'><center id='EQRSJ.SE'></center> </td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='EQRSCE.SCE'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U3RSCE.SCE'></center></td>
			  <td class=xl106 style='border-top:none;border-left:none'><center id='U4RSCE.SCE'></center></td>
			  <td class=xl106 style='border-top:none;border-left:none'><center id='EQRSCE.SCER'></center></td>
			  <td class=xl104 style='border-top:none;border-left:none'><center id='EQRSJ.SCE'></center> </td>
			  <td colspan=2 class=xl124 style='border-right:.5pt solid black;border-left:
			  none'><center id='EQRQBB.QBB'></center> </td>
			  <td colspan=2 class=xl122 style='border-right:.5pt solid black;border-left:
			  none'><center id='EQRSJ.SCER'></center></td>
			 </tr>
			 <tr height=19 style='height:14.25pt'>
			  <td height=19 class=xl78 style='height:14.25pt;border-top:none'>本月累计</td>
			  <td class=xl104 style='border-top:none;border-left:none'><center id='EQYSJ.GE'></center> </td>
			  <td class=xl104 style='border-top:none;border-left:none'><center id='U3JZYSJ.GE'></center></td>
			  <td class=xl104 style='border-top:none;border-left:none'><center id='U4JZYSJ.GE'></center> </td>
			  <td class=xl104 style='border-top:none;border-left:none'><center id='EQYSJ.SE'></center> </td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='EQYSCE.SCE'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U3YSCE.SCE'></center></td>
			  <td class=xl106 style='border-top:none;border-left:none'><center id='U4YSCE.SCE'></center></td>
			  <td class=xl106 style='border-top:none;border-left:none'><center id='EQYSCE.SCER'></center></td>
			  <td class=xl104 style='border-top:none;border-left:none'><center id='EQYSJ.SCE'></center> </td>
			  <td colspan=2 class=xl124 style='border-right:.5pt solid black;border-left:
			  none'><center id='EQNQBB.QBB'></center></td>
			  <td colspan=2 class=xl122 style='border-right:.5pt solid black;border-left:
			  none'><center id='EQYSJ.SCER'></center></td>
			 </tr>
			 <tr height=32 style='mso-height-source:userset;height:24.0pt'>
			  <td height=32 class=xl78 style='height:24.0pt;border-top:none'>本年累计</td>
			  <td class=xl104 style='border-top:none;border-left:none'><center id='EQNSJ.GE'></center> </td>
			  <td class=xl104 style='border-top:none;border-left:none'><center id='U3JZNSJ.GE'></center> </td>
			  <td class=xl104 style='border-top:none;border-left:none'><center id='U4JZNSJ.GE'></center> </td>
			  <td class=xl104 style='border-top:none;border-left:none'><center id='EQNSJ.SE'></center>  </td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='EQNSCE.SCE'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U3NSCE.SCE'></center></td>
			  <td class=xl106 style='border-top:none;border-left:none'><center id='U4NSCE.SCE'></center></td>
			  <td class=xl106 style='border-top:none;border-left:none'><center id='EQNSCE.SCER'></center></td>
			  <td class=xl104 style='border-top:none;border-left:none'><center id='ZHCX.ZHYDL'></center> </td>
			  <td colspan=2 class=xl124 style='border-right:.5pt solid black;border-left:
			  none'><center id='EQNQBBS.QBB'></center> </td>
			  <td colspan=2 class=xl122 style='border-right:.5pt solid black;border-left:
			  none'><center id='EQNSJ.SCER'></center></td>
			 </tr>
			 <tr class=xl68 height=2 style='mso-height-source:userset;height:1.5pt'>
			  <td height=2 class=xl82 style='height:1.5pt;border-top:none'>　</td>
			  <td class=xl96 style='border-top:none'>　</td>
			  <td class=xl96 style='border-top:none'>　</td>
			  <td class=xl96 style='border-top:none'>　</td>
			  <td class=xl96 style='border-top:none'>　</td>
			  <td class=xl83 style='border-top:none'>　</td>
			  <td class=xl83 style='border-top:none'>　</td>
			  <td class=xl101 style='border-top:none'>　</td>
			  <td class=xl101 style='border-top:none'>　</td>
			  <td class=xl96 style='border-top:none'>　</td>
			  <td class=xl102 style='border-top:none'>　</td>
			  <td class=xl103 style='border-top:none'>　</td>
			  <td class=xl103 style='border-top:none'>　</td>
			  <td class=xl103 style='border-top:none'>　</td>
			 </tr>
			 <tr height=30 style='height:22.5pt'>
			  <td height=30 class=xl78 style='height:22.5pt;border-top:none'>指标名称</td>
			  <td colspan=3 class=xl82 style='border-right:.5pt solid black;border-left:
			  none'>耗原煤量T</td>
			  <td colspan=3 class=xl85 width=249 style='border-right:.5pt solid black;
			  border-left:none;width:186pt'>折标煤量T</td>
			  <td colspan=3 class=xl82 style='border-right:.5pt solid black;border-left:
			  none'>煤折标煤T</td>
			  <td class=xl112 width=88 style='border-top:none;border-left:none;width:66pt'>入炉煤低位热值（MJ/KG）</td>
			  <td colspan=3 class=xl85 width=172 style='border-right:.5pt solid black;
			  border-left:none;width:129pt'>耗油量T</td>
			 </tr>
			 <tr height=19 style='height:14.25pt'>
			  <td height=19 class=xl78 style='height:14.25pt;border-top:none'>　</td>
			  <td class=xl78 style='border-top:none;border-left:none'>二期</td>
			  <td class=xl75 style='border-top:none;border-left:none'>#3机组</td>
			  <td class=xl75 style='border-top:none;border-left:none'>#4机组</td>
			  <td class=xl78 style='border-top:none;border-left:none'>二期</td>
			  <td class=xl75 style='border-top:none;border-left:none'>#3机组</td>
			  <td class=xl75 style='border-top:none;border-left:none'>#4机组</td>
			  <td class=xl78 style='border-top:none;border-left:none'>二期</td>
			  <td class=xl75 style='border-top:none;border-left:none'>#3机组</td>
			  <td class=xl75 style='border-top:none;border-left:none'>#4机组</td>
			  <td class=xl78 style='border-top:none;border-left:none'>二期</td>
			  <td class=xl78 style='border-top:none;border-left:none'>二期</td>
			  <td class=xl75 style='border-top:none;border-left:none'>#3机组</td>
			  <td class=xl75 style='border-top:none;border-left:none'>#4机组</td>
			 </tr>
			 <tr height=19 style='height:14.25pt'>
			  <td height=19 class=xl78 style='height:14.25pt;border-top:none'>本日</td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='EQRSJ.CC'></center> </td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='U3JZSJ.CC'></center> </td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='U4JZSJ.CC'></center> </td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='EQRSJ.SC'></center></td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='U3JZSJ.SC'></center> </td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='U4JZSJ.SC'></center> </td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='EQRSJ.CSC'></center> </td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='U3JZSJ.CSC'></center> </td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='U4JZSJ.CSC'></center> </td>
			  <td class=xl108 style='border-top:none;border-left:none'><center id='EQRSJ.LHV'></center> </td>
			  <td class=xl109 style='border-top:none;border-left:none'><center id='EQRSJ.CO'></center></td>
			  <td class=xl109 style='border-top:none;border-left:none'><center id='U3JZSJ.CO'></center></td>
			  <td class=xl109 style='border-top:none;border-left:none'><center id='U4JZSJ.CO'></center></td>
			 </tr>
			 <tr height=19 style='height:14.25pt'>
			  <td height=19 class=xl78 style='height:14.25pt;border-top:none'>本月累计</td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='EQYSJ.CC'></center> </td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='U3JZYSJ.CC'></center> </td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='U4JZYSJ.CC'></center> </td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='EQYSJ.SC'></center> </td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='U3JZYSJ.SC'></center> </td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='U4JZYSJ.SC'></center> </td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='EQYSJ.CSC'></center> </td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='U3JZYSJ.CSC'></center> </td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='U4JZYSJ.CSC'></center> </td>
			  <td class=xl108 style='border-top:none;border-left:none'><center id='EQYRLMRZ.LHV'></center> </td>
			  <td class=xl109 style='border-top:none;border-left:none'><center id='EQYSJ.CO'></center></td>
			  <td class=xl109 style='border-top:none;border-left:none'><center id='U3JZYSJ.CO'></center></td>
			  <td class=xl109 style='border-top:none;border-left:none'><center id='U4JZYSJ.CO'></center></td>
			 </tr>
			 <tr height=28 style='mso-height-source:userset;height:21.0pt'>
			  <td height=28 class=xl78 style='height:21.0pt;border-top:none'>本年累计</td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='EQNSJ.CC'></center> </td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='U3JZNSJ.CC'></center> </td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='U4JZNSJ.CC'></center> </td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='EQNSJ.SC'></center> </td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='U3JZNSJ.SC'></center> </td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='U4JZNSJ.SC'></center>  </td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='EQNSJ.CSC'></center>  </td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='U3JZNSJ.CSC'></center>  </td>
			  <td class=xl107 style='border-top:none;border-left:none'><center id='U4JZNSJ.CSC'></center> </td>
			  <td class=xl108 style='border-top:none;border-left:none'><center id='EQNRLMRZ.LHV'></center> </td>
			  <td class=xl109 style='border-top:none;border-left:none'><center id='EQNSJ.CO'></center></td>
			  <td class=xl109 style='border-top:none;border-left:none'><center id='U3JZNSJ.CO'></center></td>
			  <td class=xl109 style='border-top:none;border-left:none'><center id='U4JZNSJ.CO'></center></td>
			 </tr>
			 <tr class=xl68 height=3 style='mso-height-source:userset;height:2.25pt'>
			  <td height=3 class=xl82 style='height:2.25pt;border-top:none'>　</td>
			  <td class=xl97 style='border-top:none'>　</td>
			  <td class=xl97 style='border-top:none'>　</td>
			  <td class=xl97 style='border-top:none'>　</td>
			  <td class=xl97 style='border-top:none'>　</td>
			  <td class=xl97 style='border-top:none'>　</td>
			  <td class=xl97 style='border-top:none'>　</td>
			  <td class=xl97 style='border-top:none'>　</td>
			  <td class=xl97 style='border-top:none'>　</td>
			  <td class=xl97 style='border-top:none'>　</td>
			  <td class=xl100 style='border-top:none'>　</td>
			  <td class=xl91 style='border-top:none'>　</td>
			  <td class=xl91 style='border-top:none'>　</td>
			  <td class=xl91 style='border-top:none'>　</td>
			 </tr>
			 <tr height=38 style='mso-height-source:userset;height:28.5pt'>
			  <td height=38 class=xl78 style='height:28.5pt;border-top:none'>指标名称</td>
			  <td colspan=3 class=xl82 style='border-right:.5pt solid black;border-left:
			  none'>发电煤耗（克/千瓦时）</td>
			  <td class=xl79 width=95 style='border-top:none;border-left:none;width:71pt'>供电煤耗<br>
			    （克/千瓦时）</td>
			  <td colspan=3 class=xl82 style='border-right:.5pt solid black;border-left:
			  none'>运行小时H</td>
			  <td colspan=3 class=xl82 style='border-right:.5pt solid black;border-left:
			  none'>平均负荷（万千瓦）</td>
			  <td colspan=3 class=xl82 style='border-right:.5pt solid black;border-left:
			  none'>负荷率%</td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl78 style='height:13.5pt;border-top:none'>　</td>
			  <td class=xl78 style='border-top:none;border-left:none'>二期</td>
			  <td class=xl75 style='border-top:none;border-left:none'>#3机组</td>
			  <td class=xl75 style='border-top:none;border-left:none'>#4机组</td>
			  <td class=xl78 style='border-top:none;border-left:none'>二期</td>
			  <td class=xl78 style='border-top:none;border-left:none'>二期</td>
			  <td class=xl75 style='border-top:none;border-left:none'>#3机组</td>
			  <td class=xl75 style='border-top:none;border-left:none'>#4机组</td>
			  <td class=xl78 style='border-top:none;border-left:none'>二期</td>
			  <td class=xl75 style='border-top:none;border-left:none'>#3机组</td>
			  <td class=xl75 style='border-top:none;border-left:none'>#4机组</td>
			  <td class=xl78 style='border-top:none;border-left:none'>二期</td>
			  <td class=xl75 style='border-top:none;border-left:none'>#3机组</td>
			  <td class=xl75 style='border-top:none;border-left:none'>#4机组</td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl78 style='height:13.5pt;border-top:none'>本日</td>
			  <td class=xl110 style='border-top:none;border-left:none'><center id='EQRSJ.GCR'></center> </td>
			  <td class=xl110 style='border-top:none;border-left:none'><center id='U3JZSJ.GCR'></center> </td>
			  <td class=xl110 style='border-top:none;border-left:none'><center id='U4JZSJ.GCR'></center> </td>
			  <td class=xl111 style='border-top:none;border-left:none'><center id='EQRSJ.SCR'></center> </td>
			  <td class=xl106 style='border-top:none;border-left:none'><center id='ZHCXYXXS.RSH'></center></td>
			  <td class=xl106 style='border-top:none;border-left:none'><center id='U3JZSJ.SH'></center></td>
			  <td class=xl106 style='border-top:none;border-left:none'><center id='U4JZSJ.SH'></center></td>
			  <td class=xl106 style='border-top:none;border-left:none'><center id='EQRSJ.OAL'></center></td>
			  <td class=xl106 style='border-top:none;border-left:none'><center id='U3JZSJ.OAL'></center></td>
			  <td class=xl106 style='border-top:none;border-left:none'><center id='U4JZSJ.OAL'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='EQRGEV.GEV'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U3RGEV.GEV'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U4RGEV.GEV'></center></td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl78 style='height:13.5pt;border-top:none'>本月累计</td>
			  <td class=xl110 style='border-top:none;border-left:none'><center id='EQYSJ.GCR'></center> </td>
			  <td class=xl110 style='border-top:none;border-left:none'><center id='U3JZYSJ.GCR'></center> </td>
			  <td class=xl110 style='border-top:none;border-left:none'><center id='U4JZYSJ.GCR'></center> </td>
			  <td class=xl111 style='border-top:none;border-left:none'><center id='EQYSJ.SCR'></center> </td>
			  <td class=xl106 style='border-top:none;border-left:none'><center id='ZHCXYXXS.YSH'></center></td>
			  <td class=xl106 style='border-top:none;border-left:none'><center id='U3JZYSJ.SH'></center></td>
			  <td class=xl106 style='border-top:none;border-left:none'><center id='U4JZYSJ.SH'></center></td>
			  <td class=xl106 style='border-top:none;border-left:none'><center id='EQYSJ.OAL'></center></td>
			  <td class=xl106 style='border-top:none;border-left:none'><center id='U3JZYSJ.OAL'></center></td>
			  <td class=xl106 style='border-top:none;border-left:none'><center id='U4JZYSJ.OAL'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='EQYGEV.GEV'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U3YGEV.GEV'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U4GEV.GEV'></center></td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl78 style='height:13.5pt;border-top:none'>本年累计</td>
			  <td class=xl110 style='border-top:none;border-left:none'><center id='EQNSJ.GCR'></center> </td>
			  <td class=xl110 style='border-top:none;border-left:none'><center id='U3JZNSJ.GCR'></center> </td>
			  <td class=xl110 style='border-top:none;border-left:none'><center id='U4JZNSJ.GCR'></center>  </td>
			  <td class=xl111 style='border-top:none;border-left:none'><center id='EQNSJ.SCR'></center> </td>
			  <td class=xl106 style='border-top:none;border-left:none'><center id='ZHCXYXXS.NSH'></center></td>
			  <td class=xl106 style='border-top:none;border-left:none'><center id='U3JZNSJ.SH'></center></td>
			  <td class=xl106 style='border-top:none;border-left:none'><center id='U4JZNSJ.SH'></center></td>
			  <td class=xl106 style='border-top:none;border-left:none'><center id='EQNSJ.OAL'></center></td>
			  <td class=xl106 style='border-top:none;border-left:none'><center id='U3JZNSJ.OAL'></center></td>
			  <td class=xl106 style='border-top:none;border-left:none'><center id='U4JZNSJ.OAL'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='EQNGEV.GEV'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U3NGEV.GEV'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U4NGEV.GEV'></center></td>
			 </tr>
			 <tr class=xl68 height=2 style='mso-height-source:userset;height:1.5pt'>
			  <td height=2 class=xl82 style='height:1.5pt;border-top:none'>　</td>
			  <td class=xl98 style='border-top:none'>　</td>
			  <td class=xl98 style='border-top:none'>　</td>
			  <td class=xl98 style='border-top:none'>　</td>
			  <td class=xl99 style='border-top:none'>　</td>
			  <td class=xl101 style='border-top:none'>　</td>
			  <td class=xl101 style='border-top:none'>　</td>
			  <td class=xl101 style='border-top:none'>　</td>
			  <td class=xl101 style='border-top:none'>　</td>
			  <td class=xl101 style='border-top:none'>　</td>
			  <td class=xl101 style='border-top:none'>　</td>
			  <td class=xl83 style='border-top:none'>　</td>
			  <td class=xl83 style='border-top:none'>　</td>
			  <td class=xl83 style='border-top:none'>　</td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl78 style='height:13.5pt;border-top:none'>项目</td>
			  <td class=xl77 align=center style='border-top:none;border-left:none'>主汽压力</td>
			  <td class=xl77 align=center style='border-top:none;border-left:none'>主汽温度</td>
			  <td class=xl77 align=center style='border-top:none;border-left:none'>再热汽温</td>
			  <td class=xl77 align=center style='border-top:none;border-left:none'>排烟温度</td>
			  <td class=xl76 align=center width=79 style='border-top:none;border-left:none;
			  width:59pt'>凝汽器真空</td>
			  <td class=xl76 align=center width=75 style='border-top:none;border-left:none;
			  width:56pt'>烟气含氧量</td>
			  <td class=xl77 align=center style='border-top:none;border-left:none'>炉膛压力</td>
			  <td class=xl77 align=center style='border-top:none;border-left:none'>给水温度</td>
			  <td class=xl77 align=center style='border-top:none;border-left:none'>排气温度</td>
			  <td class=xl77 align=center style='border-top:none;border-left:none'>指标名称</td>
			  <td colspan=3 class=xl82 style='border-right:.5pt solid black;border-left:
			  none'>最高负荷（万千瓦）</td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl78 style='height:13.5pt;border-top:none'>　</td>
			  <td class=xl77 style='border-top:none;border-left:none'>　</td>
			  <td class=xl77 style='border-top:none;border-left:none'>　</td>
			  <td class=xl77 style='border-top:none;border-left:none'>　</td>
			  <td class=xl77 style='border-top:none;border-left:none'>　</td>
			  <td class=xl76 width=79 style='border-top:none;border-left:none;width:59pt'>　</td>
			  <td class=xl76 width=75 style='border-top:none;border-left:none;width:56pt'>　</td>
			  <td class=xl77 style='border-top:none;border-left:none'>　</td>
			  <td class=xl78 style='border-top:none;border-left:none'></td>
			  <td class=xl75 style='border-top:none;border-left:none'></td>
			  <td class=xl75 style='border-top:none;border-left:none'></td>
			  <td class=xl78 style='border-top:none;border-left:none'>二期</td>
			  <td class=xl75 style='border-top:none;border-left:none'>#3机组</td>
			  <td class=xl75 style='border-top:none;border-left:none'>#4机组</td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl78 style='height:13.5pt;border-top:none'>　</td>
			  <td class=xl105 style='border-top:none;border-left:none'>Mpa</td>
			  <td class=xl105 style='border-top:none;border-left:none'>℃</td>
			  <td class=xl105 style='border-top:none;border-left:none'>℃</td>
			  <td class=xl105 style='border-top:none;border-left:none'>℃</td>
			  <td class=xl105 style='border-top:none;border-left:none'>%</td>
			  <td class=xl105 style='border-top:none;border-left:none'>%</td>
			  <td class=xl105 style='border-top:none;border-left:none'>Mpa</td>
			  <td class=xl105 style='border-top:none;border-left:none'>℃</td>
			  <td class=xl105 style='border-top:none;border-left:none'>℃</td>
			  <td class=xl105 style='border-top:none;border-left:none'>本日</td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='EQRSJ.UML'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U3JZSJ.UML'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U4JZSJ.UML'></center></td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl78 style='height:13.5pt;border-top:none'>3号机</td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U3JZSJ.TMP'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U3JZSJ.TMT'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U3JZSJ.TRT'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U3JZSJ.FGT'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U3JZSJ.VCD'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U3JZSJ.OP'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U3JZSJ.LP'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U3JZSJ.FWT'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U3JZSJ.HPT'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'>本月累计</td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='EQYSJ.UML'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U3JZYSJ.UML'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U4JZYSJ.UML'></center></td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl78 style='height:13.5pt;border-top:none'>4号机</td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U4JZSJ.TMP'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U4JZSJ.TMT'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U4JZSJ.TRT'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U4JZSJ.FGT'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U4JZSJ.VCD'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U4JZSJ.OP'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U4JZSJ.LP'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U4JZSJ.FWT'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U4JZSJ.HPT'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'>本年累计</td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='EQNSJ.UML'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U3JZNSJ.UML'></center></td>
			  <td class=xl105 style='border-top:none;border-left:none'><center id='U4JZNSJ.UML'></center></td>
			 </tr>
			</table>
		</div>
	</body>
</html>
