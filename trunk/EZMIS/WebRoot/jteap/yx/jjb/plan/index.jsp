<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.yx.jjb.manager.PaiBanManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="java.util.List"%>
<%@page import="com.jteap.yx.jjb.model.PaiBan"%>
<%@ include file="/inc/import.jsp" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/inc/meta.jsp" %>
	<link href="zl_style.css" rel="stylesheet" type="text/css" />
	<style type="text/css"> 
		body{width:100%!important;width:100%}
		.btn{
			BORDER-RIGHT: #CEB60C 1px solid; PADDING-RIGHT: 2px; BORDER-TOP: 
	           #CEB60C 1px solid; PADDING-LEFT: 2px; FONT-SIZE: 12px; FILTER: 
	           progid:DXImageTransform.Microsoft.Gradient(GradientType=0, 
	           StartColorStr=#FFFFFF, EndColorStr=#F3DC41); BORDER-LEFT: #CEB60C 
	           1px solid; CURSOR: hand; COLOR: black; PADDING-TOP: 0px; 
	           BORDER-BOTTOM: #CEB60C 1px solid
		}
	</style>
</head>

<body lang="zh" onload="initial()" style="width:100%">
 	
 	<script type="text/javascript" src="${contextPath}/script/common.js"></script>
 	<script type="text/javascript">
 		function showPaiBanForm(){
 			showIFModule("${contextPath}/jteap/yx/jjb/plan/paiBan.jsp","排班设置","true",600,250,{});
 			window.location.reload();
 		}
 	</script>
	
<!--内容 开始-->
	<div class="Cont">
		<div align="left">
			<%
				String showSet = "style='display: none;'";
				if(isRoot || curPersonRoles.indexOf("系统管理员") != -1){
					showSet = "";
				}
			 %>
			<input type="button" value="排班设置" <%=showSet %> class="btn" onclick="showPaiBanForm()">
		</div>		
		<div class="CAL_wid">
			<div class="CAL_LN">
				<div class="CAL_Az"></div>
				<a href="javascript:void(0)" class="CAL_A1" onclick="pushBtm('YU')" title="上一年">上一年</a>
				<a href="javascript:void(0)" class="CAL_A2" title="上一月" onclick="pushBtm('MU')">上一月</a>
				<a href="javascript:void(0)" title="今日" class="CAL_A3" onclick="pushBtm('')"></a>
				<a href="javascript:void(0)" class="CAL_A4" title="下一月" onclick="pushBtm('MD')">下一月</a>
				<a href="javascript:void(0)" class="CAL_A5" title="下一年" onclick="pushBtm('YD')">下一年</a>
			</div>
			<!--日历内容区 开始-->
  <!--嵌入万年历代码 开始-->
 
<script language=javascript> 
<!--
var lunarInfo = new Array(
0x04bd8,0x04ae0,0x0a570,0x054d5,0x0d260,0x0d950,0x16554,0x056a0,0x09ad0,0x055d2,
0x04ae0,0x0a5b6,0x0a4d0,0x0d250,0x1d255,0x0b540,0x0d6a0,0x0ada2,0x095b0,0x14977,
0x04970,0x0a4b0,0x0b4b5,0x06a50,0x06d40,0x1ab54,0x02b60,0x09570,0x052f2,0x04970,
0x06566,0x0d4a0,0x0ea50,0x06e95,0x05ad0,0x02b60,0x186e3,0x092e0,0x1c8d7,0x0c950,
0x0d4a0,0x1d8a6,0x0b550,0x056a0,0x1a5b4,0x025d0,0x092d0,0x0d2b2,0x0a950,0x0b557,
0x06ca0,0x0b550,0x15355,0x04da0,0x0a5d0,0x14573,0x052d0,0x0a9a8,0x0e950,0x06aa0,
0x0aea6,0x0ab50,0x04b60,0x0aae4,0x0a570,0x05260,0x0f263,0x0d950,0x05b57,0x056a0,
0x096d0,0x04dd5,0x04ad0,0x0a4d0,0x0d4d4,0x0d250,0x0d558,0x0b540,0x0b5a0,0x195a6,
0x095b0,0x049b0,0x0a974,0x0a4b0,0x0b27a,0x06a50,0x06d40,0x0af46,0x0ab60,0x09570,
0x04af5,0x04970,0x064b0,0x074a3,0x0ea50,0x06b58,0x055c0,0x0ab60,0x096d5,0x092e0,
0x0c960,0x0d954,0x0d4a0,0x0da50,0x07552,0x056a0,0x0abb7,0x025d0,0x092d0,0x0cab5,
0x0a950,0x0b4a0,0x0baa4,0x0ad50,0x055d9,0x04ba0,0x0a5b0,0x15176,0x052b0,0x0a930,
0x07954,0x06aa0,0x0ad50,0x05b52,0x04b60,0x0a6e6,0x0a4e0,0x0d260,0x0ea65,0x0d530,
0x05aa0,0x076a3,0x096d0,0x04bd7,0x04ad0,0x0a4d0,0x1d0b6,0x0d250,0x0d520,0x0dd45,
0x0b5a0,0x056d0,0x055b2,0x049b0,0x0a577,0x0a4b0,0x0aa50,0x1b255,0x06d20,0x0ada0);
 
var solarMonth = new Array(31,28,31,30,31,30,31,31,30,31,30,31);
var sTermInfo = new Array(0,21208,42467,63836,85337,107014,128867,150921,173149,195551,218072,240693,263343,285989,308563,331033,353350,375494,397447,419210,440795,462224,483532,504758)
var nStr1 = new Array('日','一','二','三','四','五','六','七','八','九','十')
var monthName = new Array("JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC");
 
function lYearDays(y) {
   var i, sum = 348
   for(i=0x8000; i>0x8; i>>=1) sum += (lunarInfo[y-2000] & i)? 1: 0
   return(sum+leapDays(y))
}
 
function leapDays(y) {
   if(leapMonth(y))  return((lunarInfo[y-2000] & 0x10000)? 30: 29)
   else return(0)
}
 
function leapMonth(y) {
   return(lunarInfo[y-2000] & 0xf)
}
 
function monthDays(y,m) {
   return( (lunarInfo[y-2000] & (0x10000>>m))? 30: 29 )
}
 
function Lunar(objDate) {
 
   var i, leap=0, temp=0
   var baseDate = new Date(2000,0,31)
   var offset   = (objDate - baseDate)/86400000
 
   this.dayCyl = offset + 40
   this.monCyl = 14
 
   for(i=2000; i<2050 && offset>0; i++) {
      temp = lYearDays(i)
      offset -= temp
      this.monCyl += 12
   }
 
   if(offset<0) {
      offset += temp;
      i--;
      this.monCyl -= 12
   }
 
   this.year = i
   this.yearCyl = i-1864
 
   leap = leapMonth(i)
   this.isLeap = false
 
   for(i=1; i<13 && offset>0; i++) {
      if(leap>0 && i==(leap+1) && this.isLeap==false)
         { --i; this.isLeap = true; temp = leapDays(this.year); }
      else
         { temp = monthDays(this.year, i); }
 
      if(this.isLeap==true && i==(leap+1)) this.isLeap = false
 
      offset -= temp
      if(this.isLeap == false) this.monCyl ++
   }
 
   if(offset==0 && leap>0 && i==leap+1)
      if(this.isLeap)
         { this.isLeap = false; }
      else
         { this.isLeap = true; --i; --this.monCyl;}
 
   if(offset<0){ offset += temp; --i; --this.monCyl; }
 
   this.month = i
   this.day = offset + 1
}
 
function solarDays(y,m) {
   if(m==1)
      return(((y%4 == 0) && (y%100 != 0) || (y%400 == 0))? 29: 28)
   else
      return(solarMonth[m])
}
 
function calElement(sYear,sMonth,sDay,week,lYear,lMonth,lDay,isLeap,cYear,cMonth,cDay) {
 
      this.isToday    = false;
      this.sYear      = sYear;
      this.sMonth     = sMonth;
      this.sDay       = sDay;
      this.week       = week;
      this.lYear      = lYear;
      this.lMonth     = lMonth;
      this.lDay       = lDay;
      this.isLeap     = isLeap;
      this.cYear      = cYear;
      this.cMonth     = cMonth;
      this.cDay       = cDay;
 
      this.color      = '';
 
      this.lunarFestival = '';
      this.solarFestival = '';
 
}
 
function calendar(y,m) {
	
   var sDObj, lDObj, lY, lM, lD=1, lL, lX=0, tmp1, tmp2
   var lDPOS = new Array(3)
   var n = 0
   var firstLM = 0
 
   sDObj = new Date(y,m,1)
 
   this.length    = solarDays(y,m)
   this.firstWeek = sDObj.getDay()
 
   for(var i=0;i<this.length;i++) {
 
      if(lD>lX) {
         sDObj = new Date(y,m,i+1)
         lDObj = new Lunar(sDObj)
         lY    = lDObj.year
         lM    = lDObj.month
         lD    = lDObj.day
         lL    = lDObj.isLeap
         lX    = lL? leapDays(lY): monthDays(lY,lM)
 
         if(n==0) firstLM = lM
         lDPOS[n++] = i-lD+1
      }
 
      this[i] = new calElement(y, m+1, i+1, nStr1[(i+this.firstWeek)%7],
                               lY, lM, lD++, lL )
 
      if((i+this.firstWeek)%7==0)   this[i].color = "#FF5F08";//'#FF5F07'	 
      if((i+this.firstWeek)%14==6) {this[i].color = "#FF5F08";}//'#FF5F07'
      if((i+this.firstWeek)%14==13) {this[i].color = "#FF5F08";}//'#FF5F07'
   }
 
   if(y==tY && m==tM) {
   		this[tD-1].isToday = true;
   }
 
}
 
var cld;
 
function drawCld(SY,SM) {
   var i,sD,s,size;
   cld = new calendar(SY,SM);
	if(SY == "undefined")debugger;
 
   for(i=0;i<35;i++) {
      sObj=$('SD'+ i);
      lObj=$('LD'+ i);
 	
 	  if(sObj != null){
    	  sObj.style.background = '';
 	  }
 	  if(lObj != null){
	      lObj.style.background = '';
 	  }
 
      //sD = i - cld.firstWeek;
      sD = i;
	  if(sD == "undefined" || sD == undefined) debugger;
	  
      if(sD>-1 && sD<cld.length) {
         sObj.innerHTML = sD+1;
		 if(sObj.innerHTML == "undefined" || sObj.innerHTML == undefined || sObj.innerHTML.indexOf("und") != -1) {
			debugger;
		 }
         if(cld[sD].isToday){
         		sObj.style.background = '#DEFDFD';
         		lObj.style.background = '#91DAE3';
         }
 
         sObj.style.color = cld[sD].color;
 	
         s=cld[sD].lunarFestival;
         if(s.length>0) {
            if(s.length>5) s = s.substr(0, 3)+'…';
            s  = "<span style='color:#FF5F07'>"+s+"</span>";//s.fontcolor('blue');s.fontcolor('FF5F07');
         }else {
            s=cld[sD].solarFestival;
            if(s.length>0) {
               size = (s.charCodeAt(0)>0 && s.charCodeAt(0)<128)?8:4;
               if(s.length>size+1) s = s.substr(0, size-1)+'…';
               s = "<span style='color:blue'>"+s+"</span>";//s.fontcolor('0168EA');
            }
            else {
               if(s.length>0) s = s = "<span style='color:green'>"+s+"</span>";//s.fontcolor('44D7CF');
            }
         }
         if(cld[sD] != undefined){
         	lObj.innerHTML = "星期"+cld[sD].week;
         }
 			
      }
      else {
      	if(sObj != null){
          sObj.innerHTML = ' ';
      	}
      	if(lObj != null){
	      lObj.innerHTML = ' ';
      	}
      }
   }
}
 
function changeCld() {
   var y,m;
   y = CLD.SY.selectedIndex+2000;
   m = CLD.SM.selectedIndex;
   drawCld(y,m);
}
 
function pushBtm(K) {
   switch (K){
      case 'YU' :
         if(CLD.SY.selectedIndex>0) CLD.SY.selectedIndex--;
         break;
      case 'YD' :
         if(CLD.SY.selectedIndex<149) CLD.SY.selectedIndex++;
         break;
      case 'MU' :
         if(CLD.SM.selectedIndex>0) {
            CLD.SM.selectedIndex--;
         }
         else {
            CLD.SM.selectedIndex=11;
            if(CLD.SY.selectedIndex>0) CLD.SY.selectedIndex--;
         }
         break;
      case 'MD' :
         if(CLD.SM.selectedIndex<11) {
            CLD.SM.selectedIndex++;
         }
         else {
            CLD.SM.selectedIndex=0;
            if(CLD.SY.selectedIndex<149) CLD.SY.selectedIndex++;
         }
         break;
      default :
         CLD.SY.selectedIndex=tY-2000;
         CLD.SM.selectedIndex=tM;
   }
   changeCld();
}
 
var Today = new Date();
var tY = Today.getFullYear();
var tM = Today.getMonth();
var tD = Today.getDate();
 
var width = "130";
var offsetx = -85;
var offsety = -105;
if(!document.all)offsety = -10;
var x = 0;
var y = 0;
var snow = 0;
var sw = 0;
var cnt = 0;
 
var dStyle;
function $(sId){return document.getElementById(sId);}

var CLD;
var GZ;
function initial() {
   CLD = $("CLD");
   GZ = $("GZ");
   dStyle = $("detail").style;
   CLD.SY.selectedIndex=tY-2000;
   CLD.SM.selectedIndex=tM;
   if(!document.all){
		offsety = -100;
   }
   drawCld(tY,tM);
}

//-->
</script>
 
<div id="detail" style="position:absolute;visibility:visible;"></div>
<FORM name=CLD id=CLD>
<table cellSpacing=0 cellPadding=0 align=center border=0 width="100%" height="auto">
<tbody>
<tr>
<td class=bs vAlign=top>
</td>
<td class=lb vAlign=top width=100%>
<table cellSpacing=0 cellPadding=0 width=100% border=0>
<tbody>
<tr bgColor=#F2F4F4>
<td height="17%" align="center"><SPAN class=sm>
<font style="font-SIZE: 9pt" color=#ffffff>
<SELECT onchange=changeCld() name="SY" id="SY"> 
	<%
		for(int i=2000; i<=2200; i++){
	 %>
	 		<option><%=i %></option>
	 <%} %>
</SELECT> 
</font>
<font color=#333399>年 </font>
<font style="font-size: 9pt" color=#ffffff>
<select onchange="changeCld()" name=SM id="SM">
	<%for(int i=1; i<=12; i++){ %>
		<option><%=i %></option>
	<%} %>
 </select> 
 </font></SPAN><font style="font-SIZE: 9pt" color=#ffffff></font><SPAN class=sm><font color=#333399>月</font>
 <script type="text/javascript">
	document.write("</font>");
</script>
 <font class=sm id=GZ></font></SPAN>
 <script type="text/javascript">
	document.write("</font>");
</script>
<font color=#333399> </font>
 <script type="text/javascript">
	document.write("</SPAN>");
</script>
</td>
</tr></TBODY></table>
<table cellSpacing=1 cellPadding=0 width="100%" border=0>
<TBODY>
<tr>
<td vAlign=bottom>
<table cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
<tr>
<td>
<table cellSpacing=1 cellPadding=0 width=100% align=center border=0>
<TBODY>
<tr>
<td class=ch align=middle height="24%">
<table height="24%" cellSpacing=3 cellPadding=0 width=100% border=0>
<TBODY>
<%
	PaiBanManager paiBanManager = (PaiBanManager)SpringContextUtil.getBean("paiBanManager");
	
 	List<PaiBan> list = paiBanManager.getAll();
 	String baiBzb = "";
 	String zhongBzb = "";
 	String yeBzb = "";
 	
 	for(PaiBan paiBan : list){
 		if("夜班".equals(paiBan.getBc())){
 			yeBzb = paiBan.getZb();
 		}else if("白班".equals(paiBan.getBc())){
 			baiBzb = paiBan.getZb();
 		}else if("中班".equals(paiBan.getBc())){
 			zhongBzb = paiBan.getZb();
 		}
 	}
 	String[] arrayBaiZb = baiBzb.split(",");
 	String[] arrayZhongZb = zhongBzb.split(",");
 	String[] arrayYeZb = yeBzb.split(",");
%>
		<tr align=middle>
			<td width=10% style="background-color:#FFECF5;font-size:28px; font-weight:bold; font-family:SimHei; text-align:center">
				夜班
			</td>
	<%
		for(int i=0; i<arrayYeZb.length; i++){
 	%>
			<td width=16% style="background-color:#FFECF5;font-size:18px; font-weight:bold; text-align:center">
				<%=arrayYeZb[i]%>
			</td>
	<%}%>
		</tr>
		<tr align=middle>
		<td width=10% style="background-color:#FFD2D2;font-size:28px; font-weight:bold; font-family:SimHei; text-align:center">
			白班
		</td>
	<%
		for(int i=0; i<arrayBaiZb.length; i++){
 	%>
			<td width=16% style="background-color:#FFD2D2;font-size:18px; font-weight:bold; text-align:center">
				<%=arrayBaiZb[i]%>
			</td>
	<%}%>
		</tr>
		<tr align=middle>
		<td width=10% style="background-color:#D2E9FF;font-size:28px; font-weight:bold; font-family:SimHei; text-align:center">
			中班
		</td>
	<%
		for(int i=0; i<arrayZhongZb.length; i++){
 	%>
			<td width=16% style="background-color:#D2E9FF;font-size:18px; font-weight:bold; text-align:center">
				<%=arrayZhongZb[i]%>
			</td>
	<%}%>
		</tr>
</TBODY></table></td></tr>

<SCRIPT language=JavaScript><!--
var gNum
for(i=0;i<7;i++) {
   document.write(' <tr><td align=middle bgColor=#fafbfb height="24%"><table class="cal_day_NO" cellSpacing=0px cellPadding=0px><TBODY><tr align=middle><td width="10%">&nbsp;</td> ')
   for(j=0;j<5;j++) {
	  gNum = i*5+j
	  document.write('<td id="SD' + gNum +'" width="16%" style="BACKGROUND: none transparent scroll repeat 0% 0%; cursor : default; COLOR: #ff5f08" TITLE=""')
	  document.write(' TITLE=""> </td>')
   }
   document.write('</tr></table></td></tr><tr><td height="10%" align="middle" bgcolor="#F3F5F8"><table class="cal_day_jr" cellSpacing=0px cellPadding=0px><TBODY><tr align=center><td width="10%">&nbsp;</td> ')
   for(j=0;j<5;j++) {
	  gNum = i*5+j
	  document.write('<td id="LD' + gNum +'"width="16%" class="bs" TITLE="" style="BACKGROUND: none transparent scroll repeat 0% 0%; cursor : default; COLOR: #666666"> </td>')
   }
   document.write('</tr></table></td></tr>')
}
//--></SCRIPT>
</tbody></table></td></tr></tbody></table></td></tr></tbody></table></td>
<td valign=top></td></tr>
<script type="text/javascript">
	document.write("</form>");
</script>
</tbody></table>  
  <!--嵌入万年历代码 结束-->			
			</div>
			<!--日历内容区 结束-->
		</div>
<script type="text/javascript">
	document.write("</div>");
</script>
<!-- START NetEase Mail Assistant 2008 Count -->

<!-- END NetEase Mail Assistant 2008 Count -->

</body>
</html>