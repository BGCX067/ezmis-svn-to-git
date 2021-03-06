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
		items : ['-', '<font color="red">*双击查看详细信息</font>','-', '<font color="red">默认查询上月记录</font>']
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

	this.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
		var record = grid.getSelectionModel().getSelected();
		var groupSn = record.get('GROUP_SN');
		var url = link5 + "?cxid=" + groupSn;
		showModule(url, "yes", 800, 300);
		
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
			}, [ "ID", "GROUPNAME", "GROUP_SN", "HG", "BHG", "HJ", "HGL" ]),
			remoteSort : true
		});
		return ds;
	},

	/**
	 * PersonAction 列模型
	 */
	getColumnModel : function() {
		var cm;
		// 待处理
		cm = new Ext.grid.ColumnModel([{
			id : 'GROUPNAME',
			header : "部门名称",
			width : 120,
			sortable : true,
			dataIndex : 'GROUPNAME'
		}, {
			id : 'HJ',
			header : "总票数",
			align : 'right',
			width : 120,
			sortable : true,
			dataIndex : 'HJ'
		}, {
			id : 'HG',
			header : "合格票",
			align : 'right',
			width : 80,
			sortable : true,
			dataIndex : 'HG'
		}, {
			id : 'BHG',
			header : "不合格票",
			align : 'right',
			width : 100,
			sortable : true,
			dataIndex : 'BHG'
		}, {
			id : 'HGL',
			header : "合格率",
			align : 'right',
			width : 120,
			sortable : true,
			dataIndex : 'HGL',
			renderer : function(value) {
				return value+"%"
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
