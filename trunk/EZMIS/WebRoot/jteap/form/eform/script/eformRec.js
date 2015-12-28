var oDiXml = null;
var oRecordJson = null; // 业务数据记录json数据对象
var formTable = null;
var eTable = null;
var box = null;
var timer = null;
var rcMap = new Hash();	//行列对象map，用于存放做了数据项定义的td集合，以row，col作为key值，便于查找

// 获得可编辑区域
var editableInputs = null;
if (window.parent != null) {
	
	if (window.parent.editableInputs != null) {
		var tmpInputs = window.parent.editableInputs;
		editableInputs = [];
		
		for(var i=0;i<tmpInputs.length;i++){
			var item = tmpInputs[i];
			var field = {};
			field.cp = item.variable.name_cn;
			field.fd = item.variable.name;
			editableInputs.push(field);
		}
	}
}

/**
 * 根据字段名称判断该字段是否在可编辑域数组中存在，如果存在或者可编辑域是空 则返回true
 * 改表单可以不嵌入任何iframe的情况下，属于独立运行,在独立运行的情况下所有定义的域都是 可编辑的域
 */
function isInEditableInputs(fd) {
	if (editableInputs == null)
		return true;

	for (var i = 0;i < editableInputs.length; i++) {
		if (editableInputs[i].fd == fd) {
			return true;
		}
	}
	return false;
}

/**
 * 打开表单
 */
function OpenForm(formSn,docid){
	var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&docid="+docid
	window.open(url);
}

/**
 * 打开表单设计
 */
function eformDesign(formId){
	var url=contextPath+"/jteap/form/eform/eformEdit.jsp?id="+formId;
	result=showModule(url,true,800,600);
	if(result=="true"){
		var url = window.document.location+"";
		window.document.location = url;
	}
	
}
/**
 * 打印预览
 */
function openPrintForm(){
	var obj = {};
	window.parent.document.getElementById("eformFrame").document.getElementById("LYSQBH").value;
	if($("LYSQBH")){
		obj.sqbh = $("LYSQBH").value;
	}
	var url = window.document.location+"&st=02";
	var feature = "dialogWidth:720px;dialogHeight:600px;center:true;status:0;help:0;toolbar:1;resizable:yes;scroll:no";
	result=showModule2(url,true,720,600,obj,0,0,feature);
}


/**
 * 打开打印页面
function doPrintForm(){
	var ifdoc = $ifDocument("eformFrame");
	ifdoc.parentWindow.print();
}
 */
function doPrintForm(){
	var formSn = $("formSn").value;
	var docid = $('docid').value;
	var url = contextPath + "/jteap/form/eform/eformRecPrint.jsp?formSn="+formSn+"&docid="+docid;
	var fw = new $FW({
		url:url,
		width:800,
		height:600,
		resizable:true,
		id:docid
	});
	fw.show();
}
/**
 * 页面初始化
 */
function onload(){
	//参数开关
	document.body.attachEvent("onkeyup",function(e){
		if(e.ctrlKey == true){
			if(e.keyCode == 77){
				$("toolBar").toggle();
			}
		}
	});
	
	$("eformFrame").attachEvent("onload",onFormFrameLoaded);
	$("eformFrame").src=formUrl;
	if(editableInputs!=null){
		$("editableInputs").value =Object.toJSON(editableInputs);
	}
	//窗口大小该变后，需要修改EFormCntDiv的大小，使其同窗口大小一致
	window.attachEvent("onresize",function(){
		$("eformCnt").setStyle({width:(document.body.clientWidth),height:document.body.clientHeight});
	});
	//初始化的时候也需要调用一次调整EFormCntDiv大小
	
	// 调整EForm窗口大小
	adjustEFormWindowSize();
}

/**
 * 调整EForm窗口大小
 */
function adjustEFormWindowSize(){
	var width = parseInt($("formWidth").value);
	var height = parseInt($("formHeight").value);
	var screen_Width = window.screen.availWidth;
	var screen_Height = window.screen.availHeight;
	window.top.dialogWidth = width + "px";
    window.top.dialogHeight = height + "px"; 
	window.top.dialogLeft = (screen_Width - width)/2;
	window.top.dialogTop = (screen_Height - height)/2;
	window.top.resizeTo(parseInt($("formWidth").value),parseInt($("formHeight").value));
	window.top.moveTo((screen_Width - width)/2,(screen_Height - height)/2);
	$("eformCnt").setStyle({width:(document.body.clientWidth),height:document.body.clientHeight});	
}

