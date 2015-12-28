
/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link8+"?lydid="+lydid+"&lydmxids="+lydmxids);
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
	RightGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(),
		sm: this.sm,
	    margins:'2px 2px 2px 2px',
		width:600,

		height:250,
		loadMask: true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar 
	});	
	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		
		var btnEnalbe=mainToolbar.items.get('btnEnable');
		var btnModify=mainToolbar.items.get('btnModify');
		
		if(oCheckboxSModel.getSelections().length==1){
			//if(btnEnalbe) btnEnalbe.setDisabled(false);
			if(btnModify) btnModify.setDisabled(false);
		}else{
			//if(btnEnalbe) btnEnalbe.setDisabled(true);
			if(btnModify) btnModify.setDisabled(true);
		}

	});
	
	//双击记录展现相应的采购计划明细表
	this.on("cellclick",function(grid, rowIndex, columnIndex, e){
		var jhid = rightGrid.store.data.items[rowIndex].id;
		var mxids = "";
		var lydmxs = lydmxids.split(',');
		for(var i =0;i<lydmxs.length;i++){
			mxids = mxids+"'"+lydmxs[i]+"'";
			if(i<lydmxs.length-1){
				mxids = mxids+",";
			}
		}
		var whereSql = "obj.lydgl.id ='" + jhid +"' and obj.id in ("+mxids+")";
		mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
		mxGrid.store.load();
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
	            id: 'ID'
	        }, ["ID","LYSQBH","CZR","LLR","GCLB","GCXM","LYSJ","LYBM","ZT","BH","SQBH","LYDQF"]),
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
				{id:'ID',header: "领用单编号", width: 120, sortable: true, dataIndex: 'BH'},
				{id:'LYDQF',header: "领用单区分", width: 100, sortable: true, dataIndex: 'LYDQF',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
					 	return $dictKey("LYDQF",value);
					}
				},
				{id:'SQBH',header: "领用申请单编号", width: 120, sortable: true, dataIndex: 'SQBH'},
				{id:'CZR',header: "操作人", width: 100, sortable: false, dataIndex: 'CZR'},
				{id:'LLR',header: "领料人", width: 100, sortable: false, dataIndex: 'LLR'},
				{id:'GCLB',header: "工程类别", width: 100, sortable: true, dataIndex: 'GCLB'},
				{id:'GCXM',header: "工程项目", width: 100, sortable: true, dataIndex: 'GCXM'},
				{id:'LYBM',header: "领用部门", width: 100, sortable: true, dataIndex: 'LYBM'},
				{id:'LYSJ',header: "领用时间", width: 100, sortable: true, dataIndex: 'LYSJ',
					//时间已经在查询时转换成to_char(lysj,'yyyy-MM-dd')
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(value==null || value ==""){
					    	 return "";
				    	}else{
							return value;       
				    	}
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
		}
	}
	

});


/**
 * 领料单明细
 */
