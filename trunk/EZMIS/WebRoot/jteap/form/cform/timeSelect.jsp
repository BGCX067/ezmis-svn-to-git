<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<HEAD>
		<%@ page language="java" contentType="text/html; charset=gbk"
			pageEncoding="UTF-8"%>

		
		<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<META name="GENERATOR" content="IBM Software Development Platform">
		<META http-equiv="Content-Style-Type" content="text/css">
		<SCRIPT LANGUAGE="JavaScript" src="theme/js/common.js"></SCRIPT>
		<TITLE>选择时间</TITLE>
		<script>
// 日历开始
var monthnum, yearnum, cellobj, sDate;
cellobj = null;
cellcolor = null;
timeobj = null;
// 中文月份,如果想显示英文月份，修改下面的注释
/*
 * var months = new Array("January", "February", "March", "April", "May",
 * "June", "July", "August", "September", "October", "November", "December");
 */
var months = new Array("一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月",
		"十月", "十一月", "十二月");
var daysInMonth = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
// 中文周 如果想显示 英文的，修改下面的注释
/*
 * var days = new Array("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
 * "Friday", "Saturday");
 */
var days = new Array("日", "一", "二", "三", "四", "五", "六");
function getDays(month, year) {
	// 下面的这段代码是判断当前是否是闰年的
	if (1 == month)
		return ((0 == year % 4) && (0 != (year % 100))) || (0 == year % 400)
				? 29
				: 28;
	else
		return daysInMonth[month];
}

function getToday() {
	// 得到今天的年,月,日
	this.now = new Date();
	this.year = this.now.getFullYear();
	this.month = this.now.getMonth();
	this.day = this.now.getDate();
	this.hour = this.now.getHours()
	this.minute = this.now.getMinutes();
	this.second = this.now.getSeconds();
}

today = new getToday();
sDate = today.year + "年"
if ((today.month + 1) < 10)
	sDate += "0";
sDate += (today.month + 1) + "月"
if (today.day < 10)
	sDate += "0";
sDate += today.day + "日"
thour = today.hour
tmin = today.minute
tsec = today.second

function newCalendar() {
	today = new getToday();
	var parseYear = yearnum;
	var newCal = new Date(parseYear, monthnum, 1);
	var day = -1;
	var startDay = newCal.getDay();
	var daily = 0;
	if ((today.year == newCal.getFullYear())
			&& (today.month == newCal.getMonth()))
		day = today.day;
	var tableCal = document.all.calendar.tBodies.dayList;
	var intDaysInMonth = getDays(newCal.getMonth(), newCal.getFullYear());
	for (var intWeek = 0;intWeek < tableCal.rows.length; intWeek++)
		for (var intDay = 0;intDay < tableCal.rows[intWeek].cells.length; intDay++) {
			var cell = tableCal.rows[intWeek].cells[intDay];
			if ((intDay == startDay) && (0 == daily))
				daily = 1;
			if (day == daily) {
				// 今天，调用今天的Class
				cell.className = "today";
				cell.style.background = "#6699cc"
			} else if (intDay == 6) {
				// 周六
				cell.className = "sunday";
				cell.style.background = "#ffffff"
			} else if (intDay == 0) {
				// 周日
				cell.className = "satday";
				cell.style.background = "#ffffff"
			} else {
				// 平常
				cell.className = "normal";
				cell.style.background = "#ffffff"
			}
			if ((daily > 0) && (daily <= intDaysInMonth)) {
				cell.innerText = daily;
				daily++;
			} else
				cell.innerText = "";
		}
}

function getDate() {
	// 这段代码处理鼠标点击的情况
	if ("TD" == event.srcElement.tagName)
		if ("" != event.srcElement.innerText) {
			newCalendar();
			cellobj = event.srcElement;
			cellobj.style.background = "#b0b0b0";
			sDate = yearnum + "年"
			if ((monthnum + 1) < 10)
				sDate += "0";
			sDate += (monthnum + 1) + "月"
			if (event.srcElement.innerText < 10)
				sDate += "0";
			sDate += event.srcElement.innerText + "日";
			showtime();
		}
}

function chgyear(yearid) {
	yearnum += yearid;
	document.all.yearid.innerText = yearnum + "年";
	newCalendar();
}

function chgmonth(monthid) {
	monthnum += monthid
	if (monthnum < 0) {
		monthnum = 11;
		yearnum--;
		document.all.yearid.innerText = yearnum + "年";
	}
	if (monthnum > 11) {
		monthnum = 0;
		yearnum++;
		document.all.yearid.innerText = yearnum + "年";
	}
	document.all.monthid.innerText = months[monthnum];
	newCalendar();
}

function getback(obj) {
	strtime = sDate + " ";
	if (thour < 10)
		strtime += "0";
	strtime += thour + "时";
	if (tmin < 10)
		strtime += "0";
	strtime += tmin + "分";
	timeobj = null;
	
	window.returnValue=strtime+"00秒";
	window.close();
}

