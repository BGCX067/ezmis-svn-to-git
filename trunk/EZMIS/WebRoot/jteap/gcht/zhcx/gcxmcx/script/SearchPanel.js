/**
 * 查询面板
 */
SearchPanel = function(config) {
	// 点击查询后的事件
	this.searchClick = function() {
		var oPanel = searchtemp.items.get(0);
		var oItems = oPanel.items.items;
		var queryParamsJson = {};
		var queryParamsSql = "";
		Ext.each(oItems, function(oItem) {
			if (oItem.hidden == false) {
				var temp = oItem.items.items[0];
				var tempValue = temp.getValue();
				// 值不为空才作为参数
				if (tempValue != null && tempValue != "") {
					if(temp.id.split("#")[1] == "xmmc") {
						queryParamsSql += temp.id.split("#")[1] + " like '"
								+ encodeURIComponent("%" + tempValue + "%") + "' and ";
					}else if (temp.id.split("#")[1] == "cjsj") {
						queryParamsSql += " to_char(" + temp.id.split("#")[1] + ",'yyyy')='"
								+ tempValue + "' and ";
					}else{
						queryParamsSql += temp.id.split("#")[1] + " = '"
								+ encodeURIComponent(tempValue) + "' and ";
					}
				}
			};
	}	);

		var oNode = leftTree.getSelectionModel().getSelectedNode();
		if (oNode && !oNode.isRootNode()) {
			queryParamsSql += "xmyj = '"+oNode.text+"' and ";
		}
		var url = link2 + "?queryParamsSql=" + queryParamsSql.substring(0, queryParamsSql.length - 5);
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
		var url = link2;
		var oNode = leftTree.getSelectionModel().getSelectedNode();
		if (oNode && !oNode.isRootNode()) {
			url += "?queryParamsSql="+"xmyj='"+oNode.text+"'";
		}
		rightGrid.changeToListDS(url);
		rightGrid.getStore().reload();
	};
	SearchPanel.superclass.constructor.call(this, config);
};
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
		if (sType == "dateField") {
			oSearchField = new Ext.form.DateField({
				fieldLabel : sLabel,
				format : 'Y-m-d',
				width : this.txtWidth,
				id : sId
			});
		} else if (sType == "comboTree") {
			var tmp = searchField.split("#");
			var displayField = tmp[3];
			var valueField = tmp[4];
			var emptyText = tmp[5];
			oSearchField = new Ext.app.ComboTree({
				id : sId,
				dataUrl : link4,
				width : 140,
				hiddenName : sId,
				fieldLabel : sLabel,
				listWidth : 300,
				listHeight : 80,
				triggerClass : 'comboTree',
				localData : false,
				rootVisible : false,
				readOnly : true
			});
		} else if (sType == "textField") {
			oSearchField = new Ext.form.TextField({
				fieldLabel : sLabel,
				width : this.txtWidth,
				id : sId
			});
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
			
			//项目类型
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
					listWidth: this.txtWidth,
					forceSelection: true,
					editable: false,
					allowBlank: true,
					store: storeXmlx
				});
			}
		}
		tmpPanel = new Ext.Panel({
			labelWidth : this.labelWidth,
			layout : 'form',
			columnWidth : .3,
			id : 'SFP#' + sId,
			bodyStyle : 'border:0px',
			items : [oSearchField]
		});

		oPanel.add(tmpPanel);
		if (typeof(bHide) == 'boolean' && bHide == true) {
			tmpPanel.hide();
		}

	}
});