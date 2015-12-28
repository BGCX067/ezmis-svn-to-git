
/**
 * 字段列表
 */
RightGrid=function(type){
    var defaultDs = this.getDefaultDS(link4);    //新系统数据
    var grid=this;
   	this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'导出Excel',handler:function(){
			export_Excel();
		}},'-','<font color="red"></font>']
	});
	var viewConfig =  {onLoad : Ext.emptyFn,  
            listeners : {  
                beforerefresh : function(v) {  
                    v.scrollTop = v.scroller.dom.scrollTop;  
                    v.scrollHeight = v.scroller.dom.scrollHeight;  
                },  
                refresh : function(v) {  
                    v.scroller.dom.scrollTop = v.scrollTop  
                            + (v.scrollTop == 0  
                                    ? 0  
                                    : v.scroller.dom.scrollHeight  
                                            - v.scrollHeight);  
                }  
            } };  
	RightGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(type),
		sm: this.sm,
	    margins:'2px 2px 2px 2px',
		width:600,
		height:350,
		autoScroll:true,
		loadMask: true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar,
		deferRowRender:false,
		viewConfig:viewConfig
	});	
	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		
		var btnEnalbe=mainToolbar.items.get('btnEnable');
		var btnModify=mainToolbar.items.get('btnModify');
		var btnZf = mainToolbar.items.get('btnZf');
		
		
		if(oCheckboxSModel.getSelections().length==1){
			if(btnModify) {
				if(btnZf.getText()=='撤销作废'){
    		 		btnModify.setDisabled(true);
    			}else{
    				btnModify.setDisabled(false);
    			}
			}	
		}else{
			if(btnModify) btnModify.setDisabled(true);
		}

	});
	
	this.store.on('load',function(){
        //rightGrid.getSelectionModel().selectFirstRow();
		var lydid = typeof(rightGrid.store.data.items[0]) == 'undefined'?"":rightGrid.store.data.items[0].id;
		var btnZf = mainToolbar.items.get('btnZf');
    	var zt = 0;
    	if(btnZf.getText()=='撤销作废'){
    		zt = 9;
    	}
		var whereSql = "obj.lydgl.id ='" + lydid +"' and obj.zt='"+zt+"'";
		mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql,zt:zt};
		mxGrid.store.load();
    });
    this.on('render',function() {
         rightGrid.store.load();
    });
    this.on('cellclick',function(grid, rowIndex, columnIndex, e) {
    	var btnZf = mainToolbar.items.get('btnZf');
    	var zt = 0;
    	if(btnZf.getText()=='撤销作废'){
    		zt = 9;
    	}
        var aaa = rightGrid.store.data.items[rowIndex].id;
		var whereSql = "obj.lydgl.id ='" + aaa +"' and obj.zt='"+zt+"'";
		mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql,zt:zt};
		mxGrid.store.load();
    });
	
	//双击记录展现相应的采购计划明细表
	/**
	this.on("cellclick",function(grid, rowIndex, columnIndex, e){
		//var nowGridid = nowGrid.store.data.items[rowIndex].data.ID;
		//var historyGridid = historyGrid.store.data.items[rowIndex].data.ID;
		var jhid = "";
		if(typeof(nowGrid.store.data.items[rowIndex]) == "undefined"){
			jhid = historyGrid.store.data.items[rowIndex].data.ID;
		}else{
			jhid = nowGrid.store.data.items[rowIndex].data.ID;
		}
		//var jhid = (typeof(nowGrid.store.data.items[rowIndex]) == "undefined")?historyGrid.store.data.items[rowIndex].data.ID:nowGrid.store.data.items[rowIndex].data.ID;
		alert(jhid);
		var whereSql = "obj.lydgl.id ='" + jhid +"' and obj.zt=0";
		mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
		mxGrid.store.load();
	});
	**/
	
}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
	//sm:new Ext.grid.RowSelectionModel({
	//	singleSelect:true
	//}),
	
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
        }, ["ID",
	        "LYSQBH",
	        "CZR",
	        "LLR",
	        "GCLB",
	        "GCXM",
	        "LYDATE",
	        "LYBM",
	        "ZT",
	        "BH",
	        "SQBH",
	        "LYDQF",
	        "LCZT"
	    ]),
        remoteSort: true
        //如果为true,每次store在加载时或者一条记录被删除时, 清除所有改动过的记录信息
	    });
		return ds;
	},
    
	/**
	 * PersonAction 列模型
	 */
	getColumnModel:function(type){
		var cm=new Ext.grid.ColumnModel([
	    	//this.sm,
			{id:'ID',header: "领用单编号", width: 120, sortable: true, dataIndex: 'BH'},
			{id:'LYDQF',header: "领用单区分", width: 100, sortable: true, dataIndex: 'LYDQF',
				renderer:function(value,metadata,record,rowIndex,colIndex,store){
				 	return $dictKey("LYDQF",value);
				}
			},
			{id:'SQBH',header: "领用申请单编号", width: 120, sortable: true, dataIndex: 'SQBH'},
			{id:'CZR',header: "操作人", width: 70, sortable: false, dataIndex: 'CZR'},
			{id:'LLR',header: "领料人", width: 70, sortable: false, dataIndex: 'LLR'},
			{id:'GCLB',header: "工程类别", width: 90, sortable: true, dataIndex: 'GCLB'},
			{id:'GCXM',header: "工程项目", width: 90, sortable: true, dataIndex: 'GCXM'},
			{id:'LYBM',header: "领用部门", width: 90, sortable: true, dataIndex: 'LYBM'},
			{id:'LYDATE',header: "领用时间", width: 100, sortable: true, dataIndex: 'LYDATE',
			    renderer:function(value,metadata,record,rowIndex,colIndex,store){
			    	if(value=='' || value==null){
	    		 		return "";
	    		 	}else{
	    		 		return value;
	    		 	}
				//	var dt = formatDate(new Date(value['time']),"yyyy-MM-dd"); 
				//	return dt;     
				}
			},
			{id:"LCZT",header: "流程状态", width: 100, sortable: true, dataIndex:"LCZT"}
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
	deleteSelect:function(select,mxselect){
		var selections = this.getSelections();//获取被选中的行
		var rightGrid=this;
		var ids="";
		var mxIds="";
		if(mxselect && mxselect.length > 0){
			for(var i = 0; i < mxselect.length; i++){
				mxIds+=mxselect[i].id+",";
			}
		}
		Ext.each(selections,function(selectedobj){
			ids+=selectedobj.id+",";//取得他们的id并组装
		});
		
		if(window.confirm("确认作废领料单吗?")){
			/*删除领料单
			Ext.Ajax.request({
				url:link5,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			//alert("领料单删除成功!");*/
			 			//删除领料单明细
						Ext.Ajax.request({
							url:link15,
							success:function(ajax){
						 		var responseText=ajax.responseText;	
						 		var responseObject=Ext.util.JSON.decode(responseText);
						 		if(responseObject.success){
						 			//alert("领料单明细删除成功!");
						 			alert("领料单删除成功!");
						 			rightGrid.getStore().reload();
						 		}else{
						 			alert(responseObject.msg);
						 		}				
							},
						 	failure:function(){
						 		alert("提交失败!");
						 	},
						 	method:'POST',
						 	params: {ids:mxIds,id:ids}//Ext.util.JSON.encode(selections.keys)			
						});
			 			rightGrid.getStore().reload();
			 		/*}else{
			 			alert(responseObject.msg);
			 		}				
				},
			 	failure:function(){
			 		alert("提交失败!");
			 	},
			 	method:'POST',
			 	params: {ids:ids}//Ext.util.JSON.encode(selections.keys)			
			});*/
		}else{
			return ;
		}
	}
});

/**
 * Tab页(包括新系统领用单、老系统领用单)

var nowGrid = new RightGrid(1);
//nowGrid.deferRowRender = false;
nowGrid.on("cellclick",function(grid, rowIndex, columnIndex, e){
	var aaa = nowGrid.store.data.items[rowIndex].id;
	var whereSql = "obj.lydgl.id ='" + aaa +"' and obj.zt=0";
	mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
	mxGrid.store.load();
});
nowGrid.store.on("load",function(){
	nowGrid.getSelectionModel().selectFirstRow();
	var lydid = typeof(nowGrid.store.data.items[0]) == 'undefined'?"":nowGrid.store.data.items[0].id;
	var whereSql = "obj.lydgl.id ='" + lydid +"' and obj.zt=0";
	mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
	mxGrid.store.load();
});
var historyGrid = new RightGrid(2);
historyGrid.width = 970;
historyGrid.height = 200;
//historyGrid.deferRowRender = false;
historyGrid.on("cellclick",function(grid, rowIndex, columnIndex, e){
	var bbb = historyGrid.store.data.items[rowIndex].id;
	var whereSql = "obj.lydgl.id ='" + bbb +"' and obj.zt=0";
	mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
	mxGrid.store.load();
});
historyGrid.store.on("load",function(){
	historyGrid.getSelectionModel().selectFirstRow();
    var lydid = typeof(historyGrid.store.data.items[0]) == 'undefined'?"":historyGrid.store.data.items[0].id;
	var whereSql = "obj.lydgl.id ='" + lydid +"' and obj.zt=0";
	mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
	mxGrid.store.load();
});
var tabPanel = new Ext.TabPanel({
	deferredRender:false,
	activeTab:0,
	autoWidth:true,
	layoutOnTabChange:true,
	items: [{
        title: '新系统领用单',
        tabTip: '新系统领用单',
        items:[nowGrid],
        listeners:{
             activate:function(){
             	nowGrid.store.load();
             }
        }
    },{
        title: '老系统领用单',
        tabTip: '老系统领用单件',
        items:[historyGrid],
        listeners:{
             activate:function(){
             	historyGrid.store.load();
             }
        }
    }]
});

 */
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
		var btnZf = mainToolbar.items.get('btnZf');
		var btnDel=mainToolbar.items.get('btnDel');
		if(oCheckboxSModel.getSelections().length>=1){
			if(btnEnalbe){ 
				if(btnZf.getText()=='撤销作废'){
    		 		btnEnalbe.setDisabled(true);
    		 		btnDel.setDisabled(false);
    			}else{
    				btnEnalbe.setDisabled(false);
    				btnDel.setDisabled(true);
    			}
			}
			//if(btnModify) btnModify.setDisabled(false);
			if(btnZf){ btnZf.setDisabled(false)};
		}else{
			if(btnEnalbe) btnEnalbe.setDisabled(true);
			//if(btnModify) btnModify.setDisabled(true);
			if(btnZf) btnZf.setDisabled(true);
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
	//sm:new Ext.grid.RowSelectionModel({
	//	singleSelect:true
	//}),
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
			"id","xh","wzbm","wzbm.id","wzbm.wzmc","wzbm.dqkc","wzbm.xhgg",
			"wzbm.yfpsl","jldw","jhdj","pzlysl","sjlysl","zje","lszkc","xqjhDetail","zfzt"
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
				{id:'xh',header: "序号", width: 40,align:'center', sortable: false, 
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return rowIndex+1;
					}
				},
				//{id:'lydgl',header: "领用单编号", width: 150, sortable: true, dataIndex: 'lydgl.bh'},
				{id:'wzmc',header: "物资名称", width: 150, sortable: true, dataIndex: 'wzbm.wzmc',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return value+"("+record.get("wzbm.xhgg")+")";
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
				{id:'sjlysl',header: "<span style='color:red'>实际领用数量</span>", width: 80, sortable: true, dataIndex: 'sjlysl',align:'right',editor:new Ext.form.NumberField({
					xtype:'numberfield',
					decimalPrecision:4,
					minValue:0,
					maxValue:999999.99,
					value:0,
					listeners:{focus:function(a){
					        this.selectText();
					    },blur : function( f ){
					    }
					}
					}),
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = record.get('pzlysl');
							record.set('sjlysl',value);
							record.commit();
							//value = 0;
						}
						if(value>record.get('pzlysl')){
							alert('领用数量不能大于批准数量');
							value = record.get('pzlysl');
							record.set('sjlysl',value);
							record.commit();
						}else if(value<record.get('pzlysl')){
							//metadata.attr = "style=background:FFFBD0;";
							//alert(mxGrid.getView().getCell(rowIndex,colIndex-1).style.backgroundColor='#FFFF00');
						}
						
						//;
						
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
	 * 领料单生效
	 */
	 enableLydgl:function(select){
		var selections = this.getSelections();//获取被选中的行
		var lczt = "";
		var lydid = "";
		//if(tabPanel.getActiveTab().title == "老系统领用单"){
		//	lczt = historyGrid.getSelectionModel().selections.items[0].json.LCZT;    //流程状态(只有是"已完结"才可以生效)
		//	lydid = historyGrid.getSelectionModel().getSelected().get('ID');
		//}else{
			lczt = rightGrid.getSelectionModel().selections.items[0].json.LCZT;    //流程状态(只有是"已完结"才可以生效)
			lydid = rightGrid.getSelectionModel().getSelected().get('ID');
		//}
		var wzids = "";
		var ids = "";
		var mxids = "";
		var is_zyly = false; //是否自由领用
		var responseObject = null;
		if(lczt == "已完结"){
			Ext.each(selections,function(selectedobj){
				ids+=selectedobj.id+"="+selectedobj.data.sjlysl+",";//取得他们的id并组装
				if(selectedobj.json.wzlysqDetail.xqjhDetail==null){
					 is_zyly = true;
					 mxids = mxids+selectedobj.id+",";
				}
				if(wzids.indexOf(selectedobj.id)<0)
					wzids+="'"+selectedobj.json.wzbm.id+"',";//取得勾选的物资ID
			});
			if(mxids!=''){
				mxids = mxids.substring(0,mxids.length-1);
			}
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
							//var yfpsl = responseObject.list[i].yfpsl;
							//var sysl = dqkc - yfpsl;
							if(selections[j].data.sjlysl>dqkc){
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
				var type = "0";
				if(window.confirm("确认生效选中的条目吗？")){
					//if(is_zyly){
					//	if(window.confirm("是否需要补需求计划?")){
					//		type = "1";
					//	}
					//}
					Ext.Ajax.request({
						url:link7,
						success:function(ajax){
					 		var responseText=ajax.responseText;	
					 		var responseObject=Ext.util.JSON.decode(responseText);
					 		if(responseObject.success){
					 			alert("操作成功");
					 			/**
					 			if(tabPanel.getActiveTab().title == "老系统领用单"){
						 			historyGrid.getStore().on('load',function(){
										var whereSql = "obj.lydgl.id ='" + lydid +"' and obj.zt=0";
										mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
										//mxGrid.changeToListDS(link2);
										mxGrid.store.load();
									});
						 			historyGrid.store.reload();
					 			}else{
					 			**/ 
					 				if(wzids.split(',').length == mxGrid.store.data.length){
					 					rightGrid.store.reload();
					 					//lydid = rightGrid.getSelectionModel().getSelected().get('ID');
					 					//rightGrid.getStore().on('load',function(){
						 				//	alert("子表刷新");
										//	var whereSql = "obj.lydgl.id ='" + lydid +"' and obj.zt=0";
										//	mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
										//	//mxGrid.changeToListDS(link2);
										//	mxGrid.store.load();
										//});
					 				}else{
					 					var whereSql = "obj.lydgl.id ='" + lydid +"' and obj.zt=0";
										mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
										//mxGrid.changeToListDS(link2);
										mxGrid.store.load();
					 				}
					 				
						 			//rightGrid.store.reload();
					 			//}
					 			//rightGrid.getSelectionModel().clearSelections();
					 		}else{
					 			alert(responseObject.msg);
					 		}				
						},
					 	failure:function(){
					 		alert("提交失败");
					 	},
					 	method:'POST',
					 	params: {ids:ids,lydid:lydid,type:type,mxids:mxids}//Ext.util.JSON.encode(selections.keys)			
					});
				 }
			});
		}else{
			alert("流程没有完结!");
			return ;
		}
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
			},
	/**
	 * 作废
	 */
	deleteSelect:function(ids,mxIds,flag){
		var str = "确认作废该领料明细吗?";
		var str1 = "领料单作废成功!";
		if(flag==1){
			str = "确认删除该领料明细吗?";
			str1 = "领料单删除成功!";
		}
		if(window.confirm(str)){
			/*删除领料单
			Ext.Ajax.request({
				url:link5,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			//alert("领料单删除成功!");*/
			 			//删除领料单明细
						Ext.Ajax.request({
							url:link15,
							success:function(ajax){
						 		var responseText=ajax.responseText;	
						 		var responseObject=Ext.util.JSON.decode(responseText);
						 		alert(responseObject.success);
						 		if(responseObject.success){
						 			//alert("领料单明细删除成功!");
						 			alert(str1);
						 			rightGrid.getStore().reload();
						 		}else{
						 			alert(responseObject.msg);
						 		}				
							},
						 	failure:function(response, options){
						 		alert("提交失败!"); 
						 	},
						 	method:'POST',
						 	params: {ids:mxIds,id:ids,flag:flag}//Ext.util.JSON.encode(selections.keys)			
						});
			 			rightGrid.getStore().reload();
			 		/*}else{
			 			alert(responseObject.msg);
			 		}				
				},
			 	failure:function(){
			 		alert("提交失败!");
			 	},
			 	method:'POST',
			 	params: {ids:ids}//Ext.util.JSON.encode(selections.keys)			
			});*/
		}else{
			return ;
		}
	},
	/**
	 *	撤销作废
	 */
	rbSelect:function(ids,mxIds){
		if(window.confirm("确认撤销该领料明细的作废吗?")){
			/*删除领料单
			Ext.Ajax.request({
				url:link5,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			//alert("领料单删除成功!");*/
			 			//删除领料单明细
						Ext.Ajax.request({
							url:link16,
							success:function(ajax){
						 		var responseText=ajax.responseText;	
						 		var responseObject=Ext.util.JSON.decode(responseText);
						 		if(responseObject.success){
						 			//alert("领料单明细删除成功!");
						 			alert("撤销作废成功!");
						 			rightGrid.getStore().reload();
						 		}else{
						 			alert(responseObject.msg);
						 		}				
							},
						 	failure:function(){
						 		alert("提交失败!");
						 	},
						 	method:'POST',
						 	params: {ids:mxIds,id:ids}//Ext.util.JSON.encode(selections.keys)			
						});
			 			rightGrid.getStore().reload();
			 		/*}else{
			 			alert(responseObject.msg);
			 		}				
				},
			 	failure:function(){
			 		alert("提交失败!");
			 	},
			 	method:'POST',
			 	params: {ids:ids}//Ext.util.JSON.encode(selections.keys)			
			});*/
		}else{
			return ;
		}
	}
});