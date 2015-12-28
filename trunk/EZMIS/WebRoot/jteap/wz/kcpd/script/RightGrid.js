
/**
 * 采购计划
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link4);
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
//		items:['&nbsp;|&nbsp;','<font color="red">*双击记录查看采购明细</font>']
		items:['-',{text:'导出Excel',handler:function(){
		exportExcel(grid,true);
		}},'-','<font color="red"></font>']
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
		autoExpandColumn:"bz",  //自动扩展宽度的列
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
		var whereSql = "obj.pdd.id ='" + jhid +"'";
		mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
		mxGrid.store.load();
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
//			'bh','zdsj','sxsj','bz','zt','jhy','id','person','person.id','person.userName','person.userLoginName'
	       "id",
			"bh",
			"pdsj",
			"czr",
			"sl",
			"ddj",
			"xdj",
			"zksj",
			"ckmc",
			"zt"
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
		    	this.sm,
//				{id:'id',header: "ID", width: 100, sortable: true, dataIndex: 'id'},
				{id:'bh',header: "盘点单编号", width: 100, sortable: true, dataIndex: 'bh'},
				{id:'czr',header: "操作人", width: 80, sortable: false, dataIndex: 'czr',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return value;
					}
				},
				{id:'ckmc',header: "仓库名称", width: 80, sortable: false, dataIndex: 'ckmc',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return value;
					}
				},
				{id:'sl',header: "盘点数量", width: 80, sortable: false, dataIndex: 'sl',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return value;
					}
				},
				{id:'ddj',header: "单价大于", width: 80, sortable: false, dataIndex: 'ddj',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return value;
					}
				},
				{id:'xdj',header: "单价小于", width: 80, sortable: false, dataIndex: 'xdj',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return value;
					}
				},
				{id:'pdsj',header: "盘点时间", width: 150, sortable: true, dataIndex: 'pdsj',
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(!value) return;
				    	var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd hh:mm:ss"); 
						return dt;    
					}},
				{id:'bz',header: "在库时间", width: 80, sortable: false, dataIndex: 'zksj',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return value;
					}
				}
//				{id:'zt',header: "状态", width: 100, sortable: true, dataIndex: 'zt'},
//				{id:'jhy',header: "计划人", width: 100, sortable: true, dataIndex: 'jhy'}
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
	 * 盘点单生效
	 */
	enableCgjh:function(select){
		var selections = this.getSelections();//获取被选中的行
		var rightGrid=this;
		var ids="";
		var pdmxJson = "[";
		Ext.each(selections,function(selectedobj){
			ids+=selectedobj.id+",";//取得他们的id并组装
		});
		var mx = mxGrid.store.data.items;
		for(var i =0;i<mx.length;i++){
			pdmxJson+="{";
			//alert(mx[i].id);
			pdmxJson+="id:'"+mx[i].data.id+"',";
			debugger
			//alert(mx[i].pdsl);
			pdmxJson+="pdsl:'"+mx[i].data.pdsl+"',";
			//alert(mx[i].cyyy);
			pdmxJson+="cyyy:'"+mx[i].data.cyyy+"'}";
			if(i<mx.length){
				pdmxJson+=",";
			}
		}
		pdmxJson+="]";
		if(window.confirm("确认生效选中的条目吗？")){
			Ext.Ajax.request({
				url:link7,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			alert("操作成功");
			 			rightGrid.getStore().on('load',function(){
							var whereSql = "obj.pdd.id =''";
							mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
							//mxGrid.changeToListDS(link2);
							mxGrid.store.load();
			 			});
			 			rightGrid.store.reload();
			 			//mxGrid.store.reload();
			 		}else{
			 			alert(responseObject.msg);
			 		}				
				},
			 	failure:function(){
			 		alert("提交失败");
			 	},
			 	method:'POST',
			 	params: {ids:ids,pdmxJson:pdmxJson}//Ext.util.JSON.encode(selections.keys)			
			});
		}
	}
});



/**
 * 采购计划明细
 */
