var SearchPanel = function(config){
	var search = this;
	// 点击查询后的事件
	this.searchClick = function() {
		var oPanel = searchtemp.items.get(0);
		var oItems = oPanel.items.items;
		//var queryParamsJson = {};
		var queryParamsSql = "";
		Ext.each(oItems, function(oItem) {
			if (oItem.hidden == false) {
				var temp = oItem.items.items[0];
				var tempValue = temp.getValue();
				// 值不为空才作为参数
				if (tempValue != null && tempValue != "") {
					// key,value字段,可以进行模糊查询
					if (temp.getName().split("#")[1] == "NIAN") {
						queryParamsSql=queryParamsSql+"NIAN,"+tempValue+"!";
					} else if(temp.getName().split("#")[1] == "YUE"){
						queryParamsSql=queryParamsSql+"YUE,"+tempValue+"!";
					}
				}
			};
		});
		if(queryParamsSql!=""){
			queryParamsSql=queryParamsSql.substring(0,queryParamsSql.length-1);
	    	var url=link4+"?bblx="+bblx+"&curDate="+queryParamsSql;
	    	rightGrid.changeToListDS(url);
	    	rightGrid.getStore().reload();
		}
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
		var url=link4+"?bblx="+bblx;
		rightGrid.changeToListDS(url);
		rightGrid.getStore().reload();
	};
	SearchPanel.superclass.constructor.call(this,config);
}
Ext.extend(SearchPanel, Ext.app.SearchPanel, {
	appendSearchField:function(searchField,bHide){
		if(searchField!=""){
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
			}else if(sType=="comboBox"){
				var type=tmp[3];
				var ds = new Ext.data.Store( {
					proxy : new Ext.data.ScriptTagProxy( {
						url : link10
					}),
					reader : new Ext.data.JsonReader( {
					}, ['value', 'displayValue']),
					remoteSort : true
				});
				var oSearchField = new Ext.form.ComboBox( {
					hiddenName : sId,// 真正接受的名字
					store : ds,// 数据源
					displayField : 'displayValue',// 数据显示列名
					valueField : 'value',
					fieldLabel: sLabel,
					width : 80,
					readOnly:true,
					mode : 'local',// 默认以'remote'作为数据源
					triggerAction : 'all',// 单击下拉按钮时激发事件
					typeAhead : true,// 自动完成功能
					selectOnFocus : true
				});
				var loadParam={};
				AjaxRequest_Sync(link10,loadParam,function(request){
					var responseT = request.responseText;
					var responseO = responseT.evalJSON();
					if(responseO.success == true){
						var data={};
						if(type=="1"){
							data=responseO.NIAN[0];
							ds.loadData(data,true);
							oSearchField.setValue(responseO.NIANvalue);
						}else if(type=="2"){
							data=responseO.YUE[0];
							ds.loadData(data,true);
							oSearchField.setValue(responseO.YUEvalue);
						}			
					}
				});
			}
			tmpPanel=new Ext.Panel({labelWidth:40,layout:'form',columnWidth:.2,id:'SFP#'+sId,
				items:[oSearchField]
			});
			
		 	oPanel.add(tmpPanel);
		 	if(typeof(bHide)=='boolean' && bHide==true){
		 		tmpPanel.hide();
		 	}
		 }
	},
	dataStore:function(){
		var ds = new Ext.data.Store( {
			proxy : new Ext.data.ScriptTagProxy( {
				url : link10
			}),
			reader : new Ext.data.JsonReader( {
			}, ['displayValue', 'value']),
			remoteSort : true
		});
		return ds;
	}
});
