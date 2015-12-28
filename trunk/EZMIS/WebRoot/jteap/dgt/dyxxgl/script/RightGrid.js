
/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link2);
    defaultDs.load();
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
		var btnModDzz=mainToolbar.items.get('btnModDzz');
		var btnDelDzz=mainToolbar.items.get('btnDelDzz');
	
		var btnModDy=mainToolbar.items.get('btnModDy');
		var btnDelDy=mainToolbar.items.get('btnDelDy');
		
		if(oCheckboxSModel.getSelections().length==1){
			if(btnModDy)btnModDy.setDisabled(false);
			if(btnDelDy)btnDelDy.setDisabled(false);
		}else{
			if(btnModDy)btnModDy.setDisabled(true);
		}
		
		if(oCheckboxSModel.getSelections().length <= 0){
			if(btnModDy)btnModDy.setDisabled(true);
			if(btnDelDy)btnDelDy.setDisabled(true);
		}
		
	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=rightGrid.getSelectionModel().getSelections()[0];
		var formSn="TB_DGT_DYXXK"
		var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&docid="+select.json.id+"&st=02";
		showIFModule(url,"党员信息管理","true",576,460,{},null,null,null,false,"auto");
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
	        }, 
	        ['id','name','bumen','dangzu','sex','minzu','birthday','dzz.dangzu_name']),
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
		      {id:'name',header: "姓名", width: 150, sortable: true, dataIndex: 'name'},
		      {id:'bumen',header: "工作部门", width: 200, sortable: true, dataIndex: 'bumen'},
		      {id:'dangzu_name',header: "党支部", width: 150, sortable: true, dataIndex: 'dzz.dangzu_name'},
		      {id:'sex',header: "性别", width: 120, sortable: true, dataIndex: 'sex'
		      	,renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return $dictKey("XBZD",value);
					}
		      }, 
		      {id:'minzu',header: "民族", width: 120, sortable: true, dataIndex: 'minzu'},
		      {id:'birthday',header: "出生日期", width: 120, sortable: true, dataIndex: 'birthday',
		       renderer:function(value,metadata,record,rowIndex,colIndex,store){
						var dt = "";
						if(value!=null){
							dt=formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
							return dt;
						}else{
							return value;
						}
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
	}

});

