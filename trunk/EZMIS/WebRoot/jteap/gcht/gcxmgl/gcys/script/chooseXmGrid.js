/**
 * 字段列表
 */
RightGrid=function(){
	
	var url = link18+"?htcjsj="+new Date().format("Y");
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
		obj.gcxmbh = select.data.GCXMBH;
		obj.gcxmmc = select.data.GCXMMC;
		obj.gccbdw = select.data.GCCBDW;
		obj.gcnr = select.data.GCNR;
		obj.jhgq = select.data.JHGQ;
		obj.xmyj = select.data.XMYJ;
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
				"ID","GCXMBH","GCXMMC","XMYJ","GCCBDW","JHGQ","GCNR","CJSJ"
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
				{id:'ID',header: "id", width: 100, sortable: true, hidden:true, dataIndex: 'ID'},
				{id:'GCXMBH',header: "项目编号", width: 100, sortable: true, dataIndex: 'GCXMBH'},
				{id:'GCXMMC',header: "项目名称", width: 150, sortable: true, dataIndex: 'GCXMMC'},
				{id:'GCCBDW',header: "承包单位", width: 150, sortable: true, dataIndex: 'GCCBDW'},
				{id:'JHGQ',header: "计划工期", width: 130, sortable: true, dataIndex: 'JHGQ'}]);
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
