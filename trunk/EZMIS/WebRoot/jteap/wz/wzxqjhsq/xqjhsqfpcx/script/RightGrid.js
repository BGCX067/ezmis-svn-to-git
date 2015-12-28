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
    var defaultDs=this.getDefaultDS(link1+"?limit=50");
    defaultDs.load();
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: 50,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-','<font color="red">双击查询需求计划申请</font>'
		,'-','<font color="#CCCCCC">灰色为已作废需求</font>']
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
	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=rightGrid.getSelectionModel().getSelections()[0];
		if(select){
			var sqjhsqid  = select.json.ID;
			Ext.Ajax.request({
				url:link2,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			var url = link3;
						var windowUrl = link4 + "?pid=" + responseObject.processinstance + "&status=false";
						var args = "url|" + windowUrl + ";title|" + '查看流程';
						var retValue = showModule(url, "yes", 700, 600, args);
			 		}else{
			 			alert(responseObject.msg);
			 		}				
				},
			 	failure:function(){
			 		alert("提交失败");
			 	},
			 	method:'POST',
			 	params: {sqjhsqid:sqjhsqid}// Ext.util.JSON.encode(selections.keys)
			});
		}else{
			alert("至少选择一条记录!");
			return;
		}
	});
    this.on('render',function() {
         rightGrid.store.load({params: { start: 0 ,limit: 50 } });
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
		var grid = this;
	    var ds = new Ext.data.Store({
	        proxy: new Ext.data.ScriptTagProxy({
	            url: url
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount',
	            id: 'id'
	        }, ['ID','XQJHSQBH','IS_CANCEL','GCLB','GCXM','SQBMMC','XQJHQF','FLOW_STATUS','CZYXM','SQSJ', "WZMC", "XHGG", "SQSL", "JLDW", "GJDJ", 'STATUS','IS_BACK','JHY']
			),
	        remoteSort: true
	    });
	    //onload事件
		ds.on("load",function(store,records,options){
			for(var i = 0; i < store.getCount(); i++){
	             var record = store.getAt(i);
	             //如果工程项目为空 则是自由入库
	             if(record.data.IS_CANCEL == '1'){
	                  grid.getView().getRow(i).style.background="white";               
	             }else{
	             	  grid.getView().getRow(i).style.background="#CCCCCC";       
	             }
	        }
		});
		return ds;
	},
    
	/**
	 * PersonAction 列模型
	 */
	getColumnModel:function(){
			var personMap = new Hash();
			AjaxRequest_Sync(link6, null, function(ajax){
				var obj = new String(ajax.responseText).evalJSON();
				var list = obj.list;
				for (var i=0;i<list.length;i++){
					personMap.set(list[i].userLoginName,list[i].userName);
				}
			})
			var cm = new Ext.grid.ColumnModel([
						/**
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
						**/
						this.sm,
						{id:'xh',header: "序号", width: 40, sortable: false, dataIndex: 'xh',
							renderer:function(value,metadata,record,rowIndex, colIndex, store)
					        {
					              return rowIndex + 1;   
					        }
						},
						{id:'XQJHQF',header: "需求计划区分", width: 100, sortable: true, dataIndex: 'XQJHQF',
							renderer:function(value,metadata,record,rowIndex,colIndex,store){
								if(value == '1'){
									return "需求计划申请";
								}else if(value == '2'){
									return "补料计划申请";
								}
							}
						},
						{id:'XQJHSQBH',header: "需求计划申请编号", width: 120, sortable: true, dataIndex: 'XQJHSQBH'},
						{id:'WZMC',header: "物资名称", width: 120, sortable: true, dataIndex: 'WZMC'},
						{id:'is_cancel',header: "是否作废", width: 120,hidden:true, sortable: true, dataIndex: 'is_cancel'},
						{id:'XHGG',header: "型号规格", width: 120, sortable: true, dataIndex: 'XHGG'},
						{id:'SQSL',header: "申请数量", width: 70, sortable: true, dataIndex: 'SQSL'},
						{id:'JLDW',header: "计量单位", width: 70, sortable: true, dataIndex: 'JLDW'},
						{id:'GJDJ',header: "计划单价", width: 80, sortable: true, dataIndex: 'GJDJ',
							renderer:function(value,metadata,record,rowIndex,colIndex,store){
								if(value != '' || value != null){
									return parseFloat(value).toFixed(2);
								}else{
									return value;
								}
							}
						},
						{id:'GCLB',header: "工程类别", width: 100, sortable: true, dataIndex: 'GCLB'},
						{id:'GCXM',header: "工程项目", width: 120, sortable: true, dataIndex: 'GCXM'},
						{id:'SQBMMC',header: "申请部门", width: 100, sortable: true, dataIndex: 'SQBMMC'},
						{id:'JHY',header: "计划员", width: 80, sortable: true, dataIndex: 'JHY',
							renderer:function(value,metadata,record,rowIndex,colIndex,store){
								return personMap.get(value);
							}
						},
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
						}
						/**
						{id:'ISBACK',header:"是否允许回退主管",width:120,sortable: true, dataIndex: 'IS_BACK',
							renderer:function(value, metadata, record, rowIndex, colIndex, store){
								if(value == "1"){
									return "允许";
								}else{
									return "不允许";
								}
							}
						}
						*/
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

