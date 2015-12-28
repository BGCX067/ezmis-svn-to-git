/**
 * 字段列表
 */
RightGrid = function() {
	var defaultDs = this.getDefaultDS(link1+'?type='+type);
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

	// 当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange", function(oCheckboxSModel) {
		var btnModi = mainToolbar.items.get('btnModi');
		var btnDel = mainToolbar.items.get('btnDel');
		var btnModiAll = mainToolbar.items.get('btnModiAll');

		if (oCheckboxSModel.getSelections().length == 1) {
			if (btnModi)
				btnModi.setDisabled(false);
			if (btnModiAll)
				btnModiAll.setDisabled(false);
		} else {
			if (btnModi)
				btnModi.setDisabled(true);
			if (btnModiAll)
				btnModiAll.setDisabled(true);
		}

		if (oCheckboxSModel.getSelections().length <= 0) {
			if (btnDel)
				btnDel.setDisabled(true);
		} else {
			if (btnDel)
				btnDel.setDisabled(false);
		}
	});

	this.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
		var record = grid.getSelectionModel().getSelected();
		var url = link3 + "?id=" + record.get('id');
		showModule(url, true, 680, 600);
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
			}, ["id", "gzpbh", "gzpzt", "ddjrw", "gzfzr", "gzbry", "gzpdjr", "spsj", "xksj", "gzpzjr", "zjsj",
					"gzpzfr", "zfsj", "zjjd", "spr", "pzgzqx", "pzzz", "xkr", "yqsj", "pzyqzz", "yqsxsj", "zfyy",
					"zjjcqk", "pzzzmc", "xkrmc", "zjrmc", "pzyqzzmc", "zfrmc"]),
			remoteSort : true
		});
		return ds;
	},

	/**
	 * PersonAction 列模型
	 */
	getColumnModel : function() {
		var cm = new Ext.grid.ColumnModel([this.sm, {
			id : 'gzpbh',
			header : "工作票编号",
			width : 150,
			sortable : true,
			dataIndex : 'gzpbh'
		}, {
			id : 'gzpzt',
			header : "工作票状态",
			width : 70,
			sortable : true,
			dataIndex : 'gzpzt'
		}, {
			id : 'ddjrw',
			header : "地点及任务",
			width : 140,
			sortable : true,
			dataIndex : 'ddjrw',
			renderer : function(value, metadata, record, rowIndex, colIndex, store) {
				metadata.attr = 'ext:qtip="' + value + '"';
				return value;
			}
		}, {
			id : 'gzfzr',
			header : "工作负责人",
			width : 80,
			sortable : true,
			dataIndex : 'gzfzr'
		}, {
			id : 'gzbry',
			header : "工作班人员",
			width : 80,
			sortable : true,
			dataIndex : 'gzbry',
			renderer : function(value, metadata, record, rowIndex, colIndex, store) {
				metadata.attr = 'ext:qtip="' + value + '"';
				return value;
			}
		}, {
			id : 'gzpdjr',
			header : "登记人",
			width : 80,
			sortable : true,
			dataIndex : 'gzpdjr'
		}, {
			id : 'spsj',
			header : "收票时间",
			width : 120,
			sortable : true,
			dataIndex : 'spsj',
			renderer : function(value, metadata, record, rowIndex, colIndex, store) {
				if (value) {
					var dt = formatDate(new Date(value["time"]), "yyyy-MM-dd hh:mm");
					return dt;
				} else {
					return "";
				}
			}
		}, {
			id : 'spr',
			header : "收票人",
			width : 80,
			sortable : true,
			dataIndex : 'spr'
		}, {
			id : 'pzgzqx',
			header : "批准工作期限",
			width : 120,
			sortable : true,
			dataIndex : 'pzgzqx',
			renderer : function(value, metadata, record, rowIndex, colIndex, store) {
				if (value) {
					var dt = formatDate(new Date(value["time"]), "yyyy-MM-dd hh:mm");
					return dt;
				} else {
					return "";
				}
			}
		}, {
			id : 'pzzzmc',
			header : "批准值长",
			width : 80,
			sortable : true,
			dataIndex : 'pzzzmc'
		}, {
			id : 'xksj',
			header : "许可开工时间",
			width : 120,
			sortable : true,
			dataIndex : 'xksj',
			renderer : function(value, metadata, record, rowIndex, colIndex, store) {
				if (value) {
					var dt = formatDate(new Date(value["time"]), "yyyy-MM-dd hh:mm");
					return dt;
				} else {
					return "";
				}
			}
		}, {
			id : 'xkrmc',
			header : "许可人",
			width : 80,
			sortable : true,
			dataIndex : 'xkrmc'
		}, {
			id : 'yqsj',
			header : "延期期限",
			width : 120,
			sortable : true,
			dataIndex : 'yqsj',
			renderer : function(value, metadata, record, rowIndex, colIndex, store) {
				if (value) {
					var dt = formatDate(new Date(value["time"]), "yyyy-MM-dd hh:mm");
					return dt;
				} else {
					return "";
				}
			}
		}, {
			id : 'pzyqzzmc',
			header : "批准延期值长",
			width : 80,
			sortable : true,
			dataIndex : 'pzyqzzmc'
		}, {
			id : 'yqsxsj',
			header : "延期手续时间",
			width : 120,
			sortable : true,
			dataIndex : 'yqsxsj',
			renderer : function(value, metadata, record, rowIndex, colIndex, store) {
				if (value) {
					var dt = formatDate(new Date(value["time"]), "yyyy-MM-dd hh:mm");
					return dt;
				} else {
					return "";
				}
			}
		}, {
			id : 'zjsj',
			header : "终结时间",
			width : 120,
			sortable : true,
			dataIndex : 'zjsj',
			renderer : function(value, metadata, record, rowIndex, colIndex, store) {
				if (value) {
					var dt = formatDate(new Date(value["time"]), "yyyy-MM-dd hh:mm");
					return dt;
				} else {
					return "";
				}
			}
		}, {
			id : 'zjrmc',
			header : "终结人",
			width : 80,
			sortable : true,
			dataIndex : 'zjrmc'
		}, {
			id : 'zfsj',
			header : "作废时间",
			width : 120,
			sortable : true,
			dataIndex : 'zfsj',
			renderer : function(value, metadata, record, rowIndex, colIndex, store) {
				if (value) {
					var dt = formatDate(new Date(value["time"]), "yyyy-MM-dd hh:mm");
					return dt;
				} else {
					return "";
				}
			}
		}, {
			id : 'zfrmc',
			header : "作废人",
			width : 80,
			sortable : true,
			dataIndex : 'zfrmc'
		}, {
			id : 'zfyy',
			header : "作废原因",
			width : 140,
			sortable : true,
			dataIndex : 'zfyy',
			renderer : function(value, metadata, record, rowIndex, colIndex, store) {
				metadata.attr = 'ext:qtip="' + value + '"';
				return value;
			}
		}, {
			id : 'zjjd',
			header : "终结交代",
			width : 140,
			sortable : true,
			dataIndex : 'zjjd',
			renderer : function(value, metadata, record, rowIndex, colIndex, store) {
				metadata.attr = 'ext:qtip="' + value + '"';
				return value;
			}
		}, {
			id : 'zjjcqk',
			header : "终结检查情况",
			width : 140,
			sortable : true,
			dataIndex : 'zjjcqk',
			renderer : function(value, metadata, record, rowIndex, colIndex, store) {
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
	},
	/**
	 * 删除
	 */
	deleteSelect : function(select) {
		var selections = this.getSelections();// 获取被选中的行
		var rightGrid = this;
		var ids = "";
		Ext.each(selections, function(selectedobj) {
			ids += selectedobj.id + ",";// 取得他们的id并组装
			});
		if (window.confirm("确认删除选中的条目吗？")) {
			Ext.Ajax.request({
				url : link4,
				success : function(ajax) {
					var responseText = ajax.responseText;
					var responseObject = Ext.util.JSON.decode(responseText);
					if (responseObject.success) {
						alert("删除成功");
						rightGrid.getStore().reload();
					} else {
						alert(responseObject.msg);
					}
				},
				failure : function() {
					alert("提交失败");
				},
				method : 'POST',
				params : {
					ids : ids
				}
			});
		}
	}

});
