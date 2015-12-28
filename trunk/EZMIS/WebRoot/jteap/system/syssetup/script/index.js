/**
 * 初始化页面参数
 */
function initPage(){
	var url = CONTEXT_PATH + "/jteap/system/SystemFuncAction!getSystemConfigAction.do";
	AjaxRequest_Sync(url,null,function(req){
		var responseText = req.responseText;
		eval("var responseObj ="+ responseText);
		if(responseObj.success && responseObj.success=='1'){
			var config = responseObj.config;
			initConfigValue(config);
		}else{
			alert("加载数据出现异常");
		}
	});
}

/**
 * 初始化控件的值
 */
function initConfigValue(config){
	var inputs = document.all;
	for(var i=0;i<inputs.length;i++){
		var input = inputs[i];
		var name = input.name;
		if(name && (idx=name.indexOf("param_"))>=0){
			
			var key = name.substring(6);
			var value = eval("config."+key);
			input.value=value;
			
		}
	}
}

function submitForm(){
	//var param = Form.
	/*var myAjax = new Ajax.Request( 
    url, 
    {method: 'get', parameters: pars, onComplete: yourfunction} 
    ); */
	
	/*Form.request("form1",{onComplete:function(){
		alert('x');
	}});*/
    var url = contextPath + "/jteap/system/SystemFuncAction!saveSystemConfigAction.do";
    var params = Form.serialize("form1");
    var myAjax = new Ajax.Request(url,{
    	method: 'post', 
    	parameters: params, 
    	onComplete: function(req){
    		var responseText = req.responseText;
    		eval("var responseObj = "+responseText);
    		if(responseObj && responseObj.success == '1'){
    			alert('保存成功');
    		}else{
    			alert('保存失败：'+responseObj.msg);
    		}
    	}
    });
}
