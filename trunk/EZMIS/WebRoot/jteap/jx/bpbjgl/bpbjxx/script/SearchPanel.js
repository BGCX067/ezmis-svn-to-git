/**
 * 查询面板
 */
Ext.form.Field.prototype.msgTarget = 'side';
SearchPanel = function(config) {
	// 点击查询后的事件
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
							if (temp.id.split("#")[1] == "startDate") {
								tempValue = formatDate(new Date(tempValue), "yyyy-MM-dd");
								queryParamsSql += " obj.jlsj >= to_date('" + tempValue + "','yyyy-MM-dd') and ";
							} else if (temp.id.split("#")[1] == "endDate") {
								tempValue = formatDate(new Date(tempValue), "yyyy-MM-dd");
								queryParamsSql += " obj.jlsj <= to_date('" + tempValue + "','yyyy-MM-dd') and ";
							}
						}
					};
				});
		var oNode = leftTree.getSelectionModel().getSelectedNode();
		var jhId = "";
		if (oNode) {
			jhId = oNode.id;
			if (jhId.lastIndexOf("@") != -1) {
				return;
			}

		}
		var url = link2 + "?queryParamsSql=" + queryParamsSql.substring(0,queryParamsSql.length-5) + "&jhId=" + jhId;
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
		var jhId = "";
		if (oNode)
			jhId = oNode.id;
		if (jhId.lastIndexOf("@") != -1) {
			return;
		}
		var url = link2 + "?jhId=" + jhId;
		rightGrid.changeToListDS(url);
		rightGrid.getStore().reload();
	};

	SearchPanel.superclass.constructor.call(this, config);
};
Ext.extend(SearchPanel, Ext.app.SearchPanel, {});