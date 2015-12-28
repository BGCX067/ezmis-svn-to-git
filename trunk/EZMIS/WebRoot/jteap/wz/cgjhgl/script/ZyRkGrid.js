
/**
 * 采购计划
 */
ZyRkGrid=function(){
    var defaultDs=this.getDefaultDS();
    var grid=this;
    this.Toolbar=new Ext.Toolbar({
    	autoWidth:true,   
        autoShow:true, 
//		items:['&nbsp;|&nbsp;','<font color="red">*双击记录查看采购明细</font>']
		items:[{text:'增加',handler:function(){
			   var url = contextPath+"/jteap/wz/wzlb/selectWzdaIndex.jsp";
		         // var result = showModule(url, true, 800, 500,'singleSelect|0');
		          new $FW({
	             url:url,id:'wzxz',type:'T1',
	             resizable:true,menubar:false,scrollbars:true,toolbar:false}).show();
	             
	             
				//var rec = grid.createNewRecord();
				//var bOK = this.fireEvent("onadd",rec);
				///if(typeof bOK == 'undefined' || bOK == true){
				//	grid.createNew(rec);
				//}
				//var store = grid.getStore();
                //var records = store.getRange();
				//for(i=0;i<records.length;i++){
				//	records[i].set('XH',i+1);
				//}
		}},'-',{text:'删除',handler:function(){
				var items = grid.selModel.getSelections();
				for(var i=0;i<items.length;i++){
					var item = items[i];
					grid.store.remove(item);
				}
				var store = grid.getStore();
		        var selections = grid.getSelections();
				var records = store.getRange();
				var index = 1;
				var disFlag = false;
				for(i=0;i<records.length;i++){
					var flag = true;
				        for(j=0;j<selections.length;j++){
					      if(records[i].get("XH")==selections[j].get('XH')){
							flag = false;
							break;
					      }
					}
					if(flag){
						disFlag = true;
						records[i].set('XH',index)
						index++;
					}
				}
		}}]
	});
	RightGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(),
		sm: this.sm,
	    margins:'2px 2px 2px 2px',
	    title:'自由入库',
		width:600,
		height:300,
		loadMask: true,
		id:'zyrkGrid',
		frame:true,
		region:'center',
		tbar:this.Toolbar,
		listeners:{
		   			afteredit :function(e){
					       var field = e.field;           //获取改变的列
					   	   var record = e.record;    //获取改变的行
					   	   //alert(wzTemp);
					       var result;
					       try{
					       	result = wzTemp;
					       	if(result==null){
					       		  
					       		//alert("物资库没该物资！");
					       		//return ;
					       	}
					       }catch(e){}
					       record.set("IS_CANCEL","1");
					     //  record.set("XGR", getEditor("DLM").getValue());
					      // if(getEditor("IS_UPDATE").getValue() == "1"){
					     //          record.set("IS_MOD","1");
					      // }
					       //grid.getView().getRow(object.row).style.background="yellow";
					       //grid.getView().getCell(object.row,object.column).style.background="red";
					       if(result){
					       		 if(result.xhgg!=null){
					       		 	record.set("XHGG",result.xhgg);
					             	record.set("GJDJ",result.jhdj);
					             	record.set("JLDW",result.jldw);
					             	record.set("WZBM",result.id);
					             	record.set("GCLB",result.gclb); 
					             	record.set("GCXM",result.gcxm);
					             	record.set("CKMC",result.kw.ck.ckmc);
					             	record.set("CWMC",result.kw.cwmc);
					             	record.set("SQBM",result.sqbm);
					             	record.set("ISNEW","0");
					             	record.set("SQSL","0");
					       		 }else{
					       		    for(var i = 0;i<result.length;i++){
					       		    	record.set("WZMC",result[i].json.wzmc);
					       		    	record.set("XHGG",result[i].json.xhgg);
					             		record.set("GJDJ",result[i].json.jhdj);
					             		record.set("JLDW",result[i].json.jldw);
					             		record.set("WZBM",result[i].json.id);
					             		record.set("GCLB",result[i].json.gclb); 
					             		record.set("GCXM",result[i].json.gcxm);
					             		record.set("CKMC",result[i].json.kw.ck.ckmc);
					             		record.set("CWMC",result[i].json.kw.cwmc);
					             		record.set("SQBM",result[i].json.sqbm);
					             		record.set("ISNEW","0");
					             		record.set("SQSL","0");	
					             		if(i<result.length-1){
					             			var rec = grid.createNewRecord();
											grid.createNew(rec);
					             			record = grid.getStore().getAt(0);
					             		}
					       		    }
					       		    var store = grid.getStore();
					                var records = store.getRange();
									for(i=0;i<records.length;i++){
										records[i].set('XH',i+1);
									}
					       		 }
					             wzTemp = null;
					       }else{
					             if(field == 'WZMC')
					                   record.set("ISNEW","1");
					        }
					        //grid.getStore().reload(); 
							   				 
		   			}
		   		}
	});	
}
Ext.extend(ZyRkGrid, Ext.grid.EditorGridPanel, {
	 //sm:new Ext.grid.RowSelectionModel({}),
	 sm:new Ext.grid.CheckboxSelectionModel(),
	
	/**
	 * 取得默认数据源
	 * 返回数据格式
	 * {success:true,totalCount:20,list:[{id:'123',field1:'111',...,field5'222'},{...},...]} 
	 */
	getDefaultDS:function(){
	    var grid = this;
		var url = contextPath + "/jteap/form/eform/EFormAction!eformRecGetSubDataAction.do";
		var ds = new Ext.data.Store({
	        proxy: new Ext.data.HttpProxy({
	            url: url
	        }),
	        baseParams :{
	        	subTableName:'TB_WZ_XQJHSQ_DETAIL',
	        	fkFieldName:'XQJHSQID' 
	        },
	        listeners:{
	        loadexception:function(a){
	        	alert('数据加载失败');
	        } 
	        },
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount'
	        }, [
			'XH','ISNEW','XQJHSQID','ID','WZBM','WZMC','XHGG','SQSL','JLDW','GJDJ',
			'PROVIDER','XYSJ','JHY','SFDH','IS_CANCEL','C_FLAG','XGR','REMARK','IS_MOD'
	        ]),
	        remoteSort: false 
	    });
		return ds;
	},
    createNewRecord:function(){
	    var fields = this.getStore().fields.items;
		var Rec = Ext.data.Record.create(fields);
		var r = {};
		for(var i = 0;i<fields.length;i++){
			eval("r."+fields[i].name+"=''");
		}
		var rec = new Rec(r);
	    return rec;
	},
	/**
	 * PersonAction 列模型
	 */
	getColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
	    this.sm,
{header:'序号',dataIndex:'XH',width:40,sortable:false,editor:new Ext.form.TextField({})},
{header:'是否新物资',dataIndex:'ISNEW',width:75,sortable:false,hidden:'true',renderer:function
(value,metadata,record,rowIndex,colIndex,store){
  if(value == '1'){
       return "<font color='red' style='font-weight:bold'>是</font>";
  }else{ 
       return "否";
  }
}},
{header:'需求计划申请ID',dataIndex:'XQJHSQID',width:90,hidden:'true',sortable:false,editor:new Ext.form.TextField({})},
{header:'ID',dataIndex:'ID',width:32,hidden:'true',sortable:false,editor:new Ext.form.TextField({})},
{header:'物资编码',dataIndex:'WZBM',width:80,hidden:'true',sortable:false,editor:new Ext.form.TextField({})},
{header:'物资名称',dataIndex:'WZMC',width:130,sortable:false,
 editor:new Ext.app.SelectBox({forceSelection:true,readOnly:true,
       onTriggerClick:function(){
          var field = this;
          var url = contextPath+"/jteap/wz/wzlb/selectWzdaIndex.jsp";
         // var result = showModule(url, true, 800, 500,'singleSelect|0');
          new $FW({
             url:url,id:'wzxz',type:'T1',
             resizable:true,menubar:false,scrollbars:true,toolbar:false}).show();
          
         /* if (result != null) {
              this.setValue(result[0].data.wzmc);
              this.obj=result[0];
              wzTemp = result;
         } else alert('没有选中物资！');*/
       },onfocus:function(){alert()},
       listeners:{select:function(combo,record,index ){
            wzTemp = record.json;
           
       }}

   })},

