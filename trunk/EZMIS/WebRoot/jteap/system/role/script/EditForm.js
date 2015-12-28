
var EditFormWindow=function(){
		var Node=roleTree.getSelectionModel().getSelectedNode();
		var roleDefaultId="";
		if(Node&&!Node.isRootNode()){
			roleDefaultId=Node.id;	
		}
		var formWindow=this;
		//把XML中的文件解析成人员的属性
		Ext.Ajax.request({
		   url: link27,
		   success: function(response,options){
		   		var configData={};
		   		var xmlDom=getDom(response.responseText);
		   		var config=xmlDom.childNodes[1];
		   		for(var i=0;i<config.childNodes.length;i++){
		   			var oconfig=config.childNodes[i];
		   			if(oconfig.tagName=="property"){
		   			  var sConfigName=oconfig.getAttribute("name");
		   			  var sConfigValue=oconfig.getAttribute("value");
		   			  var sConfigType=oconfig.getAttribute("type");
		   			  if(sConfigType=="Date"){
		   			  	configData[sConfigName]=new Date(Date.parse(sConfigValue));
		   			  }else{
		   			  	configData[sConfigName]=sConfigValue;
		   			  }
		   			}
		   		}
		   		formWindow.datasource=configData;
		   		formWindow.load();
		   		formWindow.show();
		   }
		});

		/**
		 * 创建组织树
		 */
		function createGroupTree(){
			var tree=new Ext.tree.TreePanel({
		    	id:'groupTreex',
		        autoScroll:true,
		        height:170,
		        width:539,
		        animate:false,
		        ctrlCasecade:true,	//是否只支持 按住ctrl键进行勾选的时候是级联勾选
		        containerScroll: true,
		        defaults: {bodyStyle:'padding:0px'},
		        border :false,
		        hideBorders :true,
		        rootVisible :true,
		        lines :false,
		       	bbar:['-','<font color="blue">*按住CTRL键可进行级联选择</font>','-'],
		        bodyBorder :false,
		    	root:new Ext.app.CheckboxAsyncTreeNode({
		        	ccCheck:false,
		        	text:'组织',
		        	loader:new Ext.app.CheckboxTreeNodeLoader({
			            dataUrl:link28,
			            listeners:{
			            	beforeload:function(loader, node, callback ){
			            		this.baseParams.parentId=(node.isRoot?"rootNode":node.id);
			            	}
			            }
			        }),
			        expanded :true
		        })
		    });
		    return tree;
		}
		
		/**
		 * 创建角色树
		 */
		function createRoleTree(){
			var tree=new Ext.tree.TreePanel({
		    	id:'roleTree',
		        autoScroll:true,
		        autoHeight:false,
		        height:170,
		        width:539,
		        animate:false,
		        ctrlCasecade:true,	//是否只支持 按住ctrl键进行勾选的时候是级联勾选
		        enableDD:true,
		        containerScroll: true,
		        defaults: {bodyStyle:'padding:0px'},
		        border :false,
		        hideBorders :true,
		        rootVisible :true,
		        lines :false,
		       	bbar:['-','<font color="blue">*按住CTRL键可进行级联选择</font>','-'],
		        bodyBorder :false,
		        root:new Ext.app.CheckboxAsyncTreeNode({
		        	ccCheck:false,
		        	text:'角色',
		        	loader:new Ext.app.CheckboxTreeNodeLoader({
			            dataUrl:link29,
			            listeners:{load:function(){
			            	//将树加载完之后将已经指定的资源勾选上
		            		var defaultId=roleDefaultId;
							var nodex=tree.getNodeById(defaultId);
							if(nodex)
						    	nodex.setChecked(true,false);
						}}
			        }),
			        expanded :true
		        })
		    });
		    return tree;
		}
		
		
		
		
	this.load=function load(){
		Ext.form.Field.prototype.msgTarget = 'side';
		
		// 姓名
		var txtUserName={xtype:'textfield',fieldLabel:'姓名',name:'userName',regex:/^\S+$/,regexText:'姓名中不能包含空格',maxLength:10,minLength:2,inputType:'textfield',allowBlank:false,anchor:'90%'};
		
		var infoUserName=new Ext.app.LabelPanel('用户真实姓名，2-10汉字或有效字符');
		
		
		//工号
		var txtUserLoginName={xtype:'uniquetextfield',notUniqueText:'该工号已经存在，请使用其他工号',url:link30,id:'txtUserLoginName',fieldLabel:'工号',vtype:'alphanum',maxLength:4,minLength:4,minLengthText:'只能输入4位字母数字组合',maxLengthText:'只能输入4位字母数字组合',name:'userLoginName',allowBlank:false,anchor:'90%',
							listeners:{blur:function(txtField){
								
							}}};
		var infoUserLoginName=new Ext.app.LabelPanel('用户工号，4位字母及数字');

//		//登录名
//		var txtUserLoginName2={xtype:'uniquetextfield',otUniqueText:'该工号已存在，请使用其他工号',fieldLabel:'工号',url:link44,name:'gh',vtype:'alphanum',maxLength:10,minLength:2,minLengthText:'最少需要2位字母数字组合',maxLengthText:'最长10位字母数字组合',allowBlank:false,anchor:'90%'};
//		
//		var infoGh =new Ext.app.LabelPanel('用户工号，2-10汉字或有效字符');
		
		this.firstInput=txtUserName;

		//性别
		var comboSex={
			xtype:'combo',
			store: new Ext.data.SimpleStore({fields: ["retrunValue", "displayText"],data: [['男','男'],['女','女']]}),
			valueField :"retrunValue",
			displayField: "displayText",
			triggerAction: 'all',
			mode: 'local',
			forceSelection: true,
			blankText:'请选择性别',
			emptyText:'请选择性别',
			hiddenName:'userSex',
			editable: false,
			allowBlank:false,
			fieldLabel: '性别',
			name: 'education',
			anchor:'90%'
		}
		var infoUserSex=new Ext.app.LabelPanel('选择性别');
		
		//出生日期
		var dtBirthday={xtype:'datefield',fieldLabel: '出生日期',format:'Y-m-d',name: 'userBirthday',anchor:'90%'}
		
		var infoBirthday=new Ext.app.LabelPanel('选择出生日期');
	  	
	    //其他属性
		var propsGrid = new Ext.grid.PropertyGrid({
		        nameText: 'Properties Grid',
		        width:539,
		        height:166,
		        autoHeight:false,
		        footer :true,
		        viewConfig : {
		            forceFit:true,
		            autoFill :true,
		            scrollOffset:17 // the grid will never have scrollbars
		        },
		        source:this.datasource
		});
		
	
		var groupTreex=createGroupTree();
	
		var roleTree=createRoleTree();
		
		/**
		 * 用户属性列表为一个隐藏域
		 */
		function encodeUserConfig(){
			var configField=simpleForm.find("name","userConfig")[0];
			var dataSource=Ext.util.JSON.encode(propsGrid.source);
			configField.setValue(dataSource);
	
			}
	   /**
	    * 为组织增加人员
	    */
	
		//用户表单定义
		var simpleForm = new Ext.FormPanel({
		    labelAlign: 'left',
		    buttonAlign:'right',
		    bodyStyle:'padding:5px',
		    waitMsgTarget: true,
		    width: 566,
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
		        	items:[txtUserName]
		        },{
		        	columnWidth:.5,
		        	layout:'form',
		        	border:false,
		        	items:[infoUserName]
		        },{
		        	//第二行布局
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
			                title:'角色',
			                layout:'form',
			                items: roleTree
			            },{
			                title:'所属组织',
			                layout:'form',
			                items: groupTreex
			            },{
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
					var roleIds="",groupIds="";
					//树是否渲染了
					if(roleTree.rendered){
						roleIds=roleTree.getRootNode().getCheckedIds(true,false);
					}else{
						roleIds=roleDefaultId;
					}
					
					if(groupTreex.rendered){
						groupIds=groupTreex.getRootNode().getCheckedIds(true,false);
					}
					
					simpleForm.form.doAction('submit',{
						url:link31,
						method:'post',
	            		waitMsg:'保存数据中，请稍候...',
	            		params:{roleId:roleIds,groupId:groupIds},
	            		success:function(){
	            			if(window.confirm('保存成功,还需要新增吗？')){
	            				rightGrid.getStore().reload();
	            				groupTree.getRootNode().reload();
	            				simpleForm.form.reset();
	            				simpleForm.buttons[0].enable();
	            			}else{
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
				text:'保存&关闭',
				handler:function(){
					//提交表单的时候将属性列表中的数据转换成JSON字符串传入隐藏域中
					encodeUserConfig();
					if(!simpleForm.form.isValid()){
						alert('数据校验失败，请检查填写的数据格式是否正确');
						return;
					}
					var roleIds="",groupIds="";
					//树是否渲染了
					if(roleTree.rendered){
						roleIds=roleTree.getRootNode().getCheckedIds(true,false);
					}else{
						roleIds=roleDefaultId;
					}
					
					if(groupTreex.rendered){
						groupIds=groupTreex.getRootNode().getCheckedIds(true,false);
					}
					
					simpleForm.form.doAction('submit',{
						url:link31,
						method:'post',
	            		waitMsg:'保存数据中，请稍候...',
	            		params:{roleId:roleIds,groupId:groupIds},
	            		success:function(){
	            				alert('保存成功');
	            				rightGrid.getStore().reload();
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
	            text: '重置',
	            handler:function(){simpleForm.form.reset();}
	        },{
	            text: '取消',
	            handler:function(){formWindow.close();}
	        }]
		
		});
		
		EditFormWindow.superclass.constructor.call(this,{
	        title: '用户编辑',
	        width: 600,
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
		}
	   
Ext.extend(EditFormWindow, Ext.Window, {

});









