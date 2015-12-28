var pWin = window.dialogArguments.opener;		//


function onload(){

}
//单元格标识
var txtUserName=new Ext.form.TextField({disabled:false,width:190,maxLength : 50,allowBlank :false,fieldLabel : "用户名",name : "userLoginName"});
var txtPwd=new Ext.form.TextField({inputType:"password",disabled:false,width:190,maxLength : 50,allowBlank :false,fieldLabel : "密码",name : "userPassword"});


var simpleForm = new Ext.FormPanel({
    labelAlign: 'left',
    buttonAlign:'right',
    id:"myForm",
	style:'margin:2px',
    bodyStyle:'padding:0px',
    waitMsgTarget: true,
    width: '100%',
    frame:true, 					//圆角风格
    labelWidth:70,					//标签宽度
    items:[{
       		layout:'column',
	        border:false,
	        labelSeparator:'：',
	        defaults:{
	        	blankText:'必填字段'
	        },
	        items:[{
	        	//第一行布局
	        	columnWidth:1,
	        	layout:'form',
	        	border:false,
	        	items:[txtUserName]
	        },{
	        	//第一行布局
	        	columnWidth:1,
	        	layout:'form',
	        	border:false,
	        	items:[txtPwd]
	        }]
        }],
	buttons: [{
		text:'确定',
		handler:function(){
			if(!simpleForm.form.isValid()){
				alert('数据校验失败，请检查填写的数据是否正确');
				return;
			}
			
			var url = contextPath + "/jteap/system/person/PersonAction!validateNameAndPasswordAction.do";
			var params = Form.serialize($("myForm"));
			//提交数据
		 	Ext.Ajax.request({
		 		url:url,
		 		success:function(ajax){
		 			var responseText=ajax.responseText;	
		 			var responseObj=Ext.util.JSON.decode(responseText);
		 			if(responseObj.unique!=null && responseObj.unique == true){
						//验证成功
						window.returnValue = responseObj.person;
						window.close();
					}else{
						alert("用户名或密码错误,验证未通过");
						
					}
		 		},
		 		failure:function(){
		 			alert("提交失败");
		 		},
		 		method:'POST',
		 		params:params
		 	})
		 	
		 	
			
		}
	},{
        text: '取消',
            handler:function(){
            	window.close();
            }
    }]
});
var lyCenter = {
	id : 'center-panel',
	region : 'center',
	items:[simpleForm]
}


var titlePanel=new Ext.app.TitlePanel({caption:'签名验证',border:false,region:'north'});