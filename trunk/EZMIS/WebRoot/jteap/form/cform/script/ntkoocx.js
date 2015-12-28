var TANGER_OCX_bDocOpen = false;
var TANGER_OCX_filename;
var TANGER_OCX_actionURL; //For auto generate form fiields
var TANGER_OCX_OBJ; //The Control

//以下为V1.7新增函数示例

//从本地增加图片到文档指定位置
function AddPictureFromLocal()
{
	if(TANGER_OCX_bDocOpen)
	{	
    TANGER_OCX_OBJ.AddPicFromLocal(
	"", //路径
	true,//是否提示选择文件
	true,//是否浮动图片
	100,//如果是浮动图片，相对于左边的Left 单位磅
	100); //如果是浮动图片，相对于当前段落Top
	};	
}

//从URL增加图片到文档指定位置
function AddPictureFromURL(URL)
{
	if(TANGER_OCX_bDocOpen)
	{
    TANGER_OCX_OBJ.AddPicFromURL(
	URL,//URL 注意；URL必须返回Word支持的图片类型。
	true,//是否浮动图片
	150,//如果是浮动图片，相对于左边的Left 单位磅
	150);//如果是浮动图片，相对于当前段落Top
	};
}

//从本地增加印章文档指定位置
function AddSignFromLocal()
{

   if(TANGER_OCX_bDocOpen)
   {
      TANGER_OCX_OBJ.AddSignFromLocal(
	"匿名用户",//当前登陆用户
	"",//缺省文件
	true,//提示选择
	0,//left
	0)  //top
   }
}

//从URL增加印章文档指定位置
function AddSignFromURL(URL)
{
   if(TANGER_OCX_bDocOpen)
   {
      TANGER_OCX_OBJ.AddSignFromURL(
	"匿名用户",//当前登陆用户
	URL,//URL
	50,//left
	50)  //top
   }
}

//开始手写签名
function DoHandSign()
{
   if(TANGER_OCX_bDocOpen)
   {	
	TANGER_OCX_OBJ.DoHandSign(
	"匿名用户",//当前登陆用户 必须
	0,//笔型0－实线 0－4 //可选参数
	0x000000ff, //颜色 0x00RRGGBB//可选参数
	2,//笔宽//可选参数
	100,//left//可选参数
	50); //top//可选参数
	}
}
//开始手工绘图，可用于手工批示
function DoHandDraw()
{
	if(TANGER_OCX_bDocOpen)
	{	
	TANGER_OCX_OBJ.DoHandDraw(
	0,//笔型0－实线 0－4 //可选参数
	0x00ff0000,//颜色 0x00RRGGBB//可选参数
	3,//笔宽//可选参数
	200,//left//可选参数
	50);//top//可选参数
	}
}
//检查签名结果
function DoCheckSign()
{
	if(TANGER_OCX_bDocOpen)
	{		
	var ret = TANGER_OCX_OBJ.DoCheckSign
	(
	/*可选参数 IsSilent 缺省为FAlSE，表示弹出验证对话框,否则，只是返回验证结果到返回值*/
	);//返回值，验证结果字符串
	//alert(ret);
	}	
}
//此函数用来加入一个自定义的文件头部
function TANGER_OCX_AddDocHeader( strHeader )
{
	var i,cNum = 30;
	var lineStr = "";
	try
	{
		for(i=0;i<cNum;i++) lineStr += "_";  //生成下划线
		with(TANGER_OCX_OBJ.ActiveDocument.Application)
		{
			Selection.HomeKey(6,0); // go home
			Selection.TypeText(strHeader);
			Selection.TypeParagraph(); 	//换行
			Selection.TypeText(lineStr);  //插入下划线
			// Selection.InsertSymbol(95,"",true); //插入下划线
			Selection.TypeText("★");
			Selection.TypeText(lineStr);  //插入下划线
			Selection.TypeParagraph();
			//Selection.MoveUp(5, 2, 1); //上移两行，且按住Shift键，相当于选择两行
			Selection.HomeKey(6,1);  //选择到文件头部所有文本
			Selection.ParagraphFormat.Alignment = 1; //居中对齐
			with(Selection.Font)
			{
				NameFarEast = "宋体";
				Name = "宋体";
				Size = 12;
				Bold = false;
				Italic = false;
				Underline = 0;
				UnderlineColor = 0;
				StrikeThrough = false;
				DoubleStrikeThrough = false;
				Outline = false;
				Emboss = false;
				Shadow = false;
				Hidden = false;
				SmallCaps = false;
				AllCaps = false;
				Color = 255;
				Engrave = false;
				Superscript = false;
				Subscript = false;
				Spacing = 0;
				Scaling = 100;
				Position = 0;
				Kerning = 0;
				Animation = 0;
				DisableCharacterSpaceGrid = false;
				EmphasisMark = 0;
			}
			Selection.MoveDown(5, 3, 0); //下移3行
		}
	}
	catch(err){
		//alert("错误：" + err.number + ":" + err.description);
	}
	finally{
	}
}
function strtrim(value)
{
	return value.replace(/^\s+/,'').replace(/\s+$/,'');
}

