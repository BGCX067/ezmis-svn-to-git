var ckgl = "";
var parWhere = "";
var SearchPanel = function(config){
	this.clearSearch = function(){
		var oPanel=searchtemp.items.get(0);
		var oItems = oPanel.items.items;
		Ext.each(oItems,function(oItem){
	 		if(oItem.hidden==false){
	 			var temp = oItem.items.items[0];
	 			//var tempValue = temp.getValue();
	 			//值不为空才作为参数
	 			if(temp){
		 			temp.setValue('');
	 			}
	 		}
	 	});
	}
	//点击查询后的事件
	this.searchClick=function(){
		var oPanel=searchtemp.items.get(0);
		var zdStart=null;
		var sxStart=null;
		var oItems = oPanel.items.items;
		var queryParamsJson = {};
		var queryParamsSql="";
		var flag = false;
		var xhgg = "";
	 	Ext.each(oItems,function(oItem){
	 		
	 		if(oItem.hidden==false){
	 			var temp = oItem.items.items[0];
	 			var tempValue = temp.getValue();
	 			//值不为空才作为参数
	 			if(tempValue!=null&&tempValue!=""){
		 			//日期字段
		 			if(temp.triggerClass=="x-form-date-trigger"){
		 				tempValue = formatDate(tempValue,"yyyy-MM-dd");
		 				if(temp.id.split("#")[1]=="lyStart"){
		 					sxStart = tempValue;
		 					queryParamsSql+="to_char(obj.lysj,'yyyy-mm-dd')>='"+tempValue+"' and ";
		 				}else if(temp.id.split("#")[1]=="lyEnd"){
		 					queryParamsSql+="to_char(obj.lysj,'yyyy-mm-dd')<='"+tempValue+"' and ";
		 					if(sxStart!=null){
		 						if(sxStart>tempValue){
		 							alert("开始日期不能大于结束");
		 							flag=true;
		 							sxStart=null;
		 							return false;
		 						}else{
		 							sxStart=null;
		 						}
		 					}
		 				}
		 			}
//		 			else if(temp.id.split("#")[1]=="wzdagl.wzmc"){
//		 				queryParamsSql+="obj."+temp.id.split("#")[1]+" like  '$"+encodeURIComponent(tempValue)+"$' and ";
//		 			}
					else if(temp.id.split("#")[1]=="bh"){
	 					queryParamsSql+="lydgl.bh like '$"+encodeURIComponent(tempValue)+"$' and ";
	 				}else if(temp.id.split("#")[1]=="czr"){
	 					queryParamsSql+="lydgl.czr like '$"+encodeURIComponent(tempValue)+"$' and ";
	 				}else if(temp.id.split("#")[1]=="llr"){
	 					queryParamsSql+="lydgl.llr like '$"+encodeURIComponent(tempValue)+"$' and ";
	 				}else if(temp.id.split("#")[1]=="gclb"){
	 					queryParamsSql+="obj.gclb like '$"+encodeURIComponent(tempValue)+"$' and ";
	 				}else if(temp.id.split("#")[1]=="gcxm"){
	 					queryParamsSql+="obj.gcxm like '$"+encodeURIComponent(tempValue)+"$' and ";
	 				}else if(temp.id.split("#")[1]=="lybm"){
	 					queryParamsSql+="lydgl.lybm like '$"+encodeURIComponent(tempValue)+"$' and ";
	 				}else if(temp.id.split("#")[1]=="ckgl"){
		 				ckgl = tempValue;
		 			}else if(temp.id.split("#")[1]=="wzdagl.wzmc"){
		 				//var wzmc = tempValue.split('(')[0];
		 				//xhgg = tempValue.split('(')[1].substr(0,tempValue.split('(')[1].length-1);
		 				queryParamsSql+="obj.wzbm.wzmc like '%25"+encodeURIComponent(tempValue)+"%25' and "; 
		 				//if(xhgg!=""&&xhgg!="null"){
		 				//	queryParamsSql+=" obj.wzbm.xhgg like '%25"+encodeURIComponent(xhgg)+"%25' and ";
		 				//}
		 			}else if(temp.id.split("#")[1]=="wzdagl.xhgg"){
		 				if(xhgg==""){
		 					queryParamsSql+=" obj.wzbm.xhgg like '%25"+encodeURIComponent(tempValue)+"%25' and ";
		 				}
		 			}else{
		 				//其他字段模糊查询
		 				if(temp.id){
		 					queryParamsSql+="obj."+temp.id.split("#")[1]+" like '%"+encodeURIComponent(tempValue)+"%' and ";
		 				}
		 			}
	 			}
	 		};
	 	});
	 	if(flag){
	 		return;
	 	}
	 	//queryParamsSql+="obj.zt='1' and ";
	 	var url = link4+"?limit=100&queryParamsSql="+queryParamsSql.substring(0,queryParamsSql.length-5);
	 	parWhere = queryParamsSql.substring(0,queryParamsSql.length-5);
	 	if(ckgl != ""){
	 		url += "&ckgl="+ckgl;
	 		if(parWhere!=""){
	 			parWhere = parWhere+" and obj.wzbm.kw.ckid = '"+ckgl+"'";
	 		}else{
				parWhere = " obj.wzbm.kw.ckid = '"+ckgl+"'";	 		
	 		}
	 	}
		rightGrid.changeToListDS(url)
 		rightGrid.getStore().reload();	 	
	};
	
	//清空查询条件
	this.clearSearch=function(){
		var oPanel=searchtemp.items.get(0);
		var oItems = oPanel.items.items;
	 	Ext.each(oItems,function(oItem){
	 		if(oItem.hidden==false){
	 			var temp = oItem.items.items[0];
	 			temp.reset();
	 			//日期字段
	 			if(temp.triggerClass=="x-form-date-trigger"){
					temp.setValue('');	 				
	 			}
	 		};
	 	});
	 	ckgl = "";
 		var url = link4+"?limit=100";
 		rightGrid.changeToListDS(url);
 		rightGrid.getStore().reload();
	};
	SearchPanel.superclass.constructor.call(this,config);
}
Ext.extend(SearchPanel, Ext.app.SearchPanel, {
		appendSearchField : function(searchField, bHide) {
			var sLabel = searchField;
			var sId = "sf#" + searchField;
			var sType = searchField;
			if (searchField.indexOf("#") >= 0) {
				var tmp = searchField.split("#");
				sLabel = tmp[0];
				sId = 'sf#' + tmp[1];
				sType = tmp[2]
			}
			var oPanel = this.items.get(0);
			var oSearchField = null;
		if (sType == "textField") { // 文本类型
			oSearchField = new Ext.form.TextField( {
				id : sId,
				fieldLabel : sLabel,
				anchor : '90%'
			});
		}else if(sType == "depts"){
			oSearchField = new Ext.app.ComboTree( {
				id: sId,
				fieldLabel : sLabel,
				dataUrl : link13,
				listWidth:140,
				width:140,
				blankText:'',
				emptyText:'',
				triggerClass:'comboTree',
				anchor : '90%',
				autoScroll:true,
				localData : false,
				rootVisible : false,
				allowBlank: false,
				listeners : {
					select : function(t, node) {
						this.hiddenName = node.id;
						searchPanel.flbm = node.attributes.flbm;
					}
				}
			})
		}else if(sType == "persons"){
			oSearchField = new Ext.app.TriggerField({
				id : sId,
				fieldLabel : sLabel,
				readOnly:false,
				blankText : '', 
				emptyText: '', 
				onTriggerClick:function(){
					var field = this;
					var url = contextPath + "/jteap/wz/dbrk/personSelect.jsp" ;
					var returnValue = showModule(url, true, 800, 600);
					if (returnValue != null) {
						var arrays = Ext.decode(returnValue) ;
						if(arrays==null || arrays.length<1 ) {
							this.reset();
							Ext.getCmp(sId).reset();
						}else{
							for(var i=0 ; i < arrays.length ; i++) {
								var username = arrays[i].name;
								var loginName = arrays[i].loginName;
								
								//edr.setValue(username+"|"+loginName );
								//this.setValue(username);
								Ext.getCmp(sId).setValue(username);
							}
						}
					} else this.reset();
				},
				anchor : '90%'
			});
		} else if(sType == "wzdagl"){
			oSearchField = new Ext.app.SelectBox({
				id : sId,
				fieldLabel : sLabel,
				forceSelection:false,
				onTriggerClick:function(){
					var field = this;
					var url = contextPath+"/jteap/wz/wzlb/selectWzdaIndex.jsp";
					var result = showModule(url, true, 850, 500,'singleSelect|1');
					//ssssss
					if (result != null) {
						//this.displayValue=result.wzmc;
          				//this.setValue(result.id);
						//alert(this.id);
						var xhgg = result.xhgg.Trim()==""?"":result.xhgg.Trim();
						//alert(result.xhgg.Trim());
						//ssddddddddddd
						this.setValue(result.wzmc.Trim()+"("+xhgg+")");
					} else this.reset();
				},
				anchor : '90%'
			});
		}else if (sType == "comboBgy") {
			oSearchField = new Ext.form.ComboBox({  
				id : sId,
				fieldLabel : sLabel,
				xtype:'combo', 
				store:new Ext.data.Store({
					baseParams:{PAGE_FLAG:'PAGE_FLAG_NO'},
					autoLoad : true, 
					proxy: new Ext.data.ScriptTagProxy({url : link12}),
					reader: new Ext.data.JsonReader({root: 'list'},
							['userLoginName','userName'])
				}),  
                 valueField:'userName',  
                 displayField:'userName',  
                 mode:'local',  
                 blankText : '', 
                 emptyText: '',            
//               hiddenName:'userName', 
                 editable:false,
//         		 editable:true,selectOnFocus:true,
                 forceSelection:true,  
                 triggerAction:'all',  
	   			 triggerClass:'comboJhy',
                 anchor:'90%' 
			});
		} else if (sType == "dateField") { // 日期类型
			oSearchField = new Ext.form.DateField( {
				id : sId,
				fieldLabel : sLabel,
				format : 'Y-m-d',
				anchor : '90%'
			});
		} else if (sType == "dateFieldShowTime") {
			oSearchField = new Ext.form.DateField( {
				fieldLabel : sLabel,
				format : 'Y-m-d H:i',
				anchor : '90%',
				id : sId,
				menu : new DatetimeMenu()
			});
		} else if(sType=="comboZt"){
			var localstore = new Ext.data.SimpleStore({
                    fields: ['value', 'key'],
                    data : [['0','未生效'],['1','已出库']]
                });

			oSearchField = new Ext.form.ComboBox({  
				id : sId,
				fieldLabel : sLabel,
				xtype : 'combo',
				store : localstore,
				valueField : "value",
				displayField : "key",
				mode : 'local',
				triggerAction: 'all', 
				forceSelection : true,
				blankText : '',
				emptyText : '',
		//		hiddenName : '',
				editable : false,
		//		name : '',
				anchor : '90%'
			});
		} else if(sType == "onlickTextField"){
			oSearchField = new Ext.form.TriggerField( {
				id : sId,
				fieldLabel : sLabel,
				anchor : '90%',
				onTriggerClick:function(){
						var obj = {};
						var url = contextPath+"/jteap/wz/gcxmgl/projSelect.jsp";
						var result = showIFModule(url,"工程项目选择","true",300,550,obj);
						var resultArray = new Array();
						if(typeof(result) == "undefined"){
						      Ext.getCmp(sId).setValue("");
						      Ext.getCmp("sf#gclb").setValue("");
						}else{
						      resultArray = result.split("|");
						      Ext.getCmp(sId).setValue(resultArray[0]);
						      Ext.getCmp("sf#gclb").setValue(resultArray[1]);
						}		
				}
			});
		} else if (sType == "comboJhy") {
			oSearchField = new Ext.form.ComboBox({  
				id : sId,
				fieldLabel : sLabel,
				xtype:'combo', 
				store:new Ext.data.Store({
					baseParams:{PAGE_FLAG:'PAGE_FLAG_NO'},
					autoLoad : true, 
					proxy: new Ext.data.ScriptTagProxy({url : link2}),
					reader: new Ext.data.JsonReader({root: 'list'},
							['userLoginName','userName'])
				}),  
                 valueField:'userLoginName',  
                 displayField:'userName',  
                 mode:'local',  
                 blankText : '', 
                 emptyText:'',            
//               hiddenName:'userName', 
                 editable:false,
//         		 editable:true,selectOnFocus:true,
                 forceSelection:true,  
                 triggerAction:'all',  
	   			 triggerClass:'comboJhy',
                 anchor:'90%' 
			});
		} else if (sType == "comboCgy") {
			oSearchField = new Ext.form.ComboBox({  
				id : sId,
				fieldLabel : sLabel,
				xtype:'combo', 
				store:new Ext.data.Store({
					baseParams:{PAGE_FLAG:'PAGE_FLAG_NO'},
					autoLoad : true, 
					proxy: new Ext.data.ScriptTagProxy({url : link3}),
					reader: new Ext.data.JsonReader({root: 'list'},
							['userLoginName','userName'])
				}),  
                 valueField:'userLoginName',  
                 displayField:'userName',  
                 mode:'local',  
                 blankText : '', 
                 emptyText:'',            
//                 hiddenName:'userName', 
                 editable:false,
//         		 editable:true,selectOnFocus:true,
                 forceSelection:true,  
                 triggerAction:'all',  
	   			 triggerClass:'comboCgy',
                 anchor:'90%' 
			});
		} else if(sType=="comboZt"){
			var cgjhmxList=$dictList("CGJHMX");   
			var comboStatusStore = new Ext.data.Store({
				data: {rows:cgjhmxList},
				reader : new Ext.data.JsonReader( {
					root : 'rows',
					id : 'id'
					}, ['key', 'value', 'id'])
			});
		
			oSearchField = new Ext.form.ComboBox({  
				id : sId,
				fieldLabel : sLabel,
				xtype : 'combo',
				store : comboStatusStore,
				valueField : "value",
				displayField : "key",
				mode : 'local',
				triggerAction: 'all', 
				forceSelection : true,
				blankText : '',
				emptyText : '',
		//		hiddenName : '',
				editable : false,
		//		name : '',
				anchor : '90%'
			});
		}else if (sType == "comboCkgl") {
			oSearchField = new Ext.form.ComboBox({  
				id : sId,
				fieldLabel : sLabel,
				xtype:'combo', 
				store:new Ext.data.Store({
					baseParams:{PAGE_FLAG:'PAGE_FLAG_NO'},
					autoLoad : true, 
					proxy: new Ext.data.ScriptTagProxy({url : link10}),
					reader: new Ext.data.JsonReader({root: 'list'},
							['ckmc','id'])
				}),  
                 valueField:'id',  
                 displayField:'ckmc',  
                 mode:'local',  
                 blankText : '', 
                 emptyText:'',            
//                 hiddenName:'userName', 
                 editable:false,
//         		 editable:true,selectOnFocus:true,
                 forceSelection:true,  
                 triggerAction:'all',  
	   			 triggerClass:'comboCkgl',
                 anchor:'90%' 
			});
		} else if(sType=="comboCgfx"){
			var cgjhmxList=$dictList("CGFX");   
			var comboStatusStore = new Ext.data.Store({
				data: {rows:cgjhmxList},
				reader : new Ext.data.JsonReader( {
					root : 'rows',
					id : 'id'
					}, ['key', 'value', 'id'])
			});
		
			oSearchField = new Ext.form.ComboBox({  
				id : sId,
				fieldLabel : sLabel,
				xtype : 'combo',
				store : comboStatusStore,
				valueField : "value",
				displayField : "key",
				mode : 'local',
				triggerAction: 'all', 
				forceSelection : true,
				blankText : '',
				emptyText : '',
		//		hiddenName : '',
				editable : false,
		//		name : '',
				anchor : '90%'
			});
		}
		tmpPanel = new Ext.Panel( {
			labelWidth : this.labelWidth,
			layout : 'form',
			columnWidth : .3,
			id : 'SFP#' + sId,
			items : [oSearchField]
		});
	
		oPanel.add(tmpPanel);
		if (typeof(bHide) == 'boolean' && bHide == true) {
			tmpPanel.hide();
		}
	}
});
