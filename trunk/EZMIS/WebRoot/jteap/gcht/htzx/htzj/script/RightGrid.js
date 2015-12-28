
/**
 * 字段列表
 */
RightGrid = function() {
	var defaultDs = this.getDefaultDS("");
	var grid = this;
	this.pageToolbar = new Ext.PagingToolbar({
				pageSize : CONSTANTS_PAGE_DEFAULT_LIMIT,
				store : defaultDs,
				displayInfo : true,
				displayMsg : '共{2}条数据，目前为 {0} - {1} 条',
				emptyMsg : "没有符合条件的数据"
			});
	RightGrid.superclass.constructor.call(this, {
				ds : defaultDs,
				cm : this.getColumnModel(),
				sm : this.sm,
				margins : '2px 2px 2px 2px',
				width : 600,
				height : 300,
				loadMask : true,
				region : 'center',
				tbar : this.pageToolbar
			});

	// 当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange", function(oCheckboxSModel) {
				var btnZhongJie = mainToolbar.items.get("btnZhongJie");
				if (oCheckboxSModel.getSelections().length == 1) {
					if (btnZhongJie != null) {
						btnZhongJie.setDisabled(false);
					}
				}else{
					if (btnZhongJie != null) {
						btnZhongJie.setDisabled(true);
					}
				}
			});
	
}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
	sm : new Ext.grid.CheckboxSelectionModel(),

	/**
	 * 取得默认数据源 返回数据格式
	 */
	getDefaultDS : function(url) {
		var procFields = ["ID", "HTBH","HTJE","HTMC","HTLX","HTJE","SQR","SQBM","STATUS",
						"ID_", "VERSION_", "START_", "END_","PROCESSINSTANCE_", "FLOW_NAME","PROCESS_DATE","FLOW_CONFIG_ID","FLOW_FORM_ID"];
		
		var ds = new Ext.data.Store({
				proxy : new Ext.data.ScriptTagProxy({
							url : url
						}),
	
				reader : new Ext.data.JsonReader({
							root : 'list',
							totalProperty : 'totalCount',
							id : 'id'
						}, procFields),
				remoteSort : true
		});
		return ds;
	},

	/**
	 * 列模型
	 */
	getColumnModel : function() {
		var isHiddenHtje = false;
		if(tableName == "TB_HT_RLHT"){
			isHiddenHtje = true;	
		}
		
		var cm = new Ext.grid.ColumnModel([
			this.sm, 
			{id : 'ID',header : "ID",width : 100,hidden:true, sortable : true,dataIndex : 'ID'},
			{id : 'HTBH',header : "合同编号",width : 100,sortable : true,dataIndex : 'HTBH'}, 
			{id : 'HTMC',header : "合同名称",width : 150,sortable : true,dataIndex : 'HTMC'}, 
			{id : 'HTJE',header : "合同金额(元)",width : 100,hidden:isHiddenHtje,sortable : true,dataIndex : 'HTJE'}, 
			{id : 'HTLX',header : "合同类型",width : 80,sortable : true,dataIndex : 'HTLX'},
			{id : 'HTLX',header : "申请部门",width : 100,sortable : true,dataIndex : 'HTLX'},
			{id : 'HTLX',header : "申请人",width : 80,sortable : true,dataIndex : 'HTLX'},
			{id : 'PROCESS_DATE',header : "开始执行时间",width : 130,sortable : true,dataIndex : 'PROCESS_DATE'}]);
		return cm;
	},
	/**
	 * 切换数据源->LogAction!showList
	 */
	changeToListDS : function(url) {
		var ds = this.getDefaultDS(url);
		var cm = this.getColumnModel();
		this.pageToolbar.bind(ds);
		this.reconfigure(ds, cm);
	}

});
