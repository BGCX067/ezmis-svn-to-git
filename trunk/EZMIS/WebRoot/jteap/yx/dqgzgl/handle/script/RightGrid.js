
/**
 * 字段列表
 */
RightGrid=function(){
	var url = link1+"?queryParamsSql=to_char(obj.dqgzCreateDt,'yyyy-MM-dd')>='"+beginYmd
			+"' and to_char(obj.dqgzCreateDt,'yyyy-MM-dd')<='"+nowYmd+"' and status='"+encodeURIComponent(initStatus)+"'";
    var defaultDs=this.getDefaultDS(url);
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['<font color="red">*双击处理定期工作</font>']
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
		
		if(select.data.status == "未完成"){
			var url = contextPath + '/jteap/yx/dqgzgl/handle/handle.jsp?id='+select.id;
			var result = showIFModule(url,"添加定期工作处理","true",600,400,{});
			grid.getStore().reload();
		}else if(select.data.status == "已完成"){
			var url = contextPath + '/jteap/yx/dqgzgl/handle/handle.jsp?id='+select.id+'&wancheng=t';
			var result = showIFModule(url,"查询定期工作处理结果","true",600,400,{});
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
				"id","dqgzSetId","fzbm","fzgw","gzgl","dqgzzy",
				"bc","dqgzMc","dqgzNr","dqgzFl","dqgzCreateDt",
				"chuliRen","chuliNr","chuliDt","status","time"
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
		var dict_dqgzgl=$dictList("dqgzgl");
		
	    var cm=new Ext.grid.ColumnModel([
		    	this.sm,
				{id:'id',header: "编号", width: 100, sortable: true, hidden:true, dataIndex: 'id'},
				{id:'dqgzCatalogId',header: "定期工作分类编号", width: 100, sortable: true, hidden:true, dataIndex: 'dqgzCatalogId'},
				{id:'dqgzNr',header: "实验及切换项目", width: 310, sortable: true, dataIndex: 'dqgzNr',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    		return "<span ext:qtip='"+value+"'>"+Ext.util.Format.htmlEncode(value)+"</span>";
			    	}
			    },
				{id:'fzbm',header: "负责部门", width: 80, sortable: true, dataIndex: 'fzbm'},
				{id:'fzgw',header: "责任岗位", width: 80, sortable: true, dataIndex: 'fzgw'},
				{id:'dqgzCreateDt',header: "发送时间", width: 110, sortable: true, dataIndex: 'dqgzCreateDt',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value != null && value.time != null){
				    		var dt = new Date(value.time).format("Y-m-d H:i");
							return dt;         
						}else{
							return "";
						}
					}
				},
				{id:'chuliDt',header: "执行时间", width: 110, sortable: true, dataIndex: 'chuliDt',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value != null && value.time != null){
				    		var dt = new Date(value.time).format("Y-m-d H:i");
							return dt;         
						}else{
							return "";
						}
					}
				},
				{id:'chuliRen',header: "执行人", width: 80, sortable: true, dataIndex: 'chuliRen'},
				{id:'status',header: "完成状态", width: 60, sortable: true, dataIndex: 'status',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    		if(value == "未完成"){
				    			return "<font color='red'>" + value + "</font>";
				    		}else if(value == "已完成"){
				    			return "<font color='green'>" + value + "</font>";
				    		}
			    	}
			    },
				{id:'gzgl',header: "工作规律", width: 100, sortable: true, dataIndex: 'gzgl',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						for (var i = 0; i < dict_dqgzgl.length; i++) {
							if(dict_dqgzgl[i].value == value){
								return dict_dqgzgl[i].key;
							}
						}
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