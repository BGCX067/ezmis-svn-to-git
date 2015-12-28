var oExcel; // 父窗口的Excel对象
var bFull=false;	//是否全屏状态
var defColumnList=null;	//定义数据项列表,关联表的所有字段
var InputsArray = [];	//可编辑域对象集合


/**
 * 当页面加载完成后调用
 */
function onload() {
	var url = null;
	if (eformId != "") {
		url = contextPath + "/jteap/form/eform/EFormAction!readExcelBlobAction.do?id=" + eformId;
	}
	oExcel = document.TANGER_OCX;
	ntkoDiv.style.display = "block";
	splash.style.display = "none";
	if(typeof oExcel == 'undefined'){
		return;
	}
	TANGER_OCX_OpenDoc(url);
	oExcel = document.TANGER_OCX;
	//是否属于修改状态，如果是属于修改状态需要将锁定解开
	if($("id").value!=""){
		Excel_AttachComment();
	}
	//设置EXCEL保存为网页的编码为65001 - UTF-8
	oExcel.ActiveDocument.WebOptions.Encoding = 65001;
	
}



//对指定的Cell进行清除规则
function Excel_ResetCellRule(oExcel,iSheetIndex,row,col){
	with(oExcel.ActiveDocument.Sheets(iSheetIndex)){
		with(Cells(row,col)){			
			Validation.Delete();
		}
	}
}

//载入批注
function Excel_AttachComment(){
	TANGER_OCX_SetReadOnly(false);
	var oXml=getDom($("excelDataItemXml").value);
	if(oXml!=null){
		var size=oXml.firstChild.childNodes.length;
		for(var i=0;i<size;i++){
			try{
				var oNode=oXml.firstChild.childNodes[i];
				var sCell=getXmlAttribute(oNode,"ce");
				var row=parseInt(sCell.substring(0,sCell.indexOf(":")));
				var col=parseInt(sCell.substring(sCell.indexOf(":")+1));
				with(oExcel.ActiveDocument.Sheets(1)){
					Excel_ResetCellRule(oExcel,1,row,col);
					Cells(row,col).AddComment(oNode.xml);
					Cells(row,col).Value = "";
				}
			}catch(e){
				alert(e);
			}
		}
	}
}
/*
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

*/
//对单元格进行复位
function Excel_ResetCell(oExcel,iSheetIndex,iRow,iCol){

	with(oExcel.ActiveDocument.Sheets(iSheetIndex)){
		//设备背景色为无色填充
		Cells(iRow,iCol).Interior.ColorIndex=-4142;
		//清除批注
//		Cells(iRow,iCol).ClearComments();
	}
}
/**
 * 
 * 遍历所有批注,设置相关属性

 * cp:caption 标题
 * fd:field name字段名称
 * ce: cell 单元格   49,6
 * tp: type 数据类型  普通字符串01、日期域02、签名日期域03、意见签名域04、不用验证签名域05、需要验证签名域06、数字07
 * ctp: comput type 计算类型 创建时计算01 显示时计算02
 * cf : calculate formula 计算公式
 * mt :	must 是否必填项目  必填01 选填02
 * fm: fomat 格式化  适用于数字类型和日期类型，也可是${fdname}222${dt}类似字符串
 * st: save type 保存方式  追加01 覆盖02
 * ip: is allow input 是否允许键盘输入 允许01 不允许02
 * ev: enumValue 枚举值  是,否
 * vl: 验证规则  x>10 & x<100等
 * dv: default value 默认值  直接填写/通配符配置  
 * 		${personLoginName}				当前登录名称 可使用工号
		${sysdt(yyyy-MM-dd hh:mm:ss)}	当前时间
		${sNo(####)}					流水号
		${UUID32}						32位UUID
 * 
 * <root tn=''>
 * 	  	<di cp='' fd='' ce='' tp=''fm='' st='' ev='' vl='' dv=''/>
 * 		<di cp='' fd='' ce='' tp=''fm='' st='' ev='' vl='' dv=''/>
 * 		<di cp='' fd='' ce='' tp=''fm='' st='' ev='' vl='' dv=''/>
 * </root>
 * 
 * @param bClearCommon 遍历完毕之后是否清除批注 默认清除
 */
