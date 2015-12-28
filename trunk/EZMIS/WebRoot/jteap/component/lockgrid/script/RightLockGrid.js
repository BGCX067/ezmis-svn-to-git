

/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link4);
    var grid=this;
	RightGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	columns: this.getColumnModel(),
		stripeRows: true,	
		region:'center'
	});	
}

Ext.extend(RightGrid, Ext.grid.LockingGridPanel, {
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
			'id','fd1','fd2','fd3','fd4','fd5','fd6','fd7','fd8','fd9','fd0'
	        ]),
	        remoteSort: true
	    });
		return ds;
	},
    
	/**
	 * PersonAction 列模型
	 */
	getColumnModel:function(){
	    var cm=[
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
			];
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

