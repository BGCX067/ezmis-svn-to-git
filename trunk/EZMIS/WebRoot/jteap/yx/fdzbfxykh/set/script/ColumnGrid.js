
/**
 * 字段列表
 */
ColumnGrid=function(){
    var defaultDs=this.getColumnActionDS(link8 + "?directiveId=null");
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'导出Excel',handler:function(){
		exportExcel(grid,true);
		}}]
	});
	ColumnGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(),
		sm: this.sm,
	    margins:'0 1 1 1',
	    enableDragDrop:true,
	    ddGroup: 'firstGridDDGroup',
	 	dropAllowed: true, 
		dragAllowed: true,
		width:600,
		height:300,
		loadMask: true,
		frame:true,
		region:'center',
		tbar: this.pageToolbar
	});	
	
	//当有用户被选择的时候,显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		//只有单选的时候才能修改,多选的只能删除
		var btnModifyColumn=mainToolbar.items.get('btnModiColumn');
		var btnDelColumn=mainToolbar.items.get('btnDelColumn');
		if(oCheckboxSModel.getSelections().length==1){
			if(btnModifyColumn) btnModifyColumn.setDisabled(false);
		}else{
			if(btnModifyColumn) btnModifyColumn.setDisabled(true);
		}
		
		if(oCheckboxSModel.getSelections().length<=0){
			if(btnDelColumn) btnDelColumn.setDisabled(true);
		}else{
			if(btnDelColumn) btnDelColumn.setDisabled(false);
		}
		
	});
}
Ext.extend(ColumnGrid, Ext.grid.GridPanel, {
	sm:new Ext.grid.CheckboxSelectionModel({sortable : true}),
	/**
	 * 取得ColumnAction数据源
	 */
	getColumnActionDS:function(url){
	    var ds = new Ext.data.Store({
	        proxy: new Ext.data.ScriptTagProxy({
	            url: url
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount',
	            id: 'id'
	        }, [
	            "id","directiveId","directiveCode","directiveName","sumOrAvg","dataTable","sisCedianbianma","remark","sortno"
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
		    	{id:'id',header: "ID", width: 100, sortable: false, hidden:true, dataIndex: 'id'},
		    	{id:'directiveId',header: "小指标表定义ID", width: 100, sortable: false, hidden:true, dataIndex: 'directiveId'},
		        {id:'directiveCode',header: "指标英文名称", width: 150, sortable: false, dataIndex: 'directiveCode'},
		        {id:'directiveName',header: "指标中文名称", width: 150, sortable: false, dataIndex: 'directiveName'},
		        {id:'sumOrAvg',header: "指标类型", width: 80, sortable: false, dataIndex: 'sumOrAvg'},
				{id:'dataTable',header: "取数编码", width: 80, sortable: false,resizable : false, dataIndex: 'dataTable'},
				/*{id:'sisCedianbianma',header: "取数规则", width: 200, sortable: false,resizable : false, dataIndex: 'sisCedianbianma',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    		return "<span ext:qtip='"+value+"'>"+Ext.util.Format.htmlEncode(value)+"</span>"
			    	}
			    },*/
				{id:'remark',header: "备注", width: 200, sortable: false,resizable : false, dataIndex: 'remark',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    		return "<span ext:qtip='"+value+"'>"+Ext.util.Format.htmlEncode(value)+"</span>"
			    	}
				},
				{id:'sortno',header: "排序号", width: 80, sortable: false,resizable : false, dataIndex: 'sortno'}
			]);
		return cm;
	},
	/**
	 * 切换数据源->LogAction!showList
	 */
	changeToListDS:function(url){
		var ds = this.getColumnActionDS(url);	
		var cm=this.getColumnModel();
		this.pageToolbar.bind(ds);
		this.reconfigure(ds,cm);
	},
	/**
	 * 删除
	 */
	deleteSelect:function(select){
		var rightGrid = this;
		var ids = "";
		Ext.each(select,function(selectedobj){
			ids+=selectedobj.id+",";//取得他们的id并组装
		});
		if(window.confirm("确认删除选中的条目吗？")){
			Ext.Ajax.request({
				url:link10,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			alert("删除成功");
			 			rightGrid.getStore().reload();
			 		}else{
			 			alert(responseObject.msg);
			 		}				
				},
			 	failure:function(){
			 		alert("提交失败");
			 	},
			 	method:'POST',
			 	params: {ids:ids}//Ext.util.JSON.encode(selections.keys)			
			});
		}
	}
});
