
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
		var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&docid="+select.json.ID+"&st=02";
		//showIFModule(url,"规章制度","true",550,390,{},null,null,null,false,"auto");
		new $FW({url:url,id:select.json.ID}).show();
	
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
	        ['ID','ZDM','ZDNR','TXR','SCSJ']),
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
		      {id:'zdm',header: "制度名", width: 150, sortable: true, dataIndex: 'ZDM'},
        	  /*{id:'zgzs',header: "职工总数", width: 80, sortable: true, dataIndex: 'ZGZS'},
        	  {id:'shijia',header: "事假", width: 40, sortable: true, dataIndex: 'SJ'},
        	  {id:'hsj',header: "婚丧假", width: 60, sortable: true, dataIndex: 'HSJ'},
        	  {id:'ylwj',header: "一类违纪", width: 80, sortable: true, dataIndex: 'YLWJ'},
        	  {id:'sdrs',header: "实到人数", width: 80, sortable: true, dataIndex: 'SDRS'},
        	  {id:'bj',header: "病假", width: 40, sortable: true, dataIndex: 'BJ'},
        	  {id:'lx',header: "轮休", width: 40, sortable: true, dataIndex: 'LX'},
        	  {id:'elwj',header: "二类违纪", width: 80, sortable: true, dataIndex: 'ELWJ'},
        	  {id:'gc',header: "公差", width: 40, sortable: true, dataIndex: 'GC'},
        	  {id:'cj',header: "产假", width: 40, sortable: true, dataIndex: 'CJ'},
        	  {id:'fljlbj',header: "扶理假劳保假", width: 120, sortable: true, dataIndex: 'FLJLBJ'},
        	  {id:'slwj',header: "三类违纪", width: 90, sortable: true, dataIndex: 'SLWJ'},
        	  */
        	  {id:'zdnr',header: "制度内容", width: 300, sortable: true, dataIndex: 'ZDNR'},
        	  {id:'txr',header: "填写人", width: 120, sortable: true, dataIndex: 'TXR'},
        	  {id:'scsj',header: "创建时间", width: 90, sortable: true, dataIndex: 'SCSJ',renderer: function(value){
        	         	return value.replace("年", "-").replace("月", "-").replace("日", "");
        	  }}
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