function Excel_Bianli(bClearCommon){
	if(typeof bClearCommon == 'undefined'){
		bClearCommon = true;
	}
	
	TANGER_OCX_SetReadOnly(false);	
	var oXml=getDom("<root></root>");
	var oRoot=oXml.firstChild;
	
	//添加 table name 属性
	setXmlAttribute(oRoot,"tn",$("defTableCode").value);//表编号
	setXmlAttribute(oRoot,"schema",$("schema").value);//表schema
	setXmlAttribute(oRoot,"sn",$("txtSn").value);//表单标识
	setXmlAttribute(oRoot,"width",$("txtWidth").value);//表单宽度
	setXmlAttribute(oRoot,"height",$("txtHeight").value);//表单高度
	var iSheetIndex=1;
	var maxRow = 1;
	var maxCol = 1;
	with(oExcel.ActiveDocument.Sheets(iSheetIndex)){
		var AllComments=Comments;
		var i;
		
		var size= AllComments.Count;
		for(var j=1;j<=size;j++){
			var oComment=AllComments.Item(j);
			
			var row=oComment.Parent.Row;
			var col=oComment.Parent.Column;
			var sComment="";
			//取得当前单元格的批注内容

			var cp="",tp="",fd="",st="",ev="",vl="",fm="",ce="";
			
			sComment=oComment.Text();
			var oRuleXml=getDom(sComment).firstChild;
			if(oRuleXml == null){
				alert('存在非法定义在'+row+'行'+col+'列');
				return false;
			}
			//得到对应的值
			cp=getXmlAttribute(oRuleXml,"cp");
			fd=getXmlAttribute(oRuleXml,"fd");
			tp=getXmlAttribute(oRuleXml,"tp");
			
			
			var ei = {'fd':fd,'cp':cp,'tp':tp}
			InputsArray.push(ei);
			
			//添加验证规则之前,先清除验证规则
			//Excel_ResetCellRule(oExcel,iSheetIndex,row,col);
			
			try{
				//1.数据项标题是必要项						
				if(cp!=""){
				}else{
					sErrMsg+="单元格["+row+","+col+"]批注中没有发现标题数据项(cp项)\r\n";
				}			
				if(cp!=""){
				}else{
					sErrMsg+="单元格["+row+","+col+"]批注中没有发现字段数据项(fd项)\r\n";
				}			
			}catch(ex){
			}
			setXmlAttribute(oRuleXml,"ce",row+":"+col);
			oRoot.appendChild(oRuleXml);
			
			
			Cells(row,col).Select();
			Cells(row,col).Value = "{"+row+":"+col+"}";
			oExcel.ActiveDocument.Application.Selection.Locked=true;
			if(row>maxRow) maxRow = row;
			if(col>maxCol) maxCol = col;
		}
		
		sXml=oXml.xml;
		$("excelDataItemXml").value=sXml;
		$("editableInputs").value = Object.toJSON(InputsArray);
		
		if(bClearCommon){
			Range(Cells(1,1),Cells(maxRow,maxCol)).ClearComments();
		}
	}
	//TANGER_OCX_SetReadOnly(true);	
	return true;
}
/**
 * 表单验证
 */
function validateForm() {
	if ($("txtTitle").value == "") {
		alert("表单标题必填项，不能为空");
		$('txtTitle').focus();
		return false;
	}
	
	if($("txtDefTableName").value==""){
		alert("关联表不能为空，请选择对应的关联表或者输入新定义的");
		$('txtDefTableName').focus();
		return false;
	}
	
	
	if($("txtSn").value==""){
		alert("表单标识不能为空");
		$('txtSn').focus();
		return false;
	}
	if(opMode != "Modify" || oldFormSn!=$("txtSn").value){
		var url = contextPath + "/jteap/form/eform/EFormAction!validateEFormSnUniqueAction.do";
		var unique = false;
		AjaxRequest_Sync(url,{formSn:$("txtSn").value},function(ajax){
			var ret = ajax.responseText.evalJSON();
			if(ret.success == true){
				unique = ret.unique;
			}
		});
		if(unique == false){
			alert("该表单标识已经存在，请重新制定表单标识");
			$("txtSn").focus();
			return false;
		}
	}
	
	return true;

}
/**
 * 导出EXCEL表单
 */
function exportExcelForm(){
	var title=$("txtTitle").value;
	if(title==""){
		alert("请先填标题");
		return ;
	}
	TANGER_OCX_OBJ.SaveToLocal("c:/"+title,true);
	alert("文件成功导出："+"c:/"+title+".xls");
}
/**
 * 导入已存在的excel文档作为表单,调用隐藏的file 域， 当选择的文件改变时，使用NTKO控件打开选中的文件
 */

function importExcelForm() {
	$("selectFile").click();
}

/**
 * 打开指定的文件
 */
