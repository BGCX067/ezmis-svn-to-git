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
				items : ['-', {
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
				frame : true,
				region : 'center',
				tbar : this.pageToolbar
			});

	this.getSelectionModel().on("selectionchange", function(oCheckboxSModel) {
				var btnShowJh = mainToolbar.items.get('btnShowJh');
				var btnShowXm = mainToolbar.items.get('btnShowXm');
				var btnShowZj = mainToolbar.items.get('btnShowZj');

				if (oCheckboxSModel.getSelections().length == 1) {
					if (btnShowJh)
						btnShowJh.setDisabled(false);
					if (btnShowXm)
						btnShowXm.setDisabled(false);
					if (btnShowZj)
						btnShowZj.setDisabled(false);
				} else {
					if (btnShowJh)
						btnShowJh.setDisabled(true);
					if (btnShowXm)
						btnShowXm.setDisabled(true);
					if (btnShowZj)
						btnShowZj.setDisabled(true);
				}

			})
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
									}, ["id", "jhmc", "jxxz", "jz", "qsrq", "time", "jsrq", "rgfy", "clfy", "nrgy"]),
							remoteSort : true
						});
				return ds;
			},

			/**
			 * PersonAction 列模型
			 */
			getColumnModel : function() {
				var cm = new Ext.grid.ColumnModel([this.sm, {
							id : 'jhmc',
							header : "计划名称",
							width : 150,
							sortable : true,
							dataIndex : 'jhmc'
						}, {
							id : 'jxxz',
							header : "检修性质",
							width : 90,
							sortable : true,
							dataIndex : 'jxxz'
						}, {
							id : 'jz',
							header : "机组",
							width : 60,
							sortable : true,
							dataIndex : 'jz'
						}, {
							id : 'qsrq',
							header : "起始日期",
							width : 100,
							sortable : true,
							dataIndex : 'qsrq',
							renderer : function(value, metadata, record, rowIndex, colIndex, store) {
								var dt = formatDate(new Date(value['time']), "yyyy-MM-dd");
								return dt;
							}
						}, {
							id : 'jsrq',
							header : "结束日期",
							width : 100,
							sortable : true,
							dataIndex : 'jsrq',
							renderer : function(value, metadata, record, rowIndex, colIndex, store) {
								var dt = formatDate(new Date(value['time']), "yyyy-MM-dd");
								return dt;
							}
						}, {
							id : 'rgfy',
							header : "人工费用(万)",
							width : 100,
							sortable : true,
							dataIndex : 'rgfy'
						}, {
							id : 'clfy',
							header : "材料费用(万)",
							width : 100,
							sortable : true,
							dataIndex : 'clfy'
						}, {
							id : 'nrgy',
							header : "内容概要",
							width : 230,
							sortable : true,
							dataIndex : 'nrgy',
							renderer : function(value, metadata, record, rowIndex, columnIndex, store) {
								metadata.attr = 'ext:qtip="' + value + '"';
								return value;
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
