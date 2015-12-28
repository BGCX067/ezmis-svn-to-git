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
		items : ['-', '<font color="red">*双击查看详细信息</font>']
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
		var btnMakeBz = mainToolbar.items.get('btnMakeBz');
		var btnModiPz = mainToolbar.items.get('btnModiPz');
		var btnDelPz = mainToolbar.items.get('btnDelPz');

		if (oCheckboxSModel.getSelections().length == 1) {
			if (btnModiPz)
				btnModiPz.setDisabled(false);
		} else {
			if (btnModiPz)
				btnModiPz.setDisabled(true);
		}

		if (oCheckboxSModel.getSelections().length <= 0) {
			if (btnMakeBz)
				btnMakeBz.setDisabled(true);
			if (btnDelPz)
				btnDelPz.setDisabled(true);
		} else {
			if (btnMakeBz)
				btnMakeBz.setDisabled(false);
			if (btnDelPz)
				btnDelPz.setDisabled(false);
		}

	});

	this.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
		var record = grid.getSelectionModel().getSelected();
		var id = record.get('id');
		var url = link7 + "?id=" + id;
		var retValue = showModule(url, true, 590, 320);
	});

}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
	sm : new Ext.grid.CheckboxSelectionModel(),

	/**
	 * 取得默认数据源 返回数据格式
	 * {success:true,totalCount:20,list:[{id:'123',field1:'111',...,field5'222'},{...},...]}
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
			}, ["id", "pfl", "pmc", "cjbm", "cjr", "cjsj", "pzt", "isBzp", "gllch", "bzsm"]),
			remoteSort : true
		});
		return ds;
	},

	/**
	 * PersonAction 列模型
	 */
	getColumnModel : function() {
		var cm = new Ext.grid.ColumnModel([this.sm, {
			id : 'pfl',
			header : "票分类",
			width : 100,
			sortable : true,
			dataIndex : 'pfl',
			renderer : function(value, metadata, record, rowIndex, colIndex, store) {
				if (value == "gzp") {
					return "工作票";
				} else if (value == "czp") {
					return "操作票"
				} else if(value=="lsp"){
					return "临时票"
				}
			}
		}, {
			id : 'pmc',
			header : "票名称",
			width : 250,
			sortable : true,
			dataIndex : 'pmc'
		}, {
			id : 'cjbm',
			header : "创建部门",
			width : 100,
			sortable : true,
			dataIndex : 'cjbm'
		}, {
			id : 'cjr',
			header : "创建人",
			width : 80,
			sortable : true,
			dataIndex : 'cjr'
		}, {
			id : 'cjsj',
			header : "创建时间",
			width : 140,
			sortable : true,
			dataIndex : 'cjsj',
			renderer : function(value, metadata, record, rowIndex, colIndex, store) {
				var cjsj = (value == null) ? "" : formatDate(new Date(value["time"]), "yyyy-MM-dd");
				return cjsj;
			}
		}, {
			id : 'pzt',
			header : "票状态",
			width : 80,
			sortable : true,
			dataIndex : 'pzt',
			renderer : function(value, metadata, record, rowIndex, colIndex, store) {
				if (value == "1") {
					return "有效";
				} else if (value == "2") {
					return "无效"
				}
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
	// 删除选中的日志
		delSelected : function() {
			var selections = this.getSelections();
			var ids = "";
			var grid = this;
			Ext.each(selections, function(selectedobj) {
				ids += selectedobj.data.id + ",";
			});
			if (window.confirm("确认删除吗")) {
				Ext.Ajax.request({
					url : link8,
					success : function(ajax) {
						var responseText = ajax.responseText;
						var responseObject = Ext.util.JSON.decode(responseText);
						if (responseObject.success) {
							alert("删除成功");
							grid.getStore().reload();
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
