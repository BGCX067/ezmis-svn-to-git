/**
*eform iframe 主要在eform的iframe中进行加载，
*为eform中的相关操作提供统一的调用接口
*
*/
/**
 * 保存表单，成功后不做任何处理
 */
function Save(){
	window.parent.f_SaveForm('SAVE');
}
/**
 * 保存成功之后关闭
 */
function SaveAndClose(){
	window.parent.f_SaveForm('SAVE_CLOSE');
}

/**
 * 打印
 */
function Print(){
	window.parent.doPrintForm();
}

function OpenPrintForm(){
	window.parent.openPrintForm();
}
/**
 * 保存成功后刷新表单
 */
function SaveAndRefresh(){
	window.parent.f_SaveForm('SAVE_REFRESH');
}

/**
 * 为自定义表单设置自定义的提交地址
 */
function SetAction(url){
	window.parent.f_SetAction(url);
}

/**
 * 关闭表单
 */
function CloseForm(){
	window.parent.close();
}


/**
 * 放弃保存，直接刷新表单
 */
function RefreshForm(){
	var url = window.parent.document.location + "";
	window.parent.document.location = url;
}
/**
 * 查询
 * condition 条件 该条件将会作为SQL WHERE段进行处理
 * 
 */
function Search(condition){
	window.parent.f_SearchUniqueFormRec(condition);
}

/**
 * 创建新的表单记录
 */
function CreateNew(){
	window.parent.f_CreateNew();
}

function ExportRecExcel(){
	window.parent.f_exportRecExcel();
}
/**
 * 加载指定的文档docid为主键ID
 */
function Load(docid){
	window.parent.f_loadEFormRec(docid);
}


/**
 * 渲染控件
 * @param el html dom容器，渲染目标
 * @param edr 控件类名 TXT|COMB|SUB....
 * @param fd 映射字段名称,用户指定控件name属性
 * @param status 状态 01|编辑状态, 02|打印状态
 * @param cp 中文字段名称，用于验证失败时给予错误提示 
 */
function RenderEditor(el,edr,param,fd,status,cp,formSn,docid){
	var editor = null;
	var html = "";
	var edrFn = null;//控件定义函数对象
	try{edrFn = eval(edr)}catch(e){}
	//是否存在对应的控件定义对象
	if(typeof edrFn == 'function'){
		eval("editor = new "+edr+"(fd,param,status,null,cp,formSn,docid);");
		editor.renderTo(el);
	}else{
		alert("控件"+edr+"还未定义,字段"+fd+"没有添加控件");
	}
}