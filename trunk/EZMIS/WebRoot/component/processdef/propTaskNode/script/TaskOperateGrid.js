TaskOperateGrid = function() {
	taskOperateGrid = this ;

	this.flowConfigId = getLabelValue('flowId',rootCell);
	this.nodeName = getLabelValue('name');

	foItems = Ext.decode(getLabelValue('taskOperate')) ;
	var opeDataArray = new Array();
	for(var i=0 ; i< foItems.length ; i++) {
		opeDataArray[i] = new Object() ;
	}

	for(var i=0 ; i< foItems.length ; i++) {
		var obj = foItems[i] ;
		opeDataArray[i].name = obj.name ;
		opeDataArray[i].mark = obj.mark ; 
		opeDataArray[i].show = obj.show ;
		opeDataArray[i].opPerm_Role = obj.opPerm_Role;
		opeDataArray[i].opPerm_Group = obj.opPerm_Group;
	}
	
	toDefaultDS = this.getDataStore()

	var cm = this.getColumnMode();
	
	toDefaultDS.load()
	
	toDefaultDS.on('load',function(){
		for(var i = 0;i < this.getCount();i++){
			for(var j = 0;j < opeDataArray.length;j++){
				if(this.getAt(i).data.name == opeDataArray[j].name){
					this.getAt(i).data.mark = opeDataArray[j].mark;
					this.getAt(i).data.show = opeDataArray[j].show;
					if (!opeDataArray[j].opPerm_Role) {
						this.getAt(i).data.opPerm_Role = '';
					} else {
						this.getAt(i).data.opPerm_Role = opeDataArray[j].opPerm_Role;
					}
					if (!opeDataArray[j].opPerm_Group) {
						this.getAt(i).data.opPerm_Group = '';
					} else {
						this.getAt(i).data.opPerm_Group = opeDataArray[j].opPerm_Group;
					}
				}
			}
		}
	})
	
	TaskOperateGrid.superclass.constructor.call(this,{
		store: toDefaultDS,
		cm : cm ,
//		width:580,
        height:277,
        frame:true,
        clicksToEdit:1
	}) 
	
	this.on("cellclick", function(grid , rowIndex , columnIndex , e ){
	}) ;
	
//	this.on("afteredit",function() {
//		var record = new this.Model(
//			{
//				'name' : '',
//				'mark' : '' ,
//				'show' : ''
//			}
//		);
//		taskOperateGrid.stopEditing();
//		//toDefaultDS.insert(toDefaultDS.data.length, record);
//	})
}				  

Ext.extend(TaskOperateGrid , Ext.grid.EditorGridPanel , {
	data : {
				'totalCount':8,
				'list':Ext.decode(getLabelValue('taskOperate'))
		   },
	Model : Ext.data.Record.create(['name','mark','show','opPerm_Group','opPerm_Role']),
	//新建变量
	createOperate : function() {
		
	} ,
	//包括一些其他的封装方法:如获得CM,获得Store,转换的url之类的,PagingToolbar等
	//获得DataStore
	getDataStore : function() {
		var store = new Ext.data.Store({
			proxy : new Ext.data.ScriptTagProxy({
				url : link4
			}),
			reader : new Ext.data.JsonReader({
				 root: 'list',
	             totalProperty: 'totalCount',
	             id: 'name'
			},['name','mark','show','opPerm_Group','opPerm_Role']),
			baseParams : {'flowConfigId':this.flowConfigId,'nodeName':this.nodeName}
		});
		
//		var obj = Ext.decode(store);
//		for(var i = 0;i < store.getCount();i++){
//			for(var j = 0;j < this.opeDataArray.length;j++){
//				if(store.getAt(i).name == this.opeDataArray[j].name){
//					store.getAt(i).mark = this.opeDataArray[j].mark;
//					store.getAt(i).show = this.opeDataArray[j].show;
//				}
//			}
//		}
//		store = Ext.encode(obj);
		return store ;
	} ,
	//获得列模型
	getColumnMode : function() {
		var cm=new Ext.grid.ColumnModel([
		        {id:'name',header: "操作名称", width: 60, sortable: false, dataIndex: 'name'},
		        {id:'mark',header: "按钮标识", width: 60, sortable: false, dataIndex: 'mark',
		        	editor: new Ext.form.TextField({allowBlank:false})
				},
		        {id:'show',header: "是否显示", width: 60, sortable: false, dataIndex: 'show',
//		        	renderer:function renderShow(value, p, record){
//					        return value?'显示':'不显示';
//					},
		        	editor : new Ext.form.ComboBox({
			        	store : new Ext.data.SimpleStore({
			        		fields : ['value'],
			        		data : [
							    ['显示'],
							    ['不显示']
							]
			        	}),
						valueField :"value", 
						displayField: "value", 
			        	mode:'local' ,
						triggerAction : 'all',
						lazyRender:true,
						listClass: 'x-combo-list-small',
						listeners : {
							"beforeselect" : function(combo,record, index){
								//alert(taskOperateGrid.store);
//								taskOperateGrid.store.data.items[taskOperateGrid.lastRowIndex].data.mark=opeDataArray[index][1]
							},
							"blur":function(combox) {
//								alert(taskOperateGrid.lastRowIndex);
//								if(combox.getValue() == combox.getRawValue()) {
//									return ;
//								}
//								taskOperateGrid.store.data.items[taskOperateGrid.lastRowIndex].data.name=combox.getRawValue() ;
//								taskOperateGrid.store.data.items[taskOperateGrid.lastRowIndex].data.mark="111" ;
//								taskOperateGrid.store.data.items[taskOperateGrid.lastRowIndex].data.show=combox.getValue();
							}
						}
					})
				},{id:'opPerm_Role',header: "角色权限", width: 200, dataIndex: 'opPerm_Role',
		        	editor: new Ext.form.TextField()
				},{id:'opPerm_Group',header: "部门权限", width: 200, dataIndex: 'opPerm_Group',
		        	editor: new Ext.form.TextField()
				}
			]);
		return cm;
	} 
})