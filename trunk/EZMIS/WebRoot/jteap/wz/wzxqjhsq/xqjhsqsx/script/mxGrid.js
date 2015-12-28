var dicts2 = $dictListAjax("WZSQZT");
var channelData = [
    [dicts2[0].value,dicts2[0].key],[dicts2[1].value,dicts2[1].key]
];
function getStatusCnName2(qxzy) {
	for(var i=0;i<dicts2.length;i++){
		var dict = dicts2[i];
		if (dict.value == qxzy) {
			return dict.key;
		}
	}
}  
/**
 * 需求计划申请明细
 */
MxRightGrid=function(){
    var defaultDs=this.getDefaultDS(link2+"?limit=50");
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: 50,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'导出Excel',handler:function(){
		exportSelectedExcel(grid);
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
		title:"需求计划申请明细",
		tbar:this.pageToolbar 
	});	
	
}
Ext.extend(MxRightGrid, Ext.grid.EditorGridPanel, {
	//sm:new Ext.grid.RowSelectionModel(),
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
			"id",
			"isnew",
			"xqjhsqid",
			"wzbm",
			"xh",
			"wzmc",
			"xhgg",
			"sqsl",
			"jldw",
			"gjdj",
			"provider",
			"xysj",
			"done",
			'je',
			"jhy",
			"sfdh",
			"isCancel",
			"cflag",
			"remark","dyszt"
	        ]),
	        remoteSort: true
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
	 * PersonAction 列模型
	 */
	getColumnModel:function(){
		var personMap = new Hash();
		AjaxRequest_Sync(link3, null, function(ajax){
			var obj = new String(ajax.responseText).evalJSON();
			var list = obj.list;
			for (var i=0;i<list.length;i++){
				personMap.set(list[i].userLoginName,list[i].userName);
			}
		})
	    var cm=new Ext.grid.ColumnModel([
		    this.sm,		    
		    {id:'bh',header:'序号',dataIndex:'xqjhsqid',width:40,sortable:false,
				renderer:function(value,metadata,record,rowIndex,colIndex,store){
					return rowIndex+1;
				}
			},
			{id:'isnew',header:'是否新物资',dataIndex:'isnew',width:75,sortable:true,renderer:function(value,metadata,record,rowIndex,colIndex,store){
 				return value=='1'?"<span style='color:red'>是</span>":"否";
			}},
			{id:'isCancel',header:'<span style="color:red">是否作废</span>',dataIndex:'isCancel',width:80,sortable:true,editor:new Ext.form.ComboBox({
					id:'isCanceljhyCom',
					store:new Ext.data.SimpleStore({
						fields:['isCancelValue','isCancelKey'],
				        data:channelData
					}), 
					valueField:'isCancelValue',  
					displayField:'isCancelKey',  
					mode:'local',
					hiddenName:'isCancel', 
					editable:false,
					forceSelection:true,  
					triggerAction:'all',  
					anchor:'50%'}
					),renderer:function(value){
						return getStatusCnName2(value);//Ext.getCmp('isCanceljhyCom').getRawValue();
                    }},
			{id:'xqjhsqid',header:'需求计划申请编号',dataIndex:'xqjhsqid',width:130,sortable:false,hidden:true},
			/*******第14列*******/
			{id:'jhy',header:'计划员',dataIndex:'jhy',width:80
				,renderer:function(value,metadata,record,rowIndex,colIndex,store){
					return personMap.get(value);
				}
			    ,sortable:true},

/*******第5列*******/
{id:'wzmc',header:'物资名称',dataIndex:'wzmc',width:130,sortable:true},

/*******第6列*******/
{id:'xhgg',header:'型号规格',dataIndex:'xhgg',width:130,sortable:true},

/*******第7列*******/
{id:'sqsl',header:'申请数量',dataIndex:'sqsl',width:60,sortable:true,align:'right',
renderer:function(value,metadata,record,rowIndex,colIndex,store){
    if(!value || value==''){
          value = 0;
    }
    return parseFloat(value).toFixed(2);
}},
/**
,editor:
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
**/
/*******第8列*******/
{id:'jldw',header:'计量单位',dataIndex:'jldw',width:60,sortable:true},

/*******第9列*******/
{id:'gjdj',header:'估计单价',dataIndex:'gjdj',width:60,sortable:true,align:'right',
renderer:function(value,metadata,record,rowIndex,colIndex,store){
    if(!value || value==''){
          value = 0;
    }
    return parseFloat(value).toFixed(2);
}},
/**
editor:
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
**/
/*******第10列*******/
{id:'je',header:'金额',dataIndex:'je',width:80,sortable:false,align:'right',
renderer:function(value,metadata,record,rowIndex,colIndex,store){
    if(!value || value==''){
          value = 0;
    }
    return "<font size='2' style='font-weight:bold'>"+parseFloat(record.data["sqsl"]*record.data["gjdj"]).toFixed(2)+"</font>";
}},

/*******第11列*******/
{id:'xysj',header:'需用时间',dataIndex:'xysj',width:80,sortable:true, 
	renderer:function(value,metadata,record,rowIndex,colIndex,store){
			var time = 0;
			if(value == null){
				return "";
			}else{
				if (typeof value == "number") {
					time = value;
				} else if (typeof value == "object") {
					time = value.time;
				}
				dt = formatDate(new Date(time), "yyyy-MM-dd");
			}
			return dt;
	}
},
{id:'remark',header:'备注',width:100,sortable:true,dataIndex:'remark'}
]

);
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