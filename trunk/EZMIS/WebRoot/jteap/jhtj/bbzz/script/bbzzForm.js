var oExcel; // 父窗口的Excel对象
var bbindexid=oJson.bbindexid;
var sqlValue=oJson.sqlValue;
var box = null;
var bFull=false;	//是否全屏状态

function onload(){
	var url = null;
	box = new LightBox("idBox");
	box.Over = true;
	box.OverLay.Color = "#000";
	box.OverLay.Opacity = 50;
	box.Center = true;
	var param={};
	param.bbindexid=bbindexid;
	param.sqlValue=sqlValue;
	AjaxRequest_Sync(link13, param, function(req) {
		var responseText=req.responseText;	
		var responseObject=Ext.util.JSON.decode(responseText);
		if(responseObject.success){
			//只有增加的时候才生成数据,修改的时候的只是取数据
			var isGenerateValue=true;
			if(id==""){
				url = contextPath+ "/jteap/jhtj/bbsj/bbSjAction!readExcelBlobAction.do?indexid=" + bbindexid;
			}else{
				isGenerateValue=false;
				url = contextPath+ "/jteap/jhtj/bbzz/bbzzAction!readExcelBlobAction.do?id=" + id;
			}
			TANGER_OCX_OpenDoc(url);
			ntkoDiv.style.display = "block";
			splash.style.display = "none";
			
			oExcel = document.TANGER_OCX;
			oExcel.Activate(false);
			//使批注显示小红图标
			setExcelConfig();
			if(isGenerateValue){
				generateValue(responseObject.data);
			}else{
				if(isUpdate=="false"){
					TANGER_OCX_OBJ.SetReadOnly(true,"");
					$("saveButton").style.display="none";
					$("exportButton").style.display="none";
				}
			}
			box.Close();
		}
	});
	switchFullScreen();	 
}

