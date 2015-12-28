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
					// 日期字段
				if (temp.triggerClass == "x-form-date-trigger") {
					if (temp.id.split("#")[1] == "jhzbsj1") {
						tempValue = formatDate(tempValue, "yyyy-MM-dd");
						queryParamsSql += "to_char(obj.jhzbsj,'yyyy-mm-dd') >= '" + tempValue + "' and ";
					} else if (temp.id.split("#")[1] == "jhzbsj2") {
						tempValue = formatDate(tempValue, "yyyy-MM-dd");
						queryParamsSql += "to_char(obj.jhzbsj,'yyyy-mm-dd') <= '" + tempValue + "' and ";
					}
					if (temp.id.split("#")[1] == "sjzbsj1") {
						tempValue = formatDate(tempValue, "yyyy-MM-dd");
						queryParamsSql += "to_char(obj.sjzbsj,'yyyy-mm-dd') >= '" + tempValue + "' and ";
					} else if (temp.id.split("#")[1] == "sjzbsj2") {
						tempValue = formatDate(tempValue, "yyyy-MM-dd");
						queryParamsSql += "to_char(obj.sjzbsj,'yyyy-mm-dd') <= '" + tempValue + "' and ";
					}
					// 其他字段模糊查询
				} else {
					queryParamsSql += "obj." + temp.id.split("#")[1] + " like '"
							+ encodeURIComponent("%" + tempValue + "%") + "' and ";
				}
			}
		};
	}	);
		var url = link1 + "?queryParamsSql=" + queryParamsSql.substring(0, queryParamsSql.length - 5);
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
		} else if (sType == "comboZbfs") {
			var distsJz = $dictListAjax("GCHT_ZBFS");

			var storeJz = new Ext.data.Store({
				data : {
					rows : distsJz
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
				store : storeJz,
				valueField : 'value',
				displayField : 'key',
				triggerAction : 'all',
				mode : 'local'
			})
		} else if (sType == "textField") {
			oSearchField = new Ext.form.TextField({
				fieldLabel : sLabel,
				width : this.txtWidth,
				id : sId
			});
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