
var bFull=false;	//是否全屏状态

/**
 * 当页面加载完成后调用
 */
function onload() {
	var url = null;
	if (attachId != "") {
		url = contextPath + "/jteap/doclib/DoclibAttachAction!openAttach.do?docId="+docId+"&attachId="+attachId;
	
	}	
	TANGER_OCX_OpenDoc(url);
	ntkoDiv.style.display = "block";
	splash.style.display = "none";
}

 
/**
 * 全屏设计与非全屏设计之间切换
 */
function switchFullScreen() {
	if (bFull) {
		bFull = false;
		//$("formDiv").style.display = 'block';
		window.dialogHeight = '600px';
		window.dialogWidth = '800px';

		$("btnFull").value = "全屏设计";
	} else {
		bFull = true;
		var width = window.screen.availWidth;
		var height = window.screen.availHeight;
		//$("formDiv").style.display = 'none';
		TANGER_OCX_OBJ.focus();
		window.dialogHeight = height;
		window.dialogWidth = width;
		$("btnFull").value = "退出全屏";
	}
}
/**
 * 窗口发生变化时
 */
function windowResize() {

	if (bFull) {
		var height = parseInt(window.dialogHeight.replace("px", "")) - 70;
		$("ntkoDiv").style.height = height + "px";
	} else {
		$("ntkoDiv").style.height = "500px";
	}
}






/***
**保存
*
*/
function save(){
	if (bFull) {
		switchFullScreen();
	}
	//if (validateForm()) {
		var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在保存，请稍候..."});
		myMask.show();
		//ShowWait("生成设计中，请稍候....");
		//Excel_Bianli();
		
		//ShowWait("end");
		myMask.hide();
		TANGER_OCX_SaveDoc();
		window.returnValue = "true";
	//}
}

/**
*另存为新本版
*/
function saveLocalFile(){
	TANGER_OCX_OBJ.ShowDialog (3);
	
}


