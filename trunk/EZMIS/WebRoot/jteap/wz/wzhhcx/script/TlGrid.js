
/**
 * 字段列表
 */
TlGrid=function(){
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
		}},'-','<font color="red">*双击查看详细信息</font>']
	});
	TlGrid.superclass.constructor.call(this,{
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
		var select=tlGrid.getSelectionModel().getSelections()[0];
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
			
			var url=link29+"&docid="+select.json.tldgl.id;
			result=showModule(url,true,800,645);
		}
	});
	
}
Ext.extend(TlGrid, Ext.grid.GridPanel, {
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
	        "je",
			"id",
			"xh",
			"wzda",
			"wzda.dqkc",
			"wzda.yfpsl",
			"wzda.jhdj",
			"wzda.xhgg",
			"wzda.wzmc",
			"wzda.kw",
			"wzda.kw.cwmc",
			"wzda.kw.ck",
			"wzda.kw.ck.ckmc",
			"tlsl",
			"jldw",
			"tldgl",
			
			"tldgl.id",
			"tldgl.bh",
			"tldgl.tlsj",
			"tldgl.czr",
			"tldgl.ysr",
			"tldgl.tlr",
			"tldgl.cwfzr",
			"tldgl.personCzr",
			"tldgl.personYsr",
			"tldgl.personTlr",
			"tldgl.personCwfzr",
			
			"tldgl.personCzr.userName",
			"tldgl.personYsr.userName",
			"tldgl.personTlr.userName",
			"tldgl.personCwfzr.userName",
			
			"tldgl.personCzr.userLoginName",
			"tldgl.personYsr.userLoginName",
			"tldgl.personTlr.userLoginName",
			"tldgl.personCwfzr.userLoginName",
			
			"tldgl.tlbm",
			"tldgl.gclb",
			"tldgl.gcxm",
			"tldgl.tlyy",
			"tldgl.zt"
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
				{id:'tldgl.bh',header: "退料单编号", width: 70, sortable: true, dataIndex: 'tldgl.bh'},
				{id:'tldgl.tlsj',header: "退料时间", width: 150, sortable: true, dataIndex: 'tldgl.tlsj',
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(!value) return;
				    	var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd HH:mm:ss"); 
						return dt;    
				}},
				{id:'tldgl.gclb',header: "工程类别", width: 70, sortable: true, dataIndex: 'tldgl.gclb'},
				{id:'tldgl.gcxm',header: "工程项目", width: 70, sortable: true, dataIndex: 'tldgl.gcxm'},
				{id:'tldgl.tlbm',header: "退料部门", width: 70, sortable: true, dataIndex: 'tldgl.tlbm'},
				/*
				{id:'wzda.kw.ck.ckmc',header: "所属仓库", width: 70, sortable: true, dataIndex: 'wzda.kw.ck.ckmc'},
				{id:'wzda.wzmc',header: "物资名称", width: 180, sortable: true, dataIndex: 'wzda.wzmc',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value){
							return value + "("+store.getAt(rowIndex).get("wzda.xhgg")+")";
						}
						return "";
					}
				},
				*/
				{id:'jldw',header: "计量单位", width: 60, sortable: true, dataIndex: 'jldw'},
				{id:'tlsl',header: "退料数量", width: 100, sortable: true, dataIndex: 'tlsl',align:'right',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(4);
					}
				},
				{id:'wzda.jhdj',header: "单价", width: 100, sortable: true, dataIndex: 'wzda.jhdj',align:'right',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(4);
					}
				},
				{id:'je',header: "金额", width: 100, sortable: true, dataIndex: 'je',align:'right',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						/*
						var sl = record.get('tlsl');
						var dj = record.get('wzda.jhdj');
						return parseFloat(sl*dj).toFixed(4);
						*/
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(4);
					}
				}
				/*,
				{id:'tldgl.personCzr.userName',header: "操作人", width: 80, sortable: false, dataIndex: 'tldgl.personCzr.userName',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return value;
					}
				},
				{id:'tldgl.personYsr.userName',header: "验收人", width: 80, sortable: false, dataIndex: 'tldgl.personYsr.userName',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return value;
					}
				},
				{id:'tldgl.personTlr.userName',header: "退料人", width: 80, sortable: false, dataIndex: 'tldgl.personTlr.userName',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return value;
					}
				},
				{id:'tldgl.personCwfzr.userName',header: "财务负责人", width: 80, sortable: false, dataIndex: 'tldgl.personCwfzr.userName',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return value;
					}
				}*/
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

