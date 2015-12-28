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
			<table border=0 cellpadding=0 cellspacing=0 width=95% height=95% style='border-collapse:
			 collapse;table-layout:fixed;width:95%;vertical-align:center'  >
			 <col class=xl66 width=101 style='mso-width-source:userset;mso-width-alt:3232;
			 width:76pt'>
			 <col class=xl66 width=82 style='mso-width-source:userset;mso-width-alt:2624;
			 width:62pt'>
			 <col class=xl66 width=84 style='mso-width-source:userset;mso-width-alt:2688;
			 width:63pt'>
			 <col class=xl66 width=77 span=2 style='mso-width-source:userset;mso-width-alt:
			 2464;width:58pt'>
			 <col class=xl66 width=79 span=2 style='mso-width-source:userset;mso-width-alt:
			 2528;width:59pt'>
			 <col class=xl66 width=67 style='mso-width-source:userset;mso-width-alt:2144;
			 width:50pt'>
			 <col class=xl66 width=61 style='mso-width-source:userset;mso-width-alt:1952;
			 width:46pt'>
			 <col class=xl66 width=58 style='mso-width-source:userset;mso-width-alt:1856;
			 width:44pt'>
			 <col class=xl66 width=65 style='mso-width-source:userset;mso-width-alt:2080;
			 width:49pt'>
			 <col class=xl66 width=72 style='mso-width-source:userset;mso-width-alt:2304;
			 width:54pt'>
			 <col class=xl66 width=71 span=2 style='mso-width-source:userset;mso-width-alt:
			 2272;width:53pt'>
			 <col class=xl66 width=49 style='mso-width-source:userset;mso-width-alt:1568;
			 width:37pt'>
			 <col class=xl66 width=50 style='mso-width-source:userset;mso-width-alt:1600;
			 width:38pt'>
			 <col class=xl66 width=65 style='mso-width-source:userset;mso-width-alt:2080;
			 width:49pt'>
			 <tr height=42 style='height:31.5pt'>
			  <td colspan=17 height=42 class=xl127 >湖 北 能 源 集 团 鄂 州 电 厂 技 术 经 济 指 标 日 报<span
			  style='mso-spacerun:yes'>&nbsp;&nbsp;</span></td>
			 </tr>
			 <tr height=42 style='height:31.5pt'>
			  <td height=42 colspan=9 class=xl67 style='height:31.5pt;mso-ignore:colspan'></td>
			  <td class=xl69></td>
			  <td colspan=3 class=xl128><center id='RQ.NY'></center></td>
			  <td colspan=2 class=xl67 style='mso-ignore:colspan'></td>
			  <td colspan=2 class=xl68 style='mso-ignore:colspan'></td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl70 style='height:13.5pt'>年发电量计划:</td>
			  <td colspan=2 class=xl129 style='border-right:.5pt solid black;border-left:
			  none'><center id="QCJHSJ.NGE"></center></td>
			  <td class=xl71>万千瓦时</td>
			  <td colspan=2 class=xl131 style='border-right:.5pt solid black;border-left:
			  none'>截止本日年发电量累计:</td>
			  <td colspan=2 class=xl133 style='border-right:.5pt solid black;border-left:
			  none'><center id='ZHCXSJ.GE'></center></td>
			  <td colspan=2 class=xl135 style='border-right:.5pt solid black;border-left:
			  none'>万千瓦时</td>
			  <td colspan=2 class=xl135 style='border-right:.5pt solid black;border-left:
			  none'>完成年发电量计划：</td>
			  <td colspan=2 class=xl136 style='border-right:.5pt solid black;border-left:
			  none'><center id='ZHCXSJ.NGEV'></center></td>
			  <td class=xl72>　</td>
			  <td class=xl72>　</td>
			  <td class=xl73>　</td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl74 style='height:13.5pt'>年上网电量计划:</td>
			  <td colspan=2 class=xl129 style='border-right:.5pt solid black;border-left:
			  none'><center id="QCJHSJ.NSE"></center></td>
			  <td class=xl75>万千瓦时</td>
			  <td colspan=2 class=xl131 style='border-right:.5pt solid black;border-left:
			  none'>截止本日年上网电量累计:</td>
			  <td colspan=2 class=xl133 style='border-right:.5pt solid black;border-left:
			  none'><center id='ZHCXSJ.SE'></center></td>
			  <td colspan=2 class=xl135 style='border-right:.5pt solid black;border-left:
			  none'>万千瓦时</td>
			  <td colspan=2 class=xl135 style='border-right:.5pt solid black;border-left:
			  none'>完成年上网电量计划：</td>
			  <td colspan=2 class=xl136 style='border-right:.5pt solid black;border-left:
			  none'><center id='ZHCXSJ.NSEV'></center></td>
			  <td colspan=2 class=xl76 style='mso-ignore:colspan'></td>
			  <td class=xl77>　</td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl74 style='height:13.5pt'>月发电量计划:</td>
			  <td colspan=2 class=xl129 style='border-right:.5pt solid black;border-left:
			  none'><center id="QCJHSJ.YGE"></center></td>
			  <td class=xl75>万千瓦时</td>
			  <td colspan=2 class=xl131 style='border-right:.5pt solid black;border-left:
			  none'>截止本日月发电量累计:</td>
			  <td colspan=2 class=xl133 style='border-right:.5pt solid black;border-left:
			  none'><center id='ZHCXYSJ.GE'></center></td>
			  <td colspan=2 class=xl135 style='border-right:.5pt solid black;border-left:
			  none'>万千瓦时</td>
			  <td colspan=2 class=xl135 style='border-right:.5pt solid black;border-left:
			  none'>完成月发电计划：</td>
			  <td colspan=2 class=xl136 style='border-right:.5pt solid black;border-left:
			  none'><center id='ZHCXYSJ.YGEV'></center></td>
			  <td class=xl78>　</td>
			  <td class=xl78>　</td>
			  <td class=xl79>　</td>
			 </tr>
			 <tr height=2 style='mso-height-source:userset;height:1.5pt'>
			  <td height=2 class=xl80 style='height:1.5pt'>　</td>
			  <td class=xl81>　</td>
			  <td class=xl81>　</td>
			  <td class=xl81>　</td>
			  <td class=xl82>　</td>
			  <td class=xl82>　</td>
			  <td class=xl83>　</td>
			  <td class=xl83>　</td>
			  <td class=xl84>　</td>
			  <td class=xl84>　</td>
			  <td class=xl82>　</td>
			  <td class=xl82>　</td>
			  <td class=xl85>　</td>
			  <td class=xl85>　</td>
			  <td class=xl86>　</td>
			  <td class=xl86>　</td>
			  <td class=xl87>　</td>
			 </tr>
			 <tr height=54 style='mso-height-source:userset;height:24.0pt'>
			  <td height=32 class=xl88 align=center style='height:24.0pt'>项<font
			  class="font10"><span style='mso-spacerun:yes'>&nbsp; </span></font><font
			  class="font12">目</font></td>
			  <td colspan=3 class=xl139 style='border-right:.5pt solid black;border-left:
			  none'>发电量（万千瓦时）</td>
			  <td colspan=3 class=xl142 width=235 style='border-right:.5pt solid black;
			  border-left:none;width:176pt'><center>上网电量（万千瓦时）</center></td>
			  <td colspan=3 class=xl142 width=186 style='border-right:.5pt solid black;
			  border-left:none;width:140pt'>生产厂用电量（万千瓦时）</td>
			  <td class=xl89 width=65 style='width:49pt'>生产厂用电率（%）</td>
			  <td colspan=3 class=xl145 width=214 style='border-right:.5pt solid black;
			  border-left:none;width:160pt'>综合厂用电量（万千瓦时）</td>
			  <td colspan=3 class=xl147 style='border-right:.5pt solid black;border-left:
			  none'>综合厂用电率（%）</td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl88 style='height:13.5pt'>　</td>
			  <td class=xl90>全厂</td>
			  <td class=xl90>一期</td>
			  <td class=xl90>二期</td>
			  <td class=xl90>全厂</td>
			  <td class=xl90>一期</td>
			  <td class=xl90>二期</td>
			  <td class=xl90>全厂</td>
			  <td class=xl90>一期</td>
			  <td class=xl90>二期</td>
			  <td class=xl90>全厂</td>
			  <td class=xl90>全厂</td>
			  <td class=xl90>一期</td>
			  <td class=xl90>二期</td>
			  <td class=xl90>全厂</td>
			  <td class=xl90>一期</td>
			  <td class=xl90>二期</td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl91 style='height:13.5pt'>本日</td>
			  <td class=xl92><center id='ZRBSJ.GE'></center> </td>
			  <td class=xl92><center id='YQRSJ.GE'></center></td>
			  <td class=xl92><center id='EQRSJ.GE'></center> </td>
			  <td class=xl92><center id='ZRBSJ.SE'></center> </td>
			  <td class=xl92><center id='YQRSJ.SE'></center> </td>
			  <td class=xl92><center id='EQRSJ.SE'></center> </td>
			  <td class=xl93><center id='QCRCE.CE'></center></td>
			  <td class=xl93><center id='YQRCE.SCE'></center></td>
			  <td class=xl94><center id='EQRCE.SCE'></center></td>
			  <td class=xl95><span style='mso-spacerun:yes'>&nbsp;</span><center id='QCRCE.CER'></center></td>
			  <td class=xl92><center id='ZRBSJ.SCE'></center> </td>
			  <td class=xl92><center id='YQRSJ.SCE'></center> </td>
			  <td class=xl92><center id='EQRSJ.SCE'></center> </td>
			  <td class=xl96><center id='ZRBSJ.SCER'></center></td>
			  <td class=xl96><center id='YQRSJ.SCER'></center></td>
			  <td class=xl96><center id='EQRSJ.SCER'></center></td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl91 style='height:13.5pt'>本月累计</td>
			  <td class=xl92><center id='QCYLJ.GE'></center> </td>
			  <td class=xl92><center id='YQYLJ.GE'></center> </td>
			  <td class=xl92><center id='EQYLJ.GE'></center> </td>
			  <td class=xl92><center id='QCYLJ.SE'></center> </td>
			  <td class=xl92><center id='YQYLJ.SE'></center> </td>
			  <td class=xl92><center id='EQYLJ.SE'></center> </td>
			  <td class=xl93><center id='QCYCE.CE'></center></td>
			  <td class=xl93><center id='YQYCE.SCE'></center></td>
			  <td class=xl94><center id='EQYCE.SCE'></center></td>
			  <td class=xl95><center id='QCYCE.CER'></center></td>
			  <td class=xl92><center id='QCYLJ.SCE'></center> </td>
			  <td class=xl92><center id='YQYLJ.SCE'></center> </td>
			  <td class=xl92><center id='EQYLJ.SCE'></center> </td>
			  <td class=xl96><center id='QCYLJ.SCER'></center></td>
			  <td class=xl96><center id='YQYLJ.SCER'></center></td>
			  <td class=xl96><center id='EQYLJ.SCER'></center></td>
			 </tr>
			 <tr height=24 style='mso-height-source:userset;height:18.0pt'>
			  <td height=24 class=xl91 style='height:18.0pt'>本年累计</td>
			  <td class=xl92><center id='ZHCXSJ.GE1'></center> </td>
			  <td class=xl92><center id='YQNLJSJ.GE'></center></td>
			  <td class=xl92><center id='EQNLJSJ.GE'></center> </td>
			  <td class=xl92><center id='ZHCXSJ.SE1'></center> </td>
			  <td class=xl92><center id='YQNLJSJ.SE'></center> </td>
			  <td class=xl92><center id='EQNLJSJ.SE'></center> </td>
			  <td class=xl93><center id='QCNCE.CE'></center></td>
			  <td class=xl93><center id='YQNCE.SCE'></center></td>
			  <td class=xl94><center id='EQNCE.SCE'></center></td>
			  <td class=xl95><center id='QCNCE.CER'></center></td>
			  <td class=xl92><center id='ZHCXSJ.SCE1'></center> </td>
			  <td class=xl92><center id='YQNLJSJ.SCE'></center> </td>
			  <td class=xl92><center id='ZHCXSJ.SCE'></center> </td>
			  <td class=xl96><center id='QCNLJSJ.SCER'></center></td>
			  <td class=xl96><center id='YQNLJSJ.SCER'></center></td>
			  <td class=xl96><center id='EQNLJSJ.SCER'></center></td>
			 </tr>
			 <tr height=2 style='mso-height-source:userset;height:1.5pt'>
			  <td height=2 class=xl97 style='height:1.5pt'>　</td>
			  <td class=xl98>　</td>
			  <td class=xl98>　</td>
			  <td class=xl98>　</td>
			  <td class=xl98>　</td>
			  <td class=xl98>　</td>
			  <td class=xl98>　</td>
			  <td class=xl99>　</td>
			  <td class=xl99>　</td>
			  <td class=xl100>　</td>
			  <td class=xl101>　</td>
			  <td class=xl98>　</td>
			  <td class=xl98>　</td>
			  <td class=xl98>　</td>
			  <td class=xl102>　</td>
			  <td class=xl102>　</td>
			  <td class=xl103>　</td>
			 </tr>
			 <tr height=20 style='mso-height-source:userset;height:15.0pt'>
			  <td height=20 class=xl88 align=center style='height:15.0pt'>指标名称</td>
			  <td colspan=3 class=xl139 style='border-right:.5pt solid black;border-left:
			  none'>耗原煤量T</td>
			  <td colspan=3 class=xl142 width=235 style='border-right:.5pt solid black;
			  border-left:none;width:176pt'>折标煤量T</td>
			  <td colspan=3 class=xl147 style='border-right:.5pt solid black;border-left:
			  none'>煤折标煤T</td>
			  <td colspan=3 class=xl142 width=208 style='border-right:.5pt solid black;
			  border-left:none;width:156pt'>入炉煤低位热值（MJ/KG）</td>
			  <td colspan=3 class=xl142 width=170 style='border-right:.5pt solid black;
			  border-left:none;width:128pt'>耗油量T</td>
			  <td class=xl104 align=center width=65 style='width:49pt'>二期启备</td>
			 </tr>
			 <tr height=46 style='mso-height-source:userset;height:34.5pt'>
			  <td height=46 class=xl88 style='height:34.5pt'>　</td>
			  <td class=xl90>全厂</td>
			  <td class=xl90>一期</td>
			  <td class=xl90>二期</td>
			  <td class=xl90>全厂</td>
			  <td class=xl90>一期</td>
			  <td class=xl90>二期</td>
			  <td class=xl90>全厂</td>
			  <td class=xl90>一期</td>
			  <td class=xl90>二期</td>
			  <td class=xl90>全厂</td>
			  <td class=xl90>一期</td>
			  <td class=xl90>二期</td>
			  <td class=xl90>全厂</td>
			  <td class=xl90>一期</td>
			  <td class=xl90>二期</td>
			  <td class=xl104 align=center width=65 style='width:49pt'>变电量(万千瓦时)</td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl91 style='height:13.5pt'>本日</td>
			  <td class=xl105><center id='ZRBSJ.CC'></center> </td>
			  <td class=xl105><center id='YQRSJ.CC'></center> </td>
			  <td class=xl105><center id='EQRSJ.CC'></center> </td>
			  <td class=xl105><center id='ZRBSJ.SC'></center> </td>
			  <td class=xl105><center id='YQRSJ.SC'></center></td>
			  <td class=xl105><center id='EQRSJ.SC'></center> </td>
			  <td class=xl105><center id='ZRBSJ.CSC'></center> </td>
			  <td class=xl105><center id='YQRSJ.CSC'></center></td>
			  <td class=xl105><center id='EQRSJ.CSC'></center> </td>
			  <td class=xl106><center id='ZRBSJ.LHV'></center> </td>
			  <td class=xl106><center id='YQRSJ.LHV'></center> </td>
			  <td class=xl106><center id='EQRSJ.LHV'></center> </td>
			  <td class=xl107><center id='ZRBSJ.CO'></center></td>
			  <td class=xl107><center id='YQRSJ.CO'></center></td>
			  <td class=xl107><center id='EQRSJ.CO'></center></td>
			  <td class=xl108 align=center width=65 style='width:49pt'><center id='EQRSJ.QBB'></center></td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl91 style='height:13.5pt'>本月累计</td>
			  <td class=xl105><center id="QCYLJ.CC"></center> </td>
			  <td class=xl105><center id="YQYLJ.CC"></center> </td>
			  <td class=xl105><center id="EQYLJ.CC"></center> </td>
			  <td class=xl105><center id="QCYLJ.SC"></center> </td>
			  <td class=xl105><center id="YQYLJ.SC"></center> </td>
			  <td class=xl105><center id="EQYLJ.SC"></center> </td>
			  <td class=xl105><center id="QCYLJ.CSC"></center> </td>
			  <td class=xl105><center id="YQYLJ.CSC"></center> </td>
			  <td class=xl105><center id="EQYLJ.CSC"></center> </td>
			  <td class=xl106><center id="QCYRLMRZ.LHV"></center> </td>
			  <td class=xl106><center id="YQLHV.LHV"></center> </td>
			  <td class=xl106><center id="EQYLHV.LHV"></center> </td>
			  <td class=xl107><center id="QCYLJ.CO"></center></td>
			  <td class=xl107><center id="YQYLJ.CO"></center></td>
			  <td class=xl107><center id="EQYLJ.CO"></center></td>
			  <td class=xl108 align=center width=65 style='width:49pt'><center id="EQYLJ.QBB"></center> </td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl91 style='height:13.5pt'>本年累计</td>
			  <td class=xl105><center id="QCNLJSJ.CC"></center> </td>
			  <td class=xl105><center id="YQNLJSJ.CC"></center> </td>
			  <td class=xl105><center id="EQNLJSJ.CC"></center> </td>
			  <td class=xl105><center id="QCNLJSJ.SC"></center> </td>
			  <td class=xl105><center id="YQNLJSJ.SC"></center> </td>
			  <td class=xl105><center id="EQNLJSJ.SC"></center> </td>
			  <td class=xl105><center id="QCNLJSJ.CSC"></center> </td>
			  <td class=xl105><center id="YQNLJSJ.CSC"></center> </td>
			  <td class=xl105><center id="EQNLJSJ.CSC"></center> </td>
			  <td class=xl106><center id="QCNRLMRZ.LHV"></center> </td>
			  <td class=xl106><center id="YQNLHV.LHV"></center> </td>
			  <td class=xl106><center id="EQNLHV.LHV"></center> </td>
			  <td class=xl107><center id="QCNLJSJ.CO"></center></td>
			  <td class=xl107><center id="YQNLJSJ.CO"></center></td>
			  <td class=xl107><center id="EQNLJSJ.CO"></center></td>
			  <td class=xl109 align=center><center id="EQNLJSJ.QBB"></center> </td>
			 </tr>
			 <tr height=3 style='mso-height-source:userset;height:2.25pt'>
			  <td height=3 class=xl97 style='height:2.25pt'>　</td>
			  <td class=xl110>　</td>
			  <td class=xl110>　</td>
			  <td class=xl110>　</td>
			  <td class=xl110>　</td>
			  <td class=xl110>　</td>
			  <td class=xl110>　</td>
			  <td class=xl110>　</td>
			  <td class=xl110>　</td>
			  <td class=xl110>　</td>
			  <td class=xl111>　</td>
			  <td class=xl111>　</td>
			  <td class=xl111>　</td>
			  <td class=xl112>　</td>
			  <td class=xl112>　</td>
			  <td class=xl112>　</td>
			  <td class=xl113>　</td>
			 </tr>
			 <tr height=27 style='mso-height-source:userset;height:20.25pt'>
			  <td height=27 class=xl88 align=center style='height:20.25pt'>指标名称</td>
			  <td colspan=3 class=xl139 style='border-right:.5pt solid black;border-left:
			  none'>发电煤耗（克/千瓦时）</td>
			  <td colspan=3 class=xl142 width=235 style='border-right:.5pt solid black;
			  border-left:none;width:176pt'><center>供电煤耗（克/千瓦时）</center></td>
			  <td colspan=3 class=xl147 style='border-right:.5pt solid black;border-left:
			  none'>运行小时H</td>
			  <td colspan=3 class=xl147 style='border-right:.5pt solid black;border-left:
			  none'>最高负荷（万千瓦）</td>
			  <!-- 
			  <td colspan=2 class=xl114 align=center style='border-right:.5pt solid black;border-left:
			  none'>平均负荷(万千瓦)</td>
			  <td colspan=2 class=xl114 align=center style='border-right:.5pt solid black;border-left:
			  none'>负荷率</td>
			   -->
			  <td colspan=2 class=xl114 align=center style='border-right:.5pt solid black;border-left:
			  none'></td>
			  <td colspan=2 class=xl114 align=center style='border-right:.5pt solid black;border-left:
			  none'></td>
			  <td class=xl114>　</td>
			  <td class=xl115>　</td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl88 style='height:13.5pt'>　</td>
			  <td class=xl90>全厂</td>
			  <td class=xl90>一期</td>
			  <td class=xl90>二期</td>
			  <td class=xl90>全厂</td>
			  <td class=xl90>一期</td>
			  <td class=xl90>二期</td>
			  <td class=xl90>全厂</td>
			  <td class=xl90>一期</td>
			  <td class=xl90>二期</td>
			  <td class=xl90>全厂</td>
			  <td class=xl90>一期</td>
			  <td class=xl90>二期</td>
			  <!--  
			  <td class=xl90 colspan=2>全厂</td>
			  <td class=xl90 colspan=2>全厂</td>
			  -->
			  <td class=xl90>　</td>
			  <td class=xl90>　</td>
			  <td class=xl90>　</td>
			  <td class=xl90>　</td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl91 style='height:13.5pt'>本日</td>
			  <td class=xl116><center id="ZRBSJ.GCR"></center> </td>
			  <td class=xl116><center id="YQRSJ.GCR"></center> </td>
			  <td class=xl116><center id="EQRSJ.GCR"></center> </td>
			  <td class=xl116><center id="ZRBSJ.SCR"></center>  </td>
			  <td class=xl116><center id="YQRSJ.SCR"></center>  </td>
			  <td class=xl116><center id="EQRSJ.SCR"></center>  </td>
			  <td class=xl116><center id='ZHCXRSJ.SH'></center> </td>
			  <td class=xl116><center id="YQRSJ.SH"></center> </td>
			  <td class=xl116><center id="EQRSJ.SH"></center> </td>
			  <td class=xl117>　</td>
			  <td class=xl117><center id="YQRSJ.UML"></center></td>
			  <td class=xl117><center id="EQRSJ.UML"></center></td>
			  <!--  
			  <td class=xl117 colspan=2><center id="ZRBSJ.OAL"></center></td>
			  <td class=xl117  colspan=2><center id="ZRBSJ.GEV"></center>　</td>
			  -->
			  <td class=xl117>　</td>
			  <td class=xl118>　</td>
			  <td class=xl117>　</td>
			  <td class=xl118>　</td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl91 style='height:13.5pt'>本月累计</td>
			  <td class=xl116><center id="QCYLJ.GCR"></center> </td>
			  <td class=xl116><center id="YQYLJ.GCR"></center> </td>
			  <td class=xl116><center id="EQYLJ.GCR"></center> </td>
			  <td class=xl116><center id="QCYLJ.SCR"></center> </td>
			  <td class=xl116><center id="YQYLJ.SCR"></center> </td>
			  <td class=xl116><center id="EQYLJ.SCR"></center> </td>
			  <td class=xl116><center id='ZHCXYSJ.SH'></center></td>
			  <td class=xl116><center id="YQYLJ.SH"></center> </td>
			  <td class=xl116><center id="EQYLJ.SH"></center> </td>
			  <td class=xl117>　</td>
			  <td class=xl117><center id="YQYLJ.UML"></center></td>
			  <td class=xl117><center id="EQYLJ.UML"></center></td>
			  <!-- 
			  <td class=xl117 colspan="2"><center id="QCYLJ.OAL"></center>　</td>
			  <td class=xl117 colspan="2"><center id="QCYLJ.GEV"></center>　</td>
			   -->
			  <td class=xl117>　</td>
			  <td class=xl119>　</td>
			  <td class=xl117>　</td>
			  <td class=xl119>　</td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl91 style='height:13.5pt'>本年累计</td>
			  <td class=xl116><center id="QCNLJSJ.GCR"></center> </td>
			  <td class=xl116><center id="YQNLJSJ.GCR"></center> </td>
			  <td class=xl116><center id="EQNLJSJ.GCR"></center> </td>
			  <td class=xl116><center id="QCNLJSJ.SCR"></center> </td>
			  <td class=xl116><center id="YQNLJSJ.SCR"></center> </td>
			  <td class=xl116><center id="EQNLJSJ.SCR"></center> </td>
			  <td class=xl116><center id='ZHCXSJ.SH'></center></td>
			  <td class=xl116><center id="YQNLJSJ.SH"></center> </td>
			  <td class=xl116><center id="EQNLJSJ.SH"></center> </td>
			  <td class=xl117>　</td>
			  <td class=xl117><center id="YQNLJSJ.UML"></center></td>
			  <td class=xl117><center id="EQNLJSJ.UML"></center></td>
			  <!--  
			  <td class=xl117 colspan="2"><center id="ZHCXSJ.NOAL"></center>　</td>
			  <td class=xl117 colspan="2"><center id="ZHCXSJ.NGEVL"></center>　</td>
			  -->
			  <td class=xl117>　</td>
			  <td class=xl119>　</td>
			  <td class=xl117>　</td>
			  <td class=xl119>　</td>
			 </tr>
			
			 <!-- 
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl88 align=center style='height:13.5pt'>项目</td>
			  <td class=xl114 align=center>主汽压力</td>
			  <td class=xl114 align=center>主汽温度</td>
			  <td class=xl114 align=center>再热汽温</td>
			  <td class=xl114 align=center>排烟温度</td>
			  <td class=xl104 align=center width=79 style='width:59pt'>凝汽器真空</td>
			  <td class=xl104 align=center width=79 style='width:59pt'>烟气含氧量</td>
			  <td class=xl114 align=center>炉膛压力</td>
			  <td class=xl114 align=center>给水温度</td>
			  <td class=xl114 align=center>排汽温度</td>
			  <td class=xl114 align=center>灰飞可燃物</td>
			  <td class=xl114 align=center><span style='mso-spacerun:yes'>&nbsp;</span></td>
			  <td class=xl114>　</td>
			  <td class=xl114>　</td>
			  <td class=xl114 align=center><span style='mso-spacerun:yes'>&nbsp;</span></td>
			  <td class=xl114>　</td>
			  <td class=xl114>　</td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl88 style='height:13.5pt'>　</td>
			  <td class=xl103>Mpa</td>
			  <td class=xl103>℃</td>
			  <td class=xl103>℃</td>
			  <td class=xl103>℃</td>
			  <td class=xl103>%</td>
			  <td class=xl103>%</td>
			  <td class=xl103>Mpa</td>
			  <td class=xl103>℃</td>
			  <td class=xl103>℃</td>
			  <td class=xl123 align=center>%</td>
			  <td class=xl90>　</td>
			  <td class=xl90>　</td>
			  <td class=xl90>　</td>
			  <td class=xl124>　</td>
			  <td class=xl124>　</td>
			  <td class=xl124>　</td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl125 style='height:13.5pt'>1号机</td>
			  <td class=xl96>-</td>
			  <td class=xl96>-</td>
			  <td class=xl96>-</td>
			  <td class=xl96>-</td>
			  <td class=xl96>-</td>
			  <td class=xl96>-</td>
			  <td class=xl96>-</td>
			  <td class=xl96>-</td>
			  <td class=xl96>-</td>
			  <td class=xl96>-</td>
			  <td class=xl90>　</td>
			  <td class=xl90>　</td>
			  <td class=xl90>　</td>
			  <td class=xl90>　</td>
			  <td class=xl90>　</td>
			  <td class=xl90>　</td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl125 style='height:13.5pt'>2号机</td>
			  <td class=xl96>-</td>
			  <td class=xl96>-</td>
			  <td class=xl96>-</td>
			  <td class=xl96>-</td>
			  <td class=xl96>-</td>
			  <td class=xl96>-</td>
			  <td class=xl96>-</td>
			  <td class=xl96>-</td>
			  <td class=xl96>-</td>
			  <td class=xl96>-</td>
			  <td class=xl90>　</td>
			  <td class=xl90>　</td>
			  <td class=xl90>　</td>
			  <td class=xl90>　</td>
			  <td class=xl90>　</td>
			  <td class=xl90>　</td>
			 </tr>
			 <tr height=25 style='mso-height-source:userset;height:18.75pt'>
			  <td height=25 class=xl125 style='height:18.75pt'>3号机</td>
			  <td class=xl96><center id="U3JZSJ.TMP"></center></td>
			  <td class=xl96><center id="U3JZSJ.TMT"></center></td>
			  <td class=xl96><center id="U3JZSJ.TRT"></center></td>
			  <td class=xl96><center id="U3JZSJ.FGT"></center></td>
			  <td class=xl96><center id="U3JZSJ.VCD"></center></td>
			  <td class=xl96><center id="U3JZSJ.OP"></center></td>
			  <td class=xl96><center id="U3JZSJ.LP"></center></td>
			  <td class=xl96><center id="U3JZSJ.FWT"></center></td>
			  <td class=xl96><center id="U3JZSJ.HPT"></center></td>
			  <td class=xl126 align=center><center id="U3JZSJ.CFH"></center></td>
			  <td class=xl90>　</td>
			  <td class=xl90>　</td>
			  <td class=xl90>　</td>
			  <td class=xl90>　</td>
			  <td class=xl90>　</td>
			  <td class=xl90>　</td>
			 </tr>
			 <tr height=18 style='height:13.5pt'>
			  <td height=18 class=xl125 style='height:13.5pt'>4号机</td>
			  <td class=xl96><center id="U4JZSJ.TMP"></center></td>
			  <td class=xl96><center id="U4JZSJ.TMT"></center></td>
			  <td class=xl96><center id="U4JZSJ.TRT"></center></td>
			  <td class=xl96><center id="U4JZSJ.FGT"></center></td>
			  <td class=xl96><center id="U4JZSJ.VCD"></center></td>
			  <td class=xl96><center id="U4JZSJ.OP"></center></td>
			  <td class=xl96><center id="U4JZSJ.LP"></center></td>
			  <td class=xl96><center id="U4JZSJ.FWT"></center></td>
			  <td class=xl96><center id="U4JZSJ.HPT"></center></td>
			  <td class=xl126 align=center><center id="U4JZSJ.CFH"></center></td>
			  <td class=xl123 align=center><span
			  style='mso-spacerun:yes'>&nbsp;&nbsp;</span></td>
			  <td class=xl123 align=center><span style='mso-spacerun:yes'>&nbsp;</span></td>
			  <td class=xl123 align=center><span style='mso-spacerun:yes'>&nbsp;</span></td>
			  <td class=xl124>　</td>
			  <td class=xl124>　</td>
			  <td class=xl124>　</td>
			 </tr> -->
			</table>
		</div>
	</body>
</html>
