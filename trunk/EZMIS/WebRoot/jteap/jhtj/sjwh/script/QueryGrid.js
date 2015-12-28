
/**
 * 字段列表
 */
QueryGrid=function(){
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: new Ext.data.Store(),
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:[]
	});
	QueryGrid.superclass.constructor.call(this,{
	 	ds: new Ext.data.Store(),
	 	cm: this.getColumnModel(),
		//sm: this.sm,
	 	sm:new Ext.grid.RowSelectionModel({singleSelect:true}),
		width:320,
		height:298,
		loadMask: true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar 
	});	
	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		var btnModiCatalog=mainToolbar.items.get("btnModiCatalog");
		if(oCheckboxSModel.getSelections().length==1){
			if(btnModiCatalog) btnModiCatalog.setDisabled(false);
		}else{
			if(btnModiCatalog) btnModiCatalog.setDisabled(true);
		}
	});
}
Ext.extend(QueryGrid, Ext.grid.GridPanel, {
	//sm:new Ext.grid.CheckboxSelectionModel(),
	
	/**
	 * default 列模型
	 */
	getColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
		    	//this.sm,
		        {id:'columncode',header: " ", width: 2, sortable: true}
			]);
		return cm;
	},
	/**
	 * 切换数据源->LogAction!showList
	 */
	updateData:function(cm,ds){
		this.pageToolbar.bind(ds);
		this.reconfigure(ds,cm);
		this.store.reload();
	}

});

