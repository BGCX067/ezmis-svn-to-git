/**
 * 字段列表
 */
JzjxxmGrid = function(jhId) {
	var defaultDs = this.getDefaultDS(link3+"?jhId="+jhId);
	defaultDs.load();
	var grid = this;
	this.pageToolbar = new Ext.PagingToolbar({
				pageSize : CONSTANTS_PAGE_DEFAULT_LIMIT,
				store : defaultDs,
				displayInfo : true,
				displayMsg : '共{2}条数据，目前为 {0} - {1} 条',
				emptyMsg : "没有符合条件的数据"
			});
	JzjxxmGrid.superclass.constructor.call(this, {
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

}
Ext.extend(JzjxxmGrid, Ext.grid.GridPanel, {
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
									}, ["id", "sszy", "xmmc", "jlr", "jlsj", "remark", "jzjxjh"]),
							remoteSort : true
						});
				return ds;
			},

			/**
			 * PersonAction 列模型
			 */
			getColumnModel : function() {
				var cm = new Ext.grid.ColumnModel([this.sm, {
							id : 'sszy',
							header : "所属专业",
							width : 80,
							sortable : true,
							dataIndex : 'sszy'
						}, {
							id : 'xmmc',
							header : "项目名称",
							width : 160,
							sortable : true,
							dataIndex : 'xmmc',
							renderer : function(value, metadata, record, rowIndex, columnIndex, store) {
								metadata.attr = 'ext:qtip="' + value + '"';
								return value;
							}
						}, {
							id : 'jlr',
							header : "记录人",
							width : 120,
							sortable : true,
							dataIndex : 'jlr'
						}, {
							id : 'jlsj',
							header : "记录时间",
							width : 160,
							sortable : true,
							dataIndex : 'jlsj',
							renderer : function(value, metadata, record, rowIndex, colIndex, store) {
								var dt = formatDate(new Date(value['time']), "yyyy-MM-dd hh:mm:ss");
								return dt;
							}
						}, {
							id : 'remark',
							header : "备注说明",
							width : 200,
							sortable : true,
							dataIndex : 'remark',
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
