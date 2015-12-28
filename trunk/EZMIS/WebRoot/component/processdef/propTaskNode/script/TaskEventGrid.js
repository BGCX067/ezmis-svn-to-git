TaskEventGrid = function() {

	var store = this.getDataStore()

	var cm = this.getColumnMode();
	
	store.load()
	
	TaskEventGrid.superclass.constructor.call(this,{
		store: store,
		cm : cm ,
//		width:475,
        height:277,
        frame:true
        //clicksToEdit:1
	}) 
	
}				  

Ext.extend(TaskEventGrid , Ext.grid.EditorGridPanel , {
	data : {
				'totalCount':Ext.decode(getLabelValue('event')).length,
				'list':Ext.decode(getLabelValue('event'))
		   },
	Model:[{name:'eventName',mapping:'eventName'},'proceClass','proceScript'],
	//新建变量
	createOperate : function() {
		alert("新建变量")
	} ,
	//包括一些其他的封装方法:如获得CM,获得Store,转换的url之类的,PagingToolbar等
	//获得DataStore
	getDataStore : function() {
		var store = new Ext.data.Store({
			proxy : new Ext.data.MemoryProxy(this.data), 
			reader : new Ext.data.JsonReader({
				 root: 'list',
	             totalProperty: 'totalCount',
	             id: 'id'
			},this.Model)
		});
		return store ;
	} ,
	//获得列模型
	getColumnMode : function() {
		var cm=new Ext.grid.ColumnModel([
		        {id:'eventName',header: "事件名称", width: 100, sortable: true, dataIndex: 'eventName'},
		        {id:'proceClass',header: "处理类", width: 179, sortable: true, dataIndex: 'proceClass',editor: new Ext.form.TextField({
           })},
		        {id:'proceScript',header: "处理脚本", width: 180, sortable: true, dataIndex: 'proceScript',editor: new Ext.form.TextField({
           })}
			]);
		return cm;
	} 
})