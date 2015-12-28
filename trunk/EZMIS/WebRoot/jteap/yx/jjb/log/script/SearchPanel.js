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
		 			//日期字段
		 			if(temp.triggerClass=="x-form-date-trigger"){
		 				tempValue = formatDate(tempValue,"yyyy-MM-dd");
		 				if(temp.id.split("#")[1]=="doDate"){
		 					queryParamsSql+="to_char(obj.doDate,'yyyy-mm-dd')='"+tempValue+"' and ";
		 				}
		 			}else{
		 				if(temp.id.split("#")[1]=="jiaobanr" || temp.id.split("#")[1]=="jiebanr"){
		 					queryParamsSql+="obj."+temp.id.split("#")[1]+" like '%"+encodeURIComponent(tempValue)+"%' and ";
		 				}else{
			 				queryParamsSql+="obj."+temp.id.split("#")[1]+"='"+encodeURIComponent(tempValue)+"' and ";
		 				}
		 			}
	 			}
	 		};
	 	});
	 	var url = link1+"?queryParamsSql="+queryParamsSql.substring(0,queryParamsSql.length-5);
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
 		var url = link1;
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
			oSearchField =new Ext.form.DateField({fieldLabel: sLabel,format:'Y-m-d',width:this.txtWidth,id:sId});
		}else if(sType=="dateFieldShowTime"){
			oSearchField =new Ext.form.DateField({fieldLabel: sLabel,format:'Y-m-d H:i',width:this.txtWidth,id:sId,menu:new DatetimeMenu()});
		}else if(sType=="comboBox"){
			var arrays = searchField.split("#");
			
			var dict_zbzb = $dictList("zbzb");
			//值班值别数据源
			var storeZB = new Ext.data.Store({
				data: {rows:dict_zbzb},
				reader: new Ext.data.JsonReader( {
					root: 'rows',
					id: 'id'
				}, ['key', 'value', 'id'])
			});
			
			var dict_zbbc = $dictList("zbbc");
			//值班班次数据源
			var storeBC = new Ext.data.Store({
				data: {rows:dict_zbbc},
				reader: new Ext.data.JsonReader( {
					root: 'rows',
					id: 'id'
				}, ['key', 'value', 'id'])
			});
			
			//岗位类别数据源
			var storeGwlb = new Ext.data.SimpleStore({
				fields: ['key','value'],
				data: [
						['值长','值长'],
						['#1机长','#1机长'],
						['#2机长','#2机长'],
						['#3机长','#3机长'],
						['#4机长','#4机长'],
						['电气','电气'],
						['零米','零米']
					]		
			});
			
			//交班班次
			if(arrays[1] == 'jiaobanbc'){
				oSearchField = new Ext.form.ComboBox({
					id: sId,
					fieldLabel : sLabel,
					valueField: "value",
					displayField: "value",
					mode: 'local',
					triggerAction: 'all',
					blankText: '请选交班班次',
					emptyText: '请选交班班次',
					width : this.txtWidth,
					forceSelection: true,
					editable: false,
					allowBlank: true,
					store: storeBC
				});
			}
			
			//交班值别
			if(arrays[1] == 'jiaobanzb'){
				oSearchField = new Ext.form.ComboBox({
					id: sId,
					fieldLabel : sLabel,
					valueField: "value",
					displayField: "value",
					mode: 'local',
					triggerAction: 'all',
					blankText: '请选交班值别',
					emptyText: '请选交班值别',
					width : this.txtWidth,
					forceSelection: true,
					editable: false,
					allowBlank: true,
					store: storeZB
				});
			}
			
			//接班班次
			if(arrays[1] == 'jiebanbc'){
				oSearchField = new Ext.form.ComboBox({
					id: sId,
					fieldLabel : sLabel,
					valueField: "value",
					displayField: "value",
					mode: 'local',
					triggerAction: 'all',
					blankText: '请选接班班次',
					emptyText: '请选接班班次',
					width : this.txtWidth,
					forceSelection: true,
					editable: false,
					allowBlank: true,
					store: storeBC
				});
			}
			
			//接班值别
			if(arrays[1] == 'jiebanzb'){
				oSearchField = new Ext.form.ComboBox({
					id: sId,
					fieldLabel : sLabel,
					valueField: "value",
					displayField: "value",
					mode: 'local',
					triggerAction: 'all',
					blankText: '请选接班值别',
					emptyText: '请选接班值别',
					width : this.txtWidth,
					forceSelection: true,
					editable: false,
					allowBlank: true,
					store: storeZB
				});
			}
			
			//岗位类别
			if(arrays[1] == 'gwlb'){
				oSearchField = new Ext.form.ComboBox({
					id: sId,
					fieldLabel : sLabel,
					valueField: "value",
					displayField: "key",
					mode: 'local',
					triggerAction: 'all',
					blankText: '请选岗位类别',
					emptyText: '请选岗位类别',
					width : this.txtWidth,
					forceSelection: true,
					editable: false,
					allowBlank: true,
					store: storeGwlb
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