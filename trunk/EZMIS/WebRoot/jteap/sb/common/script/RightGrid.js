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
		emptyMsg: "没有符合条件的数据"
	});
	RightGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(),
	 	renderTo:"divRightGrid",
		sm: this.sm,
	    margins:'2px 2px 2px 2px',
		width:590,
		height:510,
		loadMask: true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar,
		stripeRows: true
	});	
	this.on('beforerender',function(param){
		this.sm.singleSelect  = true;        
	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=rightGrid.getSelectionModel().getSelections()[0];
		window.close();
		btnYxtz_Click();
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
				{id:'sbbm',header: "设备编码", width: 180, sortable: true, dataIndex: 'sbbm'},
				{id:'ybmc',header: "设备名称", width: 200, sortable: true, dataIndex: 'ybmc'},
				{id:'yt',header: "用途", width: 180, sortable: true,hidden:true, dataIndex: 'yt'},
				{id:'xsjgf',header: "型式及规范", width: 160, sortable: true, dataIndex: 'xsjgf' },
				{id:'dw',header: "单位", width: 60, sortable: true,hidden:true, dataIndex: 'dw'},
				{id:'sl',header: "数量", width: 80, sortable: true,hidden:true, dataIndex: 'sl'},
				{id:'azdd',header: "安装地点", width: 100, sortable: true,hidden:true, dataIndex: 'azdd'},
				{id:'xtth',header: "系统图号", width: 100, sortable: true,hidden:true, dataIndex: 'xtth'},
				{id:'remark',header: "备注", width: 100, sortable: true,hidden:true, dataIndex: 'remark'},
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