/**
 * 初始化row col Map集合对象
 */
function initRCMap(){
	var ifdoc = $ifDocument("eformFrame");
	var tds = ifdoc.getElementsByTagName("td");
	for(var i=0;i<tds.length;i++){
		var html = tds[i].innerHTML.toLowerCase();
		if(html!='' && html.indexOf("{")>=0){
			if(html.indexOf("span")>=0){
				html = html.replace("<span style=\"display: none\">","");
				html = html.replace("</span>","");
			}
			rcMap.set(html,tds[i]);
		}
	}
}
/**
 * 初始化表单
 */
function initEForm(){
	var ifdoc = $ifDocument("eformFrame");
	//针对iframe的document 进行调整，以适应脚本环境的需要
	adjustIFrameDocument(ifdoc);
	//取得自定义表单中的Table对象
	var formTable = ifdoc.getElementsByTagName("table")[0];
	//try{
		//为自定义表单提交套上一个FORM对象
		formTable = createSubmitForm(formTable);
		//为IFRAME添加common.js库
		var url = [];
		url[0] = contextPath + "/script/prototype.js";
		url[1] = contextPath + "/script/date.js";
		url[2] = contextPath + "/script/adapter/ext/ext-base.js";
		url[3] = contextPath + "/script/ext-all-debug.js";
		url[4] = contextPath + "/script/ext-extend/form/Datetime/Datetime.js";
		url[5] = contextPath + "/script/ext-extend/form/TriggerField.js";
		url[6] = contextPath + "/script/ext-extend/form/ComboField.js"
		url[7] = contextPath + "/script/ext-extend/form/PopTextAreaField.js"
		url[8] = contextPath + "/script/common.js";
		url[9] = contextPath + "/jteap/form/eform/script/eformEditors.js";
		url[10] = contextPath + "/jteap/form/eform/script/eformIF.js";
		url[11] = contextPath + "/script/build/locale/ext-lang-zh_CN.js";

		$import_syn(url,ifdoc);
		$importCss(contextPath + "/resources/css/ext-all.css",ifdoc);
		$importCss(contextPath + "/script/ext-extend/form/Datetime/datetime.css",ifdoc);
		

		//初始化数据模型对象
		eval("oRecordJson = " + ($("recordJson").value==""?"null":$("recordJson").value));
		//根据数据项定义初始化表单中的各种控件
		oDiXml = getDom(document.getElementsByName("excelDataItemXml")[0].value);
		initRCMap();
		initFormUsingDi(oDiXml,eTable);
		//IFRAME高度宽度自适应
		$ifAutoFix("eformFrame");
		
		//执行页面初始化脚本
		ifdoc.parentWindow.eval($("onloadScript").value);
	//}catch(e){
	//	alert('自定义表单HTML文件未生成，请通过表单设计重新生成HTML文件');
	//}
	
	
	box.Close();
}

/**
 * 自定义表单加载完毕时初始化
 */
function onFormFrameLoaded(){
	box = new LightBox("idBox");
	box.Over = true;
	box.OverLay.Color = "#000";
	//box.OverLay.Opacity = 0;
	box.Center = true;
	box.Show();
	initEForm.delay();
}



/**
 * 针对iframe的document 进行调整，以适应脚本环境的需要
 * body中第一个节点不能是#text节点，否则在Ext组件中会出现ownerDocument.createRange() (Object doesn't support this property or method.)
 * 参考:http://zgqhyh.javaeye.com/blog/542981
 * 为iframe中初始化一些变量
 */
function adjustIFrameDocument(ifdoc){
	var firstNode =(ifdoc.body.firstChild);
	while(firstNode.nodeName == '#text'){
		firstNode.removeNode(true);
		firstNode = ifdoc.body.firstChild;
	}
	ifdoc.parentWindow.contextPath = contextPath;
	
	ifdoc.body.attachEvent("onkeyup",function(e){
		if(e.ctrlKey == true){
			if(e.keyCode == 77){
				//window.parent.$("toolBar").toggle();
				window.$("toolBar").toggle();
			}
		}
	});
	
	ifdoc.body.style.margin="0";
	
	
}

/**
 * 保存表单接口
 * flag = SAVE	保存成功后什么都不做
 *  | SAVE_CLOSE 保存成功后关闭
 *  | SAVE_REFRESH 保存成功后刷新
 *  | SAVE_NEW 	保存后新建
 */
