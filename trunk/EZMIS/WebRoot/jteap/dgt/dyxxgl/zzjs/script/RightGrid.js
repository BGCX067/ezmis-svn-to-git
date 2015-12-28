
/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link1);
    var grid=this;
     defaultDs.load();
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
	    margins:'2px 2px 2px 2px',
		width:600,
		height:300,
		loadMask: true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar 
	});	
	
	
/**	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=rightGrid.getSelectionModel().getSelections()[0];
		var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&docid="+select.json.id+"&st=02";
		showIFModule(url,"自定义表单","true",800,600,{},null,null,null,false,"auto");
	});
*/	
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
	        }, 
	        ['id','month','result','content','tudi','shifu']),
	        remoteSort: true	
	    });
		return ds;
	},
    
	/**
	 * 列模型
	 */
	getColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
		    	this.sm,
		      {id:'month',header: "月份", width: 100, sortable: true, dataIndex: 'month'},
		      {id:'shifu',header: "师傅", width: 200, sortable: true, dataIndex: 'shifu'},
		      {id:'tudi',header: "徒弟", width: 200, sortable: true, dataIndex: 'tudi'},
		      {id:'result',header: "完成情况", width: 200, sortable: true, dataIndex: 'result'},
		      {id:'content',header: "培训内容", width: 200, sortable: true, dataIndex: 'content'}
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

}


);

