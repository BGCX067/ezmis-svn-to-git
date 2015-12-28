/**
 * 字段列表
 */
RightGrid = function() {
	var defaultDs = this.getDefaultDS(link18);
	var grid = this;
	this.pageToolbar = new Ext.PagingToolbar( {
		pageSize : CONSTANTS_PAGE_DEFAULT_LIMIT,
		store : defaultDs,
		displayInfo : true,
		displayMsg : '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg : "没有符合条件的数据",
		items : ['-', {
			text : '导出Excel',
			handler : function() {
				exportExcel(grid, true);
			}
		}, '-', '<font color="red">*双击查看详细信息</font>']
	});
	RightGrid.superclass.constructor.call(this, {
		ds : defaultDs,
		cm : this.getColumnModel(),
		sm : this.sm,
		margins : '2px 2px 2px 2px',
		width : 600,
		height : 300,
		loadMask : true,
		frame : true,
		region : 'center',
		tbar : this.pageToolbar
	});
	
	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		//只有单选的时候才能修改，多选的只能删除
		var oBntModiPerson=mainToolbar.items.get('btnModifyUser');
		var oBtnDelPerson=mainToolbar.items.get('btnDelUser');
		var oBtnResetPassword=mainToolbar.items.get('btnInitPwd');
		var oBtnRemovePerson=mainToolbar.items.get('btnRemoveUser');
		var oLockPerson=mainToolbar.items.get('btnLockUser');
		var oUnLockUser=mainToolbar.items.get('btnUnlockUser');
		
		if(oCheckboxSModel.getSelections().length==1){
			if(oBntModiPerson) oBntModiPerson.setDisabled(false);
			if(oBtnDelPerson) oBtnDelPerson.setDisabled(false);
			if(oBtnResetPassword) oBtnResetPassword.setDisabled(false);
			if(oLockPerson) oLockPerson.setDisabled(false);
			if(oUnLockUser) oUnLockUser.setDisabled(false);
			if(oBtnRemovePerson) oBtnRemovePerson.setDisabled(false);
		}else{
			if(oBntModiPerson) oBntModiPerson.setDisabled(true);
			if(oCheckboxSModel.getSelections().length==0){
				if(oBtnDelPerson) oBtnDelPerson.setDisabled(true);
				if(oBtnResetPassword) oBtnResetPassword.setDisabled(true);
				if(oBtnRemovePerson) oBtnRemovePerson.setDisabled(true);
				if(oLockPerson) oLockPerson.setDisabled(true);
			    if(oUnLockUser) oUnLockUser.setDisabled(true);
			}else{
				if(oBtnDelPerson) oBtnDelPerson.setDisabled(false);
				if(oBtnResetPassword) oBtnResetPassword.setDisabled(false);
				if(oLockPerson) oLockPerson.setDisabled(false);
				if(oUnLockUser) oUnLockUser.setDisabled(false);
				if(oBtnRemovePerson) oBtnRemovePerson.setDisabled(false);
			}
		}
	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=grid.getSelectionModel().getSelections()[0];
	    var id=select.get("person.id");
	    if(!id){
	       id=select.id;
	    }
		var detailForm=new DetailFormWindow(id);
		detailForm.show();
		detailForm.loadData(id);
	})
	
	

}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
	sm : new Ext.grid.CheckboxSelectionModel(),

	/**
	 * 取得默认数据源 返回数据格式
	 */
	getDefaultDS : function(url) {
		var ds = new Ext.data.Store( {
			proxy : new Ext.data.ScriptTagProxy( {
				url : url
			}),

			reader : new Ext.data.JsonReader( {
				root : 'list',
				totalProperty : 'totalCount',
				id : 'id'
			}, ['person.userLoginName','person.userLoginName2', 'person.id', 'person.userName',
					'person.sex']),
			remoteSort : true
		});
		return ds;
	},

	/**
	 * PersonAction 列模型
	 */
	getColumnModel : function() {
		var cm = new Ext.grid.ColumnModel([
		    	this.sm,
		        {id:'userName',header: "用户名称", width: 120, sortable: true, dataIndex: 'person.userName'},
		        {id:'userLoginName',header: "工号", width: 120, sortable: true, dataIndex: 'person.userLoginName'},
		        {id:'userLoginName2',header: "登录名", width: 120, sortable: true, dataIndex: 'person.userLoginName2'},
				{id:'sex',header: "性别", width: 120, sortable: true, dataIndex: 'person.sex'}
			]);
		return cm;
	},
	/**
	 * 切换数据源
	 */
	changeToListDS : function(url) {
		var ds = this.getDefaultDS(url);
		var cm = this.getColumnModel();
		this.pageToolbar.bind(ds);
		this.reconfigure(ds, cm);
	},/**
	 * 删除用户
	 */
	delPerson:function(select){
		var gridPanel=this;
		var personids=this.getPersonIds(select);
		if(window.confirm("确认删除当前用户吗")){
    			//提交数据
			 	Ext.Ajax.request({
			 		url:contextPath+"/jteap/system/person/P2GAction!batchDelPersonToTheGroupAction.do",
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
	/**
    * 解除用户和角色的关系
    */
    movePerson:function(select){
  		var gridPanel=this;
  		var personids=this.getPersonIds(select);
  		var oNode = roleTree.getSelectionModel().getSelectedNode();
		  if(window.confirm("确认移除当前用户吗")){
    		//提交数据
		  	Ext.Ajax.request({
		 		url: link26,
		 		success:function(ajax){
		 			var responseText=ajax.responseText;	
		 			var responseObject=Ext.decode(responseText);
		 			var msg=responseObject.msg;
		 			if(msg!=""){alert(msg); return;}
		 			if(responseObject.success){
		 				alert("移除成功");
		 				gridPanel.getStore().reload();
		 			}
		 		},
		 		failure:function(){
		 			alert("服务器忙，请稍后操作");
				},
				method:'POST',
				params:{
					personids:personids,
					roleId : oNode.id
				}
			})	
		}
	},
	initPersonPassWord:function(select){
		var gridPanel=this;
		var personids=this.getPersonIds(select);
		if(window.confirm("确认初始化当前用户密码吗?")){
			//提交数据
		 	Ext.Ajax.request({
		 		url:contextPath+"/jteap/system/person/PersonAction!initPersonPassWordAction.do",
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
			 		url:contextPath+"/jteap/system/person/PersonAction!lockPersonAction.do",
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
		 		url:contextPath+"/jteap/system/person/PersonAction!moveLockPersonAction.do",
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
	}
});
