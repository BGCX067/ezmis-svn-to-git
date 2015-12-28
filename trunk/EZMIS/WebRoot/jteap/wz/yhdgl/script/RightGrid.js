
/**
 * 字段列表
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
		items:['-',{text:'导出Excel',handler:function(){
		exportExcel(grid,true);
		}},'-','<font color="yellow">黄色:表示自由入库验货单</font>-<font color="white">白色:表示正常入库验货单</font>']
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
		
		var btnEnable=mainToolbar.items.get('btnEnable');
		var btnDelCForm=mainToolbar.items.get('btnDel');
		var btnModifyCForm=mainToolbar.items.get('btnModify');
		
		if(oCheckboxSModel.getSelections().length==1){
			if(btnModifyCForm) btnModifyCForm.setDisabled(false);
		}else{
			if(btnModifyCForm) btnModifyCForm.setDisabled(true);
		}
		
		if(oCheckboxSModel.getSelections().length<=0){
			//if(btnDelCForm) btnDelCForm.setDisabled(true);
			//if(btnEnable) btnEnable.disable();
		}else{
			//if(btnDelCForm) btnDelCForm.setDisabled(false);
			//if(btnEnable) btnEnable.enable();
		}
	});
	
	//双击记录展现相应的采购计划明细表
	this.on("cellclick",function(grid, rowIndex, columnIndex, e){
		var yhdid = rightGrid.store.data.items[rowIndex].id;
		var whereSql = "obj.yhdgl.id ='" + yhdid +"'";
		mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
		mxGrid.store.reload();
	});
	
	//onload事件
	this.store.on("load",function(store,records,options){
		for(var i = 0; i < store.getCount(); i++){
             var record = store.getAt(i);
             //flag【1】表示正常入库验货单；【2】表示自由入库验货单
             if(record.get("flag") == "1"){
                  grid.getView().getRow(i).style.background="white";               
             }else{
             	  grid.getView().getRow(i).style.background="yellow";       
             }
        }
	});
	
}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
	sm:new Ext.grid.RowSelectionModel({singleSelect:true}),
	
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
				'yhdmxs',
				'yhdmxs.cgjhmx',
				'ysrq',
				'ghdw',
				'cgy',
				'bz',
				'flag',
				'personCgy',
				'personBgy',
				'personCgy.userName',
				'personCgy.id',
				'personCgy.userLoginName',
				'personBgy.userName',
				'personBgy.id',
				'personBgy.userLoginName',
				'zt',
				'id',
				'bgy',
				'bh',
				'htbh',
				"cgjhgl", "bh",
				,'dhrq'
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
		    	{id:'bh',header: "验货单编号", width: 100, sortable: true, dataIndex: 'bh'},
		    	{id:'cgjhbh',header: "采购单编号", width: 100, sortable: false,dataIndex: 'htbh',
		    		renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    			if(typeof(record.data.yhdmxs[0].cgjhmx.cgjhgl) == 'undefined'){
		    				return "";
		    			}else{
			    			return record.data.yhdmxs[0].cgjhmx.cgjhgl.bh;
		    			}
		    		}
		    	},
		    	{id:'htbh',header: "合同编号",hidden:true, width: 100, sortable: true, dataIndex: 'htbh'},
				{id:'ysrq',header: "验收日期", hidden:true,width: 100, sortable: true, dataIndex: 'ysrq',
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(value == "" || value == null){
				    		return "";
				    	}else{
				    		var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
				    		return dt;
				    	}     
					}
				},
				{id:'ghdw',header: "供货单位", hidden:false, width: 250, sortable: true, dataIndex: 'ghdw'},
				{id:'bgy',header: "保管员", width: 100, sortable: false, dataIndex: 'personBgy.userName'},
				{id:'ddd',header: "采购员", width: 100, sortable: false, dataIndex: 'personCgy.userName'},
				{id:'zt',header: "状态", width: 100,hidden:true, sortable: true, dataIndex: 'zt'},
				{id:'dhrq',header: "到货日期", width: 100, sortable: true, dataIndex: 'dhrq',
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(value == "" || value == null){
				    		return "";
				    	}else{
				    		var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
				    		return dt;
				    	}
					}
				},
				{id:'bz',header: "班组", width: 100, sortable: true, 
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    		 	if(record.data.bz != ""){
		    		 		return record.data.bz;
		    		 	}else{
		    		 		return record.data.yhdmxs[0].cgjhmx.xqjhDetail.xqjh.sqbm;
		    		 	}      
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
		if(window.confirm("确认删除选中的验货单吗？")){
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
	}
	
});


/**
 * 验货单明细
 */
