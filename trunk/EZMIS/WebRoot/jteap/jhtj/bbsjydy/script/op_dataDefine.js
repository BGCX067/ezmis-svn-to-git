/*父亲窗口对象*/
var pWin=window.dialogArguments.opener;
/*当前应用连接对象编号*/


var tmpTimer;		//时钟对象
var oCurrentTable;	//当前选择的table
var loading=false;	//数据加载中
var loaded=true;	//数据已经加载完成

/*服务器程序回调函数*/
function fillTableFieldsFromServer(fields){
	//alert(fields);
	//TableUtil.removeAllRows($("table_001"));
	
	var sTableName=list1.getSelectedRowValue(0);			//表名
	var sTableCName=list1.getSelectedRowValue(1);			//表中文名

	/*判断在xml中是否已经在该表的数据,如果已经存在就不需要加进去了*/
	var oTable=xml_createTable(bbindexid,sTableName,sTableCName);
	var str="<table cellspacing='0' cellpadding='0' style='width: 101%;' id='table_001'><colgroup span='2'><col style='width: 30%;' /><col style='width: 40%;' /><col style='width: 30%;' /></colgroup>";
	/*循环加入数据*/
	for(var i=0;i<fields.length;i++){
		var field=fields[i];
		if(field.columncode==null){
			field.columncode="";
		}
		var sChecked='';
		if(field.id!=null && field.id!=''){
			//sChecked=" checked='true' ";
		}
		var sChkTd="<td><input "+sChecked+" onclick='onFieldChecked();' type='checkbox' name='chk_field_"+field.columncode+"' style='border:0px'/>"+field.columncode+"</td>";
		var sInputTd="<td><input onchange='onFieldNameChanged();' type='text' name='input_field_"+field.columncode+"' value='"+field.columncode+"' style='border:0px'/></td>";
		
		str+="<tr>"+sChkTd+sInputTd+"<td>"+field.columntype+"</td></tr>";
		var oField=xml_createField(oTable,field.columncode,field.columncode,field.columntype);
		if(sChecked!=''){
			//XMLUtil.setAttributeValue(oField,"checked",'true');
		}
	}
	str+="</table>";
	$("body2").innerHTML=str;
	/*重新帮定列表组件*/
	list2.bind($('container2'), $('head2'), $('body2'));
	//oLoadFace.hide();
	myMask.hide();
					
}
/**
 * 从客户端的xml中提取数据加载
 */
function fillTableFieldsFromXml(){
	clearInterval(tmpTimer);
	var oTable=oCurrentTable;
	//TableUtil.removeAllRows($("table_001"));
	var fields=oTable.childNodes;
	var str="<table cellspacing='0' cellpadding='0' style='width: 101%;' id='table_001'><colgroup span='2'><col style='width: 30%;' /><col style='width: 30%;' /></colgroup>";
	for(var i=0;i<fields.length;i++){
		var field=fields[i];
		var name=XMLUtil.getAttributeValue(field,"name");
		var cname=XMLUtil.getAttributeValue(field,"cname");
		var type=XMLUtil.getAttributeValue(field,"type");
		var checked=XMLUtil.getAttributeValue(field,"checked");
		var sChecked="";
		if(checked=='true'){
			sChecked="checked='true' ";
		}
		str+="<tr>" +
			 "	<td><input "+sChecked+" onclick='onFieldChecked();' type='checkbox' name='chk_field_"+name+"' style='border:0px'/>"+name+"</td>" +
			 "  <td><input onchange='onFieldNameChanged();' type='text' name='input_field_"+name+"' value='"+cname+"' style='border:0px'/></td>" +
			 "  <td>"+type+"</td>"+
			 "</tr>";
	}
	str+="</table>";
	$("body2").innerHTML=str;
	/*重新帮定列表组件*/
	list2.bind($('container2'), $('head2'), $('body2'));	
	//oLoadFace.hide();
	myMask.hide();
}


			
//////////////////////////////////////////////////////////////////////////////////////////
//=======================================事件============================================//
//////////////////////////////////////////////////////////////////////////////////////////
/*窗口加载完成事件*/

function onloadEvent(){
	/*
	SystemDataFieldService.all(function(fields){
		for(var i=0;i<fields.length;i++){
			//1.找到对应的表对象 			
			//2.将字段创建插入到表对象中去
			var field=fields[i];
			var oTable=xml_findTable(field.table.name);
			if(oTable==null){
				oTable=xml_createTable(field.table.system.id,field.table.name,field.table.cname);
			}
			var oField=xml_createField(oTable,field.name,field.cname);
			XMLUtil.setAttributeValue(oField,"checked",'true');	
		}
	
	});	*/
	myMask.hide();
}

/*列表组件的事件---- 当数据项被选中的时候触发*/
function onSelectTable(selectRows){
	myMask.show();
	if(selectRows.length<=0) return;
	var sTableName=list1.getSelectedRowValue(0);
	
	var oTable=xml_findTable(sTableName);
	oCurrentTable=oTable;
	//oLoadFace.show();
	TableUtil.removeAllRows($("table_001"));
	if(oTable!=null){
		//当客户端加载数据时由于速度过快导致loading层显示不出来，所以使用一个时钟，让程序停一下再运行
		waitForFillData(oTable);		
	}else{
		//只有当数据在内存中不存在的时候从服务器取数
		var loadParam={};
		loadParam.bbindexid=bbindexid;
		loadParam.sTableName=sTableName;
		AjaxRequest_Sync(link12,loadParam,function(request){
				var responseText = request.responseText;
				var responseObj = responseText.evalJSON();
				var fields=responseObj.list[0];
				if(responseObj.success){
					fillTableFieldsFromServer(fields);
				}
		});
	}
}


