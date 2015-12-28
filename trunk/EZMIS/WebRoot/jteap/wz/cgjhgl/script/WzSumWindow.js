
var WzSumWindow=function(){
	Ext.form.Field.prototype.msgTarget = 'side';
	var formWindow=this;	
	//统计开始时间
	var startDt = new Ext.form.DateField( {
				id : 'startDt',
				fieldLabel : '开始时间',
				format : 'Y-m-d',
				allowBlank:false,
				anchor : '90%'
			});
	
	var endDt =  new Ext.form.DateField( {
				id : 'endDt',
				fieldLabel : '结束时间',
				format : 'Y-m-d',
				allowBlank:false,
				anchor : '90%'
			});	
		
	//用户表单定义
	var simpleForm = new Ext.FormPanel({
		reader : new Ext.data.JsonReader({
			success : 'success',
			root : 'data'
		}),		
	    labelAlign: 'left',
	    buttonAlign:'center',
		style : 'margin:2px',
		bodyStyle : 'padding:0px',
	    waitMsgTarget: true,
	    height: 100,
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
	          //第1行布局
	          {columnWidth:.5,layout:'form',border:false,items:[startDt]} ,          
	          {columnWidth:.5,layout:'form',border:false,items:[endDt]}
	          ]
		}],
	
	buttons: [{
			text:'确定',
			handler:function(){
				if(!simpleForm.form.isValid()){
					alert('数据校验失败，请检查填写的数据格式是否正确');
					return;
				}else if(startDt.getValue()>endDt.getValue()){
					alert('开始时间不能大于结束时间');
					endDt.reset();
					return;
				}
				var url=link8+"?startDt="+startDt.value+"&endDt="+endDt.value;
				formWindow.close();
				window.open(url);
//				Ext.Ajax.request({
//					url:link8,
//					success:function(ajax){
//						var responseText = ajax.responseText;	
//					 	var responseObject=Ext.util.JSON.decode(responseText);
//				 		if(responseObject.success){
//					 		alert('操作成功！');
//					 		formWindow.close();
//					 	}else{
//					 			alert(responseObject.msg);
//					 		}				
//						},
//					 	failure:function(){
//					 		alert("提交失败");
//					 	},
//					 	method:'post',
//            			waitMsg:'保存数据中，请稍候...',
//					 	params:{dwdm:Ext.getCmp("dwId").getValue()}
//			});
				

			}
		},{
            text: '取消',handler:function(){
            	formWindow.close();
        	}
        }]
	
	});
	
	WzSumWindow.superclass.constructor.call(this,{
      	title: '采购物资汇总',
      	width: 550,
     	height:200,
		modal : true,
		layout : 'column',
		plain : true,
		draggable : false,
		resizable : false,
		buttonAlign : 'center',
		bodyBorder:false,
		border:false,
		closable:true,
		items : [ {
		        	//第一行布局
		        	columnWidth:1,
		        	layout:'form',
		        	height:35,
		        	width:600,
		        	border:false,
		        	frame:true,
		        	items:[new Ext.app.TitlePanel({caption:'采购物资汇总',border:false})]
		        },{
					border : false,
					columnWidth : 1,
					layout : 'form',
					items : simpleForm
				}]
	});

}

Ext.extend(WzSumWindow, Ext.Window, {
      
});
