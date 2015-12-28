BmcxGrid = function(cxid) {
	var defaultDs = this.getDefaultDS(link2+"?cxid="+cxid);
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
	BmcxGrid.superclass.constructor.call(this, {
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
		var id = record.get('ID');
		Ext.Ajax.request({
			url : link3,
			method : 'POST',
			params : {
				id : id
			},
			success : function(ajax) {
				var resText = ajax.responseText;
				var obj = Ext.decode(resText);
				if (obj.success) {
					var pid = obj.pid;
					var url = link4;
					var windowUrl = link6 + "?pid=" + pid + "&status=false";
					var args = "url|" + windowUrl + ";title|" + '查看流程';
					var retValue = showModule(url, "yes", 1100, 650, args);
				} else {
					alert('数据异常，请联系管理员！');
				}
			},
			failure : function() {
				alert('数据异常，请联系管理员！');
			}
		})

	});

}

Ext.extend(BmcxGrid, Ext.grid.GridPanel, {
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
			}, ["ID", "PH", "PMC", "BZMC", "FZRMC", "STATUS", "JHKSSJ", "JHJSSJ"]),
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
			id : 'FZRMC',
			header : "负责人",
			width : 80,
			sortable : true,
			dataIndex : 'FZRMC'
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
