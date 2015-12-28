/**
 * 创建普通字段
 */
function cteateFieldList(){
	var fieldObj=fieldAll.evalJSON();
	var html="";
	for(var i=0;i<fieldObj.length;i++){
		var sjlx=fieldObj[i].sjlx;
		var sjlxCn=fieldObj[i].sjlxCn;
		var result=createTable(sjlx,sjlxCn,fieldObj[i].data[0],"table"+i,"");
		//$("dataDiv").innerText=$("dataDiv").innerHTML+result;
		html=html+result;
	}
	$("dataDiv").innerHTML=html;
}



/**
 * 生成表格
 */
function createTable(sjlx,sjlxCn,ywflList,tableName,updateData){
	var result='<ul><li>'+sjlxCn+'</li></ul>';
	var table="<table width='100%' border='0' id='"+tableName+"' cellpadding='0' cellspacing='0'>";
	var flag=0;//标识让一个元素加tr
	var bFlag=1;//标识一个数据项
	//具体分类下面的数据项
	for(var i=0;i<ywflList.length;i++){
		var item=ywflList[i];
		if(bFlag==5){
			bFlag=1;
			flag=0;
			table=table+"</tr>";
		}
		if(flag==0){
			table=table+"<tr><td class='LM01Txt'><a href='${contextPath}/sjdbfxAction!showChartAction.do?kid="+kid+"&item="+item.item+"&iname="+item.iname+"&flflag="+flflag+"'>"+item.iname+"</a></td>";
			flag++;
		}else{
			table=table+"<td class='LM01Txt'><a href='${contextPath}/sjdbfxAction!showChartAction.do?kid="+kid+"&item="+item.item+"&iname="+item.iname+"&flflag="+flflag+"'>"+item.iname+"</a></td>";
		}
		bFlag++;
	}
	
	//最后一排有多少个字段
	var tds=ywflList.length%4;
	
	if(tds==3){
		table=table+"<td class='LM01Txt'></td></tr></table><br>";
	}else if(tds==2){
		table=table+"<td class='LM01Txt'></td><td class='LM01Txt'></td></tr></table><br>";
	}else if(tds==1){
		table=table+"<td class='LM01Txt'></td><td class='LM01Txt'></td><td class='LM01Txt'></td></tr></table><br>";
	}else{
		table=table+"</tr></table><br>";
	}
	result=result+table;
	return result;
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
 * 把勾选的普通字段进行组装
 */
function packFieldList(){
	var fieldList="";
	for(var i=0;i<dataDiv.all.length;i++){
		var curObj=dataDiv.all[i];
		if(curObj.type=='hidden'){
			var item=curObj.id;
			var itemCheck=$(item+"Check");
			if(itemCheck.checked){
				fieldList=fieldList+curObj.value+"!";
			}
		}
	}
	if(fieldList!=""){
		fieldList=fieldList.substring(0,fieldList.length-1);
	}
	return fieldList;
}

function subForm(){
	var fields=packFieldList();
	$("fields").value=fields;
	document.forms[0].submit();
}