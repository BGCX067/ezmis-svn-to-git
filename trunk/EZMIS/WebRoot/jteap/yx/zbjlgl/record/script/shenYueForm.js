/**
 * 领导审阅窗口
 */
var ShenYueFormWindow=function(zbsj, zbbc, gwlb){
	
	Ext.form.Field.prototype.msgTarget = 'side';
	var formWindow=this;
	
	//用户名
	var txtUserLoginName = {
		xtype:'textfield',
		id:'userLoginName',
		name:'userLoginName',
		fieldLabel:'用户名',
		anchor:'90%',
		minLength:4,
		maxLength:20,
		allowBlank:false
	};
	
	//密码
	var txtUserPassword = {
		xtype:'textfield',
		inputType:'password',
		id:'userPassword',
		name:'userPassword',
		fieldLabel:'当前密码',
		vtype:'alphanum',
		minLengthText:'最少需要4位字母数字组合',
		maxLengthText:'最长10位字母数字组合',
		anchor:'90%',
		maxLength:10,
		minLength:4,
		allowBlank:false
	};
	
	//用户表单定义
	var simpleForm = new Ext.FormPanel({
	    labelAlign: 'left',
	    buttonAlign:'right',
	    bodyStyle:'padding:5px',
	    waitMsgTarget: true,
	    width: 300,
	    frame:true, 					//圆角风格
	    labelWidth:80,					//标签宽度
		items: [{
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
	        	items:[txtUserLoginName]
	        },{
				//第二行布局
	        	columnWidth:1,
	        	layout:'form',
	        	border:false,
	        	items:[txtUserPassword]
	        }]
		}],
		buttons: [{
			text:'保存',
			handler:function(){
				if(!simpleForm.form.isValid()){
					alert('数据校验失败，请检查填写的数据格式是否正确');
					return;
				}
				
				Ext.Ajax.request({
            		url:link7,
            		params: {userLoginName:$("userLoginName").value, userPassword:$("userPassword").value, zbsj:zbsj, zbbc:zbbc,gwlb:gwlb},
            		method:'post',
            		success:function(ajax){
            			eval("responseObj="+ajax.responseText);
            			if(responseObj.success == true){
            				if(responseObj.validate == true){
	            				alert('审阅成功');
	            				changeRightGrid();
	            				formWindow.close();
            				}else{
							    alert('用户名或密码错误');          					
            				}
            			}
            		},
            		failure:function(){
            			alert('服务器忙，请稍候操作...');
            			simpleForm.buttons[0].disable();
            		}
        		});
			}
		},{
            text: '取消',
            handler:function(){formWindow.close();}
        }]
	})
	
	ShenYueFormWindow.superclass.constructor.call(this,{
        title: '领导审阅',
        width: 310,
        height:150,
        modal:true,
        layout: 'column',
        plain:true,
        draggable :false,
        resizable :false,
        bodyStyle:'padding:1px;',
        items: [{
	        	border:false,
	        	columnWidth:1,
	        	layout:'form',
	        	items:simpleForm
	        }]
    });
}

Ext.extend(ShenYueFormWindow, Ext.Window, {
	
})
