var oExcel; // 父窗口的Excel对象
var InputsArray = [];	//可编辑域对象集合

//此名称用于命名区域名称
var sheetRangeName = "IdsInfo";
var box = null;

/**
 * 当页面加载完成后调用
 */
function onload() {
	$("importButton").disabled=true;
	$("saveButton").disabled=true;
	$("daleteButton").disabled=true;
	
	var url = null;
	box = new LightBox("idBox");
	box.Over = true;
	box.OverLay.Color = "#000";
	box.OverLay.Opacity = 50;
	box.Center = true;
	var param={};
		param.indexid=indexid;
		AjaxRequest_Sync(link9, param, function(req) {
			var responseText=req.responseText;	
 			var responseObject=Ext.util.JSON.decode(responseText);
 			if(responseObject.success){
				url = contextPath+ "/jteap/jhtj/bbsj/bbSjAction!readExcelBlobAction.do?indexid=" + indexid;
			}
			TANGER_OCX_OpenDoc(url);
			ntkoDiv.style.display = "block";
			splash.style.display = "none";
			
			oExcel = document.TANGER_OCX;
			//使批注显示小红图标
			setExcelConfig();
			//显示公式栏
			//oExcel.ActiveDocument.Application.DisplayFormulaBar = true;
			//当有填数的时候就开始载入批注
			if(responseObject.isTs){
				if(responseObject.data[0]!=null){
					$("excelDataItemXml").value=responseObject.data[0].excelDataItemXml;
					Excel_AttachComment();
				}
			}
			box.Close();
			TANGER_OCX_OBJ.SetReadOnly(true,"");
			$("isReadOnly").value="true";
		});
}

/**
 * 提交表单,保存
 */
function submitForm() {
	oExcel.Activate(false);
	Excel_Bianli();
	TANGER_OCX_SaveDoc();
	alert("保存成功!");
	onload();
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
	var oXml = getDom($("excelDataItemXml").value);
	if(oXml!=null){
		//TANGER_OCX_SetReadOnlyPwd(false,excelTemplatePwd);		
		var size = oXml.firstChild.childNodes.length;
		for(var i=0;i<size;i++){
			try{
				var oSheetNode = oXml.firstChild.childNodes[i];//sheet  node
				//oExcel.ActiveDocument.Sheets(i+1).Range("$A$1:$B$1").Name.Delete();
				//oExcel.ActiveDocument.Sheets(i+1).Range("$A$1:$B$1").Name.Name = "index_" + oExcel.ActiveDocument.Sheets(i+1).Index;
				
				var count = oSheetNode.childNodes.length;
				for(var j= 0;j<count;j++){
					var oNode = oSheetNode.childNodes[j];
					var sCell = getXmlAttribute(oNode,"ce");
					var row = parseInt(sCell.substring(0,sCell.indexOf(":")));
					var col = parseInt(sCell.substring(sCell.indexOf(":")+1));
					with(oExcel.ActiveDocument.Sheets(i+1)){
						Excel_ResetCellRule(oExcel,i+1,row,col);
						Cells(row,col).AddComment(oNode.xml);
					}
				}
			}catch(e){
				//alert(e);
			}
		}
		//默认切换到第一个工作薄
		oExcel.ActiveDocument.Sheets(1).Select();
		oExcel.ActiveDocument.ActiveSheet.Cells(1,1).Select();
	}
	
}


