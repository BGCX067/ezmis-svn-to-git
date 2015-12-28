var oExcel; // 父窗口的Excel对象
var bFull = false; // 是否全屏状态
var defColumnList = null; // 定义数据项列表,关联表的所有字段
var oDiXml = null; // 数据项列表xml dom对象
var oRecordJson = {}; // 业务数据记录json数据对象

// 可编辑域 取父亲框架中定义的editableInputs 结构为
/*
 * [{"id":"8a8a8a892311d12e0123124c80190144","value":"",
 * "variable":{"id":"8a8a8a892311d12e0123124c80180134", "name_cn":"工作条件",
 * "storemode":"覆盖", "name":"WorkContition", "value":"", "type":"普通字符串",
 * "dispalyStyle":"", "kind":"表单变量"} }]
 */
var editableInputs = null;
if (window.parent != null) {
	if (window.parent.editableInputs != null) {
		editableInputs = window.parent.editableInputs;
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
		if (editableInputs[i].variable.name == fd) {
			return true;
		}
	}
	return false;
}

/**
 * 当页面加载完成后调用
 */
function onload() {
	var url = null;
	if (cformId != "") {
		url = contextPath
				+ "/jteap/form/cform/CFormAction!readExcelBlobAction.do?id="
				+ cformId;
	}

	TANGER_OCX_OpenDoc(url);

	ntkoDiv.style.display = "block";;
	splash.style.display = "none";
	oExcel = document.TANGER_OCX;
	oDiXml = getDom($("excelDataItemXml").value);
	
	var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在加载设计，请稍候..."});
	myMask.show();
	
	Excel_Bianli();
	
	myMask.hide();

}
/**
 * 保存表单
 */
function saveForm() {
	var ret = Excel_SaveBianli();
	if (!ret)
		return;
	$("recordJson").value = Object.toJSON(oRecordJson);
	$("myForm").submit();
}
/**
 * 保存表单
 * bValidate 是否需要验证必填项
 */
function saveFormInAjax(bValidate ) {
	var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在保存，请稍候..."});
	myMask.show();
	if(typeof bValidate != 'undefined' && bValidate == true){
		if(!Excel_Validate()){
			myMask.hide();
			return "";
		}
	}
	Excel_SaveBianli();
	$("recordJson").value = Object.toJSON(oRecordJson);
	var url = contextPath
			+ "/jteap/form/cform/CFormAction!saveOrUpdateExcelCFormRecInAjaxAction.do";
	var params = Form.serialize($("myForm"));
	var docid = "";
	var myAjax = new Ajax.Request(url, {
		method : 'post',
		parameters : params,
		asynchronous : false,// 同步调用
			onComplete : function(req) {
				var responseText = req.responseText;
				var responseObj = responseText.evalJSON();
				if (responseObj.success == true) {
					docid = responseObj.docid;
					if(responseObj.data!=null)
						$("recordJson").value = Object.toJSON(responseObj.data);
				}
			},
			onFailure : function(e) {
				alert("数据保存失败：" + e);

			}
		});
	myMask.hide();
	return docid;
}
function showMask(){
	var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"Please wait..."});
myMask.show();
}
/**
 * 验证
 */
function Excel_Validate(){
	var diList = oDiXml.documentElement.childNodes;
	var errMsg = "";
	with (oExcel.ActiveDocument.Sheets(1)) {
		for (var i = 0;i < diList.length; i++) {
			var di = diList[i];
			var fd = getXmlAttribute(di, "fd");
			// 不处理不可编辑的数据项
			if (!isInEditableInputs(fd)) {
				continue;
			}
			var ce = getXmlAttribute(di, "ce");
			var cp = getXmlAttribute(di, "cp");
			var iRow = parseInt(ce.substring(0, ce.indexOf(":")));
			var iCol = parseInt(ce.substring(ce.indexOf(":") + 1));
			var oCell = Cells(iRow, iCol);

			var mt = getXmlAttribute(di, "mt");
			var value = ((typeof oCell.value == 'undefined') ? "" : oCell.value
					+ "");
			if (mt == '01' && (value == null || value == '' || (value.indexOf("【")>=0 && value.indexOf("】")>=0))) {
				errMsg += "字段【" + cp + "】为必填项，不能为空\r\n";
				continue;
			}
		}
	}
	if (errMsg == "")
		return true;
	else {
		alert(errMsg);
		return false;
	}
}


