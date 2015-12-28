
/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link14+"?limit=30&queryParamsSql=obj.zt='1' or obj.zt='2'");
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: 30,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:[
		'-',{text:'Excel导出',handler:function(){
			//alert(parWhere);
			//exportExcels(grid,false,parWhere,null);
			var path = contextPath +"/jteap/wz/yhdmx/YhdmxsAction!exportExcel.do?parWhere="+parWhere;
			window.open(path);
		}},
		'-',{text:'打印',handler:function(){
			var selections = rightGrid.getSelections();//获取被选中的行
			var select = rightGrid.getSelectionModel().selections;
			var idsArr = new Array();
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
		}},
		'-',{text:'导出卡片',handler:function(){
			var selections = rightGrid.getSelections();//获取被选中的行
			var select = rightGrid.getSelectionModel().selections;
			var idsArr = new Array();
			var bzsArr = new Array();
			//Excel导出（创建一个表单以"POST"方式提交，避免ids多长）
			var url = link71;
			var f = document.createElement("form");
			f.name = "aaa";
			f.target="newWindow"; 
			f.method="post";
			document.body.appendChild(f);
			var input = document.createElement("input");
			var inputbz = document.createElement("input");
			input.type = "hidden";
			inputbz.type = "hidden";
			if(select && select.length > 0){
				for(var i =0; i < select.length; i++){
					if(select.items[i].json!=undefined){
						with(select.items[i].json){
							idsArr.push("'"+wzid+"'");
							if(bz==null){
								bz = " ";
							}
							bzsArr.push(wzid+":"+bz);
						}
					}
				}
				inputbz.value = bzsArr;
				inputbz.name="bz";
				
				input.value = idsArr;
				input.name = "id";
			}else{
			 	input.value = parWhere;
				input.name = "parWhere";	
			}
			f.appendChild(input);
			f.appendChild(inputbz);
			f.action = url;
			f.onsubmit=function(){
				window.open("about:blank","newWindow","");
			}
			f.submit();
		}}
		,'-','<font color="red">*双击查看明细</font>',
		'-','<font color="yellow">黄色:表示自由入库物资</font>-<font color="white">白色:表示正常入库物资</font>']
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
		 		var pageTotal = jsonDatas.pageTotal;
				var mainTotal = jsonDatas.mainTotal;
				
				//添加到当前页合计数据
	  
				if(pageTotal.last==null&&pageTotal.id!=null){
			  		store.add(new store.recordType({
			  			id:pageTotal.id,
			  			bh:pageTotal.xh,
			  			xh:"_"+mainTotal.xh,
			  			yssl:"false",
//			  			gcxm:"_false",
			  			sqdj:"_"+pageTotal.sqdj,
			  			jhje:"_"+pageTotal.jhje,
			  			sjhj:"_"+pageTotal.sjhj,
			  			se:"_"+pageTotal.sehj,
			  			sqhj:"_"+pageTotal.sqhj 
			  			})
			  		);
				} 
				//添加总条数合计数据
				if(mainTotal.id!=null){
					store.add(new store.recordType({
			  			id:mainTotal.id,
			  			bh:mainTotal.xh,
			  			xh:"_"+mainTotal.xh,
			  			yssl:"false",
//			  			gcxm:"_false",
			  			sqdj:"_"+mainTotal.sqdj,
			  			jhje:"_"+mainTotal.jhje,
			  			sjhj:"_"+mainTotal.sjhj,
			  			se:"_"+mainTotal.sehj,
			  			sqhj:"_"+mainTotal.sqhj 
			  			})
			  		);
				} 
		 	}
		 	//刷新以后让纵向滚动条显示在第一行的位置
		 	grid.getView().focusRow(0);
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
		var select=rightGrid.getSelectionModel().getSelections()[0];
		if(select){
		    if(select.json==null){
		    	return;
		    }
			if(select.json.yhdmxs!=null){
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
						var yhdmxs = select.json.yhdmxs;
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
						/*
						ids = ids.substring(0,ids.length-1);
						var url = contextPath+"/jteap/wz/cgjhmx/CgjhmxAction!showListAction.do?";
						if(responseObject != null){
							var url=link6+"&docid="+select.json.yhdgl.id;
							new $FW({url:url,height:645,width:800,type:'T1',userIF:true,baseParam:{responseObject:responseObject}}).show();	//模式对话框
						}else{
							AjaxRequest_Sync(url,{queryParamsSql:"obj.id in ("+ids+")"},function(obj){
								var strReturn=obj.responseText;
								responseObject=Ext.util.JSON.decode(strReturn);
								var url=link6+"&docid="+select.json.yhdgl.id;
								new $FW({url:url,height:645,width:800,type:'T1',userIF:true,baseParam:{responseObject:responseObject}}).show();	//模式对话框
							//result=showModule(url,true,800,645);
				        	});
						}
						*/
						var xhgg = "";
						if(select.json.xhgg!=null){
							xhgg = select.json.xhgg;
						}
						var url=link6+"?wzmc="+encodeURIComponent(select.json.wzmc)+"&xhgg="+encodeURIComponent(xhgg);
						new $FW({url:url,height:645,width:800,type:'T1',userIF:true,baseParam:{}}).show();	//模式对话框
			        
					}
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
	        "sjhj",
	        "jhje",
			"gclb",
			"gcxm",
			"sqbm",
			'ghdw','xh','sl','zf','wzmc','xhgg','wzid',
			"cwmc",
			"ckmc",'yhdmxs',"sqhj",
			'cuserName','cid','cuserLoginName',
			'buserName','bid','buserLoginName','zt',"flag",
			'yhdid','bh','bz','ysrq','htbh','ghdw','tssl','fpbh','dhsl','cgjldw','se','sqdj','yssl','id','jhdj','hsxs','rksj','dhsj'
	        ]),
	        remoteSort: true
	    });
		
		//onload事件
		ds.on("load",function(store,records,options){
			for(var i = 0; i < store.getCount(); i++){
	             var record = store.getAt(i);
	             //如果工程项目为空 则是自由入库
	             if(record.data.gcxm != ""&&record.data.gcxm != null){
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
		    	{header:'序号',width: 60, dataIndex: 'xh',renderer:function(value, cellmeta, record, rowIndex){
		    			if(value.indexOf('_')==-1){
		    				return rowIndex + 1;
		    			}else{
		    				return "";
		    			}
			        	
			    }},
				{id:'zt',header: "入库状态", width: 60, sortable: false, dataIndex: 'zt',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value=='0'){
							return '未生效';
						}else if(value =='2'){
							metadata.attr ="style=background:#FF3333";
							return '未入库';
						}else if(value =='1'){
							metadata.attr ="style=background:#00FF33";
							return '已入库';
						}
					}
				},
				{id:'zt',header: "出库状态", width: 80, sortable: false, dataIndex: 'flag',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value==0){
							metadata.attr ="style=background:#FFD700";
							return '自由物资';
						}else if(value ==1){
							metadata.attr ="style=background:#f84329";
							return '未出库';
						}else if(value == 2){
							metadata.attr ="style=background:#00FF33";
							return '已出库';
						}else if(value == 3){
							metadata.attr ="style=background:#88EAFC";
							return '部分出库';
						}
					}
				},
		    	{id:'yhdbh',header: "验货单编号", width: 100, sortable: false, dataIndex: 'bh'},
		    	{id:'wzbm',header: "物资名称型号规格", width: 200, sortable: false, dataIndex: 'wzmc',
		    		renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
		    			if(value!=null){
		    				if(record.get('xhgg')!=null){
		    					return value + "("+record.get('xhgg')+")";
		    				}else{
		    					return value + "()";
		    				}
		    				
		    			}
		        		
		        	}
		    	},
		    	{id:'wzdagl.kw.cwmc',header: "库位", width: 100, sortable: false, dataIndex: 'cwmc'},
		    	{id:'wzdagl.kw.ck.ckmc',header: "仓库", width: 100, sortable: false, dataIndex: 'ckmc'},
		    	{id:'cuserName',header: "计划员", width: 100, sortable: false, dataIndex: 'cuserName'},
		    	{id:'yssl',header: "验收数量", width: 60, sortable: false, dataIndex: 'yssl',align:'right',
		    		renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    			if(value!="false"){
		    				return parseFloat(value).toFixed(2);
		    			}else{
							return "";		    			
		    			}
					}
				},
		    	//{id:'hsxs',header: "换算系数", width: 60, sortable: true, dataIndex: 'hsxs'},
		    	{id:'cgjldw',header: "计量单位", width: 60, sortable: false, dataIndex: 'cgjldw'},
		    	/**
		    	{id:'jhdj',header: "计划单价", width: 60, sortable: true, dataIndex: 'jhdj',align:'right',
		    		renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    			if(value!=null){
		    				return parseFloat(value).toFixed(2);
		    			}
					}
		    	},
		    	{id:'jhje',header: "计划金额", width: 80, sortable: true, dataIndex: 'jhje',align:'right',
		    		renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    			/*
						var sl = record.get('yssl');
						var xs =record.get('hsxs');
						var dj = record.get('jhdj');
						
						return (sl*xs*dj).toFixed(2);
						
						if(value!=null){
							if(typeof(value)=='number'){
								return parseFloat(value).toFixed(2);
							}else{
								return "<font color='red'>"+parseFloat(value.split("_")[1]).toFixed(2)+"</font>";						
							}
						}else{
							return '0.00';
						}
		    			
					}
		    	},**/
		    	{id:'sqdj',header: "税前单价", width: 100, sortable: false, dataIndex: 'sqdj',align:'right',
		    		renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    			if(value!=null){
							if(typeof(value)=='number'){
								return parseFloat(value).toFixed(2);
							}else{
								return "<font color='red'>"+parseFloat(value.split("_")[1]).toFixed(2)+"</font>";						
							}
						}else{
							return '0.00';
						}
						
					}
		    	},
		    	{id:'sl',header: "税率", width: 50, sortable: false, dataIndex: 'sl',align:'right',
		    		renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    			if(value!=null){
		    				return parseFloat(value).toFixed(2);
		    			} 
					}
				},{id:'sqdj',header: "税前合计", width: 100, sortable: false, dataIndex: 'sqhj',align:'right',
		    		renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    			if(value!=null){
							if(typeof(value)=='number'){
								return parseFloat(value).toFixed(2);
							}else{
								return "<font color='red'>"+parseFloat(value.split("_")[1]).toFixed(2)+"</font>";						
							}
						}else{
							return '0.00';
						}
						
					}
		    	},{id:'se',header: "税额", width: 100, sortable: false, dataIndex: 'se',align:'right',
		    		renderer:function(value,metadata,record,rowIndex,colIndex,store){
		    			if(value!=null){
							if(typeof(value)=='number'){
								return parseFloat(value).toFixed(2);
							}else{
								return "<font color='red'>"+parseFloat(value.split("_")[1]).toFixed(2)+"</font>";						
							}
						}else{
							return '0.00';
						}
						
					}
		    	},
		    	{id:'sjhj',header: "税价合计", width: 100, sortable: false, dataIndex: 'sjhj',align:'right',
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
						if(value!=null){
							if(typeof(value)=='number'){
								return parseFloat(value).toFixed(2);
							}else{
								return "<font color='red'>"+parseFloat(value.split("_")[1]).toFixed(2)+"</font>";						
							}
						}else{
							return '0.00';
						}
		    			
					}
		    	},
		    	{id:'rksj',header: "入库时间", width: 100, sortable: false, dataIndex: 'rksj',
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
		    	},{id:'dhsj',header: "到货时间", width: 100, sortable: false, dataIndex: 'dhsj',
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
		    	{id:'bgy',header: "保管员", width: 60, sortable: false, dataIndex: 'buserName'},
				//{id:'cgjhmx.xqjhDetail.xqjh.gclb',header: "工程类别", width: 100, sortable: true, dataIndex: 'gclb'},
				{id:'cgjhmx.xqjhDetail.xqjh.gcxm',header: "工程项目", width: 100, sortable: true, dataIndex: 'gcxm'},
				//{id:'cgjhmx.xqjhDetail.xqjh.sqbm',header: "班组", width: 100, sortable: true, dataIndex: 'sqbm'},
				{id:'bz',header: "班组", width: 80, sortable: false, dataIndex: 'bz'},
		    	{id:'fpbh',header: "发票编号", width: 80, sortable: false, dataIndex: 'fpbh'},
//		    	{id:'htbh',header: "合同编号", width: 80, sortable: false, dataIndex: 'htbh'},
				{id:'sghdw',header: "供应单位", width: 240, sortable: true, dataIndex: 'ghdw'},
				{id:'wzid',header: "物资编码", width: 80,hidden:true, sortable: false, dataIndex: 'wzid'}

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

