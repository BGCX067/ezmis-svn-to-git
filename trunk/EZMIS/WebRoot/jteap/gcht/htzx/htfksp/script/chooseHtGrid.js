/**
 * 字段列表
 */
RightGrid=function(){
	
	var url = link18+"?tableName="+tableName+"&htcjsj="+new Date().format("Y");
    var defaultDs=this.getDefaultDS(url);
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据"
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
	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){

	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=grid.getSelectionModel().getSelections()[0];
		var obj = {};
		obj.id = select.data.ID;
		obj.htbh = select.data.HTBH;
		obj.htmc = select.data.HTMC;
		obj.htlx = select.data.HTLX;
		obj.htje = select.data.HTJE;
		window.returnValue = obj;
		window.close();
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
				"ID","HTBH","HTMC","HTLX","HTJE","HTCJSJ"
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
		var isHiddenJe = false;
		if("tb_ht_rlht" == tableName){
			isHiddenJe = true;
		}
		
	    var cm=new Ext.grid.ColumnModel([
		    	this.sm,
				{id:'ID',header: "id", width: 100, sortable: true, hidden:true, dataIndex: 'ID'},
				{id:'HTBH',header: "合同编号", width: 100, sortable: true, dataIndex: 'HTBH'},
				{id:'HTMC',header: "合同名称", width: 150, sortable: true, dataIndex: 'HTMC'},
				{id:'HTLX',header: "合同类型", width: 100, sortable: true, dataIndex: 'HTLX'},
				{id:'HTJE',header: "合同金额(元)", width: 100, sortable: true, hidden:isHiddenJe, dataIndex: 'HTJE'},
				{id:'HTCJSJ',header: "创建时间", width: 130, sortable: true, dataIndex: 'HTCJSJ',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value != null){
							return formatDate(new Date(value),"yyyy-MM-dd HH:mm:ss");
						}
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
