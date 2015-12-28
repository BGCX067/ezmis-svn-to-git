
var EditCurrPresonFormWindow=function(){
	Ext.form.Field.prototype.msgTarget = 'side';
	var formWindow=this;
	//用户名
	var txtUserLoginName={xtype:'textfield',id:'txtUserLoginName',fieldLabel:'用户名',maxLength:12,minLength:5,name:'userLoginName',disabled:true,allowBlank:false,anchor:'90%'};
	var infoUserLoginName=new Ext.app.LabelPanel('登录用户名，5-12位字母及数字');
	
	//姓名
	var txtUserName={xtype:'textfield',id:'username',fieldLabel:'昵称',name:'userName',maxLength:10,minLength:2,inputType:'textfield',allowBlank:false,anchor:'90%'};
	
	var infoUserName=new Ext.app.LabelPanel('用户真实姓名，2-10汉字或有效字符');
	
	
	//性别
	var comboSex={
		xtype:'combo',
		store: new Ext.data.SimpleStore({fields: ["retrunValue", "displayText"],data: [['男','男'],['女','女']]}),
		valueField :"retrunValue",
		displayField: "displayText",
		mode: 'local',
		forceSelection: true,
		triggerAction: 'all',
		blankText:'请选择性别',
		emptyText:'请选择性别',
		hiddenName:'userSex',
		editable: false,
		allowBlank:true,
		id:'sex',
		fieldLabel: '性别',
		name: 'education',
		anchor:'90%'
	}
	var infoUserSex=new Ext.app.LabelPanel('选择性别');
	
	//出生日期
	var dtBirthday={xtype:'datefield',id:'birthday',fieldLabel: '出生日期',format:'Y-m-d',name: 'userBirthday',anchor:'90%'}
	
	var infoBirthday=new Ext.app.LabelPanel('选择出生日期');
	        
	//其他属性
	var propsGrid = new Ext.grid.PropertyGrid({
			id:'propsGrid',
	        nameText: 'Properties Grid',
	        width:533,
	        height:166,
	        autoHeight:false,
	        footer :true,
	        viewConfig : {
	            forceFit:true,
	            autoFill :true,
	            scrollOffset:17 // the grid will never have scrollbars
	        },
	        source: {"0": "0"}
	
	});

	/**
	 * 用户属性列表为一个隐藏域
	 */
	function encodeUserConfig(){
		var configField=simpleForm.find("name","userConfig")[0];
		var dataSource=Ext.util.JSON.encode(propsGrid.getSource());
		
		configField.setValue(dataSource);
     
		}
   /**
    * 修改,更新
    */
	//用户表单定义
	var simpleForm = new Ext.FormPanel({
	   reader:new Ext.data.JsonReader({
			success:'success',
			root:'data'
		},[{name:'userLoginName',mapping:'userLoginName'},{name:'userName',mapping:'userName'},{name:'userSex',mapping:'Sex'},{name:'userConfig',mapping:'config'}
		]),
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
	        	items:[txtUserLoginName]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoUserLoginName]
	        },{
	        	//第二行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtUserName]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoUserName]
	        },{
	        	//第三行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[comboSex]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoUserSex]
	        },{
	        	//第四行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[dtBirthday]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoBirthday]
	        }
	        ,{
	        	//第五行布局
	        	columnWidth:1,
	        	layout:'form',
	        	border:false,
		        items:[{//标签面板
		            xtype:'tabpanel',
		            plain:true,
		            activeTab: 0,
		            height:200,
		            autoWidth:true,
		            defaults:{bodyStyle:'padding:2px'},
		            items:[{
		                title:'其他属性',
		                layout:'form',
		                defaultType: 'textfield',
		                items: propsGrid
		            }]
		        }]
	        }]
		},{//用户其他属性
			xtype:'hidden',
			name:'userConfig',
			value:''
		}	],
	
	buttons: [{
			text:'保存',
			handler:function(){
				//提交表单的时候将属性列表中的数据转换成JSON字符串传入隐藏域中
				encodeUserConfig();
				if(!simpleForm.form.isValid()){
					alert('数据校验失败，请检查填写的数据格式是否正确');
					return;
				}
				simpleForm.form.doAction('submit',{
            		url:link6,
            		method:'post',
            		waitMsg:'保存数据中，请稍候...',
            		success:function(){
            			if(window.confirm('修改成功')){
            				formWindow.close();
            			}
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
	
	EditCurrPresonFormWindow.superclass.constructor.call(this,{
        title: '当前用户编辑',
        width: 580,
        height:460,
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
	        	items:[new Ext.app.TitlePanel({caption:'用户信息编辑',border:false})]
	        },{
	        	border:false,
	        	columnWidth:1,
	        	layout:'form',
	        	items:simpleForm
	        }]
    });
}

Ext.extend(EditCurrPresonFormWindow, Ext.Window, {
	showData:function(person){
		
 		//对面板的操作
		var form=this;
		var txtUserLoginName=this.findById("txtUserLoginName");
			txtUserLoginName.setValue(person.userLoginName);
		var userName=this.findById("username");
			userName.setValue(person.userName);
		//设置性别
		var userSex=this.findById("sex");
		userSex.setValue(person.sex);
		//生日
		var	birthDay=this.findById("birthday");
		var bdt=person.birthday;
		if(bdt){
		var dt = formatDate(new Date(bdt["time"]),"yyyy-MM-dd"); 
		birthDay.setValue(dt);
		}
		//属性
		var propsGrid=this.findById("propsGrid");
	
		var resource=(!person.config || person.cinfig=="")?{}:Ext.util.JSON.decode(person.config);
		
		//把从数据库中取出来的数据和XML里面的对比
		Ext.Ajax.request({
		   url: link7,
		   success: function(response,options){
		   		var configData={};
		   		var xmlDom=response.responseXML;
		   		var config=xmlDom.childNodes[0];
		   		for(var i=0;i<config.childNodes.length;i++){
		   			var oconfig=config.childNodes[i];
		   			if(oconfig.tagName=="property"){
	   			    var sConfigName=oconfig.getAttribute("name");
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
		   			  	while(sConfigValue.indexOf("-")!=-1){
		   			  		sConfigValue=sConfigValue.replace("-","/");
		   			  	}
		   			  	}
		   			  configData[sConfigName]=new Date(Date.parse(sConfigValue));
		   			  }else{
		   			  configData[sConfigName]=sConfigValue;
		   			  }
		   			}
		   		}
		   		//给网格注入数据源
		   	  propsGrid.source=configData;
		   	  form.show();
		   	}
		});
		},
		showDetailFrom:function (){
		
		var form=this;
			Ext.Ajax.request({
			url:link5,
			success:function(ajax){
				var responseText=ajax.responseText;	
			 	var responseObject=Ext.util.JSON.decode(responseText);
		 		if(responseObject.success){
			 		form.showData(responseObject.data[0]);
			 	}else{
			 			alert(responseObject.msg);
			 		}				
				},
			 	failure:function(){
			 		alert("提交失败");
			 	},
			 	method:'POST'		
			});
		}
});








