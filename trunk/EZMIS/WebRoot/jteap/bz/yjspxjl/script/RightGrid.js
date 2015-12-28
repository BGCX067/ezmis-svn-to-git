
/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link1);
    defaultDs.load();
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'导出Excel',handler:function(){
		exportExcel(grid,true);
		}},'-','<font color="red">*双击查看详细信息</font>']
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
		var btnModify=mainToolbar.items.get('btnModify');
		
		if(oCheckboxSModel.getSelections().length==1){
			if(btnModify) btnModify.setDisabled(false);
		}else{
			if(btnModify) btnModify.setDisabled(true);
		}
		
		if(oCheckboxSModel.getSelections().length <= 0){
			if(btnDel) btnDel.setDisabled(true);
		}else{
			if(btnDel) btnDel.setDisabled(false);
		}
		
	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=rightGrid.getSelectionModel().getSelections()[0];
		if(select!=null){
			var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&docid="+select.json.ID+"&st=02";
			//showIFModule(url,"月技术培训记录","true",710,600,{},null,null,null,false,"auto");
			new $FW({url:url,id:select.json.ID}).show();		
		}
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
	            id: 'ID'
	        }, 
	        //['ID','BZ','ZGZS','SHIJIA','HSJ','YLWJ','SDRS','BJ','LX','ELWJ','GC','CJ','FLJLBJ','SLWJ','TXR','SJ']),
	        ['ID','JKTM','JKSJ','BZ','TXR']),
	        baseParams :{formSn:formSn},
	        remoteSort: true	
	    });
		return ds;
	},
    
	/**
	 * PersonAction 列模型
	 */
	getColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
		    	this.sm,
		      {id:'jktm',header: "讲课题目", width: 150, sortable: true, dataIndex: 'JKTM'},
		      {id:'jksj',header: "讲课时间", width: 250, sortable: true, dataIndex: 'JKSJ'},
		      {id:'bz',header: "班组", width: 150, sortable: true, dataIndex: 'BZ'},
		      {id:'txr',header: "填写人", width: 120, sortable: true, dataIndex: 'TXR'} 
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
	}

});

