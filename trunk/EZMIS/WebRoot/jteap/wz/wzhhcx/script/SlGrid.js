var responseObject = null;
/**
 * 字段列表
 */
SlGrid=function(){
    var defaultDs=this.getDefaultDS(link22+"?queryParamsSql=obj.zt='1' or obj.zt='2'");
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
	SlGrid.superclass.constructor.call(this,{
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
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=slGrid.getSelectionModel().getSelections()[0];
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
		if(select){
			var yhdmxs = select.json.yhdgl.yhdmxs;
			var ids = "";
			for (var i = 0; i < yhdmxs.length; i++) {
				if(yhdmxs[i]!=null){
					if(ids.indexOf(yhdmxs[i].cgjhmx.id)<0)
						ids = ids + "'"+yhdmxs[i].cgjhmx.id + "',";
				}else{
					if(ids.indexOf(select.json.cgjhmx.id)<0)
						ids = ids + "'"+select.json.cgjhmx.id + "',";
				}
			}
			ids = ids.substring(0,ids.length-1);
			var url = contextPath+"/jteap/wz/cgjhmx/CgjhmxAction!showListAction.do?";
			if(responseObject != null){
				var url=link6+"&docid="+select.json.yhdgl.id;
				new $FW({url:url,height:645,width:800,type:'T2',userIF:true,baseParam:{responseObject:responseObject}}).show();	//模式对话框
			}else{
				AjaxRequest_Sync(url,{queryParamsSql:"obj.id in ("+ids+")"},function(obj){
					var strReturn=obj.responseText;
					responseObject=Ext.util.JSON.decode(strReturn);
					var url=link23+"&docid="+select.json.yhdgl.id;
					new $FW({url:url,height:645,width:800,type:'T2',userIF:true,baseParam:{responseObject:responseObject}}).show();	//模式对话框
				//result=showModule(url,true,800,645);
	        	});
			}
	        
		}
	});
	
}
Ext.extend(SlGrid, Ext.grid.GridPanel, {
	sm:new Ext.grid.RowSelectionModel(),
	
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
	        "sjhj",
	        "jhje",
	        "cgjhmx",
			"cgjhmx.xqjhDetail",
			"cgjhmx.xqjhDetail.xqjh",
			"cgjhmx.xqjhDetail.xqjh.gclb",
			"cgjhmx.xqjhDetail.xqjh.gcxm",
			"cgjhmx.xqjhDetail.xqjh.sqbm",
			'ghdw','xh','sl','zf','wzdagl','wzdagl.wzmc','yhdgl','yhdgl.yhdmxs','yhdgl.personCgy','yhdgl.personBgy',
			'yhdgl.personCgy.userName','yhdgl.personCgy.id','yhdgl.personCgy.userLoginName',
			'yhdgl.personBgy.userName','yhdgl.personBgy.id','yhdgl.personBgy.userLoginName','zt',
			'yhdgl.id','yhdgl.bh','yhdgl.ysrq','yhdgl.htbh','yhdgl.ghdw','tssl','fpbh','dhsl','cgjldw','sqdj','yssl','id','jhdj','hsxs'
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
		    	{id:'xh',header: "序号", width: 40, sortable: true, 
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
					 	return rowIndex+1;
					}
				},
		    	{id:'yhdbh',header: "验货单编号", width: 80, sortable: true, dataIndex: 'yhdgl.bh'},
		    	{id:'ysrq',header: "验收日期", width: 100, sortable: true, dataIndex: 'yhdgl.ysrq',
		    		 renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    		 	if(value=='' || value==null){
		    		 		return value;
		    		 	}
						var dt = formatDate(new Date(value['time']),"yyyy-MM-dd"); 
						return dt;         
					 }
		    	},
		    	{id:'dhsl',header: "到货数量", width: 60, sortable: true, dataIndex: 'dhsl',align:'dhsl',
		    		renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
		    	},
		    	{id:'yssl',header: "验收数量", width: 60, sortable: true, align:'dhsl',dataIndex: 'yssl',
		    		renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
		    	},
		    	{id:'cgjldw',header: "计量单位", width: 60, sortable: true, dataIndex: 'cgjldw'},
				{id:'ghdw',header: "供应单位", width: 150, sortable: true, dataIndex: 'yhdgl.ghdw'},
		    	{id:'jhdj',header: "计划单价", width: 60, sortable: true, dataIndex: 'jhdj',align:'right',
		    		renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
		    	},
		    	{id:'jhje',header: "计划金额", width: 80, sortable: true, dataIndex: 'jhje',align:'right',
		    		renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    			/*
						var sl = record.get('yssl');
						var xs =record.get('hsxs');
						var dj = record.get('jhdj');
						
						return (sl*xs*dj).toFixed(2);
						*/
		    			if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
		    	},
		    	{id:'sqdj',header: "税前单价", width: 100, sortable: true, dataIndex: 'sqdj',align:'right',
		    		renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
		    	},
		    	{id:'sl',header: "税率", width: 50, sortable: true, dataIndex: 'sl',
		    		renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
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
		    			if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
		    		}
				}
		    	/*
		    	{id:'wzbm',header: "物资", width: 150, sortable: true, dataIndex: 'wzdagl.wzmc'},
		    	{id:'hsxs',header: "换算系数", width: 60, sortable: true, dataIndex: 'hsxs'},
		    	},
		    	{id:'bgy',header: "保管员", width: 60, sortable: true, dataIndex: 'yhdgl.personBgy.userName'},
		    	{id:'fpbh',header: "发票编号", width: 80, sortable: true, dataIndex: 'fpbh'},
		    	{id:'htbh',header: "合同编号", width: 80, sortable: true, dataIndex: 'yhdgl.htbh'},
				{id:'cgjhmx.xqjhDetail.xqjh.gclb',header: "工程类别", width: 100, sortable: true, dataIndex: 'cgjhmx.xqjhDetail.xqjh.gclb'},
				{id:'cgjhmx.xqjhDetail.xqjh.gcxm',header: "工程项目", width: 100, sortable: true, dataIndex: 'cgjhmx.xqjhDetail.xqjh.gcxm'},
				{id:'cgjhmx.xqjhDetail.xqjh.sqbm',header: "班组", width: 100, sortable: true, dataIndex: 'cgjhmx.xqjhDetail.xqjh.sqbm'}
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
	}

});

