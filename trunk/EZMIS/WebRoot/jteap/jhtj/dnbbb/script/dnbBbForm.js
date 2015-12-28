function loadData(){
	var de = formatDate(new Date(serverDt),"yyyy-MM-dd");
	$("curDate").value=de;
	
	var riArray = $("curDate").value.split("-");
	txtDyj.value = "当月均(01日至" + riArray[2] + "日)";
	
	Ext.Ajax.request({
		url : link1,
		method : 'POST',
		params : {
			curDate:curDate
		},
		success : function(ajax) {
			var responseText = ajax.responseText;
			var obj = Ext.decode(responseText);
			if (obj.success) {
				cols=obj.data;
				for(var i=0;i<cols.length;i++){
					var data=cols[i];
					for (var p in cols[i]) {
						//var curTd=document.getElementById(p);
						//curTd.innerText=1;
						$(p).value=data[p];
					}
				}
				//计算有功增量
//				var codes="19,25,20,26,15,30,16,28,17,29,18,21,22,23,24,3,4,5,6,11,12,7,8,9,10,13,14,1,2";
//				var codeArr=codes.split(",");
//				for(var j=0;j<codeArr.length;j++){
//					$("TOTAL-"+codeArr[j]+"-00").value=computeYgzlz($("PZ-"+codeArr[j]+"-08").value,$("PZ-"+codeArr[j]+"-00").value,$("PF-"+codeArr[j]+"-08").value,$("PF-"+codeArr[j]+"-00").value,$("CT-"+codeArr[j]).value,$("PT-"+codeArr[j]).value);
//					$("TOTAL-"+codeArr[j]+"-08").value=computeYgzlz($("PZ-"+codeArr[j]+"-16").value,$("PZ-"+codeArr[j]+"-08").value,$("PF-"+codeArr[j]+"-16").value,$("PF-"+codeArr[j]+"-08").value,$("CT-"+codeArr[j]).value,$("PT-"+codeArr[j]).value);
//					$("TOTAL-"+codeArr[j]+"-16").value=computeYgzlz($("PZ-"+codeArr[j]+"-24").value,$("PZ-"+codeArr[j]+"-16").value,$("PF-"+codeArr[j]+"-24").value,$("PF-"+codeArr[j]+"-16").value,$("CT-"+codeArr[j]).value,$("PT-"+codeArr[j]).value);
//					$("TOTAL-"+codeArr[j]).value=computeYgzlz($("PZ-"+codeArr[j]+"-24").value,$("PZ-"+codeArr[j]+"-00").value,$("PF-"+codeArr[j]+"-24").value,$("PF-"+codeArr[j]+"-00").value,$("CT-"+codeArr[j]).value,$("PT-"+codeArr[j]).value);
//				}
				//计算1-4号机组的平均负荷
				var jzcodes="23,24,1,2";
				var jzcodeArr=jzcodes.split(",");
				for(var j=0;j<jzcodeArr.length;j++){
					$("JZ-"+jzcodeArr[j]+"-00").value=computePjfh($("TOTAL-"+jzcodeArr[j]+"-00").value,8);
					$("JZ-"+jzcodeArr[j]+"-08").value=computePjfh($("TOTAL-"+jzcodeArr[j]+"-08").value,8);
					$("JZ-"+jzcodeArr[j]+"-16").value=computePjfh($("TOTAL-"+jzcodeArr[j]+"-16").value,8);
					$("JZ-"+jzcodeArr[j]+"-24").value=computePjfh($("TOTAL-"+jzcodeArr[j]+"-24").value,24);
				}
				//计算综合常用电率1期
				$("JZ-1Q-00").value=computeZhcydl1q($("TOTAL-19-00").value,$("TOTAL-20-00").value,$("TOTAL-15-00").value,$("TOTAL-16-00").value,$("TOTAL-17-00").value,$("TOTAL-21-00").value,$("TOTAL-18-00").value,$("TOTAL-23-00").value,$("TOTAL-24-00").value);
				$("JZ-1Q-08").value=computeZhcydl1q($("TOTAL-19-08").value,$("TOTAL-20-08").value,$("TOTAL-15-08").value,$("TOTAL-16-08").value,$("TOTAL-17-08").value,$("TOTAL-21-08").value,$("TOTAL-18-08").value,$("TOTAL-23-08").value,$("TOTAL-24-08").value);
				$("JZ-1Q-16").value=computeZhcydl1q($("TOTAL-19-16").value,$("TOTAL-20-16").value,$("TOTAL-15-16").value,$("TOTAL-16-16").value,$("TOTAL-17-16").value,$("TOTAL-21-16").value,$("TOTAL-18-16").value,$("TOTAL-23-16").value,$("TOTAL-24-16").value);
				$("JZ-1Q-TOTLE").value=computeZhcydl1q($("TOTAL-19-24").value,$("TOTAL-20-24").value,$("TOTAL-15-24").value,$("TOTAL-16-24").value,$("TOTAL-17-24").value,$("TOTAL-21-24").value,$("TOTAL-18-24").value,$("TOTAL-23-24").value,$("TOTAL-24-24").value);
				//计算综合常用电率2期
				$("JZ-2Q-00").value=computeZhcydl2q($("TOTAL-3-00").value,$("TOTAL-5-00").value,$("TOTAL-7-00").value,$("TOTAL-9-00").value,$("TOTAL-18-00").value,$("TOTAL-1-00").value,$("TOTAL-2-00").value);
				$("JZ-2Q-08").value=computeZhcydl2q($("TOTAL-3-08").value,$("TOTAL-5-08").value,$("TOTAL-7-08").value,$("TOTAL-9-08").value,$("TOTAL-18-08").value,$("TOTAL-1-08").value,$("TOTAL-2-08").value);
				$("JZ-2Q-16").value=computeZhcydl2q($("TOTAL-3-16").value,$("TOTAL-5-16").value,$("TOTAL-7-16").value,$("TOTAL-9-16").value,$("TOTAL-18-16").value,$("TOTAL-1-16").value,$("TOTAL-2-16").value);
				$("JZ-2Q-TOTLE").value=computeZhcydl2q($("TOTAL-3-24").value,$("TOTAL-5-24").value,$("TOTAL-7-24").value,$("TOTAL-9-24").value,$("TOTAL-18-24").value,$("TOTAL-1-24").value,$("TOTAL-2-24").value);
				
				$("JZ1-CYJ").value=obj.JZ1;
				$("JZ2-CYJ").value=obj.JZ2;
				$("JZ3-CYJ").value=obj.JZ3;
				$("JZ4-CYJ").value=obj.JZ4;
				$("1Q-CYJ").value = blxs(obj.Q1,2);
				if($("1Q-CYJ").value != ""){
					$("1Q-CYJ").value += "%";
				}
				$("2Q-CYJ").value = blxs(obj.Q2,2);
				if($("2Q-CYJ").value != ""){
					$("2Q-CYJ").value += "%";
				}
			}
		}
	});
}


