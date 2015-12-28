
/**
 * 字段列表
 */
WFLogGrid=function(){
    var defaultDs=this.getDefaultDS(link1);
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
	WFLogGrid.superclass.constructor.call(this,{
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
	defaultDs.setDefaultSort('porcessDate','ASC');
	// 请求数据
	defaultDs.load();
	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		/*
		var btnWFRestart=mainToolbar.items.get('btnWFRestart');
		var btnWFTrace=mainToolbar.items.get('btnWFTrace');
		var btnWFLog=mainToolbar.items.get('btnWFLog');
		var btnOpenForm=mainToolbar.items.get('btnOpenForm');
		
		if(btnWFRestart) btnWFRestart.setDisabled(false);
		if(btnWFTrace) btnWFTrace.setDisabled(false);
		if(btnWFLog) btnWFLog.setDisabled(false);
		if(btnOpenForm) btnOpenForm.setDisabled(false);
		*/
	});
	
	
}
Ext.extend(WFLogGrid, Ext.grid.GridPanel, {
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
	            'id', 'porcessDate', 'taskName','taskActor','taskResult','nextTaksName','nextTaksActor','remark'
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
		        {id:'porcessDate',header: "时间", width: 130, sortable: true, dataIndex: 'porcessDate',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						var dt = formatDate(new Date(value.time),"yyyy-MM-dd hh:mm:ss"); 
						return dt;         
					}},
		        {id:'taskName',header: "动作", width: 130, sortable: true, dataIndex: 'taskName'},
		        {id:'taskActor',header: "处理人", width: 80, sortable: true, dataIndex: 'taskActor'},
				{id:'taskResult',header: "处理结果", width: 200, sortable: true, dataIndex: 'taskResult'},
				{id:'nextTaksActor',header: "接收人", width: 200, sortable: true, dataIndex: 'nextTaksActor',
					renderer : function(value, metadata, record, rowIndex, columnIndex, store) {
					    metadata.attr = 'ext:qtip="' + value + '"';    
					    return value;    
					}
				},
				{id:'remark',header: "回退原因", width: 150, sortable: true, dataIndex: 'remark'}
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

