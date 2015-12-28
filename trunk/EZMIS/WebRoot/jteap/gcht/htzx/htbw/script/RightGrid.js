
/**
 * 字段列表
 */
RightGrid = function() {
	var defaultDs = this.getDefaultDS("");
	var grid = this;
	this.pageToolbar = new Ext.PagingToolbar({
		pageSize : CONSTANTS_PAGE_DEFAULT_LIMIT,
		store : defaultDs,
		displayInfo : true,
		displayMsg : '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg : "没有符合条件的数据"
	});
	RightGrid.superclass.constructor.call(this, {
		ds : defaultDs,
		cm : this.getColumnModel(),
		sm : this.sm,
	    view: new Ext.grid.GroupingView({
            forceFit:true,
            groupTextTpl: '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "Items" : "Item"]})'
        }),
		margins : '2px 2px 2px 2px',
		width : 600,
		height : 300,
		loadMask : true,
		region : 'center',
		tbar : this.pageToolbar
	});

	// 当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange", function(oCheckboxSModel) {
		var btnModi=mainToolbar.items.get('btnModi');
		var btnDel=mainToolbar.items.get('btnDel');
		
		if(oCheckboxSModel.getSelections().length < 1){
			btnModi.setDisabled(true);
			btnDel.setDisabled(true);
		}else{
			btnDel.setDisabled(false);
			if(oCheckboxSModel.getSelections().length == 1){
				btnModi.setDisabled(false);
			}else{
				btnModi.setDisabled(true);
			}
		}
	});
	
}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
	sm : new Ext.grid.CheckboxSelectionModel(),

	/**
	 * 取得默认数据源 返回数据格式
	 */
	getDefaultDS : function(url) {
		var procFields = ["HTID","HTMC","HTBH","BWID","BWNR","BWSJ"];
		
		var ds = new Ext.data.GroupingStore({
				proxy : new Ext.data.ScriptTagProxy({
							url : url
						}),
				reader : new Ext.data.JsonReader({
							root : 'list',
							totalProperty : 'totalCount',
							id : 'BWID'
						}, procFields),
				sortInfo:{field: 'HTCJSJ', direction: "DESC"},
				groupField:'HTMC',
				remoteSort : true
		});
		/*var dummyData = [
		    ['HTID','物资合同1','BWID','备忘内容1','备忘时间1'],
		    ['HTID','物资合同2','BWID','备忘内容2','备忘时间2'],
		    ['HTID','物资合同1','BWID','备忘内容2','备忘时间2'],
		    ['HTID','燃料合同1','BWID','备忘内容1','备忘时间1'],
		    ['HTID','工程合同1','BWID','备忘内容1','备忘时间1']
		];
		var reader = new Ext.data.ArrayReader({}, [
	       {name: 'HTID'},
	       {name: 'HTMC'},
	       {name: 'HTCJSJ'},
	       {name: 'BWID'},
	       {name: 'BWNR'},
	       {name: 'BWSJ'}
	    ]);
	    var ds = new Ext.data.GroupingStore({
	    	reader: reader,
            data: dummyData,
            sortInfo:{field: 'HTCJSJ', direction: "DESC"},
            groupField:'HTMC'
        });*/
		return ds;
	},

	/**
	 * 列模型
	 */
	getColumnModel : function() {
		var cm = new Ext.grid.ColumnModel([
			this.sm, 
			{id : 'HTID',header : "ID",width : 100, hidden:true, sortable : true,dataIndex : 'HTID'},
			{id : 'HTMC',header : "合同名称",width : 100, sortable : true,dataIndex : 'HTMC'},
			{id : 'HTBH',header : "合同编号",width : 100, sortable : true,dataIndex : 'HTBH'},
			{id : 'BWID',header : "备忘编号",width : 100, hidden:true, sortable : true,dataIndex : 'BWID'},
			{id : 'BWNR',header : "备忘内容",width : 150,sortable : true,dataIndex : 'BWNR',
				renderer:function(value,metadata,record,rowIndex,colIndex,store){
			    		return "<span ext:qtip='"+value+"'>"+Ext.util.Format.htmlEncode(value)+"</span>"
		    	}
		    }, 
			{id : 'BWSJ',header : "备忘时间",width : 100,sortable : true,dataIndex : 'BWSJ'}
		]);
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
