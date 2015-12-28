
/**
 * 字段列表
 */
RightGrid = function() {
	var defaultDs = this.getDefaultDS(link2);
	var grid = this;
	this.pageToolbar = new Ext.PagingToolbar({
		pageSize : CONSTANTS_PAGE_DEFAULT_LIMIT,
		store : defaultDs,
		displayInfo : true,
		displayMsg : '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg : "没有符合条件的数据",
		items : ['-', {
			text : '导出Excel',
			handler : function() {
				exportExcel(grid, true);
			}
		}, '-', '<font color="red">*双击查看详细信息</font>']
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
		if (rightGrid.getSelectionModel().getSelections().length != 1) {
			alert("请选取一条记录查看");
			return;
		}

		var node = leftTree.getSelectionModel().getSelectedNode();
		var select = rightGrid.getSelectionModel().getSelections()[0];
		var url = link3 + "?pid=" + select.get("ID_") + "&status=false";
		showIFModule(url, "燃料合同详细信息", "true", 735, 582, {});
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
			}, ["ID", "HTBH", "HTMC", "HTLX", "HTJE", "GFDW", "SQBM", "STATUS", "ID_", "VERSION_", "START_", "END_",
					"PROCESSINSTANCE_", "FLOW_NAME", "PROCESS_DATE", "FLOW_CONFIG_ID", "FLOW_FORM_ID"]),
			remoteSort : true
		});
		return ds;
	},

	/**
	 * PersonAction 列模型
	 */
	getColumnModel : function() {
		var cm = new Ext.grid.ColumnModel([this.sm, {
			id : 'HTBH',
			header : "合同编号",
			width : 100,
			sortable : true,
			dataIndex : 'HTBH'
		}, {
			id : 'HTMC',
			header : "合同名称",
			width : 120,
			sortable : true,
			dataIndex : 'HTMC'
		}, {
			id : 'HTJE',
			header : "合同金额(元)",
			width : 100,
			sortable : true,
			dataIndex : 'HTJE'
		}, {
			id : 'HTLX',
			header : "合同类型",
			width : 60,
			sortable : true,
			dataIndex : 'HTLX'
		}, {
			id : 'SQBM',
			header : "申请部门",
			width : 70,
			sortable : true,
			dataIndex : 'SQBM'
		}, {
			id : 'STATUS',
			header : "审批状态",
			width : 60,
			sortable : true,
			dataIndex : 'STATUS'
		}, {
			id : 'PROCESS_DATE',
			header : "操作时间",
			width : 130,
			sortable : true,
			dataIndex : 'PROCESS_DATE'
		}, {
			header : '合同详细',
			width : 60,
			sortable : false,
			renderer : function(value, metadata, record) {
				var url = link5 + "&htId=" + record.get('ID');
				return "<a href='javascript:void(0)' onclick='func(\"" + url + "\")' >打开合同</a>";
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
var func = function(url) {
	var args = "url|" + url + ";title|" + "物资合同详细信息";
	showModule(link7, "yes", 810, 300, args);
}