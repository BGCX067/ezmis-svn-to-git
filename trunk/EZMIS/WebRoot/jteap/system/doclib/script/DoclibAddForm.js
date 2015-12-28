var fdList = null;	//文档扩展字段列表
var formPanel = null;
var fileIndex = 0 ;
/*
 * ********************
 * 文档添加窗口
 * ********************
 */
var DoclibAddWindow = function(resourceId){
	var formWindow = this;
	this.resourceId = resourceId;

	//标题
	var txtDoclibTitle = new Ext.form.TextField({fieldLabel:'文档标题',id:'txtDoclibTitle',name:'title',maxLength:60,minLength:1,width:170});
	var infoDoclibTitle = new Ext.app.LabelPanel('文档标题，1-60个字符');
	this.firstInput = txtDoclibTitle;
	
	//创建人
	var txtDoclibCreator = new Ext.form.TextField({fieldLabel:'创建人', id:'txtDoclibCreator',name:'creator',readOnly:true,maxLength:20,minLength:1,width:170});
	var infoDoclibCreator = new Ext.app.LabelPanel('创建人,1-20个字符');
	
	//创建时间
	var txtDoclibCreatedt= new Ext.ComponentMgr.create({xtype:'datetimefield',id:'txtDoclibCreatedt',fieldLabel: '创建时间',format:'Y-m-d H:i:s',readOnly:'true',name: 'createdt',minLength:1,width:170});
	var infoDoclibCreatedt = new Ext.app.LabelPanel('创建时间必须填写');
	
	//文档级别下拉框
	var cmbDoclibLevel  = new Ext.form.ComboBox({triggerAction: 'all',id:'cmbDoclibLevel',editable :false,selectOnFocus :false,forceSelection :true,mode:'local',
										displayField:'name',valueField:'value',fieldLabel:'文档级别',width:170,
										allowBlank: false,blankText:'请选择一个文档级别',
				store:new Ext.data.SimpleStore({fields:['name','value']})
			}) ;
	var infoDoclibLevel = new Ext.app.LabelPanel('选择一个文档级别');
	
	var content = new Ext.form.TextArea( {
			id : 'content',
			name : "content",
			width : 450,
			height : 200,
			fieldLabel : "内容",
			listeners : {
				render : function(f) {
					fckEditor = new FCKeditor("content");// 初始化FCK
					fckEditor.Height = 200;// 设置FCK编辑器的高度
					fckEditor.Width = 450;// 设置FCK编辑器的宽度
					fckEditor.ToolbarSet = "Jteap";
					fckEditor.BasePath = contextPath + "/component/FCKeditor/";// 设定FCK的源文件路径，这里要注意相对和绝对路径
					fckEditor.ReplaceTextarea();// 用FCK编辑器替换Ext中的textarea
				}
			}
		})
	
	var lblAttach = new Ext.form.Label( {
		text : '附件列表:',
		width : 60
	})
	this.attachGrid = new AttachGrid(resourceId);	
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
	        }
	        /*,{
	        	//第四行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[cmbDoclibLevel]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoDoclibLevel]
	        }*/
	        ,{
	        	columnWidth: 1,
	        	layout:'form',
	        	border:false,
	        	items:[content]
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
			name:'levelID',      //文档级别ID隐藏表单域
			id:'levelID',
			value:''
		},{
			xtype:'hidden',    
			name:'fdListJson',   //文档扩展字段隐藏表单域
			id:'fdListJson',
			value:''
		}],
	
	buttons: [{
			text:'保存并关闭',
			handler:function(){
				//如果是新建，则将所有的操作传输到后台
				//提交表单的时候将属性列表中的数据转换成JSON字符串传入隐藏域中
				if(Ext.get("txtDoclibTitle").getValue() == "" || Ext.get("txtDoclibTitle").getValue() == null){
					alert("文档名称不能为空!");
					return;
				}else if(Ext.get("txtDoclibCreator").getValue() == "" || Ext.get("txtDoclibCreator").getValue() == null){
					alert("创建人不能为空!");
					return;
				}else if(Ext.get("txtDoclibCreatedt").getValue() == "" || Ext.get("txtDoclibCreatedt").getValue() == null){
					alert("创建时间不能为空!");
					return;
				}
				/*
				else if(Ext.get("cmbDoclibLevel").getValue() == "" || Ext.get("cmbDoclibLevel").getValue() == null){
					alert("请选择文档级别!");
					return;
				}
				*/
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
				//提交前将文档扩展字段值存入文档扩展字段对象中
				for(var i=0;i<fdList.length;i++){
					var id =fdList[i].id;
					var value = Ext.get(id).getValue();
					fdList[i].value = value;
					fdList[i].id = id.replace("cmp","");
				}			  
			    var json = Object.toJSON(fdList);
			    simpleForm.findById('fdListJson').setValue(json);
			    //alert(json);
			    //var levelID = simpleForm.findById('cmbDoclibLevel').getValue();
			   // simpleForm.findById('levelID').setValue(levelID);
			      
				//获取表单字段的值
	
				simpleForm.form.doAction('submit',{
            		url:link10,
            		method:'post',     		
            		waitMsg:'保存数据中，请稍候...',
            		success:function(){
            			alert('保存成功!');		
            			window.returnValue = "true" ;
            			//formWindow.close();
            			window.close();
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
           		// formWindow.close();
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
			maxLength:500,
			minLength:1,
			width:157
		});	
		simpleForm.items.add(cmp);
		simpleForm.doLayout();
		
		return cmp.getId();
	 }
	 
	 /**
	 * 根据组件类型和标签值动态添加组件 
	 **/
	 function addCmp(data){
	 	var cmp; 
	 	var id = "cmp"+data.id ;
	 	var value = data.name ;
	 	var type = data.type ;
	 	var emunValue = data.emunValue;
	 	var format = data.format ;
	 	
	 	if(type==null || type=="" || type=="字符串" || type=="数字"){
	 		if(emunValue!=""){
	 			var array = emunValue.split(",");
	 			if(array!=null && array.length > 0 ){
	 				var  dataType2 =[] ;
	 				for(var i=0;i<array.length;i++){
	 					dataType2[i]=[array[i],array[i]];
	 				}
	 				/*
	 				 cmp = new Ext.form.ComboBox({triggerAction: 'all',editable :false,fieldLabel :value,selectOnFocus :false,forceSelection :true,mode:'local',displayField:'name',valueField:'name',
						allowBlank: true,width:350,
						store:new Ext.data.SimpleStore({fields:['name','value'],data:dataType2})		
					}) ;
					*/
					cmp = new Ext.form.ComboBox({triggerAction: 'all',editable :false,fieldLabel :value,
	 				 			id:id,selectOnFocus :false,forceSelection :true,mode:'local',
	 				 			displayField:'name',valueField:'name',width:170,
						allowBlank: true,
						store:new Ext.data.SimpleStore({fields:['name','value'],data:dataType2})		
					}) ;
	 			}
	 		}else{
		 	 	cmp = new Ext.form.TextField({
		 	 		id:id,
					fieldLabel:value,
					maxLength:500,
					minLength:1,
					width:170
				});	
			}
		}else if(type =="日期"){
			alert("干嘛");
			if(format ==null || format==""){
				format = 'Y-m-d';
			}
			/*
	 		cmp = new Ext.form.DateField({xtype:'datefield',fieldLabel: value,format:format,readOnly:'true',maxLength:500,
				minLength:1,
				width:350});
			*/
			cmp = new Ext.form.DateField({xtype:'datefield',fieldLabel: value,id:id,
	 				format:format,readOnly:'true',maxLength:500,
				minLength:1,
				width:170});
	 			
	 	}else{
	 		cmp = new Ext.form.TextField({
	 			id:id,
				fieldLabel:value,
				maxLength:500,
				minLength:1,
				width:170
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
		 				//alert("=====>fieldname<========"+data[i].name+"===="+data[i].type+"enumValue===>"+data[i].emunValue+"==forma=="+data[i].format);
		 				var cmpId = addCmp(data[i]);
		 				data[i].id = cmpId;
		 			}
		 			simpleForm.add(gridItem);
		 			simpleForm.doLayout();
		 			this.attachGrid.getStore().reload();
	 			}
			}
		});	
		simpleForm.findById('txtDoclibCreator').setValue(curPersonName);   //创建人为当前用户
		var time = new Date().dateFormat("Y-m-d H:i:s");						//时间为系统当前时间
	 	simpleForm.findById('txtDoclibCreatedt').setValue(time);       
	}
	
	//加载文档级别
	this.loadDoclibLevel = function(){
		var link=contextPath + "/jteap/doclib/DoclibLevelAction!showListAction.do";
		var myAjax = new Ajax.Request(link,{
			method: 'post', 
	    	asynchronous:false,//同步调用
	    	onComplete: function(req){
	    		var responseText=req.responseText;
	 			var responseObject = responseText.evalJSON();
	 			var dataType = [] ;
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
	}
	
	DoclibAddWindow.superclass.constructor.call(this,{
 	    width: '100%',
 	    height: 620,
        modal:true,
        layout: 'column',
        plain:true,
        draggable :false,
        resizable :false,
        bodyStyle:'padding:2px;',
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

Ext.extend(DoclibAddWindow, Ext.Panel, {
});



//文件选择
function fileSelectorChanged(browse_btn){
	
	var attachFile = browse_btn.getInputFile();
	var image = document.createElement("img");  

	var attachSize = image.fileSize+"KB";
	var myUrl = attachFile.dom.value;
	var aa = myUrl.lastIndexOf("\\");
	var attachName = myUrl.substring(aa+1);
	
	var cc = myUrl.lastIndexOf(".");
	var attachType = myUrl.substring(cc+1);

	
	var Model = Ext.data.Record.create([{name:'icon'},{name:'name'},{name:'type'},{name:'doclibSize'}]) 
	attachGrid.stopEditing();
	//foDefaultDS.insert(0, record);	
	var input_file = browse_btn.detachInputFile();
	input_file.dom.style.left = "-200000";
	input_file.appendTo(document.forms[0]);
	var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"文件验证中，请稍候..."});
	myMask.show();
	validateFile(input_file,myMask);
    //attachGrid.startEditing(0, 0);
}

function validateFile(input_file,myMask){
		 var link = contextPath + "/servlet/ValidateFileServlet";	
  		 var myForm = document.forms[0];
         Ext.Ajax.request({
		      url : link,
		      method : 'POST',
		      form : myForm,
		      isUpload : true,
		      success : function(ajax){
		      			 
		    		  	  var responseText = ajax.responseText;
		    		  	  var responseObject = Ext.util.JSON.decode(responseText);
		    		  	  if(responseObject.success){
		    		  	  		var Model = Ext.data.Record.create([{name:'icon'},{name:'name'},{name:'type'},{name:'doclibSize'}]) 
		    		  	  		var data = responseObject.fdList ;
		    		  	  		var maxsize = data.maxsize ;
		    		  	  		myMask.hide();
		    		  	  		if(data.filesize>maxsize){
		    		  	  			alert("上传文件最大为10M！");
		    		  	  			myForm.removeChild(myForm.lastChild);
		    		  	  			return ;
		    		  	  		}
		    		  	  		var index= data.filename.lastIndexOf(".");
								var attachType = data.filename.substring(index+1);
								var filesize = data.filesize/1024 ;
								var size = filesize.toString().replace(/^(\d+\.\d{2})\d*$/,"$1");
		    		  	  		var record = new Model(
									{
										'icon' : attachType,
										'name' : data.filename,
										'doclibSize' : size+"KB",
										'type' : attachType,
										'fileIndex':fileIndex++          //上传附件文件索引，很重要
									}
								);
		    		  	  		foDefaultDS.insert(0, record);
		    		  	  }
		    		  },
		      failure : function(){
            			alert('服务器忙，请稍候操作...');           			
            		},
		      record: null
   		 });
         
}
 