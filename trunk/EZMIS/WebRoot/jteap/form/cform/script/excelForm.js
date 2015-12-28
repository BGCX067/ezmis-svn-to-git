var oExcel; // 父窗口的Excel对象
var bFull=false;	//是否全屏状态
var defColumnList=null;	//定义数据项列表,关联表的所有字段
var InputsArray = [];	//可编辑域对象集合




/**
 * 当页面加载完成后调用
 */
function onload() {
	var url = null;
	if (cformId != "") {
		url = contextPath
				+ "/jteap/form/cform/CFormAction!readExcelBlobAction.do?id=" + cformId;
	}else{
		var temp = new Date().format('ymdHis');
		$("defTableCode").value = 'TB_FORM_'+temp;
		$("txtDefTableName").value = '表单_'+temp;
	}
	TANGER_OCX_OpenDoc(url);
	ntkoDiv.style.display = "block";
	splash.style.display = "none";
	var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在载入设计，请稍候..."});
	myMask.show();
	oExcel = document.TANGER_OCX;
	//是否属于修改状态，如果是属于修改状态需要将锁定解开
	if($("id").value!=""){
		Excel_AttachComment();
	}
	myMask.hide();
	
	if(cformType != null && cformType!=""){
		$("selDimFormType").value = cformType;
	}
	//TANGER_OCX_OBJ.Activate();
}

/**
 * 提交表单,保存
 */
function submitForm() {
	if (bFull) {
		switchFullScreen();
	}
	if (validateForm()) {
		var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在保存，请稍候..."});
		myMask.show();
		//ShowWait("生成设计中，请稍候....");
		
		Excel_Bianli();
		
		//ShowWait("end");
		myMask.hide();
		TANGER_OCX_SaveDoc();
		window.returnValue = "true";
	}
}

/**
 * 提交表单,保存
 */
function submitHtmlForm() {
	oExcel.Activate(true);
	if (bFull) {
		switchFullScreen();
	}
	if (validateForm()) {
		var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在保存，请稍候..."});
		myMask.show();
		
		Excel_Bianli();
		myMask.hide();
		oExcel.Activate(false);
		//发布HTML文件
		TANGER_OCX_SaveAsHTMLToPublishPath();
		//保存文档并关闭窗口
		TANGER_OCX_SaveDoc();
		window.returnValue = "true";
	}
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
 * 
 */
function Excel_Bianli(){
	
	TANGER_OCX_SetReadOnly(false);	
	var oXml=getDom("<root></root>");
	var oRoot=oXml.firstChild;
	
	//添加 table name 属性
	setXmlAttribute(oRoot,"tn",$("defTableCode").value);//表编号
	setXmlAttribute(oRoot,"schema",schema);//表schema
	setXmlAttribute(oRoot,"tdict",$("txtDictId").value);//关联字典
	setXmlAttribute(oRoot,"tcell",$("txtRowZbCell").value);//行指标开始单元格编号
	setXmlAttribute(oRoot,"tp",$("selDimFormType").value);//表单类型，普通，二维，二维动态
	/*
	setXmlAttribute(oRoot,"tdc","down");//  填充方向
	**/
	var iSheetIndex=1;
	with(oExcel.ActiveDocument.Sheets(iSheetIndex)){
		var AllComments=Comments;
		var i;
		
		var oComment=null;
		try{
			oComment=AllComments.Item(1);
		}catch(ex){
		}
		
		while(oComment!=null){		
			
			var row=oComment.Parent.Row;
			var col=oComment.Parent.Column;
			
			var sComment="";
			//取得当前单元格的批注内容

			var cp="",tp="",fd="",st="",ev="",vl="",fm="",ce="";
			
			sComment=oComment.Text();
			var oRuleXml=getDom(sComment).firstChild;
			
			//得到对应的值
			cp=getXmlAttribute(oRuleXml,"cp");
			fd=getXmlAttribute(oRuleXml,"fd");
			st=getXmlAttribute(oRuleXml,"st");
			ev=getXmlAttribute(oRuleXml,"ev");
			vl=getXmlAttribute(oRuleXml,"vl");
			tp=getXmlAttribute(oRuleXml,"tp");
			ce=getXmlAttribute(oRuleXml,"ce");
			fm=getXmlAttribute(oRuleXml,"fm");
			
			
			
			var ei = {'fd':fd,'cp':cp,'tp':tp}
			InputsArray.push(ei);
			
			//添加验证规则之前,先清除验证规则
			Excel_ResetCellRule(oExcel,iSheetIndex,row,col);
			
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
			/*
			//2.设置备选数据
				if(ev!=""){
					Excel_SetSelectDataList(oExcel,iSheetIndex,row,col,ev);
				}
					
				//3.设置验证规则
				if(vl!=""){
					Excel_SetValidate(oExcel,iSheetIndex,row,col,vl,tp);
				}*/
				
			}catch(ex){
			}
			
			//最后对单元格进行清理
			Excel_ResetCell(oExcel,iSheetIndex,row,col);
			oRoot.appendChild(oRuleXml);
			
			Cells(row,col).ClearComments();
			Cells(row,col).Select();
			oExcel.ActiveDocument.Application.Selection.Locked=true;
			
			try{
				oComment=AllComments.Item(1);
			}catch(ex){
				oComment=null;
			}		
		}
		sXml=oXml.xml;
		$("excelDataItemXml").value=sXml;
		$("editableInputs").value = Object.toJSON(InputsArray);
	}
	TANGER_OCX_SetReadOnly(true);	
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
	return true;

}
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