function generateValue(value){
	var oExcel = document.TANGER_OCX;
	
	for(var i=0;i<value.length;i++){
	      var numCount=parseInt(value[i].dnum);//填充行数
	      var merge=value[i].merge;
	      var meg="";//是否合并
	      if(merge=="true"){
	      	meg=1;
	      }else{
	      	meg=2;
	      }
	      var fx =parseInt(value[i].fx);//填充方式 1为横向，2为纵向
	      var ce=value[i].ce;
          var row = parseInt(ce.split(":")[0]);//横坐标
	      var col = parseInt(ce.split(":")[1]);//纵坐标
	      var keyValue = value[i].value;//填充的数据
	      //alert("value:"+keyValue);
	      var temp1=keyValue.split(",");
	      //alert("temp1:"+temp1.length);
	      //alert(temp1);
	      //存放位置
	      var numList=new Array();
	      var numFlag=0;
	      //存放一次位置
	      var oneNum=new Array();
	      var oneFlag=0;
	      //合并
	      if(meg=="1"){
		      var flag=0;
		      if(temp1.length>1){
		      	 var pre="";
		      	 for(var t=0;t<temp1.length;t++){
		      	 	if(pre==""){
		      	 		pre=temp1[t];
		      	 		oneNum[oneFlag]=t;
		      	 	}else{
		      	 		if(pre==temp1[t]){
		      	 			flag++;
		      	 		}else{
		      	 			numList[numFlag]=oneNum[oneFlag]+"-"+flag;
		      	 			numFlag++;
		      	 			oneFlag=0;
		      	 			flag=0;
		      	 			pre=temp1[t];
		      	 			oneNum[oneFlag]=t;
		      	 		}
		      	 	}
		      	 }
		      }
		  }
		  if(keyValue!=null&&keyValue!=''){
              var data1 = keyValue.split(",");
              //如果为纵向填充，则循环横坐标+1 
              if(fx==1){
              		//不控制
                  	if(numCount==0){
                  		if(numList.length>0){
                  	  		for(var k=0;k<numList.length;k++){
                  	  			var wz=numList[k];
                  	  			var wzs=wz.split("-");
                  	  			var wz1=parseInt(wzs[0]);
                  	  			var wz2=parseInt(wzs[1]);
                  	  			if(wz2>0){
                  	  			    if(TANGER_OCX.GetOfficeVer()!=100){
                          	  			oExcel.ActiveDocument.Application.Range(oExcel.ActiveDocument.Application.Cells(wz1+row,col),oExcel.ActiveDocument.Application.Cells(wz2+row+wz1,col)).MergeCells=true;
                          	  			oExcel.ActiveDocument.Application.Range(oExcel.ActiveDocument.Application.Cells(wz1+row,col),oExcel.ActiveDocument.Application.Cells(wz2+row+wz1,col)).HorizontalAlignment=3;
                          	  			oExcel.ActiveDocument.Application.Range(oExcel.ActiveDocument.Application.Cells(wz1+row,col),oExcel.ActiveDocument.Application.Cells(wz2+row+wz1,col)).VerticalAlignment=2;
                          	  		}
                          	  		if(TANGER_OCX.GetWPSVer()!=100){
                          	  			oExcel.ActiveDocument.Application.Range(oExcel.ActiveDocument.Application.Cells(wz1+row,col),oExcel.ActiveDocument.Application.Cells(wz2+row+wz1,col)).MergeCells=true;
                          	  			oExcel.ActiveDocument.Application.Range(oExcel.ActiveDocument.Application.Cells(wz1+row,col),oExcel.ActiveDocument.Application.Cells(wz2+row+wz1,col)).HorizontalAlignment=-4108;
                          	  			oExcel.ActiveDocument.Application.Range(oExcel.ActiveDocument.Application.Cells(wz1+row,col),oExcel.ActiveDocument.Application.Cells(wz2+row+wz1,col)).VerticalAlignment=-4108;
                          	  			
                          	  		}
                  	  			}
                  	  		}
                      	}
              			for(var j=0;j<data1.length;j++){
              				oExcel.ActiveDocument.Application.Cells(row,col).Value=data1[j];
                  			row++;
                  		}
                  	  	
                  	}else{
                  		if(numList.length>0){
                  	  		for(var k=0;k<numList.length;k++){
                  	  			var wz=numList[k];
                  	  			var wzs=wz.split("-");
                  	  			var wz1=parseInt(wzs[0]);
                  	  			var wz2=parseInt(wzs[1]);
                  	  			//最后的位置
                  	  			var rowsRes=wz2+row;
                  	  			//一开始的位置
                  	  			var stars=wz1+row;
                  	  			//扩充到的行数
                  	  			var ts=numCount+row;
								if(stars<ts&&rowsRes>ts){
									rowsRes=ts-1;
								}
                  	  			if(wz2>0){
                  	  				if(TANGER_OCX.GetOfficeVer()!=100){
                          	  			oExcel.ActiveDocument.Application.Range(oExcel.ActiveDocument.Application.Cells(stars,col),oExcel.ActiveDocument.Application.Cells(rowsRes+wz1,col)).MergeCells=true;
                          	  			oExcel.ActiveDocument.Application.Range(oExcel.ActiveDocument.Application.Cells(stars,col),oExcel.ActiveDocument.Application.Cells(rowsRes+wz1,col)).HorizontalAlignment=3;
                          	  			oExcel.ActiveDocument.Application.Range(oExcel.ActiveDocument.Application.Cells(stars,col),oExcel.ActiveDocument.Application.Cells(rowsRes+wz1,col)).VerticalAlignment=2;
                  	  				}
                  	  				
                  	  				if(TANGER_OCX.GetWPSVer()!=100){
                  	  					oExcel.ActiveDocument.Application.Range(oExcel.ActiveDocument.Application.Cells(stars,col),oExcel.ActiveDocument.Application.Cells(rowsRes+wz1,col)).MergeCells=true;
                          	  			oExcel.ActiveDocument.Application.Range(oExcel.ActiveDocument.Application.Cells(stars,col),oExcel.ActiveDocument.Application.Cells(rowsRes+wz1,col)).HorizontalAlignment=-4108;
                          	  			oExcel.ActiveDocument.Application.Range(oExcel.ActiveDocument.Application.Cells(stars,col),oExcel.ActiveDocument.Application.Cells(rowsRes+wz1,col)).VerticalAlignment=-4108;
                  	  				}
                  	  			}
                  	  		}
                      	}
                      	
                  		for(var j=0;j<numCount;j++){
                  			oExcel.ActiveDocument.Application.Cells(row,col).Value=data1[j];
                  			row++;
                  		}
                  	}                 
                  
              //如果为横向填充，则循环纵坐标+1   
              }else if(fx==2){
                    //不控制
                  	if(numCount==0){
                  		if(numList.length>0){
                  	  		for(var k=0;k<numList.length;k++){
                  	  			var wz=numList[k];
                  	  			alert(wz);
                  	  			var wzs=wz.split("-");
                  	  			var wz1=parseInt(wzs[0]);
                  	  			var wz2=parseInt(wzs[1]);
                  	  			if(wz2>0){
                  	  				if(TANGER_OCX.GetOfficeVer()!=100){
                          	  			oExcel.ActiveDocument.Application.Range(oExcel.ActiveDocument.Application.Cells(row,wz1+col),oExcel.ActiveDocument.Application.Cells(row,wz2+col+wz1)).MergeCells=true;
                          	  			oExcel.ActiveDocument.Application.Range(oExcel.ActiveDocument.Application.Cells(row,wz1+col),oExcel.ActiveDocument.Application.Cells(row,wz2+col+wz1)).HorizontalAlignment=3;
                          	  			oExcel.ActiveDocument.Application.Range(oExcel.ActiveDocument.Application.Cells(row,wz1+col),oExcel.ActiveDocument.Application.Cells(row,wz2+col+wz1)).VerticalAlignment=2;
                  	  				}
                  	  				
                  	  				if(TANGER_OCX.GetWPSVer()!=100){
                  	  					oExcel.ActiveDocument.Application.Range(oExcel.ActiveDocument.Application.Cells(row,wz1+col),oExcel.ActiveDocument.Application.Cells(row,wz2+col+wz1)).MergeCells=true;
                          	  			oExcel.ActiveDocument.Application.Range(oExcel.ActiveDocument.Application.Cells(row,wz1+col),oExcel.ActiveDocument.Application.Cells(row,wz2+col+wz1)).HorizontalAlignment=-4108;
                          	  			oExcel.ActiveDocument.Application.Range(oExcel.ActiveDocument.Application.Cells(row,wz1+col),oExcel.ActiveDocument.Application.Cells(row,wz2+col+wz1)).VerticalAlignment=-4108;
                  	  				}
                  	  			}
                  	  			//alert("star:"+(wz1+col)+" end:"+(wz2+col+wz1));
                  	  		}
                         }
                         
                  		for(var j=0;j<data1.length;j++){
                  			oExcel.ActiveDocument.Application.Cells(row,col).Value=data1[j];
                  			col++;
                  		}
                  	}else{
                  		if(numList.length>0){
                  	  		for(var k=0;k<numList.length;k++){
                  	  			var wz=numList[k];
                  	  			var wzs=wz.split("-");
                  	  			var wz1=parseInt(wzs[0]);
                  	  			var wz2=parseInt(wzs[1]);
                  	  			//最后的位置
                  	  			var rowsRes=wz2+col;
                  	  			//一开始的位置
                  	  			var stars=wz1+col;
                  	  			//扩充到的行数
                  	  			var ts=numCount+col;
								if(stars<ts&&rowsRes>ts){
									rowsRes=ts-1;
								}
                  	  			if(wz2>0){
                  	  				if(TANGER_OCX.GetOfficeVer()!=100){
                          	  			oExcel.ActiveDocument.Application.Range(oExcel.ActiveDocument.Application.Cells(row,stars),oExcel.ActiveDocument.Application.Cells(row,rowsRes+wz1)).MergeCells=true;
                          	  			oExcel.ActiveDocument.Application.Range(oExcel.ActiveDocument.Application.Cells(row,stars),oExcel.ActiveDocument.Application.Cells(row,rowsRes+wz1)).HorizontalAlignment=3;
                          	  			oExcel.ActiveDocument.Application.Range(oExcel.ActiveDocument.Application.Cells(row,stars),oExcel.ActiveDocument.Application.Cells(row,rowsRes+wz1)).VerticalAlignment=2;
                  	  				}
                  	  				
                  	  				if(TANGER_OCX.GetWPSVer()!=100){
                  	  					oExcel.ActiveDocument.Application.Range(oExcel.ActiveDocument.Application.Cells(row,stars),oExcel.ActiveDocument.Application.Cells(row,rowsRes+wz1)).MergeCells=true;
                          	  			oExcel.ActiveDocument.Application.Range(oExcel.ActiveDocument.Application.Cells(row,stars),oExcel.ActiveDocument.Application.Cells(row,rowsRes+wz1)).HorizontalAlignment=-4108;
                          	  			oExcel.ActiveDocument.Application.Range(oExcel.ActiveDocument.Application.Cells(row,stars),oExcel.ActiveDocument.Application.Cells(row,rowsRes+wz1)).VerticalAlignment=-4108;
                  	  				}
                  	  			}
                  	  		}
                      	}
                      	
                  		for(var j=0;j<numCount;j++){
                  			oExcel.ActiveDocument.Application.Cells(row,col).Value=data1[j];
                  			col++;
                  		}
                  	}             
              }                                           
        }else{
        	//值为空的时候替换成空
        	oExcel.ActiveDocument.Application.Cells(row,col).Value="";
        }
	}
}



/**
 * 全屏设计与非全屏设计之间切换
 */
function switchFullScreen() {
	if (bFull) {
		bFull = false;
		window.dialogHeight = '600px';
		window.dialogWidth = '700px';
		$("btnFull").value = "全屏查看";
	} else {
		bFull = true;
		var width = window.screen.availWidth;
		var height = window.screen.availHeight;
		TANGER_OCX_OBJ.focus();
		window.dialogHeight = height;
		window.dialogWidth = width;
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
		$("ntkoDiv").style.height = "540px";
	}
}

function exportExcelForm(){
	TANGER_OCX_OBJ.WebFileName = "excel.xls";
	TANGER_OCX_OBJ.ShowDialog (2);
	//TANGER_OCX_OBJ.SaveToLocal("c:/"+title,true);
	//alert("文件成功导出："+"c:/"+title+".xls");
}


function subForm(){
	$("bbindexid").value=bbindexid;
	$("sqlValue").value=sqlValue;
	oExcel.Activate(false);
	TANGER_OCX_SaveDoc();
	window.returnValue="true";
	window.close();
}

function cancel(){
	window.close();
}