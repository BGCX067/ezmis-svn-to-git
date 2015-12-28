/**
 * 字段列表
 */
RightGrid = function() {
	var defaultDs = this.getDefaultDS(link1);
	var grid = this;
	var items = ['-', 
				{
					text : '导出Excel',
					handler : function() {
						exportSelectedExcel(grid);
					}
				},
				'-', '<font color="red">*双击查看详细信息</font>'];
	if(chooseFlbm != "null"){
		items = ['-','<font color="red">*双击选择</font>'];
	}
	this.pageToolbar = new Ext.PagingToolbar({
		pageSize : 20,
		store : defaultDs,
		displayInfo : true,
		displayMsg : '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg : "没有符合条件的数据",
		items : items
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
		var btnModifyGys = mainToolbar.items.get('btnModifyGys');
		var btnDelGys = mainToolbar.items.get('btnDelGys');

		if (oCheckboxSModel.getSelections().length < 1) {
			if (btnModifyGys) 
				btnModifyGys.setDisabled(true);
			if (btnDelGys)
				btnDelGys.setDisabled(true);
		} else {
			if (btnDelGys)
				btnDelGys.setDisabled(false);
			if (oCheckboxSModel.getSelections().length == 1) {
				if (btnModifyGys)
					btnModifyGys.setDisabled(false);
			} else {
				if (btnModifyGys)
					btnModifyGys.setDisabled(true);
			}
		}
	});

	this.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
		var select = rightGrid.getSelectionModel().getSelections()[0];
		var id = select.get("id");
		
		if(chooseFlbm != "null"){
			var obj = {};
			obj.id = id;
			obj.gysmc = select.data.gysmc;
			obj.frdb = select.data.frdb;
			obj.qylx = select.data.qylx;
			obj.catalogId = select.data.catalogId;
			obj.gyslb = select.data.gyslb;
			obj.swlxr = select.data.swlxr;
			obj.swlxrdh = select.data.swlxrdh;
			obj.gysdz = select.data.gysdz;
			obj.khyh = select.data.khyh;
			obj.yhzh = select.data.yhzh;
			obj.yffzr =  select.data.yffzr;
			obj.yfsgfzr =  select.data.yfsgfzr;
			obj.yffzrlxfs =  select.data.yffzrlxfs;
			obj.yfsgfzrlxfs =  select.data.yfsgfzrlxfs;
			window.returnValue = obj;
			window.close();
		}else{
			var url = "/jteap/form/eform/eformRec.jsp?formSn=TB_HT_GYSXX&docid="+id+"&st=02";
			var myTitle = "供应商信息";
			showIFModule(CONTEXT_PATH + url, myTitle, "true", 800, 600, {});
			rightGrid.getStore().reload();
		}
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
			}, [ "id", "gysmc", "frdb", "qylx", "catalogId", "gyslb", "swlxr", "swlxrdh", "gysdz", "khyh", "yhzh","yffzr","yffzrlxfs","yfsgfzr","yfsgfzrlxfs"]),
			remoteSort : true
		});
		return ds;
	},

	/**
	 * PersonAction 列模型
	 */
	getColumnModel : function() {
		var cm = new Ext.grid.ColumnModel([this.sm, {
			id : 'gysmc',
			header : "供应商名称",
			width : 100,
			sortable : true,
			dataIndex : 'gysmc'
		}, {
			id : 'frdb',
			header : "法人代表",
			width : 80,
			sortable : true,
			dataIndex : 'frdb'
		}, {
			id : 'qylx',
			header : "供应商类型",
			width : 80,
			sortable : true,
			dataIndex : 'qylx'
		}, {
			id : 'gyslb',
			header : "供应商类别",
			width : 140,
			sortable : true,
			dataIndex : 'gyslb'
		}, {
			id : 'swlxr',
			header : "商务联系人",
			width : 100,
			sortable : true,
			dataIndex : 'swlxr'
		} ,{
			id : 'yffzr',
			header : "乙方负责人",
			width : 100,
			sortable : true,
			dataIndex : 'yffzr'
		} ,{
			id : 'yffzrlxfs',
			header : "乙方负责人联系方式",
			width : 100,
			sortable : true,
			dataIndex : 'yffzrlxfs'
		} ,{
			id : 'yfsgfzr',
			header : "乙方施工负责人",
			width : 100,
			sortable : true,
			dataIndex : 'yfsgfzr'
		} ,{
			id : 'yfsgfzrlxfs',
			header : "乙方施工负责人联系方式",
			width : 100,
			sortable : true,
			dataIndex : 'yfsgfzrlxfs'
		},{
			id : 'gysdz',
			header : "地址",
			width : 140,
			sortable : true,
			dataIndex : 'gysdz'
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
