var oExcel; // 父窗口的Excel对象
var box = null;
var bFull = false; // 是否全屏状态

function onload() {
	var url = null;
	box = new LightBox("idBox");
	box.Over = true;
	box.OverLay.Color = "#000";
	box.OverLay.Opacity = 50;
	box.Center = true;
	var param = {
		bblx : bblx
	};
	AjaxRequest_Sync(link11, param, function(req) {
			var responseText = req.responseText;
			var responseObject = Ext.util.JSON.decode(responseText);
			if (responseObject.success) {
				$("curDate").value = responseObject.curDate;
				var id = responseObject.bbid;
				// 只有增加的时候才生成数据,修改的时候的只是取数据
				url = contextPath + "/jteap/jhtj/bbzz/bbzzAction!readExcelBlobAction.do?id=" + id;
				TANGER_OCX_OpenDoc(url);
				//
				ntkoDiv.style.display = "block";
				splash.style.display = "none";
				oExcel = document.TANGER_OCX;
				oExcel.Activate(false);
				// 使批注显示小红图标
				setExcelConfig();
				// alert("--------");
				//
				TANGER_OCX_OBJ.SetReadOnly(false, "");
				// alert("--------1");
				box.Close();
				//TANGER_OCX_OBJ.ActiveDocument.Sheets(1).Cells(1,1).Click();
			}
		});
		
	// alert("--11");
	// TANGER_OCX_OBJ.ActiveDocument.Application.ActiveWindow.Zoom=50;
	// alert("--22");
	// oExcel.ActiveDocument.Sheets(1).Columns.AutoFit;
	// oExcel.ActiveDocument.Sheets(1).Rows.AutoFit;
	// oExcel.ActiveDocument.Application.Cells(1,1).Select();
	// TANGER_OCX_OBJ.ActiveDocument.Application.ActiveWorkbook.Names.Add("test","=Sheet1!B3");
	// TANGER_OCX_OBJ.ActiveDocument.Application.goto("test");
}
function onResize() {
	oExcel = document.TANGER_OCX;
	with (oExcel.ActiveDocument) {
		// 应用程序活动窗口 比例70%显示
		if (Application.ActiveWindow) {
			Application.ActiveWindow.Zoom = 85;
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
	var param = {
		bblx : bblx,
		curDt : curDate
	};
	box = new LightBox("idBox");
	box.Over = true;
	box.OverLay.Color = "#000";
	box.OverLay.Opacity = 50;
	box.Center = true;
	AjaxRequest_Sync(link11, param, function(req) {
				var responseText = req.responseText;
				var responseObject = Ext.util.JSON.decode(responseText);
				if (responseObject.success) {
					// $("curDate").value = responseObject.curDate;
					var id = responseObject.bbid;
					// 只有增加的时候才生成数据,修改的时候的只是取数据
					url = contextPath + "/jteap/jhtj/bbzz/bbzzAction!readExcelBlobAction.do?id=" + id;
					TANGER_OCX_OpenDoc(url);
					// 使批注显示小红图标
					TANGER_OCX_OBJ.SetReadOnly(true, "");
					box.Close();
					// 默认选中第一个单元格。。
					oExcel.ActiveDocument.Sheets(1).Cells(1, 1).Select();
					with (oExcel.ActiveDocument) {
						// 应用程序活动窗口 比例70%显示
						if (Application.ActiveWindow) {
							Application.ActiveWindow.Zoom = 85;
						}
					}
				} else {
					alert("暂无" + curDate + "发布的报表");
				}
			});
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
	$("curDate").value = t.join('-');
	reFindData();
}

function submitSx(){
	oExcel = document.TANGER_OCX;
	with (oExcel.ActiveDocument) {
		// 应用程序活动窗口 比例70%显示
		if (Application.ActiveWindow) {
			Application.ActiveWindow.Zoom = 85;
		}
	}
}