/**
 * 保存是遍历Excel单元格，将单元格的值对应到json对象中去
 */
function Excel_SaveBianli() {
	TANGER_OCX_SetReadOnly(false);
	var oXml = oDiXml;
	var diList = oXml.documentElement.childNodes;
	with (oExcel.ActiveDocument.Sheets(1)) {
		for (var i = 0;i < diList.length; i++) {
			var di = diList[i];
			var fd = getXmlAttribute(di, "fd");
			var cp = getXmlAttribute(di, "cp");
			// 不处理不可编辑的数据项
			if (!isInEditableInputs(fd)) {
				continue;
			}

			var ce = getXmlAttribute(di, "ce");
			var iRow = parseInt(ce.substring(0, ce.indexOf(":")));
			var iCol = parseInt(ce.substring(ce.indexOf(":") + 1));
			var oCell = Cells(iRow, iCol);

			var mt = getXmlAttribute(di, "mt");
			var value = ((typeof oCell.value == 'undefined') ? "" : oCell.value
					+ "");

			// tp: type 数据类型
			// 普通字符串01、日期域02、签名日期域03、意见签名域04、不用验证签名域05、需要验证签名域06、数字07
			var tp = getXmlAttribute(di, "tp");
			var fm = getXmlAttribute(di, "fm");

			if (tp == '02' || tp == '03') { // 日期型数据,需要处理
				if(fm == "")
				   fm = "yyyy年MM月dd日 HH时mm分ss秒";
				time = getDateFromFormat(value, fm);
				if (eval("oRecordJson." + fd.toUpperCase() + "==null")) {
					eval("oRecordJson." + fd.toUpperCase() + "={}");
				}

				eval("oRecordJson." + fd.toUpperCase() + ".time=" + time);
			} else {
				if (value == null || value == '') {
					eval("oRecordJson." + fd.toUpperCase() + "=null");
				} else {
					if (tp != '07') {// 数字域 没有引号
						value = "'" + encodeChar(value) + "'";
					} else {
						value = encodeChar(value);
					}
					eval("oRecordJson." + fd.toUpperCase() + "=" + value + "");
				}
			}
		}
	}

	TANGER_OCX_SetReadOnly(true);
}

/**
 * 初始化遍历单元格
 */