function f_SaveForm(flag, is_Not_Valid_Null){
	var ifdoc = $ifDocument("eformFrame");
	var form = ifdoc.forms[0];
	editorsMap = ifdoc.parentWindow.editorsMap;
	editors = editorsMap.values();
	var bValid = true;
	for(var i=0;i<editors.length;i++){
		var et=editors[i];
		if (isInEditableInputs(et.name)) {
			var diList = oDiXml.documentElement.childNodes;
			var di = diList[i];
			var cp = getXmlAttribute(di, "cp")//字段中文名
			et.onBeforeSave();
			if(!et.validate(cp, is_Not_Valid_Null)){
				bValid = false;
				break;
			}
		}
	}
	
	if(bValid){
		var ret = form.fireEvent('onsubmit');
		if(ret && ret!=false){
			form.submit();
			box.Show();
			if(typeof flag == 'function') {
				timer = window.setInterval("monitor("+flag+")",1000);
			} else {
				timer = window.setInterval("monitor('"+flag+"')",1000);
			}
		}
		
	}
}

/**
 * 查询
 */
function f_SearchUniqueFormRec(condition){
	var url = contextPath + "/jteap/form/eform/EFormAction!eformRecSearchAction.do";
	var param = {condition:condition,formSn:$("formSn").value};
	AjaxRequest_Sync(url,param,function(ajax){
		var obj = ajax.responseText.evalJSON();
		if(obj.success==true){
			var docid = obj.docid;
			f_loadEFormRec(docid);
		}else{
			alert("没有找到符合条件的记录!");
		}
	})

}
/**
 * 根据docid加载对应的记录
 */
function f_loadEFormRec(docid){
	var formSn = $("formSn").value;
	var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&docid="+docid;
	document.location = url;
	
}

/**
 * 导出记录的Excel文档
 */
function f_exportRecExcel(){
	var formSn = $("formSn").value;
	var docid = $('docid').value;
	var url = contextPath + "/jteap/form/eform/EFormAction!eformRecExportEFormRecExcelAction.do?formSn="+formSn+"&docid="+docid;
	document.location = url;
}
/**
 * 创建新的表单记录
 */
function f_CreateNew(){
	var formSn = $("formSn").value;
	var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn="+formSn;
	document.location = url;
}

/**
 * 为自定义表单设置自定义的提交地址
 */
function f_SetAction(url){
	var ifdoc = $ifDocument("eformFrame");
	var form = ifdoc.forms[0];
	form.action = url;
}

/**
 * 提交自定义表单
 */
function submitForm(){
	f_SaveForm('SAVE_REFRESH');
}


/**
 * 监视保存结果
 * flag = SAVE	保存成功后什么都不做
 *  | SAVE_CLOSE 保存成功后关闭
 *  | SAVE_REFRESH 保存成功后刷新
 *  | SAVE_NEW 	保存后新建
 */
function monitor(flag){
	var iFrameSubmitTarget = $ifDocument("eformFrame").getElementsByName("submitTarget")[0];
	if(iFrameSubmitTarget.readyState=='complete'){
		var iFrameCnt = $ifContent(iFrameSubmitTarget);
		if(iFrameCnt != null && iFrameCnt!=''){
			window.clearInterval(timer);
			try{
			eval("var result ="+(iFrameCnt)+"");
			if(result.success == true){
				if(typeof flag == 'function'){
					box.Close();
					flag(result.docid);
				}else{
					alert('保存成功');
					box.Close();
					if(flag == 'SAVE_CLOSE'){
						window.returnValue = result.docid;
						window.close();
					}else if(flag == 'SAVE_REFRESH'){
						var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn="+$("formSn").value+"&docid="+result.docid
						document.location = url;
					}else if(flag == 'SAVE_NEW'){
						var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn="+$("formSn").value;
						document.location = url;
					}
				}
			}else{
				alert('保存失败：'+result.msg);
				box.Close();
			}
			}catch(ex){
				alert('保存失败');
				box.Close();
			}
		}
	}
}



/**
 * 为自定义表单套上一个FORM对象，并为这个FORM对象提供一个提交目标
 * 并返回重新定位的Table dom对象
 */
