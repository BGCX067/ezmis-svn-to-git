
/**
 * 查询面板
 */
SearchPanel = function(config) {
	Ext.apply(config, {
		txtWidth : 150
	});
	// 点击查询后的事件
	this.searchClick = function() {
		if (Ext.getCmp("sf#jhjssj").getValue() < Ext.getCmp("sf#jhkssj").getValue()) {
			alert('开始时间不能大于结束时间！');
			return;
		}
		var tableName = "";
		var tableCname = "";
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
						queryParamsSql += "to_char(a." + temp.id.split("#")[1] + ",'yyyy-mm-dd')>='" + tempValue + "' and ";
					} else if (temp.id.split("#")[1] == "jhjssj") {
						tempValue = formatDate(tempValue, "yyyy-MM-dd");
						queryParamsSql += "to_char(a." + temp.id.split("#")[1] + ",'yyyy-mm-dd')<='" + tempValue + "' and ";
					} else if (temp.id.split("#")[1] == "ph") {
						queryParamsSql += "a." + temp.id.split("#")[1] + " like '" + encodeURIComponent("%" + tempValue + "%") + "' and ";
					} else if (temp.id.split("#")[1] == "bz") {
						queryParamsSql += "a.BZID = '" + temp.hideMode + "' and ";
					} else if (temp.id.split("#")[1] == "pfl") {
						tableName = tempValue;
						tableCname = temp.getRawValue();
					}
				}
			};
		});
		var oNode = leftTree.getSelectionModel().getSelectedNode();
		var url = link1 + "?cxid=" + oNode.id + "&tableName=" + tableName + "&tableCname=" + tableCname
				+ "&queryParamsSql=" + queryParamsSql.substring(0, queryParamsSql.length - 5);
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
		var oNode = leftTree.getSelectionModel().getSelectedNode();
		var url = link1 + "?cxid=" + oNode.id
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
		if (sType == "textField") {
			oSearchField = new Ext.form.TextField({
				fieldLabel : sLabel,
				width : this.txtWidth,
				id : sId
			});
			// 日期类型
	} else if (sType == "dateField") {
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
			dataUrl : link2,
			width : 160,
			hiddenName : sId,
			fieldLabel : sLabel,
			listWidth : 300,
			listHeight : 80,
			triggerClass : 'comboTree',
			localData : false,
			rootVisible : false,
			readOnly : true,
			listeners : {
				select : function(t, node) {
					if (!node.leaf) {
						t.setValue('');
						alert('只能选择班组！')
						return;
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