function showtime() {
	// document.all.sj.innerText="发送时间:"+sDate;
}

function init() {
	newCalendar();
	showtime();
}

function limitinput(obj) {
	if (obj.value.length >= 2)
		obj.value = obj.value.substring(1, obj.value.length)
}

function chstime(obj) {
	timeobj = obj;
	// calend.style.top=event.clientY+document.body.scrollTop;
	// calend.style.left=event.clientX;
	calend.style.visibility = 'visible';
	newCalendar();
	// window.open('settime.asp','settime','toolbar=no,menubar=no,resizable=no,width=200,height=180')
}

function closetime() {
	calend.style.visibility = 'hidden';
	self.close();
}

function cleartime(){
	window.returnValue = "cleartime";
	self.close();
}

//日历结束
</script>
		<SCRIPT>
var pWin=window.dialogArguments.opener;

var isWtCompleteFlag=0;
//窗口:检测是否加载完成
function initWaitForLoad(){
  if (document.readyState=="complete"){
    isWtCompleteFlag=1;
  }else{
  }
  if(isWtCompleteFlag==1) {
	document.all.calend.style.visibility="visible";
	document.all.calend.style.position="static";
	chstime('')
    //do what you want.......
    clearInterval(tmpTimer);
  }
}
tmpTimer=window.setInterval("initWaitForLoad()",300);
</SCRIPT>
	</HEAD>

	<BODY background='#CAEFFD' style="background-color: #CAEFFD">
		<div id="calend"
			style="position: static; visibility: hidden; top: 70; left: 300">
			<TABLE align="center" ID="calendar" cellspacing="0" cellpadding="0"
				style="font-size: 9pt">
				<THEAD>
					<TR>
						<TD colspan=7 ALIGN=CENTER>
							<table>
								<tr>
									<td>
										<a class="dgr" href="#" onClick="chgmonth(-1)">&lt&lt</a>
									</td>
									<td class="time" id="monthid">
										<SCRIPT LANGUAGE="JavaScript"> 
for (var intLoop = 0; intLoop < months.length; intLoop++)
if(today.month == intLoop){ 
document.write(months[intLoop]);
monthnum=intLoop
} 
</SCRIPT>
									</td>
									<td>
										<a class="dgr" href="#" onClick="javascript:chgmonth(1)">&gt&gt</a>
									</td>
									<td>
										<a class="dgr" href="#" onClick="javascript:chgyear(-1)">&lt&lt</a>
									</td>
									<td class="time" ID="yearid">
										<SCRIPT LANGUAGE="JavaScript"> 
document.write(today.year+"年")
yearnum=today.year 
</SCRIPT>
									</td>
									<td>
										<a class="dgr" href="#" onClick="javascript:chgyear(1)">&gt&gt</a>
									</td>
								</tr>
							</table>
						</TD>
					</TR>
					<TR CLASS="days" align="center">
						<SCRIPT LANGUAGE="JavaScript"> 
document.write("<TD class=satday>" + days[0] + "</TD>"); 
for (var intLoop = 1; intLoop < days.length-1; 
intLoop++) 
document.write("<TD>" + days[intLoop] + "</TD>"); 
document.write("<TD class=sunday>" + days[intLoop] + "</TD>"); 
</SCRIPT>
					</TR>
				</THEAD>
				<TBODY border=2 cellspacing="0" cellpadding="0" ID="dayList"
					ALIGN=CENTER ONCLICK="getDate()">
					<SCRIPT LANGUAGE="JavaScript"> 
for (var intWeeks = 0; intWeeks < 6; intWeeks++) { 
document.write("<TR style='cursor:hand'>"); 
for (var intDays = 0; intDays < days.length; 
intDays++) 
document.write("<TD></TD>"); 
document.write("</TR>"); 
} 
</SCRIPT>
				</TBODY>
			</TABLE>
			<table align="center">
				<tr>
					<td class="time" align="center" colspan="2" id="sj">
					</td>
				</tr>
				<tr>
					<td class="time" align="center" colspan="2">
						<select onChange="thour=this.options[this.selectedIndex].text">
							<script>
for(i=0;i<24;i++){
if(i==today.hour)
	document.write("<option selected>")
else
	document.write("<option>")
document.write(i)
document.write("</option>")}
</script>
						</select>
						时
						<select onChange="tmin=this.options[this.selectedIndex].text">
							<script>
for(i=0;i<60;i++){
if(i==today.minute)
	document.write("<option selected>")
else
	document.write("<option>")
if(i<10)
	document.write(i)
else
	document.write(i)
document.write("</option>")}
</script>
						</select>
						分
					</td>
				</tr>
				<tr align="center">
					<td>
						<a class="dgr" href="#" onClick="getback();">确定</a>
					</td>
					<td>
						<a class="dgr" href="#" onClick="closetime();">取消</a>
					</td>
					<td>
						<a class="dgr" href="#" onClick="cleartime();">清空</a>
					</td>
				</tr>
			</table>
		</div>
	</BODY>
</html>
