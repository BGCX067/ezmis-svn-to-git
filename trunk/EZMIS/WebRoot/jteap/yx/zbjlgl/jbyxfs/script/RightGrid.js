
/**
 * 字段列表
 */
RightGrid=function(nowYmd){
	var url = link1+"?zbsj="+Ext.getCmp("sf#zbsj").getValue().format("Y-m-d")+"&zbbc="+Ext.getCmp("sf#zbbc").getValue();
    var defaultDs=this.getDefaultDS(url);
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
		tbar:mainToolbar,
		items: this.pageToolbar
	});	
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=grid.getSelectionModel().getSelections()[0];
		var id = select.get("id");
		var formSn = select.data.formSn;
		var url = CONTEXT_PATH+"/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&docid="+id;
		
		
		var fw = new $FW({
			url:url,
			width:750,
			height:582,			
			id:id,							//id,相同id的窗口只会打开一个,没设置id时该值为Date().getTime()
			type:"T1",						//窗口类型  T1  T2  T3  分别代表普通窗口、模式对话框、非模式对话框
			title: "查询交班运行方式",			//标题
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
		//showIFModule(url,"查询交班运行方式","true",800,600,{});
    	//grid.getStore().reload();
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
				"id","zbsj","zbbc","zbzb","tianxieren","tianxieshijian","gwlb","formSn","jizhangtype"
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
				{id:'formSn',header: "formSn", width: 100, sortable: true, hidden:true, dataIndex: 'formSn'},
				{id:'jizhangtype',header: "jizhangtype", width: 100, sortable: true, hidden:true, dataIndex: 'jizhangtype'},
				{id:'gwlb',header: "运行方式", width: 150, sortable: true, dataIndex: 'gwlb'},
				{id:'zbsj',header: "值班时间", width: 150, sortable: true, dataIndex: 'zbsj'},
				{id:'zbbc',header: "值班班次", width: 150, sortable: true, dataIndex: 'zbbc'},
				{id:'zbzb',header: "值班值别", width: 150, sortable: true, dataIndex: 'zbzb'},
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
