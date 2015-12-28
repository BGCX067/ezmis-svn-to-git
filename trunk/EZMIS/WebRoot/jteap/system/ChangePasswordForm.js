


var ChangePasswordFormWindow=function(){
	
	Ext.form.Field.prototype.msgTarget = 'side';
	
	var formWindow=this;
	
	/*
	 * ************************
	 * 控件定义
	 * ************************
	 */
	
	//当前密码
	var txtCurPassword={
		xtype:'uniquetextfield',
		inputType:'password',
		url:link2,
		notUniqueText:'当前密码不正确',
		id:'txtCurPassword',
		fieldLabel:'当前密码',
		maxLength:10,
		minLength:5,
		minLengthText:'最少需要6位字母数字组合',
		maxLengthText:'最长10位字母数字组合',
		name:'userPassword',
		allowBlank:false,
		anchor:'90%'
	};
	var infoCurPassword=new Ext.app.LabelPanel('当前用户密码，6-10位字母及数字');
	
	//新密码
	var txtNewPassword={xtype:'textfield',inputType:'password',fieldLabel:'新密码',name:'newPassword',maxLength:10,minLength:6,allowBlank:false,anchor:'90%'};
	var infoNewPassword=new Ext.app.LabelPanel('新用户密码，6-10位字母及数字');
	
		//新密码
	var txtNewPasswordConfirm={xtype:'textfield',inputType:'password',fieldLabel:'新密码',name:'newPasswordConfirm',maxLength:10,minLength:6,allowBlank:false,anchor:'90%'};
	var infoNewPasswordConfirm=new Ext.app.LabelPanel('请确认新用户密码，6-10位字母及数字');
	
	//用户表单定义
	var simpleForm = new Ext.FormPanel({
	    labelAlign: 'left',
	    buttonAlign:'right',
	    bodyStyle:'padding:5px',
	    waitMsgTarget: true,
	    width: 560,
	    frame:true, 					//圆角风格
	    labelWidth:80,					//标签宽度
		items: [{
			layout:'column',
	        border:false,
	        labelSeparator:'：',
	        defaults:{
	        	blankText:'必填字段'
	        },
	        items:[
	        	{
	        	//第一行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtCurPassword]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoCurPassword]
	        },{
	        	//第二行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtNewPassword]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoNewPassword]
	        },{
	        	//第三行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtNewPasswordConfirm]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoNewPasswordConfirm]
	        }]
		}],
	
	buttons: [{
			text:'保存',
			handler:function(){				
				if(!simpleForm.form.isValid()){
					alert('请检查填写的数据是否正确');
					return;
				}
				simpleForm.form.doAction('submit',{
            		url:link3,
            		method:'POST',
            		waitMsg:'密码修改中，请稍候...',
            		params:'',
            		success:function(){
            			alert("密码修改成功");
            			formWindow.close();
            		},
            		failure:function(){
            			alert('服务器忙，请稍候操作...');
            			simpleForm.buttons[0].enable();
            		}
            	});
            	this.disable();
			}
		},{
            text: '取消',
            handler:function(){formWindow.close();}
        }]
	
	});
	
	ChangePasswordFormWindow.superclass.constructor.call(this,{
        title: '修改密码',
        width: 580,
        height:250,
        modal:true,
        layout: 'column',
        plain:true,
        draggable :false,
        resizable :false,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        items: [{
	        	//第一行布局
	        	columnWidth:1,
	        	layout:'form',
	        	height:40,
	        	width:600,
	        	border:false,
	        	frame:true,
	        	items:[new Ext.app.TitlePanel({caption:'修改密码',border:false})]
	        },{
	        	border:false,
	        	columnWidth:1,
	        	layout:'form',
	        	items:simpleForm
	        }]
    });
}

Ext.extend(ChangePasswordFormWindow, Ext.Window, {
	
});








