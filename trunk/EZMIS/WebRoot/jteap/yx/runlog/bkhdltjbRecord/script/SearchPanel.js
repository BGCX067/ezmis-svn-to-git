/**
 * 查询面板
 */
var SearchPanel = function(config){
	//点击查询后的事件
	this.searchClick = function() {
		var oPanel = searchtemp.items.get(0);
		var oItems = oPanel.items.items;
		var queryParamsSql = "";
		Ext.each(oItems, function(oItem) {
			if (oItem.hidden == false) {
				var temp = oItem.items.items[0];
				var tempValue = temp.getValue();
				// 值不为空才作为参数
				if (tempValue != null && tempValue != "") {
					if (temp.id.split("#")[1] == "cbr") {
						queryParamsSql += " a." + temp.id.split("#")[1] + " like '"
								+ encodeURIComponent("%" + tempValue + "%") + "' and ";
					}else if (temp.id.split("#")[1] == "zbbc") {
						queryParamsSql += " a." + temp.id.split("#")[1] + " = '" + encodeURIComponent(tempValue)
								+ "' and ";
					} else if (temp.id.split("#")[1] == "beginYmd") {
						tempValue = formatDate(new Date(tempValue), "yyyy-MM-dd");
						queryParamsSql += " to_char(a.CBSJ,'yyyy-MM-dd')>= '"+ tempValue + "' and ";
					} else if (temp.id.split("#")[1] == "endYmd") {
						tempValue = formatDate(new Date(tempValue), "yyyy-MM-dd");
						queryParamsSql += " to_char(a.CBSJ,'yyyy-MM-dd')<= '"+ tempValue + "' and ";
					} else if (temp.id.split("#")[1] == "jzbh") {
						queryParamsSql += " a." + temp.id.split("#")[1] + " = '"
								+ encodeURIComponent(tempValue) + "' and ";
					} else if (temp.id.split("#")[1] == "zbzb") {
						queryParamsSql += " a." + temp.id.split("#")[1] + " = '"
								+ encodeURIComponent(tempValue) + "' and ";
					}
				} 
				//下拉框取用户输入值
				if ( temp.id.split("#")[1] == "YY" ){
					if (temp.getRawValue() != null && temp.getRawValue() != "") {
				    	queryParamsSql += " a." + temp.id.split("#")[1] + " like '"
				            + encodeURIComponent(temp.getRawValue()) + "' and "; 
				    }
				}
			};
		});
		var url = link1 + "?queryParamsSql=" + queryParamsSql.substring(0, queryParamsSql.length - 5)+"&formSn="+formSn;
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
				temp.setValue('');	 				
	 		};
	 	});
 		var url = link1+"?formSn="+formSn;
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
		}else if(sType == "CobomBoxBC"){
				    var dict = $dictList("zbbc");
				    var storeBC = new Ext.data.Store({
					data : {
						rows : dict
					},
					reader : new Ext.data.JsonReader({
						root : 'rows',
						id : 'id'
					}, ['key', 'value', 'id'])
				});
		
				oSearchField = new Ext.form.ComboBox({
					id : sId,
					fieldLabel : sLabel,
					width : this.txtWidth,
					editable : false,
					store : storeBC,
					valueField : 'value',
					displayField : 'key',
					triggerAction : 'all',
					mode : 'local'
				});
		}else if(sType == "CobomBoxJZ"){
				    var dict = $dictList("jizu");
				    var storeJZ = new Ext.data.Store({
					data : {
						rows : dict
					},
					reader : new Ext.data.JsonReader({
						root : 'rows',
						id : 'id'
					}, ['key', 'value', 'id'])
				});
		
				oSearchField = new Ext.form.ComboBox({
					id : sId,
					fieldLabel : sLabel,
					width : this.txtWidth,
					editable : false,
					store : storeJZ,
					valueField : 'value',
					displayField : 'key',
					triggerAction : 'all',
					mode : 'local'
				});
		}else if(sType == "ComboBoxZB"){
					var dict = $dictList("zbzb");
				    var storeZB = new Ext.data.Store({
					data : {
						rows : dict
					},
					reader : new Ext.data.JsonReader({
						root : 'rows',
						id : 'id'
					}, ['key', 'value', 'id'])
				});
		
				oSearchField = new Ext.form.ComboBox({
					id : sId,
					fieldLabel : sLabel,
					width : this.txtWidth,
					editable : false,
					store : storeZB,
					valueField : 'value',
					displayField : 'key',
					triggerAction : 'all',
					mode : 'local'
				});
		}else if(sType == "ComboBoxYY"){
					var dict = $dictList("BKHDLYY");
				    var storeYY = new Ext.data.Store({
					data : {
						rows : dict
					},
					reader : new Ext.data.JsonReader({
						root : 'rows',
						id : 'id'
					}, ['key', 'value', 'id'])
				});
		
				oSearchField = new Ext.form.ComboBox({
					id : sId,
					fieldLabel : sLabel,
					width : 130,
					editable : true,
					store : storeYY,
					valueField : 'value',
					displayField : 'key',
					triggerAction : 'all',
					mode : 'local'
				});
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