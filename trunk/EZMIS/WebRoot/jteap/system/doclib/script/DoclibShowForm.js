var fdList = null;	//文档扩展字段列表
var formPanel = null;

/*
 * ********************
 * 文档信息显示窗口
 * ********************
 */
var DoclibShowWindow = function(resourceId){
	var formWindow = this;
	this.resourceId = resourceId;

	//标题
	var txtDoclibTitle = new Ext.form.TextField({fieldLabel:'文档标题',id:'txtDoclibTitle',name:'title',readOnly:'true',maxLength:500,minLength:1,anchor:'90%',width:160});
	var infoDoclibTitle = new Ext.app.LabelPanel('文档标题，1-30个字符');
	this.firstInput = txtDoclibTitle;
	
	//创建人
	var txtDoclibCreator = new Ext.form.TextField({fieldLabel:'创建人', id:'txtDoclibCreator',name:'creator',readOnly:'true',maxLength:500,minLength:1,anchor:'90%',width:160});
	var infoDoclibCreator = new Ext.app.LabelPanel('创建人,1-20个字符');
	
	//创建时间
	//var txtDoclibCreatedt = new Ext.form.TextField({fieldLabel:'创建时间',id:'txtDoclibCreatedt',name:"createdt",maxLength:500,minLength:1,anchor:'90%'});
	var txtDoclibCreatedt= {xtype:'textfield',id:'txtDoclibCreatedt',fieldLabel: '创建时间',format:'Y-m-d',disabled:'true',name: 'createdt',minLength:1,anchor:'90%',width:160};
	var infoDoclibCreatedt = new Ext.app.LabelPanel('创建时间必须填写!');
	
	//文档级别下拉框
	var cmbDoclibLevel  = new Ext.form.ComboBox({triggerAction: 'all',id:'cmbDoclibLevel',editable :false,disabled:'true',selectOnFocus :false,width:160,forceSelection :true,mode:'local',
										displayField:'name',valueField:'value',fieldLabel:'文档级别',
										allowBlank: false,
				store:new Ext.data.SimpleStore({fields:['name','value']})
			}) ;
	
	this.attachGrid = new AttachViewGrid(resourceId);	
	var gridItem = {
      	//第五行布局
      	columnWidth:1,
             layout:'form',
             border:false,
       items:[this.attachGrid]
      };
	
	
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
	    width: '100%',
	    height: '100%',
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
	        },{
	        	//第三行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtDoclibCreatedt]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoDoclibCreatedt]
	        },{
	        	//第四行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[cmbDoclibLevel]
	        }]
		},{//用户其他属性
			xtype:'hidden',
			name:'id',
			value:resourceId
		},{
		//父亲模块
			xtype:'hidden',
			name:'parentId',
			value:resourceId
		},{
			xtype:'hidden',
			name:'ops',
			id:'ops',
			value:''
		},{
			xtype:'hidden',    
			name:'fdListJson',   //文档扩展字段隐藏表单域
			id:'fdListJson',
			value:''
		}],
	
	buttons: [{
            text: '关闭',
            handler:function(){
           		window.close();      //关闭当前窗体，将formWindow改为Panel后修改窗体关闭方式。
            }
        }]
	
	});
	   
	formPanel = simpleForm;
	/**
	 * 动态添加组件
	 **/
	 function addCmp(value){
	 	var cmp = new Ext.form.TextField({
			fieldLabel:value,
			readOnly:'true',
			maxLength:500,
			minLength:1,
			width:200
		});	
		simpleForm.items.add(cmp);
		simpleForm.doLayout();
		return cmp.getId();
	 }
	 
	 /**
	 * 根据组件类型和比标签值动态添加组件 
	 **/
	 function addCmp(value,id,type){
	 	var cmp; 
	 	if(type==null || type=="" || type=="字符串" || type=="数字"){
	 	 	cmp = new Ext.form.TextField({
	 	 		id :id ,
				fieldLabel:value,
				readOnly:'true',
				maxLength:500,
				minLength:1,
				width:320
			});	
		}else if(type =="日期"){
	 		cmp = new Ext.form.DateField({xtype:'datefield',fieldLabel: value,id :id ,format:'Y-m-d',readOnly:'true',maxLength:500,
				minLength:1,
				width:157,anchor:'45%'});
	 			
	 	}else{
	 		cmp = new Ext.form.TextField({
	 			id :id ,
				fieldLabel:value,
				readOnly:'true',
				maxLength:500,
				minLength:1,
				width:157
			});	
	 	}
		simpleForm.items.add(cmp);
		simpleForm.doLayout();
		return cmp.getId();
	 }
	 
	
	/**
	 * 加载初始化数据
	 */
	this.loadData=function(){
		var link=contextPath+"/jteap/doclib/DoclibAction!showDoclibFieldValueAction.do";
		Ext.Ajax.request({	
			url:link+"?id="+resourceId,
			method:'post',
			success:function(ajax){
				var responseText=ajax.responseText;	
	 			var responseObject=Ext.util.JSON.decode(responseText);
	 			if(responseObject.success){
	 				var data=responseObject.fdlist;
	 				fdList = data;
		 			for(var i =0; i < data.length;i++){
		 				var cmpId = addCmp(data[i].name,data[i].id,data[i].type);				 			
		 				simpleForm.findById(cmpId).setValue(data[i].value);
		 			}
		 			simpleForm.add(gridItem);
		 			simpleForm.doLayout();
		 			this.attachGrid.getStore().reload();
	 			}		
			}
		});	
	}
	this.loadDocData=function(){
		var dataType = [] ;
		//加载文档级别信息
		var link=contextPath + "/jteap/doclib/DoclibLevelAction!showListAction.do";
		var myAjax = new Ajax.Request(link,{
			method: 'post', 
	    	asynchronous:false,//同步调用
	    	onComplete: function(req){
	    		var responseText=req.responseText;
	 			var responseObject = responseText.evalJSON();
	 				var data=responseObject.list; 
	 				for(var i=0;i<data.length;i++){
	 					dataType[i]= [data[i].levelName,data[i].id];
	 				}
	 				simpleForm.findById("cmbDoclibLevel").store.loadData(dataType);
	    	},
	    	onFailure: function(e){
	    		alert("请求失败："+e);
	    	}
	    });
	    
		var link=contextPath + "/jteap/doclib/DoclibAction!modifyDoclibInfoAction.do";
		var myAjax = new Ajax.Request(link+"?id="+resourceId,{
			method: 'post', 
	    	asynchronous:false,//同步调用
	    	onComplete: function(req){
	    		var responseText=req.responseText;
	 			var responseObject = responseText.evalJSON();
	 			if(responseObject.success){
	 				var data=responseObject.fdlist;
		 			simpleForm.findById("txtDoclibTitle").setValue(data.title);
		 			simpleForm.findById("txtDoclibCreator").setValue(data.creator);
		 			var time = new Date(data.createdt.time).dateFormat("Y-m-d");
		 			simpleForm.findById("txtDoclibCreatedt").setValue(time);
		 			simpleForm.findById("txtDoclibCreatedt").setValue(time);
		 			simpleForm.findById("cmbDoclibLevel").setValue(data.doclibLevel.id );	  //设置当前文档级别ID 	
	 			}	 		
	    	},
	    	onFailure: function(e){
	    		alert("请求失败："+e);
	    	}
	    });
	}
	
	DoclibShowWindow.superclass.constructor.call(this,{
 	    width: '99%',
 	   	height: 420,
 	   	autoScroll: true, 
        modal:true,
        layout: 'column',
        plain:true,
        draggable :false,
        resizable :false,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        items: [
        	/*{
	        	//第一行布局
	        	columnWidth:1,
	        	layout:'form',
	        	height:40,
	        	width:600,
	        	border:false,
	        	frame:true,
	        	items:[new Ext.app.TitlePanel({caption:'添加文档信息',border:false})]
	        } */
	        {
	        	border:false,
	        	columnWidth:1,
	        	layout:'form',
	        	items:simpleForm
	        }]
    });
}

Ext.extend(DoclibShowWindow, Ext.Panel, {
});



//文件选择
function fileSelectorChanged(browse_btn){
	
	var attachFile = browse_btn.getInputFile();
	var image = new Image();
	image.dynsrc = attachFile.dom.value;
	var attachSize = image.fileSize+"KB";
	var myUrl = attachFile.dom.value;
	var aa = myUrl.lastIndexOf("\\");
	var attachName = myUrl.substring(aa+1);
	//alert("文件名称:"+bb);
	var cc = myUrl.lastIndexOf(".");
	var attachType = myUrl.substring(cc+1);
	//alert("文件类型:"+attachType);
	
	var Model = Ext.data.Record.create([{name:'icon'},{name:'name'},{name:'type'},{name:'doclibSize'}]) 
	var record = new Model(
		{
			'icon' : 'xx',
			'name' : attachName,
			'doclibSize' : attachSize,
			'type' : attachType
		}
	);
	attachGrid.stopEditing();
	foDefaultDS.insert(0, record);	
	var input_file = browse_btn.detachInputFile();
	input_file.dom.style.left = "-200000";
	input_file.appendTo(document.forms[0]);
	
    //attachGrid.startEditing(0, 0);
}

 