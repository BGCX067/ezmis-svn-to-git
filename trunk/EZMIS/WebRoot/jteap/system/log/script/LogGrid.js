var LogGrid=function(){
	var templink1 = link1+"?type="+type
	var logGrid = this
	this.exportLink = templink1
	var defaultDs = this.getLogActionDS(templink1);
	var defaultCm = this.getLogActionColumnModel();
	this.pageToolbar = new Ext.PagingToolbar({
		    pageSize: 25,
		    store: defaultDs,
		    displayInfo: true,
		    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
			emptyMsg: "没有符合条件的数据",
			items:['-',{text:'导出Excel',listeners:{click:function(){
				exportExcel(logGrid,true)
				/*
				var path = logGrid.exportLink
				var paths = path.split("?")
				var temp1 = paths[0]
				var temp3 = paths[1]
				temp1 = temp1.split("!")[0]
				path = temp1+"!exportExcel.do?"+temp3
				var cm = logGrid.getColumnModel();
				var flag = true
				var sum = defaultCm.getColumnCount();
				var j = 1 ;
				if(!flag) {
					j = 0
				}
				var paraHeader = ""; 
				var paraDataIndex = "";
				for(var i=j;i<sum;i++) {
					paraHeader += cm.getColumnHeader(i) + ","
					paraDataIndex += defaultCm.getDataIndex(i) + ","
				}
				paraHeader = paraHeader.substr(0,paraHeader.length-1)
				paraDataIndex = paraDataIndex.substr(0,paraDataIndex.length-1)
				path = path + "&paraHeader="+paraHeader+"&paraDataIndex="+paraDataIndex
				window.open(path)
				*/
			
			}}},'-','<font color="red">*双击查看详细信息</font>']
	});
	LogGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	    cm: defaultCm,
		sm: this.defaultSm,
		width:600,
		height:300,
		loadMask:true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar
	});
	//当有日志被选择的时候，显示工具栏的删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		var oBtnDelLog=mainToolbar.items.get('btnDelLog');
		
		if(oCheckboxSModel.getSelections().length==0){
			oBtnDelLog.setDisabled(true);
		}else{
			oBtnDelLog.setDisabled(false);
		}
	});
	//添加双击查看
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var detailForm=new DetailFormWindow(grid.getStore().getAt(rowIndex));
		detailForm.show();
	});
}
Ext.extend(LogGrid, Ext.grid.GridPanel, {
	defaultSm:new Ext.grid.CheckboxSelectionModel(),
	//删除选中的日志
	delSelectedLog:function(){
		var selections = this.getSelections();
		var logids="";
		var grid = this;
		Ext.each(selections,function(selectedobj){
			logids+=selectedobj.id+",";
		});
		if(window.confirm("确认删除日志吗")){
			Ext.Ajax.request({
				url:link2,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			alert("删除成功");
			 			grid.getStore().reload();
			 		}else{
			 			alert(responseObject.msg);
			 		}				
				},
			 	failure:function(){
			 		alert("提交失败");
			 	},
			 	method:'POST',
			 	params: {ids:logids}//Ext.util.JSON.encode(selections.keys)			
			});
		}
	},
	//删除该类型所有的日志
	delAllLog:function(){
		var grid = this;
		var templink3 = link3+"?type="+type
		if(window.confirm("确认清空该类型的所有日志吗")){
			Ext.Ajax.request({
				url:templink3,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			alert("清除日志成功");
			 			grid.getStore().reload();
			 		}else{
			 			alert(responseObject.msg);
			 		}				
			 	}
			});
		}
	},
	/**
	 * 列模型
	 */
	getLogActionColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
			    	this.defaultSm,
			    	{header: "IP地址", width: 100, sortable: true, dataIndex: 'ip'},
			        {header: "日志内容", width: 300, sortable: false, dataIndex: 'log'},
					{header: "用户", width: 120, sortable: true, dataIndex: 'personName'},
					{header: "帐号", width: 120, sortable: true, dataIndex: 'personLoginName'},
					{header: "日期", width: 120, sortable: true, dataIndex: 'dt',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd hh:mm:ss"); 
						return dt;         
					}}	
				])
		return cm;
	},
	/**
	 * 日志数据源
	 */
	getLogActionDS:function(url){
	    var ds = new Ext.data.Store({
	        proxy: new Ext.data.ScriptTagProxy({
	            url: url
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount',
	            id: 'id'
	        }, [
	            'id', 'log', 'personName',"personLoginName", 'dt','ip'
	        ]),
	        remoteSort: true	
	    });	        
		//按照data进行排序
   		ds.setDefaultSort('dt', 'desc');
	    return ds;
	},
	/**
	 * 切换数据源->LogAction!showList
	 */
	changeToListDS:function(url){
		this.exportLink = url
		var ds = this.getLogActionDS(url);	
		var cm=this.getLogActionColumnModel();
		this.pageToolbar.bind(ds);
		this.reconfigure(ds,cm);
	}
});