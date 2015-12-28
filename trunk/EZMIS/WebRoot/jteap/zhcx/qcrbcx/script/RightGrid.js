
/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link4+"?bblx="+bblx);
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
		
		//var btnDelCForm=mainToolbar.items.get('btnDelCForm');
		//var btnModifyCForm=mainToolbar.items.get('btnModifyCForm');
		var btnFindCatalog=mainToolbar.items.get('btnFindCatalog');
		if(oCheckboxSModel.getSelections().length==1){
			if(btnFindCatalog) btnFindCatalog.setDisabled(false);
		}else{
			if(btnFindCatalog) btnFindCatalog.setDisabled(true);
		}
	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		btnFindCatalog_Click();
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
			'id','cname','key','zzrname','zzrcname','zbrq.time','status'
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
				{id:'id',header: "id", width: 130, sortable: true, dataIndex: 'id',hidden:true},
				{id:'cname',header: "报表中文名", width: 130, sortable: true, dataIndex: 'cname'},
				{id:'key',header: "条件字段", width: 100, sortable: true, dataIndex: 'key'},
				{id:'zzrname',header: "制作人ID", width: 100, sortable: true, dataIndex: 'zzrname'},
				{id:'zzrcname',header: "制作人名", width: 100, sortable: true, dataIndex: 'zzrcname'},
				{id:'zbrq',header: "制作时间", width: 130, sortable: true, dataIndex: 'zbrq.time',
					renderer : function(value, metadata, record, rowIndex,colIndex, store) {
						var CREATEDATE = formatDate(new Date(value),
								"yyyy-MM-dd HH:mm:ss");
						return CREATEDATE;
					}
				},
				{id:'status',header: "状态", width: 100, sortable: true, dataIndex: 'status',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						var dict_2=$dictList("BBZT");
						for(var i=0;i<dict_2.length;i++){
							if(dict_2[i].value==value){
								return dict_2[i].key;
							}
						}
						return "";
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
			ids+=selectedobj.id+",";//取得他们的id并组装
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

