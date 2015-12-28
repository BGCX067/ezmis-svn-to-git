Ext.onReady(function() {
	Ext.QuickTips.init();

	var htFkDs = new Ext.data.JsonStore({
		url : link6 + "?htId=" + htId + "&tableName="+tableName,
		root : 'list',
		fields : ["ID", "HTBH", "HTJE", "HTMC", "HTLX", "HTJE", "SKDW", "BCSQFKJE", "SQBMZDR", "STATUS", "ID_",
				"VERSION_", "START_", "END_", "PROCESSINSTANCE_", "FLOW_NAME", "PROCESS_DATE", "FLOW_CONFIG_ID",
				"FLOW_FORM_ID"]
	});
	htFkDs.load();

	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});

	var cm = new Ext.grid.ColumnModel([sm, {
		id : 'HTBH',
		header : "合同编号",
		width : 100,
		sortable : true,
		dataIndex : 'HTBH'
	}, {
		id : 'HTMC',
		header : "合同名称",
		width : 140,
		sortable : true,
		dataIndex : 'HTMC'
	}, {
		id : 'HTJE',
		header : "合同金额(元)",
		width : 80,
		sortable : true,
		dataIndex : 'HTJE'
	}, {
		id : 'HTLX',
		header : "合同类型",
		width : 60,
		sortable : true,
		dataIndex : 'HTLX'
	}, {
		id : 'SKDW',
		header : "收款单位",
		width : 100,
		sortable : true,
		dataIndex : 'SKDW'
	}, {
		id : 'BCSQFKJE',
		header : "申付金额(元)",
		width : 80,
		sortable : true,
		dataIndex : 'BCSQFKJE'
	}, {
		id : 'STATUS',
		header : "审批状态",
		width : 80,
		sortable : true,
		dataIndex : 'STATUS'
	}, {
		id : 'PROCESS_DATE',
		header : "操作时间",
		width : 130,
		sortable : true,
		dataIndex : 'PROCESS_DATE'
	}])

	var htFkGrid = new Ext.grid.GridPanel({
		ds : htFkDs,
		cm : cm,
		sm : sm,
		margins : '2px 2px 2px 2px',
		width : 600,
		height : 300,
		loadMask : true,
		frame : true
	});

	htFkGrid.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
		if (grid.getSelectionModel().getSelections().length != 1) {
			alert("请选取一条记录查看");
			return;
		}

		var select = grid.getSelectionModel().getSelections()[0];
		var url = link3 + "?pid=" + select.get("ID_") + "&status=false";
		showIFModule(url, "查看流程", "true", 735, 582, {});
	})

	var viewport = new Ext.Viewport({
		layout : 'fit',
		items : [htFkGrid]
	});

	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
});