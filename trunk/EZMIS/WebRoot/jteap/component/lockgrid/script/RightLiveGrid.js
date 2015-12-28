
RightGrid=function(){
	myView = new Ext.ux.grid.livegrid.GridView({
		//当前展现的数据靠近100条记录的时候重新加载新的一页
        nearLimit : 100,
        loadMask  : {
            msg :  '正在加载数据，请稍等...'
        }
    });
	var grid=this;
    var defaultDs=this.getDefaultDS(link7);
    this.pageToolbar=new Ext.ux.grid.livegrid.Toolbar({
    	view:myView,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-']
	});
	RightGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(),
		selModel : new Ext.ux.grid.livegrid.RowSelectionModel(),
		view     : myView,
	    margins:'2px 2px 2px 2px',
		width:600,
		height:300,
		loadMask: true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar 
	});	
}
Ext.extend(RightGrid, Ext.ux.grid.livegrid.GridPanel, {
	/**
	 * 取得默认数据源
	 * 返回数据格式
	 * {success:true,totalCount:20,list:[{id:'123',field1:'111',...,field5'222'},{...},...]} 
	 */
	getDefaultDS:function(url){
	    var ds = new Ext.ux.grid.livegrid.Store({
	        autoLoad : false,
	        url:url,
	        bufferSize : 250,
	        reader: new Ext.ux.grid.livegrid.JsonReader({
	            root: 'data',
	            versionProperty : 'version',
	            totalProperty: 'total_count',
	            id: 'id'
	        }, [
			'id','fd1','fd2','fd3','fd4','fd5','fd6','fd7','fd8','fd9','fd0'
	        ]),
	        sortInfo   : {field: 'fd1', direction: 'ASC'}
	    });
		return ds;
	},
    
	/**
	 * PersonAction 列模型
	 */
	getColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
	    		new Ext.grid.RowNumberer({header : '#' }),
				{id:'id',header: "id", width: 100, sortable: true, dataIndex: 'id'},
				{id:'fd1',header: "fd1", width: 100, sortable: true, dataIndex: 'fd1'},
				{id:'fd2',header: "fd2", width: 100, sortable: true, dataIndex: 'fd2'},
				{id:'fd3',header: "fd3", width: 100, sortable: true, dataIndex: 'fd3'},
				{id:'fd4',header: "fd4", width: 100, sortable: true, dataIndex: 'fd4'},
				{id:'fd5',header: "fd5", width: 100, sortable: true, dataIndex: 'fd5'},
				{id:'fd6',header: "fd6", width: 100, sortable: true, dataIndex: 'fd6'},
				{id:'fd7',header: "fd7", width: 100, sortable: true, dataIndex: 'fd7'},
				{id:'fd8',header: "fd8", width: 100, sortable: true, dataIndex: 'fd8'},
				{id:'fd9',header: "fd9", width: 100, sortable: true, dataIndex: 'fd9'},
				{id:'fd0',header: "fd0", width: 100, sortable: true, dataIndex: 'fd0'}
			]);
		return cm;
	}


});