function selectDefTable() {
	var url = "excelFormDefTableSelector.jsp";
	var result = showModule(url, true, 300, 500);
	if (result != null) {
		$("txtDefTableName").value = result.caption;
		$("defTableId").value = result.id;
		$("defTableCode").value = result.tableCode;
		window.defColumnList=null;
	}
}

/** 创建表 */
function createTable(row, col){
	Ext.Ajax.request({
		url: contextPath+"/jteap/form/dbdef/DefTableInfoAction!saveUpdateReturnIdAction.do",
		method: 'POST',
		params:{tableCode:$("defTableCode").value, tableName:$("txtDefTableName").value},
		success: function(ajax){
			var temp = ajax.responseText;
			eval("responseObj="+temp);
			if(responseObj.success == true){	
				/** 获得表ID */
				defTableId = responseObj.id;
				/** 设值到父窗口对象 */
				$("defTableId").value = defTableId;
				alert("表 "+ $("txtDefTableName").value +" 已创建");
				
				/** 显示模式窗口 -- 字段定义 */
				showDIForm(row, col);
			}else{
				alert('服务器忙，请稍候操作...');
				return;
			}
		},
		failure: function(){
			Ext.Msg.alert("操作提示","服务器忙,请稍候操作...");
			return;
		}
	});
}

function showDIForm(row, col){
	var url = "excelFormDIForm.jsp";
	var argObj={};
	argObj.caption=$("txtDefTableName").value;
	argObj.title=$("txtTitle").value;
	argObj.defTableId=$("defTableId").value;
	argObj.row=row;
	argObj.col=col;
	
	try{
		with(oExcel.ActiveDocument.Sheets(1)){
			var commentObj=Cells(row,col).Comment;
			var comment="";
			if(commentObj!=null)
				comment=commentObj.Text();
				
			if(comment!="")
				argObj.comment=comment;
			var result = showModule2(url, true, 650, 525,argObj);
			if(result!=null){
				var common="<di " +
						" cp='"+encodeXml(result.cp)+"'" +	//标题
						" fd='"+encodeXml(result.fd)+"'" +	//字段名称
						" ce='"+row+":"+col+"'" +			//单元格
						" tp='"+encodeXml(result.tp)+"'" +	//数据类型
						" st='"+encodeXml(result.st)+"'" +	//存储方式
						" ip='"+encodeXml(result.ip)+"'" +	//是否允许键盘输入
						" mt='"+encodeXml(result.mt)+"'" +	//是否必填项目
						
						(result.fm.length>0?" fm='"+encodeChars(result.fm,"',<,>,&,\"")+"'":" ")+	//展现格式
						(result.dv.length>0?" dv='"+encodeChars(result.dv,"',<,>,&,\"")+"'":" ")+	//默认值
					    (result.ev.length>0?" ev='"+encodeChars(result.ev,"',<,>,&,\"")+"'":" ")+	//枚举值
					    (result.cf_sh.length>0?" cf_sh='"+encodeChars(result.cf_sh,"',<,>,&,\"")+"'":" ")+	//计算公式
					    (result.cf_sa.length>0?" cf_sa='"+encodeChars(result.cf_sa,"',<,>,&,\"")+"'":" ")+	//计算公式
					    (result.cf_cr.length>0?" cf_cr='"+encodeChars(result.cf_cr,"',<,>,&,\"")+"'":" ")+	//计算公式
					    (result.evt.length>0?" evt='"+encodeChars(result.evt,"',<,>,&,\"")+"'":" ")+	//签名事件
					    (result.vl.length>0?" vl='"+encodeChars(result.vl,"',<,>,&,\"")+"'":" ")+" />";	//验证公式
				if(commentObj!=null){
					Cells(row,col).ClearComments();
				}
				Cells(row,col).AddComment(common);
			}
		}
	}catch(e){}
		TANGER_OCX_OBJ.Activate();
	return true;
}

/**
 * 单元格双击事件
 */
function onCellDbClick(row, col) {
	if($("txtTitle").value == ""){
		alert("请先填标题");
		return false;
	}
	
	/** 关联表定义编号为空, 则为创建 */
	if($("defTableId").value == ""){
		Ext.Msg.confirm('操作提示','数据表 ' + $("txtDefTableName").value + ' 未生成,是否建立?',
			function(btn,text){
				if(btn == 'yes'){
					/** 创建表 */
					createTable(row, col);
				}});
	}else{
		showDIForm(row, col);	
	}
}

/*******************************************************************************************
 * 处理二维表单函数
 */
function setRowZbCell(){
	var oCell =  oExcel.ActiveDocument.Application.ActiveCell;
	$("txtRowZbCell").value = oCell.Row + ":" + oCell.Column;
	//alert(oCell.Row + ":" + oCell.Column);
}


