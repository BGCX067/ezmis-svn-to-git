
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
		}},'-','<font color="red"></font>']
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
		
		var btnDelCForm=mainToolbar.items.get('btnDel');
		var btnModifyCForm=mainToolbar.items.get('btnModify');
		
		if(oCheckboxSModel.getSelections().length==1){
			if(btnModifyCForm) btnModifyCForm.setDisabled(false);
		}else{
			if(btnModifyCForm) btnModifyCForm.setDisabled(true);
		}
		
		if(oCheckboxSModel.getSelections().length<=0){
			if(btnDelCForm) btnDelCForm.setDisabled(true);
		}else{
			if(btnDelCForm) btnDelCForm.setDisabled(false);
		}
	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=rightGrid.getSelectionModel().getSelections()[0];
		if(select.get("type")=="EFORM"){
			//var eformUrl=select.get("eformUrl");
			
			var url="/jteap/cform/fceform/common/djframe.htm?djsn="+select.get("sn")+"&catalogName="+select.get("eformUrl")
		    window.open(CONTEXT_PATH+url);
		}
		
		if(select.get('type')=="EXCEL"){
			var url=CONTEXT_PATH+"/jteap/cform/excelFormRec.jsp?cformId="+select.json.id;
			var features="menubar=no,toolbar=no,width=800,height=600";
			window.open(url,"_blank",features);
		}
		
		if(select){
			var url=link15+"&docid="+select.json.id;
			var params = "wzmc|"+select.json.wzbm.wzmc+";"+
						 "zje|"+select.json.zje;
			result=showModule(url,true,800,645,params);
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
			"id",
			"bh",
			"bm",
			"zgbm",
			"wzbm",
			"wzbm.wzmc",
			"wzbm.xhgg",
			"ffsj",
			"dqsj",
			"zyf",
			"ffsl",
			"dj",
			"zje",
			"zg",
			"bmzg",
			"sjc",
			"flr",
			"llr",
			"czr",
			"grgyqf"
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
		    	{id:'bh',header: "编号", width: 80, sortable: true, dataIndex: 'bh'},
		    	{id:'zgbm',header: "职工", width: 70, sortable: true, dataIndex: 'zgbm'},
		    	{id:'wzbm.wzmc',header: "物资名称", width: 150, sortable: true, dataIndex: 'wzbm.wzmc'},
		    	{id:'ffsj',header: "发放时间", width: 100, sortable: true, dataIndex: 'ffsj',
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
						var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
						return dt;         
					}},
				{id:'dqsj',header: "到期时间", width: 100, sortable: true, dataIndex: 'dqsj',
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
						var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
						return dt;         
					}},
				{id:'ffsl',header: "发放数量", width: 70, sortable: true,align:'right', dataIndex: 'ffsl',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(4);
					}
				},
				{id:'dj',header: "单价", width: 70, sortable: true, dataIndex: 'dj',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(4);
					}
				},
				{id:'zje',header: "总金额", width: 70, sortable: true, dataIndex: 'zje',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(4);
					}
				},
				{id:'zg',header: "主管", width: 70, sortable: true, dataIndex: 'zg'},
				{id:'czr',header: "操作员", width: 70, sortable: true, dataIndex: 'czr'},
//				{id:'bmzg',header: "部门主管", width: 70, sortable: true, dataIndex: 'bmzg'},
//				{id:'sjc',header: "生计处", width: 70, sortable: true, dataIndex: 'sjc'},
				{id:'flr',header: "发料人", width: 70, sortable: true, dataIndex: 'flr'},
				{id:'zyf',header: "在用否", width: 60, sortable: true, dataIndex: 'zyf',
					renderer:function(value){
						var retValue;
						if(value=='N') retValue='未使用';
							else if(value=='Y') retValue='使用中';
						return retValue;
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

