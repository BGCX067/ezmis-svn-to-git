
/**
 * 采购计划
 */
FpGrid=function(){
    var defaultDs=this.getDefaultDS(link18);
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据"
		//items:['&nbsp;|&nbsp;','<font color="red">*双击记录查看采购明细</font>']
//		items:['-',{text:'导出Excel',handler:function(){
//		exportExcel(grid,true);
//		}},'-','<font color="red"></font>']
	});
	FpGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(),
		sm: this.sm,
	    margins:'2px 2px 2px 2px',
		width:900,
		height:250,
		loadMask: true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar 
	});	
	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
	});
	
	//双击记录展现相应的采购计划明细表
	this.on("cellclick",function(grid, rowIndex, columnIndex, e){
	});
	
}
Ext.extend(FpGrid, Ext.grid.GridPanel, {
	sm:new Ext.grid.RowSelectionModel({
		singleSelect:true
	}),
	
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
	            id: 'XQJHMXID'
	        }, [
				"XQJHMXID",
				"XQJHBH", 
				"BH",
				"WZBM", 
				"XH",
				"PZSL", 
				"JLDW",
				"JHDJ", 
				"FREE",
				"CGSL",
				"DHSL",
				"LYSL",
				"SLSL",
				"DQKC",
				"YFPSL"
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
		    	//this.sm,
		    	{id:'xh',header: "序号", width: 40, sortable: true, 
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
					 	return rowIndex+1;
					}
				},
//				{id:'id',header: "ID", width: 100, sortable: true, dataIndex: 'id'},
				{id:'BH',header: "需求计划编号", width: 100, sortable: true, dataIndex: 'BH'},
				{id:'JLDW',header: "计量单位", width: 100, sortable: true, dataIndex: 'JLDW'},
				{id:'JHDJ',header: "计划单价", width: 100, sortable: true,align:'right', dataIndex: 'JHDJ',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value==null){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'PZSL',header: "批准数量", width: 100, sortable: true, align:'right',dataIndex: 'PZSL',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value==null){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'YFPSL',header: "已分配", width: 100, sortable: true, align:'right',dataIndex: 'YFPSL',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value==null){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'CGSL',header: "已采购", width: 100, sortable: true,align:'right', dataIndex: 'CGSL',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value==null){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'DHSL',header: "已到货", width: 100, sortable: true,align:'right', dataIndex: 'DHSL',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value==null){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'LYSL',header: "已领用", width: 100, sortable: true, align:'right',dataIndex: 'LYSL',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value==null){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'ZYKCL',header: "占用库存量", width: 100, sortable: true, align:'right',dataIndex: 'ZYKCL',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						var dqkc = record.get('DQKC');
						var yfpsl = record.get('YFPSL');
						value = dqkc - yfpsl;
						return parseFloat(value).toFixed(2);
					}
				}
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