function TANGER_OCX_doFormOnSubmit()
{
	var form = document.forms[0];
  	if (form.onsubmit)
	{
    	var retVal = form.onsubmit();
     	if (typeof retVal == "boolean" && retVal == false)
       	return false;
	}
	return true;
}

//允许或禁止显示修订工具栏和工具菜单（保护修订）
function TANGER_OCX_EnableReviewBar(boolvalue)
{
	TANGER_OCX_OBJ.ActiveDocument.CommandBars("Reviewing").Enabled = boolvalue;
	TANGER_OCX_OBJ.ActiveDocument.CommandBars("Track Changes").Enabled = boolvalue;
	TANGER_OCX_OBJ.IsShowToolMenu = boolvalue;	//关闭或打开工具菜单
}

//打开或者关闭修订模式
function TANGER_OCX_SetReviewMode(boolvalue)
{
	TANGER_OCX_OBJ.ActiveDocument.TrackRevisions = boolvalue;
}

//进入或退出痕迹保留状态，调用上面的两个函数
function TANGER_OCX_SetMarkModify(boolvalue)
{
	TANGER_OCX_SetReviewMode(boolvalue);
	TANGER_OCX_EnableReviewBar(!boolvalue);
}

//显示/不显示修订文字
function TANGER_OCX_ShowRevisions(boolvalue)
{
	TANGER_OCX_OBJ.ActiveDocument.ShowRevisions = boolvalue;
}

//打印/不打印修订文字
function TANGER_OCX_PrintRevisions(boolvalue)
{
	TANGER_OCX_OBJ.ActiveDocument.PrintRevisions = boolvalue;
}

function TANGER_OCX_SaveToServer()
{
	if(!TANGER_OCX_bDocOpen)
	{
		alert("没有打开的文档。");
		return;
	}
	
	TANGER_OCX_filename = prompt("附件另存为：","新文档.doc");
	if ( (!TANGER_OCX_filename))
	{
		TANGER_OCX_filename ="";
		return;
	}
	else if (strtrim(TANGER_OCX_filename)=="")
	{
		alert("您必须输入文件名！");
		return;
	}
	//alert(TANGER_OCX_filename);
	TANGER_OCX_SaveDoc();
}


//设置页面布局
function TANGER_OCX_ChgLayout()
{
 	try
	{
		TANGER_OCX_OBJ.showdialog(5); //设置页面布局
	}
	catch(err){
		alert("错误：" + err.number + ":" + err.description);
	}
	finally{
	}
}

//打印文档
function TANGER_OCX_PrintDoc()
{
	try
	{
		TANGER_OCX_OBJ.Activate();
		TANGER_OCX_OBJ.printout(true);
	}
	catch(err){
		//alert("错误：" + err.number + ":" + err.description);
	}
	finally{
	}
}
//打印预览
function TANGER_OCX_PrintPreview()
{
	//var xxx=TANGER_OCX_OBJ.ActiveDocument.Application;
	//alert('xxxx')
	try
	{
		TANGER_OCX_OBJ.Activate();
		TANGER_OCX_OBJ.PrintPreview();
	}
	catch(err){
		alert("错误：" + err.number + ":" + err.description);
	}
	finally{
	}   
	
}
function TANGER_OCX_SaveEditToServer()
{
	if(!TANGER_OCX_bDocOpen)
	{
		alert("没有打开的文档。");
		return;
	}
	
	TANGER_OCX_filename = "excel.xls";
	if ( (!TANGER_OCX_filename))
	{
		TANGER_OCX_filename ="";
		return;
	}
	else if (strtrim(TANGER_OCX_filename)=="")
	{
		alert("您必须输入文件名！");
		return;
	}
	//alert(TANGER_OCX_filename);
	TANGER_OCX_SaveDoc();
}

