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
	 				if(/[@#\$%\^&\*]+/g.test(tempValue)||tempValue.indexOf("'")!=-1){
 						alert("请不要输入非法字符");
 					}else{
 						queryParamsSql+=temp.id.split("#")[1]+"="+encodeURIComponent(tempValue)+"&";
	 				}
	 			}
	 		};
	 	});
	 	var url = link1+"?"+queryParamsSql.substring(0,queryParamsSql.length-1);
		rightGrid.changeToListDS(url);
 		rightGrid.getStore().reload();	 	
	};
	// 清空查询条件
	this.clearSearch = function() {
		var oPanel = searchtemp.items.get(0);
		var oItems = oPanel.items.items;
		Ext.each(oItems, function(oItem) {
					if (oItem.hidden == false) {
						var temp = oItem.items.items[0];
						temp.reset();
						// 日期字段
						if (temp.triggerClass == "x-form-date-trigger") {
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
	 		oSearchField=new Ext.form.TextField({fieldLabel: sLabel,width:this.txtWidth,id:sId,labelStyle:'width:80'});
		//日期类型
		}else if(sType=="dateField"){
			oSearchField =new Ext.form.DateField({fieldLabel: sLabel,format:'Y-m-d',width:this.txtWidth,id:sId,labelStyle:'width:80'});
		}else if(sType=="dateFieldShowTime"){
			oSearchField =new Ext.form.DateField({fieldLabel: sLabel,format:'Y-m-d H:i',width:this.txtWidth,id:sId,menu:new DatetimeMenu()});
		}else if(sType=="comboBox"){
			var arrays = searchField.split("#");
			//评级级别数据源
		
			var storeSex=  new Ext.data.SimpleStore({
				fields: ["retrunValue", "displayText"],
				data: [['GONGHUI1','工会1'],['GONGHUI_2','工会2']]
			});
			var storeYear=  new Ext.data.SimpleStore({
				fields: ["retrunValue", "displayText"],
				data: [['2005','2005'],['2006','2006'],['2007','2007'],['2008','2008'],['2009','2009'],['2010','2010'],['2011','2011'],['2012','2012'],['2013','2013'],['2014','2014']]
			});
			var storeJidu=  new Ext.data.SimpleStore({
				fields: ["retrunValue", "displayText"],
				data: [['1','一季度'],['2','二季度'],['3','三季度'],['4','四季度']]
			});
			
			
			//性别
			if(arrays[1] == 'gonghui'){
				oSearchField = new Ext.form.ComboBox({
					id: sId,
					fieldLabel : sLabel,
					valueField: "retrunValue",
					displayField: "displayText",
					mode: 'local',
					labelStyle:'width:80',
					triggerAction: 'all',
					blankText: '请选择工会',
					emptyText: '请选择工会',
					width : this.txtWidth,
					forceSelection: true,
					editable: false,
					allowBlank: true,
					store: storeSex
				});
			}
				if(arrays[1] == 'year'){
				oSearchField = new Ext.form.ComboBox({
					id: sId,
					fieldLabel : sLabel,
					valueField: "retrunValue",
					displayField: "displayText",
					mode: 'local',
					labelStyle:'width:80',
					triggerAction: 'all',
					blankText: '请选择年份',
					emptyText: '请选择年份',
					width : this.txtWidth,
					forceSelection: true,
					editable: false,
					allowBlank: true,
					store: storeYear
				});
			}
			if(arrays[1] == 'jidu'){
				oSearchField = new Ext.form.ComboBox({
					id: sId,
					fieldLabel : sLabel,
					valueField: "retrunValue",
					displayField: "displayText",
					mode: 'local',
					labelStyle:'width:80',
					triggerAction: 'all',
					blankText: '请选择季度',
					emptyText: '请选择季度',
					width : this.txtWidth,
					forceSelection: true,
					editable: false,
					allowBlank: true,
					store: storeJidu
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