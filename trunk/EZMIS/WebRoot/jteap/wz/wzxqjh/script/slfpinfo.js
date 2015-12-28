// 需求计划编号
var txtXqjhbh= new Ext.form.TextField({
	id: 'txtXqjhbh',
	renderTo: 'divXqjhbh',
	maxLength: 32,
	width: 120,
	value: window.dialogArguments.pjfl,
	maxLengthText: '最长32个字符',
	allowBlank: false,
	listeners:{
		"beforerender":function(param){
			Ext.getCmp('txtXqjhbh').readOnly = true;
		}
	}
});


// 生效时间
var txtSxsj= new Ext.form.TextField({
	id: 'txtSxsj',
	renderTo: 'divSxsj',
	minValue: 0,
	maxLength: 64,
	width: 120,
	allowBlank: false,
	listeners:{
		"beforerender":function(param){
			Ext.getCmp('txtSxsj').readOnly = true;
		}
	}
});

// 操作员
var txtOperator= new Ext.form.TextField({
	id: 'txtOperator',
	renderTo: 'divOperator',
	maxLength: 500,
	maxLength: 64,
	width: 120,
	maxLengthText: '最长500个字符',
	listeners:{
		"beforerender":function(param){
			Ext.getCmp('txtOperator').readOnly = true;
		}
	}
});

// 工程类别
var txtGclb	= new Ext.form.TextField({
	id: 'txtGclb',
	renderTo: 'divGclb',
	maxLength: 500,
	maxLength: 64,
	width: 120,
	maxLengthText: '最长500个字符',
	listeners:{
		"beforerender":function(param){
			Ext.getCmp('txtGclb').readOnly = true;
		}
	}
});

// 工程项目
var txtGcxm	= new Ext.form.TextField({
	id: 'txtGcxm',
	renderTo: 'divGcxm',
	maxLength: 500,
	maxLength: 64,
	width: 120,
	maxLengthText: '最长500个字符',
	listeners:{
		"beforerender":function(param){
			Ext.getCmp('txtGcxm').readOnly = true;
		}
	}
});

// 申请部门
var txtSqbm	= new Ext.form.TextField({
	id: 'txtSqbm',
	renderTo: 'divSqbm',
	maxLength: 500,
	maxLength: 64,
	width: 120,
	maxLengthText: '最长500个字符',
	listeners:{
		"beforerender":function(param){
			Ext.getCmp('txtSqbm').readOnly = true;
		}
	}
});

// 申请时间
var txtSqsj	= new Ext.form.TextField({
	id: 'txtSqsj',
	renderTo: 'divSqsj',
	maxLength: 500,
	maxLength: 64,
	width: 120,
	maxLengthText: '最长500个字符',
	listeners:{
		"beforerender":function(param){
			Ext.getCmp('txtSqsj').readOnly = true;
		}
	}
});



var xqjhid = window.dialogArguments.xqjhid;

if(xqjhid != null){
	// 修改时 赋值
	txtXqjhbh.setValue(window.dialogArguments.xqjhbh);
	txtSxsj.setValue(window.dialogArguments.sxsj);
	txtOperator.setValue(window.dialogArguments.personName);
	txtGclb.setValue(window.dialogArguments.gclb);
	txtGcxm.setValue(window.dialogArguments.gcxm);
	txtSqbm.setValue(window.dialogArguments.sqbm);
	txtSqsj.setValue(window.dialogArguments.sqsj);
}

/** ********数量分配（需求计划明细列表）********* */
/**
 * 字段列表
 */
XqjhDetailGrid=function(xqjhid){
	
    var defaultDs=this.getDefaultDS(link3+"?sqjhid="+xqjhid);
    defaultDs.load();
    defaultDs.on('load',function(store, records, options){
    	(function(){
		    grid.getSelectionModel().selectFirstRow();
		}).defer(1);
    });
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: 20,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据"
//		,
//		items:['-',{text:'导出Excel',handler:function(){
//		exportExcel(grid,true);
//		
//		}}]
	});
	XqjhDetailGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(),
	 	renderTo:"divXqjhDetailForslfpGrid",
		sm: this.sm,
	    margins:'2px 2px 2px 2px',
		width:780,
		height:255,
		loadMask: true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar,
		stripeRows: true,
		deferRowRender:false
	});	
}



