
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
		var whereSql = "obj.wzbfd.id ='" + jhid +"'";
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
			"bfrq",
			"hsry",
			"clry",
			"zg",
			"cld",
			"personHsry",
			"personClry",
			"personZg",
			"personCld",
			
			"personHsry.userName",
			"personClry.userName",
			"personZg.userName",
			"personCld.userName",
			
			"personHsry.userLoginName",
			"personClry.userLoginName",
			"personZg.userLoginName",
			"personCld.userLoginName",
			"bz",
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
				{id:'bh',header: "报废单编号", width: 100, sortable: true, dataIndex: 'bh'},
				{id:'personHsry.userName',header: "核算人员", width: 100, sortable: false, dataIndex: 'personHsry.userName',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return value;
					}
				},
				{id:'personClry.userName',header: "处理人员", width: 100, sortable: false, dataIndex: 'personClry.userName',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return value;
					}
				},
				{id:'personZg.userName',header: "主管", width: 100, sortable: false, dataIndex: 'personZg.userName',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return value;
					}
				},
				{id:'personCld.userName',header: "厂领导", width: 100, sortable: false, dataIndex: 'personCld.userName',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return value;
					}
				},
				{id:'bfrq',header: "报废日期", width: 100, sortable: true, dataIndex: 'bfrq',
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(!value) return;
				    	var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
						return dt;    
					}},
				{id:'bz',header: "备注", width: 400, sortable: true, dataIndex: 'bz'}
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
	 * 采购计划单生效
	 */
	enableCgjh:function(select){
		var selections = this.getSelections();//获取被选中的行
		var rightGrid=this;
		var ids="";
		var idsIn = "";
		var mxstore = mxGrid.getStore();
		Ext.each(selections,function(selectedobj){
			ids+=selectedobj.id+",";//取得他们的id并组装
			idsIn +="'"+selectedobj.id+"',";
		});
		var doEnable = false;
		var ds = mxGrid.getDefaultDS(link14);
		var whereSql = "obj.wzbfd.id in (" + idsIn.substring(0,idsIn.length-1) +")";
		ds.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
		
		ds.load({   
        callback :function(r,options,success){ 
            if(success){
            	var flag = false;
				for(var i=0;i<r.length;i++){   
					var record = r[i];   
					var bfsl = record.data.bfsl;   
					var dqkc = record.data.wzda.dqkc;
					var yfpsl = record.data.wzda.yfpsl;
					var yxkc = dqkc - yfpsl;
					//var jhdhrq = record.data.jhdhrq;   
                	  
					if(bfsl<=0){
						alert("请输入报废物资的数量");
						flag = false;
						return ;
					}else if(bfsl>yxkc){
						alert("物资："+ record.data.wzda.wzmc +" 的当前库存量无法满足出库要求");
						flag = false;
						return ;
					}else{
						flag = true;
					}
				}
				if(flag){
					if(window.confirm("确认生效选中的条目吗？")){
						Ext.Ajax.request({
							url:link7,
							success:function(ajax){
						 		var responseText=ajax.responseText;	
						 		var responseObject=Ext.util.JSON.decode(responseText);
						 		if(responseObject.success){
						 			alert("操作成功");
						 			rightGrid.getStore().on('load',function(){
										var whereSql = "obj.wzbfd.id =''";
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
						 	params: {ids:ids}//Ext.util.JSON.encode(selections.keys)			
						});
					}
				}
			}else{
				alert("提交失败");
			}
    	}});    
	}
});



/**
 * 采购计划明细
 */
MxRightGrid=function(){
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
Ext.extend(MxRightGrid, Ext.grid.GridPanel, {
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
			"wzda.dqkc",
			"wzda.yfpsl",
			"wzda.wzmc",
			"wzda.xhgg",
			"wzda.kw",
			"wzda.kw.cwmc",
			"wzda.kw.ck",
			"wzda.kw.ck.ckmc",
			"wzbfd",
			"wzbfd.bh",
			"jldw",
			"kcj",
			"bfsl",
			"clj",
			"zzsl",
			"glf",
			"ddyzf",
			"clje",
			"bfje"
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
				{id:'xh',header: "序号", width: 40, sortable: true, dataIndex: 'xh'},
				{id:'wzmc',header: "报废物资", width: 180, sortable: true, dataIndex: 'wzda.wzmc',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value){
							return value + "("+store.getAt(rowIndex).get("wzda.xhgg")+")";
						}
						return "";
					}
				},
				{id:'jldw',header: "计量单位", width: 100, sortable: true, dataIndex: 'jldw'},
				{id:'bfsl',header: "报废数量", width: 100, sortable: true,align:'right', dataIndex: 'bfsl',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(4);
					}
				},
				{id:'kcj',header: "库存价", width: 100, sortable: true,align:'right', dataIndex: 'kcj',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(4);
					}
				},
				{id:'clj',header: "处理价", width: 100, sortable: true,align:'right', dataIndex: 'clj',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(4);
					}
				},
				{id:'clje',header: "处理金额", width: 100, sortable: true,align:'right', dataIndex: 'clje',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(4);
					}
				},
				{id:'glf',header: "管理费", width: 100, sortable: true,align:'right', dataIndex: 'glf',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(4);
					}
				},
				{id:'ddyzf',header: "代垫运杂费", width: 100, sortable: true,align:'right', dataIndex: 'ddyzf',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(4);
					}
				},
				{id:'bfje',header: "报废金额", width: 100, sortable: true,align:'right', dataIndex: 'bfje',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(4);
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



