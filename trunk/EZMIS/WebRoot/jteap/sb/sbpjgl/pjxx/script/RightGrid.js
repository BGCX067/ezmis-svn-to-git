/**
 * 字段列表
 */
RightGrid=function(){
	
    var defaultDs=this.getDefaultDS(link4+"?limit=20");
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: 20,
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
		
		var btnDel=mainToolbar.items.get('btnDelPj');
		var btnModify=mainToolbar.items.get('btnPj');
		
		if(oCheckboxSModel.getSelections().length==1){
			if(btnModify) btnModify.setDisabled(false);
		}else{
			if(btnModify) btnModify.setDisabled(true);
		}
		
		if(oCheckboxSModel.getSelections().length<=0){
			if(btnDel) btnDel.setDisabled(true);
		}else{
			if(btnDel) btnDel.setDisabled(false);
		}
	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=rightGrid.getSelectionModel().getSelections()[0];
		if(select){
		var obj = {};
		//设备台帐分类Id
		obj.id = select.data.id;
		obj.pjfl = select.data.pjfl;
		obj.sbbm = select.data.sbbm;
		obj.sbmc = select.data.sbmc;
		obj.sbgg = select.data.sbgg;
		obj.scpjrq = select.data.scpjrq;
		obj.scpjjb = select.data.scpjjb;
		obj.scpjr = select.data.scpjr;
		obj.bcpjrq = select.data.bcpjrq;
		obj.bcpjjb = select.data.bcpjjb;
		obj.bcpjr = select.data.bcpjr;
		obj.remark = select.data.remark;
		obj.sbpjCatalogId = select.data.sbpjCatalog.id;
		var url = contextPath + '/jteap/sb/sbpjgl/pjxx/pjxxinfo.jsp' + "?modi=m&sbpjCatalogId="+obj.sbpjCatalogId+"&type=show";
		showIFModule(url,"设备评级信息","true",700,300,obj);
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
				"id","sbpjCatalog","pjfl","sbbm","sbmc","sbgg","scpjrq","scpjjb","scpjr","bcpjrq","bcpjjb","bcpjr","remark"
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
		//var dict_sbtzgl=$dictList("sbtz");
		
	    var cm=new Ext.grid.ColumnModel([
		    	this.sm,
				{id:'pjfl',header: "评级分类", width: 100, sortable: true, hidden:true,dataIndex: 'pjfl'},
				{id:'sbbm',header: "设备编码", width: 100, sortable: true, dataIndex: 'sbbm'},
				{id:'sbmc',header: "设备名称", width: 100, sortable: true, dataIndex: 'sbmc'},
				{id:'sbgg',header: "设备规格", width: 80, sortable: true, dataIndex: 'sbgg'},
				{id:'scpjrq',header: "上次评级日期", width: 140, sortable: true, dataIndex: 'scpjrq',
				renderer:function(value,metadata,record,rowIndex,colIndex,store){
					var scpjrq = (value == null)?null:formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
					return scpjrq;
				}},
				{id:'scpjjb',header: "上次评级级别", width: 80, sortable: true, dataIndex: 'scpjjb'},
				{id:'scpjr',header: "上次评级人", width: 100, sortable: true, dataIndex: 'scpjr'},
				{id:'bcpjrq',header: "本次评级日期", width: 140, sortable: true, dataIndex: 'bcpjrq',
				renderer:function(value,metadata,record,rowIndex,colIndex,store){
					var bcpjrq = (value == null)?null:formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
					return bcpjrq;
				}},
				{id:'bcpjjb',header: "本次评级级别", width: 80, sortable: true, dataIndex: 'bcpjjb'},
				{id:'bcpjr',header: "本次评级人", width: 100, sortable: true, dataIndex: 'bcpjr'},
				{id:'remark',header: "备注", width: 100, sortable: true, dataIndex: 'remark'},
				{id:'sbpjCatalog',header: "设备评级分类", width: 100, sortable: true,hidden:true, dataIndex: 'sbpjCatalog'}
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