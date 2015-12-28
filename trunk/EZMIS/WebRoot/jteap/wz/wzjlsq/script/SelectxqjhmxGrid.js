
/**
 * 采购计划
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link13+"?bm="+bm+"&gclb="+gclb+"&gcxm="+gcxm);
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据"
		//items:['&nbsp;|&nbsp;','<font color="red">*双击记录查看采购明细</font>']
//		items:['-',{text:'导出Excel',handler:function(){
//		exportExcel(grid,true);
//		}},'-','<font color="red"></font>']
	});
	RightGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(),
		sm: this.sm,
	    margins:'2px 2px 2px 2px',
	    title:'需求计划列表',
		width:900,
		height:250,
		loadMask: true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar 
	});	
	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		
		var btnEnable=mainToolbar.items.get('btnEnable');
		var btnDelCForm=mainToolbar.items.get('btnDel');
		var btnModifyCForm=mainToolbar.items.get('btnModify');
		var btnPrint=mainToolbar.items.get('btnPrint');
		
		if(oCheckboxSModel.getSelections().length==1){
			if(btnModifyCForm) btnModifyCForm.setDisabled(false);
			if(btnPrint) btnPrint.enable();
		}else{
			if(btnModifyCForm) btnModifyCForm.setDisabled(true);
			if(btnPrint) btnPrint.disable();
		}
		
		if(oCheckboxSModel.getSelections().length<=0){
			if(btnDelCForm) btnDelCForm.setDisabled(true);
			if(btnEnable) btnEnable.disable();
		}else{
			if(btnDelCForm) btnDelCForm.setDisabled(false);
			if(btnEnable) btnEnable.enable();
		}
	});
	
	//双击记录展现相应的采购计划明细表
	this.on("cellclick",function(grid, rowIndex, columnIndex, e){
		var jhid = rightGrid.store.data.items[rowIndex].id;
		//var whereSql = "obj.cgjhgl.id ='" + jhid +"'";
		lymxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',xqjhbh:jhid,zt:1};
		lymxGrid.store.load({callback:function(r,options,success){
			var ids = "";
			if(tmpStore!=null){
				for (var i = 0; i < tmpStore.getCount(); i++) {
					ids += tmpStore.getAt(i).get('XQJHMXBM')+",";
				}
			}
			var mxstore = lymxGrid.store;
		for (var i = 0; i < r.length; i++) {
			if(ids.indexOf(r[i].get('XQJHMXID'))>=0){
				mxstore.remove(r[i]);
			}
		}
		}});
	});
	
}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
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
	            id: 'XQJHID'
	        }, [
			"XQJHBH",'XQJHID','XQJHSQBH','SXSJ','GCLB','SQBM','GCXM','STATUS','OPERATOR'
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
//				{id:'id',header: "ID", width: 100, sortable: true, dataIndex: 'id'},
				{id:'XQJHBH',header: "单据编号", width: 100, sortable: true, dataIndex: 'XQJHBH'},
				{id:'GCLB',header: "工程类别", width: 100, sortable: true, dataIndex: 'GCLB'},
				{id:'GCXM',header: "工程项目", width: 100, sortable: true, dataIndex: 'GCXM'},
				{id:'SQBM',header: "申请部门", width: 100, sortable: true, dataIndex: 'SQBM'},
				{id:'OPERATOR',header: "操作员", width: 100, sortable: true, dataIndex: 'OPERATOR'},
				{id:'SXSJ',header: "生效时间", width: 100, sortable: true, dataIndex: 'SXSJ',
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(!value) return;
						var dt = formatDate(new Date(value),"yyyy-MM-dd"); 
						return dt;         
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
		}else return 'cancle';
	},
	/**
	 * 采购计划单生效
	 */
	enableCgjh:function(select){
		var selections = this.getSelections();//获取被选中的行
		var rightGrid=this;
		var ids="";
		Ext.each(selections,function(selectedobj){
			ids+=selectedobj.id+",";//取得他们的id并组装
		});
		if(window.confirm("确认生效选中的条目吗？")){
			Ext.Ajax.request({
				url:link7,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			alert("操作成功");
			 			rightGrid.store.reload();
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



/**
 * 采购计划明细
 */
LyMxRightGrid=function(){
    var defaultDs=this.getDefaultDS(link14);
    var grid=this;
//    this.pageToolbar=new Ext.PagingToolbar({
//	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
//	    store: defaultDs,
//	    displayInfo: true,
//	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
//		emptyMsg: "没有符合条件的数据",
//		items:['-',{text:'导出Excel',handler:function(){
//		exportExcel(grid,true);
//		}},'-','<font color="red"></font>']
//	});
	LyMxRightGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(),
		sm: this.sm,
	    margins:'2px 2px 2px 2px',
		width:900,

		height:300,
		loadMask: true,
		frame:true,
		region:'center'
		//tbar:this.pageToolbar 
	});	
	
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		var select=lymxGrid.getSelectionModel().getSelected();
		if(select){
			window.returnValue = select.json;
		}
	});
	
	//双击记录展现相应的采购计划明细表
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		/*
		var select=lymxGrid.getSelectionModel().getSelected();
		if(select){
			var arg = window.dialogArguments;
			window.returnValue = select.json;
		}
		*/
	});
	
}
Ext.extend(LyMxRightGrid, Ext.grid.GridPanel, {
	sm:new Ext.grid.RowSelectionModel({
		//singleSelect:true
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
	            id: 'XQJHMXID'
	        }, [
			"XQJHMXID","XQJHBH", "WZBM", "XH","PZSL", "JLDW","JHDJ", "FREE","CGSL","DHSL","LYSL","SLSL","PJJ","KWBM","WZMC","CKMC","DQKC","YFPSL","XHGG"
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
//				{id:'id',header: "ID", width: 100, sortable: true, dataIndex: 'id'},
				{id:'WZMC',header: "物资", width: 150, sortable: true, dataIndex: 'WZMC',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return value + "("+record.get('XHGG')+")";
					}
				},
				{id:'CKMC',header: "仓库", width: 80, sortable: true, dataIndex: 'CKMC'},
				{id:'JLDW',header: "计量单位", width: 60, sortable: true, dataIndex: 'JLDW'},
				{id:'JHDJ',header: "计划单价", width: 100, sortable: true, dataIndex: 'JHDJ'},
				{id:'PJJ',header: "平均价", width: 100, sortable: true, dataIndex: 'PJJ'},
				{id:'PZSL',header: "批准数量", width: 100, sortable: true, dataIndex: 'PZSL'},
				{id:'PZSL',header: "已分配", width: 80, sortable: true, dataIndex: 'PZSL'},
				{id:'CGSL',header: "已采购", width: 80, sortable: true, dataIndex: 'CGSL'},
				{id:'YFPSL',header: "已分配", width: 80, sortable: true, dataIndex: 'YFPSL'},
				{id:'DHSL',header: "已到货", width: 80, sortable: true, dataIndex: 'DHSL',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
					 	return value;
					}
				},
				{id:'SLSL',header: "已申领", width: 80, sortable: true, dataIndex: 'SLSL',
					renderer:function(value){
						return value;
					}
				},
				{id:'LYSL',header: "已领用", width: 100, sortable: true, dataIndex: 'LYSL',
					renderer:function(value){
						return value;
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

var lymxGrid=new LyMxRightGrid(); 