{header:'型号规格',dataIndex:'XHGG',width:130,sortable:false,editor:new Ext.form.TextField({})},
{header:'申请数量',dataIndex:'SQSL',width:60,sortable:false,align:'right',editor:new Ext.form.NumberField ({
		decimalPrecision:4,
		minValue:0,
		maxValue:999999.99,
		value:0,
		listeners:{focus:function(a){
		        this.selectText();
		    }
		}
	}),
renderer:function(value,metadata,record,rowIndex,colIndex,store){
    if(!value || value==''){
          value = 0;
    }
    return parseFloat(value).toFixed(2);
}
,align:'right'},
{header:'计量单位',dataIndex:'JLDW',width:60,sortable:false},
{header:'估计单价',dataIndex:'GJDJ',width:60,sortable:false,align:'right',
renderer:function(value,metadata,record,rowIndex,colIndex,store){
    if(!value || value==''){
          value = 0;
    }
    return parseFloat(value).toFixed(2);
}
,align:'right'},
{header:'金额',dataIndex:'PROVIDER',width:60,sortable:false,align:'right',editor:new Ext.form.NumberField ({
decimalPrecision:4,
readOnly:true,
minValue:0,
maxValue:999999.99,
value:0,
listeners:{focus:function(a){
        this.selectText();
    }
}
}),
renderer:function(value,metadata,record,rowIndex,colIndex,store){
    if(!value || value==''){
          value = 0;
    }
    return parseFloat(record.data["SQSL"]*record.data["GJDJ"]).toFixed(2);
}
,align:'right'},
{header:'需用时间',dataIndex:'XYSJ',width:120,sortable:false,editor:new Ext.form.DateField ({format:'Y-m-d H:i:s'})},
{header:'计划员',dataIndex:'JHY',width:8,hidden:'true',sortable:false,editor:new Ext.form.TextField({})},

