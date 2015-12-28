/*
 * ***********************
 * 文档分类编辑窗口
 * ***********************
 */

var canModify = false ;

var DoclibCatalogEditWindow=function(resourceId){
	var formWindow=this;
	this.resourceId=resourceId;
	/*
	 * ************************
	 * 控件定义
	 * ************************
	 */
	this.operationGrid = new OperationGrid(resourceId);
	
	//文档分类
	var txtDoclibCatalogName = new Ext.form.TextField({fieldLabel: '分类名称',id:'txtDoclibCatalogName',name:'title',maxLength:500,minLength:1,anchor:'90%'});
	var infoDoclibCatalogName = new Ext.app.LabelPanel('文档分类名称，1-30个字符');
	
	
	//文档分类代码
	var txtDoclibCatalogCode = new Ext.form.TextField({fieldLabel: '分类代码',id:'txtDoclibCatalogCode',name:'catalogCode',readOnly:true,maxLength:500,minLength:1,anchor:'90%'});
	var infoDoclibCatalogCode = new Ext.app.LabelPanel('文档分类代码，标识文档分类，1-30个字符');
	
	//文档级别下拉框
	var cmbDoclibLevel  = new Ext.form.ComboBox({triggerAction: 'all',id:'cmbDoclibLevel',editable :false,selectOnFocus :false,forceSelection :true,mode:'local',
										displayField:'name',valueField:'value',fieldLabel:'文档级别',width:170,
										allowBlank: false,blankText:'请选择一个文档级别',
				store:new Ext.data.SimpleStore({fields:['name','value']})
			}) ;
	var infoDoclibLevel = new Ext.app.LabelPanel('选择一个文档级别');
	
	//文档目录下的文档显示模板
	var cmbDoclibTemplateFile  = new Ext.form.ComboBox({triggerAction: 'all',id:'cmbDoclibTemplateFile',editable :false,selectOnFocus :false,forceSelection :true,mode:'local',
										displayField:'name',valueField:'value',fieldLabel:'模板文件',width:170,
										allowBlank: false,blankText:'请选择一个模板文件',
				store:new Ext.data.SimpleStore({fields:['name','value']})
			}) ;
	var infoDoclibTemplateFile = new Ext.app.LabelPanel('选择一个模板文件');
	
	//文档排序序号
	var xhDoclibCatalogCode = new Ext.form.TextField({fieldLabel: '序号',id:'xhDoclibCatalogCode',name:'sortNo',maxLength:500,minLength:1,anchor:'90%'});
	var tsDoclibCatalogCode = new Ext.app.LabelPanel('文档排序序号，请输入数字');
	
	this.firstInput=txtDoclibCatalogName;

	
	//this.doclibCatalogGrid=new DoclibCatalogGrid();
	var oCurrentDoclibCatalog=leftTree.getSelectionModel().getSelectedNode();
	//用户表单定义
	var simpleForm = new Ext.FormPanel({
		reader:new Ext.data.JsonReader({
			success:'success',
			root:'data'
		},[{name:'title',mapping:'title'}]),
	    labelAlign: 'left',
	    buttonAlign:'right',
	    bodyStyle:'padding:5px',
	    waitMsgTarget: true,
	    width: '100%',
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
	        	//顶部间隔
	        	columnWidth : 1 ,
	        	border : true ,
	        	xtype : 'panel' ,
	        	height : 10 
	        },{
	        	//第一行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtDoclibCatalogName]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoDoclibCatalogName]
	        },{
	        	//第二行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtDoclibCatalogCode]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoDoclibCatalogCode]
	        },{
	        	//第三行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[cmbDoclibLevel]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoDoclibLevel]
	        },{
	        	//第四行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[cmbDoclibTemplateFile]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoDoclibTemplateFile]
	        },{
	        	//第五行布局
	        	columnWidth:.5,
	        	hidden:'true',
	        	layout:'form',
	        	border:false,
	        	items:[xhDoclibCatalogCode]
	        },{
	        	columnWidth:.5,
	        	hidden:'true',
	        	layout:'form',
	        	border:false,
	        	items:[tsDoclibCatalogCode]
	        },{
	        	//中间间隔
	        	columnWidth : 1 ,
	        	border : true ,
	        	xtype : 'panel' ,
	        	height : 10 
	        },{
	        	//第二行布局
	        	columnWidth:1,
                layout:'form',
                border:false,
		        items:[{//标签面板
		            xtype:'tabpanel',
		            plain:true,
		            activeTab: 0,
		            height:201,
		            autoWidth:true,
		            defaults:{bodyStyle:'padding:2px'},
		            items:[{
		                title:'其他字段',
		                layout:'form',
		                defaultType: 'textfield',
		                items: this.operationGrid
		            }]
		        }]
	        }]
		},{//用户其他属性
			xtype:'hidden',
			name:'id',
			value:resourceId
		},{
		//父亲模块
			xtype:'hidden',
			name:'parentId',
			value:oCurrentDoclibCatalog!=null && !oCurrentDoclibCatalog.isRootNode()?oCurrentDoclibCatalog.id:""
		},{
			xtype:'hidden',    
			name:'levelID',      //文档级别ID隐藏表单域
			id:'levelID',
			value:''
		},{
			xtype:'hidden',    
			name:'template',      //模板文件ID隐藏表单域
			id:'template',
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
				if(Ext.get("txtDoclibCatalogName").getValue() == "" || Ext.get("txtDoclibCatalogName").getValue() == null){
					alert("分类名称不能为空!");
					return;
				}
				//判断扩展字段是否有同名现象
				var variableItems = operationGrid.store.data.items ;
				for(var i=0 ; i<variableItems.length ; i++) {
					var firstName = variableItems[i].data.name ;
					var type = variableItems[i].data.type;			
					if(firstName == "") {
						alert("扩展字段名不能为空!") ;
						return ;
					}
					if(type ==""){
						alert("请选择一种数据类型!") ;
						return ;
					}				
					for(var j=i+1 ; j<variableItems.length ; j++) {
						var secName = variableItems[j].data.name ;
						if(firstName == secName) {
							alert("请先处理好扩展字段名称同名现象!") ;
							return ;
						}
					}
				}
				var levelID = simpleForm.findById('cmbDoclibLevel').getValue();
				simpleForm.findById('levelID').setValue(levelID);
				var template = simpleForm.findById('cmbDoclibTemplateFile').getValue();
				simpleForm.findById('template').setValue(template);
				var ops=[];
            	var ds=formWindow.operationGrid.getStore();
            	ds.each(function(r){
            		ops.push(r.data);
            	});
				simpleForm.findById('ops').setValue(Ext.encode(ops));
				
				if(!simpleForm.form.isValid()){
					alert('数据校验失败，请检查填写的数据格式是否正确!');
					return;
				}
			
				//获取表单字段的值
				simpleForm.form.doAction('submit',{
            		url:link8,
            		method:'post',
            		waitMsg:'保存数据中，请稍候...',
            		success:function(){
            			alert('保存成功!');
            			leftTree.getRootNode().reload();
            			formWindow.close();    			
            		},
            		failure:function(){
            			alert('服务器忙，请稍候操作...');
            			simpleForm.buttons[0].enable();
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
	
	/**
	 * 加载修改数据
	 */
	this.loadData=function(){
		if(resourceId!=""){
			Ext.Ajax.request({
				url:link6+"?id="+resourceId,
				method:'POST',
				waitMsg:'等待加载数据',
				success:function(ajax){
					var responseText=ajax.responseText;	
		 			var responseObject=Ext.util.JSON.decode(responseText);
		 			canModify = responseObject.canModify;
		 			var data=responseObject.data[0];
		 			
		 			var resName=simpleForm.findById('txtDoclibCatalogName');
		 			var catalogCode = simpleForm.findById('txtDoclibCatalogCode');
		 			var xh = simpleForm.findById('xhDoclibCatalogCode');
		 			resName.setValue(data.title);
		 			catalogCode.setValue(data.catalogCode);
		 			xh.setValue(data.sortno);
		 			if(data.catalogPerm!=null){
			 			simpleForm.findById("cmbDoclibLevel").setValue(data.catalogPerm.id );	  //设置当前文档级别ID 	
					}
					if(data.templateFile!=null){
						simpleForm.findById("cmbDoclibTemplateFile").setValue(data.templateFile );	  //设置当前文档级别ID 	
					}
				}
			});
		}
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
	
	//加载文档目录下的文档显示模板列表
	this.loadTemplateList = function(){
		var doclibConfigUrl = contextPath + "/jteap/system/doclib/doclib-template-config.xml";
		var doclibXmlDom = loadXmlFile(doclibConfigUrl);
		var templates = doclibXmlDom.selectNodes("/root/template");
		var templateList = [] ;
		for(var i=0;i<templates.length;i++){
			var name = getXmlAttribute(templates[i],"name");
			var path = getXmlAttribute(templates[i],"path");
			templateList[i]= [name,path];
		}
		simpleForm.findById("cmbDoclibTemplateFile").store.loadData(templateList);	
	}
	
	//this.operationGrid.getStore().reload();
	
	DoclibCatalogEditWindow.superclass.constructor.call(this,{
        title: '文档分类信息编辑',
        width: 600,
        height:500,
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
	        	//frame:true,
	        	frame: false,    
	        	style:{bgcolor:'#fffff'},
	        	items:[new Ext.app.TitlePanel({caption:'文档分类信息编辑',border:false,frame:false})]
	        },{
	        	border:false,
	        	columnWidth:1,
	        	layout:'form',
	        	items:simpleForm
	        }]
    });
    //this.loadXmlDoclibCatalog();
}

Ext.extend(DoclibCatalogEditWindow, Ext.Window, {
	/**
	 * 加载已经配置了模块信息的xml配置文件
	 */
	loadXmlDoclibCatalog:function(){
		var tree=this.findById("txtDoclibCatalogName").getTree();
		Ext.Ajax.request({
		   url: link9,//doclibCatalog-config.xml
		   success: function(response,options){
		   		var xmlDom=response.responseXML;
		   		var doclibCatalogs=xmlDom.childNodes[0];
		   		for(var i=0;i<doclibCatalogs.childNodes.length;i++){
		   			//分组
		   			var oGroup=doclibCatalogs.childNodes[i];
		   			if(oGroup.nodeType==1){
		   				
		   				var sGroupName=oGroup.getAttribute("name");
		   				//添加分组节点
		   				var groupNode=new Ext.tree.TreeNode({text:sGroupName,expanded:true});
		   				tree.getRootNode().appendChild(groupNode);
		   				//添加各个模块
		   				for(var j=0;j<oGroup.childNodes.length;j++){
		   					var oDoclibCatalog=oGroup.childNodes[j];
		   					if(oDoclibCatalog.nodeType==1){
		   						var sDoclibCatalogName=oDoclibCatalog.getAttribute("name");
		   						//创建模块节点的时候，将模块的xml节点作为参数传入节点中
		   						groupNode.appendChild(new Ext.tree.TreeNode({text:sDoclibCatalogName,data:oDoclibCatalog}));
		   						
		   					}
		   				}
		   			}
		   		}
		   },
		   failure: function(){
		   	
		   }
		});

	}          
});








