
/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link4+"?limit=30");//+"?queryParamsSql=obj.zt='1'"
    defaultDs.load();
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: 30,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'Excel导出',handler:function(){
			//alert(parWhere);
			//exportExcels(grid,false,parWhere,null);
			var path = contextPath +"/jteap/wz/lydgl/LydmxAction!exportExcel.do?parWhere="+parWhere;
			window.open(path);
		}},'-',{text:'打印',handler:function(){
			var selections = rightGrid.getSelections();//获取被选中的行
			var select = rightGrid.getSelectionModel().selections;
			var idsArr = new Array();
			//var rightGrid=this;
			/*
			var ids="";
			//var mxstore = mxGrid.getStore();
			Ext.each(selections,function(selectedobj){
				ids+=selectedobj.id+",";//取得他们的id并组装
			});			
			*/
			//Excel导出（创建一个表单以"POST"方式提交，避免ids多长）
			var url = link7;
			var f = document.createElement("form");
			f.name = "aaa";
			f.target="newWindow"; 
			f.method="post";
			document.body.appendChild(f);
			var input = document.createElement("input");
			input.type = "hidden";
			if(select && select.length > 0){
				for(var i =0; i < select.length; i++){
					idsArr.push("'"+select.items[i].id+"'");
				}		
				//var url = link7 + "?id="+idsArr;
				//window.open(url);
				input.value = idsArr;
				input.name = "id";
			}else{
			 	input.value = parWhere;
				input.name = "parWhere";
			}
			f.appendChild(input);
			f.action = url;
			f.onsubmit=function(){
				window.open("about:blank","newWindow","");
			}
			f.submit();
		}},'-','<font color="red">*双击查看明细</font>',
		'-','<font color="yellow">黄色:表示自由领用物资</font>-<font color="white">白色:表示正常领用物资</font>']
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
	this.getView().on("refresh",function(){
			var store = grid.getStore();
		 	var jsonDatas = store.reader.jsonData;
		 	if(jsonDatas!=null){
				var mainTotal = jsonDatas.mainTotal;
				//添加总条数合计数据
				if(mainTotal.id!=null){
					store.add(new store.recordType({
			  			id:mainTotal.id,
			  			xh:"_",
			  			jhje:"_"+mainTotal.jhje,
			  			sjje:"_"+mainTotal.sjje
			  			})
			  		);
				} 
		 	}
		 	//刷新以后让纵向滚动条显示在第一行的位置
		 	grid.getView().focusRow(0);
	});
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		
		var btnDelCForm=mainToolbar.items.get('btnDel');
		var btnModifyCForm=mainToolbar.items.get('btnModify');
		
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
	/*
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
			var url=link6+"&docid="+select.json.lydgl.id;
			var sqbh = select.json.lydgl.bh;
			var param = 'sqbh|'+sqbh;
			result=showModule(url,true,800,645,param);
		}
	});*/
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=rightGrid.getSelectionModel().getSelections()[0];
		if(select){
		    if(select.json==null){
		    	return;
		    }
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
					var url = "";
					if(select.json.xhgg!=null){
						url=link14+"?wzmc="+encodeURIComponent(select.json.wzmc)+"&xhgg="+encodeURIComponent(select.json.xhgg);
					}else{
						url=link14+"?wzmc="+encodeURIComponent(select.json.wzmc);
					}
					
					new $FW({url:url,height:645,width:800,type:'T1',userIF:true,baseParam:{}}).show();	//模式对话框
		        
				}
		}
	});
}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
	//sm:new Ext.grid.RowSelectionModel({
	//	singleSelect:true
	//}),
	sm:new Ext.grid.CheckboxSelectionModel(),
	
	/**
	 * 取得默认数据源
	 * 返回数据格式
	 * {success:true,totalCount:20,list:[{id:'123',field1:'111',...,field5'222'},{...},...]} 
	 */
	getDefaultDS:function(url){
		grid = this;
	    var ds = new Ext.data.Store({
	        proxy: new Ext.data.ScriptTagProxy({
	            url: url
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount',
	            id: 'id'
	        }, [
				"id", "lysj", "lydqf", "gclb", "gcxm",
				"lybm", "lysqbh", "llr","czr","sqsj","bh","wzbm","sjdj",
				"xhgg", "wzmc", "xh","jldw", "jhdj", "dqkc","zt", "ckmc","sjlysl","pzlysl","xqjhmx"
				]),
	        remoteSort: true
	    });
	    //onload事件
		ds.on("load",function(store,records,options){
			for(var i = 0; i < store.getCount(); i++){
	             var record = store.getAt(i);
				//如果工程项目为空 则是自由领用
	             if(record.data.xqjhmx){
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
		    	{id:'xh',header: "序号", width: 40, sortable: false, 
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
					 	if(value!=undefined){
		    				return rowIndex + 1;
		    			}else{
		    				return "";
		    			}
					}
				},
		    	{id:'bh',header: "领用单编号", width: 80, sortable: true, dataIndex: 'bh'},
		    	{id:'zt',header: "领用单状态", width: 100, sortable: false, dataIndex: 'zt',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
					 	if(value=='0'){
					 		metadata.attr ="style=background:#FF3333";
					 		return '未生效';
					 	}else if(value =='1'){
					 		metadata.attr ="style=background:#00FF33";
					 		return '已出库';
					 	}
					}
				},
		    	{id:'lydqf',header: "领用单区分", width: 80, sortable: false, dataIndex: 'lydqf',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value!=null){
					 		return $dictKey("LYDQF",value);
					 	}
					}
				},
				{id:'wzmc',header: "物资名称", width: 250, sortable: false, dataIndex: 'wzmc',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value!=null){
							if(record.get('xhgg')!=null){
								return value+"("+record.get('xhgg')+")";
							}else{
								return value+"()";							
							}
						}
					}
				},
				{id:'ckmc',header: "仓库名称", width: 70, sortable: false, dataIndex: 'ckmc',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
					 	if(record.json!=undefined){
					 		return value;
					 	}else{
					 		return "<font color = 'red'>合计：</font>";
					 	}
					}
				},
				{id:'jldw',header: "计量单位", width: 70, sortable: true, dataIndex: 'jldw'},
				/**
				{id:'jhdj',header: "计划单价", width: 70, sortable: true, dataIndex: 'jhdj',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value!=null){
							return parseFloat(value).toFixed(2);
						}
					}
				},**/
				{id:'sjdj',header: "实际单价", width: 70, sortable: false, dataIndex: 'sjdj',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value!=null){
							return parseFloat(value).toFixed(2);
						}
					}
				},
				{id:'sjlysl',header: "<span style='color:red'>实际领用数量</span>", width: 80, sortable: true, dataIndex: 'sjlysl',align:'right',editor:new Ext.form.NumberField({
					xtype:'numberfield',
					decimalPrecision:2,
					minValue:0,
					maxValue:999999.99,
					listeners:{focus:function(a){
					        this.selectText();
					    },blur : function( f ){
					    }
					}
					}),
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value == '' || value == null){
							record.set('sjlysl',value);
							value = record.get('pzlysl');
							//record.commit();
						}
						if(value>record.get('pzlysl')){
							alert('领用数量不能大于批准数量');
							record.set('sjlysl',value);
							value = record.get('pzlysl');
							record.commit();
						}else if(value<record.get('pzlysl')){
						
						}
						if(value !=null){
							return "<span style='color:red;'>"+parseFloat(value).toFixed(2)+"</span>";
						}
					}
				},/**
				{id:'jhje',header: "计划金额", width: 70, sortable: true, 
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(value==undefined){
				    		return "<font color='red'>"+parseFloat(record.data.jhje.split("_")[1]).toFixed(2)+"</font>";
				    	}else{
							var jhdj = record.get('jhdj');
				    		var lysl = (record.get('sjlysl')==0)?record.get('pzlysl'):record.get('sjlysl');    
				    		return parseFloat(jhdj*lysl).toFixed(2);				    	
				    	}
					}
				},**/{id:'sjje',header: "实际金额", width: 70, sortable: false, 
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(value==undefined){
				    		return "<font color='red'>"+parseFloat(record.data.sjje.split("_")[1]).toFixed(2)+"</font>";
				    	}else{
							var sjdj = record.get('sjdj');
				    		var lysl = (record.get('sjlysl')==0)?record.get('pzlysl'):record.get('sjlysl');    
				    		return parseFloat(sjdj*lysl).toFixed(2);				    	
				    	}
					}
				},{id:'sqsj',header: "领用申请时间", width: 100, sortable: false,dataInedx:'sqsj',
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(typeof(record.data.sqsj) == 'undefined'){
		    				return "";
		    			}else{
			    			//return formatDate(new Date(record.data.sqsj['time']),"yyyy-MM-dd");
			    			return record.data.sqsj; 
		    			}     
					}
				},
				{id:'lysj',header: "领用时间", width: 100, sortable: false, dataIndex: 'lysj',
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	//if(value==null) return value;
						//var dt = formatDate(new Date(value['time']),"yyyy-MM-dd"); 
						return value;         
					}
				},
				{id:'czr',header: "操作人", width: 100, sortable: false, dataIndex: 'czr'},
				{id:'llr',header: "领料人", width: 100, sortable: false, dataIndex: 'llr'},
				{id:'gclb',header: "工程类别", width: 100, sortable: true, dataIndex: 'gclb'},
				{id:'gcxm',header: "工程项目", width: 100, sortable: true, dataIndex: 'gcxm'},
				{id:'lybm',header: "领用部门", width: 100, sortable: true, dataIndex: 'lybm'}
				
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

