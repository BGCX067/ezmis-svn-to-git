
var ModuleEditFormWindow=function(resourceId){
	
	var formWindow=this;
	this.resourceId=resourceId;
	/*
	 * ************************
	 * 控件定义
	 * ************************
	 */
	
	
	//模块名
	var txtModuleName={xtype:'combotree',id:'txtModuleName',localData:true,fieldLabel:'模块名',rootVisible:false,maxLength:30,minLength:1,minLengthText:'最少需要1个字符（包括汉子和合法字符）',maxLengthText:'最长30个字符',name:'resName',allowBlank:false,listWidth:220,anchor:'90%',
		editable:true,//可编辑
		listeners:{
			//渲染的时候加载xml配置中的模块
			render:function(txtField){
				
				formWindow.loadXmlModule();
			},
			//选择节点后，将选择的模板填写在适当的位置
			select:function(tree,node){
				formWindow.operationGrid.clearOperation();	    
				if(!node.attributes.data)
					return;
				txtModuleLink.setValue(node.attributes.data.getAttribute("url"));
				txtModuleIcon.setValue(node.attributes.data.getAttribute("icon"));
				
				var ops=node.attributes.data.childNodes;
				for(var i=0;i<ops.length;i++){
					var opNode=ops[i];
					if(opNode.nodeType==1){
						var op={
							name:opNode.getAttribute("name"),
							text:opNode.getAttribute("text"),
							tip:opNode.getAttribute("tip"),
							icon:opNode.getAttribute("icon")
						}
						var tmp=opNode.getAttribute("showText");
						
						if(tmp!=null && tmp!=""){
							op.showText=((tmp=="true")?true:false);
						}else{
							op.showText=true;
						}
						
						tmp=opNode.getAttribute("adminOp");
						if(tmp!=null && tmp!=""){
							op.adminOp=((tmp=="true")?true:false);							
						}else{
							op.adminOp=false;
						}
						formWindow.operationGrid.insertOperation(op);
					}
				}
			}
		}
	};
	var infoModuleName=new Ext.app.LabelPanel('模块名称，1-30个字符');
	
	this.firstInput=txtModuleName;
	
	
	//模块链接
	var txtModuleLink=new Ext.form.TextField({fieldLabel: '模块链接',id:'txtModuleLink',name:'link',maxLength:500,minLength:1,anchor:'90%'});
	var infoModuleLink=new Ext.app.LabelPanel('模块链接地址，1-500个字符');
	
	//模块图标
	var txtModuleIcon=new Ext.form.TextField({fieldLabel: '模块图标',id:'txtModuleIcon',name:'icon',maxLength:500,minLength:1,anchor:'90%'});
	var infoModuleIcon=new Ext.app.LabelPanel('模块图标地址，1-500个字符');
	
	//模块描述
	var txtRemark={xtype:'textfield',fieldLabel: '模块描述',id:'txtRemark',name: 'remark',anchor:'90%',maxLength:300}
	var infoRemark=new Ext.app.LabelPanel('模块描述，最多200字');
	
	//模块是否可见
	var visable={xtype:'checkbox',checked:'true',id:'visabled',fieldLabel: '模块是否可见',name: 'visiabled',value:'true'}
	var infoVisable=new Ext.app.LabelPanel('模块是否可见，默认为可见');

	this.operationGrid = new OperationGrid(resourceId);
	//this.moduleGrid=new ModuleGrid();
	var oCurrentModule=moduleTree.getSelectionModel().getSelectedNode();
	//用户表单定义
	var simpleForm = new Ext.FormPanel({
		reader:new Ext.data.JsonReader({
			success:'success',
			root:'data'
		},[{name:'resName',mapping:'resName'},{name:'link',mapping:'link'},{name:'remark',mapping:'remark'}]),
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
	        	items:[txtModuleName]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoModuleName]
	        },{
	        	//第二行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtModuleLink]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoModuleLink]
	        },{
	        	//第三行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtModuleIcon]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoModuleIcon]
	        },{
	        	//第四行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtRemark]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoRemark]
	        },{
	        	//第五行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[visable]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoVisable]
	        }
	        ,{
	        	//第六行布局
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
			value:oCurrentModule!=null && !oCurrentModule.isRootNode()?oCurrentModule.id:""
		},{
			xtype:'hidden',
			name:'ops',
			id:'ops',
			value:''
		}],
	
	buttons: [{
			text:'保存',
			handler:function(){
				//判断是新建还是修改
				if(formWindow.resourceId){
					//如果是修改，就将ops中修改了的操作传输过去
					
					if(formWindow.operationGrid.dirtyRecords.length>0){
						
						simpleForm.findById('ops').setValue(Ext.encode(formWindow.operationGrid.dirtyRecords));
					}
				}else{
					//如果是新建，则将所有的操作传输到后台
					//提交表单的时候将属性列表中的数据转换成JSON字符串传入隐藏域中
					var ops=[];
	            	var ds=formWindow.operationGrid.getStore();
	            	ds.each(function(r){
	            		ops.push(r.data);
	            	});
					simpleForm.findById('ops').setValue(Ext.encode(ops));
				}
				
				
				if(!simpleForm.form.isValid()){
					alert('数据校验失败，请检查填写的数据格式是否正确');
					return;
				}

				simpleForm.form.doAction('submit',{
            		url:link11,
            		method:'post',
            		waitMsg:'保存数据中，请稍候...',
            		success:function(){
            			alert('保存成功');
            			moduleTree.getRootNode().reload();
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
            handler:function(){formWindow.close();}
        }]
	
	});
	
	/**
	 * 加载修改数据
	 */
	this.loadData=function(){
		Ext.Ajax.request({
			url:link4+"?id="+resourceId,
			method:'GET',
			waitMsg:'等待加载数据',
			success:function(ajax){
				var responseText=ajax.responseText;	
	 			var responseObject=Ext.util.JSON.decode(responseText);
	 			var data=responseObject.data[0];
	 			
	 			var resName=simpleForm.findById('txtModuleName');
	 			var icon=simpleForm.findById('txtModuleIcon');
	 			var link=simpleForm.findById('txtModuleLink');
	 			var remark=simpleForm.findById('txtRemark');
	 			var visibled=simpleForm.findById('visabled');
	 			resName.setValue(data.resName,data.resName);
	 			icon.setValue(data.icon);
	 			link.setValue(data.link);
	 			remark.setValue(data.remark);
	 			visibled.setValue(data.visiabled);
	 			
	 			//将操作填入网格控件
	 			if(data.childRes.length>0){
	 				for(var i=0;i<data.childRes.length;i++){
	 					var op=data.childRes[i];
	 					if(op.type=='操作'){
	 						formWindow.operationGrid.insertOperation(op);
	 						//debugger;
	 					}
	 				}
	 			}
			}
		});
	}
	
	ModuleEditFormWindow.superclass.constructor.call(this,{
        title: '模块信息编辑',
        width: 580,
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
	        	frame:true,
	        	items:[new Ext.app.TitlePanel({caption:'模块信息编辑',border:false})]
	        },{
	        	border:false,
	        	columnWidth:1,
	        	layout:'form',
	        	items:simpleForm
	        }]
    });
    //this.loadXmlModule();
}

Ext.extend(ModuleEditFormWindow, Ext.Window, {
	/**
	 * 加载已经配置了模块信息的xml配置文件
	 */
	loadXmlModule:function(){
		
		var tree=this.findById("txtModuleName").getTree();
		var xmlDom = loadXmlFile(link9);
		var modules=xmlDom.documentElement || xmlDom;
		
		for(var i=0;i<modules.childNodes.length;i++){
   			//分组
   			var oGroup=modules.childNodes[i];
   			if(oGroup.nodeType==1){
   				var sGroupName=oGroup.getAttribute("name");
   				//添加分组节点
   				var groupNode=new Ext.tree.TreeNode({text:sGroupName,expanded:true});
   				tree.getRootNode().appendChild(groupNode);
   				//添加各个模块
   				for(var j=0;j<oGroup.childNodes.length;j++){
   					var oModule=oGroup.childNodes[j];
   					if(oModule.nodeType==1){
   						var sModuleName=oModule.getAttribute("name");
   						//创建模块节点的时候，将模块的xml节点作为参数传入节点中
   						groupNode.appendChild(new Ext.tree.TreeNode({text:sModuleName,data:oModule}));
   						
   					}
   				}
   			}
   		}
	}          
});








