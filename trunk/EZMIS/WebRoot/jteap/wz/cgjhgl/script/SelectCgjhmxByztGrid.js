
/**
 * 指定采购计划
 */
zdRightGrid=function(){
    var defaultDs=this.getDefaultDS(link10 + "&cgy="+cgy+"&zt=1");
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['&nbsp;|&nbsp;','<font color="red">*双击记录查看采购明细</font>']
//		items:['-',{text:'导出Excel',handler:function(){
//		exportExcel(grid,true);
//		}},'-','<font color="red"></font>']
	});
	RightGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(),
		sm: this.sm,
	    margins:'2px 2px 2px 2px',
	    title:'采购计划列表',
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
		var jhid = zdrightGrid.store.data.items[rowIndex].id;
		var whereSql = "obj.cgjhgl.id ='" + jhid +"' and obj.cgy='"+cgy+"' and obj.dyszt = '2'";
		zdmxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql,zt:1,czr:"true"};
		zdmxGrid.store.load({callback:function(r,options,success){
			var mxstore = zdmxGrid.store;
			var ids ="";
		for (var i = 0; i < r.length; i++) {
			var cgsl = r[i].get('cgsl');
			var dhsl = r[i].get('dhsl');
			var zt = r[i].get('zt');
			var tmp = 0;
			for (var j = 0; j < tmpStore.getCount(); j++) {
				var yssl = tmpStore.getAt(j).get('YSSL');
				if(r[i].get('id')==tmpStore.getAt(j).get('CGDMX') ){
					dhsl += yssl;
					if(cgsl<=dhsl || zt=='0'){
						mxstore.remove(r[i]);
						ids +="["+i+"],";
					}else{
						r[i].set('dqsl',cgsl-dhsl);
					}
				}else{
					if(ids.indexOf("["+i+"]")<0)
						r[i].set('dqsl',cgsl-dhsl);
				}
			}
		}
		}});
		
	});
	
}
Ext.extend(zdRightGrid, Ext.grid.GridPanel, {
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
			'bh','zdsj','cgjhmxs',"xqjhDetail", "xqjh", "xqjhbh",'sxsj','bz','zt','id'
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
				{id:'bh',header: "采购计划编号", width: 100, sortable: true, dataIndex: 'bh'},
				{id:'xqbh',header: "需求计划编号", width: 100, sortable: true, 
		    		renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    			if(typeof(record.data.cgjhmxs[0].xqjhDetail) == 'undefined'){
		    				return "";
		    			}else{
			    			return record.data.cgjhmxs[0].xqjhDetail.xqjh.xqjhbh;
		    			}
		    		}
		    	},
				{id:'zdsj',header: "制定时间", width: 100, sortable: true, dataIndex: 'zdsj',
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(!value) return;
						var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
						return dt;         
					}},
				{id:'sxsj',header: "生效时间", width: 100, sortable: true, dataIndex: 'sxsj',
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
	}

});



/**
 * 采购计划明细
 */
zdMxRightGrid=function(){
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
	
	//双击记录展现相应的采购计划明细表
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
	});
	
}
Ext.extend(zdMxRightGrid, Ext.grid.GridPanel, {
	//sm:new Ext.grid.RowSelectionModel({}),
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
			'dqsl','id','cgjhgl','cgjhgl.bh','cgjhgl.jhy',
			'cgjhgl.zdsj','cgjhgl.sxsj','cgjhgl.bz','wzdagl',
			'wzdagl.dqkc','wzdagl.wzmc','wzdagl.kw.ck','wzdagl.kw.ck.id',
			'wzdagl.kw.ck.ckmc','wzdagl.xhgg','wzdagl.kw','wzdagl.kw.cwmc',
			'wzdagl.kw.ck','wzdagl.kw.ck.ckmc','wzdagl.kw.ck.id','xh','jhdj',
			'cgjldw','cgsl','hsxs','jhdhrq','dhsl','cgfx','cgy','je','zt','person',
			'person.id','person.userName','person.userLoginName',"xqjhDetail",
			"xqjhDetail.xqjh",
			"xqjhDetail.xqjh.gclb",
			"xqjhDetail.xqjh.gcxm",
			"xqjhDetail.xqjh.sqbm",
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
				{id:'xh',header: "序号", width: 40, sortable: true, dataIndex: 'xh',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return rowIndex+1;
					}
				},
				{id:'wzbm',header: "物资名称(规格)", width: 140, sortable: true, dataIndex: 'wzdagl.wzmc',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return value+"("+record.get('wzdagl.xhgg')+")";
					}
				},
				//{id:'wzbm',header: "型号规格", width: 100, sortable: true, dataIndex: 'wzdagl.xhgg'},
				{id:'wzbm',header: "仓库(库位编码)", width: 120, sortable: true, dataIndex: 'wzdagl.kw.cwmc',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return  store.getAt(rowIndex).get("wzdagl.kw.ck.ckmc")+ "("+ value+")";
					}
				},
				{id:'xqjhDetail.xqjh.gclb',header: "工程类别", width: 80, sortable: true, dataIndex: 'xqjhDetail.xqjh.gclb'},
				{id:'xqjhDetail.xqjh.gcxm',header: "工程项目", width: 80, sortable: true, dataIndex: 'xqjhDetail.xqjh.gcxm'},
				{id:'xqjhDetail.xqjh.sqbm',header: "班组", width: 80, sortable: true, dataIndex: 'xqjhDetail.xqjh.sqbm'},
				{id:'cgsl',header: "采购数量", width: 80, sortable: true, dataIndex: 'cgsl'},
				{id:'dqsl',header: "可用数量", width: 80, sortable: true, dataIndex: 'dqsl'},
				{id:'cgjldw',header: "采购计量单位", width: 100, sortable: true, dataIndex: 'cgjldw'},
				//{id:'hsxs',header: "换算系数", width: 100, sortable: true, dataIndex: 'hsxs'},
				{id:'cgy',header: "采购员", width: 100, sortable: true, dataIndex: 'person.userName'},
				{id:'cgfx',header: "采购方向", width: 100, sortable: true, dataIndex: 'cgfx',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
					 	return $dictKey("CGFX",value);
					}
				},
				{id:'jhdj',header: "计划单价", width: 100, sortable: true,align:'right', dataIndex: 'jhdj',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'je',header: "金额", width: 100, sortable: true, dataIndex: 'je',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'jhdhrq',header: "计划到货日期", width: 100, sortable: true, align:'right',dataIndex: 'jhdhrq',
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(!value) return;
						var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
						return dt;         
					}
				}
				
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

