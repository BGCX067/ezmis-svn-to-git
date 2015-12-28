//
/**
 * 自定义表单中的数据类型
 * var dataType=[	['普通字符串','01'],
 *				['日期域','02'],
 *				//['签名日期域','03'],
 *				['意见签名域','04'],
 *				['不用验证签名域','05'],
 *				['需要验证签名域','06'],
 *				['数字域','07'],
*				['计算域','08']
*			];
 */
			
var EForm2FLOW_VAR_MAP =[
	['VARCHAR2', '字符串'],
	['TIMESTAMP','日期型'],
	['DATE','日期型'],
	['CLOB','大字段'],
	['NUMBER','数字型']
]

var CForm2FLOW_VAR_MAP = [
	['01','字符串'],
	['02','日期型'],
	['04','字符串'],
	['05','字符串'],
	['06','字符串'],
	['07','整型'],
	['08','字符串']
]

//流程变量的类型
var VarTypes=[
	['01','字符串'],
	['02','整型'],
	['03','浮点型'],
	['04','日期型']
]

			
			
/**
 * 根据变量类型的编码取得变量类型的中文名称(EFORM)
 */
function getVarTypeNameEForm(id){
	for(var i=0;i<EForm2FLOW_VAR_MAP.length;i++){
		if(EForm2FLOW_VAR_MAP[i][0] == id){
			return EForm2FLOW_VAR_MAP[i][1]; 
		}
	}
	return "";
}

/**
 * 根据变量类型的编码取得变量类型的中文名称(CFORM)
 */
function getVarTypeNameCForm(id){
	for(var i=0;i<CForm2FLOW_VAR_MAP.length;i++){
		if(CForm2FLOW_VAR_MAP[i][0] == id){
			return CForm2FLOW_VAR_MAP[i][1]; 
		}
	}
	return "";
}

/**
 * 流程变量表格
 */
FlowVariableGrid = function() {
	flowVariableGrid = this ; 
	//默认DS
	fvDefaultDS = this.getDataStore();
	//工具栏
	FlowVariableGrid.superclass.constructor.call(this,{
		tbar : ['->',{text:'导入表单变量',handler : this.importCFormVariable},{text:'清空变量',handler : this.clearVariable},{text:'新增变量',handler : function(){flowVariableGrid.createVariable('','','','流程变量');}},{text:'删除变量',handler : this.deleteVariable}],
		loadMask : true ,
		sm : this.sm,
		cm : this.getColumnMode() ,
		store : fvDefaultDS,
		//autoWidth:true,
		width:570,
		height:230,
		bbar:["<span style='color:red'>双击以修改指定的值</span>"]
	})
	
	//做变量名是否相同的提示
	this.on("beforeedit",function(obj) {
		var name = obj.record.data.variableName ;
		var items = flowVariableGrid.store.data.items ;
		for(var i=0 ; i<items.length ; i++) {
			if(obj.record.id == items[i].id) {
				continue ;
			}
			if(obj.record.data.variableName == items[i].data.variableName) {
				flowVariableGrid.store.remove(obj.record);
				return ; 
			}
		}
	})
	
	//做变量名是否相同的提示
	this.on("afteredit",function(obj){
		var name = obj.record.data.variableName ;
		var items = flowVariableGrid.store.data.items ;
		for(var i=0 ; i<items.length ; i++) {
			if(obj.record.id == items[i].id) {
				continue ;
			}
			if(obj.record.data.variableName == items[i].data.variableName) {
				alert("有同名变量,请处理好同名变量,否则不能保存") ;
				return ; 
			}
		}
	}) ;
	
	this.on("cellclick",function(grid, rowIndex, columnIndex, e ) {
		
	}) ;
	fvDefaultDS.load();
}


