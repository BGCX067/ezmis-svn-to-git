
/**
 * 采购计划
 */
XqGrid=function(){
    var defaultDs=this.getDefaultDS(link19);
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
	XqGrid.superclass.constructor.call(this,{
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
	
	//双击记录展现相应的需求计划申请明细表单
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=this.getSelectionModel().getSelections()[0];
		if(select){
			var xqjhsqid  = select.json.XQJHSQID;
			Ext.Ajax.request({
				url:link32,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			var url = link33;
						var windowUrl = link34 + "?pid=" + responseObject.processinstance + "&status=false";
						var args = "url|" + windowUrl + ";title|" + '查看流程';
						var retValue = showModule(url, "yes", 700, 600, args);
			 		}else{
			 			alert(responseObject.msg);
			 		}				
				},
			 	failure:function(){
			 		alert("提交失败");
			 	},
			 	method:'POST',
			 	params: {xqjhsqid:xqjhsqid}// Ext.util.JSON.encode(selections.keys)
			});
		}else{
			alert("至少选择一条记录!");
			return;
		}
	});
	
}
Ext.extend(XqGrid, Ext.grid.GridPanel, {
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
				"GCLB",
				"GCXM",
				"SQBM",
				"XQJHSQBH",
				"XQJHSQID",
				"WZBM", 
				"XH",
				"XYSJ",
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
				{id:'XQJHSQBH',header: "需求计划申请编号", width: 120, sortable: true, dataIndex: 'XQJHSQBH'},
				{id:'GCLB',header: "工程类别", width: 100, sortable: true, dataIndex: 'GCLB'},
				{id:'GCXM',header: "工程项目", width: 100, sortable: true, dataIndex: 'GCXM'},
				{id:'SQBM',header: "申请部门", width: 100, sortable: true, dataIndex: 'SQBM'},
				{id:'PZSL',header: "批准数量", width: 100, sortable: true,align:'right', dataIndex: 'PZSL',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value==null){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'JLDW',header: "计量单位", width: 100, sortable: true, dataIndex: 'JLDW'},
				{id:'XYSJ',header: "需要时间", width: 150, sortable: true, dataIndex: 'XYSJ',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(!value) return;
				    	var dt = formatDate(new Date(value),"yyyy-MM-dd hh:mm:ss"); 
						return dt;    
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