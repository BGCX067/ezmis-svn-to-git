
/**
 * 字段列表
 */
BbIOGrid=function(bbindexid){
    var defaultDs=this.getDefaultDS(link10+"?bbindexid="+bbindexid);
    var grid=this;
    var fname; //默认的选中的字段
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '',
		items:[]
	});
	BbIOGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(),
		//sm: this.sm,
	    margins:'2px 2px 2px 2px',
		width:280,
		deferRowRender:false,//要设置列表的默认显示必须设置该属性为false
		height:360,
		loadMask: true,
		frame:true,
		tbar:this.pageToolbar 
	});	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		if(oCheckboxSModel.getSelections().length==1){
			var bbioGrid=dataSelectIndex.getBbioGrid();
			var select=bbioGrid.getSelectionModel().getSelections()[0];
			if(select!=null){
				//要分页就调用link11
				var url=link12+"?bbioid="+select.json.id;
				var bbsjzdGrid=dataSelectIndex.getBbSjzdGrid();
		    	bbsjzdGrid.changeToListDS(url);
		    	bbsjzdGrid.getStore().reload();
		    	//让字段默认被选中
		    	bbsjzdGrid.getStore().on("load",function(store,records,options){
					for(var i=0;i<records.length;i++){
						if(fname==records[i].json.fname){
							bbsjzdGrid.getSelectionModel().selectRow(i);
						}
					}
				});
			}
		}
	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		
	});
	
	this.setDefaultFname=function(dafalutFname){
		fname=dafalutFname;
	}
}
Ext.extend(BbIOGrid, Ext.grid.GridPanel, {
	//sm:new Ext.grid.CheckboxSelectionModel(),
	
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
			'id','vname','bbbm','cvname'
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
		    	//this.sm,
				{id:'id',header: "id", width: 100, sortable: true, dataIndex: 'id',hidden:true},
				{id:'vname',header: "视图名", width: 120, sortable: true, dataIndex: 'vname'},
				{id:'cvname',header: "视图中文名", width: 130, sortable: true, dataIndex: 'cvname'}
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
			 			bbIOGrid.getStore().reload();
			 			var url=link8;
				    	bbSjzdGrid.changeToListDS(url);
				    	bbSjzdGrid.getStore().reload();
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

