
/**
 * 字段列表
 */
BbIOGrid=function(){
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
		}}]
	});
	BbIOGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(),
		sm: this.sm,
	    margins:'2px 2px 2px 2px',
		width:600,
		height:250,
		loadMask: true,
		frame:true,
		region:'north',
		tbar:this.pageToolbar 
	});	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		
		var btnModiCatalog=mainToolbar.items.get("btnModiCatalog");
		var btnDelCatalog=mainToolbar.items.get("btnDelCatalog");
		if(oCheckboxSModel.getSelections().length==1){
			if(btnModiCatalog) btnModiCatalog.setDisabled(false);
			if(btnDelCatalog) btnDelCatalog.setDisabled(false);
			var select=bbIOGrid.getSelectionModel().getSelections()[0];
			if(select!=null){
				var url=link8+"?bbioid="+select.json.id;
		    	bbSjzdGrid.changeToListDS(url);
		    	bbSjzdGrid.getStore().reload();
			}
		}else{
			if(btnModiCatalog) btnModiCatalog.setDisabled(true);
			if(btnDelCatalog) btnDelCatalog.setDisabled(true);
		}
	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		
	});
	
	
	
}
Ext.extend(BbIOGrid, Ext.grid.GridPanel, {
	sm:new Ext.grid.CheckboxSelectionModel(),
	
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
			'id','vname','bbbm','cvname','sqlstr'
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
				{id:'id',header: "id", width: 100, sortable: true, dataIndex: 'id',hidden:true},
				{id:'vname',header: "视图名", width: 100, sortable: true, dataIndex: 'vname'},
				{id:'cvname',header: "视图中文名", width: 100, sortable: true, dataIndex: 'cvname'},
				{id:'sqlstr',header: "SQL串", width: 200, sortable: true, dataIndex: 'sqlstr'}
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
		ds.on("load",function(store,records,options ){
			bbIOGrid.getSelectionModel().selectFirstRow();
	    });
	},
	/**
	 * 删除
	 */
	deleteSelect:function(select){
		var selections = this.getSelections();//获取被选中的行
		var rightGrid=this;
		var ids="";
		Ext.each(selections,function(selectedobj){
			ids+=selectedobj.id+",";//取得他们的id并组装
		});
		if(window.confirm("确认删除选中的条目吗？")){
			Ext.Ajax.request({
				url:link5,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			alert("删除成功");
			 			bbIOGrid.getStore().reload();
			 			var url=link8;
				    	bbSjzdGrid.changeToListDS(url);
				    	bbSjzdGrid.getStore().reload();
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