MxRightGrid=function(){
    var defaultDs=this.getDefaultDS(link14);
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'导出Excel',handler:function(){
		exportExcel(grid,true);
		}},'-','<font color="red"></font>']
	});
	MxRightGrid.superclass.constructor.call(this,{
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
	
//	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
//		var select=rightGrid.getSelectionModel().getSelections()[0];
//		if(select.get("type")=="EFORM"){
//			//var eformUrl=select.get("eformUrl");
//			
//			var url="/jteap/cform/fceform/common/djframe.htm?djsn="+select.get("sn")+"&catalogName="+select.get("eformUrl")
//		    window.open(CONTEXT_PATH+url);
//		}
//		
//		if(select.get('type')=="EXCEL"){
//			var url=CONTEXT_PATH+"/jteap/cform/excelFormRec.jsp?cformId="+select.json.id;
//			var features="menubar=no,toolbar=no,width=800,height=600";
//			window.open(url,"_blank",features);
//		}
//	});
}
Ext.extend(MxRightGrid, Ext.grid.EditorGridPanel, {
	sm:new Ext.grid.RowSelectionModel(),
	
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
			"id",
			"xh",
			"wzda",
			"wzda.wzmc",
			"wzda.xhgg",
			"wzda.kw",
			"wzda.kw.cwmc",
			"wzda.kw.ck",
			"wzda.kw.ck.ckmc",
			"pdd",
			"bh",
			"jldw",
			"pqsl",
			"pqje",
			"slcy",
			"jecy",
			"cyyy",
			"zksj",
			"pjj",
			"pdsl"
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
				{id:'xh',header: "序号", width: 40, sortable: true, hidden: true, dataIndex: 'xh'},
				{id:'wzmc',header: "物资名称", width: 140, sortable: true, dataIndex: 'wzda.wzmc'},
				{id:'xhgg',header: "型号规格", width: 180, sortable: true, dataIndex: 'wzda.xhgg'},
				{id:'wzda.kw.ck.ckmc',header: "仓库", width: 100, sortable: true, dataIndex: 'wzda.kw.ck.ckmc'},
				{id:'wzda.kw.cwmc',header: "库位", width: 100, sortable: true, dataIndex: 'wzda.kw.cwmc'},
				{id:'pjj',header: "盘前单价", width: 70, sortable: true,align:'right', dataIndex: 'pjj',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'pqsl',header: "盘前数量", width: 70, sortable: true,align:'right', dataIndex: 'pqsl',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'pqje',header: "盘前金额", width: 70, sortable: true,align:'right', dataIndex: 'pqje',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'pdsl',header: "盘点数量", width: 70, sortable: true,align:'right', dataIndex: 'pdsl',
				editor:new Ext.form.NumberField({
					xtype:'numberfield',
					decimalPrecision:4,
					minValue:0,
					maxValue:999999.99,
					value:0,
					listeners:{focus:function(a){
					        this.selectText();
					    }
					}
					}),
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}else{
							//record.set('pdsl',value);
							//record.commit();
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'zksj',header: "在库时间", width: 70, sortable: true, dataIndex: 'zksj'},
				{id:'slcy',header: "数量差异", width: 70, sortable: true, dataIndex: 'slcy',
		    		renderer:function(value,metadata,record,rowIndex,colIndex,store){
						//获取盘点数量
						var pdsl = record.get('pdsl');
						//盘前数量
						var pqsl =record.get('pqsl');
						return pdsl-pqsl;
					}
				},
				{id:'jecy',header: "金额差异", width: 70, sortable: true, dataIndex: 'jecy',
		    		renderer:function(value,metadata,record,rowIndex,colIndex,store){
						//获取盘点数量
						var pdsl = record.get('pdsl');
						//盘前数量
						var pqsl =record.get('pqsl');
						//盘前单价
						var pjj =record.get('pjj');
						return (pdsl-pqsl)*pjj;
					}
				},
				{id:'cyyy',header: "差异原因", width: 300, sortable: true, dataIndex: 'cyyy',
				editor:new Ext.form.TextField()}
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



