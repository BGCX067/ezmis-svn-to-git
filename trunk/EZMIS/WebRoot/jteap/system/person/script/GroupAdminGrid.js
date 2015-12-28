var GroupAdminGrid=function(groupId){
	//人员记录结构
	var Person=Ext.data.Record.create([
		{name: 'person.userLoginName'},
	    {name: 'person.userName'},{name:'person.sex'},{name:'person.status'}
	]);
	var adminGrid=this;
	this.groupId=groupId;
	//取得当前组织的管理员列表数据相关参数----------------------------------
	var ds_admins = new Ext.data.Store({
        proxy: new Ext.data.ScriptTagProxy({
            url: contextPath+"/jteap/system/person/P2GAction!getAdminPersonAction.do?id="+groupId
        }),
        reader: new Ext.data.JsonReader({
            root: 'list',
            totalProperty: 'totalCount',
            id: 'id'
        }, Person),
        remoteSort: true	
    });	
	var cm_admins = new Ext.grid.ColumnModel([{
           header: "用户名",
           dataIndex: 'person.userLoginName',
           width: 130
        },{
           header: "昵称",
           dataIndex: 'person.userName',
           width: 130
        },{
        	header:"操作",
        	dataIndex:'op',
        	renderer:function(){return "<a href='javascript:void(0)'>移除</a>"}
        }
    ]);
    ds_admins.load();
    
    //取得当前组织的非管理员列表数据相关参数-----------------------------------
	var fieldTxt=new Ext.form.TextField();
	fieldTxt.on("specialkey",function(field,e){
		if(e.getKey()==Ext.EventObject.ENTER){
			adminGrid.combogrid.grid.getStore().filter("person.userLoginName",field.getValue(),true);	
		}
	})
	
	var cm_noAdmins=new Ext.grid.ColumnModel([
        {id:'userName',header: "昵称", width: 120, sortable: true, dataIndex: 'person.userName'},
        {id:'userLoginName',header: "用户名", width: 200, sortable: true, dataIndex: 'person.userLoginName'}
	])
	var ds_noAdmins = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: link1+"?isAdmin=0&groupId="+groupId
        }),
        reader: new Ext.data.JsonReader({
            root: 'list',
            totalProperty: 'totalCount',
            id: 'id'
        }, Person),
        remoteSort: false	
    });
    
    
    //-------------------------------------------------------------------
	this.combogrid=new Ext.app.ComboGrid({
		xtype:'combogrid',
		ds:ds_noAdmins,
		cm:cm_noAdmins,
		dropDownRefreshData:true,//每次下拉框都需要更新数据
		gridWidth:380,
		gridHeight:250,
		valueField:'person.userLoginName',
		displayField:'person.userName',
		//listWidth:300,
		fieldLabel:'父角色',
		name:'parentRole',
		anchor:'90%',
		dataUrl:'combogrid.json',
		tbar:["用户名:",fieldTxt,{text:'查询',handler:function(){
													adminGrid.combogrid.grid.getStore().filter("person.userLoginName",fieldTxt.getValue(),true);									  
											  }},'-','<font color="red">*双击条目选择</font>']
	});
	
    //构建初始化参数
	var config={
        store: ds_admins,
        cm: cm_admins,
        width:532,
        height:168,
        
       	tbar:[this.combogrid,{
       		id:'btn_tjglry',
       		text: '添加管理人员',
       		disabled :false,
            handler : function(bt){
            	
            	//bt.setDisabled(true);
				var comboGrid=adminGrid.combogrid;
				var userLoginName=comboGrid.getHiddenValue();
				var userName=comboGrid.getValue();
				if(userName==null || userName==""){
					alert("请选择本组织的用户作为管理员");
					return;
				}
				var userName=comboGrid.getValue();
				var newPerson=new Person({
					"person.userLoginName":userLoginName,
					"person.userName":userName
				});
				//提交数据
			 	Ext.Ajax.request({
			 		url:link9,
			 		success:function(ajax){
			 			var responseText=ajax.responseText;	
		 				var responseObject=Ext.util.JSON.decode(responseText);
		 				if(responseObject.success){
		 					ds_admins.insert(0,newPerson);
		 					//debugger;
             				comboGrid.clearValue();
             				bt.setDisabled(false);
             				alert("管理员添加成功");
             				
		 				}
			 		},
			 		failure:function(){
			 			alert("服务器忙，请稍后处理");
			 		},
			 		method:'POST',
			 		params:{userLoginName:userLoginName,groupId:groupId}
			 	})
			 	
				
            }
       }],
        frame:true
    };
    
	GroupAdminGrid.superclass.constructor.call(this,config);
	
	//为选择模型添加事件，操作列上不需要选择操作

    this.on("cellclick",function(grid, rowIndex, columnIndex,e ){
    	if(e.target.innerHTML=="移除"){
    		if(confirm("确定去除该用户的管理员资格吗？")){
    			var store=grid.getStore();
    			var person=store.getAt(rowIndex);
    			var userLoginName=person.data["person.userLoginName"];
    			
	    		
	    		//提交数据
			 	Ext.Ajax.request({
			 		url:link10+"?userLoginName="+userLoginName+"&groupId="+this.groupId,
			 		success:function(ajax){
			 			var responseText=ajax.responseText;	
		 				var responseObject=Ext.util.JSON.decode(responseText);
		 				if(responseObject.success){
		 					store.remove(store.getAt(rowIndex));
             				alert("移除成功");
		 				}
			 		},
			 		failure:function(){
			 			alert("服务器忙，请稍后处理");
			 		},
			 		method:'POST',
			 		params:{}
			 	})
	    		
	    		
    		}
    		
    	}
    })
   
};

Ext.extend(GroupAdminGrid,Ext.grid.GridPanel,{
})