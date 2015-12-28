
/**
 * 初始化页面参数
 */
function initExtend(){
	AjaxRequest_Sync(link25,null,function(req){
		//初始化扩展信息
		var configData="";
		var resource={};
		var xmlDom=getDom(req.responseText);
		var config=xmlDom.childNodes[1];
		//记录扩展信息的名称
		var infoArray=new Array();
   		for(var i=0;i<config.childNodes.length;i++){
   			var oconfig=config.childNodes[i];
   			if(oconfig.tagName=="property"){
   				//信息ID
			    var sConfigId=oconfig.getAttribute("id");
			    //信息名
			    var sConfigName=oconfig.getAttribute("name");
			    infoArray.push(sConfigName);
			    configData=configData+"<span class='sp1'><span class='sp2'>"+sConfigName+"</span>";
			    //信息默认值
			    var sConfigValue=oconfig.getAttribute("value");
			    if(resource[sConfigName]!=null){
		   			sConfigValue=resource[sConfigName];
		   		}
	   			var sConfigType=oconfig.getAttribute("type");
	   			if(sConfigType=="Date"){
	   			  	//"2006-10-15T00:00:00解析成 2006/10/15
		   			var index=sConfigValue.indexOf("T");
		   			if(index!=-1){
		   			  	sConfigValue=sConfigValue.substring(0,index);
			   			
		   			}
		   			while(sConfigValue.indexOf("/")!=-1){
			   			 sConfigValue=sConfigValue.replace("/","-");
			   		}
	   				//configData[sConfigName]=new Date(Date.parse(sConfigValue));
		   			configData=configData+"<input type='text' name='"+sConfigName+"' value='"+sConfigValue+"' class='Wdate' onFocus='WdatePicker({readOnly:true})'></span>";
	   			}else if(sConfigType=="Enum"){
	   				var sConfigMode=oconfig.getAttribute("mode");
	   				var sConfigEnumValue=oconfig.getAttribute("enumValue");
	   				configData=configData+"<select name='"+sConfigName+"'>";
	   				if(sConfigMode=="local"){
	   					var vals=sConfigEnumValue.split("#");
	   					for(var j=0;j<vals.length;j++){
	   						var proAndVal=vals[j].split(":");
	   						if(proAndVal.length==2){
	   							if(sConfigValue==proAndVal[1]){
	   								configData=configData+"<option selected='selected' value='"+proAndVal[1]+"'>"+proAndVal[0]+"</option>";
	   							}else{
	   								configData=configData+"<option value='"+proAndVal[1]+"'>"+proAndVal[0]+"</option>";
	   							}
	   						}
	   					}
	   				}else if(sConfigMode=="remote"){
	   					var sConfigUrl=oconfig.getAttribute("enumValue");
	   					AjaxRequest_Sync(contextPath+sConfigUrl,null,function(reqs){
	   						var responseT = reqs.responseText;
							var obj = responseT.evalJSON();
							var data=obj.data;
							for(var j=0;j<data.length;j++){
								if(sConfigValue==data[j].value){
		   							configData=configData+"<option selected='selected' value='"+data[j].value+"'>"+data[j].name+"</option>";
								}else{
									configData=configData+"<option value='"+data[j].value+"'>"+data[j].name+"</option>";
								}
		   					}
	   					});
	   				}
	   				configData=configData+"</select></span>";
	   			}else{
	   				configData=configData+"<input type='text' name='"+sConfigName+"' value='"+sConfigValue+"'></span>";
	   			}
   			}
   		}	
   		$("extendinfo").innerHTML=configData;
   		//如果是修改，那么把值初始化到页面上面来
   		if(personid!=""){
   			var param={};
   			param.personid=personid;
   			AjaxRequest_Sync(link41,param,function(ajax){
   				var responseText=ajax.responseText;	
				var responseObject=responseText.evalJSON();
				if(responseObject.success){
					//基本信息
					var data=responseObject.data[0];
					$("userLoginName").value=isEmptyObj(data.userLoginName);
					$("userName").value=isEmptyObj(data.userName);
					$("sex").value=isEmptyObj(data.sex);
					$("birthday").value=isEmptyObj(data.birthday);
					if($("birthday").value!=""){
			   			$("birthday").value=formatDate(new Date(data.birthday.time),"yyyy-MM-dd"); 
			   		}
					
					$("mz").value=isEmptyObj(data.mz);
			   		$("csd").value=isEmptyObj(data.csd);
			   		$("userName2").value=isEmptyObj(data.userName2);
			   		$("jg").value=isEmptyObj(data.jg);
			   		//formatDate(new Date(value),"yyyy-MM-dd");
			   		$("cjgzrq").value=isEmptyObj(data.cjgzrq);
			   		if($("cjgzrq").value!=""){
			   			$("cjgzrq").value=formatDate(new Date(data.cjgzrq.time),"yyyy-MM-dd"); 
			   		}
			   		$("jsglrq").value=isEmptyObj(data.jsglrq);
			   		if($("jsglrq").value!=""){
			   			$("jsglrq").value=formatDate(new Date(data.jsglrq.time),"yyyy-MM-dd"); 
			   		}
			   		$("jkzk").value=isEmptyObj(data.jkzk);
			   		$("zwjb").value=isEmptyObj(data.zwjb);
			   		$("sfz").value=isEmptyObj(data.sfz);
			   		$("sbh").value=isEmptyObj(data.sbh);
			   		$("jrdwsj").value=isEmptyObj(data.jrdwsj);
			   		if($("jrdwsj").value!=""){
			   			$("jrdwsj").value=formatDate(new Date(data.jrdwsj.time),"yyyy-MM-dd"); 
			   		}
			   		$("ygxz").value=isEmptyObj(data.ygxz);
			   		$("hkxz").value=isEmptyObj(data.hkxz);
			   		$("hkszd").value=isEmptyObj(data.hkszd);
			   		$("hyzk").value=isEmptyObj(data.hyzk);
			   		$("sj").value=isEmptyObj(data.sj);
			   		$("bgdh").value=isEmptyObj(data.bgdh);
			   		$("jtdz").value=isEmptyObj(data.jtdz);
			   		$("dzyj").value=isEmptyObj(data.dzyj);
			   		$("hbgb").value=isEmptyObj(data.hbgb);
			   		$("schbrq").value=isEmptyObj(data.schbrq);
			   		if($("schbrq").value!=""){
			   			$("schbrq").value=formatDate(new Date(data.schbrq.time),"yyyy-MM-dd"); 
			   		}
			   		$("grsf").value=isEmptyObj(data.grsf);
			   		$("ygdwbs").value=isEmptyObj(data.ygdwbs);
			   		
			   		if(readOnly!=""){
			   			setReadOnly();
			   		}
			   		
			   		//扩展信息
			   		if(data.config!=""){
			   			$("config").value=data.config;
			   			//json化
			   			var config=$("config").value.evalJSON();
			   			for(var j=0;j<infoArray.length;j++){
			   				if(config[infoArray[j]]!=null){
			   					$(infoArray[j]).value=config[infoArray[j]];
			   					if(readOnly!=""){
			   						$(infoArray[j]).readOnly=true;
			   					}
			   				}
			   			}
			   		}
			   		
				}
   			});
   		}
	});
}

