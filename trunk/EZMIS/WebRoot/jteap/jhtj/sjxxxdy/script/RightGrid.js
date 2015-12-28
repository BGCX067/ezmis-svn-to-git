
/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link4);
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'导出Excel',handler:function(){
		exportExcel(grid,true);
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
		tbar:this.pageToolbar 
	});	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		
		var btnModiCatalog=mainToolbar.items.get("btnModiCatalog");
		var btnDelCatalog=mainToolbar.items.get("btnDelCatalog");
		if(oCheckboxSModel.getSelections().length==1){
			if(btnModiCatalog) btnModiCatalog.setDisabled(false);
			if(btnDelCatalog) btnDelCatalog.setDisabled(false);
		}else{
			if(btnModiCatalog) btnModiCatalog.setDisabled(true);
			if(btnDelCatalog) btnDelCatalog.setDisabled(true);
		}
	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		
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
			'id','iorder','item','iname','dw',"sjlx","xsw","qsfs","itype","dfunId","dname"
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
				{id:'id',header: "id", width: 100, sortable: true, dataIndex: 'id',hidden:true},
				{id:'iorder',header: "序号", width: 30, sortable: true, dataIndex: 'iorder',align:'center'},
				{id:'item',header: "信息项编码", width: 70, sortable: true, dataIndex: 'item',align:'center'},
				{id:'iname',header: "信息项名称", width: 100, sortable: true, dataIndex: 'iname',align:'center'},
				{id:'dw',header: "单位", width: 80, sortable: true, dataIndex: 'dw',align:'center'},
				{id:'itype',header: "数据类型", width: 60, sortable: true, dataIndex: 'itype',align:'center'},
				{id:'xsw',header: "小数位数", width: 60, sortable: true, dataIndex: 'xsw',align:'center'},
				{id:'qsfs',header: "取数方式", width: 100, sortable: true, dataIndex: 'qsfs',align:'center',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						var dict_2=$dictList("QSFS");
						for(var i=0;i<dict_2.length;i++){
							if(dict_2[i].value==value){
								return dict_2[i].key;
							}
						}
						return "";
					}
				},
				{id:'sjlx',header: "业务类型", width: 100, sortable: true, dataIndex: 'sjlx',align:'center',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						var dict_2=$dictList("YWLX");
						for(var i=0;i<dict_2.length;i++){
							if(dict_2[i].value==value){
								return dict_2[i].key;
							}
						}
						return "";
					}
				},
				{id:'dfunId',header: "dll功能号", width: 80, sortable: true, dataIndex: 'dfunId',align:'center'},
				{id:'dname',header: "应用接口dll", width: 80, sortable: true, dataIndex: 'dname',align:'center'}
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
	deleteSelect:function(select,kid){
		//var selections = this.getSelections();//获取被选中的行
		var rightGrid=this;
		var ids=select.json.id;
		var param={};
		param.kid=kid;
		//Ext.each(selections,function(selectedobj){
		//	ids+=selectedobj.id+",";//取得他们的id并组装
		//});
		if(window.confirm("确认删除选中的条目吗？")){
			Ext.Ajax.request({
				url:link5,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
						AjaxRequest_Sync(link12,param,function(res){
							var resText = res.responseText;
							var resObj = resText.evalJSON();
							if(resObj.success){
								alert("删除成功");
								rightGrid.getStore().reload();
							}
						});
			 		}else{
			 			alert(responseObject.msg);
			 		}				
				},
			 	failure:function(){
			 		alert("提交失败");
			 	},
			 	method:'POST',
			 	params: {ids:ids,kid:kid}//Ext.util.JSON.encode(selections.keys)			
			});
		}
	}

});

