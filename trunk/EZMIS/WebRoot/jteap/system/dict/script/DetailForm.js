

var DetailFormWindow=function(){
	var formWindow=this;
	
	/*
	 * ************************
	 * 控件定义
	 * ************************
	 */
	
	//键
	var txtKey=new Ext.app.LabelValuePanel({id:'txtKey',label:'键：',value:' '});
	
	//值
	var txtValue=new Ext.app.LabelValuePanel({id:'txtValue',label:'值：',value:' '});

	//描述
	var txtRemark=new Ext.app.LabelValuePanel({id:'txtRemark',label:'描述：',value:' '});
	
	//字典表单定义
	var simpleForm = new Ext.FormPanel({
	    labelAlign: 'left',
	    buttonAlign:'right',
	    bodyStyle:'padding:5px',
	    waitMsgTarget: true,
	    width: 360,
	    frame:true, 					//圆角风格
	    labelWidth:80,					//标签宽度
		items: [{
			layout:'column',
	        border:false,
	        labelSeparator:'：',
	        items:[
	        	{
	        	//第一行布局
	        	columnWidth:1,
	        	layout:'form',
	        	border:false,
	        	items:[txtKey]
	        },{
	        	//第二行布局
	        	columnWidth:1,
	        	layout:'form',
	        	border:false,
	        	items:[txtValue]
	        },{
	        	//第三行布局
	        	columnWidth:1,
	        	layout:'form',
	        	border:false,
	        	items:[txtRemark]
	        }]
		}],
		//按钮
		buttons: [{
            text: '关闭',
            handler:function(){formWindow.close();}
        }]
	});

	DetailFormWindow.superclass.constructor.call(this,{
        title: '字典编辑',
        width: 380,
        height:235,
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
	        	width:300,
	        	border:false,
	        	frame:true,
	        	items:[new Ext.app.TitlePanel({caption:'字典编辑',border:false})]
	        },{
	        	border:false,
	        	columnWidth:1,
	        	layout:'form',
	        	items:simpleForm
	        }]
    });
}

Ext.extend(DetailFormWindow, Ext.Window, {
	loadData:function(dictId){
		formWindow=this;
		Ext.Ajax.request({
	 		url:link9+"?id="+dictId,
	 		method:'get',
	 		success:function(ajax){
	 			var responseText=ajax.responseText;	
	 			var responseObject=Ext.util.JSON.decode(responseText);
	 			if(responseObject.success){ 
	 				var key=responseObject.data[0].key;
	 				var value=responseObject.data[0].value;
	 				var remark=responseObject.data[0].remark;
	 				formWindow.findById('txtKey').setValue(key);
	 				formWindow.findById('txtValue').setValue(value);
	 				formWindow.findById('txtRemark').setValue(remark);		
	 			}
	 		},
	 		failure:function(){
	 			alert("提交失败");
	 		}
	 	})
	}
});















