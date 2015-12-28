
/**
 * EFORM自定义表单子表控件封装
 */
SubGrid=function(config){
    var grid=this;
	config = Ext.apply({
			ds: new Ext.data.Store(),
		 	cm: this.getColumnModel(),
			loadMask: true,
			border:true,
			frame:false,
			disableSelection :true,
			enableColumnHide :false,
			enableColumnMove :false,
			enableColumnResize :false,
			trackMouseOver :false,
			enableHdMenu :false
		},config);
	
	SubGrid.superclass.constructor.call(this,config);	
}
Ext.extend(SubGrid, Ext.grid.GridPanel, {
	
	/**
	 * default 列模型
	 */
	getColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
		        {id:'columncode',header: " ", width: 2, sortable: true}
			]);
		return cm;
	},
	/**
	 * 切换数据源->LogAction!showList
	 */
	updateData:function(cm,ds){
		this.reconfigure(ds,cm);
		this.store.reload();
	}

});






/**
 * EFORM自定义表单子表控件封装
 */
SubEditGrid=function(config){
    var grid=this;
	config = Ext.apply({
			ds: new Ext.data.Store(),
		 	cm: this.getColumnModel(),
			loadMask: true,
			border:true,
			frame:false
		},config);
	
	if(config.bShowToolBar){
		config.tbar =[{text:'签名',handler:function(){
				grid.editor.createNew();
				if(grid.editor){
					grid.editor.doSave();
				}
			}}
			/**
			,{text:'保存',handler:function(){
				if(grid.editor){
					grid.editor.doSave();
				}
			}},{text:'刷新',handler:function(){
				grid.store.reload();
			}}
			**/
			];
	}
	SubEditGrid.superclass.constructor.call(this,config);	
}
Ext.extend(SubEditGrid, Ext.grid.EditorGridPanel, {
	
	/**
	 * default 列模型
	 */
	getColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
		        {id:'columncode',header: " ", width: 2, sortable: true}
			]);
		return cm;
	},
	/**
	 * 切换数据源->LogAction!showList
	 */
	updateData:function(cm,ds){
		this.reconfigure(ds,cm);
		this.store.reload();
	},
	createNew:function(){
	    var fields = this.getStore().fields.items;
		var Rec = Ext.data.Record.create(fields);
		var r = {};
		for(var i = 0;i<fields.length;i++){
			eval("r."+fields[i].name+"=''");
		}
		var rec = new Rec(r);
	    this.store.insert(0, rec);
	}

});