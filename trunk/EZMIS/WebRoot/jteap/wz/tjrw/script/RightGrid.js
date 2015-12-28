 
/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link1);
    defaultDs.load();
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-']
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
		tbar:this.pageToolbar,
		stripeRows: true
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
	            id: 'ID'
	        }, 
	        ["id", "rwmc", "rwlb", "nf", "yf","cjsj.time","zt","bz","startStr","endStr"]),
	        remoteSort: true	
	    });
		return ds;
	},
    
	/**
	 * PersonAction 列模型
	 */
	getColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
	    	  {header: "统计年份", width: 60, sortable: true, dataIndex: 'nf'},
	    	  {header: "统计月份", width: 60, sortable: true, dataIndex: 'yf'},
	    	  {header: "统计名称", width: 130, sortable: true, dataIndex: 'rwmc'},
		      {header: "统计阶段", width: 200, sortable: true, dataIndex: 'bz'},
        	  {header: "创建时间", width: 130, sortable: true, dataIndex: 'cjsj.time',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						var dt = formatDate(new Date(value),"yyyy-MM-dd hh:mm:ss"); 
						return dt;         
				}},
			  {header: "任务开始时间", width: 130, sortable: true, dataIndex: 'startStr',
				renderer:function(value,metadata,record,rowIndex,colIndex,store){
					if(value!=""){
						return value;  
					}else{
						return "正在等待执行......";
					}
					       
				}},
			  {header: "任务结束时间", width: 130, sortable: true, dataIndex: 'endStr',
				renderer:function(value,metadata,record,rowIndex,colIndex,store){
					if(value!=""){
						return value;  
					}else{
						return "正在等待执行......";
					}     
				}},
        	  {header: "统计状态", width: 100, sortable: true, dataIndex: 'zt',
        	  renderer:function(value){	
        	  	if(value=='0'){
        	  		return "<font color='greed'>等待统计......</font>";
        	  	}
        	  	if(value=='1'){
        	  		return "<font color='red'>正在统计......</font>";
        	  	}
        	  	if(value=='2'){
        	  		return "<font color='blue'>完成统计</font>";
        	  	}
        	  	return value;
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