function createSubmitForm(formTable){
	var url = contextPath + "/jteap/form/eform/EFormAction!eformRecSaveOrUpdateAction.do";
	var tabelParentNode = formTable.parentNode;
	var html = "" +
			"<div style='display:none'>" +
				"<IFRAME name='submitTarget' src='' width='100%' height='100'></IFRAME>" +
			"</div>" +
			"<form id='eformForm' METHOD='POST' ENCTYPE='multipart/form-data' action='"+url+"' target='submitTarget'>" +
				"<div style='display:none' style='text-align:left'>" +
					"映射元数据:<input type='text' value='"+$("excelDataItemXml").value+"' name='excelDataItemXml'/><br/>" +
					"可编辑域:<input type='text' value='"+$("editableInputs").value+"' name='editableInputs'/><br/>" +
					"数据编号：<input type='text' value='"+$("docid").value+"' name='docid'/><br/>" +
					"表单编号：<input type='text' value='"+$("eformId").value+"' name='eformId'/><br/>" +
					"查询字符串：<input type='text' value='"+$("queryString").value+"' name='queryString'/><br/>" +
				"</div>" +
				tabelParentNode.innerHTML+
			"</form>";
	tabelParentNode.innerHTML = html;
	return tabelParentNode.childNodes[1].childNodes[1];
	//var doc = $ifDocument("eformFrame");
}


/**
 * 通过XML数据项定义，初始化表单
 */
function initFormUsingDi(oXml,eTable) {
	var sEncodeChars = "',<,>,&,\"";
	var diList = oXml.documentElement.childNodes;

	for (var i = 0;i < diList.length; i++) {
		var di = diList[i];
		
		//控件属性
		var edr = getXmlAttribute(di, "edr");//编辑控件
		var edr_pa = decodeChars(getXmlAttribute(di, "edr_pa"),sEncodeChars);//控件参数
		if(edr_pa == "") edr_pa = "{}";
		var paramObject = new String(edr_pa).evalJSON();
		
		var fd = getXmlAttribute(di, "fd"); // 字段名
		var cp = getXmlAttribute(di, "cp"); // 字段中文名称
		var ce = getXmlAttribute(di, "ce"); // 单元格 ce=行号:列号
		var nn = getXmlAttribute(di, "nn"); // 不能为空NOT NULL
		var unq = getXmlAttribute(di, "unq"); // 唯一性
		var vl = getXmlAttribute(di, "vl"); // 验证字符串 正则表达式
		var vltip = getXmlAttribute(di, "vltip"); // 验证提示
		var cf_sh = getXmlAttribute(di, "cf_sh"); // 显示时计算的计算公式
		
		var iRow = parseInt(ce.substring(0, ce.indexOf(":")));
		var iCol = parseInt(ce.substring(ce.indexOf(":") + 1));
		
		//定位单元格
		//var exFormCol = eTable.locate(iRow,iCol);
		
		var oTD = rcMap.get("{"+iRow+":"+iCol+"}");
		if(oTD==null)
			continue;
			
		var oRecordValue = {};
			//在对应的TD上渲染控件
		if(oRecordJson!=null){
			eval("oRecordValue = oRecordJson."+fd.toUpperCase());
			if(oRecordValue!=null){
				paramObject.value = oRecordValue;
				paramObject._value = oRecordValue;//由于value值有可能通过各种计算改变原有的值，所以保留一个value的副本命名为_value
			}
		}
		
		// 需要显示时计算
		if (cf_sh != null && cf_sh != "") {
			paramObject.value = calculate_show(cf_sh, oRecordValue);
		}
		
		
		if(paramObject.value){
			paramObject.value = evalCalcFormula(paramObject.value);
		}
		paramObject.nn = nn;		//NOT NULL
		paramObject.unq = unq;		//唯一性
		paramObject.vl = vl;		//验证
		paramObject.vltip = vltip;	//验证提示
		
		//有些控件中有可能会用到主表记录的id，比如子表控件
		paramObject.docid = $("docid").value;
		edr_pa = Object.toJSON(paramObject);
		
		var tmpStat = status;	//编辑状态
		if (!isInEditableInputs(fd)) {
			tmpStat = "02";//不可编辑状态
			if (edr == 'UPL') {
				tmpStat = "03";
			}
		}
		var formSn = $("txtSn").value;
		var docid = $("docid").value;
		
		//渲染控件
		renderEditor(oTD,edr,edr_pa,fd,tmpStat,cp,formSn,docid);
	}
		
	return true;
	
}

/**
 * 计算公式
 */