//对单元格进行复位
function Excel_ResetCell(oExcel,iSheetIndex,iRow,iCol){

	with(oExcel.ActiveDocument.Sheets(iSheetIndex)){
		//设备背景色为无色填充
		Cells(iRow,iCol).Interior.ColorIndex = -4142;
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
	var oXml = getDom("<root></root>");
	var oRoot = oXml.firstChild;
	var iSheetIndex =1;
	//for(var iSheetIndex =1;iSheetIndex<=oExcel.ActiveDocument.Sheets.Count;iSheetIndex++){
		with(oExcel.ActiveDocument.Sheets(iSheetIndex)){
			var sheetNode = oXml.createNode(1,"sheet","");
			oRoot.appendChild(sheetNode);

			var AllComments = Comments;
			var oComment = null;
			try{
				oComment = AllComments.Item(1);
			}catch(ex){
			}
			while(oComment != null){		
				var row = oComment.Parent.Row;
				var col = oComment.Parent.Column;
				
				var sComment = "";
				
				//取得当前单元格的批注内容
				var vname="",fname="",ce="",fx="",dnum="",merge="",bbindexid="",bbioid="",bbsjzdid="";
				
				sComment = oComment.Text();
				var oRuleXml = getDom(sComment).firstChild;
				//得到对应的值
				vname = getXmlAttribute(oRuleXml,"vname");
				fname = getXmlAttribute(oRuleXml,"fname");
				ce = getXmlAttribute(oRuleXml,"ce");
				fx = getXmlAttribute(oRuleXml,"fx");
				dnum = getXmlAttribute(oRuleXml,"dnum");
				merge = getXmlAttribute(oRuleXml,"merge");
				bbindexid = getXmlAttribute(oRuleXml,"bbindexid");
				bbioid = getXmlAttribute(oRuleXml,"bbioid");
				bbsjzdid = getXmlAttribute(oRuleXml,"bbsjzdid");
				
				var iRow = parseInt(ce.substring(0, ce.indexOf(":")));
				var iCol = parseInt(ce.substring(ce.indexOf(":") + 1));
				var oCell = Cells(iRow, iCol);
				
				var ei = {'vname':vname,'fname':fname,'ce':ce,'fx':fx,'dnum':dnum,'merge':merge,'bbindexid':bbindexid,'bbioid':bbioid,'bbsjzdid':bbsjzdid}
				InputsArray.push(ei);
				
				//最后对单元格进行清理
				Excel_ResetCell(oExcel,iSheetIndex,row,col);
				sheetNode.appendChild(oRuleXml);
				
				Cells(row,col).ClearComments();
				oExcel.Activate(true);
				oExcel.ActiveDocument.ActiveSheet.Cells(row,col).Select();
				oExcel.ActiveDocument.Application.Selection.Locked = true;
				try{
					oComment = AllComments.Item(1);
				}catch(ex){
					oComment=null;
				}		
			}
		}
		sXml = oXml.xml;
		$("excelDataItemXml").value = sXml;
		$("editableInputs").value = Object.toJSON(InputsArray);
	//}
	return true;
}


//导入模版
function exportExcelForm(){
	var title=$("txtTitle").value;
	if(title==""){
		alert("请先填标题");
		return ;
	}
	TANGER_OCX_OBJ.WebFileName = title;
	TANGER_OCX_OBJ.ShowDialog (2);
	
	//TANGER_OCX_OBJ.SaveToLocal("c:/"+title,true);
	//alert("文件成功导出："+"c:/"+title+".xls");
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
		TANGER_OCX_SaveDoc();
		box.Close();
		hideSplash();
		TANGER_OCX_OBJ.SetReadOnly(true,"");
		$("isReadOnly").value="true";
		alert("导入成功!");
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

function design(){
	TANGER_OCX_OBJ.SetReadOnly(false,"");
	TANGER_OCX_OBJ.Activate(true);
	$("importButton").disabled=false;
	$("saveButton").disabled=false;
	$("daleteButton").disabled=false;
	$("isReadOnly").value="false";
}

function cancel(){
	TANGER_OCX_OBJ.SetReadOnly(true,"");
	TANGER_OCX_OBJ.Activate(false);
	$("importButton").disabled=true;
	$("saveButton").disabled=true;
	$("daleteButton").disabled=true;
	$("isReadOnly").value="true";
	onload();
}

function deleteContent(){
	//返回一个 Range 对象
	var activecell=oExcel.ActiveDocument.Application.ActiveCell;
	//列
	var column=activecell.Column;
	//行
	var row=activecell.Row;
	//得到单元格
	var cell=oExcel.ActiveDocument.Application.Cells(row,column);
	cell.ClearComments();
	cell.value="";
}



/**
 * 单元格双击事件
 */
function onCellDbClick(row, col) {
	if($("isReadOnly").value=="true"){
	}else{
		var url = contextPath+ "/jteap/jhtj/bbsj/dataSelectIndex.jsp"
		var argObj = {};
		argObj.row = row;
		argObj.col = col;
		argObj.indexid=indexid;
		//try{
			with(oExcel.ActiveDocument.ActiveSheet){
				var commentObj = Cells(row,col).Comment;
				var comment="";
				if(commentObj!=null)
					comment = commentObj.Text();	
					
				if(comment!="")
					argObj.comment = comment;
				var result = showModule2(url, true, 600, 500,argObj);
				if(result!=null){
					//Names(sheetRangeName).Comment = "index_" + arrIndex;
					var common="<di " +
							" vname='"+result.vname+"'" +	//视图名
							" fname='"+result.fname+"'" +	//字段名
							" ce='"+row+":"+col+"'" +			//单元格
							" fx='"+result.fx+"'" +	//数据类型
							" dnum='"+result.dnum+"'" +	//存储方式
							" merge='"+result.merge+"'"+
							" bbindexid='"+result.bbindexid+"'"+
							" bbioid='"+result.bbioid+"'"+
							" bbsjzdid='"+result.bbsjzdid+"'"+"/>"//是否允许键盘输入
							
					if(commentObj!=null){
						Cells(row,col).ClearComments();
						Cells(row,col).value="";
					}
					Cells(row,col).AddComment(common);
					Cells(row,col).value=result.vname+"."+result.fname;
				}
			}
		//}catch(e){}
			TANGER_OCX_OBJ.Activate();
	}
	return true;
}


