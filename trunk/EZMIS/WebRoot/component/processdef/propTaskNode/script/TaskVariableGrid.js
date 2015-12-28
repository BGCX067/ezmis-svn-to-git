
var clearFlag = function(array) {
	for(var i=0 ; i<array.length ; i++) {
		array[i].flag = "false" ;
	}
	return array ;
} ;

/**
 * 环节变量表格
 */
TaskVariableGrid = function() {
	taskVariableGrid = this ;
	
	foItems = Ext.decode(getLabelValue('flowVariable',rootCell)) ;
	varDataArray = new Array() ;
	for(var i=0 ; i< foItems.length ; i++) {
		varDataArray[i] = new Array() ;
	}

	for(var i=0 ; i< foItems.length ; i++) {
		var obj = foItems[i] ;
		varDataArray[i][0] = obj.name ;
		varDataArray[i][1] = obj.name_cn ;
		varDataArray[i][2] = obj.value ;
		varDataArray[i][3] = obj.kind ;
	}
	
	//默认DS
	tvDefaultDS = this.getDataStore();
	TaskVariableGrid.superclass.constructor.call(this,{
		tbar : ['->',
//			{text:'新增变量',handler : this.createVariable},
			{text:'删除变量',handler : this.deleteVariable}],
		loadMask : true ,
		cm : this.getColumnMode() ,
		store : tvDefaultDS,
		//width : 510, 
		margins:'2 2 2 2',
		trackMouseOver : true,//鼠标移上去时高亮显示
		frame : true,
//		stripeRows : true,	//隔行高亮显示
		autoWidth : true,
		region : 'center',
		height : 270
	})
	tvDefaultDS.load();
	
	//记住最后一次访问的行索引
	this.on("cellclick", function(grid , rowIndex , columnIndex , e ){
		if(columnIndex == 0 || columnIndex == 1) {
			grid.lastRowIndex = rowIndex ;
		}
	}) ;
	
	//做变量名是否相同的提示
	this.on("beforeedit",function(obj) {
		
		var name = obj.record.data.variableName ;
		var items = taskVariableGrid.store.data.items ;
		for(var i=0 ; i<items.length ; i++) {
			if(obj.record.id == items[i].id) {
				continue ;
			}
			if(obj.record.data.variableName == items[i].data.variableName) {
				taskVariableGrid.store.remove(obj.record);
				return ; 
			}
		}
	})
	
	//做变量名是否相同的提示
	this.on("afteredit",function() {
		/*var obj = tvDefaultDS.data.items[tvDefaultDS.data.items.length-1]
		if(obj.data.variableName=="" && obj.data.cnName==""){
			return ;		
		}
		var record = new this.Model(
			{
				'variableName' : '',
				'cnName' : '',
				'defaultValue' : '',
				'type' : '',
				'isNeed' : ''
			}
		);
		taskOperateGrid.stopEditing();
		tvDefaultDS.insert(tvDefaultDS.data.length, record);
		this.view.refresh() ;*/
	})
	
	
}

