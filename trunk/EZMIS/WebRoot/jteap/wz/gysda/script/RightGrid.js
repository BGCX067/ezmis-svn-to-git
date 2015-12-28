
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
//		autoExpandColumn:"gysmc",  //自动扩展宽度的列
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
	
//	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
//		var select=rightGrid.getSelectionModel().getSelections()[0];
//		if(select.get("type")=="EFORM"){
//			//var eformUrl=select.get("eformUrl");
//			
//			var url="/jteap/cform/fceform/common/djframe.htm?djsn="+select.get("sn")+"&catalogName="+select.get("eformUrl")
//		    window.open(CONTEXT_PATH+url);
//		}
//		
//		if(select.get('type')=="EXCEL"){
//			var url=CONTEXT_PATH+"/jteap/cform/excelFormRec.jsp?cformId="+select.json.id;
//			var features="menubar=no,toolbar=no,width=800,height=600";
//			window.open(url,"_blank",features);
//		}
//	});
	
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
			'bm','id','gysmc','dz','dh','czh','yzbm','lxr','yxdz','zywz','frdb','khyh','zh','swdjh','qtxx','sfxydw','sfsndw','zjm'
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
				{id:'bm',header: "供应商编码", width: 100, sortable: true, dataIndex: 'bm'},
//				{id:'id',header: "ID", width: 100, sortable: true, dataIndex: 'id'},
				{id:'gysmc',header: "供应商名称", width: 300, sortable: true, dataIndex: 'gysmc'},
				{id:'zjm',header: "助记码", width: 100, sortable: true, dataIndex: 'zjm'},
				{id:'dz',header: "地址", width: 100, sortable: true, dataIndex: 'dz'},
				{id:'dh',header: "电话", width: 100, sortable: true, dataIndex: 'dh'},
				{id:'czh',header: "传真号", width: 100, sortable: true, dataIndex: 'czh'},
				{id:'yzbm',header: "邮政编码", width: 100, sortable: true, dataIndex: 'yzbm'},
				{id:'lxr',header: "联系人", width: 100, sortable: true, dataIndex: 'lxr'},
				{id:'yxdz',header: "Email", width: 100, sortable: true, dataIndex: 'yxdz'},
//				{id:'zywz',header: "主页网址", width: 100, sortable: true, dataIndex: 'zywz'},
				{id:'frdb',header: "法人代表", width: 100, sortable: true, dataIndex: 'frdb'},
				{id:'khyh',header: "开户银行", width: 100, sortable: true, dataIndex: 'khyh'},
				{id:'zh',header: "账号", width: 100, sortable: true, dataIndex: 'zh'},
				{id:'swdjh',header: "税务登记号", width: 100, sortable: true, dataIndex: 'swdjh'}
//				{id:'qtxx',header: "其它信息", width: 100, sortable: true, dataIndex: 'qtxx'},
//				{id:'sfxydw',header: "是否协议单位", width: 100, sortable: true, dataIndex: 'sfxydw'},
//				{id:'sfsndw',header: "是否市内单位", width: 100, sortable: true, dataIndex: 'sfsndw'}
				
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

