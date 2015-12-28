/**
 * 字段列表
 */
RightGrid=function(){
	
    var defaultDs=this.getDefaultDS(link1+"?limit=100");
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
		//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		var btnDel=mainToolbar.items.get('btnDel');
		if(oCheckboxSModel.getSelections().length < 1){
			if(btnDel) btnDel.setDisabled(true);
		}else{
			if(btnDel) btnDel.setDisabled(false);
		}
	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		var lxid  = select.json.ID;
		Ext.Ajax.request({
			url:link2,
			method:'POST',
		 	params: {lxid:lxid},// Ext.util.JSON.encode(selections.keys)
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
		 	}
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
				"ID", "XMBH", "XMMC", "XMLX","XMYJ","CBFS","GCYS","KGRQ","SQBMQZ","SQBM","CJSJ","STATUS","ISKG"
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
				{id:'XMBH',header: "项目编号", width: 120, sortable: true, dataIndex: 'XMBH'},
				{id:'STATUS',header: "申请状态", width: 100, sortable: true, dataIndex: 'STATUS',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
					 	if(value =='已立项'){
							metadata.attr ="style=background:#00FF33";
							return value;
						}else if(value == null){
							metadata.attr ="style=background:#FF3333";
							return '草  稿';
						}else{
							metadata.attr ="style=background:yellow";
							return value;
						}
					}
				},
				/*
				{id:'ldstatus',header: "流程状态", width: 100, sortable: true, dataIndex: 'ldstatus'},
				*/
				{id:'SQBM',header: "申请部门", width: 120, sortable: true, dataIndex: 'SQBM'},
				{id:'SQBMQZ',header: "申请人", width: 120, sortable: true, dataIndex: 'SQBMQZ' },
				{id:'CJSJ',header: "申请时间", width: 140, sortable: true, dataIndex: 'CJSJ',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(value != null){
							return formatDate(new Date(value),"yyyy-MM-dd HH:mm:ss");
						}
					}
				},
				{id:'XMYJ',header: "工程类别", width: 100, sortable: true, dataIndex: 'XMYJ'},
				{id:'XMMC',header: "工程项目", width: 100, sortable: true, dataIndex: 'XMMC' },
				{id : 'ISKG',header : "是否开工",width : 140,sortable : true,dataIndex : 'ISKG',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						 	if(value == '已开工'){
								metadata.attr ="style=color:green";
								return value;
							}else if(value == '流程中'){
								metadata.attr ="style=color:Orange";
								return value;
							}else{
								metadata.attr ="style=color:red";
								return '未开工';
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
	},
	/**
	 * 删除
	 */
	deleteSelect:function(select){
		var selections = this.getSelections();//获取被选中的行
		var rightGrid=this;
		var ids="";
		Ext.each(selections,function(selectedobj){
			ids+=selectedobj.json.ID+",";//取得他们的id并组装
		});
		if(window.confirm("确认删除选中的条目吗？")){
			Ext.Ajax.request({
				url:link5,
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