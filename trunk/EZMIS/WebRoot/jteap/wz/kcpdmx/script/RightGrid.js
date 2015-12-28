
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
		}},'-','<font color="red">*双击查看明细</font>']
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
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=rightGrid.getSelectionModel().getSelections()[0];
		if(select){
		    if(select.json==null){
		    	return;
		    }
			var xhgg = "";
			if(select.json.xhgg!=null){
				xhgg = select.json.wzda.xhgg;
			}
			var url=link6+"?wzmc="+encodeURIComponent(select.json.wzda.wzmc)+"&xhgg="+encodeURIComponent(xhgg);
			new $FW({url:url,height:645,width:800,type:'T1',userIF:true,baseParam:{}}).show();	//模式对话框
        
		}
	});
	
}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
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
	            id: 'id'
	        }, [
			"id",
			"xh",
			"wzda",
			"wzda.wzmc",
			"wzda.xhgg",
			"wzda.kw",
			"wzda.kw.cwmc",
			"wzda.kw.ck",
			"wzda.kw.ck.ckmc",
			"pdd",
			"pdd.bh",
			"pdd.czr",
			"jldw",
			"pqsl",
			"pqje",
			"slcy",
			"jecy",
			"cyyy",
			"zksj",
			"pjj",
			"pdsl"
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
		  {id:'pdd.bh',header: "盘点单编号", width: 100, sortable: true, dataIndex: 'pdd.bh'},
				{id:'wzmc',header: "物资名称", width: 180, sortable: true, dataIndex: 'wzda.wzmc',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value){
							return value + "("+store.getAt(rowIndex).get("wzda.xhgg")+")";
						}
						return "";
					}
				},
				{id:'wzda.kw.ck.ckmc',header: "仓库", width: 100, sortable: true, dataIndex: 'wzda.kw.ck.ckmc'},
				{id:'wzda.kw.cwmc',header: "库位", width: 100, sortable: true, dataIndex: 'wzda.kw.cwmc'},
				{id:'pjj',header: "盘前单价", width: 70, sortable: true,align:'right', dataIndex: 'pjj',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'pqsl',header: "盘前数量", width: 70, sortable: true,align:'right', dataIndex: 'pqsl',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'pqje',header: "盘前金额", width: 70, sortable: true,align:'right', dataIndex: 'pqje',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'pdsl',header: "盘点数量", width: 70, sortable: true,align:'right', dataIndex: 'pdsl',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}else{
							//record.set('pdsl',value);
							//record.commit();
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'zksj',header: "在库时间", width: 70, sortable: true, dataIndex: 'zksj'},
				{id:'slcy',header: "数量差异", width: 70, sortable: true, dataIndex: 'slcy'},
				{id:'jecy',header: "金额差异", width: 70, sortable: true, dataIndex: 'jecy'},
				{id:'cyyy',header: "差异原因", width: 300, sortable: true, dataIndex: 'cyyy'},
				{id:'pdd.czr',header: "操作人", width: 100, sortable: true, dataIndex: 'pdd.czr'}
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

