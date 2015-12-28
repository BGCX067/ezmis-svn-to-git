var SJFL_NIAN="NIAN";
var SJFL_YUE="YUE";
var SJFL_RI="RI";
var SJFL_JZ="JZ";
var box = null;
/**
 * 创建关键字字段
 */
function cteateKeyList(){
	var keyObj=keyData.evalJSON();
	if(keyObj.NIAN!=null){
		var nianList=keyObj.NIAN[0];
		createSelect(nianList,SJFL_NIAN,"年:");
	}
	if(keyObj.YUE!=null){
		var yueList=keyObj.YUE[0];
		createSelect(yueList,SJFL_YUE,"月:");
	}
	if(keyObj.RI!=null){
		var riList=keyObj.RI[0];
		createSelect(riList,SJFL_RI,"日:");
	}
	if(keyObj.JZ!=null){
		var jzList=keyObj.JZ[0];
		createSelect(jzList,SJFL_JZ,"机组:");
	}

	if(keyObj.keydata!=""){
		setKeyDefaultValue(keyObj.keydata,isRoot);
	}
}

function createSelect(keyModelList,key,name){
	var result="";
	result="<select name='"+key+"' onchange='setDay(this);'>";
	for(var i=0;i<keyModelList.length;i++){
		result=result+"<option value='"+keyModelList[i].value+"'>"+keyModelList[i].displayValue+"</option>";
	}
	result=result+"</select>&nbsp;&nbsp;";
	$(key+"Td").innerHTML=name+result;
}

/**
 * 设置关键字的默认值
 */
function setKeyDefaultValue(keyList,root){
	var keys=keyList.split("!");
	if(root!="true"){
		for(var i=0;i<keys.length;i++){
			var vals=keys[i].split(",");
			if(vals[0]==SJFL_NIAN){
				createText(vals[0],vals[1],"年:");
			}else if(vals[0]==SJFL_YUE){
				createText(vals[0],vals[1],"月:");
			}else if(vals[0]==SJFL_RI){
				createText(vals[0],vals[1],"日:");
			}else if(vals[0]==SJFL_JZ){
				createText(vals[0],vals[1],"机组:");
			}else{
				createText(vals[0],vals[1],"");
			}
		}
	}else{
		for(var i=0;i<keys.length;i++){
			var vals=keys[i].split(",");
			$(vals[0]).value=vals[1];
		}
	}
}

function createText(key,value,name){
	var result="<input type='text' name="+key+" value='"+value+"' style='width:40px;' readOnly='true'>&nbsp;&nbsp;";
	$(key+"Td").innerHTML=name+result;
}

/**改变年或者月的时候更新日
	**/
function setDay(obj){
	if(obj.name=="NIAN" || obj.name=="YUE"){
		if($("NIAN")!=null&&$("YUE")!=null){
			var param={};
			param.curDate=$("NIAN").value+"-"+$("YUE").value+"-1";
			AjaxRequest_Sync(link16,param,function(lastDateRes){
				var lastDateText = lastDateRes.responseText;
				var lastDateObj = lastDateText.evalJSON();
				if(lastDateObj.success){
					var lastDate=lastDateObj.day;
					if(lastDate!=""){
						var end=parseInt(lastDate);
						var result="日:<SELECT NAME='RI'>";
						for(var i=1;i<=end;i++){
							result=result+"<OPTION VALUE="+i+">"+i+"</OPTION>";
						}
						result=result+"</SELECT>";
						$("RITd").innerHTML=result;
					}else{
						alert("初始日期失败!");
					}
				}
			});
		}
	}
}

/**
 * 创建普通字段
 */
function cteateFieldList(){
	var fieldObj=fieldAll.evalJSON();
	var valuesObj=fieldValues.evalJSON();
	var html="";
	for(var i=0;i<fieldObj.length;i++){
		var sjlx=fieldObj[i].sjlx;
		var sjlxCn=fieldObj[i].sjlxCn;
		var result=createTable(sjlx,sjlxCn,fieldObj[i].data[0],"table"+i,"");
		html=html+result;
	}
	$("div6").innerHTML=html;
	
	for(var i=0;i<document.all.length;i++){
		var curObj=document.all[i];
		if(curObj.type=='text'&&curObj.tabIndex==1){
			curObj.focus();
			break;
		}
	}
	
	setFieldDefaultValue(valuesObj);
}

/**
 * 设置普通字段默认值
 */
