
/**
 * 字段列表
 */
RightGrid = function() {
	var defaultDs = this.getDefaultDS(link1);
	var grid = this;
	this.pageToolbar = new Ext.PagingToolbar({
		pageSize : CONSTANTS_PAGE_DEFAULT_LIMIT,
		store : defaultDs,
		displayInfo : true,
		displayMsg : '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg : "没有符合条件的数据"
	});
	var bbar = new Ext.Toolbar({
		items : ['->',{
			text : '确定',
			handler : function() {
				var record = grid.getSelectionModel().getSelected();
				if (!record) {
					alert('请选中一条招标信息！');
					return;
				}
				var obj = {};
				obj.id = record.get('id');
				obj.zbbh = record.get('zbbh');
				obj.xmmc = record.get('xmmc');
				obj.zbpc = record.get('zbpc');
				obj.zbfs = record.get('zbfs');
				obj.jhzbsj = record.get('jhzbsj');
				obj.sjzbsj = record.get('sjzbsj');
				obj.zbdz = record.get('zbdz');
				obj.zbnr = record.get('zbnr');
				obj.zbfl = record.get('zbfl');
				obj.zbflid = record.get('zbflid');
				window.returnValue = obj;
				window.close();
			}
		}, {
			text : '关闭',
			handler : function() {
				window.close();
			}
		}]
	})
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
		tbar : this.pageToolbar,
		bbar : bbar
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
			}, ["id", "zbbh", "xmmc", "zbfs", "zbpc", "jhzbsj", "sjzbsj", "zbdz", "zbnr", "zbfl", "zbflid"]),
			remoteSort : true
		});
		return ds;
	},

	/**
	 * PersonAction 列模型
	 */
	getColumnModel : function() {
		var cm = new Ext.grid.ColumnModel([this.sm, {
			id : 'xmmc',
			header : "项目名称",
			width : 100,
			sortable : true,
			dataIndex : 'xmmc'
		}, {
			id : 'zbpc',
			header : "招标批次",
			width : 80,
			sortable : true,
			dataIndex : 'zbpc'
		}, {
			id : 'zbfs',
			header : "招标方式",
			width : 80,
			sortable : true,
			dataIndex : 'zbfs'
		}, {
			id : 'zbfl',
			header : "招标类别",
			width : 80,
			sortable : true,
			dataIndex : 'zbfl'
		}, {
			id : 'zbdz',
			header : "招标地址",
			width : 100,
			sortable : true,
			dataIndex : 'zbdz',
			renderer : function(value, metadata, record, rowIndex, columnIndex, store) {
				metadata.attr = 'ext:qtip="' + value + '"';
				return value;
			}
		}, {
			id : 'jhzbsj',
			header : "计划招标时间",
			width : 80,
			sortable : true,
			dataIndex : 'jhzbsj'
		}, {
			id : 'sjzbsj',
			header : "实际招标时间",
			width : 80,
			sortable : true,
			dataIndex : 'sjzbsj'
		}, {
			id : 'zbnr',
			header : "招标内容",
			width : 140,
			sortable : true,
			dataIndex : 'zbnr',
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
