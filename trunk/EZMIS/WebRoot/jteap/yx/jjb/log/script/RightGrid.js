
/**
 * 字段列表
 */
RightGrid = function(){
	
    var defaultDs = this.getDefaultDS(link1);
	
    var grid = this;
    
    this.pageToolbar = new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:[
			'-',
			{text:'导出Excel',handler:function(){
			exportExcel(grid,true);
		}}]
	});
	
	RightGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(),
		sm: this.sm,
	    margins: '2px 2px 2px 2px',
		width: 600,
		height: 300,
		loadMask: true,
		frame: true,
		region: 'center',
		tbar: this.pageToolbar 
	});	
	
}

Ext.extend(RightGrid, Ext.grid.GridPanel, {
	
	sm: new Ext.grid.CheckboxSelectionModel(),
	
	/**
	 * 取得默认数据源
	 * 返回数据格式
	 * {success:true,totalCount:20,list:[{id:'123',field1:'111',...,field5'222'},{...},...]} 
	 */
	getDefaultDS: function(url){
	    var ds = new Ext.data.Store({
	        proxy: new Ext.data.ScriptTagProxy({
	            url: url
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount',
	            id: 'id'
	        }, [
			'id','jjbsj','jiaobanbc','jiaobanzb','jiaobanr','jiebanbc','jiebanzb','jiebanr','doDate','gwlb'
	        ]),
	        remoteSort: true
	    });
		return ds;
	},
    
	/**
	 * JiaoJieBanAction 列模型
	 */
	getColumnModel: function(){
	    var cm = new Ext.grid.ColumnModel([
		    	this.sm,
				{id:'id',header: "id", width: 0, sortable: true,hidden:true, dataIndex: 'id'},
				{id:'jjbsj',header: "班次时间", width: 100, sortable: true,hidden:true, dataIndex: 'jjbsj',
				renderer:function(value,metadata,record,rowIndex,colIndex,store){
					var jjbsj = formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
					return jjbsj;
				}},	
				{id:'doDate',header: "交接班时间", width: 150, sortable: true, dataIndex: 'doDate',
				renderer:function(value,metadata,record,rowIndex,colIndex,store){
					if(value != null){
						return formatDate(new Date(value["time"]),"yyyy-MM-dd HH:mm:ss");
					}
				}},
				{id:'jiaobanbc',header: "交班班次", width: 70, sortable: true, dataIndex: 'jiaobanbc'},
				{id:'jiaobanzb',header: "交班值别", width: 70, sortable: true, dataIndex: 'jiaobanzb'},
				{id:'jiaobanr',header: "交班人", width: 100, sortable: true, dataIndex: 'jiaobanr'},
				{id:'jiebanbc',header: "接班班次", width: 70, sortable: true, dataIndex: 'jiebanbc'},
				{id:'jiebanzb',header: "接班值别", width: 70, sortable: true, dataIndex: 'jiebanzb'},
				{id:'jiebanr',header: "接班人", width: 100, sortable: true, dataIndex: 'jiebanr'},
				{id:'gwlb',header: "岗位类别", width: 120, sortable: true, dataIndex: 'gwlb'}
			]);
		return cm;
	},
	/**
	 * 切换数据源->JiaoJieBanAction!showList
	 */
	changeToListDS: function(url){
		var ds = this.getDefaultDS(url);	
		var cm = this.getColumnModel();
		this.pageToolbar.bind(ds);
		this.reconfigure(ds,cm);
	}

});
