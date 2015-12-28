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
				
		var url = contextPath + '/jteap/yx/tz/cydtj600/editForm.jsp?query=q';
		showIFModule(url,"查看记录","true",600,285,obj);
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
				"id","tianXieRen","tianXieShiJian","zb","cbr","cbsj","fdjyg_3","gcb_3","fdjyg_4","gcb_4","lcb_3","lcb_4","qbb_2"
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
				{id:'zb',header: "值别", width: 100, sortable: true, dataIndex: 'zb'},
				{id:'cbr',header: "抄表人", width: 100, sortable: true, dataIndex: 'cbr'},
				{id:'cbsj',header: "抄表时间", width: 110, sortable: true, dataIndex: 'cbsj'},
				{id:'tianXieShiJian',header: "填写时间", width: 110, sortable: true, hidden:true,  dataIndex: 'tianXieShiJian'},
				{id:'tianXieRen',header: "填写人", width: 100, sortable: true, hidden:true,  dataIndex: 'tianXieRen'},
				{id:'qbb_2',header: "#02启备变", width: 100, sortable: true, dataIndex: 'qbb_2'},
				{id:'fdjyg_3',header: "#3发电机有功", width: 100, sortable: true, dataIndex: 'fdjyg_3'},
				{id:'gcb_3',header: "#3高厂变", width: 100, sortable: true, dataIndex: 'gcb_3'},
				{id:'fdjyg_4',header: "#4发电机有功", width: 100, sortable: true, dataIndex: 'fdjyg_4'},
				{id:'gcb_4',header: "#4高厂变", width: 100, sortable: true, dataIndex: 'gcb_4'},
				{id:'lcb_3',header: "#3励磁变", width: 100, sortable: true, dataIndex: 'lcb_3'},
				{id:'lcb_4',header: "#4励磁变", width: 100, sortable: true, dataIndex: 'lcb_4'}
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