//允许或禁止文件－>新建菜单
function TANGER_OCX_EnableFileNewMenu(boolvalue)
{
	TANGER_OCX_OBJ.EnableFileCommand(0) = boolvalue;
}
//允许或禁止文件－>打开菜单
function TANGER_OCX_EnableFileOpenMenu(boolvalue)
{
	TANGER_OCX_OBJ.EnableFileCommand(1) = boolvalue;
}
//允许或禁止文件－>关闭菜单
function TANGER_OCX_EnableFileCloseMenu(boolvalue)
{
	TANGER_OCX_OBJ.EnableFileCommand(2) = boolvalue;
}
//允许或禁止文件－>保存菜单
function TANGER_OCX_EnableFileSaveMenu(boolvalue)
{
	TANGER_OCX_OBJ.EnableFileCommand(3) = boolvalue;
}
//允许或禁止文件－>另存为菜单
function TANGER_OCX_EnableFileSaveAsMenu(boolvalue)
{
	TANGER_OCX_OBJ.EnableFileCommand(4) = boolvalue;
}
//允许或禁止文件－>打印菜单
function TANGER_OCX_EnableFilePrintMenu(boolvalue)
{
	TANGER_OCX_OBJ.EnableFileCommand(5) = boolvalue;
}
//允许或禁止文件－>打印预览菜单
function TANGER_OCX_EnableFilePrintPreviewMenu(boolvalue)
{
	TANGER_OCX_OBJ.EnableFileCommand(6) = boolvalue;
}

function TANGER_OCX_OpenDoc(url)
{
	try{
	TANGER_OCX_OBJ = document.all.item("TANGER_OCX");
	if(url != null && url!="")
	{	

//		TANGER_OCX_OBJ.BeginOpenFromURL("ReadDocServlet?templateId=" + docid);
		TANGER_OCX_OBJ.OpenFromURL(url);
	}
	else
	{
		TANGER_OCX_OBJ.CreateNew("Excel.Sheet");
	}
	}catch(e){
		alert("报错啦："+e);
		//window.document.location.reload();
	}
}
function test2(){
	TANGER_OCX_OBJ.Activate(false);
	TANGER_OCX_SetReadOnly(false);	
	
	
}
function TANGER_OCX_OnDocumentOpened(str, obj)
{
	
	TANGER_OCX_bDocOpen = true;		
}

function TANGER_OCX_OnDocumentClosed()
{
   TANGER_OCX_bDocOpen = false;
}

function TANGER_OCX_SaveDoc()
{
	var newwin,newdoc;

	if(!TANGER_OCX_bDocOpen)
	{
		alert("没有打开的文档。");
		return;
	}

	try
	{
	 	if(!TANGER_OCX_doFormOnSubmit())return; //如果存在，则执行表单的onsubmit函数。
	 	//调用控件的SaveToURL函数
	 	
		var retHTML = TANGER_OCX_OBJ.SaveToURL
		(
			document.forms[0].action,  //此处为uploadedit.asp
			"excelFile",	//文件输入域名称,可任选,不与其他<input type=file name=..>的name部分重复即可
			"", //可选的其他自定义数据－值对，以&分隔。如：myname=tanger&hisname=tom,一般为空
			"excel.xls", //文件名,此处从表单输入获取，也可自定义
			"myForm" //控件的智能提交功能可以允许同时提交选定的表单的所有数据.此处可使用id或者序号
		); //此函数会读取从服务器上返回的信息并保存到返回值中。
		
		//打开一个新窗口显示返回数据		
//		newwin = window.open("","_blank","left=200,top=200,width=400,height=300,status=0,toolbar=0,menubar=0,location=0,scrollbars=1,resizable=1",false);
//		newdoc = newwin.document;
//		newdoc.open();
//		newdoc.write("<html><head><title>返回的数据</title></head><body><center><hr>")
//		newdoc.write(retHTML+"<hr>");
//		newdoc.write("<input type=button VALUE='关闭窗口' onclick='window.close()'>");
//		newdoc.write('</center></body></html>');
//		newdoc.close();
//		alert("保存成功");
		
//		if(window.opener) //如果父窗口存在，刷新并关闭当前窗口
//		{
//			window.opener.location.reload();
//		}
		if(0 == TANGER_OCX_OBJ.StatusCode){
//			alert('关');
			window.close();
		}
	}
	catch(err){
		alert("不能保存到URL：" + err.number + ":" + err.description);
	}
}




