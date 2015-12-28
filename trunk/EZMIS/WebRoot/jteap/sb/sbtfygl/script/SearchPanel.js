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
		 					queryParamsSql+="to_char(obj.tfysj,'yyyy-mm-dd')>='"+tempValue+"' and ";
		 				}else if(temp.id.split("#")[1]=="endDate"){
		 					queryParamsSql+="to_char(obj.tfysj,'yyyy-mm-dd')<='"+tempValue+"' and ";
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
		 			}else{
		 				//其他字段模糊查询
		 				queryParamsSql+="obj."+temp.id.split("#")[1]+"='"+encodeURIComponent(tempValue)+"' and ";
					}
	 			}
	 		};
	 	});
	 	var url = link4+"?queryParamsSql="+queryParamsSql.substring(0,queryParamsSql.length-5)+"&limit=20";
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
 		var url = link4;
 		rightGrid.changeToListDS(url);
 		rightGrid.getStore().reload();
	};
	
	SearchPanel.superclass.constructor.call(this,config);
}

Ext.extend(SearchPanel, Ext.app.SearchPanel, {
	
	/**
	 * 覆盖父类方法，添加comboBox类型的查询框
	 */
	appendSearchField: function(searchField,bHide){
	 	var sLabel=searchField;
	 	var sId="sf#"+searchField;
	 	var sType = searchField;
	 	if(searchField.indexOf("#")>=0){
	 		var tmp=searchField.split("#");
	 		sLabel=tmp[0];
	 		sId='sf#'+tmp[1];
	 		sType = tmp[2]
	 	}
	 	var oPanel=this.items.get(0);
	 	var oSearchField=null;
	 	//文本类型
		if(sType == "textField"){
	 		oSearchField=new Ext.form.TextField({fieldLabel: sLabel,width:this.txtWidth,id:sId});
		//日期类型
		}else if(sType=="dateField"){
			oSearchField =new Ext.form.DateField({fieldLabel: sLabel,format:'Y-m-d',width:this.txtWidth,readOnly : true,id:sId});
		}else if(sType=="dateFieldShowTime"){
			oSearchField =new Ext.form.DateField({fieldLabel: sLabel,format:'Y-m-d H:i',width:this.txtWidth,readOnly:true,id:sId,menu:new DatetimeMenu()});
		}else if(sType=="comboBox"){
			var arrays = searchField.split("#");
			//机组分类数据源
			var storeJz=  new Ext.data.SimpleStore({
				fields: ["retrunValue", "displayText"],
				data: [['','全部'],['#1机组','#1机组'],['#2机组','#2机组'],['#3机组','#3机组'],['#4机组','#4机组']]
			});
			
			//评级级别
			if(arrays[1] == 'jz'){
				oSearchField = new Ext.form.ComboBox({
					id: sId,
					fieldLabel : sLabel,
					valueField: "retrunValue",
					displayField: "displayText",
					mode: 'local',
					triggerAction: 'all',
					blankText: '请选择机组',
					emptyText: '请选择机组',
					width : this.txtWidth,
					forceSelection: true,
					editable: false,
					allowBlank: true,
					store: storeJz
				});
			}
		}
		tmpPanel=new Ext.Panel({labelWidth:this.labelWidth,layout:'form',columnWidth:.3,id:'SFP#'+sId,
			items:[oSearchField]
		});
		
	 	oPanel.add(tmpPanel);
	 	if(typeof(bHide)=='boolean' && bHide==true){
	 		tmpPanel.hide();
	 	}

	 }
	 
});