/**
 * 查询面板
 */
var SearchPanel = function(config){
	//点击查询后的事件
	this.searchClick=function(){
		var oPanel=searchtemp.items.get(0);
		var oItems = oPanel.items.items;
		var queryParamsSql = "";
		var dysczr = "";
	 	Ext.each(oItems,function(oItem){
	 		if(oItem.hidden==false){
	 			var temp = oItem.items.items[0];
	 			var tempValue = temp.getValue();
	 			//值不为空才作为参数
	 			if(tempValue!=null&&tempValue!=""){
	 				if(temp.triggerClass=="x-form-date-trigger"){
	 					//日期字段
		 				tempValue = formatDate(tempValue,"yyyy-MM-dd");
		 				if(temp.id.split("#")[1]=="startDate"){
		 					startDt = tempValue;
		 					queryParamsSql+="to_char(obj.sqsj,'yyyy-mm-dd')>='"+tempValue+"' and ";
		 				} 
		 			}else if(temp.id.split("#")[1]=="dysczr"){
		 					dysczr = encodeURIComponent(tempValue);
		 			}else{
		 				//其他字段模糊查询
		 				queryParamsSql+="obj."+temp.id.split("#")[1]+" like '"+encodeURIComponent("$"+tempValue+"$")+"' and ";
					}
	 			}
	 		};
	 	});
	 	var url = "";
	 	if(dysczr == ""){
		 	url = link1+"?queryParamsSql=obj.lydqf='1'&limit=100";
	 	}else{
		 	url = link1+"?queryParamsSql=obj.lydqf='1'&dysczr="+dysczr+"&limit=100";
	 	}
		rightGrid.changeToListDS(url);
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
 		var url = link1+"?queryParamsSql=obj.lydqf='1'"+"&limit=100";
 		rightGrid.changeToListDS(url);
 		rightGrid.getStore().reload();
	};
	
	SearchPanel.superclass.constructor.call(this,config);
}

Ext.extend(SearchPanel, Ext.app.SearchPanel, {

			/**
			 * 覆盖父类方法，添加comboBox类型的查询框
			 */
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
				// 文本类型
				if (sType == "textField") {
					oSearchField = new Ext.form.TextField({
								fieldLabel : sLabel,
								width : this.txtWidth,
								anchor : '90%',
								id : sId
							});
					// 日期类型
				} else if (sType == "dateField") {
					oSearchField = new Ext.form.DateField({
								fieldLabel : sLabel,
								format : 'Y-m-d',
								width : this.txtWidth,
								anchor : '90%',
								readOnly : true,
								id : sId
							});
				} else if (sType == "dateFieldShowTime") {
					oSearchField = new Ext.form.DateField({
								fieldLabel : sLabel,
								format : 'Y-m-d H:i',
								width : this.txtWidth,
								readOnly:true,
								id : sId,
								anchor : '90%',
								menu : new DatetimeMenu()
							});
				} else if (sType == "comboBox") {
					oSearchField = new Ext.form.ComboBox({  
						id : sId,
						fieldLabel : sLabel,
						xtype:'combo', 
						store:new Ext.data.Store({
							baseParams:{PAGE_FLAG:'PAGE_FLAG_NO'},
							autoLoad : true, 
							proxy: new Ext.data.ScriptTagProxy({url : link21}),
							reader: new Ext.data.JsonReader({root: 'list'},
									['userLoginName','userName'])
						}),  
		                 valueField:'userLoginName',  
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
				}
				tmpPanel = new Ext.Panel({
							labelWidth : this.labelWidth,
							layout : 'form',
							columnWidth : .3,
							id : 'SFP#' + sId,
							anchor : '90%',
							items : [oSearchField]
						});

				oPanel.add(tmpPanel);
				if (typeof(bHide) == 'boolean' && bHide == true) {
					tmpPanel.hide();
				}

			}

		});