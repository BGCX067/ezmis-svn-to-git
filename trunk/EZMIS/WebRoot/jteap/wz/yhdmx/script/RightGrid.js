
/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link4+"?queryParamsSql=obj.zt='0' or obj.zt='2'");//+"?queryParamsSql=obj.yhdgl.zt='2'"
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'导出Excel',handler:function(){
		exportSelectedExcel(grid);
		}}
		,'-','<font color="yellow">黄色:表示自由入库物资</font>-<font color="white">白色:表示正常入库物资</font>'
		]
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
		
		var btnDelCForm=mainToolbar.items.get('btnDelCForm');
		var btnModifyCForm=mainToolbar.items.get('btnModifyCForm');
		
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

	/**
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=rightGrid.getSelectionModel().getSelections()[0];
		if(select.get("type")=="EFORM"){
			//var eformUrl=select.get("eformUrl");
			
			var url="/jteap/cform/fceform/common/djframe.htm?djsn="+select.get("sn")+"&catalogName="+select.get("eformUrl")
		    window.open(CONTEXT_PATH+url);
		}
		
		if(select.get('type')=="EXCEL"){
			var url=CONTEXT_PATH+"/jteap/cform/excelFormRec.jsp?cformId="+select.json.id;
			var features="menubar=no,toolbar=no,width=800,height=600";
			window.open(url,"_blank",features);
		}
		
		//这里还是用原始方法来查询
		if(select){
			var url=link8+"&docid="+select.json.yhdgl.id;
			result=showModule(url,true,800,645);
		}

	});
	**/
}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
	//sm:new Ext.grid.RowSelectionModel({
		//singleSelect:true
	//}),
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
	        }, [
	        "sjhj",
	        "jhje",
	        "cgjhmx",
			"cgjhmx.xqjhDetail",
			"cgjhmx.xqjhDetail.xqjh",
			"cgjhmx.xqjhDetail.xqjh.gclb",
			"cgjhmx.xqjhDetail.xqjh.gcxm",
			"cgjhmx.xqjhDetail.xqjh.sqbm",
			'ghdw','xh','sl','zf','wzdagl','wzdagl.wzmc','wzdagl.xhgg','yhdgl','yhdgl.yhdmxs','yhdgl.personCgy','yhdgl.personBgy',
			'yhdgl.personCgy.userName','yhdgl.personCgy.id','yhdgl.personCgy.userLoginName',
			'yhdgl.personBgy.userName','yhdgl.personBgy.id','yhdgl.personBgy.userLoginName',"zt","remark",
			'yhdgl.id','yhdgl.bh','yhdgl.ysrq','yhdgl.htbh','yhdgl.bz','yhdgl.gclb','yhdgl.gcxm','yhdgl.ghdw','tssl','fpbh','dhsl','cgjldw','sqdj','yssl','id','jhdj','hsxs'
	        ]),
	        remoteSort: true
	    });
	    
	    
    	//onload事件
		ds.on("load",function(store,records,options){
			//alert("MD");
			for(var i = 0; i < store.getCount(); i++){
	             var record = store.getAt(i);
	             //如果工程项目为空 则是自由入库
	             if(record.data.cgjhmx.xqjhDetail.xqjh.gcxm != ""){
	                  grid.getView().getRow(i).style.background="white";               
	             }else{
	             	  grid.getView().getRow(i).style.background="yellow";       
	             }
	        }
		});
		return ds;
	},
    
	/**
	 * PersonAction 列模型
	 */
	getColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
		    	this.sm,
		    	{header:'序号',width: 35,align:'center',renderer:function(value, cellmeta, record, rowIndex){
			        return rowIndex + 1;
			    }},
				{id:'zt',header: "状态", width: 80, sortable: true, dataIndex: 'zt',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value=='0'){
							metadata.attr ="style=background:#FF3333";
							return '未生效';
						}else if(value =='2'){
							metadata.attr ="style=background:#00FF33";
							return '已生效';
						}else if(value =='1'){
							metadata.attr ="style=background:#f84329";
							return '已入库';
						}
					}
				},
		    	{id:'yhdbh',header: "验货单编号", width: 80, sortable: true, dataIndex: 'yhdgl.bh'},
		    	{id:'wzbm',header: "物资名称型号规格", width: 150, sortable: true, dataIndex: 'wzdagl.wzmc',
		    		renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
		        		return value + "("+record.get('wzdagl.xhgg') +")";
		        	}
		    	},
		    	{id:'yssl',header: "验收数量", width: 60, sortable: true, dataIndex: 'yssl',align:'right',
		    		renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return parseFloat(value).toFixed(2);
					}
		    	},
		    	//{id:'hsxs',header: "换算系数", width: 60, sortable: true, dataIndex: 'hsxs'},
		    	{id:'cgjldw',header: "计量单位", width: 60, sortable: true, dataIndex: 'cgjldw'},
		    	{id:'jhdj',header: "计划单价", width: 60, sortable: true, dataIndex: 'jhdj',align:'right',
		    		renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return parseFloat(value).toFixed(2);
					}
		    	},
		    	{id:'jhje',header: "计划金额", width: 80, sortable: true,isExport:'false', dataIndex: 'jhje',align:'right',
		    		renderer:function(value,metadata,record,rowIndex,colIndex,store){
						/*
						var sl = record.get('yssl');
						var xs =record.get('hsxs');
						var dj = record.get('jhdj');
						
						return (sl*xs*dj).toFixed(2);
						*/
		    			return parseFloat(value).toFixed(2);
					}
		    	},
		    	{id:'sqdj',header: "税前单价", width: 100, sortable: true, dataIndex: 'sqdj',align:'right',
		    		renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return parseFloat(value).toFixed(2);
					}
		    	},
		    	{id:'sl',header: "税率", width: 50, sortable: true, align:'right',dataIndex: 'sl',align:'right',
		    		renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return parseFloat(value).toFixed(2);
					}
				},
		    	{id:'sjhj',header: "税价合计", width: 100, sortable: true, dataIndex: 'sjhj',align:'right',
		    		renderer:function(value,metadata,record,rowIndex,colIndex,store){
						/*
						var yhsl = record.get('yssl');
						var xs =record.get('hsxs');
						var sqj = record.get('sqdj');
						var sl =record.get('sl');
						var rkje = yhsl*xs*sqj;
						var se = sl*rkje;
						var sjhj = rkje+se
						
						//record.set('SE',se.toFixed(2));
						//record.set('SJHJ',sjhj.toFixed(2));
						return sjhj.toFixed(2) ;
						*/
		    			return parseFloat(value).toFixed(2);
					}
		    	},
		    	{id:'ysrq',header: "验收日期", width: 100, sortable: true, dataIndex: 'yhdgl.ysrq',
		    		 renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    		 	if(value=='' || value==null){
		    		 		return value;
		    		 	}
						var dt = formatDate(new Date(value['time']),"yyyy-MM-dd"); 
						return dt;         
					 }
		    	},
		    	{id:'bgy',header: "保管员", width: 60, sortable: true, dataIndex: 'yhdgl.personBgy.userName'},
		    	{id:'fpbh',header: "发票编号", width: 80, sortable: true, dataIndex: 'fpbh'},
		    	{id:'htbh',header: "合同编号", width: 80, sortable: true, dataIndex: 'yhdgl.htbh'},
				{id:'ghdw',header: "供应单位", width: 150, sortable: true, dataIndex: 'yhdgl.ghdw'}
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

