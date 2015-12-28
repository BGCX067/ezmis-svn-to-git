RjpglGrid = function() {
	var url = link1 + "?cxid=qc&tableName=TB_LP_GZP_RLJX&tableCname=热力机械票";
	var queryParamsSql = " a.status='合格' ";
	url += "&queryParamsSql=" + queryParamsSql;
	var ds = new Ext.data.Store({
		proxy : new Ext.data.ScriptTagProxy({
			url : url
		}),

		reader : new Ext.data.JsonReader({
			root : 'list',
			totalProperty : 'totalCount',
			id : 'id'
		}, ["ID", "PH", "BZMC", "FZRMC", "STATUS", "JHKSSJ", "JHJSSJ", "PMC"]),
		remoteSort : true
	});
	ds.load();

	var cm = new Ext.grid.ColumnModel([{
		id : 'PH',
		header : "票号",
		width : 150,
		sortable : true,
		dataIndex : 'PH'
	}, {
		id : 'BZMC',
		header : "创建部门",
		width : 80,
		sortable : true,
		dataIndex : 'BZMC'
	}, {
		id : 'FZRMC',
		header : "负责人",
		width : 80,
		sortable : true,
		dataIndex : 'FZRMC'
	}, {
		id : 'JHKSSJ',
		header : "计划开始时间",
		width : 140,
		sortable : true,
		dataIndex : 'JHKSSJ'
	}, {
		id : 'JHJSSJ',
		header : "计划结束时间",
		width : 140,
		sortable : true,
		dataIndex : 'JHJSSJ'
	}]);

	this.pageToolbar = new Ext.PagingToolbar({
		pageSize : CONSTANTS_PAGE_DEFAULT_LIMIT,
		store : ds,
		displayInfo : true,
		displayMsg : '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg : "没有符合条件的数据"
	});

	RjpglGrid.superclass.constructor.call(this, {
		ds : ds,
		cm : cm,
		margins : '2px 2px 2px 2px',
		width : 600,
		height : 300,
		loadMask : true,
		region : 'center',
		tbar : this.pageToolbar
	});

	this.on('celldblclick', function(grid, rowIndex, columnIndex, e) {
		var record = grid.getSelectionModel().getSelected();
		if (!record) {
			alert('请选择一张工作票!');
			return;
		}
		var ph = record.get('PH');
		window.returnValue = ph;
		window.close();
	})
}

Ext.extend(RjpglGrid, Ext.grid.GridPanel, {})

var rjpglGrid = new RjpglGrid();
var panel = new Ext.Panel({
	layout : 'fit',
	region : 'center',
	items : [rjpglGrid],
	buttons : [{
		text : '确定',
		handler : function() {
			var record = rjpglGrid.getSelectionModel().getSelected();
			if (!record) {
				alert('请选择一张工作票!');
				return;
			}
			var ph = record.get('PH');
			window.returnValue = ph;
			window.close();
		}
	}, {
		text : '取消',
		handler : function() {
			window.close();
		}
	}]
})
