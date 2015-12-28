var dicts = $dictListAjax("WZXQJHSQ");
function getStatusCnName(qxzy) {
	for(var i=0;i<dicts.length;i++){
		var dict = dicts[i];
		if (dict.value == qxzy) {
			return dict.key;
		}
	}
}

/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link1);
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: 50,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据"
//		,
//		items:['-',{text:'导出Excel',handler:function(){
//		exportExcel(grid,true);
//		}},'-','<font color="red"></font>']
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
		tbar:this.pageToolbar,
		deferRowRender:false
	});	
	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		
		var btnFpForm=mainToolbar.items.get('btnFp');
		
		//if(oCheckboxSModel.getSelections().length==1){
		//	if(btnModifyCForm) btnModifyCForm.setDisabled(true);
		//}else{
			var select=rightGrid.getSelectionModel().getSelected();
			if(select){
				var xqjhsqId = select.json.ID;
				var url = link2+"?xqjhsqId="+xqjhsqId;
				mxGrid.changeToListDS(url);			
				mxGrid.store.load();
			}
		//}
	});
	this.store.on('load',function(){
         rightGrid.getSelectionModel().selectFirstRow();
    });
    this.on('render',function() {
         rightGrid.store.load();
    });
}
Ext.extend(RightGrid, Ext.grid.EditorGridPanel, {
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
	        }, ['ID','XQJHSQBH', 'GCLB', 'GCXM', 'XQJHQF', 'SQBMMC','FLOW_STATUS','CZYXM','SQSJ','STATUS', 'IS_BACK', 'FLOW_NAME', 'START_', 'END_',
							'PROCESSINSTANCE_', 'FLOW_CONFIG_ID']
			),
	        remoteSort: true
	    });
		return ds;
	},
    
	/**
	 * PersonAction 列模型
	 */
	getColumnModel:function(){
			//是否允许回退到主管数据源
			var storeBack =  new Ext.data.SimpleStore({
				fields: ["retrunValue", "displayText"],
				data: [['允许','1'],['不允许','0']]
			});
			var cm = new Ext.grid.ColumnModel([
						{
							id : 'FLOW_STATUS',
							header : "需求计划申请单状态",
							width : 120,
							sortable : true,
							dataIndex : 'FLOW_STATUS'
//							renderer:function(value,metadata,record,rowIndex,colIndex,store){
//								if(!value)
//									return "填写申请";
//								return getStatusCnName(value);
//							}
						},
						{id:'XQJHQF',header: "需求计划区分", width: 120, sortable: true, dataIndex: 'XQJHQF',
							renderer:function(value,metadata,record,rowIndex,colIndex,store){
								if(value == '1'){
									return "需求计划申请";
								}else if(value == '2'){
									return "补料计划申请";
								}else if(value=='3'){
									return "固定资产申请";
								}
							}
						},
						{id:'XQJHSQBH',header: "需求计划申请编号", width: 120, sortable: true, dataIndex: 'XQJHSQBH'},
						{id:'GCLB',header: "工程类别", width: 80, sortable: true, dataIndex: 'GCLB'},
						{id:'GCXM',header: "工程项目", width: 100, sortable: true, dataIndex: 'GCXM'},
						{id:'SQBMMC',header: "申请部门", width: 100, sortable: true, dataIndex: 'SQBMMC'},
						{id:'CZYXM',header: "操作员", width: 100, sortable: true, dataIndex: 'CZYXM'},
						{id:'SQSJ',header: "申请时间", width: 100, sortable: true, dataIndex: 'SQSJ',
						    renderer:function(value,metadata,record,rowIndex,colIndex,store){
						    	if(value == "" || value == null){
						    		return "";
						    	}else{
									var dt = formatDate(new Date(value),"yyyy-MM-dd"); 
				    				return dt;         
						    	}
							}
						},
						{id:'ISBACK',header:"是否允许回退主管",width:120,sortable: true, dataIndex: 'IS_BACK',
							editor:new Ext.form.ComboBox({
							id:'isBackCom',
							store:storeBack,
							valueField:'displayText',  
							displayField:'retrunValue',  
							mode:'local',  
							blankText : '', 
							emptyText:'[无]', 
							//hiddenName:'ISBACK', 
							editable:false,
							forceSelection:true,  
							triggerAction:'all',  
							allowBlank: true,
							anchor:'50%'}
							),renderer:function(value,metadata,record,rowIndex,colIndex,store){
								if(value == "" || value == null){
									return "<span style='color:#FFB0B0'>请选择是否允许回退!</span>";
								}else{
									//return Ext.getCmp('isBackCom').getRawValue();
									if(value == "1"){
										return "允许";
									}else{
										return "不允许";
									}
								}
		                    },listeners:{
		                    	"load":function(){
		                    		this.setValue("1");
		                    	}
		                    }       
					    }
//						,{
//							id : 'START_',
//							header : "开始时间",
//							width : 140,
//							sortable : true,
//							dataIndex : 'START_',
//							renderer : function(value, metadata, record, rowIndex, colIndex, store) {
//								var dt = formatDate(new Date(value), "yyyy-MM-dd hh:mm:ss");
//								return dt;
//							}
//						}, {
//							id : 'END_',
//							header : "结束时间",
//							width : 140,
//							sortable : true,
//							dataIndex : 'END_',
//							renderer : function(value, metadata, record, rowIndex, colIndex, store) {
//								if (value == null) {
//									return "";
//								}
//								var dt = formatDate(new Date(value), "yyyy-MM-dd hh:mm:ss");
//								return dt;
//							}
//						}
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

