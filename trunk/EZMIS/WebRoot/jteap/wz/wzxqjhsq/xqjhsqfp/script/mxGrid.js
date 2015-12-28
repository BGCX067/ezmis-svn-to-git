

/**
 * 采购计划明细
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
		items:['-','<font color="#33CC66">绿色为已到货物资</font>']
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
	             //alert(record.data.dyszt);
	             //如果待验收状态不为0则是已指定
	             if(record.data.dyszt == "2"){
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
		
		
	    var cm=new Ext.grid.ColumnModel([
		    this.sm,		    
			{id:'isnew',header:'是否新物资',dataIndex:'isnew',width:75,sortable:true,renderer:function(value,metadata,record,rowIndex,colIndex,store){
 				return value=='1'?"是":"否";
			}},
			/*******第14列*******/
			{id:'jhy',header:'<span style="color:red">计划员</span>',dataIndex:'jhy',width:120,css:'background:red',
				editor:new Ext.form.ComboBox({
					id:'jhyCom',
					store:new Ext.data.Store({
						baseParams:{PAGE_FLAG:'PAGE_FLAG_NO'},
						autoLoad : true, 
						proxy: new Ext.data.ScriptTagProxy({url : link3}),
						reader: new Ext.data.JsonReader({root: 'list'},['userLoginName','userName'])						
					}), 
					valueField:'userLoginName',  
					displayField:'userName',  
					mode:'local',  
					blankText : '', 
					emptyText:'[无]', 
					hiddenName:'jhy', 
					editable:false,
					forceSelection:true,  
					triggerAction:'all',  
					anchor:'90%'}
					),renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value == ""){
							return "<span style='color:#FFB0B0'>请双击选择归属者!</span>";
						}else{
							return Ext.getCmp('jhyCom').getRawValue();
						}
                    }       
			    ,sortable:true},

/*******第5列*******/
{id:'wzmc',header:'物资名称',dataIndex:'wzmc',width:130,sortable:true},

/*******第6列*******/
{id:'xhgg',header:'型号规格',dataIndex:'xhgg',width:130,sortable:true},

/*******第7列*******/
{id:'sqsl',header:'申请数量',dataIndex:'sqsl',width:60,sortable:true,align:'right',editor:
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

/*******第8列*******/
{id:'jldw',header:'计量单位',dataIndex:'jldw',width:60,sortable:true},

/*******第9列*******/
{id:'gjdj',header:'估计单价',dataIndex:'gjdj',width:60,sortable:true,align:'right',
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
**/
renderer:function(value,metadata,record,rowIndex,colIndex,store){
    if(!value || value==''){
          value = 0;
    }
    return parseFloat(value).toFixed(2);
}},

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
	renderer : function(value, metadata, record, rowIndex, colIndex, store) {
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

{id:'isCancel',header:'是否作废',dataIndex:'isCancel',width:60,sortable:true,
	renderer:function(value,metadata,record,rowIndex,colIndex,store){
 				return value=='1'?"未作废":"已作废";
	}
},
{id:'remark',header:'备注',width:100,sortable:true,dataIndex:'remark'}
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