function selectFileChanged() {
	var fileName = $("selectFile").value;
	var tmp = fileName.substr(fileName.lastIndexOf(".")).toUpperCase();
	if (tmp != '.XLS' && tmp != '.XLSX') {
		alert("不支持的文件类型,只能打开以XLS或XLSX后缀结尾的文件");
	} else {
		showSplash();
		TANGER_OCX_OBJ.OpenLocalFile(fileName);
		hideSplash();
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

	if (bFull) {
		var height = parseInt(window.dialogHeight.replace("px", "")) - 70;
		$("ntkoDiv").style.height = height + "px";
	} else {
		$("ntkoDiv").style.height = "460px";
	}
}

/**
 * 显示数据源定义
 */
function showDsDef() {
	var url = "excelFormDsDef.jsp";
	var result = showModule(url, true, 500, 300);
	if (result == "true") {

	}
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
 * 选择关联表
 */
function selectDefTable() {
	var url = "eformDefTableSelector.jsp";
	var result = showModule(url, true, 300, 500);
	if (result != null) {
		$("txtDefTableName").value = result.caption;
		$("defTableId").value = result.id;
		$("defTableCode").value = result.tableCode;
		$("txtSn").value = result.tableCode;
		$("txtTitle").value = result.tableName;
		window.defColumnList=null;
	}
}


/**
 * 全屏设计与非全屏设计之间切换
 */
function switchFullScreen() {
	
	if (bFull) {
		bFull = false;
		$("formDiv").style.display = 'block';
		window.dialogHeight = '600px';
		window.dialogWidth = '800px';

		$("btnFull").value = "全屏设计";
	} else {
		bFull = true;
		var width = window.screen.availWidth;
		var height = window.screen.availHeight;
		$("formDiv").style.display = 'none';
		TANGER_OCX_OBJ.focus();
		window.dialogHeight = height;
		window.dialogWidth = width;

		//window.resizeTo(width, height);
		$("btnFull").value = "退出全屏";
	}
		//TANGER_OCX_OBJ.FullScreenMode=true;
		//TANGER_OCX_OBJ.Activate();
}

/**
 * 创建表定义
 */
function createTable(tableCode,tableName){
	var url = contextPath+"/jteap/form/dbdef/DefTableInfoAction!saveUpdateReturnIdAction.do";
	var param = {tableCode:tableCode, tableName:tableName};
	AjaxRequest_Sync(url,param,function(ajax){
		var temp = ajax.responseText;
		eval("responseObj="+temp);
		if(responseObj.success == true){	
			defTableId = responseObj.id; //获得表ID
			$("defTableId").value = defTableId;//设值到父窗口对象
			alert("表 "+tableName+" 创建完成");
		}
	});
}

/**
 * 确定是否创建对应的表定义
 */
function confirmSaveDefTable(){
	
	if($("defTableId").value == ""){
		if(confirm("相关联的表定义不存在，是否需要创建对应的表定义?")){
			createTable($("txtDefTableName").value,$("txtDefTableName").value);
		}else{
			return false;
		}
	}
	return true;
}

/**
 * 提交表单,保存
 */
function submitForm() {
	if(bFull)
		switchFullScreen();
	if(!confirmSaveDefTable())
		return;
	if (validateForm()) {
		Excel_Bianli();
		TANGER_OCX_SaveDoc();
		window.returnValue = "true";
	}
}



/**
 * 提交表单,保存
 */
function submitHtmlForm() {
	if(bFull)
		switchFullScreen();
	if (validateForm()) {
		var htmlPath = document.all["excelHtmlPublishPath"].value;
		var fileName = $("txtSn").value+".html";
		fileName = htmlPath + "\\"+ fileName;
		oExcel.Activate(true);
		if(!confirmSaveDefTable())
			return;
	
		if(!Excel_Bianli())
			return;
		
		oExcel.Activate(false);
		//发布HTML文件
		TANGER_OCX_SaveAsHTMLToPublishPath(fileName);
		//保存文档并关闭窗口
		TANGER_OCX_SaveDoc();
		window.returnValue = "true";
	}
}

/**
 * 单元格双击事件
 */
function onCellDbClick(row, col) {
	if(!confirmSaveDefTable())
		return;
	var title=$("txtTitle").value;
	var formSn=$("txtSn").value;
	if(title==""){
		alert("请先填标题");
		$("txtTitle").focus();
		return ;
	}
	
	if(formSn == ""){
		alert("表单标识不能为空");
		$("formSn").focus();
		return;
	}
	var url = "eformDIForm.jsp?defTableId="+$("defTableId").value;
	var argObj={caption:$("txtDefTableName").value,title:$("txtTitle").value,defTableId:$("defTableId").value,row:row,col:col};
	argObj.formSn = $("txtSn").value;
	with(oExcel.ActiveDocument.ActiveSheet){
		var oCell = Cells(row,col);
		argObj.comment=_getCellCommonText(oCell);
	}
	//var ddd = oExcel.Data;
	//oExcel.Close();
	var result = showModule2(url, true, 650, 565,argObj);
	//oExcel.Data = ddd;
	with(oExcel.ActiveDocument.ActiveSheet){
		if(result!=null){
			var comment = _buildComment(result);
			var commentObj=Cells(row,col).Comment;
			if(commentObj!=null){
				Cells(row,col).ClearComments();
			}
			Cells(row,col).AddComment(comment);
		}
	}
	TANGER_OCX_OBJ.Activate();
	return true;
}
/**
 * 根据单元格配置对象构建对应的批注内容
 */
function _buildComment(oCellConfig){
	var sEncodeChars = "',<,>,&,\"";
	var common="<di " +
			" cp='"+encodeChars(oCellConfig.cp,sEncodeChars)+"'" +	//标题
			" fd='"+encodeChars(oCellConfig.fd,sEncodeChars)+"'" +	//字段名称
			//" ce='"+oCellConfig.row+":"+oCellConfig.col+"'" +			//单元格
			" tp='"+encodeChars(oCellConfig.tp,sEncodeChars)+"'" +	//数据类型
			" edr='"+encodeChars(oCellConfig.edr,sEncodeChars)+"'" +	//编辑器
			" nn='"+oCellConfig.nn+"'" +	//编辑器
			" unq='"+oCellConfig.unq+"'" +	//编辑器
			(oCellConfig.edr_pa.length>0?" edr_pa='"+encodeChars(oCellConfig.edr_pa,sEncodeChars)+"'":" ")+	//编辑器参数
			(oCellConfig.wid.length>0?" wid='"+encodeChars(oCellConfig.wid,sEncodeChars)+"'":" ")+	//宽度
			(oCellConfig.sort.length>0?" sort='"+encodeChars(oCellConfig.sort,sEncodeChars)+"'":" ")+	//排序
			(oCellConfig.vl.length>0?" vl='"+encodeChars(oCellConfig.vl,sEncodeChars)+"'":" ") +	//验证公式
		    (oCellConfig.vltip.length>0?" vltip='"+encodeChars(oCellConfig.vltip,sEncodeChars)+"'":" ")+	//验证提示
		    (oCellConfig.cf_sh.length>0?" cf_sh='"+encodeChars(oCellConfig.cf_sh,sEncodeChars)+"'":" ")+	//计算公式
		    (oCellConfig.cf_sa.length>0?" cf_sa='"+encodeChars(oCellConfig.cf_sa,sEncodeChars)+"'":" ")+	//计算公式
		    (oCellConfig.cf_cr.length>0?" cf_cr='"+encodeChars(oCellConfig.cf_cr,sEncodeChars)+"'":" ");	//计算公式
	common+=" />";
	return common;
}

/**
 * 取得指定单元格的批注内容
 */
function _getCellCommonText(oCell){
	var commentObj=oCell.Comment;
	var comment="";
	if(commentObj!=null)
		comment=commentObj.Text();
	return comment;
		
}

/**
 * 同步输入的表名
 */
function doSynTableName(){
	$("txtTitle").value = $("txtDefTableName").value;
	$("txtSn").value = $("txtDefTableName").value;
}

/**
 * 同步数据定义
 * 遍历所有定义的单元格，将数据项映射信息传递给服务器端进行字段同步
 */
function synchronizeDefTable(){
	if(!confirmSaveDefTable())
		return;
	oExcel.Activate(true);
	Excel_Bianli(false);
	oExcel.Activate(false);
	var url = contextPath + "/jteap/form/eform/EFormAction!synchronizeDefTableAction.do";
	var param = {excelDataItemXml:$("excelDataItemXml").value,defTableId:$("defTableId").value};
	AjaxRequest_Sync(url,param,function(ajax){
		var temp = ajax.responseText;
		eval("responseObj="+temp);
		if(responseObj.success == true){	
			alert('同步完成');
		}
	});
}

/**
 * 编辑初始化脚本
 */
function editInitInfo(){
	var url = "eformInitInfoForm.jsp";
	var param = {onloadScript:$("onloadScript").value};
	var result = $mw(url, true, 650, 565,param);
	if(result!=null && result.onloadScript)
		$("onloadScript").value = result.onloadScript;
}
/**
 * 加载表单模板
 */
function loadFormTemplate(){
	var url = contextPath + "/jteap/form/eform/eformTemplate.xls";
	TANGER_OCX_OpenDoc(url);
}
