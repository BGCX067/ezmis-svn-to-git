
/**
 * 字段列表
 */
CgGrid=function(){
    var defaultDs=this.getDefaultDS(link20);
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
	CgGrid.superclass.constructor.call(this,{
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
		var select=cgGrid.getSelectionModel().getSelections()[0];
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
			var url=link21+"&docid="+select.json.cgjhgl.id;
			result=showModule(url,true,800,645);
		}
	});
	
}
Ext.extend(CgGrid, Ext.grid.GridPanel, {
	sm:new Ext.grid.RowSelectionModel({
		singleSelect:true
	}),
	
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
			'id','cgjhgl','cgjhgl.bh','cgjhgl.jhy','cgjhgl.zdsj','cgjhgl.sxsj','cgjhgl.bz','wzdagl','wzdagl.wzmc','wzdagl.xhgg','wzdagl.kw','wzdagl.kw.cwmc','wzdagl.kw.ck','wzdagl.kw.ck.id','wzdagl.kw.ck.ckmc','xh','jhdj','cgjldw','cgsl','hsxs','jhdhrq','dhsl','cgfx','cgy','je','zt','person','person.id','person.userName',"XqjhDetail"
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
		    	{id:'xh',header: "序号", width: 40, sortable: true, 
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
					 	return rowIndex+1;
					}
				},
				{id:'cgjhgl.bh',header: "采购计划编号", width: 80, sortable: true, dataIndex: 'cgjhgl.bh'},
				{id:'cgjhgl.sxsj',header: "生效时间", width: 100, sortable: true, dataIndex: 'cgjhgl.sxsj',
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(!value) return;
						var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
						return dt;         
					}
				},
				{id:'jhdj',header: "计划单价", width: 100, sortable: true, dataIndex: 'jhdj',align:'right',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'cgjldw',header: "采购计量单位", width: 80, sortable: true, dataIndex: 'cgjldw'},
				{id:'cgsl',header: "采购数量", width: 80, sortable: true,align:'right', dataIndex: 'cgsl',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'je',header: "金额", width: 100, sortable: true, dataIndex: 'je',align:'right',
					renderer:function(value){
						return "<span><font color='red'><b>"+parseInt(value).toFixed(2)+"</b></font></span>";
					}
				},
				{id:'jhdhrq',header: "计划到货日期", width: 100, sortable: true, dataIndex: 'jhdhrq',
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(!value) return;
						var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
						return dt;         
					}
				},
				{id:'cgy',header: "采购员", width: 100, sortable: true, dataIndex: 'person.userName'}
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

