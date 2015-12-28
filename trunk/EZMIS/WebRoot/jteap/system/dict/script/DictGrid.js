DictGrid=function(){
	//数据字典表格
	dictGrid=this;
    	
    var sm = new Ext.grid.CheckboxSelectionModel();
    var dictDs=this.getDictActionDS(link1);
    this.exportLink = link1
    this.pageToolbar=new Ext.PagingToolbar({
	    store: dictDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'导出Excel',listeners:{click:function(){
				exportExcel(dictGrid,true)
		}}},'-']
	});
		
	DictGrid.superclass.constructor.call(this,{
	 	ds: dictDs,
	    cm: new Ext.grid.ColumnModel([
	    	sm,
	        {id:'key',header: "键", width: 120, sortable: true, dataIndex: 'key'},
			{header: "值", width: 120, sortable: true, dataIndex: 'value'},
			{header: "备注", width: 200, dataIndex: 'remark'}	
		]),
		sm: sm,
		ddGroup:'GridDD',
	 	enableDragDrop:true,
	 	dropAllowed: true, 
		dragAllowed: true,
		width:600,
		height:300,
		loadMask: true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar
	});
	//this.getStore().load({params:{start:0, limit:CONSTANTS_PAGE_DEFAULT_LIMIT}});  
    
	//当有字典被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		//只有单选的时候才能修改，多选的只能删除
		var oBntModiDict=mainToolbar.items.get('btnModiDict');
		var oBtnDelDict=mainToolbar.items.get('btnDelDict');
		
		if(oCheckboxSModel.getSelections().length==1){
			oBntModiDict.setDisabled(false);
			oBtnDelDict.setDisabled(false);
		}else{
			oBntModiDict.setDisabled(true);
			if(oCheckboxSModel.getSelections().length==0){
				oBtnDelDict.setDisabled(true);
			}else{
				oBtnDelDict.setDisabled(false);
			}
		}
	});
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var selectId = grid.getSelectionModel().getSelected().id;
		var detailFormWindow = new DetailFormWindow();
		detailFormWindow.show();
		detailFormWindow.loadData(selectId);
	})
}
Ext.extend(DictGrid, Ext.grid.GridPanel, {
	sm:new Ext.grid.CheckboxSelectionModel(),
	
	/**
	 * 数据源DictAction!showList
	 */
	changeToListDS:function(url){
		this.exportLink = url
		var ds=this.getDictActionDS(url);
		var cm=this.getDictActionColumnModel();
		this.pageToolbar.bind(ds);
		this.reconfigure(ds,cm);
	},
	
	/**
	 * DictAction 列模型
	 */
	getDictActionColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
	    	this.sm,
	        {id:'key',header: "键", width: 120, sortable: true, dataIndex: 'key',
	         renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    	return "<span style='color:blue;font-weight:bold;'>"+value+"</span>";
		    }},
	        {id:'value',header: "值", width: 200, sortable: true, dataIndex: 'value',
	         renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    	return "<span style='color:red;font-weight:bold;'>"+value+"</span>";
		    }},
			{id:'remark',header: "描述", width: 120, sortable: true, dataIndex: 'remark'},
			{id:'sortNo',header: "排序号", width: 120, sortable: true, dataIndex: 'sortNo'}
		]);
		return cm;
	},
	/**
	 * 获取数据字典的数据源
	 */
	getDictActionDS:function(url){
	    var ds = new Ext.data.Store({
	        proxy: new Ext.data.ScriptTagProxy({
	            url: url
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount',
	            id: 'id'
	        }, [
	            'key', 'value','remark','sortNo'
	        ]),
	        remoteSort: true	
	    });
	   	return ds;
	},
	
	/**
	 * 删除选中的字典
	 */
	delSelectedDict:function(){
	var selections = this.getSelections();//获取被选中的行
	var dictids="";
	//var grid = this;
	Ext.each(selections,function(selectedobj){
		dictids+=selectedobj.id+",";//取得他们的id并组装
	});
	if(window.confirm("确认删除字典吗？")){
		Ext.Ajax.request({
			url:link10,
			success:function(ajax){
		 		var responseText=ajax.responseText;	
		 		var responseObject=Ext.util.JSON.decode(responseText);
		 		if(responseObject.success){
		 			alert("删除成功");
		 			dictGrid.getStore().reload();
		 		}else{
		 			alert(responseObject.msg);
		 		}				
			},
		 	failure:function(){
		 		alert("提交失败");
		 	},
		 	method:'POST',
		 	params: {ids:dictids}//Ext.util.JSON.encode(selections.keys)			
		});
	}
}
	
	
});