Ext.extend(FlowVariableGrid , Ext.grid.EditorGridPanel , {
	sm:new Ext.grid.RowSelectionModel(),
	data:{
			'totalCount':Ext.decode(getLabelValue('flowVariable')).length,
			'list': Ext.decode(getLabelValue('flowVariable'))
		}, 
	/**
	 * 新建变量
	 * fd:变量名称
	 * cp:变量中文名称
	 * tp:变量类型 '普通字符串','日期域',签名日期域,意见签名域,不用验证签名域,需要验证签名域
	 * tp2:'流程变量'  '表单变量'
	 */
	createVariable : function(fd,cp,tp,tp2) {
		var Model = Ext.data.Record.create([{name:'variableName',mapping:'name'},{name:'cnName',mapping:'name_cn'},{name:'variableType',mapping:'type'},{name:'variableValue',mapping:'value'},{name:'type',mapping:'kind'},{name:'storeMode',mapping:'storemode'}])
		var record = new Model(
			{
				'variableName' : fd,
				'cnName' : cp,	
				'variableType' : tp ,
				'variableValue' : '' ,
				'type' : tp2 ,
				'storeMode' : '覆盖'
			}
		);
		//flowVariableGrid.store.data
		 flowVariableGrid.stopEditing();
         fvDefaultDS.insert(0, record);
         flowVariableGrid.startEditing(0, 0);
		 //flowVariableGrid.insert(0,record)
	} ,
	//删除变量
	deleteVariable : function() {
		if(!confirm("是否确定移除选择的变量")){
			return;
		}
		var selection = flowVariableGrid.sm.getSelections();
		if(selection == null) {
			alert("请任选一单元格") ;
			return ;
		}
		flowVariableGrid.store.commitChanges() ;
		for(var i=0;i<selection.length;i++){
			var record = flowVariableGrid.store.getById(selection[i].id);
			flowVariableGrid.store.remove(record);
		}
	} ,
	
	clearVariable : function(){
		if(confirm("是否确定移除所有以添加的变量")){
			flowVariableGrid.store.removeAll();
		}
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
	             	{name: 'name'},
	             	{name: 'name_cn'},
	             	{name: 'type'},
	             	{name: 'value'},
	             	{name: 'kind'},
				 	{name: 'storemode'} 
	             ]
			},[{name:'variableName',mapping:'name'},{name:'cnName',mapping:'name_cn'},{name:'variableType',mapping:'type'},{name:'variableValue',mapping:'value'},{name:'type',mapping:'kind'},{name:'storeMode',mapping:'storemode'}])
		});
		return store ;
	} ,
	//获得列模型
	getColumnMode : function() {
		var cm=new Ext.grid.ColumnModel([
		        {id:'variableName',fixed :true,header: "变量名称", width: 100, sortable: true, dataIndex: 'variableName',editor:new Ext.form.TextField({allowBlank: false})},
		        {id:'cnName',header: "中文名称", width: 110, sortable: true, dataIndex: 'cnName',editor:new Ext.form.TextField({allowBlank: false})},
				{id:'variableType',header: "变量类型", width: 100, sortable: true, dataIndex: 'variableType',editor : new Ext.form.ComboBox({
		        	store : new Ext.data.SimpleStore({
		        		fields : ['retrunValue','displayText'],
		        		data : VarTypes
		        	}),
		        	listWidth:150,
					displayField: "displayText", 
		        	mode:'local' ,
					triggerAction : 'all',
					lazyRender:true,
					listClass: 'x-combo-list-small'
				})},
				{id:'variableValue',header: "变量值", width: 79, sortable: true, dataIndex: 'variableValue',editor:new Ext.form.TextField()},	
				{id:'type',header: "类别", width: 79, sortable: true, dataIndex: 'type',editor : new Ext.form.ComboBox({
		        	store : new Ext.data.SimpleStore({
		        		fields : ['retrunValue','displayText'],
		        		data : [['流程变量','流程变量'],['表单变量','表单变量']]
		        	}),
					valueField :"retrunValue", 
					displayField: "displayText", 
		        	mode:'local' ,
					triggerAction : 'all',
					lazyRender:true,
					listClass: 'x-combo-list-small'
				})},
				{id:'storeMode',header: "存储方式", width: 70, sortable: true, dataIndex: 'storeMode',editor : new Ext.form.ComboBox({
		        	store : new Ext.data.SimpleStore({
		        		fields : ['retrunValue','displayText'],
		        		data : [['追加','追加'],['覆盖','覆盖']]
		        	}),
					valueField :"retrunValue", 
					displayField: "displayText", 
		        	mode:'local' ,
					triggerAction : 'all',
					lazyRender:true,
					listClass: 'x-combo-list-small'
				})}
			]);
		return cm;
	},
	
	/**
	 * 导入自定义表单变量
	 */
	importCFormVariable : function(){
		var formType = radioFormType.getGroupValue();
		if(formType != "01" && formType != "02" ){
			alert("不能导入非自定义表单的表单变量,请选择表单类型为自定义表单");
			return;
		}

		var link = formType == "01" ? link4 : link2;
		var formId = relFormId.getValue();

		//提交数据
	 	Ext.Ajax.request({
	 		url:link,
	 		success:function(ajax){
	 			var responseText=ajax.responseText;	
	 			var responseObject=Ext.util.JSON.decode(responseText);
	 			if(responseObject.success){
	 				var inputs = responseObject.editableInputs;
	 				Ext.each(inputs,function(input){
						var typeName = "";
	 					if (formType == '01') {
		 					typeName = getVarTypeNameEForm(input.tp);
	 					} else {
	 						typeName = getVarTypeNameCForm(input.tp);
	 					}
	 					
	 					flowVariableGrid.createVariable(input.fd,input.cp,typeName,"表单变量");
	 				});
	 				alert("导入成功");
	 			}else{
	 				alert("数据请求失败");
	 			}
	 		},
	 		failure:function(){
	 			alert("提交失败");
	 		},
	 		method:'POST',
	 		params:{id:formId}
	 	})
	}
	
})