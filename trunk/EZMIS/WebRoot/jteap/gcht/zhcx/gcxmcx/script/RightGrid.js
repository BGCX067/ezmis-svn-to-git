
/**
 * 字段列表
 */
RightGrid = function() {
	var url = link2+"?queryParamsSql=to_char(cjsj,'yyyy')='"+new Date().format("Y-m-d")+"'";
	var defaultDs = this.getDefaultDS(url);
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
		frame : true,
		region : 'center',
		tbar : this.pageToolbar
	});

	this.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
		
	});
}

Ext.extend(RightGrid, Ext.grid.GridPanel, {
	sm : new Ext.grid.CheckboxSelectionModel(),

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
			}, ["id","xmbh","xmmc","xmlx","sqbm","cbfs","gcys","status","cjsj"]),
			remoteSort : true
		});
		return ds;
	},

	/**
	 * PersonAction 列模型
	 */
	getColumnModel : function() {
		var cm = new Ext.grid.ColumnModel([this.sm, {
			id : 'id',
			header : "id",
			width : 100,
			hidden : true,
			sortable : true,
			dataIndex : 'id'
		}, {
			id : 'xmbh',
			header : "合同编号",
			width : 100,
			sortable : true,
			dataIndex : 'xmbh'
		}, {
			id : 'xmmc',
			header : "项目名称",
			width : 120,
			sortable : true,
			dataIndex : 'xmmc'
		}, {
			id : 'xmlx',
			header : "项目类型",
			width : 100,
			sortable : true,
			dataIndex : 'xmlx'
		}, {
			id : 'gcys',
			header : "项目预算(元)",
			width : 100,
			sortable : true,
			dataIndex : 'gcys'
		}, {
			id : 'sqbm',
			header : "申请部门",
			width : 70,
			sortable : true,
			dataIndex : 'sqbm'
		}, {
			id : 'status',
			header : "项目状态",
			width : 100,
			sortable : true,
			dataIndex : 'status'
		}, {
			id : 'cjsj',
			header : "创建时间",
			width : 130,
			sortable : true,
			dataIndex : 'cjsj'
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
