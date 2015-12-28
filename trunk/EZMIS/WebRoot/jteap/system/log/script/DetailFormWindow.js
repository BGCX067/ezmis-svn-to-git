var DetailFormWindow=function(config){
	Ext.form.Field.prototype.msgTarget = 'side';

	var formWindow=this;
	
	/*
	 * ************************
	 * 控件定义
	 * ************************
	 */
	var lvLogContentValue = config.data.log;
	var lvUserNameValue= config.data.personName;
	var lvUserLoginNameValue = config.data.personLoginName;
	var lvDataValue = formatDate(new Date(config.data.dt.time),"yyyy-MM-dd")
	var LV=Ext.app.LabelValuePanel;
	//用户名new Ext.form.TextArea({maxLength : 50,allowBlank :true,fieldLabel : "验证规则",name : "txtVLRule",width:190	});
	var lvLogContent=new Ext.form.TextArea({fieldLabel:'日志内容',value:lvLogContentValue,height:200,width:448});;
	var lvUserName=new LV({label:'用户名：',value:lvUserNameValue}); 
	var lvUserLoginName = new LV({label:'用户帐号：',value:lvUserLoginNameValue}); 
	var lvdata=new LV({label:'时间：',value:lvDataValue}); 
	
	
	//用户表单定义
	var simpleForm = new Ext.FormPanel({
	    buttonAlign:'right',
	    bodyStyle:'padding:5px',
	    width: 465,
	    height:360,
	    frame:true, 					//圆角风格
		items: [{
			layout:'column',
	        border:false,
	        items:[
	        	{
	        	//第一行布局
	        	columnWidth:10,
	        	layout:'form',
	        	border:false,
	        	items:[lvUserName]
	        },{
	        	//第二行布局
	        	columnWidth:10,
	        	layout:'form',
	        	border:false,
	        	items:[lvUserLoginName]
	        },{
	        	//第三行布局
	        	columnWidth:1,
	        	layout:'form',
	        	border:false,
	        	items:[lvdata]
	        },{
	        	//第四行布局
	        	columnWidth:1,
	        	labelWidth:1,
	        	border:false,
	        	items:[lvLogContent]
	    	}]
		}],
		//按钮
		buttons: [{
            text: '关闭',
            handler:function(){formWindow.close();}
        }]
	
	});
	
	DetailFormWindow.superclass.constructor.call(this,{
        title: '日志信息',
        width: 500,
        height:450,
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
	        	items:[new Ext.app.TitlePanel({caption:'日志信息查看',border:false})]
	        },{
	        	border:false,
	        	columnWidth:1,
	        	layout:'form',
	        	items:simpleForm
	        }]
    });
}

Ext.extend(DetailFormWindow, Ext.Window, {
	
});








