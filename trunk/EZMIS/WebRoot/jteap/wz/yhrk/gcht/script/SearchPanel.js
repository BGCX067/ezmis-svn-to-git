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
		var dhStart=null;
		var oItems = oPanel.items.items;
		var queryParamsJson = {};
		var queryParamsSql="";
		var flag = false;
	 	Ext.each(oItems,function(oItem){
	 		if(oItem.hidden==false){
	 			var temp = oItem.items.items[0];
	 			var tempValue = temp.getValue();
	 			//值不为空才作为参数
	 			if(tempValue!=null&&tempValue!=""){
		 			//日期字段
		 			if(temp.triggerClass=="x-form-date-trigger"){
		 				tempValue = formatDate(tempValue,"yyyy-MM-dd");
		 				if(temp.id.split("#")[1]=="dhStart"){
		 					dhStart = tempValue;
		 					queryParamsSql+="to_char(obj.yhdgl.dhrq,'yyyy-mm-dd')>='"+tempValue+"' and ";
		 				}else if(temp.id.split("#")[1]=="dhEnd"){
		 					queryParamsSql+="to_char(obj.yhdgl.dhrq,'yyyy-mm-dd')<='"+tempValue+"' and ";
		 					if(dhStart!=null){
		 						if(dhStart>tempValue){
		 							alert("开始日期不能大于结束");
		 							flag=true;
		 							dhStart=null;
		 							return false;
		 						}else{
		 							dhStart=null;
		 						}
		 					}
		 				}
		 			//其他字段模糊查询
		 			}else{
		 				queryParamsSql+="obj."+temp.id.split("#")[1]+" like '%25"+encodeURIComponent(tempValue)+"%25' and ";
		 			}
	 			}
	 		};
	 	});
	 	if(flag){
	 		return;
	 	}
	 	//queryParamsSql+="(obj.zt='1' or obj.zt='2') and ";
	 	var url = link4+"?queryParamsSql="+queryParamsSql.substring(0,queryParamsSql.length-5);
		rightGrid.changeToListDS(url)
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
		} else if (sType == "comboBgy") {
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
							['userName','userName'])
				}),  
                 valueField:'userName',  
                 displayField:'userName',  
                 mode:'local',  
                 blankText : '', 
                 emptyText:'',            
//               hiddenName:'userName', 
                 editable:false,
//         		 editable:true,selectOnFocus:true,
                 forceSelection:true,  
                 triggerAction:'all',  
	   			 triggerClass:'comboCgy',
                 anchor:'90%' 
			});
		} else if(sType=="comboZt"){
			/*
			var cgjhmxList=$dictList("CGJHMX");   
			var comboStatusStore = new Ext.data.Store({
				data: {rows:cgjhmxList},
				reader : new Ext.data.JsonReader( {
					root : 'rows',
					id : 'id'
					}, ['key', 'value', 'id'])
			});
		*/
			var localstore = new Ext.data.SimpleStore({
                    fields: ['value', 'key'],
                    data : [['1','已入库'],['2','未入库']]
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
		} else if(sType == "wzdagl"){
			oSearchField = new Ext.app.TriggerField({
				id : sId,
				fieldLabel : sLabel,
				readOnly :true,
				readOnly:true,
				onTriggerClick:function(){
					var field = this;
					var url = contextPath+"/jteap/wz/wzlb/selectWzdaIndex.jsp";
					var result = showModule(url, true, 850, 500,'singleSelect|1');
					if (result != null) {
						//this.displayValue=result.wzmc;
          				//this.setValue(result.id);
						//alert(this.id);
						this.setValue(result.wzmc);
					} else this.reset();
				},
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
