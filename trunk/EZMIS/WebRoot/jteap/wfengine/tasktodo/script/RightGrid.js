
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
		var btnWFTrace=mainToolbar.items.get('btnWFTrace');
		var btnWFLog=mainToolbar.items.get('btnWFLog');
		var btnWFOpenForm=mainToolbar.items.get('btnWFOpenForm');
		
		if(btnWFTrace) btnWFTrace.setDisabled(false);
		if(btnWFLog) btnWFLog.setDisabled(false);
		if(btnWFOpenForm) btnWFOpenForm.setDisabled(false);
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
	            id: 'id'
	        }, [
	            'flowname','curNodePerson','postPerson','postTime','curTaskName','flowTopic','flowInstance','token', 'curSignIn'
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
		    	{header: "主题", width: 220, sortable: true, dataIndex: 'flowTopic'},
		        {header: "流程名称", width: 150, sortable: true, dataIndex: 'flowname'},
		        {header: "当前环节名称", width: 100, sortable: true, dataIndex: 'curTaskName'},
		        {header: "发送人", width: 100, sortable: true, dataIndex: 'postPerson'},
		        {header: "发送时间", width: 120, sortable: true, dataIndex: 'postTime',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						var dt = formatDate(new Date(value.time),"yyyy-MM-dd hh:mm:ss"); 
						return dt; 
					}
				},
				{header: "TOKEN", width: 100, sortable: true, dataIndex: 'token'},
				{header: "当前签收人", width: 100, sortable: true, dataIndex: 'curSignIn',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if (value !=null) {
							return value.userName;
						}
						return "";
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

