/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link4);
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'导出Excel',handler:function(){
		exportExcel(grid,true);
		}},'-','<font color="red">*双击查看详细信息</font>']
	});
	RightGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(),
		sm: this.sm,
	    margins:'0 1 1 1',
		width:600,
		height:300,
		loadMask: true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar 
	});	
	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		var btnDelDocument = mainToolbar.items.get('btnDelDocument');
		var btnModiDocument = mainToolbar.items.get('btnModiDocument');
		
		if(oCheckboxSModel.getSelections().length==1){
			if(btnModiDocument) btnModiDocument.setDisabled(false);
		}else{
			if(btnModiDocument) btnModiDocument.setDisabled(true);
		}
		
		if(oCheckboxSModel.getSelections().length<=0){
			if(btnDelDocument) btnDelDocument.setDisabled(true);
		}else{
			if(btnDelDocument) btnDelDocument.setDisabled(false);
		}
		
	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		
		var select=rightGrid.getSelectionModel().getSelections()[0];
		if(select == null){
			return ;
		}
		var url=contextPath+"/jteap/system/doclib/doclibShowForm.jsp?doclibId="+select.get("id");
		var result=showModule(url,true,600,500);
		if(result=="true"){
			rightGrid.getStore().reload();
		}
	
	});	
}

Ext.extend(RightGrid, Ext.grid.GridPanel, {
	sm:new Ext.grid.CheckboxSelectionModel(),
	
	//删除选中文档
	deleteSelectedDoclib:function(){
		var store = this.store;
	    var records = rightGrid.selModel.getSelections();
	    var recordsLen = records.length;                     //得到行数组的长度
	    var ids = "";
	    var id = "";
		if(rightGrid.getSelectionModel().getSelected() == null) {
			Ext.Msg.alert("提示","请先选择要删除的行!");
			return ;
		}
		Ext.MessageBox.confirm('系统提示信息', '确定要删除所选的记录吗?', function(buttonobj){
			if(buttonobj == 'yes'){
				//判断扩展字段是否拥有id，没有就直接在前台删除
				var bFlag=false;
				
				for(var i = 0; i < recordsLen; i++){
					if(records[i].get("id") == "" || records[i].get("id") == null){
						rightGrid.store.commitChanges() ;
						rightGrid.store.remove(rightGrid.getSelectionModel().getSelected()) ;
					}else{
						bFlag=true;
						var id = records[i].get("id");
						if(id == null || id == ""){
							//ids += id ;
						}else{
							if(i != 0){
								ids += ","+id;
							}else{
								ids += id ;
							}
						}
					}
				}
				if(bFlag){
					//如果有id，就直接在数据库删除
					Ext.Ajax.request({
						url:link5,
						method:'post',
						params: {ids:ids},
						success:function(ajax){
							var responseText=ajax.responseText;	
					 		var responseObject=Ext.util.JSON.decode(responseText);
					 		if(responseObject.success){
					 			alert("删除成功!");
					 			rightGrid.getStore().reload();
					 		}else{
					 			alert(responseObject.msg);
					 		}
						},
						failure:function(){
							alert("提交失败!");
						}
					});
				}
			}else{
				//Ext.Msg.alert("提示","请先选择要删除的行!");
	    	}
		});
	},
	
	
	/**
	 * 取得默认数据源
	 * 返回数据格式
	 * {success:true,totalCount:20,list:[{id:'123',field1:'111',...,field5'222'},{...},...]} 
	 */
	getDefaultDS:function(url){
	    var ds = new Ext.data.Store({
	        proxy: new Ext.data.ScriptTagProxy({
	            url: url
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount',
	            id: 'id'
	        }, [
	            'id','title', 'creator', 'createdt'
	        ]),
	        remoteSort: true	
	    });
		return ds;
	},
    
	/**
	 * PersonAction 列模型
	 */
	getColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
		    	this.sm,
		        {id:'title',header: "标题", width: 180, sortable: true, dataIndex: 'title'},
		        {id:'creator',header: "创建人", width: 180, sortable: true, dataIndex: 'creator'},
				{id:'createdt',header: "创建日期", width: 180, sortable: true, dataIndex: 'createdt',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd hh:mm:ss"); 
						return dt;         
					}
				}
			]);
		return cm;
	},
	/**
	 * 切换数据源->LogAction!showList
	 */
	changeToListDS:function(url){
		var ds = this.getDefaultDS(url);	
		var cm=this.getColumnModel();
		this.pageToolbar.bind(ds);
		this.reconfigure(ds,cm);
	}

});