function TANGER_OCX_SaveAsHTML()
{
	var newwin,newdoc;

	if(!TANGER_OCX_bDocOpen)
	{
		alert("没有打开的文档。");
		return;
	}
	try
	{
		//调用控件的PublishAsHTMLToURL函数
		var retHTML = TANGER_OCX_OBJ.PublishAsHTMLToURL
			(
				document.forms[0].action,
				"htmlExcelFile", //文件输入域名称,可任选,所有相关文件都以此域上传
				"",
				"sss",
				"myForm"
				//此处省略了第5个参数，HTML FORM得索引或者id.这样,不会提交表单数据
				//只提交所有得html文件相关得文件
			);
		newwin = window.open("","_blank","left=200,top=200,width=400,height=300,status=0,toolbar=0,menubar=0,location=0,scrollbars=1,resizable=1",false);
		newdoc = newwin.document;
		newdoc.open();
		newdoc.write("<center><hr>"+retHTML+"<hr><input type=button VALUE='关闭窗口' onclick='window.close()'></center>");
		newdoc.close();	
		newwin.focus();
		if(window.opener) //如果父窗口存在，刷新并关闭当前窗口
		{
			window.opener.location.reload();
		}
	}
	catch(err){
		alert("不能保存到URL：" + err.number + ":" + err.description);
	}
	finally{
	}
}



function TANGER_OCX_SetReadOnly(boolvalue)
{
	var appName,i;
	
		if (boolvalue) TANGER_OCX_OBJ.IsShowToolMenu = false;
		
		
		with(TANGER_OCX_OBJ.ActiveDocument.Sheets(1))
		{	
				if (!boolvalue){
					Unprotect();
				}
				if (boolvalue){			
					Protect();
				}
		}

}


function WRITE_TANGER_OCX(){
	document.write('<!-- 用来产生编辑状态的ActiveX控件的JS脚本-->   ');
	document.write('<!-- 因为微软的ActiveX新机制，需要一个外部引入的js-->   ');
	document.write('<object id="TANGER_OCX" classid="clsid:C9BC4DFF-4248-4a3c-8A49-63A7D317F404"    ');
	document.write('codebase="OfficeControl.cab#version=4,0,1,2" width="100%" height="95%">   ');
	document.write('<param name="BorderStyle" value="1">   ');
	document.write('<param name="BorderColor" value="14402205">   ');
	document.write('<param name="TitlebarColor" value="15658734">   ');
	document.write('<param name="TitlebarTextColor" value="0">   ');
	document.write('<param name="Caption" value="NTKO OFFICE文档控件V4.0 Build 1.2 http://www.ntko.com">   ');
	document.write('<param name="IsShowToolMenu" value="-1">   ');
	document.write('<param name="IsUseUTF8URL" value="-1">   ');
	document.write('<param name="MaxUploadSize" value="10000000">   ');
	document.write('<param name="CustomMenuCaption" value="我的菜单">   ');
	document.write('<param name="MenubarColor" value="14402205">   ');
	document.write('<PARAM NAME="MenuButtonColor" VALUE="16180947">   ');
	document.write('<param name="MenuBarStyle" value="3">   ');
	document.write('<param name="MenuButtonStyle" value="7">   ');
	document.write('<SPAN STYLE="color:red">不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。</SPAN>   ');
	document.write('</object>   ');

}






function TANGER_OCX_SaveAsHTMLToPublishPath()
{
	
	//TANGER_OCX_SetReadOnly(false);	
	//TANGER_OCX_OBJ.Activate();
	var htmlPath = document.all["excelHtmlPublishPath"].value;
	var fileName = document.all["excelHtmlFileName"].value;
	fileName = htmlPath + "\\"+ fileName;
	var newwin,newdoc;
	if(!TANGER_OCX_bDocOpen)
	{
		alert("没有打开的文档。");
		return;
	}
	
		var xlSourceSheet = 1;
		var xlHtmlStatic = 0;
		var title = fileName;
		var Sheet = "Sheet1";
		var pub = oExcel.ActiveDocument.PublishObjects.Add(xlSourceSheet,fileName,Sheet,"", xlHtmlStatic,"Book1_2361", "");
		pub.Publish(true);
}


