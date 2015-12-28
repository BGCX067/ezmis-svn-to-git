
/**
 * 字段列表
 */
LlGrid=function(){ 
    var defaultDs=this.getDefaultDS(link24);
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
	LlGrid.superclass.constructor.call(this,{
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
	
	
	
}
Ext.extend(LlGrid, Ext.grid.GridPanel, {
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
	        }, ["id", "lysj", "lydqf", "gclb", "gcxm",
				"lybm", "lysqbh", "llr","czr","sqsj","bh","wzbm","sjdj",
				"xhgg", "wzmc", "xh","jldw", "jhdj", "dqkc","zt", "ckmc","sjlysl","pzlysl"]),
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
		    	{id:'bh',header: "领用单编号", width: 80, sortable: true, dataIndex: 'bh'},
		    	{id:'zt',header: "领用单状态", width: 100, sortable: true, dataIndex: 'zt',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
					 	if(value=='0'){
					 		metadata.attr ="style=background:#FF3333";
					 		return '未生效';
					 	}else if(value =='1'){
					 		metadata.attr ="style=background:#00FF33";
					 		return '已出库';
					 	}
					}
				},
				/**
		    	{id:'lydgl.lydqf',header: "领用单区分", width: 80, sortable: true, dataIndex: 'lydqf',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value == '1'){
					 		return $dictKey("LYDQF",value);
					 	}
					}
				},
				**/
				{id:'wzmc',header: "物资名称", width: 250, sortable: true, dataIndex: 'wzmc',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value!=null){
							if(record.get('xhgg')!=null){
								return value+"("+record.get('xhgg')+")";
							}else{
								return value+"()";							
							}
						}
					}
				},
				{id:'ckmc',header: "仓库名称", width: 70, sortable: true, dataIndex: 'ckmc',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
					 	if(record.json!=undefined){
					 		return record.json.ckmc;
					 	}else{
					 		return "<font color = 'red'>合计：</font>";
					 	}
					}
				},
				{id:'jldw',header: "计量单位", width: 70, sortable: true, dataIndex: 'jldw'},
				/**{id:'jhdj',header: "计划单价", width: 70, sortable: true, dataIndex: 'wzbm.jhdj',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value!=null){
							return parseFloat(value).toFixed(2);
						}
					}
				},**/{id:'sjdj',header: "实际单价", width: 70, sortable: true, dataIndex: 'sjdj',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value!=null){
							return parseFloat(value).toFixed(2);
						}
					}
				},
				{id:'sjlysl',header: "<span style='color:red'>实际领用数量</span>", width: 80, sortable: true, dataIndex: 'sjlysl',align:'right',editor:new Ext.form.NumberField({
					xtype:'numberfield',
					decimalPrecision:2,
					minValue:0,
					maxValue:999999.99,
					listeners:{focus:function(a){
					        this.selectText();
					    },blur : function( f ){
					    }
					}
					}),
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value == '' || value == null){
							record.set('sjlysl',value);
							value = record.get('pzlysl');
							//record.commit();
						}
						if(value>record.get('pzlysl')){
							alert('领用数量不能大于批准数量');
							record.set('sjlysl',value);
							value = record.get('pzlysl');
							record.commit();
						}else if(value<record.get('pzlysl')){
						
						}
						if(value !=null){
							return "<span style='color:red;'>"+parseFloat(value).toFixed(2)+"</span>";
						}
					}
				},
				/**{id:'jhje',header: "计划金额", width: 70, sortable: true, dataIndex: 'jhje',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(value!=undefined){
				    		return "<font color='red'>"+parseFloat(value.split("_")[1]).toFixed(2)+"</font>";
				    	}else{
							var jhdj = record.get('jhdj');
				    		var lysl = (record.get('sjlysl')==0)?record.get('pzlysl'):record.get('sjlysl');    
				    		return parseFloat(jhdj*lysl).toFixed(2);				    	
				    	}
					}
				},**/{id:'sjje',header: "实际金额", width: 70, sortable: true, 
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(value==undefined){
				    		return "<font color='red'>"+parseFloat(record.data.sjje.split("_")[1]).toFixed(2)+"</font>";
				    	}else{
							var sjdj = record.get('sjdj');
				    		var lysl = (record.get('sjlysl')==0)?record.get('pzlysl'):record.get('sjlysl');    
				    		return parseFloat(sjdj*lysl).toFixed(2);				    	
				    	}
					}
				},
				{id:'lysj',header: "领用时间", width: 100, sortable: false, dataIndex: 'lysj',
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	return value;         
					}
				},{id:'sqsj',header: "领用申请时间", width: 100, sortable: false,dataInedx:'sqsj',
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(typeof(record.data.sqsj) == 'undefined'){
		    				return "";
		    			}else{
			    			//return formatDate(new Date(record.data.sqsj['time']),"yyyy-MM-dd");
			    			return record.data.sqsj; 
		    			}     
					}
				},
				{id:'czr',header: "操作人", width: 100, sortable: false, dataIndex: 'czr'},
				{id:'llr',header: "领料人", width: 100, sortable: false, dataIndex: 'llr'},
				{id:'gclb',header: "工程类别", width: 100, sortable: true, dataIndex: 'gclb'},
				{id:'gcxm',header: "工程项目", width: 100, sortable: true, dataIndex: 'gcxm'},
				{id:'lybm',header: "领用部门", width: 100, sortable: true, dataIndex: 'lybm'}
				
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

