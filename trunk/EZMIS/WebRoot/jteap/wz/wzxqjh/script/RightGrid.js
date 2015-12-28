/**
 * 字段列表
 */
RightGrid=function(){
	
    var defaultDs=this.getDefaultDS(link1+"?limit=50");
    defaultDs.load();
    defaultDs.on('load',function(store, records, options){
    	(function(){
		    grid.getSelectionModel().selectFirstRow();
		}).defer(1);
    });
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: 50,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'导出Excel',handler:function(){
		exportExcel(grid,true);
		}},'-','<font color="#33CC66">绿色为已到货物资</font>']
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
		stripeRows: false,
		deferRowRender:false
	});	
//	
//	this.view.on("refresh",function(view){
//		grid.getSelectionModel().selectFirstRow();
//	});
//	
	this.on('render',function(param){
		grid.getSelectionModel().selectFirstRow();
		grid.getSelectionModel().selectNext();
	});
	// 当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		
		var btnQuerySq=mainToolbar.items.get('btnQuerySq');
		var btnSlfp=mainToolbar.items.get('btnSlfp');
		var btnWzhz=mainToolbar.items.get('btnWzhz');
		var btnSccg=mainToolbar.items.get('btnSccg');
		var btnRb = mainToolbar.items.get('btnRb');
		
		if(oCheckboxSModel.getSelections().length==1){
			if(btnQuerySq) btnQuerySq.setDisabled(false);
			if(btnSlfp) btnSlfp.setDisabled(false);
			if(btnWzhz) btnWzhz.setDisabled(false);
			if(btnSccg) btnSccg.setDisabled(false);
			if(btnRb) btnRb.setDisabled(false);
		}else{
			if(btnQuerySq) btnQuerySq.setDisabled(true);
			if(btnSlfp) btnSlfp.setDisabled(true);
			if(btnWzhz) btnWzhz.setDisabled(true);
			if(btnSccg) btnSccg.setDisabled(true);
			if(btnRb) btnRb.setDisabled(true);
		}
		
		if(oCheckboxSModel.getSelections().length<=0){
			if(btnQuerySq) btnQuerySq.setDisabled(true);
			if(btnSlfp) btnSlfp.setDisabled(true);
			if(btnWzhz) btnWzhz.setDisabled(true);
			if(btnSccg) btnSccg.setDisabled(true);
			if(btnRb) btnRb.setDisabled(true);
		}else{
			if(btnQuerySq) btnQuerySq.setDisabled(false);
			if(btnSlfp) btnSlfp.setDisabled(false);
			if(btnWzhz) btnWzhz.setDisabled(false);
			if(btnSccg) btnSccg.setDisabled(false);
			if(btnRb) btnRb.setDisabled(false);
		}
	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=rightGrid.getSelectionModel().getSelections()[0];
		if(select){
			var sqjhsqid  = select.json.XQJHSQID;
			Ext.Ajax.request({
				url:link5,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			var url = link9;
						var windowUrl = link10 + "?pid=" + responseObject.processinstance + "&status=false";
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
	
}



Ext.extend(RightGrid, Ext.grid.GridPanel, {
	sm:new Ext.grid.CheckboxSelectionModel(),
	//sm:new Ext.grid.RowSelectionModel(),
	
	/**
	 * 取得默认数据源 返回数据格式
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
	        }, [
				"ID", "XQJHBH",  "XQJHSQBH", "SXSJ", "OPERATOR", "PERSONNAME",
							"SQBM", "CZY", "GCLB", "GCXM", "WZBM", "WZMC", "XHGG", "XQJHMXID", "PZSL", "JLDW",
							"JHDJ", "XYSJ", "FREE", "CGSL", "STATUS","CZYNAME","XQJHSQID","DYSZT","XH"
	        ]),
	        remoteSort: true,
	        // 如果为true,每次store在加载时或者一条记录被删除时, 清除所有改动过的记录信息
	        pruneModifiedRecords: true
	    });
	     //onload事件
		ds.on("load",function(store,records,options){
			for(var i = 0; i < store.getCount(); i++){
	             var record = store.getAt(i);
	           //如果待验收状态不为0则是已指定
	             if(record.data.dyszt == "2"||record.data.dyszt == 2){
	               grid.getView().getRow(i).style.background="#33CC66";          
	             }else{
	             	  //alert("变色");
	             	grid.getView().getRow(i).style.background="white";           
	             }
	        }
		});
		return ds;
	},
    
	/**
	 * JctzAction 列模型
	 */
	getColumnModel:function(){
		
		var grid = this;
		
	    var cm=new Ext.grid.ColumnModel([
	    		this.sm,
				new Ext.grid.RowNumberer({header:'序号',width:40}),
				{id:'xqjhmxid',header: "需求计划明细编号", width: 80, sortable: true, hidden:true, dataIndex: 'XQJHMXID'},		
				{id:'xh',header: "序号", width: 80, sortable: true, hidden:true, dataIndex: 'XH'},	
				{id:'XQJHSQBH',header: "需求计划申请编号", width: 120, sortable: true, dataIndex: 'XQJHSQBH'},
				{id:'wzbm',header: "物资编码", width: 160, sortable: true, hidden:true, dataIndex: 'WZBM'},
				{id:'wzmc',header: "物资名称规格", width: 160, sortable: true, dataIndex: 'WZMC',
					renderer:function(value, metadata, record, rowIndex, colIndex, store){
						var wzmc = record.data.WZMC;
						var xhgg = (record.data.XHGG == null)?"":record.data.XHGG;
						return wzmc+"("+xhgg+")";
					}
				},
				{id:'jldw',header: "计量单位", width: 60, sortable: true, dataIndex: 'JLDW'},
				{id:'pzsl',header: "批准数量", width: 80, sortable: true, dataIndex: 'PZSL' , align:'right',editor:
					new Ext.form.NumberField({
					xtype:'numberfield',
					decimalPrecision:4,
					minValue:0,
					maxValue:999999.99,
					value:0,
					listeners:{focus:function(a){
					        this.selectText();
					    }
					}}),
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
					    if(!value || value==''){
					          value = 0;
					    }
					    return parseFloat(value).toFixed(2);
					}},
				{id:'jhdj',header: "计划单价", width: 70, sortable: true, dataIndex: 'JHDJ', align:'right',editor:
					new Ext.form.NumberField({
					xtype:'numberfield',
					decimalPrecision:4,
					minValue:0,
					maxValue:999999.99,
					value:0,
					listeners:{focus:function(a){
					        this.selectText();
					    }
					}}),
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
					    if(!value || value==''){
					          value = 0;
					    }
					    return parseFloat(value).toFixed(2);
					}},
				{id:'xysj',header: "需要时间", width: 100, sortable: true, dataIndex: 'XYSJ',
				renderer:function(value,metadata,record,rowIndex,colIndex,store){
					var xysj = (value == null)?null:formatDate(new Date(value),"yyyy-MM-dd"); 
					return xysj;
				}},
				{id:'sqbm',header: "申请部门", width: 70, sortable: true, dataIndex: 'SQBM'},
				{id:'sqr',header: "申请人", width: 70, sortable: true, dataIndex: 'CZYNAME'},
				{id:'operator',header: "操作员编号", width: 70, sortable: true, hidden:true, dataIndex: 'OPERATOR'},
				{id:'loginname',header: "操作员", width: 70, sortable: true,  dataIndex: 'PERSONNAME'},
				{id:'status',header: "状态", width: 50, sortable: true, dataIndex: 'STATUS',
				renderer:function(value,metadata,record,rowIndex,colIndex,store){
					var status = "";
					if(value == "1"){
						status = "未处理";
					}
					return status;
				}
				},
				{id:'gclb',header: "工程类别", width: 100, sortable: true, dataIndex: 'GCLB'}
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
	/**
	 * 删除
	 */
//	deleteSelect:function(select){
//		var selections = this.getSelections();// 获取被选中的行
//		var rightGrid=this;
//		var ids="";
//		Ext.each(selections,function(selectedobj){
//			ids+=selectedobj.id+",";// 取得他们的id并组装
//		});
//		if(window.confirm("确认删除选中的条目吗？")){
//			Ext.Ajax.request({
//				url:link5,
//				success:function(ajax){
//			 		var responseText=ajax.responseText;	
//			 		var responseObject=Ext.util.JSON.decode(responseText);
//			 		if(responseObject.success){
//			 			alert("删除成功");
//			 			rightGrid.getStore().reload();
//			 		}else{
//			 			alert(responseObject.msg);
//			 		}				
//				},
//			 	failure:function(){
//			 		alert("提交失败");
//			 	},
//			 	method:'POST',
//			 	params: {ids:ids}// Ext.util.JSON.encode(selections.keys)
//			});
//		}
//	}
	
});