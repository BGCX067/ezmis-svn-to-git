/*
 * ***********************
 * 文档级别编辑窗口
 * ***********************
 */

var DoclibLevelEditWindow=function(resourceId){
	var formWindow=this;
	this.resourceId=resourceId;
	/*
	 * ************************
	 * 控件定义
	 * ************************
	 */
	
	
	//文档级别
	var txtDoclibLevelName=new Ext.form.TextField({fieldLabel: '级别名称',id:'txtDoclibLevelName',name:'levelName',maxLength:500,minLength:1,anchor:'90%'});
	var infoDoclibTitle = new Ext.app.LabelPanel('文档级别，1-50个字符');
	//文档级别描述
	var txtDoclibLevelDesc = new Ext.form.TextArea({fieldLabel:'级别描述', id:'txtDoclibLevelDesc',name:'levelDesc',maxLength:500,minLength:1,anchor:'90%'});
	var infoDoclibCreator = new Ext.app.LabelPanel('级别描述,1-100个字符');
	this.firstInput=txtDoclibLevelName;
	
	//this.operationGrid = new OperationGrid(resourceId);
	/**
	* 创建角色树
	*/
		function createRoleTree(){
			var tree=new Ext.tree.TreePanel({
		    	id:'roleTree',
		        autoScroll:true,
		        autoHeight:false,
		        height:170,
		        width:'100%',
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
			           dataUrl:link16
			        }),
			        expanded :true
		        })
		    });
		    return tree;
		}	
		
	
	var roleTree=createRoleTree();
	roleTree.getRootNode().on("expand",function(){
			formWindow.loadRole();
			
	   });
	
	//用户表单定义
	var simpleForm = new Ext.FormPanel({
		reader:new Ext.data.JsonReader({
			success:'success',
			root:'data'
		},[{name:'levelName',mapping:'levelName'}]),
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
	        	columnWidth:.6,
	        	layout:'form',
	        	border:false,
	        	items:[txtDoclibLevelName]
	        },{
	        	//第一行布局
	        	columnWidth:.4,
	        	layout:'form',
	        	border:false,
	        	items:[infoDoclibTitle]
	        },{
	        	//第二行布局
	        	columnWidth:.6,
	        	layout:'form',
	        	border:false,
	        	items:[txtDoclibLevelDesc]
	        },{
	        	//第二行布局
	        	columnWidth:.4,
	        	layout:'form',
	        	border:false,
	        	items:[infoDoclibCreator]
	        },{
	        	//中间间隔
	        	columnWidth : 1 ,
	        	border : true ,
	        	xtype : 'panel' ,
	        	height : 10 
	        },{
	        	//第三行布局
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
		                title:'角色列表',
		                layout:'form',
		                defaultType: 'textfield',
		                items: roleTree
		            }]
		        }]
	        }]
		},{//用户其他属性
			xtype:'hidden',
			name:'id',
			value:resourceId
		}],
	
	buttons: [{
			text:'保存并关闭',
			handler:function(){
				//如果是新建，则将所有的操作传输到后台
				//提交表单的时候将属性列表中的数据转换成JSON字符串传入隐藏域中
				if(Ext.get("txtDoclibLevelName").getValue() == "" || Ext.get("txtDoclibLevelName").getValue() == null){
					alert("级别名称不能为空!");
					return;
				}
				var roleIds="" ;
				//树是否渲染了
				if(roleTree.rendered){
						roleIds=roleTree.getRootNode().getCheckedIds(true,false);
				}
				if(!simpleForm.form.isValid()){
					alert('数据校验失败，请检查填写的数据格式是否正确!');
					return;
				}

				//获取表单字段的值
				simpleForm.form.doAction('submit',{
            		url:link1,
            		method:'post',
            		params:{roleId:roleIds},
            		waitMsg:'保存数据中，请稍候...',
            		success:function(){
            			alert('保存成功!');
            			rightGrid.getStore().reload();
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
		Ext.Ajax.request({
			url:link2+"?id="+resourceId,
			method:'post',
			waitMsg:'等待加载数据',
			success:function(ajax){
				var responseText=ajax.responseText;	
	 			var responseObject=Ext.util.JSON.decode(responseText);
	 			if(responseObject.success){
	 				var data=responseObject.fdlist;
	 				var resName=simpleForm.findById('txtDoclibLevelName');
	 				resName.setValue(data.levelName);
	 				simpleForm.findById('txtDoclibLevelDesc').setValue(data.levelDesc);			 				
	 			}
			}
		});
		
	}
	//this.operationGrid.getStore().reload();
	
	//加载已经选中的角色
	this.loadRole = function(){
		var node  ;
		Ext.Ajax.request({
			url:link18+"?id="+resourceId,
			method:'post',
			waitMsg:'等待加载数据',
			success:function(ajax){
				var responseText=ajax.responseText;	
	 			var responseObject=Ext.util.JSON.decode(responseText);
	 			if(responseObject.success){
	 				var data=responseObject.fdlist;			
	 				for(var i=0;i<data.length;i++){
	 					node = roleTree.getNodeById(data[i].role.id);
	 					if(node!=null){
	 						node.getUI().checkbox.checked = true ;
	 					}
	 				}
	 			}
			}
		});
			
	}
	
	DoclibLevelEditWindow.superclass.constructor.call(this,{
        title: '文档级别信息编辑',
        width: 420,
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
	        	frame:false,
	        	items:[new Ext.app.TitlePanel({caption:'文档级别信息编辑',border:false})]
	        },{
	        	border:false,
	        	columnWidth:1,
	        	layout:'form',
	        	items:simpleForm
	        }]
    });
    //this.loadXmlDoclibCatalog();
}

Ext.extend(DoclibLevelEditWindow, Ext.Window, {
});








