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
					tempValue = formatDate(tempValue, "yyyy-MM-dd");
					queryParamsSql += "to_char(obj." + temp.id.split("#")[1] + ",'yyyy-mm-dd') > '" + tempValue + "' and ";
					// 其他字段模糊查询
				} else {
					queryParamsSql += "obj." + temp.id.split("#")[1] + " like '" + encodeURIComponent("%" + tempValue + "%")
							+ "' and ";
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
		if (sType == "dateFieldShowTime") {
			oSearchField = new Ext.form.DateField({
				fieldLabel : sLabel,
				format : 'Y-m-d H:i',
				width : this.txtWidth,
				id : sId,
				menu : new DatetimeMenu()
			});
		} else if (sType == "comboboxJzlx") {
			var distsJzlx = $dictListAjax("YX_JZLX");
			var dists300Kgmc = $dictListAjax("YX_300_KGMC");
			var dists600Kgmc = $dictListAjax("YX_600_KGMC");

			var storeJzlx = new Ext.data.Store({
				data : {
					rows : distsJzlx
				},
				reader : new Ext.data.JsonReader({
					root : 'rows',
					id : 'id'
				}, ['key', 'value', 'id'])
			});

			var ds300Kgmc = new Ext.data.Store({
				reader : new Ext.data.JsonReader({
					root : 'rows',
					id : 'id'
				}, ['key', 'value', 'id']),
				data : {
					rows : dists300Kgmc
				}
			});

			var ds600Kgmc = new Ext.data.Store({
				reader : new Ext.data.JsonReader({
					root : 'rows',
					id : 'id'
				}, ['key', 'value', 'id']),
				data : {
					rows : dists600Kgmc
				}
			});

			oSearchField = new Ext.form.ComboBox({
				id : sId,
				fieldLabel : sLabel,
				width : this.txtWidth,
				editable : false,
				store : storeJzlx,
				valueField : 'value',
				displayField : 'key',
				triggerAction : 'all',
				mode : 'local',
				listeners : {
					select : function(t, r, index) {
						var value = r.data.value;
						if (value == "600MW机组") {
							Ext.getCmp('sf#kgmc').bindStore(ds600Kgmc);
						} else {
							Ext.getCmp('sf#kgmc').bindStore(ds300Kgmc);
						}
					}
				}
			})
		} else if (sType == "comboboxKgmc") {
			var dsKgmc = new Ext.data.Store({
				reader : new Ext.data.JsonReader({
					root : 'rows',
					id : 'id'
				}, ['key', 'value', 'id'])
			});

			oSearchField = new Ext.form.ComboBox({
				id : sId,
				fieldLabel : sLabel,
				width : this.txtWidth,
				store : dsKgmc,
				editable : false,
				valueField : 'value',
				displayField : 'key',
				triggerAction : 'all',
				mode : 'local'
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