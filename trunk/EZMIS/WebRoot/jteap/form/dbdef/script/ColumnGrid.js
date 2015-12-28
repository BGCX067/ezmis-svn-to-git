
/**
 * 字段列表
 */
ColumnGrid=function(){
    var defaultDs=this.getColumnActionDS(link3);
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: 1000,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'导出Excel',handler:function(){
		exportExcel(grid,true);
		}},'-',{text:'上调',handler : function() {
			var keys = grid.sm.selections.keys;
			grid.moveListDS(keys,'up');
		}},{text:'下调',handler : function() {
			var keys = grid.sm.selections.keys;
			grid.moveListDS(keys,'down');
		}},{text:'该表排序',handler : function() {
			var keys = grid.sm.selections.keys;
			grid.resetSortNo(keys);
		}}]
	});
	ColumnGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(),
		sm: this.sm,
	    margins:'0 1 1 1',
	    enableDragDrop:true,
	    ddGroup          : 'firstGridDDGroup',
	 	dropAllowed: true, 
		dragAllowed: true,
		width:600,
		height:300,
		loadMask: true,
		frame:true,
		region:'center',
		tbar:[{text:'上调',handler : function() {
			var keys = grid.sm.selections.keys;
			grid.moveListDS(keys,'up');
		}},{text:'下调',handler : function() {
			var keys = grid.sm.selections.keys;
			grid.moveListDS(keys,'down');
		}},{text:'该表排序',handler : function() {
			var keys = grid.sm.selections.keys;
			grid.resetSortNo(keys);
		}}]
	});	
	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		//只有单选的时候才能修改，多选的只能删除
		var btnModifyColumn=mainToolbar.items.get('btnModifyColumn');
		var btnDelColumn=mainToolbar.items.get('btnDelColumn');
		if(oCheckboxSModel.getSelections().length==1){
			if(btnModifyColumn) btnModifyColumn.setDisabled(false);
		}else{
			if(btnModifyColumn) btnModifyColumn.setDisabled(true);
		}
		
		if(oCheckboxSModel.getSelections().length<=0){
			if(btnDelColumn) btnDelColumn.setDisabled(true);
		}else{
			if(btnDelColumn) btnDelColumn.setDisabled(false);
		}
		
	});
	
	
}
Ext.extend(ColumnGrid, Ext.grid.GridPanel, {
	sm:new Ext.grid.CheckboxSelectionModel({sortable : true}),
	/**
	 * 取得ColumnAction数据源
	 */
	getColumnActionDS:function(url){
	    var ds = new Ext.data.Store({
	        proxy: new Ext.data.ScriptTagProxy({
	            url: url
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount',
	            id: 'id'
	        }, [
	            'columncode', 'columnname', 'columntype','columnlength','pk'
	        ]),
	        remoteSort: true	
	    });
		return ds;
	},
    
	/**
	 * PersonAction 列模型
	 */
	getColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
		    	this.sm,
		        {id:'columncode',header: "字段名", width: 120, sortable: false, dataIndex: 'columncode'},
		        {id:'columnname',header: "字段中文名", width: 200, sortable: false, dataIndex: 'columnname'},
				{id:'columntype',header: "字段类型", width: 120, sortable: false,resizable : false, dataIndex: 'columntype'},
				{id:'columnlength',header: "字段长度", width: 60, sortable: false,resizable : false, dataIndex: 'columnlength'},
				{id:'pk',header: "主键", width: 40, sortable: false,resizable : false, dataIndex: 'pk',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value==true)
							return "<span style='color:red'>★</span>";
						else
							return "";
		   			}
		   		}
			]);
		return cm;
	},
	/**
	 * 切换数据源->LogAction!showList
	 */
	changeToListDS:function(url){
		var ds = this.getColumnActionDS(url);	
		var cm=this.getColumnModel();
		this.pageToolbar.bind(ds);
		this.reconfigure(ds,cm);
	},
	
	/**
	 * 上下移动数据
	 */
	moveListDS:function(keys,dir){
		if(this.sm.getSelections().length <= 0){
			Ext.MessageBox.alert('提示', '请至少选择一条记录！');
			return;
		}
		Ext.MessageBox.show({
			msg : '正在请求数据, 请稍侯',
			progressText : '正在请求数据',
			width : 300,
			wait : true,
			waitConfig : {
				interval : 200
			}
		});
		var request = {
				"table_id": this.table_id,
				"ids" : keys,
				"dir" : dir
		}
		Ext.Ajax.request({
			url : link14,
			method : 'POST',
			params : request,// the unique id(s)
			scope : this,
			callback : function(options, success, response) {
//				if (success) {
//					var obj = Ext.decode(response.responseText);
//					Ext.MessageBox.hide();
//					Ext.getCmp('modifyuserform').getForm().setValues(obj.data);
//				} else {
//					Ext.MessageBox.hide();
//					Ext.MessageBox.alert("失败，请重试",
//							response.responseText);
//				}
			},
			failure : function(response, options) {
				Ext.MessageBox.hide();
				ReturnValue = Ext.MessageBox.alert("警告",
						"出现异常错误！请联系管理员！");
			},
			success : function(response, options) {
				Ext.MessageBox.hide();
				this.store.reload();
			}
		})
	},
	
	resetSortNo:function(keys){
		if(this.sm.getSelections().length <= 0){
			Ext.MessageBox.alert('提示', '请至少选择一条记录！');
			return;
		}
		Ext.MessageBox.show({
			msg : '正在请求数据, 请稍侯',
			progressText : '正在请求数据',
			width : 300,
			wait : true,
			waitConfig : {
				interval : 200
			}
		});
		var request = {
				"ids" : keys,
				"tableid":this.table_id,
				"charset":"UTF-8"
		}
		
		Ext.Ajax.request({
			url : link15,
			method : 'POST',
			params : request,// the unique id(s)
			scope : this,
			callback : function(options, success, response) {

			},
			failure : function(response, options) {
				Ext.MessageBox.hide();
				ReturnValue = Ext.MessageBox.alert("警告",
						"出现异常错误！请联系管理员！");
			},
			success : function(response, options) {
				Ext.MessageBox.hide();
				this.store.reload();
			}
		})
	}

});

