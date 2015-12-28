/**
 * 字段列表
 */
RightGrid=function(){
	
    var defaultDs=this.getDefaultDS(link1+"?queryParamsSql=obj.xqjhqf='2'"+"&limit=100");
    defaultDs.load();
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: 100,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'导出Excel',handler:function(){
		exportSelectedExcel(grid);
		}}]
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
		tbar:this.pageToolbar,
		stripeRows: true
	});	
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		var xqjhsqid  = select.json.id;
		Ext.Ajax.request({
			url:link2,
			success:function(ajax){
		 		var responseText=ajax.responseText;	
		 		var responseObject=Ext.util.JSON.decode(responseText);
		 		if(responseObject.success){
		 			var url = link3;
					var windowUrl = link4 + "?pid=" + responseObject.processinstance + "&status=false";
					var args = "url|" + windowUrl + ";title|" + '查看流程';
					var retValue = showModule(url, "yes", 700, 600, args);
		 		}else{
		 			alert(responseObject.msg);
		 		}				
			},
		 	failure:function(){
		 		alert("提交失败");
		 	},
		 	method:'POST',
		 	params: {xqjhsqid:xqjhsqid}// Ext.util.JSON.encode(selections.keys)
		});
	}else{
		alert("至少选择一条记录!");
		return;
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
				"id", "xqjhsqbh", "gclb", "gcxm", "sqbm", "sqsj",
				"czy", "czyxm", "flowStatus", "isBack", "isUpdate", "xgsjsz",
				"scsjsz", "xqjhqf", "xjsjsz", "lydid", "qmzt", "xqjhsqDetail",
				"sqbmmc", "status"
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
		    	{id:'xh',header: "序号", width: 40, sortable: true, 
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return rowIndex+1;
					}
				},
				{id:'xqjhsqbh',header: "补料申请编号", width: 120, sortable: true, dataIndex: 'xqjhsqbh'},
				{id:'flowStatus',header: "补料申请状态", width: 130, sortable: true, dataIndex: 'flowStatus',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
					 	if(value != ''){
							metadata.attr ="style=background:yellow";
							return value;
						}else if(value == ''){
							metadata.attr ="style=background:#FF3333";
							return '草  稿';
						}
					}
				},
				/*
				{id:'ldstatus',header: "流程状态", width: 100, sortable: true, dataIndex: 'ldstatus'},
				*/
				{id:'sqbm',header: "申请部门", width: 120, sortable: true, dataIndex: 'sqbmmc'},
				{id:'czyxm',header: "申请人", width: 120, sortable: true, dataIndex: 'czyxm' },
				{id:'sqsj',header: "申请时间", width: 140, sortable: true, dataIndex: 'sqsj',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(value == "" || value == null){
				    		return "";
				    	}else{
				    		var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
				    		return dt;
				    	}
					}
				},
				{id:'gclb',header: "工程类别", width: 100, sortable: true, dataIndex: 'gclb'},
				{id:'gcxm',header: "工程项目", width: 100, sortable: true, dataIndex: 'gcxm' }
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