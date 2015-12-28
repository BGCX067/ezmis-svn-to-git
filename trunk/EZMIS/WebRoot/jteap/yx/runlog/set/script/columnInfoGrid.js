
/**
 * 字段列表
 */
ColumnInfoGrid=function(){
	var url = link8+"?limit=17&tableId="+tableId;
    var defaultDs=this.getDefaultDS(url);
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: 17,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据"
	});
	ColumnInfoGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(),
		sm: this.sm,
	    margins:'2px 2px 2px 2px',
		width:838,
		height:478,
		loadMask: true,
		frame:true,
		border: false,
		tbar:mainTool,
		items: [searchPanel,this.pageToolbar],
		renderTo:'divCenter'
	});	
	
	//当有数据被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		var btnDel=mainTool.items.get('btnDel');
		var btnModi=mainTool.items.get('btnModi');
		
		if(oCheckboxSModel.getSelections().length==1){
			if(btnModi) btnModi.setDisabled(false);
		}else{
			if(btnModi) btnModi.setDisabled(true);
		}
		
		if(oCheckboxSModel.getSelections().length<=0){
			if(btnDel) btnDel.setDisabled(true);
		}else{
			if(btnDel) btnDel.setDisabled(false);
		}		
	});
	
}
Ext.extend(ColumnInfoGrid, Ext.grid.GridPanel, {
	
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
				"id","tableId","columnCode","columnName","edingzhi",
				"jiliangdanwei","sisCedianbianma","sortno","dataTableCode","jizu"
	        ]),
	        remoteSort: true,
	        //如果为true,每次store在加载时或者一条记录被删除时, 清除所有改动过的记录信息
	        pruneModifiedRecords: true
	    });
		return ds;
	},
    
	/**
	 * DqgzAction 列模型
	 */
	getColumnModel:function(){
		
		var grid = this;
				
	    var cm=new Ext.grid.ColumnModel([
		    	this.sm,
				{id:'id',header: "编号", width: 100, sortable: true, hidden:true, dataIndex: 'id'},
				{id:'tableId',header: "报表编号", width: 100, sortable: true, hidden:true, dataIndex: 'tableId'},
				{id:'columnCodes',header: "指标名", width: 100, sortable: true, hidden:true, dataIndex: 'columnCode'},
				{id:'columnName',header: "指标名称", width: 200, sortable: true, dataIndex: 'columnName'},
				{id:'jizu',header: "机组", width: 100, sortable: true, dataIndex: 'jizu'},
				{id:'dataTableCode',header: "取数表编码", width: 100, sortable: true, dataIndex: 'dataTableCode'},
				{id:'sisCedianbianma',header: "测点编码", width: 100, sortable: true, dataIndex: 'sisCedianbianma'},
				{id:'edingzhi',header: "额定值", width: 100, sortable: true, dataIndex: 'edingzhi'},
				{id:'jiliangdanwei',header: "计量单位", width: 100, sortable: true, dataIndex: 'jiliangdanwei'},
				{id:'sortno',header: "排序号", width: 100, sortable: true, dataIndex: 'sortno'}
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
		var grid=this;
		var ids="";
		Ext.each(selections,function(selectedobj){
			ids+=selectedobj.id+",";//取得他们的id并组装
		});
		if(window.confirm("确认删除选中的条目吗？")){
			Ext.Ajax.request({
				url:link9,
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
			 	params: {ids:ids}//Ext.util.JSON.encode(selections.keys)			
			});
		}
	}
	
});