Ext.extend(XqjhDetailGrid, Ext.grid.GridPanel, {
	
	sm:new Ext.grid.RowSelectionModel(),
	
	/**
	 * 取得默认数据源 返回数据格式
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
				"ID", "XQJHBH", "WZMC", "XHGG", "DQKC", "YFPSL", "CKMC",
							"WZBM", "XH", "PZSL", "JLDW", "JHDJ", "FREE",
							"CGSL", "DHSL", "LYSL", "SLSL", "PJJ", "KWBM"
	        ]),
	        remoteSort: true,
	        // 如果为true,每次store在加载时或者一条记录被删除时, 清除所有改动过的记录信息
	        pruneModifiedRecords: true
	    });
		return ds;
	},
    
	/**
	 * JctzAction 列模型
	 */
	getColumnModel:function(){
		
		var grid = this;
		
	    var cm=new Ext.grid.ColumnModel([
				new Ext.grid.RowNumberer({header:'序号',width:40}),
				{id:'id',header: "需求计划明细编号", width: 100, sortable: true, hidden:true, dataIndex: 'ID'	},
				{id:'wzmc',header: "物资名称规格", width: 120, sortable: true, dataIndex: 'WZMC',
					renderer:function(value, metadata, record, rowIndex, colIndex, store){
						var wzmc = record.data.WZMC;
						var xhgg = (record.data.XHGG == null)?"":record.data.XHGG;
						return wzmc+"("+xhgg+")";
					}
				},
				{id:'ckmc',header: "仓库", width: 100, sortable: true, dataIndex: 'CKMC'	},
				{id:'wzbm',header: "物资编码", width: 60, sortable: true,hidden:true, dataIndex: 'WZBM'},
				{id:'kwbm',header: "库位编码", width: 60, sortable: true,hidden:true, dataIndex: 'KWBM'},
				{id:'jldw',header: "计量单位", width: 60, sortable: true, dataIndex: 'JLDW'},
				{id:'pjj',header: "平均价", width: 60, sortable: true, dataIndex: 'PJJ' , align:'right',editor:
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
				{id:'pzsl',header: "批准数量", width: 60, sortable: true, dataIndex: 'PZSL',align:'right',editor:
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
				{id:'free',header: "已分配", width: 80, sortable: true, dataIndex: 'FREE',align:'right',editor:
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
//				,
//				renderer:function(value,metadata,record,rowIndex,colIndex,store){
//					var free = "";
//					if(value==null || value == ""){
//						free = 0;
//					}else{
//						free = value;
//					}
//					return free;
//				}},
				{id:'cgsl',header: "已采购", width: 60, sortable: true, dataIndex: 'CGSL',align:'right',editor:
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
				{id:'dhsl',header: "已到货", width: 60, sortable: true, dataIndex: 'DHSL',align:'right',editor:
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
				{id:'slsl',header: "已申领", width: 60, sortable: true, dataIndex: 'SLSL',align:'right',editor:
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
				{id:'lysl',header: "已领用", width: 60, sortable: true, dataIndex: 'LYSL',align:'right',editor:
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
	}
})

var xqjhDetailGrid = new XqjhDetailGrid(xqjhid);

function setting(){
	var select=xqjhDetailGrid.getSelectionModel().getSelections()[0];
	var obj = {};
	if(select){
		var xqjhDetailId = select.json.ID;           //需求计划明细编号
		var freekc = select.json.DQKC - select.json.YFPSL;
		var wzbm  = select.json.WZBM;            //物资编码
		var wzmc = select.json.WZMC;             //物资名称
		var xhgg = select.json.XHGG;               //型号规格
		var free = select.json.FREE;                  //自由库存量
		var url = contextPath + "/jteap/wz/wzxqjh/kcltz.jsp?wzbm="+wzbm+"&wzmc="+encodeURIComponent(wzmc)+"&xhgg="+xhgg+"&free="+free+"&xqjhDetailId="+xqjhDetailId+"&freekc="+freekc;
		var returnValue = showIFModule(url,"需求计划分配调整","true",500,150,obj);
		if(returnValue){
			xqjhDetailGrid.getStore().reload();
			return ;
		}
	}else{
		alert("请至少选择一条记录!");
		return;
	}
}

//库存量分配后提交
//function save(){
//	alert(document.getElementById("tzsl").value);
//	var fpsl = document.getElementById("tzsl").value;
//	Ext.Ajax.request({
//			url:link4,
//			success:function(ajax){
//		 		var responseText=ajax.responseText;	
//		 		var responseObject=Ext.util.JSON.decode(responseText);
//		 		if(responseObject.success){
//					if(responseObject.flag){
//						alert("库存量调整成功!'");
//						xqjhDetailGrid.getStore().reload();
//					}else{
//						alert("库存量不足!");
//						return;
//					}
//		 		}else{
//		 			alert(responseObject.msg);
//		 		}				
//			},
//		 	failure:function(){
//		 		alert("提交失败");
//		 	},
//		 	method:'POST',
//		 	params: {fpsl:fpsl}// Ext.util.JSON.encode(selections.keys)
//		});
//	window.close();
//}

