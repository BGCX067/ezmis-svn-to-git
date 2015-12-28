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

	this.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
		var select=grid.getSelectionModel().getSelections()[0];
		var id = select.get("ID");
		var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn=" + "TB_FORM_SJBZB_BKHDLYTJ" 
		+ "&st=02&docid="+id;
		var result = showModule(url, "yes", 800, 600, null, "", "");
	});
	// 当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange", function(oCheckboxSModel) {
		var btnDel = mainToolbar.items.get('btnDel');
		var btnMod = mainToolbar.items.get('btnMod');
		
		if (oCheckboxSModel.getSelections().length < 1) {
			if(btnDel){
				btnDel.setDisabled(true);
			}
			if(btnMod){
				btnMod.setDisabled(true);
			}
		} else {
			if (oCheckboxSModel.getSelections().length == 1) {
				if(btnDel){
					btnDel.setDisabled(false);
				}
				if(btnMod){
					btnMod.setDisabled(false);
				}
			} else {
				if(btnDel){
					btnDel.setDisabled(false);
				}
				if(btnMod){
					btnMod.setDisabled(true);
				}
			}
		}
	});

}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
	sm:new Ext.grid.CheckboxSelectionModel(),

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
			}, ["ID","KSRQ","JSRQ","TJR"]),
			remoteSort : true
		});
		return ds;
	},

	/**
	 * PersonAction 列模型
	 */
	getColumnModel : function() {
		var cm = new Ext.grid.ColumnModel([this.sm, {
			id : 'TXR',
			header : "填报人",
			width : 120,
			sortable : true,
			dataIndex : 'TJR'
		},{
			id : 'KSRQ',
			header : "统计开始时间",
			width : 120,
			sortable : true,
			dataIndex : 'KSRQ'
		},{
			id : 'JSRQ',
			header : "统计结束时间",
			width : 120,
			sortable : true,
			dataIndex : 'JSRQ'
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
