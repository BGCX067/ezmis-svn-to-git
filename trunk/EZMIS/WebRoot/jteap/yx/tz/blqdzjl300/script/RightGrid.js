/**
 * 字段列表
 */
RightGrid=function(){
	
    var defaultDs=this.getDefaultDS(link1);
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'导出Excel',handler:function(){
		exportExcel(grid,true);
		}}, '-', '<font color="red">*双击查看详细信息</font>']
	});
	RightGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(),
		sm: this.sm,
	    margins:'2px 2px 2px 2px',
		width:600,
		height:300,
		loadMask: true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar
	});	
	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		var btnDel=mainToolbar.items.get('btnDel');
		var btnModi=mainToolbar.items.get('btnModi');
		
		if(oCheckboxSModel.getSelections().length == 1){
			if(btnModi) btnModi.setDisabled(false);
		}else{
			if(btnModi) btnModi.setDisabled(true);
		}
		
		if(oCheckboxSModel.getSelections().length < 1){
			if(btnDel) btnDel.setDisabled(true);
		}else{
			if(btnDel) btnDel.setDisabled(false);
		}
	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=grid.getSelectionModel().getSelections()[0];
		var obj = {};
		obj.grid = grid;
		obj.select = select;
				
		var url = contextPath + '/jteap/yx/tz/blqdzjl300/editForm.jsp?query=q';
		showIFModule(url,"查看记录","true",600,500,obj);
	});
	
}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
	
	sm:new Ext.grid.CheckboxSelectionModel(),
	
	/**
	 * 取得默认数据源
	 * 返回数据格式
	 * {success:true,totalCount:20,list:[{id:'123',field1:'111',...,field5'222'},{...},...]} 
	 */
	getDefaultDS:function(url){
	    var ds = new Ext.data.Store({
	        proxy: new Ext.data.ScriptTagProxy({
	            url: url
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount',
	            id: 'id'
	        }, [
				"id","tianXieRen","tianXieShiJian","cbr","cbsj","zbgyc_a1","zbgyc_b1","zbgyc_c1","zbgyczxd_1","zbgyc_a2",
				"zbgyc_b2","zbgyc_c2","zbgyczxd_2","qbbgyc_a01","qbbgyc_b01","qbbgyc_c01","mx_a1","mx_b1","mx_c1","mx_a2","mx_b2","mx_c2"
	        ]),
	        remoteSort: true,
	        //如果为true,每次store在加载时或者一条记录被删除时, 清除所有改动过的记录信息
	        pruneModifiedRecords: true
	    });
		return ds;
	},
    
	/**
	 * DqgzAction 列模型
	 */
	getColumnModel:function(){
		var grid = this;
				
	    var cm=new Ext.grid.ColumnModel([
		    	this.sm,
				{id:'id',header: "id", width: 100, sortable: true, hidden:true, dataIndex: 'id'},
				{id:'cbr',header: "检查人", width: 100, sortable: true, dataIndex: 'cbr'},
				{id:'cbsj',header: "检查时间", width: 100, sortable: true, dataIndex: 'cbsj'},
				{id:'tianXieShiJian',header: "填写时间", width: 110, sortable: true, hidden:true, dataIndex: 'tianXieShiJian'},
				{id:'tianXieRen',header: "填写人", width: 100, sortable: true, hidden:true, dataIndex: 'tianXieRen'},
				{id:'zbgyc_a1',header: "#1主变高压侧A相", width: 110, sortable: true, dataIndex: 'zbgyc_a1'},
				{id:'zbgyc_b1',header: "#1主变高压侧B相", width: 110, sortable: true, dataIndex: 'zbgyc_b1'},
				{id:'zbgyc_c1',header: "#1主变高压侧C相", width: 110, sortable: true, dataIndex: 'zbgyc_c1'},
				{id:'zbgyczxd_1',header: "#1主变高压侧中性点", width: 120, sortable: true, dataIndex: 'zbgyczxd_1'},
				{id:'zbgyc_a2',header: "#2主变高压侧A相", width: 110, sortable: true, dataIndex: 'zbgyc_a2'},
				{id:'zbgyc_b2',header: "#2主变高压侧B相", width: 110, sortable: true, dataIndex: 'zbgyc_b2'},
				{id:'zbgyc_c2',header: "#2主变高压侧C相", width: 110, sortable: true, dataIndex: 'zbgyc_c2'},
				{id:'zbgyczxd_2',header: "#2主变高压侧中性点", width: 120, sortable: true, dataIndex: 'zbgyczxd_2'},
				{id:'qbbgyc_a01',header: "#01启备变高压侧A相", width: 120, sortable: true, dataIndex: 'qbbgyc_a01'},
				{id:'qbbgyc_b01',header: "#01启备变高压侧B相", width: 120, sortable: true, dataIndex: 'qbbgyc_b01'},
				{id:'qbbgyc_c01',header: "#01启备变高压侧B相", width: 120, sortable: true, dataIndex: 'qbbgyc_c01'},
				{id:'mx_a1',header: "220KV #1母线A相", width: 110, sortable: true, dataIndex: 'mx_a1'},
				{id:'mx_b1',header: "220KV #1母线B相", width: 110, sortable: true, dataIndex: 'mx_b1'},
				{id:'mx_c1',header: "220KV #1母线C相", width: 110, sortable: true, dataIndex: 'mx_c1'},
				{id:'mx_a2',header: "220KV #2母线A相", width: 110, sortable: true, dataIndex: 'mx_a2'},
				{id:'mx_b2',header: "220KV #2母线B相", width: 110, sortable: true, dataIndex: 'mx_b2'},
				{id:'mx_c2',header: "220KV #2母线C相", width: 110, sortable: true, dataIndex: 'mx_c2'}
			]);
		return cm;
	},
	/**
	 * 切换数据源->LogAction!showList
	 */
	changeToListDS:function(url){
		var ds = this.getDefaultDS(url);	
		var cm=this.getColumnModel();
		this.pageToolbar.bind(ds);
		this.reconfigure(ds,cm);
	},
	/**
	 * 删除
	 */
	deleteSelect:function(select){
		var selections = this.getSelections();//获取被选中的行
		var rightGrid=this;
		var ids="";
		Ext.each(selections,function(selectedobj){
			ids+=selectedobj.id+",";//取得他们的id并组装
		});
		if(window.confirm("确认删除选中的条目吗？")){
			Ext.Ajax.request({
				url:link2,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			alert("删除成功");
			 			rightGrid.getStore().reload();
			 		}else{
			 			alert(responseObject.msg);
			 		}				
				},
			 	failure:function(){
			 		alert("提交失败");
			 	},
			 	method:'POST',
			 	params: {ids:ids}//Ext.util.JSON.encode(selections.keys)			
			});
		}
	}
	
});