function Excel_Bianli() {
	TANGER_OCX_SetReadOnly(false);
	var oXml = oDiXml;
	var diList = oXml.documentElement.childNodes;
	with (oExcel.ActiveDocument.Sheets(1)) {
		var xx = new Array();
		var totalRange = Cells(1, 1);
		for (var i = 0;i < diList.length; i++) {
			var di = diList[i];
			// tp: type 数据类型
			// 普通字符串01、日期域02、签名日期域03、意见签名域04、不用验证签名域05、需要验证签名域06、数字07
			var tp = getXmlAttribute(di, "tp");
			var cp = getXmlAttribute(di, "cp");
			var fd = getXmlAttribute(di, "fd"); // 字段名
			var fm = getXmlAttribute(di, "fm"); // 显示格式
			var dv = getXmlAttribute(di, "dv"); // 默认值
			var evt = getXmlAttribute(di, "evt"); // 默认值
			var ev =getXmlAttribute(di,"ev");
			var vl =getXmlAttribute(di,"vl");
			var mt = getXmlAttribute(di, "mt"); // 必填项
			var ce = getXmlAttribute(di, "ce"); // 单元格 ce=行号:列号
			var ip = getXmlAttribute(di, "ip"); // 是否允许键盘输入 01允许 02不允许
			var iRow = parseInt(ce.substring(0, ce.indexOf(":")));
			var iCol = parseInt(ce.substring(ce.indexOf(":") + 1));
			var oCell = Cells(iRow, iCol);
			
			
		
				
			evalEvent(evt,di);
			
			
			// 取对应单元格的值
			var recordValue = eval("oRecordJson." + fd.toUpperCase());

			if (recordValue != null && typeof recordValue == 'string') {
				recordValue = decodeChar(recordValue);
			}
			// 有值就设置值，如果没有值，则设置默认值
			if (recordValue != null && recordValue != '') {
				if (tp == '02' || tp == '03') {// 日期型数据，需要格式化
					var dt = new Date(recordValue.time);
					try {
						if(fm == "")
				  			fm = "yyyy年MM月dd日 HH时mm分ss秒";
						recordValue = formatDate(dt, fm);
					} catch (ex) {
						alert("格式化日期时出错：" + ex.description)
					}
				}
			} else {
				if (dv != null && dv != '') {
					recordValue = getDefaultValue(dv, di);
				}
			}
			
			oCell.value = recordValue;
			
			// 如果不在可编辑的域里面则继续
			if (!isInEditableInputs(fd))
				continue;
	
				
				// 显示之前进行计算
			var cf_sh = getXmlAttribute(di, "cf_sh"); // 单元格 ce=行号:列号
			if (cf_sh != null && cf_sh != "") {
				recordValue = calculate_show(cf_sh, recordValue);
			}
			oCell.value = recordValue;
			
			
			//2.设置备选数据
			if(ev!=""){
				if(ev.indexOf("@SQL(") >= 0){
					
				}else{
					Excel_SetSelectDataList(oExcel,1,iRow,iCol,ev);
				}
			} else {
				//3.设置验证规则
				if(vl!=""){
					Excel_SetValidate(oExcel,1,iRow,iCol,vl,tp);
				}				
			}
			
			oCell.select();
			if(tp == "08"){	//如果是计算域
				//oCell.select();
				oExcel.ActiveDocument.Application.Selection.Interior.Color = 16777164;
				continue;
			}
			
			if(ip == "02"){	//如果是不允许键盘输入的字段
				//oCell.select();
				oExcel.ActiveDocument.Application.Selection.Interior.Color = 16183537;
				continue;
			}
			
			if (fd == "显示型字段") {
				oExcel.ActiveDocument.Application.Selection.Interior.Color = 13434828;
				continue;
			}
			//alert(cp);
			//oCell.select();
			oExcel.ActiveDocument.Application.Selection.Locked = false;
			oExcel.ActiveDocument.Application.Selection.Interior.Color = 16183537;
			

			// 将当前单元格加入可编辑单元格的Range
			/*if (i == 0) {
				totalRange = oCell;
			} else {
				totalRange = oExcel.ActiveDocument.Application.Union(
						totalRange, oCell);
			}*/
		}
		// 针对可编辑单元格的集合进行统一变色，解锁等操作
		//totalRange.Select();
		
		Cells(1, 1).Select();
	}
	TANGER_OCX_SetReadOnly(true);
	return true;
}
//设置单元格的可选数据列表
function Excel_SetSelectDataList(oExcel,iSheetIndex,row,col,sDataList){
	with(oExcel.ActiveDocument.Sheets(iSheetIndex)){
		with(Cells(row,col)){			
			Validation.Add(3,3,1,sDataList);
			Validation.ErrorMessage = "注意：您输入一个不在备选数据中的数据";
		}
	}
}

