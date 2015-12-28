PersonGrid=function(){
	var personGrid = this
    this.ddText="移动{0}个人" ;
    var defaultDs=this.getPersonActionDS(link12);
    this.exportLink = link12
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'导出Excel',listeners:{click:function() {
			exportExcel(personGrid,true)
		}}},'-','<font color="red">*双击查看详细信息</font>']
	});
	PersonGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getPersonActionColumnModel(),
		sm: this.sm,
	 	ddGroup:'GridDD',
	 	enableDragDrop:true,
	 	dropAllowed: true, 
		dragAllowed: true,
	   margins:'2px 2px 2px 2px',
		width:600,
		height:300,
		loadMask: true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar ,
		bbar:['*蓝色人员为当前组织的管理人员']
	});	
	//this.getStore().load({params:{start:0, limit:CONSTANTS_PAGE_DEFAULT_LIMIT}});
	
	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		//只有单选的时候才能修改，多选的只能删除
		var oBntModiPerson=mainToolbar.items.get('btnModifyUser');
		var oBtnDelPerson=mainToolbar.items.get('btnDelUser');
		var oBtnResetPassword=mainToolbar.items.get('btnInitPwd');
		var oBtnRemovePerson=mainToolbar.items.get('btnRemoveUser');
		var oLockPerson=mainToolbar.items.get('btnLockUser');
		var oUnLockUser=mainToolbar.items.get('btnUnlockUser');
		var obtnSetupAdmin=mainToolbar.items.get('btnSetupAdmin');
		var obtnSetupRole=mainToolbar.items.get('btnSetupRole');
		var obtnSetupRes=mainToolbar.items.get('btnSetupRes');
		var btnDataperm = mainToolbar.items.get("btnDataperm");//数据权限
		var btnRemoveAdmin = mainToolbar.items.get("btnRemoveAdmin");
		var btnRoleLink = mainToolbar.items.get("btnRoleLink");
		
		if(oCheckboxSModel.getSelections().length==1){
			if(obtnSetupRole) obtnSetupRole.setDisabled(false);
			if(obtnSetupRes) obtnSetupRes.setDisabled(false);
			if(oBntModiPerson) oBntModiPerson.setDisabled(false);
			if(oBtnDelPerson) oBtnDelPerson.setDisabled(false);
			if(btnRoleLink) btnRoleLink.setDisabled(false);
			
			if(oBtnResetPassword) oBtnResetPassword.setDisabled(false);
			if(oLockPerson) oLockPerson.setDisabled(false);
			if(oUnLockUser) oUnLockUser.setDisabled(false);
			var Node=groupTree.getSelectionModel().getSelectedNode();
			if(!Node.dissociation&&!Node.isRootNode()){
				if(oBtnRemovePerson) oBtnRemovePerson.setDisabled(false);
				if(obtnSetupAdmin)	obtnSetupAdmin.setDisabled(false);
				if(btnRemoveAdmin && oCheckboxSModel.getSelections()[0].json.admin)	btnRemoveAdmin.setDisabled(false);
			}
			if(btnDataperm) btnDataperm.setDisabled(false);
			
		}else{
			if(btnRoleLink) btnRoleLink.setDisabled(true);
			if(oBntModiPerson) oBntModiPerson.setDisabled(true);
		    if(btnRemoveAdmin)	btnRemoveAdmin.setDisabled(true);
			if(oCheckboxSModel.getSelections().length==0){
				if(obtnSetupRole) obtnSetupRole.setDisabled(true);
				if(btnDataperm) btnDataperm.setDisabled(true);
				if(oBtnDelPerson) oBtnDelPerson.setDisabled(true);
				if(oBtnResetPassword) oBtnResetPassword.setDisabled(true);
				if(oBtnRemovePerson) oBtnRemovePerson.setDisabled(true);
				if(oLockPerson) oLockPerson.setDisabled(true);
			    if(oUnLockUser) oUnLockUser.setDisabled(true);
			    if(obtnSetupAdmin)	obtnSetupAdmin.setDisabled(true);
			    if(obtnSetupRes) obtnSetupRes.setDisabled(true);
			}else{
				if(obtnSetupRole) obtnSetupRole.setDisabled(false);
				if(oBtnDelPerson) oBtnDelPerson.setDisabled(false);
				if(oBtnResetPassword) oBtnResetPassword.setDisabled(false);
				if(oLockPerson) oLockPerson.setDisabled(false);
				if(oUnLockUser) oUnLockUser.setDisabled(false);
				if(obtnSetupRes) obtnSetupRes.setDisabled(false);
				var Node=groupTree.getSelectionModel().getSelectedNode();
				if(!Node.dissociation&&!Node.isRootNode()){
					if(oBtnRemovePerson) oBtnRemovePerson.setDisabled(false);
					if(obtnSetupAdmin)	obtnSetupAdmin.setDisabled(false);
				}	
			}
		}
	});
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=gridPanel.getSelectionModel().getSelections()[0];
	    var id=select.get("person.id");
	    if(!id){
	       id=select.id;
	    }
		var detailForm=new DetailFormWindow(id);
		detailForm.show();
		detailForm.loadData(id);
	})
	
}
Ext.extend(PersonGrid, Ext.grid.GridPanel, {
	sm:new Ext.grid.CheckboxSelectionModel(),
	/**
	 * 切换数据源->P2GAction!showList
	 */
	changeToListDS:function(url){
		var ds = this.getP2GActionDS(url);	
		var cm=this.getP2GActionColumnModel();
		this.pageToolbar.bind(ds);
		this.reconfigure(ds,cm);
	},
	/**
	 * 切换数据源->PersonAction!showList 主要显示游离用户
	 */
	changeToPersonDS:function(url){
		var ds=this.getPersonActionDS(url);
		var cm=this.getPersonActionColumnModel();
		this.pageToolbar.bind(ds);
		this.reconfigure(ds,cm);
	},
	/**
	 * 取得P2G数据源
	 */
	getP2GActionDS:function(url){
	    var ds = new Ext.data.Store({
	        proxy: new Ext.data.ScriptTagProxy({
	            url: url
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount',
	            id: 'id'
	        }, [
	            'person.userLoginName','person.id','person.userName', 'person.sex','person.status','person.myRoles','person.userLoginName2', 'person.config'
	        ]),
	        remoteSort: true	
	    });
	    ds.setDefaultSort('person.userName', 'desc');
	   	 return ds;
	},	
	/**
	 * 取得PersonAction数据源
	 */
	getPersonActionDS:function(url){
	    var ds = new Ext.data.Store({
	        proxy: new Ext.data.ScriptTagProxy({
	            url: url
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount',
	            id: 'id'
	        }, [
	            'userLoginName', 'userName', 'sex','status','myRoles', 'userLoginName2', 'config'
	        ]),
	        remoteSort: true	
	    });
	    ds.setDefaultSort('userName', 'desc');
		return ds;
	},
    
	/**
	 * P2GAction 列模型
	 */
	getP2GActionColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
		    	this.sm,
		        {id:'userName',header: "用户名称", width: 80, sortable: true, dataIndex: 'person.userName',
		         renderer:function(value,metadata,record,rowIndex,colIndex,store){
			    	if(record.json.admin==true)
			    		return "<span style='color:blue;font-weight:bold;'>"+value+"</span>";
			    	else
			    		return value;
			    }},
		        {id:'userLoginName',header: "工号", width: 80, sortable: true, dataIndex: 'person.userLoginName'},
		        {id:'userLoginName2',header: "登录名", width: 80, sortable: true, dataIndex: 'person.userLoginName2'},
				{id:'sex',header: "性别", width: 50, sortable: true, dataIndex: 'person.sex'},
				{id:'myRoles',header: "角色", width: 300, sortable: false, dataIndex: 'person.myRoles'},
				{id:'status',header: "状态", width: 120, sortable: true, dataIndex: 'person.status',					
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						var result=record.json.person.status;
						if(result=='锁定'){
							result="<span style='font-weight:bold;color:red'>"+result+"</span>"
						}
						return result;
					}
				}	
			]);
		return cm;
	},
	/**
	 * PersonAction 列模型
	 */
	getPersonActionColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
		    	this.sm,
		        {id:'userName',header: "用户名称", width: 80, sortable: true, dataIndex: 'userName'},
		        {id:'userLoginName',header: "工号", width: 80, sortable: true, dataIndex: 'userLoginName'},
		        {id:'userLoginName2',header: "登录名", width: 80, sortable: true, dataIndex: 'userLoginName2'},
				{id:'sex',header: "性别", width: 50, sortable: true, dataIndex: 'sex'},
				{id:'myRoles',header: "角色", width: 300, sortable: false, dataIndex: 'myRoles'},
				{id:'status',header: "状态", width: 120, sortable: true, dataIndex: 'status',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						result=record.json.status;
						if(result=='锁定'){
							result="<span style='font-weight:bold;color:red'>"+result+"</span>"
						}
						return result;
					}
				}	
			]);
		return cm;
	},
	getPersonIds:function(select){
		  var personids="";
			for(var i=0;i<select.length;i++){
			    var temp=select[i];
			    var id=temp.get("person.id");
				if(!id){
				   personids+=temp.id+",";
				}else{
				   personids+=id+",";
				}
			}
		return personids;
	},
	getP2GIds:function(select){
		var p2gids="";
		for(var i=0;i<select.length;i++){
		    var temp=select[i];
		    var id=temp.id;
			p2gids+=temp.id+",";
		}
		return p2gids;
	},
	/**
	 * 删除用户
	 */
	delPerson:function(select){
		var gridPanel=this;
		var personids=this.getPersonIds(select);
		if(window.confirm("确认删除当前用户吗")){
    			//提交数据
			 	Ext.Ajax.request({
			 		url:link21,
			 		success:function(ajax){
			 			var responseText=ajax.responseText;	
			 			var responseObject=Ext.decode(responseText);
			 			var msg=responseObject.msg;
			 			if(msg!=""){alert(msg); return;}
			 			if(responseObject.success){
			 				alert("删除成功");
			 				gridPanel.getStore().reload();
			 			}else{
			 				alert("删除失败");
			 			}
			 		},
			 		failure:function(){
			 			alert("提交失败");
			 		},
			 		method:'POST',
			 		params:{personids:personids}
			 	})	
       }
	},
   /**
    * 解除用户和组织的关系
    */
    movePerson:function(select){
  		var gridPanel=this;
  		var p2gids=this.getP2GIds(select);
		  if(window.confirm("确认移除当前用户吗")){
    		//提交数据
		  	Ext.Ajax.request({
		 		url:link22,
		 		success:function(ajax){
		 			var responseText=ajax.responseText;	
		 			var responseObject=Ext.decode(responseText);
		 			var msg=responseObject.msg;
		 			if(msg!=""){alert(msg); return;}
		 			if(responseObject.success){
		 				alert("移除成功");
		 				gridPanel.getStore().reload();
//		 				groupTree.getRootNode().reload();
		 			}
		 		},
		 		failure:function(){
		 			alert("服务器忙，请稍后操作");
				},
				method:'POST',
				params:{p2gids:p2gids}
			})	
		}
	},
	initPersonPassWord:function(select){
		var gridPanel=this;
		var personids=this.getPersonIds(select);
		if(window.confirm("确认初始化当前用户密码吗?")){
			//提交数据
		 	Ext.Ajax.request({
		 		url:link24,
		 		success:function(ajax){
		 			var responseText=ajax.responseText;	
		 			var responseObject=Ext.util.JSON.decode(responseText);
		 			if(responseObject.success){
		 				alert("初始化成功");
		 				gridPanel.getStore().reload();
		 			}else{
		 				alert("初始化失败");
		 			}
		 		},
		 		failure:function(){
		 			alert("提交失败");
		 		},
		 		method:'POST',
		 		params:{personids:personids}
		 	})	
		}
	},
	lockPerson:function(select){
		var gridPanel=this;
		var personids=this.getPersonIds(select);
		if(window.confirm("确认锁定用户吗?")){
    			//提交数据
			 	Ext.Ajax.request({
			 		url:link26,
			 		success:function(ajax){
			 			var responseText=ajax.responseText;	
			 			var responseObject=Ext.util.JSON.decode(responseText);
			 			if(responseObject.success){
			 				alert("锁定成功");
			 				gridPanel.getStore().reload();
			 			}else{
			 				alert("锁定失败");
			 			}
			 		},
			 		failure:function(){
			 			alert("提交失败");
			 		},
			 		method:'POST',
			 		params:{personids:personids}
			 	})	
       }
	},
	moveLockPerson:function(select){
		var gridPanel=this;
		var personids=this.getPersonIds(select);
		if(window.confirm("确认解除锁定用户吗?")){
			//提交数据
		 	Ext.Ajax.request({
		 		url:link27,
		 		success:function(ajax){
		 			var responseText=ajax.responseText;	
		 			var responseObject=Ext.util.JSON.decode(responseText);
		 			if(responseObject.success){
		 				alert("解锁成功");
		 				gridPanel.getStore().reload();
		 			}else{
		 				alert("解锁定失败");
		 			}
		 		},
		 		failure:function(){
		 			alert("提交失败");
		 		},
		 		method:'POST',
		 		params:{personids:personids}
		 	})	
		}
	},
	setupAdmin:function(select){
		var gridPanel=this;
  		var p2gids=this.getP2GIds(select);
		if(window.confirm("确认设定管理员吗?")){
			//提交数据
		 	Ext.Ajax.request({
		 		url:link29,
		 		success:function(ajax){
		 			var responseText=ajax.responseText;	
		 			var responseObject=Ext.util.JSON.decode(responseText);
		 			var msg=responseObject.msg;
		 			if(responseObject.success){
		 				alert("设定管理员成功");
		 				gridPanel.getStore().reload();
		 			}else{
		 				alert("设定管理员失败");
		 			}
		 		},
		 		failure:function(){
		 			alert("提交失败");
		 		},
		 		method:'POST',
		 		params:{p2gids:p2gids}
		 	})	
		}
	},
	removeAdmin : function(select) {
	   	if(confirm("确定去除该用户的管理员资格吗？")){
			var person=select[0]
			var userLoginName=person.data["person.userLoginName"];
			var groupId = person.json.group.id;
			
    		//提交数据
		 	Ext.Ajax.request({
		 		url:link10+"?userLoginName="+userLoginName+"&groupId="+groupId,
		 		success:function(ajax){
		 			var responseText=ajax.responseText;	
	 				var responseObject=Ext.util.JSON.decode(responseText);
	 				if(responseObject.success){
	 					gridPanel.getStore().reload();
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
	},
	/**
	 * 清除权限
	 */
	clearRes:function(select){
		var gridPanel=this;
  		var personids=this.getPersonIds(select);
		if(window.confirm("您确定清除指定人员的所有权限吗?")){
			//提交数据
		 	Ext.Ajax.request({
		 		url:link33,
		 		success:function(ajax){
		 			var responseText=ajax.responseText;	
		 			var responseObject=Ext.util.JSON.decode(responseText);
		 			var msg=responseObject.msg;
		 			if(msg!="") {alert(msg); return;}
		 			if(responseObject.success){
		 				alert("清除成功");
		 				gridPanel.getStore().reload();
		 			}
		 		},
		 		failure:function(){
		 			alert("提交失败");
		 		},
		 		method:'POST',
		 		params:{personIds:personids}
		 	})	
		}
	},
	/**
	 * 批量设置角色
	 */
	setupRole:function(select){
		var gridPanel=this;
		var personids = this.getPersonIds(select);
		var idx = personids.lastIndexOf(",");
		if(idx == personids.length-1){
			personids = personids.substr(0,idx);
		}
		this.showIndicatRoleWindow(personids);
	},
	/**
	 * 批量设置资源
	 */
	setupRes:function(select){
		var personids=this.getPersonIds(select);
		this.showSetupResourceWindow(personids);
		
	},
	
	/**
	 * 显示指定角色的窗口
	 */
	showIndicatRoleWindow: function (personids){
		var gridPanel=this;
		//指定角色树
		var personid=""
		if(personids.indexOf(",")<0){
			personid = personids;
		}
		
		var indicatTree=new Ext.tree.TreePanel({
	    	id:'setupRoleTree',
	        autoScroll:true,
	        autoHeight:false,
	        height:170,
	        width:150,
	        originalValue:"",
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
	        	ccCheck:true,
	        	text:'我的角色',
	        	loader:new Ext.app.CheckboxTreeNodeLoader({
		            dataUrl:contextPath+"/jteap/system/role/RoleAction!showRloeTreeForIsCheckAction.do?id="+personid
		         }),
		        expanded :true
	        }),
	        submitChange:function(){
	        	var thisx=this;
	        	var currentIds=this.getRootNode().getCheckedIds(true,false);//取得包含第三状态的节点的被选中节点编号
	        	
	        	if(personids!=""&&personids!=null){
		        	//提交数据
				 	Ext.Ajax.request({
				 		url:link30,
				 		success:function(ajax){
				 			var responseText=ajax.responseText;	
				 			var responseObject=Ext.util.JSON.decode(responseText);
				 			if(responseObject.success){
				 				alert("角色指定成功");
				 				gridPanel.getStore().reload();
				 			}else
				 				alert("角色指定失败");
				 			indicatWindow.close();
						},
				 		failure:function(){
				 			alert("角色指定失败");
				 		},
				 		method:'POST',
				 		params:{personids:personids,roleids:currentIds}
				 	})
				 }
	         }
	    });
	    //资源选择窗口
		 var indicatWindow = new Ext.Window({
            layout:'fit',
            title:'角色选择器',
            width:250,
            height:350,
            frame:true,
            items: indicatTree,
            buttons: [{
                text:'确定',handler:function(){
                	indicatTree.submitChange();
                }
            },{
                text: '取消',
                handler: function(){
                    indicatWindow.close();
                }
            }]
		});
		//显示窗口
		indicatWindow.show();
	},
	/**
	 * 选择资源的窗口
	 */
	showSetupResourceWindow:function(personIds){
		var personid=personIds.substring(0,personIds.length-1);
		//指定资源树
		var resTree=new Ext.tree.TreePanel({
	    	id:'setupResTree',
	        autoScroll:true,
	        autoHeight:false,
	        height:170,
	        width:150,
	        originalValue:"",
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
	        	ccCheck:true,
	        	text:'我所拥有的资源',
	        	loader:new Ext.app.CheckboxTreeNodeLoader({
		            dataUrl:contextPath+"/jteap/system/resource/ResourceAction!showAllResourcesIsForCheck.do?id="+personid
		         }),
		        expanded :true
	        }),
	        submitChange:function(){
	        	var thisx=this;
	        	//取得包含第三状态的节点的被选中节点编号
	        	var resIds=this.getRootNode().getCheckedIds(false,false);
	        	
	        	//提交数据
			 	Ext.Ajax.request({
			 		url:link32+"?personIds="+personIds+"&resIds="+resIds,
			 		success:function(ajax){
			 			var responseText=ajax.responseText;	
			 			var responseObject=Ext.util.JSON.decode(responseText);
			 			if(responseObject.success)
			 				alert("资源指定成功");
			 			else
			 				alert("资源指定失败："+responseObject.msg);
			 			
						indicatWindow.close();						
			 		},
			 		failure:function(){
			 			alert("资源指定失败");
			 		},
			 		method:'POST'
			 	})
	        	
	        }
	    });
	    //资源选择窗口
		 var indicatWindow = new Ext.Window({
            layout:'fit',
            title:'资源选择器',
            width:250,
            height:350,
            frame:true,
            items: resTree,
            buttons: [{
                text:'确定',handler:function(){
                	resTree.submitChange();
                }
            },{
                text: '取消',
                handler: function(){
                    indicatWindow.close();
                }
            }]
		});
		//显示窗口
		indicatWindow.show();
		
	}
});

