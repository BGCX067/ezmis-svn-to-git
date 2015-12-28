
/**
 * EFORM自定义表单子表控件封装
 */
SubGrid=function(config){
    var grid=this;
	config = Ext.apply({
			ds: new Ext.data.Store(),
		 	cm: this.defaultColumnModel(),
			loadMask: true,
			border:true,
			frame:false,
			disableSelection :true,
			enableColumnHide :false,
			enableColumnMove :true,
			enableColumnResize :true,
			trackMouseOver :false,
			enableHdMenu :false
		},config);
	SubGrid.superclass.constructor.call(this,config);	
	this.on("celldblclick",function( grid, rowIndex, columnIndex, e){
	//	var col = this.
	})
}
Ext.extend(SubGrid, Ext.grid.GridPanel, {
	
	/**
	 * default 列模型
	 */
	defaultColumnModel:function(){
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
		 	cm: this.defaultColumnModel(),
			loadMask: true,
			border:true,
			frame:false
		},config);
	
	if(config.bShowToolBar){
		config.tbar =[{text:'增加',handler:function(){ 
				//需求计划申请流程过滤子表添加功能
				if(getEditor("XQJHQF") && getEditor("XJSJSZ")){
					//需求申请时
					if(getEditor("XQJHQF").getValue() == "1" && getEditor("XJSJSZ").getValue() == ""){
						grid.editor.createNew();
					}else{
						//需求申请以后节点
						if(getEditor('QMZT').getValue() == "" && getEditor("IS_UPDATE").value == ""){
							grid.editor.createNew();
						}else{
							alert("没有添加权限!");
						}
					}
				}else 
				//固定资产流程控制子表控件
				if(getEditor("ZCBM")){
					if(getEditor("SQR").getValue()==""){
						grid.editor.createNew();
					}else{
						alert("没有添加权限");
					}
				}else{
					grid.editor.createNew();
				}
			}},{text:'删除',handler:function(){
				//grid.editor为EForm 实例化 subgrid时向其增加的SUB的对象实例 editors.js
				grid.editor.doSave();
				grid.editor.doDelete();
			}},
			/*
			{text:'保存',handler:function(){
				if(grid.editor){
					grid.editor.doSave();
				}
			}},
			*/
			{text:'刷新',handler:function(){
				grid.store.reload();
			}}];
	}
	SubEditGrid.superclass.constructor.call(this,config);	
	this.on("celldblclick",function( grid, rowIndex, columnIndex, e){
	     if(this.getColumnModel().config[columnIndex].editor && this.getColumnModel().config[columnIndex].editor.field.xtype == 'poptextarea'){
	     	var fieldName = grid.getColumnModel().getDataIndex(columnIndex);
			var record = grid.getSelectionModel().selections.items[0];
			var data = record.get(fieldName);
	     	var pop = this.getColumnModel().config[columnIndex].editor.field;
	     	pop.setTxtValue(data);
	     	pop.purgeListeners();
	     	pop.on("close",function(){
	     		if(this.ok == true){
	     			var value = this.getTxtValue();
	     			record.set(fieldName,value);
	     		}
	     	});
	     	pop.showPop();
	     }
	})
}
Ext.extend(SubEditGrid, Ext.grid.EditorGridPanel, {
	
	/**
	 * default 列模型
	 */
	defaultColumnModel:function(){
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
	createNewRecord:function(){
	    var fields = this.getStore().fields.items;
		var Rec = Ext.data.Record.create(fields);
		var r = {};
		for(var i = 0;i<fields.length;i++){
			eval("r."+fields[i].name+"=''");
		}
		var rec = new Rec(r);
	    return rec;
	},
	createNew:function(rec,order){
		if(rec!=null)
			this.store.insert(order!=null&&order=='bottom'?this.store.getCount()+1:0, rec);
		
	}
});



