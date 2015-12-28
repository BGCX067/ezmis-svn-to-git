/**
 * 字段列表
 */
RightGrid=function(){
	
    var defaultDs=this.getDefaultDS(link4+"&limit=20");
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
		tbar:this.pageToolbar,
		stripeRows: true
	});	
	this.on('beforerender',function(param){
		//如何是'基础台帐'可以多选，如何是'运行台帐'只能单选("1"表示基础台帐;"2"表示运行台帐)
		if(type == 1){                   
			this.sm.singleSelect = false;        
		}else if(type == 2){
			this.sm.singleSelect  = true;        
		}	
	});
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		
		var btnDel=mainToolbar.items.get('btnDel');
		var btnModify=mainToolbar.items.get('btnModify');
		
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
		obj.kks = select.data.kks;
		obj.sbbm = select.data.sbbm;
		obj.ybmc = select.data.ybmc;
		obj.xsjgf = select.data.xsjgf;
		obj.dw = select.data.dw;
		obj.sl = select.data.sl;
		obj.azdd = select.data.azdd;
		obj.xtth = select.data.xtth;
		obj.yt = select.data.yt;
		obj.remark = select.data.remark;
		obj.sbtzCatalogId = select.data.sbtzCatalog.id;
		var url = contextPath + '/jteap/sb/sbtzgl/jctz/jctzinfo.jsp' + "?modi=m&sbtzCatalogId="+obj.sbtzCatalogId+"&type=show";;
		showIFModule(url,"基础台帐信息","true",500,360,obj);
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
				"id","sbtzCatalog","kks","sbbm","ybmc","yt","xsjgf","dw","sl","azdd","xtth","remark","cjsj"
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
				{id:'kks',header: "KKS码", width: 80, sortable: true,hidden:true, dataIndex: 'kks'},
				{id:'sbbm',header: "设备编码", width: 100, sortable: true, dataIndex: 'sbbm'},
				{id:'ybmc',header: "设备名称", width: 100, sortable: true, dataIndex: 'ybmc'},
				{id:'yt',header: "用途", width: 180, sortable: true, dataIndex: 'yt'},
				{id:'xsjgf',header: "型式及规范", width: 95, sortable: true, dataIndex: 'xsjgf' },
				{id:'dw',header: "单位", width: 60, sortable: true, dataIndex: 'dw'},
				{id:'sl',header: "数量", width: 80, sortable: true, dataIndex: 'sl'},
				{id:'azdd',header: "安装地点", width: 100, sortable: true, dataIndex: 'azdd'},
				{id:'xtth',header: "系统图号", width: 100, sortable: true, dataIndex: 'xtth'},
				{id:'remark',header: "备注", width: 100, sortable: true, dataIndex: 'remark'},
				{id:'cjsj',header: "创建时间", width: 100, sortable: true,hidden:true, dataIndex: 'cjsj'},
				{id:'sbtzCatalog',header: "设备台帐分类", width: 100, sortable: true,hidden:true, dataIndex: 'sbtzCatalog'}
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