function reFindData(){
	var curDate=$("curDate").value;
	
	var riArray = $("curDate").value.split("-");
	txtDyj.value = "当月均(01日至" + riArray[2] + "日)";
	
	Ext.Ajax.request({
		url : link1,
		method : 'POST',
		params : {
			curDate:curDate
		},
		success : function(ajax) {
			var responseText = ajax.responseText;
			var obj = Ext.decode(responseText);
			if (obj.success) {
				cols=obj.data;
				for(var i=0;i<cols.length;i++){
					var data=cols[i];
					for (var p in cols[i]) {
						//var curTd=document.getElementById(p);
						//curTd.innerText=1;
						$(p).value=data[p];
					}
				}
				//计算有功增量
//				var codes="19,25,20,26,15,30,16,28,17,29,18,21,22,23,24,3,4,5,6,11,12,7,8,9,10,13,14,1,2";
//				var codeArr=codes.split(",");
//				for(var j=0;j<codeArr.length;j++){
//					$("TOTAL-"+codeArr[j]+"-00").value=computeYgzlz($("PZ-"+codeArr[j]+"-08").value,$("PZ-"+codeArr[j]+"-00").value,$("PF-"+codeArr[j]+"-08").value,$("PF-"+codeArr[j]+"-00").value,$("CT-"+codeArr[j]).value,$("PT-"+codeArr[j]).value);
//					$("TOTAL-"+codeArr[j]+"-08").value=computeYgzlz($("PZ-"+codeArr[j]+"-16").value,$("PZ-"+codeArr[j]+"-08").value,$("PF-"+codeArr[j]+"-16").value,$("PF-"+codeArr[j]+"-08").value,$("CT-"+codeArr[j]).value,$("PT-"+codeArr[j]).value);
//					$("TOTAL-"+codeArr[j]+"-16").value=computeYgzlz($("PZ-"+codeArr[j]+"-24").value,$("PZ-"+codeArr[j]+"-16").value,$("PF-"+codeArr[j]+"-24").value,$("PF-"+codeArr[j]+"-16").value,$("CT-"+codeArr[j]).value,$("PT-"+codeArr[j]).value);
//					$("TOTAL-"+codeArr[j]).value=computeYgzlz($("PZ-"+codeArr[j]+"-24").value,$("PZ-"+codeArr[j]+"-00").value,$("PF-"+codeArr[j]+"-24").value,$("PF-"+codeArr[j]+"-00").value,$("CT-"+codeArr[j]).value,$("PT-"+codeArr[j]).value);
//				}
				//计算1-4号机组的平均负荷
				var jzcodes="23,24,1,2";
				var jzcodeArr=jzcodes.split(",");
				for(var j=0;j<jzcodeArr.length;j++){
					$("JZ-"+jzcodeArr[j]+"-00").value=computePjfh($("TOTAL-"+jzcodeArr[j]+"-00").value,8);
					$("JZ-"+jzcodeArr[j]+"-08").value=computePjfh($("TOTAL-"+jzcodeArr[j]+"-08").value,8);
					$("JZ-"+jzcodeArr[j]+"-16").value=computePjfh($("TOTAL-"+jzcodeArr[j]+"-16").value,8);
					$("JZ-"+jzcodeArr[j]+"-24").value=computePjfh($("TOTAL-"+jzcodeArr[j]+"-24").value,24);
				}
				//计算综合常用电率1期
				$("JZ-1Q-00").value=computeZhcydl1q($("TOTAL-19-00").value,$("TOTAL-20-00").value,$("TOTAL-15-00").value,$("TOTAL-16-00").value,$("TOTAL-17-00").value,$("TOTAL-21-00").value,$("TOTAL-18-00").value,$("TOTAL-23-00").value,$("TOTAL-24-00").value);
				$("JZ-1Q-08").value=computeZhcydl1q($("TOTAL-19-08").value,$("TOTAL-20-08").value,$("TOTAL-15-08").value,$("TOTAL-16-08").value,$("TOTAL-17-08").value,$("TOTAL-21-08").value,$("TOTAL-18-08").value,$("TOTAL-23-08").value,$("TOTAL-24-08").value);
				$("JZ-1Q-16").value=computeZhcydl1q($("TOTAL-19-16").value,$("TOTAL-20-16").value,$("TOTAL-15-16").value,$("TOTAL-16-16").value,$("TOTAL-17-16").value,$("TOTAL-21-16").value,$("TOTAL-18-16").value,$("TOTAL-23-16").value,$("TOTAL-24-16").value);
				$("JZ-1Q-TOTLE").value=computeZhcydl1q($("TOTAL-19-24").value,$("TOTAL-20-24").value,$("TOTAL-15-24").value,$("TOTAL-16-24").value,$("TOTAL-17-24").value,$("TOTAL-21-24").value,$("TOTAL-18-24").value,$("TOTAL-23-24").value,$("TOTAL-24-24").value);
				//计算综合常用电率2期
				$("JZ-2Q-00").value=computeZhcydl2q($("TOTAL-3-00").value,$("TOTAL-5-00").value,$("TOTAL-7-00").value,$("TOTAL-9-00").value,$("TOTAL-18-00").value,$("TOTAL-1-00").value,$("TOTAL-2-00").value);
				$("JZ-2Q-08").value=computeZhcydl2q($("TOTAL-3-08").value,$("TOTAL-5-08").value,$("TOTAL-7-08").value,$("TOTAL-9-08").value,$("TOTAL-18-08").value,$("TOTAL-1-08").value,$("TOTAL-2-08").value);
				$("JZ-2Q-16").value=computeZhcydl2q($("TOTAL-3-16").value,$("TOTAL-5-16").value,$("TOTAL-7-16").value,$("TOTAL-9-16").value,$("TOTAL-18-16").value,$("TOTAL-1-16").value,$("TOTAL-2-16").value);
				$("JZ-2Q-TOTLE").value=computeZhcydl2q($("TOTAL-3-24").value,$("TOTAL-5-24").value,$("TOTAL-7-24").value,$("TOTAL-9-24").value,$("TOTAL-18-24").value,$("TOTAL-1-24").value,$("TOTAL-2-24").value);
				
				$("JZ1-CYJ").value=obj.JZ1;
				$("JZ2-CYJ").value=obj.JZ2;
				$("JZ3-CYJ").value=obj.JZ3;
				$("JZ4-CYJ").value=obj.JZ4;
				$("1Q-CYJ").value = blxs(obj.Q1,2);
				if($("1Q-CYJ").value != ""){
					$("1Q-CYJ").value += "%";
				}
				$("2Q-CYJ").value = blxs(obj.Q2,2);
				if($("2Q-CYJ").value != ""){
					$("2Q-CYJ").value += "%";
				}
				if(obj.firstDate!=undefined){
					$("JZ-23-24").value=obj.JZ1;
					$("JZ-24-24").value=obj.JZ2;
					$("JZ-1-24").value=obj.JZ3;
					$("JZ-2-24").value=obj.JZ4;
				}
			}
		}
	});
}

