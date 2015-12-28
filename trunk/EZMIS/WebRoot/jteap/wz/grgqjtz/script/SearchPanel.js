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
		var dtStart=null;
		var oItems = oPanel.items.items;
		var queryParamsJson = {};
		var queryParamsSql='';
		var flag = false;
		var hasSearchCondition = false;
	 	Ext.each(oItems,function(oItem){
	 		
	 		if(oItem.hidden==false){
	 			var temp = oItem.items.items[0];
	 			var tempValue = temp.getValue();
	 			//值不为空才作为参数
	 			if(tempValue!=null&&tempValue!=""){
	 				hasSearchCondition=true;
		 			//日期字段
		 			if(temp.triggerClass=="x-form-date-trigger"){
		 				tempValue = formatDate(tempValue,"yyyy-MM-dd");
		 				if(temp.id.split("#")[1]=="ffsj"){
		 					dtStart = tempValue;
		 					queryParamsSql+="to_char(obj.ffsj,'yyyy-mm-dd')>='"+tempValue+"' and ";
		 				}else if(temp.id.split("#")[1]=="dqsj"){
		 					queryParamsSql+="to_char(obj.dqsj,'yyyy-mm-dd')<='"+tempValue+"' and ";
		 					if(dtStart!=null){
		 						if(dtStart>tempValue){
		 							alert("发放日期不能大于到期日期");
		 							flag=true;
		 							dtStart=null;
		 							return false;
		 						}
		 					}
		 				}
		 			}else{
		 				//其他字段查询
		 				queryParamsSql+="obj."+temp.id.split("#")[1]+" like '%"+encodeURIComponent(tempValue)+"%' and ";
		 			}
	 			}
	 		};
	 	});
	 	if(flag){
	 		return;
	 	}
	 	var url=link4;
	 	if(hasSearchCondition) url=link4.split('?')[0]+"?queryParamsSql=obj.grgyqf='1' and "+queryParamsSql.substring(0,queryParamsSql.length-5);
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
		}else if (sType == "dateFieldShowTime") {
			oSearchField = new Ext.form.DateField( {
				fieldLabel : sLabel,
				format : 'Y-m-d H:i',
				anchor : '90%',
				id : sId,
				menu : new DatetimeMenu()
			});
		}else if(sType == "wzdagl"){
			oSearchField = new Ext.app.TriggerField({
				id : sId,
				fieldLabel : sLabel,
				readOnly:true,
				onTriggerClick:function(){
					var field = this;
					var url = contextPath+"/jteap/wz/wzlb/selectWzdaIndex.jsp";
					var result = showModule(url, true, 850, 500,'singleSelect|1');
					if (result != null) {
						this.setValue(result.wzmc);
					} else this.reset();
				},
				anchor : '90%'
			});
		}else if(sType == "persons"){
			oSearchField = new Ext.app.TriggerField({
				id : sId,
				fieldLabel : sLabel,
				readOnly:false,
				onTriggerClick:function(){
					var field = this;
					/*
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
					*/
					var url = contextPath+"/jteap/wz/grgqjtz/personSelect.jsp";
					var result = showModule2(url, true, 650, 500,{});
					if (result != null) {
						//getEditor('BMZG').setValue(result.name+'|'+result.name);
						Ext.getCmp(sId).setValue(result.name);
					}else{
						this.reset();
						Ext.getCmp(sId).reset();
					}
				},
				anchor : '90%'
			});
		} else if (sType == "zg") {
			oSearchField = new Ext.app.TriggerField({
				id : sId,
				fieldLabel : sLabel,
				readOnly:true,
				onTriggerClick:function(){
					var field = this;
					var url = contextPath+"/jteap/wz/grgqjtz/personSelect.jsp";
					var result = showModule2(url, true, 700, 600,{});
					if (result != null) {
						this.setValue(result.name);
					} else this.reset();
				},
				anchor : '90%'
			});
		} else if (sType == "comboCkgl") {
			oSearchField = new Ext.form.ComboBox({  
				id : sId,
				fieldLabel : sLabel,
				xtype:'combo', 
				store:new Ext.data.Store({
					baseParams:{PAGE_FLAG:'PAGE_FLAG_NO'},
					autoLoad : true, 
					proxy: new Ext.data.ScriptTagProxy({url : link3}),
					reader: new Ext.data.JsonReader({root: 'list'},
							['ckmc','ckbm'])
				}),  
                 valueField:'ckbm',  
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
		} else if(sType=="comboCkrzlb"){
			var cklbList=$dictList("CKRZLB");   
			var comboStatusStore = new Ext.data.Store({
				data: {rows:cklbList},
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