function evalCalcFormula(cf){
	var value = cf;
	var re=/\$|\#|@[a-zA-Z]*\(/ig;
	if(typeof cf == 'string' && cf.match(re)){
		var url = contextPath + "/jteap/form/eform/EFormAction!evalCalcFormulaAction.do?"+$("queryString").value;
		var param = {cf:cf};
		AjaxRequest_Sync(url,param,function(ajax){
			var responseObject =  ajax.responseText.evalJSON();
			if(responseObject.success==true){
				value = responseObject.ret;
			}
		})
	}
	return value;
}

/**
 * 显示时计算
 */
function calculate_show(cf, value) {
	var url = contextPath + "/jteap/form/eform/EFormAction!evalCalcFormulaAction.do?"+$("queryString").value;;

	var myAjax = new Ajax.Request(url, {
		method : 'post',
		parameters : {
			cf : cf,
			value : value,
			recordJson : $("recordJson").value
		},
		asynchronous : false,// 同步调用
			onComplete : function(req) {
				var responseText = req.responseText;
				var responseObj = responseText.evalJSON();
				if (responseObj.success == true) {
					value = responseObj.ret;
				} else {
					Ext.MessageBox.alert('Status', responseObj.msg);
				}
			},
			onFailure : function(e) {
				alert("计算公式失败：" + e);
			}
		});
	return value;
}

/**
 * 构建并渲染对应的控件
 * edr = "TXT|COMB...."
 */
function renderEditor(el,edr,edr_pa,fd,status,cp,formSn,docid){
	var doc = $ifDocument("eformFrame");
	doc.parentWindow.RenderEditor(el,edr,edr_pa,fd,status,cp,formSn,docid);
}

/**
 * 定稿
 */
function doFinalManuscript(id){
	var url=contextPath + "/jteap/form/eform/EFormAction!finalManuscriptAction.do";
	AjaxRequest_Sync(url,{id:id},function(ajax){
		var obj = ajax.responseText.evalJSON();
		if(obj.success==true){
			alert('定稿成功');
			document.location.reload();
		}else{
			alert('定稿失败');
		}
	})
	
}

/**
 * 验证EFORM表单的有效性
 */
function Eform_Validate() {
	var sEncodeChars = "',<,>,&,\"";
	var diList = oDiXml.documentElement.childNodes;
	var errMsg = "";

	for (var i = 0;i < diList.length; i++) {
		var di = diList[i];
		var fd = getXmlAttribute(di, "fd"); // 字段名

		// 不可编辑域不做验证
		if (!isInEditableInputs(fd)) {
			continue;
		}

		//控件属性
		var edr = getXmlAttribute(di, "edr");//编辑控件
		var edr_pa = decodeChars(getXmlAttribute(di, "edr_pa"),sEncodeChars);//控件参数
		if(edr_pa == "") edr_pa = "{}";
		var tp = getXmlAttribute(di, "tp")//字段类型
		var cp = getXmlAttribute(di, "cp")//字段中文名
		var paramObject = new String(edr_pa).evalJSON();
		
		var ce = getXmlAttribute(di, "ce"); // 单元格 ce=行号:列号
		var iRow = parseInt(ce.substring(0, ce.indexOf(":")));
		var iCol = parseInt(ce.substring(ce.indexOf(":") + 1));

		//定位单元格
		var exFormCol = eTable.locate(iRow,iCol);
        var value = exFormCol.dom.childNodes[0].value;
        
        // 普通控件，判断值是否为空
        if (edr != "UPL" && edr != "HTML"
        		&& (value == null || value == '')) {
			errMsg += "字段【" + cp + "】为必填项，不能为空\r\n";
			continue;
		}
		// 上传控件，判断是否有上传连接
		if (edr == "UPL") {
			if ((exFormCol.dom.childNodes[0].innerText == null || exFormCol.dom.childNodes[0].innerText == '')
					&& (value == null || value == ''))  {
				errMsg += "字段【" + cp + "】为必填项，不能为空\r\n";
				continue;
 			}
		}
		
		// HTML控件判断
		if (edr == "HTML") {
			var htmlId = exFormCol.dom.childNodes[2].id;
			var oEditor = window.eformFrame.FCKeditorAPI.GetInstance(htmlId);
			var data = oEditor.GetData();
			if (data == null || data == '') {
				errMsg += "字段【" + cp + "】为必填项，不能为空\r\n";
				continue;
 			}
		}

		//如果该区域是数字域，并且填的值不是数字
		if(tp == 'number' && isNaN(Number(value))){
			errMsg += "字段【" + cp + "】必须为数字\r\n";
			continue;
		}
	}
	
	if (errMsg == "")
		return true;
	else {
		alert(errMsg);
		return false;
	}
}

