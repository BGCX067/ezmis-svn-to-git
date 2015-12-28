/**
 * 查询面板
 */
Ext.form.Field.prototype.msgTarget = 'side';
SearchPanel = function(config) {
	// 点击查询后的事件
	this.searchClick = function() {
		var oPanel = searchtemp.items.get(0);
		var oItems = oPanel.items.items;
		var queryParamsSql = "?t=t";
		Ext.each(oItems, function(oItem) {
					if (oItem.hidden == false) {
						var temp = oItem.items.items[0];
						var tempValue = temp.getValue();
						// 值不为空才作为参数
						if (tempValue != null && tempValue != "") {
							if (temp.id.split("#")[1] == "xmbh") {
								queryParamsSql += "&xmbh="+encodeURIComponent(tempValue);
							}else if (temp.id.split("#")[1] == "xmmc") {
								queryParamsSql += "&xmmc="+encodeURIComponent(tempValue);
							}else if (temp.id.split("#")[1] == "xmlx") {
								queryParamsSql += "&xmlx="+encodeURIComponent(tempValue);
							}else if (temp.id.split("#")[1] == "cbfs") {
								queryParamsSql += "&cbfs="+encodeURIComponent(tempValue);
							}else if (temp.id.split("#")[1] == "htcjsj") {
								queryParamsSql += "&htcjsj="+encodeURIComponent(tempValue);
							}
						}
					};
				});
		var url = link18+queryParamsSql;
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
						temp.setValue('');
					};
				});
		rightGrid.changeToListDS(link18);
		rightGrid.getStore().reload();
	};

	SearchPanel.superclass.constructor.call(this,config);
};
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
		}else if(sType=="yearField"){
			oSearchField =new Ext.form.TimeField({fieldLabel: sLabel,format:'Y',width:this.txtWidth,id:sId,value:new Date().format("Y"),minValue:'2010',maxValue:'2100',increment:525600,readOnly:true});
		}else if(sType=="comboBox"){
			var arrays = searchField.split("#");

			var storeXmlx = new Ext.data.SimpleStore({
				fields: ['key','value'],
				data: [
						['一般立项','一般立项'],
						['委托项目','委托项目']
					]		
			});
			var storeCbfs = new Ext.data.SimpleStore({
				fields: ['key','value'],
				data: [
						['包工包料','包工包料'],
						['包工不包料','包工不包料']
					]		
			});
			
			if(arrays[1] == 'xmlx'){
				oSearchField = new Ext.form.ComboBox({
					id: sId,
					fieldLabel : sLabel,
					valueField: "value",
					displayField: "value",
					mode: 'local',
					triggerAction: 'all',
					blankText: '请选择',
					emptyText: '请选择',
					width : this.txtWidth,
					forceSelection: true,
					editable: false,
					allowBlank: true,
					store: storeXmlx
				});
			}
			if(arrays[1] == 'cbfs'){
				oSearchField = new Ext.form.ComboBox({
					id: sId,
					fieldLabel : sLabel,
					valueField: "value",
					displayField: "value",
					mode: 'local',
					triggerAction: 'all',
					blankText: '请选择',
					emptyText: '请选择',
					width : this.txtWidth,
					forceSelection: true,
					editable: false,
					allowBlank: true,
					store: storeCbfs
				});
			}
		}
		tmpPanel=new Ext.Panel({labelWidth:this.labelWidth,layout:'form',columnWidth:.5,id:'SFP#'+sId,
			items:[oSearchField]
		});
		
	 	oPanel.add(tmpPanel);
	 	if(typeof(bHide)=='boolean' && bHide==true){
	 		tmpPanel.hide();
	 	}
	 }
	 
});