Ext.extend(TaskVariableGrid,Ext.grid.EditorGridPanel,{
	sm:new Ext.grid.RowSelectionModel(),
	data:{
			'totalCount':Ext.decode(getLabelValue('taskVariable')).length,
			'list':clearFlag(Ext.decode(getLabelValue('taskVariable')))
		 },
	Model : Ext.data.Record.create([{name:'variableName',mapping:'name'},{name:'cnName',mapping:'name_cn'},{name:'defaultValue',mapping:'value'},{name:'type',mapping:'kind'},'isNeed','flag']),
	//新建变量
	createVariable : function(data) {
		var gridData = taskVariableGrid.getStore().data.items;
		for(var i = 0;i < gridData.length;i++){
			if(gridData[i].data.variableName == data.variableName)
				return;
		}
//		taskVariableGrid.lastRowIndex = 0 ;
//		var Model = Ext.data.Record.create(['variableName','cnName','defaultValue','type','isEdit'])
		var record = new taskVariableGrid.Model(
//			{
//				'variableName' : '',
//				'cnName' : '' ,	
//				'defaultValue' : '' ,
//				'type' : '流程变量' ,
//				'isNeed' : '可编辑' ,
//				'flag' : 'true'
//			}
			data
		);
		 taskVariableGrid.stopEditing();
         tvDefaultDS.insert(0, record);
//         taskVariableGrid.startEditing(0, 0);
	},
	//删除变量
	deleteVariable : function() {
		if(!confirm("是否确定移除选择的变量")){
			return;
		}
		var selection = taskVariableGrid.sm.getSelections();
		if(selection == null) {
			alert("请任选一单元格") ;
			return ;
		}
		taskVariableGrid.store.commitChanges() ;
		
		for(var i=0;i<selection.length;i++){
			var record = taskVariableGrid.store.getById(selection[i].id);
			taskVariableGrid.store.remove(record);
		}
		//环节变量列表重新加载数据
		var nodeVar = Ext.getCmp('nodeVar');
		nodeVar.store = nodeVar.view.store=new Ext.data.SimpleStore({    
            data:getVariableData(),    
            fields:["name", "name_cn","value","kind","isNeed","displayfield"]//['sysVarKey']    
        },['displayfield','name']);
        nodeVar.view.store.sort('displayfield');
		nodeVar.view.refresh();
	},
	//包括一些其他的封装方法:如获得CM,获得Store,转换的url之类的,PagingToolbar等
	//获得DataStore
	getDataStore : function() {
		var store = new Ext.data.Store({
			proxy : new Ext.data.MemoryProxy(this.data), 
			reader : new Ext.data.JsonReader({
				 root: 'list',
	             totalProperty: 'totalCount',
	             id: 'id',
	             fields : [
	             	{name:'name'},
	             	{name:'name_cn'},
	             	{name:'value'},
	             	{name:'环节变量'},
	             	{name:'isNeed'}
	             ]
			},this.Model)
		});
		return store ;
	} ,
	//获得列模型
	getColumnMode : function() {
		var cm=new Ext.grid.ColumnModel([
		        {id:'variableName',header: "变量名称", width: 128, sortable: true, dataIndex: 'variableName'
//		        ,editor : new Ext.form.ComboBox({
//		        	store : new Ext.data.SimpleStore({
//		        		fields : ['name'],
//		        		data : varDataArray
//		        	}),
//					valueField :"name", 
//					editable :false,
//					displayField: "name", 
//					listWidth:200,
//		        	mode:'local' ,
//					triggerAction : 'all',
//					lazyRender:true,
//					listClass: 'x-combo-list-small',
//					listeners : {
//						"beforeselect" : function(combo,record, index){
//											taskVariableGrid.store.data.items[taskVariableGrid.lastRowIndex].data.cnName = varDataArray[index][1]
//											taskVariableGrid.store.data.items[taskVariableGrid.lastRowIndex].data.defaultValue = varDataArray[index][2]
//											taskVariableGrid.store.data.items[taskVariableGrid.lastRowIndex].data.type = varDataArray[index][3]
//											taskVariableGrid.store.data.items[taskVariableGrid.lastRowIndex].data.isNeed = '可编辑' ;
//											taskVariableGrid.store.data.items[taskVariableGrid.lastRowIndex].data.flag = 'false' ;
//						},
//						"blur":function(combox) {
//							
//							//设置每条数据是否为新建，如果为新建 flag为true
//							if(varDataArray != null) {
//								for(var i=0 ; i<varDataArray.length ; i++) {
//									if(combox.getRawValue() == varDataArray[i][0]) {
//										taskVariableGrid.store.data.items[taskVariableGrid.lastRowIndex].data.flag = 'false' ;
//										break ; 
//									}
//									taskVariableGrid.store.data.items[taskVariableGrid.lastRowIndex].data.flag = 'true' ;
//								}
//							}
//							
//							if(combox.getValue() == combox.getRawValue()) {
//								return ;
//							}
//							if(taskVariableGrid.lastRowIndex == null) {
//								taskVariableGrid.store.data.items[0].data.variableName=combox.getRawValue() ;
//							} else {
//								taskVariableGrid.store.data.items[taskVariableGrid.lastRowIndex].data.variableName=combox.getRawValue() ;
//								taskVariableGrid.store.data.items[taskVariableGrid.lastRowIndex].data.flag = 'true' ;
//							}
//							taskVariableGrid.view.refresh() ;
//						}
//					}
//				})
				},
		        {id:'cnName',header: "中文名称", width : 140 , sortable: true, dataIndex: 'cnName'
//		        ,editor : new Ext.form.ComboBox({
//			        	store : new Ext.data.SimpleStore({
//			        		fields : ['name','name_cn'],
//			        		data : varDataArray
//			        	}),
//						valueField :"name_cn", 
//						displayField: "name_cn", 
//						listWidth:200,
//			        	mode:'local' ,
//						triggerAction : 'all',
//						lazyRender:true,
//						listClass: 'x-combo-list-small',
//						listeners : {
//							"beforeselect" : function(combo,record, index){
//								taskVariableGrid.store.data.items[taskVariableGrid.lastRowIndex].data.variableName = varDataArray[index][0]
//								taskVariableGrid.store.data.items[taskVariableGrid.lastRowIndex].data.defaultValue = varDataArray[index][2]
//								taskVariableGrid.store.data.items[taskVariableGrid.lastRowIndex].data.type = varDataArray[index][3]
//								taskVariableGrid.store.data.items[taskVariableGrid.lastRowIndex].data.isNeed = '可编辑' ;
//								taskVariableGrid.store.data.items[taskVariableGrid.lastRowIndex].data.flag = 'false' ;
//							},
//							"blur":function(combox) {
//								//设置每条数据是否为新建，如果为新建 flag为true
//								if(varDataArray != null) {
//									for(var i=0 ; i<varDataArray.length ; i++) {
//										if(combox.getRawValue() == varDataArray[i][0]) {
//											taskVariableGrid.store.data.items[taskVariableGrid.lastRowIndex].data.flag = 'false' ;
//											break ; 
//										}
//										taskVariableGrid.store.data.items[taskVariableGrid.lastRowIndex].data.flag = 'true' ;
//									}
//								}
//								if(combox.getValue() == combox.getRawValue()) {
//									return ;
//								}
//								if(taskVariableGrid.lastRowIndex == null) {
//									taskVariableGrid.store.data.items[0].data.variableName=combox.getRawValue() ;
//								} else {
//									taskVariableGrid.store.data.items[taskVariableGrid.lastRowIndex].data.variableName=combox.getRawValue() ;
//									taskVariableGrid.store.data.items[taskVariableGrid.lastRowIndex].data.flag = 'true' ;
//								}
//								taskVariableGrid.view.refresh() ;
//							}
//						}
//					})
				},
				{id:'defaultValue',header: "默认值", width: 100, sortable: true, dataIndex: 'defaultValue'
					,editor:new Ext.form.TextField()
				},
				{id:'type',header: "变量类别", width: 75, sortable: true, dataIndex: 'type'
//				,editor : new Ext.form.ComboBox({
//		        	store : new Ext.data.SimpleStore({
//		        		fields : ['retrunValue','displayText'],
//		        		data : [['表单变量','表单变量'],['流程变量','流程变量'],['环节变量','环节变量']]
//		        	}),
//					valueField :"retrunValue", 
//					displayField: "displayText", 
//		        	mode:'local' ,
//					triggerAction : 'all',
//					lazyRender:true,
//					listClass: 'x-combo-list-small'
//				})
				},
				{id:'isNeed',header: "可编辑", width: 75, sortable: true, dataIndex: 'isNeed'
				,editor : new Ext.form.ComboBox({
		        	store : new Ext.data.SimpleStore({
		        		fields : ['retrunValue','displayText'],
		        		data : [['可编辑','可编辑'],['不可编辑','不可编辑']]
		        	}),
					valueField :"retrunValue", 
					displayField: "displayText", 
		        	mode:'local' ,
					triggerAction : 'all',
					lazyRender:true,
					listClass: 'x-combo-list-small'
				})
				}
			]);
		return cm;
	}
	
})