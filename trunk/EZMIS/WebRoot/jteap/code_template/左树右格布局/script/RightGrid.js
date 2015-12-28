RightGrid = function(link, myBody, zlbh, isPage) {
	var defaultDs = this.getDefaultDS(link);
	var grid = this;
	this.zlbh = zlbh;
	if (isPage == 1) {
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
							}, '-', '']
				});
	} else if (isPage == 2) {
		this.pageToolbar = null;
	}

	RightGrid.superclass.constructor.call(this, {
				ds : defaultDs,
				cm : this.getColumnModel(),
				margins : '2px 2px 2px 2px',
				width : 600,
				height : 300,
				loadMask : true,
				frame : true,
				renderTo : Ext.DomQuery.select("div.detailData", myBody)[0],
				tbar : this.pageToolbar
			});

	this.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
				var select = grid.getSelectionModel().getSelections()[0];
				if (select) {
					var url = link6 + "?fileid=" + select.json.ID + "&zlbh="
							+ this.zlbh;
					result = showModule2(url, true, 700, 600, select.json);
					if (result == "true") {
						grid.getStore().reload();
					}
				}
			});

}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
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
								id : 'ID'
							}, ["ID", "NSRSBH", "ZLNAME", "FILESTATUS",
									"PAGES", "CREATEDATE"]),
					remoteSort : true
				});
		return ds;
	},

	/**
	 * PersonAction 列模型
	 */
	getColumnModel : function() {
		var cm = new Ext.grid.ColumnModel([{
					id : 'ID',
					hidden : true,
					header : "id",
					width : 100,
					sortable : false,
					dataIndex : 'ID'
				}, {
					id : 'NSRSBH',
					header : "纳税人识别号",
					width : 130,
					sortable : false,
					dataIndex : 'NSRSBH'
				}, {
					id : 'ZLNAME',
					header : "资料名称",
					width : 200,
					sortable : false,
					dataIndex : 'ZLNAME'
				}, {
					id : 'FILESTATUS',
					header : "文件状态",
					width : 60,
					sortable : false,
					dataIndex : 'FILESTATUS',
					renderer : function(value, metadata, record, rowIndex,
							colIndex, store) {
						if (record.json.FILESTATUS == "0") {
							return "<span style='color:blue;font-weight:bold;'>暂存</span>";
						} else if (record.json.FILESTATUS == "1") {
							return "<span style='color:blue;font-weight:bold;'>通过</span>";
						} else if (record.json.FILESTATUS == "2") {
							return "<span style='color:blue;font-weight:bold;'>历史</span>";
						} else if (record.json.FILESTATUS == "3") {
							return "<span style='color:blue;font-weight:bold;'>删除</span>";
						} else
							return value;
					}
				}, {
					id : 'PAGES',
					header : "页数",
					width : 40,
					sortable : false,
					dataIndex : 'PAGES'
				}, {
					header : "上传时间",
					width : 100,
					sortable : false,
					dataIndex : 'CREATEDATE',
					renderer : function(value, metadata, record, rowIndex,
							colIndex, store) {
						var CREATEDATE = formatDate(new Date(value),
								"yyyy-MM-dd");
						return CREATEDATE;
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
		if (isPage == 1) {
			this.pageToolbar.bind(ds);
		}
		this.reconfigure(ds, cm);
	}

});
