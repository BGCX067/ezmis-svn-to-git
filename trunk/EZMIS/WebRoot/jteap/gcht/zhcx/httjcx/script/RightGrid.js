
/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link4);
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'导出Excel',handler:function(){
		exportExcel(grid,true);
		}}]
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
		tbar:this.pageToolbar 
	});	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		if (grid.getSelectionModel().getSelections().length != 1) {
			alert("请选取一条记录查看");
			return;
		}

		var select = grid.getSelectionModel().getSelections()[0];
		var url = link3 + "?pid=" + select.get("ID_") + "&status=false";
		showIFModule(url, "合同详细信息", "true", 735, 582, {});
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
			'id','htmc','htbh','htlx','fyxz','status','htcjsj', "ID_", "VERSION_", "START_", "END_",
					"PROCESSINSTANCE_", "FLOW_NAME", "PROCESS_DATE", "FLOW_CONFIG_ID", "FLOW_FORM_ID"
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
				{id:'id',header: "id", width: 100, sortable: true, dataIndex: 'id',hidden:true},
				{id:'htmc',header: "合同名称", width: 100, sortable: true, dataIndex: 'htmc'},
				{id:'htbh',header: "合同编号", width: 130, sortable: true, dataIndex: 'htbh'},
				{id:'htlx',header: "合同类型", width: 100, sortable: true, dataIndex: 'htlx'},
				{id:'fyxz',header: "费用性质", width: 100, sortable: true, dataIndex: 'fyxz'},
				{id:'status',header: "合同状态", width: 100, sortable: true, dataIndex: 'status'},
				{id : 'htcjsj',header : "创建时间",width : 130,sortable : true,dataIndex : 'htcjsj',
				renderer:function(value,metadata,record,rowIndex,colIndex,store){
					if(value != null){
						return formatDate(new Date(value),"yyyy-MM-dd HH:mm:ss");
					}
				}}]);
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
	},
	/**
	 * 删除
	 */
	deleteSelect:function(select){
		var selections = this.getSelections();//获取被选中的行
		var rightGrid=this;
		var ids="";
		Ext.each(selections,function(selectedobj){
			ids+=selectedobj.id+",";//取得他们的id并组装
		});
		if(window.confirm("确认删除选中的条目吗？")){
			Ext.Ajax.request({
				url:link5,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			alert("删除成功");
			 			rightGrid.getStore().reload();
			 		}else{
			 			alert(responseObject.msg);
			 		}				
				},
			 	failure:function(){
			 		alert("提交失败");
			 	},
			 	method:'POST',
			 	params: {ids:ids}//Ext.util.JSON.encode(selections.keys)			
			});
		}
	}

});

