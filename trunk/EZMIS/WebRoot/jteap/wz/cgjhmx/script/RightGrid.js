
/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link4+"?limit=100&queryParamsSql=obj.zt='0' or obj.zt='1' or obj.zt='2' or obj.zt='3'");
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: 100,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:[
		'-',{text:'物资汇总',id:'wzhz',disabled:true,handler:function(){
			var selections = rightGrid.getSelections();//获取被选中的行
			//var rightGrid=this;
			var ids="";
			//var mxstore = mxGrid.getStore();
			Ext.each(selections,function(selectedobj){
				ids+=selectedobj.id+",";//取得他们的id并组装
			});			
			if(ids!=''){
				//var url = link8 + "?ids="+ids;
				//window.open(url);
				//Excel导出（创建一个表单以"POST"方式提交，避免ids多长）
				var url = link8;
				var f = document.createElement("form");
				f.name = "aaa";
				f.target="newWindow"; 
				f.method="post";
				document.body.appendChild(f);
				var i = document.createElement("input");
				i.type = "hidden";
				f.appendChild(i);
				i.value = ids;
				i.name = "ids";
				f.action = url;
				f.onsubmit=function(){
					window.open("about:blank","newWindow","");
				}
				f.submit();
			}
		}},
		'-',{text:'导出Excel',handler:function(){
		exportSelectedExcel(grid);
		}},'-','<font color="red">*双击记录查看采购明细</font>']
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
		var wzhz = rightGrid.pageToolbar.items.items[12];
		if(oCheckboxSModel.getSelections().length>0){
			wzhz.setDisabled(false);
		}else{
			wzhz.setDisabled(true);
		}
	});
	
	this.store.load({params: { start: 0 ,limit: 100 } });
	
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
		
		if(select){
			var url=link6+"&docid="+select.json.cgjhgl.id;
			result=showModule(url,true,800,645);
		}
	});
	
}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
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
			'id',
			'cgjhgl',
			'bz',
			'cgjhgl.id',
			'cgjhgl.bh',
			'cgjhgl.jhy',
			'cgjhgl.zdsj',
			'cgjhgl.sxsj',
			'cgjhgl.bz',
			'wzdagl',
			'wzdagl.wzmc',
			'xqjhsq',
			'sqbmmc',
			'xqjhsqbh',
			'czyxm',
			'xqjhsqDetail',
			'remark',
			'xh',
			'jhdj',
			'cgjldw',
			'cgsl',
			'hsxs',
			'jhdhrq',
			'dhsl',
			'cgfx',
			'cgy',
			'zt',
			'person',
			'person.id',
			'person.userName'
	        ]),
	        remoteSort: true
	    });
	    
	    //onload事件
		ds.on("load",function(store,records,options){
			for(var i = 0; i < store.getCount(); i++){
	             var record = store.getAt(i);
	             //采购计划明细被删除
	             if(record.data.zt == "4"){
	                  grid.getView().getRow(i).style.background="yellow";               
	             }else{
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
//				{id:'id',header: "ID", width: 100, sortable: true, dataIndex: 'id'},
				{id:'xh',header: "序号", width: 40, sortable: false, dataIndex: 'xh',
					renderer:function(value,metadata,record,rowIndex, colIndex, store)
			        {
			              return rowIndex + 1;   
			        }
				},
				{id:'cgjhbh',header: "采购计划编号", width: 80, sortable: true, dataIndex: 'cgjhgl.bh'},
				{id:'xqjhsqbh',header: "需求申请编号", width: 80, sortable: true, 
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(record.json.xqjhDetail.xqjh.xqjhsq){
							return record.json.xqjhDetail.xqjh.xqjhsq.xqjhsqbh;
						}else{
							return "";
						}
					}
				},
				{id:'zt',header: "状态", width: 100, sortable: true, dataIndex: 'zt',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
					 	//return $dictKey("CGJHMX",value);
					 	if(value=='0'){
							metadata.attr ="style=background:#FF3333";
							return '未生效';
						}else if(value =='1'){
							metadata.attr ="style=background:#00FF33";
							return '已生效';
						}else if(value =='2'){
							metadata.attr ="style=background:yellow";
							return "已到货";
						}else if(value =='3'){
							metadata.attr ="style=background:#8EDBF8";
							return "已到货(部分)";
						}
					}
				},
				{id:'wzbm',header: "物资名称规格", width: 200, sortable: true, dataIndex: 'wzdagl.wzmc',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						return value+"("+record.data.wzdagl.xhgg+")";
					}
				},
				/**
				{id:'zdsj',header: "分配时间", width: 100, sortable: true, dataIndex: 'cgjhgl.zdsj',
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    		 	if(value=='' || value==null){
		    		 		return value;
		    		 	}
						var dt = formatDate(new Date(value['time']),"yyyy-MM-dd"); 
						return dt;         
					 }
				},
				**/
				{id:'cgjldw',header: "计量单位", width: 60, sortable: true, dataIndex: 'cgjldw'},
				{id:'cgsl',header: "采购数量", width: 80, sortable: true, dataIndex: 'cgsl',align:'right',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				/**
				{id:'dhsl',header: "到货数量", width: 80, sortable: true, dataIndex: 'dhsl',align:'right',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(!value || value==''){
							value = 0;
						}
						return parseFloat(value).toFixed(2);
					}
				},
				**/
				{id:'jhy',header: "计划员", width: 60, sortable: true, dataIndex: 'cgjhgl.jhy',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
					 	return record.data.cgjhgl.person.userName;
					}
				},
				{id:'cgy',header: "采购员", width: 60, sortable: true, dataIndex: 'person.userName'},
				/**
				{id:'cgfx',header: "采购方向", width: 100, sortable: true, dataIndex: 'cgfx',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
					 	return $dictKey("CGFX",value);
					}
				},
				**/
				{id:'sxsj',header: "生效时间", width: 100, sortable: true, dataIndex: 'cgjhgl.sxsj',
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    		 	if(value=='' || value==null){
		    		 		return value;
		    		 	}
						var dt = formatDate(new Date(value['time']),"yyyy-MM-dd"); 
						return dt;         
					 }
				},
				/**
				{id:'jhdhrq',header: "计划到货日期", width: 100, sortable: true, dataIndex: 'jhdhrq',
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    		 	if(value=='' || value==null){
		    		 		return value;
		    		 	}
						var dt = formatDate(new Date(value['time']),"yyyy-MM-dd"); 
						return dt;         
					 }
				},
				**/
				{id:'xqjhsqbm',header: "申请部门", width: 100, sortable: true,
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    		 	 if(record.json.xqjhDetail.xqjh.xqjhsq){
		    		 	 	return record.json.xqjhDetail.xqjh.sqbm;
		    		 	 }else{
		    		 	 	return "";
		    		 	 } 
					}
				},
				{id:'xqjhsqr',header: "申请人", width: 60, sortable: true,
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    		 	 if(record.json.xqjhDetail.xqjh.xqjhsq){
		    		 	 	return record.json.xqjhDetail.xqjh.xqjhsq.czyxm;
		    		 	 }else{
		    		 	 	return "";
		    		 	 }
					}
				},
				{id:'xqjhsqxm',header: "工程项目", width: 100, sortable: true,
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    		 	 if(record.json.xqjhDetail.xqjh.xqjhsq){
		    		 	 	return record.json.xqjhDetail.xqjh.gcxm;
		    		 	 }else{
		    		 	 	return "";
		    		 	 }    
					}
				},
				{id:'xqjhsqlb',header: "工程类别", width: 100, sortable: true,
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    		 	 if(record.json.xqjhDetail.xqjh.xqjhsq){
		    		 	 	return record.json.xqjhDetail.xqjh.gclb;
		    		 	 }else{
		    		 	 	return "";
		    		 	 }   
					}
				},
				{id:'xqjhxqbz',header: "备注", width: 120, sortable: true, 
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    		  	if(record.json.xqjhDetail.xqjh.xqjhsq){
		    		 	 	var xqjhsqDetailJson = record.json.xqjhDetail.xqjh.xqjhsq.xqjhsqDetail;
		    		 	 	for(var i = 0; i < xqjhsqDetailJson.length; i++){
		    		 	 		if(record.data.wzdagl.wzmc == xqjhsqDetailJson[i].wzmc && record.data.wzdagl.xhgg == xqjhsqDetailJson[i].xhgg){
		    		 	 			return xqjhsqDetailJson[i].remark;
		    		 	 		}
		    		 	 	}
		    		 	 }else if(record.data.cgjhgl.bz != ""){
		    		 	 	return record.data.cgjhgl.bz;
		    		 	 }else{
		    		 	 	return "";
		    		 	 }
					 }
				},
				{id:'bz',header: "采购明细备注", width: 120, sortable: true, dataIndex: 'bz',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
					 	if(record.data.zt == '4'){
							metadata.attr ="style=background:#FF3333";
							return value;
						}else{
							metadata.attr ="style=background:#8EDBF8";
							return value;
						}
					}
				}
//				{id:'jhdj',header: "计划单价", width: 100, sortable: true, dataIndex: 'jhdj'},
//				{id:'cgjldw',header: "采购计量单位", width: 100, sortable: true, dataIndex: 'cgjldw'},
//				{id:'hsxs',header: "换算系数", width: 100, sortable: true, dataIndex: 'hsxs'}
				
				
				
				
				
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