//导出Excel
function exportDnbsExcel(){
	window.open(link15+"?curDate="+curDate.value);
}

//换表处理
function change(){
	var url = contextPath + '/jteap/jhtj/dnbbb/change.jsp';
	showIFModule(url,"换表处理","true",900,500,obj);
	window.location.reload();
}


/**计算有功增量
**/
function computeYgzlz(zxyg1,zxyg2,fxyg1,fxyg2,ct,pt){
	if(zxyg1 == "" || zxyg2 == "" || fxyg1 == "" || fxyg2 == "" || ct == "" || pt == ""){
		return "";
	}
	return formatNum(((zxyg1-zxyg2)-(fxyg1-fxyg2))*ct*pt,4);
}

/**计算1-4号机组的平均负荷
**/
function computePjfh(zl,bs){
	if(zl == ""){
		return "";
	}
	return formatNum(zl/bs,4);
}

/**计算综合常用电率1期
**/
function computeZhcydl1q(cs1,cs2,cs3,cs4,cs5,cs6,cs7,cs8,cs9){
	if(cs1 == "" || cs2 == "" || cs3 == "" || cs4 == "" || cs5 == "" 
	|| cs6 == "" || cs7 == "" || cs8 == "" || cs9 == ""){
		return "";
	}
	
	var folCs1=parseFloat(cs1);
	var folCs2=parseFloat(cs2);
	var folCs3=parseFloat(cs3);
	var folCs4=parseFloat(cs4);
	var folCs5=parseFloat(cs5);
	var folCs6=parseFloat(cs6);
	var folCs7=parseFloat(cs7);
	var folCs8=parseFloat(cs8);
	var folCs9=parseFloat(cs9);
	
	var result = formatNum((1-(folCs1+folCs2+folCs3+folCs4+folCs5+folCs6+folCs7)/(folCs8+folCs9+1))*100,4);
	return blxs(result,2)+"%";
}
/**计算综合常用电率2期
**/
function computeZhcydl2q(cs1,cs2,cs3,cs4,cs5,cs6,cs7){
	if(cs1 == "" || cs2 == "" || cs3 == "" || cs4 == "" || cs5 == "" 
	|| cs6 == "" || cs7 == ""){
		return "";
	}
	
	var folCs1=parseFloat(cs1);
	var folCs2=parseFloat(cs2);
	var folCs3=parseFloat(cs3);
	var folCs4=parseFloat(cs4);
	var folCs5=parseFloat(cs5);
	var folCs6=parseFloat(cs6);
	var folCs7=parseFloat(cs7);
	
	var result = formatNum((1-(folCs1+folCs2+folCs3+folCs4-folCs5)/(folCs6+folCs7+1))*100,4);
	return blxs(result,2)+"%";
}

