
/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link1 + "?ny="+nowYm);
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'导出Excel',handler:function(){
		exportExcel(grid,true);
		}},'-','<font color="red">*双击查看相关工作规律详细统计报表</font>']
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
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=rightGrid.getSelectionModel().getSelections()[0];
		var gzgl = select.data.gzgl;
		var gzglKey = select.data.gzglKey;
		var obj = {};
		obj.gzgl = gzgl;
		
		if(gzglKey != "合计"){
			var url = contextPath + '/jteap/yx/dqgzgl/statistics/Detail.jsp';
			var result = showIFModule(url,"定期工作列表 【工作规律:"+gzglKey+"】","true",850,600,obj);
		}
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
				"gzglKey","gzgl","setSendNum","handleYNum","handleWNum","wancl"
	        ]),
	        remoteSort: true,
	        //如果为true,每次store在加载时或者一条记录被删除时, 清除所有改动过的记录信息
	        pruneModifiedRecords: true
	    });
		return ds;
	},
    
	/**
	 * DqgzAction 列模型
	 */
	getColumnModel:function(){
		
		var grid = this;
				
	    var cm=new Ext.grid.ColumnModel([
		    	this.sm,
		    	{id:'gzgl',header: "工作规律", width: 200, sortable: true, hidden:true, dataIndex: 'gzgl'},
				{id:'gzglKey',header: "工作规律", width: 200, sortable: true, dataIndex: 'gzglKey'},
				{id:'setSendNum',header: "定期工作发送数", width: 150, sortable: true, dataIndex: 'setSendNum'},
				{id:'handleYNum',header: "定期工作完成数", width: 150, sortable: true, dataIndex: 'handleYNum'},
				{id:'handleWNum',header: "定期工作未完成数", width: 150, sortable: true, dataIndex: 'handleWNum'},
				{id:'wancl',header: "完成率", width: 120, sortable: true, dataIndex: 'wancl'}
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