/**
 * 字段列表
 */
RightGrid = function() {
	var defaultDs = this.getDefaultDS(link1);
	defaultDs.load();
	var grid = this;
	this.pageToolbar = new Ext.PagingToolbar({
		pageSize : CONSTANTS_PAGE_DEFAULT_LIMIT,
		store : defaultDs,
		displayInfo : true,
		displayMsg : '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg : "没有符合条件的数据",
		items : ['-', '<font color="red">*双击查看详细信息</font>']
	});
	RightGrid.superclass.constructor.call(this, {
		ds : defaultDs,
		cm : this.getColumnModel(),
		sm : this.sm,
		margins : '2px 2px 2px 2px',
		width : 600,
		height : 300,
		loadMask : true,
		frame : true,
		region : 'center',
		tbar : this.pageToolbar
	});

	this.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
		var record = rightGrid.getSelectionModel().getSelected();
		var stadt = formatDate(new Date(record.get('STRDT')),"yyyy-MM-dd");
		var enddt = formatDate(new Date(record.get('ENDDT')),"yyyy-MM-dd");
		var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn=" + "TB_FORM_BZTJ" + "&tjkssj="
				+ stadt+"&tjjssj="+enddt+"&txr="+encodeURIComponent(record.get('TXR'));
		var result = showModule(url, "yes", 800, 600, null, "", "");
		//showIFModule(url,"班组统计","true",700,600,{},null,null,null,false,"auto");
	});

}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
	sm : new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	}),

	/**
	 * 取得默认数据源 返回数据格式
	 */
	getDefaultDS : function(url) {
		var ds = new Ext.data.Store({
			proxy : new Ext.data.ScriptTagProxy({
				url : url
			}),

			reader : new Ext.data.JsonReader({
				root : 'list',
				totalProperty : 'totalCount',
				id : 'id'
			}, ["ID", "STRDT","ENDDT", "TXR", "TXSJ"]),
			remoteSort : true
		});
		return ds;
	},

	/**
	 * PersonAction 列模型
	 */
	getColumnModel : function() {
		var cm = new Ext.grid.ColumnModel([{
			id : 'STRDT',
			header : "统计开始时间",
			width : 120,
			sortable : true,
			dataIndex : 'STRDT',
			renderer:function(value,metadata,record,rowIndex,colIndex,store){
				var dt = formatDate(new Date(value),"yyyy-MM-dd");
				return dt;
			}
		}, {
			id : 'ENDDT',
			header : "统计开始时间",
			width : 120,
			sortable : true,
			dataIndex : 'ENDDT',
			renderer:function(value,metadata,record,rowIndex,colIndex,store){
				var dt = formatDate(new Date(value),"yyyy-MM-dd");
				return dt;
			}
		},{
			id : 'TXR',
			header : "填报人",
			width : 120,
			sortable : true,
			dataIndex : 'TXR'
		}, {
			id : 'TXSJ',
			header : "统计时间",
			width : 180,
			sortable : true,
			dataIndex : 'TXSJ',
			renderer : function(value, metadata, record, rowIndex, colIndex, store) {
				var dt = formatDate(new Date(value), "yyyy-MM-dd");
				return dt;
			}
		}]);
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
