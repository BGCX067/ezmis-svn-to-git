
/**
 * 字段列表
 */
DcGrid=function(){
    var defaultDs=this.getDefaultDS(link26);
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'导出Excel',handler:function(){
		exportExcel(grid,true);
		}},'-','<font color="red">*双击查看详细信息</font>']
	});
	DcGrid.superclass.constructor.call(this,{
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
		
		var btnDelCForm=mainToolbar.items.get('btnDel');
		var btnModifyCForm=mainToolbar.items.get('btnModify');
		
		if(oCheckboxSModel.getSelections().length==1){
			if(btnModifyCForm) btnModifyCForm.setDisabled(false);
		}else{
			if(btnModifyCForm) btnModifyCForm.setDisabled(true);
		}
		
		if(oCheckboxSModel.getSelections().length<=0){
			if(btnDelCForm) btnDelCForm.setDisabled(true);
		}else{
			if(btnDelCForm) btnDelCForm.setDisabled(false);
		}
	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=dcGrid.getSelectionModel().getSelections()[0];
		if(select.get("type")=="EFORM"){
			//var eformUrl=select.get("eformUrl");
			
			var url="/jteap/cform/fceform/common/djframe.htm?djsn="+select.get("sn")+"&catalogName="+select.get("eformUrl")
		    window.open(CONTEXT_PATH+url);
		}
		
		if(select.get('type')=="EXCEL"){
			var url=CONTEXT_PATH+"/jteap/cform/excelFormRec.jsp?cformId="+select.json.id;
			var features="menubar=no,toolbar=no,width=800,height=600";
			window.open(url,"_blank",features);
		}
		if(select){
			var url=link27+"&docid="+select.json.dbd.id;
			result=showModule(url,true,800,645);
		}
	});
	
}
Ext.extend(DcGrid, Ext.grid.GridPanel, {
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
	        "je",
			"id",
			"xh",
			"wzda",
			"wzda.xhgg",
			"wzda.wzmc",
			"wzda.kw",
			"wzda.kw.cwmc",
			"wzda.kw.ck",
			"wzda.kw.ck.ckmc",
			"dbd",
			"dbd.bh",
			"dbd.dbsj",
			"dbsl",
			"jldw",
			"dbdj"
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
				{id:'dbd.bh',header: "调出单编号", width: 100, sortable: true, dataIndex: 'dbd.bh'},
				{id:'dbd.dbsj',header: "调出时间", width: 150, sortable: true, dataIndex: 'dbd.dbsj',
				 	renderer:function(value,metadata,record,rowIndex,colIndex,store){
				 		var aa = new Date(value);
						var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd HH:mm:ss"); 
						return dt;         
					}
				},
				{id:'jldw',header: "计量单位", width: 100, sortable: true, dataIndex: 'jldw'},
				{id:'dbsl',header: "调出数量", width: 100, sortable: true,align:'right', dataIndex: 'dbsl',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(4);
					}
				},
				{id:'dbdj',header: "调出单价", width: 100, sortable: true,align:'right', dataIndex: 'dbdj',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(4);
					}
				},
				{id:'je',header: "金额", width: 100, sortable: true,align:'right', dataIndex: 'je',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						//var sl = record.get('dbsl');
						//var dj = record.get('dbdj');
						//value = sl*dj;
						return parseFloat(value).toFixed(4);
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

