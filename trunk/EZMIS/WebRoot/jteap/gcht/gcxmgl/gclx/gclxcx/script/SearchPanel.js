var xmbh = "";
var status = "";
var sqbmqz = "";

/**
 * 查询面板
 */
var SearchPanel = function(config){
	//点击查询后的事件
	this.searchClick=function(){
		var oPanel=searchtemp.items.get(0);
		var oItems = oPanel.items.items;
		var queryParamsSql = "";
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
		 					queryParamsSql+=" and to_char(cjsj,'yyyy-mm-dd')>='"+tempValue+"'";
		 				}else if(temp.id.split("#")[1]=="endDate"){
		 					queryParamsSql+=" and to_char(cjsj,'yyyy-mm-dd')<='"+tempValue+"'";
		 					if(startDt!=null){
		 						if(startDt>tempValue){
		 							alert("开始日期不能大于结束");
		 							flag=true;
		 							startDt=null;
		 							return false;
		 						}else{
		 							startDt=null;
		 						}
		 					}
		 				}
		 			}else if(temp.id.split("#")[1]=="xmbh"){
		 					xmbh = tempValue;
		 			}else if(temp.id.split("#")[1]=="zt"){
		 				    status = tempValue;
		 			}else if(temp.id.split("#")[1]=="czyxm"){
		 				    sqbmqz = tempValue;
		 			}else{
		 				//其他字段模糊查询
		 				queryParamsSql+="obj."+temp.id.split("#")[1]+" like '"+encodeURIComponent("$"+tempValue+"$")+"' and ";
					}
	 			}
	 		};
	 	});
	 	var url = link1+"?queryParamsSql=" + queryParamsSql+ "&limit=50";
	 	/**
	 	if(queryParamsSql.substring(0,queryParamsSql.length-5) == ""){
	 		if(sqbm != ""){
		 		url = link1+"?queryParamsSql=obj.xqjhqf='1'&limit=20&sqbm="+sqbm+"";
	 		}else{
	 			url = link1+"?queryParamsSql=obj.xqjhqf='1'&limit=20";
	 		}
	 	}else{
	 		url = link1+"?queryParamsSql=obj.xqjhqf='1' and "+queryParamsSql.substring(0,queryParamsSql.length-5)+"&limit=20&wzmc="+wzmc+"";
	 	}
	 	**/
	 	if(xmbh != ""){
	 		url += "&xmbh="+xmbh;
	 	}
	 	if(status != ""){
		 	url += "&status="+status;
	 	}
	 	if(sqbmqz != ""){
		 	url += "&sqbmqz="+sqbmqz;
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
 		xmbh = "";
	 	status = "";
	 	sqbmqz = "";
 		var url = link1+"?limit=50";
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
				}else if(sType == "persons"){
					oSearchField = new Ext.app.TriggerField({
						id : sId,
						fieldLabel : sLabel,
						blankText: '请选择申请人',
						emptyText: '请选择申请人',
						readOnly:false,
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
						anchor : '80%'
					});
				} else if (sType == "comboBox") {
					var arrays = searchField.split("#");
					//需求计划申请状态数据源
					var storeStatus = new Ext.data.SimpleStore({
								fields : ["retrunValue", "displayText"],
								data : [['0', '待审批'], ['1', '已完成']]
							});
					if(arrays[1] == 'zt'){
						oSearchField = new Ext.form.ComboBox({
							id: sId,
							fieldLabel : sLabel,
							xtype : 'combo',
							valueField: "retrunValue",
							displayField: "displayText",
							mode: 'local',
							triggerAction: 'all',
							blankText: '请选择状态',
							emptyText: '请选择状态',
							width : this.txtWidth,
							anchor : '90%',
							forceSelection: true,
							editable: false,
							store: storeStatus
						});
					}
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