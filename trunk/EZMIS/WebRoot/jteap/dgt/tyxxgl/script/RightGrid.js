
/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link2);
    var grid=this;
     defaultDs.load({params:{start:0,limit:11}});
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: 11,
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
		var btnMod=mainToolbar.items.get('btnMod');
		var btnDel=mainToolbar.items.get('btnDel');
		var btnLt=mainToolbar.items.get('btnLt');
		if(oCheckboxSModel.getSelections().length==1){
			if(btnMod)btnMod.setDisabled(false);
			if(btnDel)btnDel.setDisabled(false);
			if(btnLt)btnLt.setDisabled(false);
		}else{
			if(btnMod)btnMod.setDisabled(true);
		}
		if(oCheckboxSModel.getSelections().length <= 0){
			if(btnMod)btnMod.setDisabled(true);
			if(btnDel)btnDel.setDisabled(true);
			if(btnLt)btnLt.setDisabled(true);
		}
		
	});
	
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=rightGrid.getSelectionModel().getSelections()[0];
		var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&docid="+select.json.id+"&st=02";
		showIFModule(url,"自定义表单","true",650,340,{},null,null,null,false,"auto");
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
	        ['id','name','bumen','tuanzu','tuan_zhiwu','sex','birthday','zhengzhi','status']),
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
		      {id:'bumen',header: "工作部门", width: 150, sortable: true, dataIndex: 'bumen'},
		      {id:'tuanzu',header: "团支部", width: 150, sortable: true, dataIndex: 'tuanzu'
		      	,renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return $dictKey("TZBZZ",value)}
		      },
		      {id:'tuan_zhiwu',header: "团内职务", width: 150, sortable: true, dataIndex: 'tuan_zhiwu'},
		      {id:'sex',header: "性别", width: 150, sortable: true, dataIndex: 'sex'
		      ,	renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return $dictKey("XBZD",value)}
				}, 
		      {id:'birthday',header: "出生日期", width: 150, sortable: true, dataIndex: 'birthday',
		     	 renderer:function(value,metadata,record,rowIndex,colIndex,store){
						var dt = "";
						if(value!=null){
							dt=formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
							return dt;
						}else{
							return value;
						}
					}
		      },
		      {id:'zhengzhi',header: "政治面貌", width: 100, sortable: true, dataIndex: 'zhengzhi'
		    	,	renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return $dictKey("ZZMM",value)}
		      },
		      {id:'status',header: "状态", width: 50, sortable: true, dataIndex: 'status'
		      ,renderer:function(value,metadata,record,rowIndex,colIndex,store){
				var a='';
						if(value=='0'){
							a='在团';
						}else if(value=='1'){
							a='离团';
						}
							return a;
						}}
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

