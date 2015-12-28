/**
 * 字段列表
 */
RightGrid=function(){
	
	var url = link18+"?htcjsj="+new Date().format("Y");
    var defaultDs=this.getDefaultDS(url);
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items : ['-', '<font color="red">*双击选择</font>']
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
		var select=grid.getSelectionModel().getSelections()[0];
		var obj = {};
		obj.id = select.data.ID;
		obj.xmbh = select.data.XMBH;
		obj.xmmc = select.data.XMMC;
		obj.cbfs = select.data.CBFS;
		obj.xmyj = select.data.XMYJ;
		obj.jycjdw = select.data.JYCJDW;
		obj.gcnr = select.data.GCNR;
		obj.kgrq = select.data.KGRQ;
		obj.jhgq = select.data.JHGQ;
		obj.jz = select.data.JZ;
		obj.zy = select.data.ZY;
		
		obj.yffzr = select.data.YFFZR;
		obj.yffzrlxfs = select.data.YFFZRLXFS;
		obj.yfsgfzr = select.data.YFSGFZR;
		obj.yfsgfzrlxfs = select.data.YFSGFZRLXFS;
		
		window.returnValue = obj;
		window.close();
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
				"ID","XMBH","XMMC","GCYS","CBFS","XMYJ","JYCJDW","GCNR","KGRQ","JHGQ","JZ","ZY","YFFZR","YFFZRLXFS","YFSGFZR","YFSGFZRLXFS","SQBM","STATUS","CJSJ"
	        ]),
	        remoteSort: true,
	        //如果为true,每次store在加载时或者一条记录被删除时, 清除所有改动过的记录信息
	        pruneModifiedRecords: true
	    });
		return ds;
	},
    
	/**
	 * DqgzAction 列模型
	 */
	getColumnModel:function(){
		var grid = this;
				
	    var cm=new Ext.grid.ColumnModel([
		    	this.sm,
				{id:'ID',header: "id", width: 100, sortable: true, hidden:true, dataIndex: 'ID'},
				{id:'XMBH',header: "项目编号", width: 100, sortable: true, dataIndex: 'XMBH'},
				{id:'XMMC',header: "项目名称", width: 150, sortable: true, dataIndex: 'XMMC'},
				{id:'GCYS',header: "工程预算", width: 100, sortable: true, dataIndex: 'GCYS'},
				{id:'CBFS',header: "承包方式", width: 100, sortable: true, dataIndex: 'CBFS'},
				{id:'YFFZR',header: "乙方负责人", width: 100, sortable: true, dataIndex: 'YFFZR'},
				{id:'YFFZRLXFS',header: "乙方负责人联系方式", width: 100, sortable: true, dataIndex: 'YFFZRLXFS'},
				{id:'YFSGFZR',header: "乙方施工负责人", width: 100, sortable: true, dataIndex: 'YFSGFZR'},
				{id:'YFSGFZRLXFS',header: "乙方施工负责人联系方式", width: 100, sortable: true, dataIndex: 'YFSGFZRLXFS'},
				{id:'SQBM',header: "申请部门", width: 100, sortable: true, dataIndex: 'SQBM'},
				{id:'STATUS',header: "审批状态", width: 100, sortable: true, dataIndex: 'STATUS'},
				{id:'CJSJ',header: "创建时间", width: 130, sortable: true, dataIndex: 'CJSJ'}]);
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