mxRightGrid=function(){
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
	mxRightGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(),
		//sm: this.sm,
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
		
		var btnEnable=mainToolbar.items.get('btnEnable');
		var btnDelCForm=mainToolbar.items.get('btnDel');
		var btnModifyCForm=mainToolbar.items.get('btnModify');
		/*
		if(oCheckboxSModel.getSelections().length==1){
			if(btnModifyCForm) btnModifyCForm.setDisabled(false);
		}else{
			if(btnModifyCForm) btnModifyCForm.setDisabled(true);
		}
		*/
		if(oCheckboxSModel.getSelections().length<=0){
			if(btnDelCForm) btnDelCForm.setDisabled(true);
			if(btnEnable) btnEnable.disable();
		}else{
			if(btnDelCForm) btnDelCForm.setDisabled(false);
			if(btnEnable) btnEnable.enable();
		}
	});
	
	this.on("afteredit",function(e){
		var r = e.record;
		var g = e.grid;
		if(e.value<r.get('yssl')){
			g.getView().getCell(e.row,e.column).style.backgroundColor='#FFFF00';
			if(g.getView().getHeaderCell(e.column-1)!=null && g.getView().getHeaderCell(e.column-1).innerText=='验货数量'){
				g.getView().getCell(e.row,e.column-1).style.backgroundColor='#FFFF00';
			}
		}
		
	});
}
Ext.extend(mxRightGrid, Ext.grid.EditorGridPanel, {
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
			"cgjhmx",
			"cgjhmx.xqjhDetail",
			"cgjhmx.xqjhDetail.xqjh",
			"cgjhmx.xqjhDetail.xqjh.gclb",
			"cgjhmx.xqjhDetail.xqjh.gcxm",
			"cgjhmx.xqjhDetail.xqjh.sqbm",
			'sxsl','ghdw','xh','sl','zf','remark','wzdagl','wzdagl.kw',"wzdagl.xhgg",'wzdagl.kw.cwmc','wzdagl.kw.ck','wzdagl.kw.ck.id','wzdagl.kw.ck.ckmc','wzdagl.wzmc','yhdgl','yhdgl.bh','yhdgl.bz','yhdgl.gclb','yhdgl.gcmx','tssl','fpbh','dhsl','cgjldw','sqdj','yssl','id','jhdj','hsxs'
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
		    	//{id:'fpbh',header: "发票号码", width: 100, sortable: true, dataIndex: 'fpbh'},
				{id:'wzmc',header: "物质名称", width: 150, sortable: true, dataIndex: 'wzdagl.wzmc',
					renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
		        		return value + "("+record.get('wzdagl.xhgg') +")";
		        	}
				},
				{id:'dhsl',header: "到货数量", width: 100, sortable: true, dataIndex: 'dhsl',align:'right',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'yssl',header: "验货数量", width: 80, sortable: true, dataIndex: 'yssl',align:'right',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				{id:'sxsl',header: "<span style='color:red'>生效数量</span>", width: 80, sortable: true, dataIndex: 'sxsl',align:'right',editor:new Ext.form.NumberField({
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
							value = record.get('yssl');
							record.set('sxsl',value);
							record.commit();
							//value = 0;
						}
						if(value>record.get('yssl')){
							alert('生效数量不能大于验收数量');
							value = record.get('yssl');
							record.set('sxsl',value);
							record.commit();
						}else if(value<record.get('yssl')){
							//metadata.attr = "style=background:FFFBD0;";
							//alert(mxGrid.getView().getCell(rowIndex,colIndex-1).style.backgroundColor='#FFFF00');
						}
						
						//;
						
						return "<span style='color:red;'>"+parseFloat(value).toFixed(2)+"</span>";
					}
				},
				{id:'cgjldw',header: "计量单位", width: 80, sortable: true, dataIndex: 'cgjldw'},
				/*
				{id:'hsxs',header: "换算系数", width: 100, sortable: true, dataIndex: 'hsxs',align:'right',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				*/
				//{id:'zf',header: "杂费", width: 100, sortable: true, dataIndex: 'zf'},
				{id:'jhdj',header: "计划单价", width: 80, sortable: true, dataIndex: 'jhdj',align:'right',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				//{id:'jhje',header: "计划金额", width: 100, sortable: true, dataIndex: ''},
				//{id:'sqdj',header: "税前价", width: 100, sortable: true, dataIndex: 'sqdj'},
				//{id:'rkje',header: "入库金额", width: 100, sortable: true, dataIndex: ''},
				//{id:'sl',header: "税率", width: 100, sortable: true, dataIndex: 'sl'},
				//{id:'se',header: "税额", width: 100, sortable: true, dataIndex: ''},
				//{id:'sjhj',header: "税价合计", width: 100, sortable: true, dataIndex: ''},
				{id:'kw',header: "存储位置", width: 100, sortable: true, dataIndex: 'wzdagl.kw.cwmc'},
				{id:'ck',header: "仓库", width: 100, sortable: true, dataIndex: 'wzdagl.kw.ck.ckmc'}
				,{id:'aaa',header: "工程类别", width: 100, sortable: true, 
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    		 	if(record.data.yhdgl.gclb != ""){
		    		 		return record.data.yhdgl.gclb;
		    		 	}else{
		    		 		return record.data.cgjhmx.xqjhDetail.xqjh.gclb;
		    		 	}       
					 }
				},
				{id:'bbb',header: "工程项目", width: 100, sortable: true, 
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    		 	if(record.data.yhdgl.gcxm != ""){
		    		 		return record.data.yhdgl.gcxm;
		    		 	}else{
		    		 		return record.data.cgjhmx.xqjhDetail.xqjh.gcxm;
		    		 	}       
					 }
				},
				{id:'ccc',header: "班组", width: 100, sortable: true, 
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    		 	if(record.data.yhdgl.bz != ""){
		    		 		return record.data.yhdgl.bz; 
		    		 	}else{
		    		 		return record.data.cgjhmx.xqjhDetail.xqjh.sqbm;
		    		 	}       
					 }
				},
				{id:'remark',header: "备注", width: 100, sortable: true, dataIndex: 'remark'}
			]);
		return cm;
	},
	/**
	 * 切换数据源->LogAction!showList
	 */
	changeToListDS:function(url){
		var ds = this.getDefaultDS(url);	
		var cm=this.getColumnModel();
		//this.pageToolbar.bind(ds);
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
		if(window.confirm("确认删除选中的明细吗？")){
			Ext.Ajax.request({
				url:link15,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			alert("删除成功");
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
	},
	/**
	 * 验货单生效
	 */
	enableYhd:function(){
		var selections = this.getSelections();//获取被选中的行
		//var rightGrid=this;
		var ids="";
		var is_zyrk = false;
		var yhdid = rightGrid.getSelectionModel().getSelected().get('id');
		
		Ext.each(selections,function(selectedobj){
			if(selectedobj.data.cgjhmx.xqjhDetail.xqjh.gcxm	==""){
				is_zyrk = true;
			} 
			ids+=selectedobj.id+"="+selectedobj.data.sxsl+",";//取得他们的id并组装
		});
		/*
		var sels = mxGrid.getSelectionModel().getSelections();
		for (var i = 0; i < sels.length; i++) {
			alert(sels[i].get('sxsl'));
		}
		*/
		var type ='0';
		if(window.confirm("确认生效选中的条目吗？")){
			if(is_zyrk){
				if(window.confirm("是否需要补需求计划?")){
					type = '1';
				}
			}
			Ext.Ajax.request({
				url:link7,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			alert("操作成功！");
			 			rightGrid.store.reload();
			 			rightGrid.getStore().on('load',function(){
							var whereSql = "obj.yhdgl.id ='"+yhdid+"'";
							mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
							//mxGrid.changeToListDS(link2);
							mxGrid.store.load();
			 			});
			 		}else{
			 			alert(responseObject.msg);
			 		}				
				},
			 	failure:function(){
			 		alert("提交失败！");
			 	},
			 	method:'POST',
			 	//type（'1'表示需要补需求计划；'0'表示不需要不需求计划）
			 	params: {ids:ids,yhdid:yhdid,type:type}//Ext.util.JSON.encode(selections.keys)			
			});
		}
	}

});


