
/**
 * 字段列表
 */
RightGrid=function(){
	var url = link1+"?formSn="+formSn+"&beginYmd="+beginYmd+"&endYmd="+nowYmd;
	
    var defaultDs=this.getDefaultDS(url);
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-','<font color="red">*双击查看详细信息</font>']
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
		tbar:mainToolbar,
		items: this.pageToolbar
	});	
	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		var btnModi=mainToolbar.items.get('btnModi');
		var btnDel=mainToolbar.items.get('btnDel');
		
		if(oCheckboxSModel.getSelections().length < 1){
			btnModi.setDisabled(true);
			btnDel.setDisabled(true);
		}else{
			btnDel.setDisabled(false);
			if(oCheckboxSModel.getSelections().length == 1){
				btnModi.setDisabled(false);
			}else{
				btnModi.setDisabled(true);
			}
		}
	});
	
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=rightGrid.getSelectionModel().getSelections()[0];
		var id = select.get("id");
		var url = "/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&docid="+id;
		var myTitle = "查看台账表单";
		showIFModule(CONTEXT_PATH+url,myTitle,"true",800,600,{});
    	rightGrid.getStore().reload();
	});
	
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
	        }, [
				"id","tianxieren","tianxieshijian"
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
				{id:'id',header: "id", width: 100, sortable: true, hidden:true, dataIndex: 'id'},
				{id:'tianxieren',header: "填写人", width: 150, sortable: true, dataIndex: 'tianxieren'},
				{id:'tianxieshijian',header: "填写时间", width: 150, sortable: true, dataIndex: 'tianxieshijian'}
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

});
