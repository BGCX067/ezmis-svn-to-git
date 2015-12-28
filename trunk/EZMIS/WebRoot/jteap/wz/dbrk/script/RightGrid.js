
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
//		items:['&nbsp;|&nbsp;','<font color="red"></font>']
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
		var whereSql = "obj.dbd.id ='" + jhid +"'";
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
			"dbsj",
			"czr",
			"ysr",
			"thr",
			"cwfzr",
			"personCzr",
			"personYsr",
			"personThr",
			"personCwfzr",
			
			"personCzr.userName",
			"personYsr.userName",
			"personThr.userName",
			"personCwfzr.userName",
			
			"personCzr.userLoginName",
			"personYsr.userLoginName",
			"personThr.userLoginName",
			"personCwfzr.userLoginName",
			
			"dbxz",
			"dbqdw",
			"dbhdw",
			"dbyy",
			"bz",
			"dbqf",
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
				{id:'bh',header: "调入单编号", width: 100, sortable: true, dataIndex: 'bh'},
				{id:'dbxz',header: "调入性质", width: 100, sortable: true, dataIndex: 'dbxz'},
				{id:'dbqdw',header: "调入前单位", width: 100, sortable: true, dataIndex: 'dbqdw'},
				{id:'dbhdw',header: "调入后单位", width: 100, sortable: true, dataIndex: 'dbhdw'},
				{id:'personCzr.userName',header: "操作人", width: 80, sortable: false, dataIndex: 'personCzr.userName',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return value;
					}
				},
				{id:'personYsr.userName',header: "验收人", width: 80, sortable: false, dataIndex: 'personYsr.userName',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return value;
					}
				},
				{id:'personThr.userName',header: "提货人", width: 80, sortable: false, dataIndex: 'personThr.userName',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return value;
					}
				},
				{id:'personCwfzr.userName',header: "财务负责人", width: 80, sortable: false, dataIndex: 'personCwfzr.userName',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return value;
					}
				},
				{id:'dbsj',header: "调入时间", width: 150, sortable: true, dataIndex: 'dbsj',
				 renderer:function(value,metadata,record,rowIndex,colIndex,store){
				 	var aa = new Date(value);
						var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd HH:mm:ss"); 
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
		var whereSql = "obj.dbd.id in (" + idsIn.substring(0,idsIn.length-1) +")";
		ds.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
		
		ds.load({   
        callback :function(r,options,success){ 
            if(success){
            	var flag = false;
				for(var i=0;i<r.length;i++){   
					var record = r[i];   
					var cgsl = record.data.dbsl;   
					//var jhdhrq = record.data.jhdhrq;   
                	  
					if(cgsl<=0){
						alert("请输入要调入的数量");
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
										var whereSql = "obj.dbd.id =''";
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
			"wzda.xhgg",
			"wzda.wzmc",
			"wzda.kw",
			"wzda.kw.cwmc",
			"wzda.kw.ck",
			"wzda.kw.ck.ckmc",
			"dbd",
			"bh",
			"dbsl",
			"jldw",
			"dbdj"
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
				{id:'wzmc',header: "物资名称", width: 180, sortable: true, dataIndex: 'wzda.wzmc',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value){
							return value + "("+store.getAt(rowIndex).get("wzda.xhgg")+")";
						}
						return "";
					}
				},
				{id:'jldw',header: "计量单位", width: 100, sortable: true, dataIndex: 'jldw'},
				{id:'dbdj',header: "调入单价", width: 100, sortable: true,align:'right', dataIndex: 'dbdj',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(4);
					}
				},
				{id:'dbsl',header: "调入数量", width: 100, sortable: true,align:'right', dataIndex: 'dbsl',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(4);
					}
				}
				/*
				{id:'wzbm',header: "型号规格", width: 100, sortable: true, dataIndex: 'wzdagl.xhgg'},
				{id:'wzbm',header: "仓库(库位编码)", width: 120, sortable: true, dataIndex: 'wzdagl.kw.cwmc',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return  store.getAt(rowIndex).get("wzdagl.kw.ck.ckmc")+ "("+ value+")";
					}
				},
				{id:'cgsl',header: "采购数量", width: 80, sortable: true,align:'right', dataIndex: 'cgsl',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'cgjldw',header: "采购计量单位", width: 80, sortable: true, dataIndex: 'cgjldw'},
				{id:'hsxs',header: "换算系数", width: 100, sortable: true, dataIndex: 'hsxs',align:'right',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'cgy',header: "采购员", width: 100, sortable: true, dataIndex: 'person.userName'},
				{id:'cgfx',header: "采购方向", width: 100, sortable: true, dataIndex: 'cgfx',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
					 	return $dictKey("CGFX",value);
					}
				},
				{id:'jhdj',header: "计划单价", width: 100, sortable: true, dataIndex: 'jhdj',align:'right',
					renderer:function(value){
						return parseInt(value).toFixed(2);
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
				}
				*/
				
//				{id:'cgjhbh',header: "采购计划编号", width: 80, sortable: true, dataIndex: 'cgjhgl.bh'},
//				{id:'zt',header: "状态", width: 70, sortable: true, dataIndex: 'zt',
//					renderer:function(value,metadata,record,rowIndex,colIndex,store){
//					 	return $dictKey("CGJHMX",value);
//					}
//				},
//				{id:'zdsj',header: "分配时间", width: 100, sortable: true, dataIndex: 'cgjhgl.zdsj',
//				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
//						var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
//						return dt;         
//					}
//				},
//				
//				{id:'dhsl',header: "到货数量", width: 100, sortable: true, dataIndex: 'dhsl'},
//				{id:'jhy',header: "计划员", width: 100, sortable: true, dataIndex: 'cgjhgl.jhy'},
//				
//				
//				{id:'sxsj',header: "生效时间", width: 100, sortable: true, dataIndex: 'cgjhgl.sxsj',
//				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
//						var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
//						return dt;         
//					}
//				},
//				
//				{id:'bz',header: "备注", width: 100, sortable: true, dataIndex: 'cgjhgl.bz'}
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



