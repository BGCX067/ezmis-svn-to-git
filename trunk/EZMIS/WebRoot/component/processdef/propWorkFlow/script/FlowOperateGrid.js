FlowOperateGrid = function() {

	flowOperateGrid = this
	//默认DS
	foDefaultDS = this.getDataStore();
	//工具栏
	this.pageToolbar = new Ext.PagingToolbar({
		store : foDefaultDS,
		pageSize : CONSTANTS_PAGE_DEFAULT_LIMIT ,
		displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据"
		//items : ['分页工具']
	});
	FlowOperateGrid.superclass.constructor.call(this,{
		
		tbar : ['->',{text:'新增操作',handler : this.createOperate},{text:'删除操作',handler : this.deteleOperate}],
		loadMask : true ,
		cm : this.getColumnMode() ,
		store : foDefaultDS,
		autoWidth:true,
		height : 280 , 
		bbar : this.pageToolbar
	})
	foDefaultDS.load();
}


Ext.extend(FlowOperateGrid , Ext.grid.EditorGridPanel , {
	data : {'totalCount':Ext.decode(getLabelValue('flowVariable')).length,'list':Ext.decode(getLabelValue('flowOperate'))},
	//新建变量
	createOperate : function() {
		var Model = Ext.data.Record.create([{name:'operaName',mapping:'name'},{name:'btnSn',mapping:'mark'}])
		var record = new Model(
			{
				'operaName' : 'operaName' ,
				'btnSn' : 'btnSn'
			}
		);
		 flowOperateGrid.stopEditing();
         foDefaultDS.insert(0, record);
         flowOperateGrid.startEditing(0, 0);
	} ,
	deteleOperate : function() {
		if(flowOperateGrid.getSelectionModel().selection == null) {
			alert("请任选一单元格") ;
			return ;
		}
		flowOperateGrid.store.commitChanges() ;
		flowOperateGrid.store.remove(flowOperateGrid.getSelectionModel().selection.record) ;
	} ,
	//包括一些其他的封装方法:如获得CM,获得Store,转换的url之类的,PagingToolbar等
	//获得DataStore
	getDataStore : function() {
		var store = new Ext.data.Store({
			proxy : new Ext.data.MemoryProxy(this.data), 
			reader : new Ext.data.JsonReader({
				 root: 'list',
	             totalProperty: 'totalCount',
	             id: 'id',
	             fields: [
				      {name: 'name'},
				      {name: 'mark'} 
      			 ]
	             
			},[{name:'operaName',mapping:'name'},{name:'btnSn',mapping:'mark'}])
		});
		return store ;
	} ,
	//获得列模型
	getColumnMode : function() {
		var cm=new Ext.grid.ColumnModel([
		        {id:'operaName',header: "操作名称", width: 239, sortable: true, dataIndex: 'operaName',editor:new Ext.form.TextField({allowBlank: false})},
		        {id:'btnSn',header: "按钮标识", width: 230, sortable: true, dataIndex: 'btnSn',editor:new Ext.form.TextField({allowBlank: false})}
			]);
		return cm;
	}
})