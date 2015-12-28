
/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs = this.getDefaultDS(link12);    //新系统数据
    var grid=this;
   	this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:[]
	});
	RightGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(),
		sm: this.sm,
		margins:'2px 2px 2px 2px',
	    title:'需求计划申请列表',
		width:600,
		height:350,
		autoScroll:true,
		loadMask: true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar,
		deferRowRender:false
	});	
	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		var btnEnalbe=mainToolbar.items.get('btnEnable');
	});
	this.on('render',function() {
         grid.store.load();
    });
    this.store.on('load',function(){
        grid.getSelectionModel().selectFirstRow();
       	var jhid = grid.store.data.items[0].id;
		xqjhsqMxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',id:jhid};
		xqjhsqMxGrid.store.load();
    });
    this.on('cellclick',function(grid, rowIndex, columnIndex, e) {
    	var jhid = grid.store.data.items[rowIndex].id;
		xqjhsqMxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',id:jhid};
		xqjhsqMxGrid.store.load();
    });
}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
		//sm:new Ext.grid.RowSelectionModel({
	//	singleSelect:true
	//}),
	
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
            id: 'ID'
        }, ["ID", "XQJHSQBH", "GCLB", "GCXM", "SQBMMC"
	    ])
        //如果为true,每次store在加载时或者一条记录被删除时, 清除所有改动过的记录信息
	    });
		return ds;
	},
    
	/**
	 * PersonAction 列模型
	 */
	getColumnModel:function(){
		var cm=new Ext.grid.ColumnModel([
	      	//this.sm,
				//{id:'id',header: "ID", width: 100, sortable: true, dataIndex: 'id'},
				{id:'XQJHSQBH',header: "需求计划申请编号", width: 150, sortable: true, dataIndex: 'XQJHSQBH'},
				{id:'GCLB',header: "工程类别", width: 100, sortable: true, dataIndex: 'GCLB'},
				{id:'GCXM',header: "工程项目", width: 100, sortable: true, dataIndex: 'GCXM'},
				{id:'SQBM',header: "申请部门", width: 400, sortable: true, dataIndex: 'SQBMMC'}
//				{id:'zt',header: "状态", width: 100, sortable: true, dataIndex: 'zt'},
//				{id:'jhy',header: "计划人", width: 100, sortable: true, dataIndex: 'jhy'}
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

/**
 * 需求计划申请明细
 */
xqjhsqMxGrid=function(){
    var defaultDs=this.getDefaultDS(link13);
    var grid=this;
//    this.pageToolbar=new Ext.PagingToolbar({
//	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
//	    store: defaultDs,
//	    displayInfo: true,
//	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
//		emptyMsg: "没有符合条件的数据",
//		items:['-',{text:'导出Excel',handler:function(){
//		exportExcel(grid,true);
//		}},'-','<font color="red"></font>']
//	});
	xqjhsqMxGrid.superclass.constructor.call(this,{
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
	
//	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
//		var select=rightGrid.getSelectionModel().getSelections()[0];
//		if(select.get("type")=="EFORM"){
//			//var eformUrl=select.get("eformUrl");
//			
//			var url="/jteap/cform/fceform/common/djframe.htm?djsn="+select.get("sn")+"&catalogName="+select.get("eformUrl")
//		    window.open(CONTEXT_PATH+url);
//		}
//		
//		if(select.get('type')=="EXCEL"){
//			var url=CONTEXT_PATH+"/jteap/cform/excelFormRec.jsp?cformId="+select.json.id;
//			var features="menubar=no,toolbar=no,width=800,height=600";
//			window.open(url,"_blank",features);
//		}
//	});
	
	//双击记录展现相应的采购计划明细表
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
	});
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		
		var btnEnalbe=mainToolbar.items.get('btnEnable');
		if(oCheckboxSModel.getSelections().length>=1){
			if(btnEnalbe){ 
   		 		btnEnalbe.setDisabled(false);
			}
		}else{
			if(btnEnalbe) btnEnalbe.setDisabled(true);
		}

	});
}
Ext.extend(xqjhsqMxGrid, Ext.grid.GridPanel, {
	//sm:new Ext.grid.RowSelectionModel({}),
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
	           // totalProperty: 'totalCount',
	            id: 'id'
	        }, [
			"wzmc","xhgg","sqsl","xqjhsq","xqjhsq.gcxm","xqjhsq.gclb","xqjhsq.sqbmmc","id" 
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
//				{id:'id',header: "ID", width: 100, sortable: true, dataIndex: 'id'},
				{id:'xh',header: "序号", width: 40, sortable: true, dataIndex: 'xh',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return rowIndex+1;
					}
				},
				{id:'wzbm',header: "物资名称(规格)", width: 140, sortable: true, dataIndex: 'wzmc',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return value+"("+record.get('xhgg')+")";
					}
				},
				{id:'sqsl',header: "申请数量", width: 80, sortable: true, dataIndex: 'sqsl'},
				{id:'xqjhsq.gclb',header: "工程类别", width: 80, sortable: true, dataIndex: 'xqjhsq.gclb'},
				{id:'xqjhsq.gcxm',header: "工程项目", width: 120, sortable: true, dataIndex: 'xqjhsq.gcxm'},
				{id:'xqjhsq.sqbm',header: "班组", width: 80, sortable: true, dataIndex: 'xqjhsq.sqbmmc'}
				//{id:'cgsl',header: "采购数量", width: 80, sortable: true, dataIndex: 'cgsl'},
				//{id:'dqsl',header: "可用数量", width: 80, sortable: true, dataIndex: 'dqsl'},
				//{id:'cgjldw',header: "采购计量单位", width: 100, sortable: true, dataIndex: 'cgjldw'},
				//{id:'hsxs',header: "换算系数", width: 100, sortable: true, dataIndex: 'hsxs'},
				//{id:'cgy',header: "采购员", width: 100, sortable: true, dataIndex: 'person.userName'},
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

