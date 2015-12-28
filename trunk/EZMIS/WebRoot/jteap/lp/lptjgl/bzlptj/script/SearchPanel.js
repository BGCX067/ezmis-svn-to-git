
/**
 * 查询面板
 */
SearchPanel = function(config) {
	Ext.apply(config, {
		txtWidth : 150
	});
	// 点击查询后的事件
	this.searchClick = function() {
		if (Ext.getCmp("sf#jhjssj").getValue() != '' && Ext.getCmp("sf#jhkssj").getValue() != ''
				&& (Ext.getCmp("sf#jhjssj").getValue() < Ext.getCmp("sf#jhkssj").getValue())) {
			alert('开始时间不能大于结束时间！');
			return;
		}
		var tableName = "";
		var groupSn = "";
		var oPanel = searchtemp.items.get(0);
		var oItems = oPanel.items.items;
		var queryParamsSql = "";
		Ext.each(oItems, function(oItem) {
			if (oItem.hidden == false) {
				var temp = oItem.items.items[0];
				var tempValue = temp.getValue();
				// 值不为空才作为参数
				if (tempValue != null && tempValue != "") {
					if (temp.id.split("#")[1] == "jhkssj") {
						tempValue = formatDate(tempValue, "yyyy-MM-dd");
						queryParamsSql += "to_char(a." + temp.id.split("#")[1] + ",'yyyy-mm-dd')>='" + tempValue
								+ "' and ";
					} else if (temp.id.split("#")[1] == "jhjssj") {
						tempValue = formatDate(tempValue, "yyyy-MM-dd");
						queryParamsSql += "to_char(a." + temp.id.split("#")[1] + ",'yyyy-mm-dd')<='" + tempValue
								+ "' and ";
					} else if (temp.id.split("#")[1] == "fxbz") {
						groupSn = temp.hideMode;
					} else if (temp.id.split("#")[1] == "pfl") {
						tableName = tempValue;
					}
				}
			};
		});
		var url = link1 + "?tableName=" + tableName + "&groupSn=" + groupSn + "&queryParamsSql="
				+ queryParamsSql.substring(0, queryParamsSql.length - 5);
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
		var url = link1
		rightGrid.changeToListDS(url);
		rightGrid.getStore().reload();
	};
	SearchPanel.superclass.constructor.call(this, config);
};
Ext.extend(SearchPanel, Ext.app.SearchPanel, {
	appendSearchField : function(searchField, bHide) {
		var searchPanel = this;
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
		if (sType == "dateField") {
			oSearchField = new Ext.form.DateField({
				fieldLabel : sLabel,
				format : 'Y-m-d',
				width : this.txtWidth,
				id : sId
			});
		} else if (sType == "comboBoxPfl") {
			var tmp = searchField.split("#");
			var displayField = tmp[3];
			var valueField = tmp[4];
			var emptyText = tmp[5];
			var distsLp = $dictListAjax("LP_PZ2TABLE");
			var dsLp = new Ext.data.Store({
				reader : new Ext.data.JsonReader({
					root : 'rows',
					id : 'id'
				}, ['key', 'value', 'id']),
				data : {
					rows : distsLp
				}
			})
			oSearchField = new Ext.form.ComboBox({
				id : sId,
				fieldLabel : sLabel,
				store : dsLp,
				displayField : "key",
				valueField : "value",
				mode : 'local',
				triggerAction : 'all',
				selectOnFocus : true
			});
		} else if (sType == "comboBoxBz") {
			var tmp = searchField.split("#");
			var displayField = tmp[3];
			var valueField = tmp[4];
			var emptyText = tmp[5];
			oSearchField = new Ext.app.ComboTree({
				id : sId,
				fieldLabel : sLabel,
				dataUrl : link2,
				localData : false,
				triggerClass : 'comboTree',
				listeners : {
					select : function(t, node) {
						if (!node.attributes.leaf) {
							alert("请选择部门下面的班组！")
							t.setValue('');
							return ;
						}
					}
				}
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