/**保留小数
 * @param {} result
 * @param {} num
 */
function blxs(result, num){
	result = result+"";
	if(result.indexOf(".") != -1){
		var arrayResult = result.split(".");
		var a = arrayResult[0];
		var b = arrayResult[1];
		if(b.length > num){
			b = b.substring(0,num);
		}
		result = a + "." + b;
	}
	return result;
}

/**四舍五入
**/
function formatNum(Num1,Num2){
     if(isNaN(Num1)||isNaN(Num2)){
           return(0);
     }else{
           Num1=Num1.toString();
           Num2=parseInt(Num2);
           if(Num1.indexOf('.')==-1){
                 return(Num1);
           }else{
                 var b=Num1.substring(0,Num1.indexOf('.')+Num2+1);
                 var c=Num1.substring(Num1.indexOf('.')+Num2+1,Num1.indexOf('.')+Num2+2);
                 if(c==""){
                       return(b);
                 }else{
                       if(parseInt(c)<5){
                             return(b);
                       }else{
                             return((Math.round(parseFloat(b)*Math.pow(10,Num2))+Math.round(parseFloat(Math.pow(0.1,Num2).toString().substring(0,Math.pow(0.1,Num2).toString().indexOf('.')+Num2+1))*Math.pow(10,Num2)))/Math.pow(10,Num2));
                       }
                 }
           }
     }
}

function submitTopDay() {//上一天  
  var curDate=$("curDate").value;          
  d = new Date(Date.parse(curDate.replace(/-/g,   '/')));       
  d.setDate(d.getDate()-1);       
  t = [d.getYear(),d.getMonth()+1,d.getDate()];       
  $("curDate").value=t.join('-');
  reFindData();
}    
   
   
function submitNextDay(date) {//下一天  
  var curDate=$("curDate").value;           
  d = new Date(Date.parse(curDate.replace(/-/g,'/')));       
  d.setDate(d.getDate()+1);       
  t = [d.getYear(), d.getMonth()+1, d.getDate()];       
  $("curDate").value=t.join('-');   
  reFindData();  
} 