/*******第15列*******/
{header:'SFDH',dataIndex:'SFDH',width:2,hidden:'true',sortable:false,editor:new Ext.form.TextField({})},

/*******第16列*******/
{header:'是否作废',dataIndex:'IS_CANCEL',width:60,sortable:false,
renderer:function(value,metadata,record,rowIndex,colIndex,store){
      return "未作废";
}
},

/*******第17列*******/
{header:'C_FLAG',dataIndex:'C_FLAG',width:1,hidden:'true',sortable:false,editor:new Ext.form.TextField({})},
{header:'CK',dataIndex:'CKMC',width:1,hidden:'true',sortable:false,editor:new Ext.form.TextField({})},
{header:'CW',dataIndex:'CWMC',width:1,hidden:'true',sortable:false,editor:new Ext.form.TextField({})},
/*******第18列*******/
{header:'修改人',dataIndex:'XGR',width:80,sortable:false,editor:new Ext.form.TextField({})},

/*******第19列*******/
{header:'备注',dataIndex:'REMARK',width:100,sortable:false,editor:new Ext.form.TextArea ({rows:'10',cols:'8'})},

/*******第20列*******/
{id:'IS_MOD',header:'是否修改',dataIndex:'IS_MOD',width:80,hidden:'true',sortable:false,editor:new Ext.form.TextField({})}
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
	} ,
	createNew:function(rec){
		if(rec!=null)
		this.store.insert(0, rec);
	}
});