/**
 * 保存页面信息
 */
function saveData(){
	if(_submit1()){
		AjaxRequest_Sync(link25,null,function(req){
			//封装扩展属性
			var configData='';
			var xmlDom=getDom(req.responseText);
			var config=xmlDom.childNodes[1];  
	   		for(var i=0;i<config.childNodes.length;i++){
	   			var oconfig=config.childNodes[i];
	   			if(oconfig.tagName=="property"){
	   				//var sConfigId=oconfig.getAttribute("id");
	   				var sConfigName=oconfig.getAttribute("name");
	   				configData=configData+'"'+sConfigName+'":'+'"'+$(sConfigName).value+'",';
	   			}
	   		}
	   		if(configData!=''){
	   			configData=configData.substring(0,configData.length-1);
	   			configData="{"+configData+"}"
	   			$("config").value=configData;
	   		}
	
	   		var param={};
	   		param.personid=personid;
	   		param.userLoginName=$("userLoginName").value;
	   		param.userName=$("userName").value;
	   		param.sex=$("sex").value;
	   		param.birthday=$("birthday").value;
	   		param.mz=$("mz").value;
	   		param.csd=$("csd").value;
	   		param.userName2=$("userName2").value;
	   		param.jg=$("jg").value;
	   		param.cjgzrq=$("cjgzrq").value;
	   		param.jsglrq=$("jsglrq").value;
	   		param.jkzk=$("jkzk").value;
	   		param.zwjb=$("zwjb").value;
	   		param.sfz=$("sfz").value;
	   		param.sbh=$("sbh").value;
	   		param.jrdwsj=$("jrdwsj").value;
	   		param.ygxz=$("ygxz").value;
	   		param.hkxz=$("hkxz").value;
	   		param.hkszd=$("hkszd").value;
	   		param.hyzk=$("hyzk").value;
	   		param.sj=$("sj").value;
	   		param.bgdh=$("bgdh").value;
	   		param.jtdz=$("jtdz").value;
	   		param.dzyj=$("dzyj").value;
	   		param.hbgb=$("hbgb").value;
	   		param.schbrq=$("schbrq").value;
	   		param.grsf=$("grsf").value;
	   		param.ygdwbs=$("ygdwbs").value;
	   		param.config=$("config").value;
	   		AjaxRequest_Sync(link38,param,function(reqs){
	   			var responseText=reqs.responseText;	
				var responseObject=responseText.evalJSON();
				if(responseObject.success){
					alert("保存成功!");
					doCancel();
				}
	   		});
	   		
		});
	}
}

