/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link1);
    defaultDs.load();
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: 20,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据"
	});
	RightGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	renderTo:"divGrid",
	 	cm: this.getColumnModel(),
		sm: this.sm,
	    margins:'2px 2px 2px 2px',
		width:575,
		height:370,
		loadMask: true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar,
		stripeRows: true
	});	
	this.on('beforerender',function(param){
		this.sm.singleSelect  = true;        
	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=rightGrid.getSelectionModel().getSelections()[0];
		if(select){
		var obj = {};
		//反馈问题Id
		obj.id = select.data.id;
		obj.createPerson = select.data.createPerson;
		obj.createDate = select.data.createDate;
		obj.content = select.data.content;
		obj.remark = select.data.remark;
		var url = contextPath
				+ '/jteap/index/questionFeedback/questionFeedback.jsp?questionFeedbackId='+obj.id+'&type=show';
		showIFModule(url, "MIS系统问题反馈", "true", 500, 345, obj);
		rightGrid.getStore().reload();
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
	            id: 'id'
	        }, [
				"id","createPerson","createDate","content","remark"
	        ]),
	        remoteSort: true,
	        //如果为true,每次store在加载时或者一条记录被删除时, 清除所有改动过的记录信息
	        pruneModifiedRecords: true
	    });
		return ds;
	},
    
	/**
	 * JctzAction 列模型
	 */
	getColumnModel:function(){
		
		var grid = this;
		
	    var cm=new Ext.grid.ColumnModel([
		    	this.sm,
				{id:'createPerson',header: "反馈人", width: 80, sortable: true, dataIndex: 'createPerson'},
				{id:'createDate',header: "反馈时间", width: 80, sortable: true, dataIndex: 'createDate',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
					var createDate = (value == null)?null:formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
					return createDate;
				}},
				{id:'content',header: "反馈内容", width: 270, sortable: true, dataIndex: 'content',
					editor: {xtype:'textarea',listeners:{render:function(ta){ta.el.dom.rows=3;}}},
					renderer:function(value, metadata, record){ 
						metadata.attr ='style="white-space:normal;"'; 
						return value; 
					}
				},
				{id:'remark',header: "备注", width: 100, sortable: true, dataIndex: 'remark',
					editor: {xtype:'textarea',listeners:{render:function(ta){ta.el.dom.rows=3;}}},
					renderer:function(value, metadata, record){ 
						metadata.attr ='style="white-space:normal;"'; 
						return value; 
					}
				}
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