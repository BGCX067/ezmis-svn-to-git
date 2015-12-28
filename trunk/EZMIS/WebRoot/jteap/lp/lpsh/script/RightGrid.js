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
		items : ['-', '<font color="red">*双击查看详细信息</font>','-', {
							text : '导出Excel',
							handler : function() {
								exportExcel(grid, true);
							}
						}]
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
		var btnSh = mainToolbar.items.get("btnSh");
		var btnCkpmxx = mainToolbar.items.get("btnCkpmxx");

		if (oCheckboxSModel.getSelections().length != 1) {
			if (btnSh)
				btnSh.setDisabled(true);
			if (btnCkpmxx)
				btnCkpmxx.setDisabled(true);
		} else {
			if (btnSh)
				btnSh.setDisabled(false);
			if (btnCkpmxx)
				btnCkpmxx.setDisabled(false);
		}
	});

	this.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
		var record = grid.getSelectionModel().getSelected();
		var arg = "";
		arg += "id|" + record.get('ID') + ";";
		arg += "ph|" + record.get('PH') + ";";
		arg += "pmc|" + record.get('PMC') + ";";
		arg += "jhkssj|" + record.get('JHKSSJ') + ";";
		arg += "jhjssj|" + record.get('JHJSSJ') + ";";
		arg += "tablename|" + record.get('TABLENAME') + ";";
		arg += "shr|" + ((record.get('SHR') == null) ? "" : record.get('SHR')) + ";";
		arg += "shbm|" + ((record.get('SHBM') == null) ? "" : record.get('SHBM')) + ";";
		arg += "shsj|" + ((record.get('SHSJ') == null) ? "" : record.get('SHSJ')) + ";";
		arg += "shzt|" + ((record.get('SHZT') == null) ? "" : record.get('SHZT')) + ";";
		arg += "pz|" + ((record.get('PZ') == null) ? "" : record.get('PZ'));

		var url = link10;
		showModule(url, true, 590, 320, arg);
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
			}, ["ID", "PH", "BZMC", "FZRMC", "STATUS", "JHKSSJ", "JHJSSJ", "SHBM", "SHR", "SHSJ", "PMC", "PZ", "SHZT",
					"TABLENAME"]),
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
			id : 'PH',
			header : "票号",
			width : 140,
			sortable : true,
			dataIndex : 'PH'
		}, {
			id : 'PMC',
			header : "票种类",
			width : 120,
			sortable : true,
			dataIndex : 'PMC'
		}, {
			id : 'FZRMC',
			header : "负责人",
			width : 80,
			sortable : true,
			dataIndex : 'FZRMC'
		}, {
			id : 'STATUS',
			header : "工作票状态",
			width : 80,
			sortable : true,
			dataIndex : 'STATUS'
		}, {
			id : 'BZMC',
			header : "发现班组",
			width : 100,
			sortable : true,
			dataIndex : 'BZMC'
		}, {
			id : 'JHKSSJ',
			header : "开始时间",
			width : 120,
			sortable : true,
			dataIndex : 'JHKSSJ'
		}, {
			id : 'JHJSSJ',
			header : "结束时间",
			width : 120,
			sortable : true,
			dataIndex : 'JHJSSJ'
		}, {
			id : 'SHBM',
			header : "审核部门",
			width : 120,
			sortable : true,
			dataIndex : 'SHBM'
		}, {
			id : 'SHR',
			header : "审核人",
			width : 120,
			sortable : true,
			dataIndex : 'SHR'
		}, {
			id : 'SHSJ',
			header : "审核时间",
			width : 120,
			sortable : true,
			dataIndex : 'SHSJ'
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
