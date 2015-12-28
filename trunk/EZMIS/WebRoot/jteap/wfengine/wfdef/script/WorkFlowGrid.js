WorkFlowGrid=function(){
	//数据字典表格
	workFlowGrid=this;
    	
    var workFlowDs=this.getWorkFlowActionDS(link4);
    this.pageToolbar=new Ext.PagingToolbar({
	    store: workFlowDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'导出Excel',listeners:{click:function(){
				exportExcel(workFlowGrid,true)
		}}},'-']
	});
		
	WorkFlowGrid.superclass.constructor.call(this,{
		//bodyStyle : 'margin:5px 5px 0px 5px',
		boder : true,
		bodyBorder : true , 
	 	ds: workFlowDs,
	 	margins:'2px 2px 2px 2px',
	    cm:this.getWorkFlowActionColumnModel(),
		ddGroup:'GridDD',
	 	enableDragDrop:true,
	 	dropAllowed: true, 
		dragAllowed: true,
		width:888,
		height:484,
		loadMask: true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar
	});
	//this.getStore().load({params:{start:0, limit:CONSTANTS_PAGE_DEFAULT_LIMIT}});  
    
	//当有字典被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		//只有单选的时候才能修改，多选的只能删除
		var oBntModiFlow=mainToolbar.items.get('btnModiFlow');
		var oBtnDelFlow=mainToolbar.items.get('btnDelFlow');
		var oBtnBackFlow=mainToolbar.items.get('btnBackFlow');
		var oBtnIssueFlow=mainToolbar.items.get('btnIssueFlow') ;
		var oBtnDraftFlow=mainToolbar.items.get('btnDraftFlow') ;
		var oBtnExportFlow=mainToolbar.items.get('btnExportFlow') ;
		var btnValidateFlow=mainToolbar.items.get('btnValidateFlow');
		var btnCopyFlow=mainToolbar.items.get('btnCopyFlow');
		
		if(oCheckboxSModel.getSelections().length==1){
			if (oBntModiFlow)
				oBntModiFlow.setDisabled(false);
			if (oBtnDelFlow)
				oBtnDelFlow.setDisabled(false);
			if (oBtnBackFlow)
				oBtnBackFlow.setDisabled(false);
			if (oBtnIssueFlow)
			oBtnIssueFlow.setDisabled(false) ;
			if (oBtnDraftFlow)
				oBtnDraftFlow.setDisabled(false) ; 
			if (oBtnExportFlow)
				oBtnExportFlow.setDisabled(false) ;
			if (btnValidateFlow)
				btnValidateFlow.setDisabled(false);
			if (btnCopyFlow)
				btnCopyFlow.setDisabled(false);
		}else{
			if (oBntModiFlow)
				oBntModiFlow.setDisabled(true);
			if (oBtnDraftFlow)
				oBtnDraftFlow.setDisabled(true) ;
			if (oBtnExportFlow)
				oBtnExportFlow.setDisabled(true) ;
			if (oBtnBackFlow)
				oBtnBackFlow.setDisabled(true);
			if (btnValidateFlow)
				btnValidateFlow.setDisabled(true);
			if (btnCopyFlow)
				btnCopyFlow.setDisabled(true);
			if(oCheckboxSModel.getSelections().length==0){
				if (oBtnIssueFlow)
					oBtnIssueFlow.setDisabled(true);
				if (oBtnDelFlow)
					oBtnDelFlow.setDisabled(true);

			}else{
				if (oBtnIssueFlow)
					oBtnIssueFlow.setDisabled(false);
				if (oBtnDelFlow)
					oBtnDelFlow.setDisabled(false);
			}
		}
	});
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		
	})
}
Ext.extend(WorkFlowGrid, Ext.grid.GridPanel, {
	sm:new Ext.grid.CheckboxSelectionModel(),
	
	/**
	 * 数据源DictAction!showList
	 */
	changeToListDS:function(url){
		var ds=this.getWorkFlowActionDS(url);
		var cm=this.getWorkFlowActionColumnModel();
		this.pageToolbar.bind(ds);
		this.reconfigure(ds,cm);
	},
	
	/**
	 * WorkFlowGrid 列模型
	 */
	getWorkFlowActionColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
	    	this.sm,
	        {id:'name',header: "流程名称", width: 180, sortable: true, dataIndex: 'name'},
			{header: "创建人", width: 100, sortable: true, dataIndex: 'creater'},
			{header: "创建时间", width: 150, dataIndex: 'createrTime',renderer:function(value,metadata,record,rowIndex,colIndex,store){
						var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd hh:mm:ss"); 
						return dt;         
					}},
			{header: "版本号", width: 50, dataIndex: 'version'} ,
			{header: "部署状态", width: 60, sortable: true, dataIndex: 'pd_id',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value!=null && value>0)
							return "<img src='icon/icon_14.gif'/>";
						else
							return "";
		   			}
		   	},{header: "验证状态", width: 60, sortable: true, dataIndex: 'validated',
					renderer:function(val){
						if(val==true)
							return "<img src='icon/icon_14.gif'/>";
						else
							return "";
		   			}
		   	}
		]);
		return cm;
	},
	/**
	 * 获取数据字典的数据源
	 */
	getWorkFlowActionDS:function(url){
	    var ds = new Ext.data.Store({
	    	//proxy:new Ext.data.MemoryProxy({totalCount:'1',list:[{id:'1',workFlowName:'流程一',creator:'root',date:'2008-01-01',remark:'备注',version:'版本号'}]}),
	        proxy: new Ext.data.ScriptTagProxy({
	            url: url
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount',
	            id: 'id'
	        }, [
	            "name","creater","createrTime","version","description","pd_id","validated"
	        ]),
	        remoteSort: true	
	    });
	   	return ds;
	},
	
	/**
	 * 删除选中的字典
	 */
	delSelectedDict:function(){
	var selections = this.getSelections();//获取被选中的行
	var dictids="";
	Ext.each(selections,function(selectedobj){
		dictids+=selectedobj.id+",";//取得他们的id并组装
	});
	if(window.confirm("确认删除字典吗？")){
		Ext.Ajax.request({
			url:link10,
			success:function(ajax){
		 		var responseText=ajax.responseText;	
		 		var responseObject=Ext.util.JSON.decode(responseText);
		 		if(responseObject.success){
		 			alert("删除成功");
		 			workFlowGrid.getStore().reload();
		 		}else{
		 			alert(responseObject.msg);
		 		}				
			},
		 	failure:function(){
		 		alert("提交失败");
		 	},
		 	method:'POST',
		 	params: {ids:dictids}//Ext.util.JSON.encode(selections.keys)			
		});
	}
}
	
	
});

