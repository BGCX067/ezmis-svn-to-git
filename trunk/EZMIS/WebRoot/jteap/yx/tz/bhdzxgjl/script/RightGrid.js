
/**
 * 字段列表
 */
RightGrid = function() {
	var url = link1;
	var defaultDs = this.getDefaultDS(url);
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
		tbar : mainToolbar,
		items : this.pageToolbar
	});

	// 当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange", function(oCheckboxSModel) {
		var btnModi = mainToolbar.items.get('btnModi');
		var btnDel = mainToolbar.items.get('btnDel');

		if (oCheckboxSModel.getSelections().length < 1) {
			if (btnModi)
				btnModi.setDisabled(true);
			if (btnDel)
				btnDel.setDisabled(true);
		} else {
			if (btnDel)
				btnDel.setDisabled(false);
			if (oCheckboxSModel.getSelections().length == 1) {
				if (btnModi)
					btnModi.setDisabled(false);
			} else {
				if (btnModi)
					btnModi.setDisabled(true);
			}
		}
	});

	this.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
		var record = grid.getSelectionModel().getSelected();
		var url = link3 + "?id=" + record.get('id');
		var sFeature = "dialogWidth:760px;dialogHeight:550px;center:true;status:0;help:0;";
		showModule(url, true, "", "", "", "", "", sFeature);
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
				id : 'id'
			}, ["id", "bhmc", "ggsj", "ggyy", "ggflr", "ggzhr", "ggyhjc"]),
			remoteSort : true
		});
		return ds;
	},

	/**
	 * PersonAction 列模型
	 */
	getColumnModel : function() {

		var cm = new Ext.grid.ColumnModel([this.sm, {
			id : 'bhmc',
			header : "设备名称",
			width : 120,
			sortable : true,
			dataIndex : 'bhmc'
		}, {
			id : 'ggsj',
			header : "更改时间",
			width : 140,
			sortable : true,
			dataIndex : 'ggsj',
			renderer : function(value, metadata, record, rowIndex, colIndex, store) {
				if (value) {
					var dt = formatDate(new Date(value["time"]), "yyyy-MM-dd hh:mm");
					return dt;
				} else {
					return "";
				}
			}
		}, {
			id : 'ggyy',
			header : "更改原因",
			width : 140,
			sortable : true,
			dataIndex : 'ggyy',
			renderer : function(value, metadata, record, rowIndex, columnIndex, store) {
				metadata.attr = 'ext:qtip="' + value + '"';
				return value;
			}
		}, {
			id : 'ggflr',
			header : "更改发令人",
			width : 140,
			sortable : true,
			dataIndex : 'ggflr'
		}, {
			id : 'ggzhr',
			header : "执行人",
			width : 140,
			sortable : true,
			dataIndex : 'ggzhr'
		}, {
			id : 'ggyhjc',
			header : "更改运行检查",
			width : 140,
			sortable : true,
			dataIndex : 'ggyhjc'
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
