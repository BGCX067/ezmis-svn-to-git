var fdList = null;	//文档扩展字段列表
var formPanel = null;

/*
 * ********************
 * 文档级别添加窗口 add by 2009-08-24
 * ********************
 */
var DoclibLevelAddWindow = function(){
	var formWindow = this;
	//this.resourceId = "";
	
	//标题
	var txtDoclibTitle = new Ext.form.TextField({fieldLabel:'文档级别',id:'txtDoclibTitle',name:'title',maxLength:500,minLength:1,anchor:'90%'});
	var infoDoclibTitle = new Ext.app.LabelPanel('文档标题，1-30个字符');
	this.firstInput = txtDoclibTitle;
	
	//创建人
	var txtDoclibCreator = new Ext.form.TextField({fieldLabel:'创建人', id:'txtDoclibCreator',name:'creator',maxLength:500,minLength:1,anchor:'90%'});
	var infoDoclibCreator = new Ext.app.LabelPanel('创建人,1-20个字符');
	
	//this.attachGrid = new AttachGrid(resourceId);	
	/*
	var gridItem = {
      	//第五行布局
      	columnWidth:1,
             layout:'form',
             border:false,
       items:[{//标签面板
           xtype:'tabpanel',
           plain:true,
           activeTab: 0,
           height:220,
           autoWidth:true,
           defaults:{bodyStyle:'padding:2px'},
           items:[{
               title:'文档附件',
               layout:'form',
               defaultType: 'textfield',
               items: this.attachGrid
           }]
       }]
      };
	*/
	
	//文档表单定义
	var simpleForm = new Ext.FormPanel({
	id:"myForm",
		reader:new Ext.data.JsonReader({
			success:'success',
			root:'data'
		},[{name:'name',mapping:'name'}]),
	    labelAlign: 'left',
	    buttonAlign:'right',
	    bodyStyle:'padding:5px',
		fileUpload: true,   
	   	waitMsgTarget: true,
	    width: 560,
	    enctype:'multipart/form-data',
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
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtDoclibTitle]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoDoclibTitle]
	        },{
	        	//第二行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtDoclibCreator]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoDoclibCreator]
	        }]
		},{//用户其他属性
			xtype:'hidden',
			name:'id',
			value:''
		},{
			xtype:'hidden',
			name:'ops',
			id:'ops',
			value:''
		}],
	
	buttons: [{
			text:'保存并关闭',
			handler:function(){
				//如果是新建，则将所有的操作传输到后台
				//提交表单的时候将属性列表中的数据转换成JSON字符串传入隐藏域中
				
				if(Ext.get("txtDoclibTitle").getValue() == "" || Ext.get("txtDoclibTitle").getValue() == null){
					alert("文档级别名称不能为空!");
					return;
				}else if(Ext.get("txtDoclibCreator").getValue() == "" || Ext.get("txtDoclibCreator").getValue() == null){
					alert("创建人不能为空!");
					return;
				}
				
				//判断扩展字段是否有同名现象
				var variableItems = attachGrid.store.data.items ;
				for(var i=0 ; i<variableItems.length ; i++) {
					var firstName = variableItems[i].data.name ;
					if(firstName == "") {
						alert("附件名不能为空!") ;
						return ;
					}
					for(var j=i+1 ; j<variableItems.length ; j++) {
						var secName = variableItems[j].data.name ;
						if(firstName == secName) {
							alert("请先处理好附件名称同名现象!") ;
							return ;
						}
					}
				}
				var ops=[];
            	var ds=formWindow.attachGrid.getStore();
            	ds.each(function(r){
            		ops.push(r.data);
            	});
				simpleForm.findById('ops').setValue(Ext.encode(ops));
				
				if(!simpleForm.form.isValid()){
					alert('数据校验失败，请检查填写的数据格式是否正确!');
					return;
				}
			
			    var json = Object.toJSON(fdList);
			    simpleForm.findById('fdListJson').setValue(json);
			      
				//获取表单字段的值
	
				simpleForm.form.doAction('submit',{
            		url:link10,
            		method:'post',     		
            		waitMsg:'保存数据中，请稍候...',
            		success:function(){
            			alert('保存成功!');		
            			window.returnValue = "true" ;
            			formWindow.close();		
            		},
            		failure:function(){
            			alert('服务器忙，请稍候操作...');
            			//simpleForm.buttons[0].enable();
            		}
            	});
            	//this.disable();
			}
		},{
            text: '取消',
            handler:function(){
           		 formWindow.close();      
            }
        }]
	
	});
	   
	
	formPanel = simpleForm;
	 
	 
	
	/**
	 * 加载初始化数据
	 */
	 /*
	this.loadData=function(){
		Ext.Ajax.request({
			url:link11+"?id="+resourceId,
			method:'post',
			success:function(ajax){
				var responseText=ajax.responseText;	
	 			var responseObject=Ext.util.JSON.decode(responseText);
	 			if(responseObject.success){
	 				var data=responseObject.fdlist;
	 				fdList = data;
		 			for(var i =0; i < data.length;i++){
		 				var cmpId = addCmp(data[i].name,data[i].type);
		 				data[i].id = cmpId;
		 			}
		 			simpleForm.add(gridItem);
		 			simpleForm.doLayout();
		 			this.attachGrid.getStore().reload();
	 			}
	 			
	 			
	 			
			}
		});	
	}
	*/
	
	DoclibLevelAddWindow.superclass.constructor.call(this,{
 	    width: '100%',
        modal:true,
        layout: 'column',
        plain:true,
        draggable :false,
        resizable :false,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        items: [
        	{
	        	//第一行布局
	        	columnWidth:1,
	        	layout:'form',
	        	height:40,
	        	width:600,
	        	border:false,
	        	frame:true,
	        	items:[new Ext.app.TitlePanel({caption:'添加文档级别信息',border:false})]
	        } ,
	        {
	        	border:false,
	        	columnWidth:1,
	        	layout:'form',
	        	items:simpleForm
	        }]
    });
}

Ext.extend(DoclibLevelAddWindow, Ext.Window, {
});