function setFieldDefaultValue(fieldList){
	//debugger;
	var data=fieldList.data;
	for(var i=0;i<data.length;i++){
		var curData=data[i];
		for (var p in data[i]) {
			if($(p+"Text")!=null){
				$(p+"Text").value=curData[p];
			}
		}
	}
}



/**
 * 生成表格
 */
var tabindex=1;//标识文本框的顺序
function createTable(sjlx,sjlxCn,ywflList,tableName,updateData){
	var table="<table width='100%' height=100 border='0' id='"+tableName+"' cellpadding='1' cellspacing='1' style='word-wrap: break-word; word-break: break-all;' class='TreeMain2'>";
	var result=""+sjlxCn+"<input type='checkbox' id='"+sjlx+"Check' onclick='checkAllchecked("+tableName+",this);'  checked/>"+"";//结果
	
	var flag=0;//标识让一个元素加tr
	var bFlag=1;//标识一个数据项
	//具体分类下面的数据项
	for(var i=0;i<ywflList.length;i++){
		var item=ywflList[i];
		if(bFlag==4){
			bFlag=1;
			flag=0;
			result=result+"</tr>";
		}
		var style="";
		if(item.qsfs=="1"){
			style="color:red";
		}else if(item.qsfs=="2"){
			style="color:#000000";
		}else if(item.qsfs=="3"){
			style="color:#000000";
		}else if(item.qsfs=="4"){
			style="color:#000000";
		}
		if(flag==0){
			if(updateData==""){
				result=result+"<tr height=20><td width='16%'  align='right'><table><tr><td style='"+style+"' align='right'>"+item.iname+"</td><td><input type='checkbox' id='"+item.item+"Check' checked/></td></tr></table></td>"+
				"<td width='17%' align='left' style='color:#000000;'><input type='text' class='Input10' name='"+item.item+"Text' width='90%' tabindex='"+tabindex+"' onkeydown='if(event.keyCode==13)event.keyCode=9;'>"+item.dw+"</td>";
			}else if(updateData=="updateData"){
				result=result+"<tr height=20><td width='16%' align='right'><table><tr><td style='"+style+"'  align='right'>"+item.iname+"</td><td><input type='checkbox' id='"+item.item+"Check' checked/></td></tr></table></td>"+
				"<td width='17%' align='left' style='color:#000000;'><input type='text' class='Input10' name='"+item.item+"Text' width='90%' value='"+""+"' tabindex='"+tabindex+"' onkeydown='if(event.keyCode==13)event.keyCode=9;'>"+item.dw+"</td>";
			}else if(updateData=="findData"){
				result=result+"<tr height=20><td width='16%'  align='right'><table><tr><td style='"+style+"' align='right'>"+item.iname+"</td><td><input type='checkbox' id='"+item.item+"Check' checked/></td></tr></table></td>"+
				"<td width='17%' align='left' style='color:#000000;'><input type='text' class='Input11' name='"+item.item+"Text' width='90%' value='"+""+"' readOnly='true'>"+item.dw+"</td>";
			}
			flag++;
		}else{
			var oldTemp=item.iname;
			if(updateData==""){
				result=result+"<td width='16%'  align='right'><table><tr><td  style='"+style+"' align='right'><div title='"+oldTemp+"'>"+item.iname+"</div></td><td><input type='checkbox' id='"+item.item+"Check' checked/></td></tr></table></td>"+
				"<td width='17%' align='left' style='color:#000000;'><input type='text' class='Input10' name='"+item.item+"Text' width='90%' tabindex='"+tabindex+"' onkeydown='if(event.keyCode==13)event.keyCode=9;'>"+item.dw+"</td>";
			}else if(updateData=="updateData"){
				result=result+"<td width='16%' align='right'><table><tr><td  style='"+style+"' align='right'><div title='"+oldTemp+"'>"+item.iname+"</div></td><td><input type='checkbox' id='"+item.item+"Check' checked/></td></tr></table></td>"+
				"<td width='17%' align='left' style='color:#000000;'><input type='text' class='Input10' name='"+item.item+"Text' width='90%' value='"+""+"' tabindex='"+tabindex+"' onkeydown='if(event.keyCode==13)event.keyCode=9;'>"+item.dw+"</td>";
			}else if(updateData=="findData"){
				result=result+"<td width='16%' align='right'><table><tr><td  style='"+style+"' align='right'><div title='"+oldTemp+"'>"+item.iname+"</div></td><td><input type='checkbox' id='"+item.item+"Check' checked/></td></tr></table></td>"+
				"<td width='17%' align='left' style='color:#000000;'><input type='text' class='Input11' name='"+item.item+"Text' width='90%' value='"+""+"' readOnly='true'>"+item.dw+"</td>";
			}
		}
		tabindex++;
		bFlag++;
	}

	//最后一排有多少个字段
	var tds=ywflList.length%3;

	if(tds==1){
		result=result+"<td width='16%' align='right' style='color:#000000;'>&nbsp;</td>"+
				"<td width='17%' align='left' style='color:#000000;'>&nbsp;</td><td width='16%' align='right'>&nbsp;</td>"+
				"<td width='17%' align='left' style='color:#000000;'>&nbsp;</td></tr>";
	}else if(tds==2){
		result=result+"<td width='16%' align='right'></td>"+
				"<td width='17%' align='left' style='color:#000000;'></td></tr>";
	}else{
		result=result+"</tr>";
	}
	
	result=result+"<tr><td colspan='6'></td></tr>";
	table=table+result+"</table>";
	return table;
}


