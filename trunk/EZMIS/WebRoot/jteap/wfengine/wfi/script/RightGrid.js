
/**
 * 字段列表
 */
RightGrid=function(){
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
		var btnResetFlow=mainToolbar.items.get('btnResetFlow');
		var btnWFTrace=mainToolbar.items.get('btnWFTrace');
		var btnWFLog=mainToolbar.items.get('btnWFLog');
		var btnWFOpenForm=mainToolbar.items.get('btnWFOpenForm');
		var btnDel=mainToolbar.items.get('btnDel');
		
		if(btnResetFlow) btnResetFlow.setDisabled(false);
		if(btnWFTrace) btnWFTrace.setDisabled(false);
		if(btnWFLog) btnWFLog.setDisabled(false);
		if(btnWFOpenForm) btnWFOpenForm.setDisabled(false);
		if(btnDel) btnDel.setDisabled(false);
	});
	
	
}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
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
	            id: 'ID_'
	        }, [
	            'ID_', 'FLOW_TOPIC', 'VERSION_','START_','END_',
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
		        {id:'ID_',header: "流程编号", width: 80, sortable: true, dataIndex: 'ID_'},
		        {id:'FLOW_TOPIC',header: "流程名称", width: 220, sortable: true, dataIndex: 'FLOW_TOPIC'},
		        //{id:'FLOW_NAME',header: "流程名称", width: 240, sortable: true, dataIndex: 'FLOW_NAME'},
				{id:'START_',header: "开始时间", width: 200, sortable: true, dataIndex: 'START_',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						var dt = formatDate(new Date(value),"yyyy-MM-dd hh:mm:ss"); 
						return dt;         
					}},
				{id:'END_',header: "结束时间", width: 200, sortable: true, dataIndex: 'END_',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value!=null){
							var dt = formatDate(new Date(value),"yyyy-MM-dd hh:mm:ss"); 
							return dt;
						}
					}}
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

