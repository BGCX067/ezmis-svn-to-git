


var DetailFormWindow=function(id){
	//句柄
     var formWindow=this;
	/**
	* 创建组织树
    */
	function createGroupTree(){
		var tree=new Ext.tree.TreePanel({
			id:'groupTree',
		    autoScroll:true,
		    height:170,
		    width:368,
		    animate:false,
		    enableDD:true,
		    containerScroll: true,
		    defaults: {bodyStyle:'padding:0px'},
		    border :false,
		    hideBorders :true,
		    rootVisible :true,
		    lines :false,
		    bodyBorder :false,
		    root:new Ext.tree.TreeNode({
		    	text:'我的组织',expanded:true})
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
	        width:368,
	        animate:false,
	        
	        enableDD:true,
	        containerScroll: true,
	        defaults: {bodyStyle:'padding:0px'},
	        border :false,
	        hideBorders :true,
	        rootVisible :true,
	        lines :false,
	        bodyBorder :false,
	        root:new Ext.tree.TreeNode({text:'我的角色',expanded:true})
	    });
	    return tree;
	}
	/**
	 * 创建权限树
	 */
	function createResTree(){
		var tree=new Ext.tree.TreePanel({
	    	id:'resTree',
	        autoScroll:true,
	        autoHeight:false,
	        height:170,
	        width:368,
	        animate:false,
	        
	        enableDD:true,
	        containerScroll: true,
	        defaults: {bodyStyle:'padding:0px'},
	        border :false,
	        hideBorders :true,
	        rootVisible :true,
	        lines :false,
	        bodyBorder :false,
	        root:new Ext.tree.TreeNode({text:'我的权限',expanded:true})
	    });
	    return tree;
	}
	
	var LV=Ext.app.LabelValuePanel;
	
	var lvUserName=new LV({id:'lvUserName',label:'中文名：',value:' '}); 
	var lvUserLoginName=new LV({id:'lvUserLoginName',label:'工号：',value:' '});;
	var lvUserLoginName2 = new LV({id:'lvUserLoginName2',label:'登录名：',value:' '})
	var lvSex=new LV({id:'lvSex',label:'性别：',value:' '}); 
	var lvBirthDay=new LV({id:'lvBirthDay',label:'出生日期：',value:' '}); 
   
	var propsGrid = new Ext.grid.PropertyGrid({
	        nameText: 'Properties Grid',
	        width:368,
	        id:'personProperties',
	        height : 166,
	        footer :true,
	        viewConfig : {
	            forceFit:true,
	            autoFill :true,
	            scrollOffset:17 // the grid will never have scrollbars
	        }
	});
	propsGrid.on("beforeedit",function(e){
		e.cancel=true;
	})

	//创建结构
	this.groupTree=createGroupTree();
    this.roleTree=createRoleTree();
	this.resTree=createResTree();
	
	/**
	 * 用户属性列表为一个隐藏域
	 */
	function encodeUserConfig(){
		var configField=simpleForm.find("name","userConfig")[0];
		var dataSource=Ext.util.JSON.encode(propsGrid.getSource());
		configField.setValue(dataSource);
	}
	
	//表单定义
	var simpleForm = new Ext.FormPanel({
	    buttonAlign:'right',
	    bodyStyle:'padding:5px',
	    width: 393,
	    frame:true, 					//圆角风格
		items: [{
			layout:'column',
	        border:false,
	        items:[{
	        	//第一行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[lvUserLoginName]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[lvUserName]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[lvUserLoginName2]
	        },{
	        	//第二行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[lvSex]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[lvBirthDay]
	        },{
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
		            },{
		                title:'所属组织',
		                layout:'form',
		                items: this.groupTree
		            },{
		                title:'角色',
		                layout:'form',
		                items: this.roleTree
		            },{
		                title:'权限',
		                layout:'form',
		                items: [this.resTree]
		            }]
		        }]
	        }]
		},{//用户其他属性
			xtype:'hidden',
			name:'userConfig',
			value:''
		}],
	
		buttons: [{
            text: '关闭',
            handler:function(){formWindow.close();}
        }]
	});
			
			
	DetailFormWindow.superclass.constructor.call(this,{
        title: '用户详细信息',
        width: 430,
        height:430,
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
	        	items:[new Ext.app.TitlePanel({caption:'用户信息',border:false})]
	        },{
	        	border:false,
	        	columnWidth:1,
	        	layout:'form',
	        	items:simpleForm
	        }]
    });
		   
}
/**
 * 显示页面数据调用
 */
Ext.extend(DetailFormWindow, Ext.Window, {
	
	loadData:function(id){
		formWindow=this;
		Ext.Ajax.request({
	 		url:link23,
	 		success:function(ajax){
	 			var responseText=ajax.responseText;	
	 			var responseObject=Ext.util.JSON.decode(responseText);
	 			if(responseObject.success){ 
	 				var person=responseObject.data[0];
	 				var group=responseObject.group;
	 				var role=responseObject.role;
	 				var resource=responseObject.resource;
	 				var config=person.config;
	 			 	var bdt=person.birthday;
	 				formWindow.findById('lvUserLoginName').setValue(person.userLoginName);
	 				formWindow.findById('lvUserName').setValue(person.userName);
	 				formWindow.findById('lvSex').setValue(person.sex);
	 				formWindow.findById('lvUserLoginName2').setValue(person.userLoginName2);
	 				if(bdt){
	 					formWindow.findById('lvBirthDay').setValue(formatDate(new Date(person.birthday["time"]),"yyyy-MM-dd"));
	 				}
	 				if(config!=null&&config!=""){
	 					var configObject=Ext.util.JSON.decode(config);
	 					formWindow.findById('personProperties').setSource(configObject);
	 				}
					var	groupRoot=formWindow.groupTree.getRootNode();
					var roleRoot=formWindow.roleTree.getRootNode();
					var resRoot=formWindow.resTree.getRootNode();
			
	 				for(var i=0;i<group.length;i++){
		            	groupRoot.appendChild(new Ext.tree.TreeNode({text:group[i],expanded:true}));
		            }
					for(var i=0;i<role.length;i++){
						roleRoot.appendChild(new Ext.tree.TreeNode({text:role[i],expanded:true}));
					}
					for(var i=0;i<resource.length;i++){
						resRoot.appendChild(new Ext.tree.TreeNode({text:resource[i],expanded:true}));
					}
					
	 				
	 			}else{
	 				alert("删除失败");
	 			}
	 		},
	 		failure:function(){
	 			alert("提交失败");
	 		},
	 		method:'POST',
	 		params:{id:id}
	 	})
	}
});