//loaing组件的事件 ---- 数据加载中的时候,列表为无效状态*/
function onLoading(){
	list1.disabled=true;
	list2.disabled=true;
	loading=true;
	loaded=false;
}



/*loading组件的事件 ---- 数据加裁完成后，列表成有效状态*/
function onLoaded(){
	list1.disabled=false;
	list2.disabled=false;	
	loaded=true;
	loading=false;			
}



/*当前表名发生变化的时候*/
function onTableNameChanged(){
	var oInput=event.srcElement;
	var oChk=oInput.parentNode.previousSibling.childNodes[0];
	
	var sTableName=oInput.name;
	sTableName=sTableName.substring(sTableName.lastIndexOf("table_")+6);
	oTable=xml_findTable(sTableName);
	XMLUtil.setAttributeValue(oTable,"cname",oInput.value);
	xml_setTableChecked(sTableName,oChk.checked);
}



/*当前表字段名发生变化的时候*/
function onFieldNameChanged(){
	var oInput=event.srcElement;
	var sFieldName=oInput.name;
	sFieldName=sFieldName.substring(sFieldName.lastIndexOf("field_")+6);
	var sTableName=list1.getSelectedRowValue(0);

	var oField=xml_findField(sTableName,sFieldName);
	XMLUtil.setAttributeValue(oField,"cname",oInput.value);
	//xml_setTableChecked(sTableName,oChk.checked);
	var oRow=list1.getSelectedRowObject();
	var checked=oRow.childNodes[0].childNodes[0].checked;
	xml_setTableChecked(sTableName,checked);
}


/*当客户端加载数据时由于速度过快导致loading层显示不出来，所以使用一个时钟，让程序停一下再运行*/
function waitForFillData(oTable){
	oCurrentTable=oTable;
	tmpTimer=window.setInterval("fillTableFieldsFromXml()",10);
}

function onFieldChecked(){
	var oChk=event.srcElement;
	var rowIndex=oChk.parentNode.parentNode.rowIndex;
	list2.selectRow(rowIndex);
	
	//找到oField对象并设置其checked状态
	var sFieldName=oChk.name;sFieldName=sFieldName.substring(sFieldName.lastIndexOf("field_")+6);
	var sTableName=list1.getSelectedRowValue(0);
	var oField=xml_findField(sTableName,sFieldName);	
	if(oField!=null){
		xml_setFieldChecked(sTableName,sFieldName,oChk.checked);		

		var oRow=list1.getSelectedRowObject();
		var checked=oRow.childNodes[0].childNodes[0].checked;
		xml_setTableChecked(sTableName,checked);//为了表明该表下的数据已经更改，所以需要设置对应table的checked状态		
	}
	
	
}
/*事件 ---- 当表被checked的时候 */
function onTableChecked(){

	var oChk=event.srcElement;
	var rowIndex=oChk.parentNode.parentNode.rowIndex;
	var sTableName=oChk.name;
	sTableName=sTableName.substring(sTableName.lastIndexOf("table_")+6);
	
	list1.selectRow(rowIndex);
	var oTable=xml_findTable(sTableName);
	/**
	 * 如果表已经加载到客户端,则直接设置checked状态,
	 * 否则需要等待数据加载完成后才能设置状态
	 */
	if(oTable!=null){
		xml_setTableChecked(sTableName,oChk.checked);
	}else{
		var args=new Array();//构造参数
		args[0]=sTableName;
		args[1]=oChk.checked;
		
		LoadingDataTimer.func=waitToCheckTheTable;//指定数据加载后需要回调的函数
		LoadingDataTimer.args=args;				  //指定函数需要的参数数组
		LoadingDataTimer.start();				  //开始
	}
}
/**
 * 等数据加载完成以后再设置oTable的checked属性
 */
function waitToCheckTheTable(args){
	var sTableName=args[0];
	var checked=args[1];
	xml_setTableChecked(sTableName,checked);
}



/**
 *  LoadingDataTimer   等待数据加载完成后，指定执行的函数及参数
 * 
 *  LoadingDataTimer.func=waitToCheckTheTable;	指定数据加载后需要回调的函数
	LoadingDataTimer.args=args;					指定函数需要的参数数组
	LoadingDataTimer.start();					开始
 */
function LoadingDataTimer(){
}
LoadingDataTimer.func=null;
LoadingDataTimer.args=null;
LoadingDataTimer.timer=null;

LoadingDataTimer.start=function(){
	window.clearInterval(LoadingDataTimer.timer);
	if(loading && !loaded){
		LoadingDataTimer.timer=window.setInterval("LoadingDataTimer.start()",100);
	}else{
		if(LoadingDataTimer.func){			
			LoadingDataTimer.func(LoadingDataTimer.args);
		}
	}
}




/**全选操作
		**/
function checkedAll(obj){
	var tab=document.getElementById("table_001");
	var child=tab.all;
	var bool=true;
	
	for(var i=0;i<child.length;i++){
		var tempId=child.item(i).name;
		if(tempId!=undefined){
			if(tempId.indexOf("chk")>-1){
				bool=false;
				var che=document.getElementById(tempId);
				che.click();
			}
		}
	}
	if(bool){
		alert("请选择数据视图!");
		return;
	}
	if(obj.value=="全选"){
		//obj.value="全不选";
	}else{
		//obj.value="全选";
	}
}


function submitForm(){
	//var loadParam={};
	//loadParam.sysid=currSystemId;
	//loadParam.xmlFields=oXmlDoc.xml;
	$("xmlField").value=oXmlDoc.xml;
	$("bbindexid").value=bbindexid;
	$("isUpdate").value=isUpdate;
	$("id").value=id;
	window.parent.parent.document.all.formFrame.style.display="none";
	window.parent.parent.document.all.next.style.display="inline";
    $("myForm").submit();// 提交！
}