/**
 * 上传附件
 */
function uploadAcc(){
	var accessorie=$("upload").value;
	if(accessorie!=""){
		$("ulButton").disabled=true;
		uploadForm.target="accIframe";
		uploadForm.submit();
	}
}

function uploadIcon(){
	var icon=$("upload").value;
	if(icon!=""){
		$("uploadButton").disabled=true;
		form1.action=form1.action+"?personid="+personid;
		form1.submit();
	}
}

/**
 * 加载附件信息
 */
function loadAccInfo(){
	var content="";
	var param={};
	param.personid=personid;
	AjaxRequest_Sync(link37,param,function(req){
		var responseText=req.responseText;	
		var responseObject=responseText.evalJSON();
		if(responseObject.success){
			var data=responseObject.data;
			for(var i=0;i<data.length;i++){
				content=content+"<tr><td>"+(i+1)+"</td><td>"+data[i].fileName+"</td><td>"+data[i].fjlx+"</td><td>"+data[i].fjdx+"</td><td><a href='"+link42+"?accid="+data[i].id+"'>下载</a>&nbsp;&nbsp;<a href='"+link43+"?accid="+data[i].id+"'>删除</a></td></tr>";
			}
		}
	});
	if(content!=""){
		content="<table width='700'  cellpadding='0' cellspacing='0'><tr><td>序号</td><td>文件名</td><td>文件类型</td><td>文件大小(字节)</td><td>操作</td></tr>"+content+"<tr><td colspan='5'><input type='file' name='upload'><input type='button' value='上传' width='20' style='height:18px;' name='ulButton' onclick='uploadAcc();'></td></tr></table>";
		$("accDiv").innerHTML=content;
	}
	if(readOnly!=""){
		$("ulButton").style.display="none";
		$("upload").style.display="none";
	}
}

/**
 * 加载用户图像
 */
function onlaodIcon(){
	var param={};
	param.personid=personid;
	AjaxRequest_Sync(link40,param,function(req){
		var responseText=req.responseText;	
		var responseObject=responseText.evalJSON();
		if(responseObject.success){
			$("icon").src=link39+"?personid="+personid;
		}
	});
	if(readOnly!=""){
		$("upload").style.display="none";
		$("uploadButton").style.display="none";
	}
}

function isEmptyObj(obj){
	if(obj==""||obj==null){
		return "";
	}
	return obj;
}

function setReadOnly(){
	$("mz").readOnly=true;
	$("csd").readOnly=true;
	$("userName2").readOnly=true;
	$("jg").readOnly=true;
	$("cjgzrq").readOnly=true;
	$("jsglrq").readOnly=true;
	$("jkzk").readOnly=true;
	$("zwjb").readOnly=true;
	$("sfz").readOnly=true;
	$("sbh").readOnly=true;
	$("jrdwsj").readOnly=true;
	$("ygxz").readOnly=true;
	$("hkxz").readOnly=true;
	$("hkszd").readOnly=true;
	$("hyzk").readOnly=true;
	$("sj").readOnly=true;
	$("bgdh").readOnly=true;
	$("jtdz").readOnly=true;
	$("dzyj").readOnly=true;
	$("hbgb").readOnly=true;
	$("schbrq").readOnly=true;
	$("grsf").readOnly=true;
	$("ygdwbs").readOnly=true;
	$("saveButton").style.display="none";
}