//设置单元格的验证方式
function Excel_SetValidate(oExcel,iSheetIndex,row,col,sValidateString,tp){
	//可能的情况情况
	//小于<
	//大于>
	//等于=
	//介于< <		
	sValidateString=decodeXml(sValidateString);
	var param1="";
	var param2="";
	var iFlag=0;     //比较方式  xlBetween = 1,        xlNotBetween = 2,        xlEqual = 3,        xlNotEqual = 4,        xlGreater = 5,        xlLess = 6,        xlGreaterEqual = 7,        xlLessEqual = 8
	var sTmpStr=sValidateString;	
	var validateType = ((tp == '07')?2:6);     //xlValidateTextLength:6  xlValidateDecimal：2
	var sTmpRule=((tp == '07')?"":"长度");
	
	if(sTmpStr.indexOf("<")>=0){
		if(sTmpStr.substring(sTmpStr.indexOf("<")+1).indexOf("<")>=0){//看是不是有两个<号
			//1.介于
			param1=sTmpStr.substring(0,sTmpStr.indexOf("<"));
			param2=sTmpStr.substring(sTmpStr.lastIndexOf("<")+1);			
			sTmpRule+="介于"+param1+"与"+param2+"之间";
			iFlag=1;
		}else{
			//6.小于
			param1=sTmpStr.substring(sTmpStr.indexOf("<")+1);
			sTmpRule+="小于"+param1;
			iFlag=6;
		}
	}else if(sTmpStr.indexOf(">")>=0){		
		//5.大于
		param1=sTmpStr.substring(sTmpStr.indexOf(">")+1);
		sTmpRule+="大于"+param1;
		iFlag=5;
	}else if(sTmpStr.indexOf("=")>=0){
		//3.等于
		param1=sTmpStr.substring(sTmpStr.indexOf("=")+1);
		sTmpRule+="等于"+param1;
		iFlag=3;
	}	
	with(oExcel.ActiveDocument.Sheets(iSheetIndex)){
		with(Cells(row,col)){
			Validation.Add(validateType,1,iFlag,param1,param2);
			Validation.ErrorMessage = "注意：当前数据应该"+sTmpRule+"\r\n您输入的数据不符合要求";
		}
	}
}

function evalEvent(evt,oDi){
	if(evt!=null && evt!=""){
		evt = decodeChars(getXmlAttribute(oDi, "evt"),"',<,>,&,\"");
		window.execScript(evt);
	}
}

/**
 * 显示时计算
 */