/**点击业务类型
**/
function checkAllchecked(tableName,checkObj){
	var result;
	if(checkObj.checked){
		result=true;
	}else{
		result=false;
	}
	checkObj.checked=result;
	for(var i=0;i<tableName.all.length;i++){
 		var e=tableName.all[i];
 		if((e.type=='checkbox')&&(!e.disabled)&&e!=checkObj){
		 	e.checked=result;
  		}
  	}
}


/**
 * 取数或计算
 */
function getDataOrComputeAll(totalType){
	//box.style.display="";
	box = new LightBox("idBox");
	box.Over = true;
	box.OverLay.Color = "#000";
	box.OverLay.Opacity = 50;
	box.Center = true;
	box.Show();
	//$("showmes").style.display="block";
	setButtonState(true,true,true);
	//alert();
	//组装key列表
	var keyList=packKeyList();
	
	//组装普通字段列表
	var fieldList=packFieldList();
	
	var link="";
	if(totalType==1){
		link=link17;
	}else if(totalType==2){
		link=link18;
	}
	
	var param={};
	param.kid=kid;
	param.keyList=keyList;
	param.fieldList=fieldList;
	AjaxRequest_Sync(link,param,function(res){
		var resText = res.responseText;
		var resObj = resText.evalJSON();
		if(resObj.success){
			var datas=resObj.list[0];
			for(var i=0;i<datas.length;i++){
				var data=datas[i];
				for (var p in datas[i]) {
					$(p+"Text").value=data[p];
					setButtonState(false,false,false);
					window.setTimeout("box.Close()", 2000);
					//$("showmes").style.display="NONE";
					
				}
			}
		}
	});
}

/**
 * 把关键字进行组装
 */
function packKeyList(){
	var keyList="";
	keyObj=keyData.evalJSON();
	var keys=keyObj.keydata.split("!");
	for(var i=0;i<keys.length;i++){
		var vals=keys[i].split(",");
		keyList=keyList+vals[0]+","+$(vals[0]).value+"!";
	}
	if(keyList!=""){
		keyList=keyList.substring(0,keyList.length-1);
	}
	return keyList;
}

/**
 * 把勾选的普通字段进行组装
 */
function packFieldList(){
	var fieldList="";
	for(var i=0;i<div6.all.length;i++){
		var curObj=div6.all[i];
		if(curObj.type=='text'){
			var item=curObj.name.substring(0,curObj.name.lastIndexOf("Text"));
			var itemCheck=$(item+"Check");
			if(itemCheck.checked){
				var value=curObj.value;
				fieldList=fieldList+item+","+value+"!";
			}
		}
	}
	if(fieldList!=""){
		fieldList=fieldList.substring(0,fieldList.length-1);
	}
	return fieldList;
}


function saveOrUpdateData(){
	//组装key列表
	var keyList=packKeyList();
	//组装普通字段列表
	var fieldList=packFieldList();
	var param={};
	param.kid=kid;
	param.keyList=keyList;
	param.fieldList=fieldList;
	AjaxRequest_Sync(link19,param,function(res){
		var resText = res.responseText;
		var resObj = resText.evalJSON();
		if(resObj.success){
			alert("保存成功!");
			window.returnValue = "true";
			window.close();
		}
	});
}

/**
 * 设置按钮状态
 */
function setButtonState(qsButtonValue,jsButtonValue,saveButtonValue){
	$("qsButton").disabled=qsButtonValue;
	$("jsButton").disabled=jsButtonValue;
	$("saveButton").disabled=saveButtonValue;
}
