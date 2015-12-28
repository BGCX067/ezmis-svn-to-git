var oExcel; // 父窗口的Excel对象
var box = null;
var bFull = false; // 是否全屏状态

function onload() {
	var url = null;

	var param = {
		bbbm : bblx
	};
	AjaxRequest_Sync(link11, param, function(req) {
			var responseText = req.responseText;
			var responseObject = Ext.util.JSON.decode(responseText);
			if (responseObject.success) {
				$("curDate").value = responseObject.curDate;
				var datas = responseObject.data;
				for(var i =0;i<datas.length;i++){
					var key = datas[i].vname+"."+datas[i].fname;
					if(document.getElementById(key)){
					   document.getElementById(key).innerHTML=datas[i].value;
					}
				}
				//TANGER_OCX_OBJ.ActiveDocument.Sheets(1).Cells(1,1).Click();
			}
		});
		
	
}
function onResize() {
	var size = document.getElementById("viewSize").value;
	oExcel = document.TANGER_OCX;
	with (oExcel.ActiveDocument) {
		// 应用程序活动窗口 比例70%显示
		if (Application.ActiveWindow) {
			Application.ActiveWindow.Zoom = size;
		}
	}
}
function subForm() {
	oExcel.Activate(false);
	TANGER_OCX_SaveDoc();
	window.returnValue = "true";
	window.close();
}

function cancel() {
	window.close();
}

// 查询
function reFindData() {
	var curDate = $("curDate").value;
	var arys = curDate.split('-');
	var staDate = new Date(arys[0],parseInt(arys[1]-1),arys[2]);   
	var myDate = new Date();
	//var nowDate = myDate.getYear()+"-"+(myDate.getMonth()+1)+"-"+myDate.getDate();
	if(staDate>=myDate){
		alert("暂无"+curDate+"发布的报表");
		return;
	}else{
	 	var param = {
		bbbm : bblx,
		curDt : curDate
		};
		AjaxRequest_Sync(link11, param, function(req) {
				var responseText = req.responseText;
				var responseObject = Ext.util.JSON.decode(responseText);
				if (responseObject.success) {
					var datas = responseObject.data;
					for(var i =0;i<datas.length;i++){
						var key = datas[i].vname+"."+datas[i].fname;
						if(document.getElementById(key)){
						   document.getElementById(key).innerHTML=datas[i].value;
						}
					}
					//TANGER_OCX_OBJ.ActiveDocument.Sheets(1).Cells(1,1).Click();
				} else {
					alert("暂无" + curDate + "发布的报表");
				}
			});
	}
	
}

function submitTopDay() {// 上一天
	var curDate = $("curDate").value;
	d = new Date(Date.parse(curDate.replace(/-/g, '/')));
	d.setDate(d.getDate() - 1);
	t = [d.getYear(), d.getMonth() + 1, d.getDate()];
	$("curDate").value = t.join('-');
	reFindData();
}

function submitNextDay(date) {// 下一天
	var curDate = $("curDate").value;
	d = new Date(Date.parse(curDate.replace(/-/g, '/')));
	d.setDate(d.getDate() + 1);
	t = [d.getYear(), d.getMonth() + 1, d.getDate()];
	var myDate = new Date();
	var nowDate = myDate.getYear()+"-"+(myDate.getMonth()+1)+"-"+myDate.getDate();
	if(t.join('-')>=nowDate){
		alert("暂无"+t.join('-')+"发布的报表");
		return;
	}else{
		$("curDate").value = t.join('-');
		reFindData();
	}
	
}

function submitSx(){
	
	var size = document.getElementById("viewSize").value;
	oExcel = document.TANGER_OCX;
	with (oExcel.ActiveDocument) {
		// 应用程序活动窗口 比例70%显示
		if (Application.ActiveWindow) {
			Application.ActiveWindow.Zoom = size;
		}
	}
	//setTimeout("reSize()", 1000);  //希望在这里暂停2秒
	SetCookie(bblx,size,100);
}
function SetCookie(name,value,expires) 
{ 
	var expDays = expires*24*60*60*1000; 
	var expDate = new Date(); 
	expDate.setTime(expDate.getTime()+expDays);
	var expString = ((expires==null) ? "" : (";expires="+expDate.toGMTString()))  
	document.cookie = name + "=" + value + expString; 
} 

