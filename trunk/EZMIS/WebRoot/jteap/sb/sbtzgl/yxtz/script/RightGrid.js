/**
 * 字段列表
 */
RightGrid=function(type){
    var defaultDs="";
    this.type = type;
    if(type == 1){
	    defaultDs = this.getDefaultDS(type,link1+"?limit=20&sbbm="+sbbm);
    }else if(type == 2){
    	defaultDs = this.getDefaultDS(type,link2+"?limit=20&sbbm="+sbbm);
    }else if(type == 3){
    	defaultDs = this.getDefaultDS(type,link3+"?limit=20&sbbm="+sbbm);
    }else if(type == 4){
    	defaultDs = this.getDefaultDS(type,link4+"?limit=20&sbbm="+sbbm);
    }
    defaultDs.load();
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: 20,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据"
	});
	RightGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(type),
		sm: this.sm,
	    margins:'2px 2px 2px 2px',
		width:800,
		height:570,
		loadMask: true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar,
		stripeRows: true
	});	
	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		
		//var btnDel=mainToolbar.items.get('btnDel');
		//var btnModify=mainToolbar.items.get('btnModify');
		
//		if(oCheckboxSModel.getSelections().length==1){
//			if(btnModify) btnModify.setDisabled(false);
//		}else{
//			if(btnModify) btnModify.setDisabled(true);
//		}
//		
//		if(oCheckboxSModel.getSelections().length<=0){
//			if(btnDel) btnDel.setDisabled(true);
//		}else{
//			if(btnDel) btnDel.setDisabled(false);
//		}
	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		//var select=rightGrid.getSelectionModel().getSelections()[0];
		
	});
	
}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
	
	sm:new Ext.grid.CheckboxSelectionModel(),
	
	/**
	 * 取得默认数据源
	 * 返回数据格式
	 * {success:true,totalCount:20,list:[{id:'123',field1:'111',...,field5'222'},{...},...]} 
	 */
	getDefaultDS:function(type,url){
	    var ds = "";
	     if(type == 1){
		    ds = new Ext.data.Store({
		        proxy: new Ext.data.ScriptTagProxy({
		            url: url
		        }),
		        reader: new Ext.data.JsonReader({
		            root: 'list',
		            totalProperty: 'totalCount',
		            id: 'id'
		        }, [
					"id","qxzy","sbmc","qxmc","qxtz","fxr","fxsj"
		        ]),
		        remoteSort: true,
		        //如果为true,每次store在加载时或者一条记录被删除时, 清除所有改动过的记录信息
		        pruneModifiedRecords: true
		    });
	    }else if(type == 2){
	    	ds = new Ext.data.Store({
		        proxy: new Ext.data.ScriptTagProxy({
		            url: url
		        }),
		        reader: new Ext.data.JsonReader({
		            root: 'list',
		            totalProperty: 'totalCount',
		            id: 'id'
		        }, [
					"id","sbbm","sbmc","xsjgg","dw","edsl","sjsl","yjsl","remark"
		        ]),
		        remoteSort: true,
		        //如果为true,每次store在加载时或者一条记录被删除时, 清除所有改动过的记录信息
		        pruneModifiedRecords: true
		    });
	    }else if(type == 3){
	    	ds = new Ext.data.Store({
		        proxy: new Ext.data.ScriptTagProxy({
		            url: url
		        }),
		        reader: new Ext.data.JsonReader({
		            root: 'list',
		            totalProperty: 'totalCount',
		            id: 'id'
		        }, [
					"id","sbbm","sbmc","gg","scpjrq","scpjjb","scpjr","bcpjrq","bcpjjb","bcpjr","remark"
		        ]),
		        remoteSort: true,
		        //如果为true,每次store在加载时或者一条记录被删除时, 清除所有改动过的记录信息
		        pruneModifiedRecords: true
		    });
	    }else if(type == 4){
	    	ds = new Ext.data.Store({
		        proxy: new Ext.data.ScriptTagProxy({
		            url: url
		        }),
		        reader: new Ext.data.JsonReader({
		            root: 'list',
		            totalProperty: 'totalCount',
		            id: 'id'
		        }, [
					"id","ydmc","status","sqbm","fzr","sqsj"
		        ]),
		        remoteSort: true,
		        //如果为true,每次store在加载时或者一条记录被删除时, 清除所有改动过的记录信息
		        pruneModifiedRecords: true
		    });
	    }
		return ds;
	},
    
	/**
	 * 缺陷Action 列模型
	 */
	getColumnModel:function(type){
		
		var grid = this;
		//var dict_sbtzgl=$dictList("sbtz");
		var cm = "";
		if(type == 1){
			cm=new Ext.grid.ColumnModel([
		    	this.sm,
				{id:'qxzy',header: "缺陷专业", width: 80, sortable: true, dataIndex: 'qxzy'},
				{id:'sbmc',header: "设备名称", width: 80, sortable: true, dataIndex: 'sbmc'},
				{id:'qxmc',header: "缺陷名称", width: 80, sortable: true, dataIndex: 'qxmc'},
				{id:'qxtz',header: "缺陷状态", width: 80, sortable: true, dataIndex: 'qxtz'},
				{id:'fxr',header: "发现人", width: 80, sortable: true, dataIndex: 'fxr'},
				{id:'fxsj',header: "发现时间", width: 80, sortable: true, dataIndex: 'fxsj',
				renderer:function(value,metadata,record,rowIndex,colIndex,store){
					var fxsj = (value == null)?null:formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
					return fxsj;
				}}
			]);
		}else if(type == 2){
			cm=new Ext.grid.ColumnModel([
		    	this.sm,
				{id:'sbbm',header: "设备编码", width: 80, sortable: true, dataIndex: 'sbbm'},
				{id:'sbmc',header: "设备名称", width: 80, sortable: true, dataIndex: 'sbmc'},
				{id:'xsjgg',header: "型式及规格", width: 80, sortable: true, dataIndex: 'xsjgg'},
				{id:'dw',header: "单位", width: 80, sortable: true, dataIndex: 'dw'},
				{id:'edsl',header: "额定数量", width: 80, sortable: true, dataIndex: 'edsl'},
				{id:'sjsl',header: "实际数量", width: 80, sortable: true, dataIndex: 'sjsl'},
				{id:'yjsl',header: "预警数量", width: 80, sortable: true, dataIndex: 'yjsl'},
				{id:'remark',header: "备注", width: 80, sortable: true, dataIndex: 'remark'}
			]);
		}else if(type == 3){
			cm=new Ext.grid.ColumnModel([
		    	this.sm,
				{id:'sbbm',header: "设备编码", width: 80, sortable: true, dataIndex: 'sbbm'},
				{id:'sbmc',header: "设备名称", width: 80, sortable: true, dataIndex: 'sbmc'},
				{id:'gg',header: "规格", width: 80, sortable: true, dataIndex: 'gg'},
				{id:'scpjrq',header: "上次评级日期", width: 80, sortable: true, dataIndex: 'scpjrq',
				renderer:function(value,metadata,record,rowIndex,colIndex,store){
					var scpjrq = (value == null)?null:formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
					return scpjrq;
				}},
				{id:'scpjjb',header: "上次评级级别", width: 80, sortable: true, dataIndex: 'scpjjb'},
				{id:'scpjr',header: "上次评级人", width: 80, sortable: true, dataIndex: 'scpjr'},
				{id:'bcpjrq',header: "本次评级日期", width: 80, sortable: true, dataIndex: 'bcpjrq',
				renderer:function(value,metadata,record,rowIndex,colIndex,store){
					var bcpjrq = (value == null)?null:formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
					return bcpjrq;
				}},
				{id:'bcpjjb',header: "本次评级级别", width: 80, sortable: true, dataIndex: 'bcpjjb'},
				{id:'bcpjr',header: "本次评级人", width: 80, sortable: true, dataIndex: 'bcpjr'},
				{id:'remark',header: "备注", width: 80, sortable: true, dataIndex: 'remark'}
			]);
		}else if(type == 4){
			cm=new Ext.grid.ColumnModel([
		    	this.sm,
				{id:'ydmc',header: "异动名称", width: 80, sortable: true, dataIndex: 'ydmc'},
				{id:'status',header: "异动状态", width: 80, sortable: true, dataIndex: 'status'},
				{id:'sqbm',header: "申请部门", width: 80, sortable: true, dataIndex: 'sqbm'},
				{id:'fzr',header: "负责人", width: 80, sortable: true, dataIndex: 'fzr'},
				{id:'sqsj',header: "申请时间", width: 80, sortable: true, dataIndex: 'sqsj',
				renderer:function(value,metadata,record,rowIndex,colIndex,store){
					var sqsj = (value == null)?null:formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
					return sqsj;
				}}
			]);
		}
		return cm;
	},
	/**
	 * 切换数据源->LogAction!showList
	 */
	changeToListDS:function(type,url){
		var ds = "";
		if(type == 1){
			ds = this.getDefaultDS(url);	
		}else if(type ==2){
			ds = this.getDefaultDS(url)
		}else if(type == 3){
			ds = this.getDefaultDS(url)
		}else if(type == 4){
			ds = this.getDefaultDS(url)
		}
		var cm=this.getColumnModel(type);
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

/**
 * Tab页(包括缺陷、备品备件、评级、异动)
 */
var qxGrid = new RightGrid(1);
var bpbjGrid = new RightGrid(2);
var pjGrid = new RightGrid(3);
var ydGrid = new RightGrid(4);
var tabPanel = new Ext.TabPanel({
	deferredRender:false,
	activeTab:2,
	autoWidth:true,
	items: [{
        title: '缺陷',
        tabTip: '缺陷',
        items:[qxGrid]
    },{
        title: '备品备件',
        tabTip: '备品备件',
        items:[bpbjGrid]
    },{
        title: '评级',
        tabTip: '评级',
        items:[pjGrid]
    },{
        title: '异动',
        tabTip: '异动',
        items:[ydGrid]
    }]
});
