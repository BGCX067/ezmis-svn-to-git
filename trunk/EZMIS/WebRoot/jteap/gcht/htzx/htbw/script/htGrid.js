var tableName = window.dialogArguments.tableName;

/**
 * 字段列表
 */
RightGrid = function(){
	var url = link6+"?tableName="+tableName;
    var defaultDs = this.getDefaultDS(url);
    var grid = this;
    
    this.pageToolbar = new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:[
			'-','<font color=red>双击选择</font>'
		]
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
	
	this.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
		var select = rightGrid.getSelectionModel().getSelections()[0];
		var result = {};
		result.htid = select.get("HTID");
		result.htmc = select.get("HTMC");
		result.htbh = select.get("HTBH");
		window.returnValue = result;
		window.close();
	});
	
}

Ext.extend(RightGrid, Ext.grid.GridPanel, {
	
	sm: new Ext.grid.CheckboxSelectionModel(),
	
	/**
	 * 取得默认数据源
	 * 返回数据格式
	 */
	getDefaultDS: function(url){
	    var ds = new Ext.data.Store({
	        proxy: new Ext.data.ScriptTagProxy({
	            url: url
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount',
	            id: 'HTID'
	        }, [
				'HTID','HTMC','HTBH',"HTLX","HTCJSJ","STATUS"
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
				{id:'HTID',header: "HTID", width: 0, sortable: true,hidden:true, dataIndex: 'HTID'},
				{id:'HTMC',header: "合同名称", width: 120, sortable: true, dataIndex: 'HTMC'},
				{id:'HTBH',header: "合同编号", width: 100, sortable: true, dataIndex: 'HTBH'},
				{id:'HTLX',header: "合同类型", width: 60, sortable: true, dataIndex: 'HTLX'},
				{id:'STATUS',header: "审批状态", width: 80, sortable: true,hidden:true, dataIndex: 'STATUS'},
				{id:'HTCJSJ',header: "创建时间", width: 100, sortable: true, dataIndex: 'HTCJSJ'}
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

//用户列表
var rightGrid = new RightGrid();
rightGrid.getStore().load();

var searchAllFs = "合同名称#HTMC#textField,合同编号#HTBH#textField,开始时间#beginYmd#dateField,结束时间#endYmd#dateField".split(",");
var searchDefaultFs = "合同名称#HTMC#textField,合同编号#HTBH#textField,开始时间#beginYmd#dateField,结束时间#endYmd#dateField".split(",");
//var searchAllFs = "合同名称#HTMC#textField,合同编号#HTBH#textField".split(",");
//var searchDefaultFs = "合同名称#HTMC#textField,合同编号#HTBH#textField".split(",");
var searchPanel = new SearchPanel({
	searchDefaultFs : searchDefaultFs,
	searchAllFs : searchAllFs
});

//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'1 0 0 -1',
	items:[rightGrid,searchPanel]
}
