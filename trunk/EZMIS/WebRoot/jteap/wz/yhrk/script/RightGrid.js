
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
		tbar:this.pageToolbar 
	});	
	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		
		var btnEnable=mainToolbar.items.get('btnEnable');
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
	            id: 'id'
	        }, [
	        'bh','htbh','ghdw','personBgy.userName','personCgy.userName','zt','dhrq'
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
		    	{id:'htbh',header: "合同编号", width: 100, sortable: true, dataIndex: 'htbh'},
				/*{id:'ysrq',header: "验收日期", width: 100, sortable: true, dataIndex: 'ysrq',
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	var dt = '';
				    	if(value)
						 	dt = formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
						return dt;         
					}},*/
				{id:'ghdw',header: "供货单位", width: 200, sortable: true, dataIndex: 'ghdw'},
				{id:'bgy',header: "保管员", width: 100, sortable: false, dataIndex: 'personBgy.userName'},
				{id:'cgy',header: "采购员", width: 100, sortable: false, dataIndex: 'personCgy.userName'},
				{id:'zt',header: "状态", width: 100, hidden:true,sortable: true, dataIndex: 'zt'},
				{id:'dhrq',header: "到货日期", width: 100, sortable: true, dataIndex: 'dhrq',
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	var dt = '';
				    	if(value)
						 	dt = formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
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
		var btnModifyCForm=mainToolbar.items.get('btnModify');
		var btnDelCForm=mainToolbar.items.get('btnDel');
		
		if(oCheckboxSModel.getSelections().length<=0){
			if(btnEnable) btnEnable.disable();
			if(btnDelCForm) btnDelCForm.disable();
		}else{
			if(btnEnable) btnEnable.enable();
			if(btnDelCForm) btnDelCForm.enable();
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
	this.getView().on("refresh",function(){
		var store = grid.getStore();
	 	 var moneyCount = 0;
	 	 var seCount = 0;
	 	 var sqdjCount = 0;
	 	 var rkjeCount = 0;
		//获取总行数
		var total = store.getCount();
		for(var i=0;i<total;i++){
	 		var rec = store.getAt(i);
			var yhsl = rec.get('yssl');
			var xs =rec.get('hsxs');
			var sqj = rec.get('sqdj');
			var sl =rec.get('sl');
			var rkje = yhsl*xs*sqj;
			//税额运算是不经过四舍五入的
			var se = sl*rkje;
			//四舍五入
			var rkjeNum = new Number(rkje.toFixed(2));
			//四舍五入
			var seNum = new Number(se.toFixed(2));
			//将四舍五入的税额及入库金额累计
			var sjhj = rkjeNum+seNum
			//var value = sjhj;
			//var value = rec.get('sjhj');
			seCount = seCount+seNum;
			rkjeCount = rkjeCount+rkjeNum;
			moneyCount = moneyCount+sjhj;
		}
		if(total>=1){
			store.add(new store.recordType({
	  			sxsl:"false",
	  			dhsl:"false",
	  			yssl:"false",
	  			zf:"false",
	  			jhdj:"false",
	  			jhje:"false",
	  			sqdj:"false",
	  			rkje:rkjeCount,
	  			sl:"false",
	  			se:seCount,
	  			sjhj:moneyCount,
	  			cgbh:"false",
	  			id:"false",
	  			cgjldw:"<font color='red'>合计:</font>"
	  			})
  			);
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
			"cgjhmx.cgjhgl.bh",
			'sxsl','ghdw','xh','sl','zf','wzdagl.xhgg','wzdagl.wzmc',
			'wzdagl.kw.cwmc','wzdagl.kw.ck.ckmc','tssl',
			'fpbh','dhsl','cgjldw','sqdj','yssl','id','jhdj','hsxs'
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
		    	{id:'fpbh',header: "发票号码", width: 100, sortable: true, dataIndex: 'fpbh'},
		    	{id:'cgjhbh',header: "采购编号", width: 100, sortable: true, dataIndex:"cgbh", 
		    		renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    			if(value!="false"){
		    				return record.json.cgjhmx.cgjhgl.bh;
		    			}else{
		    				return "";
		    			}
		    		}
		    	},
				{id:'wzmc',header: "物质名称", width: 150, sortable: true, dataIndex: 'wzdagl.wzmc',
					renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
						if(value==undefined){
							return "";						
						}else{
							return value + "("+record.get('wzdagl.xhgg') +")";
						}
		        	}
				},
				{id:'dhsl',header: "到货数量", width: 100, sortable: true, dataIndex: 'dhsl',align:'right',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value!="false"){
							if(!value || value==''){
								value = 0;
							}
							return parseFloat(value).toFixed(2);
						}else{
							return "";
						}
					}
				},
				{id:'yssl',header: "验货数量", width: 100, sortable: true, dataIndex: 'yssl',align:'right',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value!="false"){
							if(!value || value==''){
								value = 0;
							}
							return parseFloat(value).toFixed(2);
						}else{
							return "";
						}
					}
				},
				{id:'sxsl',header: "<span style='color:red'>生效数量</span>", width: 100, sortable: true, dataIndex: 'sxsl',align:'right',editor:new Ext.form.NumberField({
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
						if(value!="false"){
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
						}else{
							return "";
						}
					}
				},
				{id:'cgjldw',header: "采购计量单位", width: 100, sortable: true, dataIndex: 'cgjldw'},
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
				{id:'zf',header: "杂费", width: 100, align:'right',sortable: true, dataIndex: 'zf',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value!="false"){
							if(!value || value==''){
								value = 0;
							}
							return parseFloat(value).toFixed(2);
						}else{
							return "";
						}
					}
				},
				{id:'jhdj',header: "计划单价", width: 100, sortable: true, dataIndex: 'jhdj',align:'right',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value!="false"){
							if(!value || value==''){
								value = 0;
							}
							return parseFloat(value).toFixed(2);
						}else{
							return "";
						}
						
					}
				},
				{id:'jhje',header: "计划金额", width: 100, align:'right',sortable: true, dataIndex: 'jhje',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value!="false"){
							var sl = record.get('yssl');
							var xs =record.get('hsxs');
							var dj = record.get('jhdj');
							return (sl*xs*dj).toFixed(2);
						}else{
							return "";
						}
					}
				},
				{id:'sqdj',header: "税前单价", width: 100, align:'right',sortable: true, dataIndex: 'sqdj',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value!="false"){
							if(!value || value==''){
								value = 0;
							}
							return parseFloat(value).toFixed(2);
						}else{
							return "";
						}
					}
				},
				{id:'rkje',header: "入库金额", width: 100, align:'right',sortable: true, dataIndex: 'rkje',
				renderer:function(value,metadata,record,rowIndex,colIndex,store){
					if(value==undefined){
						var yhsl = record.get('yssl');
						var xs =record.get('hsxs');
						var sqj = record.get('sqdj');
						var sl =record.get('sl');
						var rkje = yhsl*xs*sqj;
						
						//record.set('SE',se.toFixed(2));
						//record.set('SJHJ',sjhj.toFixed(2));
						return rkje.toFixed(2) ;
					}else{
						return "<font color='red' style='font-weight:bold'>"+value.toFixed(2)+"</font>" ;
					}
				}},
				{id:'sl',header: "税率", width: 100, sortable: true, dataIndex: 'sl',align:'right',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value!="false"){
							if(!value || value==''){
								value = 0;
							}
							return parseFloat(value).toFixed(4);
						}else{
							return "";
						}
					}
				},
				{id:'se',header: "税额", width: 100, align:'right',sortable: true, dataIndex: 'se',
				renderer:function(value,metadata,record,rowIndex,colIndex,store){
					if(value==undefined){
						var yhsl = record.get('yssl');
						var xs =record.get('hsxs');
						var sqj = record.get('sqdj');
						var sl =record.get('sl');
						var rkje = yhsl*xs*sqj;
						var se = sl*rkje;
						var sjhj = rkje+se
						
						//record.set('SE',se.toFixed(2));
						//record.set('SJHJ',sjhj.toFixed(2));
						return "<font color='red' style='font-weight:bold'>"+se.toFixed(2)+"</font>" ;
					}else{
						return "<font color='red' style='font-weight:bold'>"+value.toFixed(2)+"</font>" ;
					}
					
				}},
				{id:'sjhj',header: "税价合计", width: 100, align:'right',sortable: true, dataIndex: 'sjhj',
				renderer:function(value,metadata,record,rowIndex,colIndex,store){
					if(value==undefined){
						var yhsl = record.get('yssl');
						var xs =record.get('hsxs');
						var sqj = record.get('sqdj');
						var sl =record.get('sl');
						var rkje = yhsl*xs*sqj;
						var se = sl*rkje;
						//四舍五入
						var rkjeNum = new Number(rkje.toFixed(2));
						//四舍五入
						var seNum = new Number(se.toFixed(2));
						//将四舍五入的税额及入库金额累计
						var sjhj = rkjeNum+seNum
						
						//record.set('SE',se.toFixed(2));
						//record.set('SJHJ',sjhj.toFixed(2));
						return "<font color='red' style='font-weight:bold'>"+sjhj.toFixed(2)+"</font>" ;
					}else{
						return "<font color='red' style='font-weight:bold'>"+value.toFixed(2)+"</font>" ;
					}
					
				}},
				{id:'kw',header: "存储位置", width: 100, sortable: true, dataIndex: 'wzdagl.kw.cwmc'},
				{id:'ck',header: "仓库", width: 100, sortable: true, dataIndex: 'wzdagl.kw.ck.ckmc'}
				//,{id:'aaa',header: "工程类别", width: 100, sortable: true, dataIndex: 'cgjhmx.xqjhDetail.xqjh.gclb'},
				//{id:'bbb',header: "工程项目", width: 100, sortable: true, dataIndex: 'cgjhmx.xqjhDetail.xqjh.gcxm'},
				//{id:'ccc',header: "班组", width: 100, sortable: true, dataIndex: 'cgjhmx.xqjhDetail.xqjh.sqbm'}
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
	 * 验货单生效
	 */
	enableYhd:function(){
		var selections = this.getSelections();//获取被选中的行
		var r = selections;
		debugger
		//var rightGrid=this;
		var ids="";
		var idsIn = "";
		var yhdid = rightGrid.getSelectionModel().getSelected().id;
		Ext.each(selections,function(selectedobj){
			ids+=selectedobj.id+"="+selectedobj.data.sxsl+",";//取得他们的id并组装
			idsIn +="'"+selectedobj.id+"',";
		});
		//var doEnable = false;
		//var ds = mxGrid.getDefaultDS(link2);
		//var whereSql = "obj.yhdgl.id in (" + idsIn.substring(0,idsIn.length-1) +")";
		//ds.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
		//ds.load({   
        //callback :function(r,options,success){ 
            //if(success){   
            	var flag = false;
                for(var i=0;i<r.length;i++){   
                      var record = r[i];   
                      var fpbh = record.data.fpbh;   
                      var sqdj =  record.data.sqdj;   
                	  
                      if(fpbh==''){
                      		alert('发票号码不能为空');    
                      		flag = false;
							return ;
                      }else if(sqdj<=0){
                      		alert('必须填写税前价格');
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
								 			alert("操作成功！");
								 			rightGrid.getStore().on('load',function(){
												var whereSql = "obj.yhdgl.id ='"+yhdid+"'";
												mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
													//mxGrid.changeToListDS(link2);
												mxGrid.store.load();
								 			});
								 			rightGrid.store.reload();
								 		}else{
								 			alert(responseObject.msg);
								 		}				
									},
								 	failure:function(){
								 		alert("提交失败！");
								 	},
								 	method:'POST',
								 	params: {ids:ids,yhdid:yhdid}//Ext.util.JSON.encode(selections.keys)			
								});
							}
	              }
	        	//}   
	    	//}   
	    //});  
	}

});


