
var mainToolbar = null;
//左边的树
var leftTree=new LeftTree();
//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="物资名称#wzmc#textField,型号规格#xhgg#textField,仓库#ckgl#comboCkgl".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="物资名称#wzmc#textField,型号规格#xhgg#textField,仓库#ckgl#comboCkgl".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs,labelWidth:70,txtWidth:70});

//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
//	items:[searchPanel,rightPanel],
	items:[searchPanel,rightGrid],
	buttons:[{text:'确定',handler:function(){
		//window.close();
		//alert(window.opener.wzmcResult);
        //ssrcccccdadads
		//window.opener.wzTemp  = rightGrid.getSelectionModel().getSelections();
		var result = rightGrid.getSelectionModel().getSelections();
		//如果抛出异常 则是用showmodel方式打开页面 window.opener会取不到
		try{
			var cgmc = window.opener.wzmcGrid;
			var  record = cgmc.getStore().getAt(0);
			if(cgmc.title!="自由入库"&&cgmc.title!="自由领用"){
				if(record==undefined || record.get("WZMC")!=""){
					cgmc.editor.createNew();
		            record = cgmc.getStore().getAt(0);
				}
				for(var i = 0;i<result.length;i++){
		            record.set("IS_CANCEL","1");
		            record.set("XGR", window.opener.getEditor("DLM").getValue());
				    if(window.opener.getEditor("IS_UPDATE").getValue() == "1"){
		                   record.set("IS_MOD","1");
				    }
		       
		            record.set("WZMC",result[i].data.wzmc);
		            record.set("XHGG",result[i].data.xhgg);
					 record.set("GJDJ",result[i].data.pjj);
					 record.set("JLDW",result[i].data.jldw);
					 record.set("WZBM",result[i].data.id);
					 record.set("GCLB",result[i].data.gclb); 
					 record.set("GCXM",result[i].data.gcxm);
					 record.set("SQBM",result[i].data.sqbm);
					 record.set("ISNEW","0");
					 record.set("SQSL","0");
					 if(cgmc.title=="自由领用"){
             			record.set("DQKC",result[i].data.dqkc);
             		}
		             if(i<result.length-1){
		               cgmc.editor.createNew();
		               record = cgmc.getStore().getAt(0);
		             }
		          } 
			}else{
				if(record==undefined || record.get("WZMC")!=""){
					var rec = cgmc.createNewRecord();
					cgmc.createNew(rec);
           			record = cgmc.getStore().getAt(0);
				}
				 for(var i = 0;i<result.length;i++){
       		    	record.set("WZMC",result[i].json.wzmc);
       		    	record.set("XHGG",result[i].json.xhgg);
             		record.set("GJDJ",result[i].json.pjj);
             		record.set("JLDW",result[i].json.jldw);
             		record.set("WZBM",result[i].json.id);
             		record.set("GCLB",result[i].json.gclb); 
             		record.set("GCXM",result[i].json.gcxm);
             		record.set("CKMC",result[i].json.kw.ck.ckmc);
             		record.set("CWMC",result[i].json.kw.cwmc);
             		record.set("SQBM",result[i].json.sqbm);
             		record.set("ISNEW","0");
             		record.set("SQSL","0");	
             		if(cgmc.title=="自由领用"){
             			record.set("DQKC",result[i].json.dqkc);
             		}
             		if(i<result.length-1){
             			var rec = cgmc.createNewRecord();
						cgmc.createNew(rec);
             			record = cgmc.getStore().getAt(0);
             		}
       		    }
       		    var store = cgmc.getStore();
                var records = store.getRange();
				for(i=0;i<records.length;i++){
					records[i].set('XH',i+1);
				}
			
			}
			
         }catch(e){
            //如果出现此类异常 则不是grid取值调用
            if(e.message == "无法获取属性“wzmcGrid”的值: 对象为 null 或未定义"){
          		window.close();
             }else{
//            	window.close();
          		//alert(e.message);
            }
       }
		
	}}]
}

