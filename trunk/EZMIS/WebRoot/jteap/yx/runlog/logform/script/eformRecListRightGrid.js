
/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link1+"?formSn="+formSn+"&beginYmd="+beginYmd+"&endYmd="+nowYmd);
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-','<font color="red">*双击查看详细信息</font>']
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
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=rightGrid.getSelectionModel().getSelections()[0];
		var id = select.get("id");
		
		var url = CONTEXT_PATH+"/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&docid="+id;
		var myTitle = "修改运行日志";
		var selectTxsj = select.data.tianxieshijian;
		selectTxsj = selectTxsj.substring(0,10);
				
		if(nowYmd != selectTxsj){
			//当前时间不等于选中记录的填写时间、则为只读
			url += "&st=02";
			myTitle = "查询运行日志";
		}
		
		var fw = new $FW({
			url:url,
			width:750,
			height:582,			
			id:id,							//id,相同id的窗口只会打开一个,没设置id时该值为Date().getTime()
			type:"T1",						//窗口类型  T1  T2  T3  分别代表普通窗口、模式对话框、非模式对话框
			title: myTitle,					//标题
			status: false,					//状态栏
			toolbar:false,					//工具栏
			scrollbars:false,				//滚动条
			menubar:false,					//菜单栏
			userIF:false,					//是否采用Iframe套框,为解决模式窗口无法刷新的问题
			resizable:false,				//是否支持调整大小
			callback:function(retValue){	//回调函数
			    rightGrid.getStore().reload();
			}
		});
		fw.show();
		//showIFModule(CONTEXT_PATH+url,myTitle,"true",800,600,{});
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
				"id","tianxieren","tianxieshijian"
	        ]),
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
				{id:'id',header: "id", width: 100, sortable: true, hidden:true, dataIndex: 'id'},
				{id:'tianxieren',header: "填写人", width: 150, sortable: true, dataIndex: 'tianxieren'},
				{id:'tianxieshijian',header: "填写时间", width: 150, sortable: true, dataIndex: 'tianxieshijian'}
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
