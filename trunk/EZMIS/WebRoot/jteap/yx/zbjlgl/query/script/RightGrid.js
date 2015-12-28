
/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link1+"?jllb=query");
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
	    margins:'2px 2px 2px 2px',
		width:600,
		height:300,
		loadMask: true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar 
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
			'id','jzh','gwlb','jllb','zbsj','zbbc','zbzb','jlsj','jlnr','jlr','tzgw','zy','time'
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
				{id:'id',header: "id", width: 100, sortable: true, hidden:true, dataIndex: 'id'},
				{id:'jllb',header: "记录类别", width: 60, sortable: true, dataIndex: 'jllb'},
				{id:'gwlb',header: "岗位类别", width: 120, sortable: true, dataIndex: 'gwlb'},
				{id:'jlnr',header: "记录内容", width: 400, sortable: true, dataIndex: 'jlnr',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    		return "<span ext:qtip='"+value+"'>"+Ext.util.Format.htmlEncode(value)+"</span>"
			    	}
			    },
				{id:'jlsj',header: "记录时间", width: 120, sortable: true, dataIndex: 'jlsj',
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value != null && value["time"] != null){
				    		var dt = new Date(value["time"]).format("Y-m-d H:i");
							return dt;         
						}else{
							return "";
						}       
					}},
				{id:'jlr',header: "记录人", width: 80, sortable: true, dataIndex: 'jlr'},
				{id:'tzgw',header: "通知岗位", width: 150, sortable: true, dataIndex: 'tzgw',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    		return "<span ext:qtip='"+value+"'>"+Ext.util.Format.htmlEncode(value)+"</span>"
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
