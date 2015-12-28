
var columns0 = ['xh','wzmc','xhgg','jldw','jhdj','pjj','dqkc','je','yfpsl','dyx','zgcbde','zdcbde','wzlb.wzlbmc','abcfl','tsfl','kw.cwmc','cskc','csjg','tjm','id'];
/**
 * 字段列表
 */
WzdaGrid=function(){
    var defaultDs=this.getDefaultDS(link16);
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'导出Excel',handler:function(){
		//exportExcel(grid,true);
			var path = grid.store.proxy.url;
			var paths = path.split("?")
			var temp1 = paths[0]
			var temp3 = paths[1]
			temp1 = temp1.split("!")[0]
			path = temp1 + "!exportWzDAExcel.do?" + temp3
			window.open(path);
		}},'-','<font color="red">*双击查看详细信息</font>']
	});
	WzdaGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getThisColumnModel(),
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
		/*
		if(mainToolbar){
				var btnDel=mainToolbar.items.get('btnDelDAModule');
				var btnModify=mainToolbar.items.get('btnModiDAModule');
				
				if(oCheckboxSModel.getSelections().length==1){
					if(btnModify) btnModify.setDisabled(false);
				}else{
					if(btnModify) btnModify.setDisabled(true);
				}
				
				if(oCheckboxSModel.getSelections().length<=0){
					if(btnDel) btnDel.setDisabled(true);
				}else{
					if(btnDel) btnDel.setDisabled(false);
				}
		}
		var select=WzdaGrid.getSelectionModel().getSelected();
		if(select){
			window.returnValue = select.json.wzmc+"|"+select.json.id;
		}
		*/
	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=wzdaGrid.getSelectionModel().getSelected();
		var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_SWZDA&docid="+select.json.id+"&st=02";
		var args = "wzlb|"+select.json.wzlb.wzlbmc+";cwmc|"+select.json.kw.cwmc;
		showModule(url,true,800,600,args);
		//window.open(url);
	});
}
Ext.extend(WzdaGrid, Ext.grid.GridPanel, {
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
	        }, columns0
	        ),
	        //baseParams :{formSn:formSn},
	        remoteSort: true	
	    });
		return ds;
	},
    
	/**
	 * PersonAction 列模型
	 */
	getThisColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
		    	//this.sm,		
		    	{id:'xh',header: "序号", width: 40, sortable: true, 
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
					 	return rowIndex+1;
					}
				},    	
		        {id:'wzmc',header: "物资名称", width: 100, sortable: true, dataindex: 'wzmc'},
		        {id:'xhgg',header: "型号规格", width: 150, sortable: true, dataindex: 'xhgg'},
		        {id:'jldw',header: "记量单位", width: 80, sortable: true, dataindex: 'jldw'},
	            {id:'jhdj',header: "计划单价", width: 80, sortable: true, align:'right',dataindex: 'jhdj',
	            	renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value==null){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					} 
	            },
	            {id:'pjj',header: "平均价", width: 80, sortable: true, align:'right',dataindex: 'pjj',
	            	renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value==null){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
	            },
	            {id:'dqkc',header: "当前库存", width: 80, sortable: true,align:'right',dataindex: 'dqkc',
	            	renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value==null){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
	            },
	            {id:'je',header: "金额", width: 80, sortable: true,align:'right',dataindex:'je',renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
	            	value = record.data["dqkc"]*record.data["jhdj"];
					if(value==null){
						value = 0;
					}
					return parseFloat(value).toFixed(2);
	            }},
	            {id:'yfpsl',header: "已分配量", width: 80, sortable: true,align:'right', dataindex: 'yfpsl',
	            	renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value==null){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
	            },
	            {id:'dyx',header: "代用型", width: 80, sortable: true, dataindex: 'dyx'},
	            {id:'zgcbde',header: "高储", width: 80, sortable: true, align:'right',dataindex: 'zgcbde',
	            	renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value==null){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
	            },
	            {id:'zdcbde',header: "低储", width: 80, sortable: true,align:'right', dataindex: 'zdcbde',
	            	renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value==null){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
	            },
	            {id:'wzlbbm',header: "物资类别", width: 80, sortable: true, dataindex: 'wzlb.cwmc'},
	            {id:'abcfl',header: "abc分类", width: 80, sortable: true, dataindex: 'abcfl'},
	            {id:'tsfl',header: "特殊分类", width: 80, sortable: true, dataindex: 'tsfl'},
	            {id:'kwbm',header: "库位", width: 80, sortable: true, dataindex: 'kw.cwmc'},
	            {id:'cskc',header: "初始库存", width: 80, sortable: true,align:'right', dataindex: 'cskc',
	            	renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value==null){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
	            },
	            {id:'csjg',header: "初始价格", width: 80, sortable: true, align:'right',dataindex: 'csjg',
	            	renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value==null){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
	            },
	            {id:'tjm',header: "统计码", width: 80, sortable: true, dataindex: 'tjm'}
			]);
		return cm;
	},
	/**
	 * 切换数据源->LogAction!showList
	 */
	changeToListDS:function(url){
		var ds = this.getDefaultDS(url);	
		var cm=this.getThisColumnModel();
		this.pageToolbar.bind(ds);
		this.reconfigure(ds,cm);
	} 
});
//var columns1=  [1,2,3,4,5,6];
//var columns2=  [1,2,5,7];
//var columns3=  [1,7,8,9,10,11,12,13,14,15,16];