MxRightGrid=function(){
    var defaultDs=this.getDefaultDS(link2);
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
		
		var btnEnalbe=mainToolbar.items.get('btnEnable');
		var btnModify=mainToolbar.items.get('btnModify');
		
		if(oCheckboxSModel.getSelections().length>=1){
			if(btnEnalbe) btnEnalbe.setDisabled(false);
			//if(btnModify) btnModify.setDisabled(false);
		}else{
			if(btnEnalbe) btnEnalbe.setDisabled(true);
			//if(btnModify) btnModify.setDisabled(true);
		}

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
	
		this.on("afteredit",function(e){
		var r = e.record;
		var g = e.grid;
		if(e.value<r.get('pzlysl')){
			g.getView().getCell(e.row,e.column).style.backgroundColor='#FFFF00';
			if(g.getView().getHeaderCell(e.column-1)!=null && g.getView().getHeaderCell(e.column-1).innerText=='批准领用数量'){
				g.getView().getCell(e.row,e.column-1).style.backgroundColor='#FFFF00';
			}
		}
		
	});
}
Ext.extend(MxRightGrid, Ext.grid.EditorGridPanel, {
	sm : new Ext.grid.CheckboxSelectionModel(),
	
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
			"id","xh","lydgl","lydgl.id","lydgl.bh","wzbm","wzbm.id","wzbm.xhgg","wzbm.wzmc","wzbm.dqkc",
			"wzbm.yfpsl","jldw","jhdj","pzlysl","sjlysl","zje","lszkc","wzlysqDetail","wzlysqDetail.id"
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
				{id:'xh',header: "序号", width: 40,align:'center', sortable: false, dataIndex: 'xh'},
				//{id:'lydgl',header: "领用单编号", width: 150, sortable: true, dataIndex: 'lydgl.bh'},
				{id:'wzmc',header: "物资名称", width: 150, sortable: true, dataIndex: 'wzbm.wzmc',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return value+"("+record.get("wzbm.xhgg")+")"
					}
				},
				{id:'jldw',header: "计量单位", width: 100, sortable: true, dataIndex: 'jldw'},
				{id:'jhdj',header: "计划单价", width: 100, sortable: true, dataIndex: 'jhdj',align:'right',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'pzlysl',header: "批准领用数量", width: 100, sortable: true, dataIndex: 'pzlysl',align:'right',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'sjlysl',header: "<span style='color:red'>实际领用数量</span>", width: 80, sortable: true, dataIndex: 'sjlysl',align:'right',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return "<span style='color:red;'>"+parseFloat(value).toFixed(2)+"</span>";
					}
				},
				//{id:'sjlysl',header: "实际领用数量", width: 100, sortable: true, dataIndex: 'sjlysl'},
				{id:'zje',header: "总金额", width: 100,align:'right', sortable: true, dataIndex: 'zje',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return "<font color='red' style='font-weight:bold'>"+(record.get('jhdj')*record.get('sjlysl')).toFixed(2)+"</font>";
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
	},
	/**
	 * 领料单生效
	 */
	 enableLydgl:function(select){
		var selections = this.getSelections();//获取被选中的行
		var wzids = "";
		var ids = "";
		var responseObject = null;
		var lydid = rightGrid.getSelectionModel().getSelected().get('ID');
		Ext.each(selections,function(selectedobj){
			ids+=selectedobj.id+"="+selectedobj.data.sjlysl+",";//取得他们的id并组装
			if(wzids.indexOf(selectedobj.id)<0)
				wzids+="'"+selectedobj.json.wzbm.id+"',";//取得勾选的物资ID
		});
		if(ids!='')
			ids = ids.substring(0,ids.length-1);
		if(wzids!='')
			wzids = wzids.substring(0,wzids.length-1);
		var url = contextPath+"/jteap/wz/wzda/WzdaAction!showListAction.do?";
		AjaxRequest_Sync(url,{queryParamsSql:"obj.id in ("+wzids+")"},function(obj){
			var strReturn=obj.responseText;
			var wzmcs = "";
			responseObject=Ext.util.JSON.decode(strReturn);
			for (var i = 0; i < responseObject.list.length; i++) {
				for (var j = 0; j < selections.length; j++) {
					if(responseObject.list[i].id==selections[j].json.wzbm.id){
						var dqkc = responseObject.list[i].dqkc;
						var yfpsl = responseObject.list[i].yfpsl;
						var sysl = dqkc - yfpsl;
						if(selections[j].data.sjlysl>sysl){
							if(wzmcs.indexOf(selections[j].data.wzbm.wzmc)<0){
								wzmcs += selections[j].data.wzbm.wzmc + "、"
								break;
							}
						}else{
							responseObject.list[i].yfpsl = responseObject.list[i].yfpsl + selections[j].data.sjlysl;
						}
					}
				}
			}
			if(wzmcs!=''){
				alert("您所选中的物资 ： "+wzmcs.substring(0,wzmcs.length-1)+" 的当前自由库存无法满足出库要求");
				return ;
			}
			if(window.confirm("确认生效选中的条目吗？")){
				Ext.Ajax.request({
					url:link7,
					success:function(ajax){
				 		var responseText=ajax.responseText;	
				 		var responseObject=Ext.util.JSON.decode(responseText);
				 		if(responseObject.success){
				 			alert("操作成功");
				 			rightGrid.getStore().on('load',function(){
								var whereSql = "obj.lydgl.id ='" + lydid +"' and obj.zt=0";
								mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
								//mxGrid.changeToListDS(link2);
								mxGrid.store.load();
							});
				 			rightGrid.store.reload();
				 			//rightGrid.getSelectionModel().clearSelections();
				 		}else{
				 			alert(responseObject.msg);
				 		}				
					},
				 	failure:function(){
				 		alert("提交失败");
				 	},
				 	method:'POST',
				 	params: {ids:ids,lydid:lydid}//Ext.util.JSON.encode(selections.keys)			
				});
			 }
		});
		/*
		return ;
		//var rightGrid=this;
		var ids="";
		//需要重新生成领料单的老领料单编号
		var reCreateIds = "";
		Ext.each(selections,function(selectedobj){
			ids+=selectedobj.id+",";//取得他们的id并组装
		});
		var mx_store = mxGrid.getStore();
		var errMsg = "";
		//判断实际领用数量是否小于批准数量
		var doFlag = false;
		for(i=0;i<mx_store.getCount();i++){
			if(mx_store.getAt(i).get("sjlysl")<=0){
				errMsg +=  mx_store.getAt(i).get('wzbm.wzmc')+"、";
			}else{
				if(mx_store.getAt(i).get("sjlysl")< mx_store.getAt(i).get("pzlysl")){
					doFlag = true;
					reCreateIds += mx_store.getAt(i).get("lydgl.id")+",";
				}
			}
		}
		if(errMsg!=""){
			alert("没有可以出库的物资!"+" 物资名称："+errMsg.substring(0,errMsg.length-1));
			return ;
		}
		
		if(window.confirm("确认生效选中的条目吗？")){
			if(doFlag){
				if(!window.confirm("本次领用为不完全领用，要把为领用的物资生成新的领料单吗？")){
					doFlag = false;
				}else{
					reCreateIds = reCreateIds.substring(0,reCreateIds.length-1);
				}
			}
			Ext.Ajax.request({
				url:link7,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			alert("操作成功");
			 			rightGrid.getStore().on('load',function(){
							var whereSql = "obj.lydgl.id =''";
							mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
							//mxGrid.changeToListDS(link2);
							mxGrid.store.load();
						});
			 			rightGrid.store.reload();
			 			rightGrid.getSelectionModel().clearSelections();
			 		}else{
			 			alert(responseObject.msg);
			 		}				
				},
			 	failure:function(){
			 		alert("提交失败");
			 	},
			 	method:'POST',
			 	params: {ids:ids,doFlag:doFlag,reCreateIds:reCreateIds}//Ext.util.JSON.encode(selections.keys)			
			});
		}
	}
*/
			}
});