function calculate_show(cf, value) {
	var url = contextPath + "/jteap/form/cform/CFormAction!calcFormulaShowAction.do";

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
 * 为指定的单元格设置默认值 dv规则： 1.不包含任何${}表达式的默认值 2.${gh} ${sysdt(yyyy-MM-dd hh:mm:ss)}
 * ${UUID32} ${sNo(####)} 3.${ID(IDNAME)} 配置文件中配置的编码定义
 * 
 * di: data item xml object <di ..../>
 */
function getDefaultValue(dv, di) {
	if (dv.indexOf('${') >= 0) {
		dv = getDefaultValue2(dv, di)
	}
	return dv;
}

/**
 * 取得默认值 dv规则： 1.不包含任何${}表达式的默认值 2.${gh} ${sysdt(yyyy-MM-dd hh:mm:ss)} ${UUID32}
 * ${sNo(####)} 3.${ID(IDNAME)} 配置文件中配置的编码定义
 */
function getDefaultValue2(dv, di) {

	var tn = getXmlAttribute(oDiXml.documentElement, "tn");
	var fd = getXmlAttribute(di, "fd");
	var url = contextPath
			+ "/jteap/form/cform/CFormAction!defaultValueAction.do?dv=" + dv
			+ "&tn=" + tn + "&fd=" + fd;
	var result = "";
	try {
		var ajax = new Ajax.Request(url, {
			method : 'post',
			asynchronous : false
		});
		var responseText = ajax.transport.responseText;

		var responseObject = responseText.evalJSON();
		if (responseObject.success) {
			result = responseObject.value;
		}
	} catch (ex) {
	}
	return result;
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
 * 双击单元格事件 返回值boolean 是否取消单元格的双击事件 不允许单元格处于编辑状态
 */
function onCellDbClick(row, col) {
	var xpath = "//di[@ce='" + row + ":" + col + "']";
	var oDi = oDiXml.selectSingleNode(xpath);
	if (oDi != null) {
		var tp = getXmlAttribute(oDi, "tp");
		var fm = getXmlAttribute(oDi, "fm");
		var fd = getXmlAttribute(oDi, "fd");
		var ev = getXmlAttribute(oDi, "ev");
		var st = getXmlAttribute(oDi, "st");
		if(!isInEditableInputs(fd)) return true; //如果不在当前可编辑域内,取消该事件
		// tp: type 数据类型 普通字符串01、日期域02、签名日期域03、意见签名域04、不用验证签名域05、需要验证签名域06、数字07
		//日期域
		if (tp == "02") {
			var sDt = showdate();
			if (typeof sDt != 'undefined') {
				var dt = "";
				if(sDt!="cleartime"){
					dt = new Date(getDateFromFormat(sDt,"yyyy年MM月dd日 HH时mm分ss秒"));
					if (fm != null && fm != '') {
						dt = formatDate(dt, fm);
					} else {
						dt = sDt;
					}
				}
				setCellValue(row,col,dt);
			}
			return true;
		}
		
		//需验证签名域
		if(tp == "06"){
			var person = showSignWindow();
			if(person!=null){
				// 保存方式
				if (st == "追加") {
					// 取对应单元格的值
					var recordValue = eval("oRecordJson." + fd.toUpperCase());
					
					// 追加时，将新签名人追加到原来签名人后面，用","隔开。
					if (recordValue != null && recordValue != '') {
						recordValue = decodeChar(recordValue);
						setCellValue(row,col,recordValue + "," + person.userName);
					} else {
						setCellValue(row,col,person.userName);
					}
				} else {
					// 覆盖时，直接将该签名人设置到EXCEL单元格中。
					setCellValue(row,col,person.userName);
				}
				
				
				fireSignEvent(fd,oDi,person);
				//var evt = decodeChars(getXmlAttribute(oDi, "evt"),"',<,>,&,\"");
				//eval(evt);
			}
			return true;
		}
		
		//select data
		
		if(ev.indexOf("@SQL(")>=0){
			ev = decodeChars(ev,"',<,>,&,\"");
			var sql = ev.substring(ev.indexOf("\"")+1,ev.lastIndexOf("\""));
			var record = showSelectWindow(sql);
			if(record!=null)
				fireSelectDataEvent(fd,oDi,record);
			return true;
		}
		
	}
	return false;
}

function showSelectWindow(sql){
	url = "excelFormSelectData.jsp";
	var record = showModule2(url, "yes",500, 380, {sql:sql});
	return record;
}
/**
 * 触发签名事件
 */
function fireSignEvent(fd,di,person){
	var func = null;
	try{
		func = eval(fd+"_evt_sign");
	}catch(e){}
	if(typeof func == "function"){
		func(di,person);
	}
}

/**
 * 触发选择数据事件
 */
function fireSelectDataEvent(fd,di,record){
	
	var func = null;
	try{
		func = eval(fd+"_evt_SelectData");
	}catch(e){}
	if(typeof func == "function"){
		func(di,record);
	}
}
/**
 * 根据字段名向单元格设置数据
 */
function FillField(fd,value){
	var xpath = "//di[@fd='" + fd + "']";
	var oDi = oDiXml.selectSingleNode(xpath);
	if(oDi!=null){
		var ce = getXmlAttribute(oDi, "ce");
		var tp = getXmlAttribute(oDi, "tp");
		var fm = getXmlAttribute(oDi, "fm");
		var row=parseInt(ce.substring(0,ce.indexOf(":")));
		var col=parseInt(ce.substring(ce.indexOf(":")+1));
		//日期域,需要格式化
		if(tp == "02"){
			if(typeof value == "number"){
				value = new Date(value);
			}
			value = formatDate(value,fm);
		}
		setCellValue(row,col,value);
	}
	
}
function setCellValue(row,col,value){
	TANGER_OCX_SetReadOnly(false);
	oExcel.ActiveDocument.Sheets(1).Cells(row, col).value = value;
	TANGER_OCX_SetReadOnly(true);
}

/**
 * 显示签名窗口
 */
function showSignWindow(){
	url = "excelFormSign.jsp";
	var dt = showModule(url, "yes",300, 200, "");
	return dt;
}

/**
 * 将指定的值写到指定的单元格中
 */
function setExcelCellValue(oExcel, iSheetIndex, row, col, sValue) {
	with (oExcel.ActiveDocument.Sheets(iSheetIndex)) {
		Cells(row, col).value = sValue;
	}
}

/**
 * 选择日期
 */
function showdate() {
	url = "timeSelect.jsp";
	var dt = showModule(url, "yes", 250, 250, "");
	return dt;
}


























