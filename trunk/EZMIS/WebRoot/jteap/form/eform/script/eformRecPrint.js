var oExcel; // 父窗口的Excel对象
var bFull=false;	//是否全屏状态
var bPreview = false;	//是否是预览状态


function doPrintPreview(){
	if(bPreview == false){
		$("btnPreview").value='退出预览';
		$("btnPrint").hide();
		bPreview = true;
	}else{
		$("btnPreview").value='打印预览';
		$("btnPrint").show();
		bPreview = false;
	}
	TANGER_OCX_PrintPreview();
}
/**
 * 当页面加载完成后调用
 */
function onload() {
	if(formSn == "TB_WZ_GDZCDJ"){
		url = contextPath + "/jteap/wz/gdzc/GdzcdjAction!eformRecExportEFormRecExcelAction.do?formSn="+formSn+"&docid="+docid;
	}else{
		url = contextPath + "/jteap/form/eform/EFormAction!eformRecExportEFormRecExcelAction.do?formSn="+formSn+"&docid="+docid;
	}
	oExcel = document.TANGER_OCX;
	ntkoDiv.style.display = "block";
	splash.style.display = "none";
	if(typeof oExcel == 'undefined'){
		return;
	}
	TANGER_OCX_OpenDoc(url);
		with(oExcel.ActiveDocument.Sheets(1)){
		Cells(1,1).Select();
	}
}



/**
 * 显示mask
 */
function showSplash() {
	$("splash").style.display = "block";
	$("ntkoDiv").style.display = "none";
}

/**
 * 隐藏mask
 */
function hideSplash() {
	$("splash").style.display = "none";
	$("ntkoDiv").style.display = "block";
}
/**
 * 窗口发生变化时
 */
function windowResize() {
		var height = window.document.body.clientHeight - 50;
		$("ntkoDiv").style.height = height + "px";
}

/**
 * 参数开关
 */
function paramSwithToggle(){
	var xx = $("hideDiv").style.display;
	if(xx=="none"){
		$("hideDiv").style.display = "block";
	}else{
		$("hideDiv").style.display = "none";
	}
	
}



/**
 * 全屏设计与非全屏设计之间切换
 */
function switchFullScreen() {
	
	if (bFull) {
		bFull = false;
		window.resizeTo(800, 600);
		$("btnFull").value = "全屏设计";
	} else {
		bFull = true;
		var width = window.screen.availWidth;
		var height = window.screen.availHeight;
		TANGER_OCX_OBJ.focus();
		window.dialogHeight = height;
		window.dialogWidth = width;
		window.resizeTo(width, height);
		$("btnFull").value = "退出全屏";
	}
}



