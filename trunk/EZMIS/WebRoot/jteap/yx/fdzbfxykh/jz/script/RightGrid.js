
/**
 * 字段列表
 */
RightGrid=function(){
	
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: 20,
	    store: new Ext.data.Store(),
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'导出Excel',handler:function(){
			exportMyExcel(true);
		}}]
	});
	RightGrid.superclass.constructor.call(this,{
		ds: new Ext.data.Store(),
	 	cm: this.getMyColumnModel(),
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
	 * default 列模型
	 */
	getMyColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
		    	this.sm,
		        {id:'columncode',header: " ", width: 2, sortable: true}
			]);
		return cm;
	},
	/**
	 * 切换数据源->LogAction!showList
	 */
	updateData:function(cm,ds){
		this.pageToolbar.bind(ds);
		this.reconfigure(ds,cm);
		this.store.reload();
	}
	
});