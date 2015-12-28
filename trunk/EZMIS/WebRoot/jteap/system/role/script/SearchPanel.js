/**
 * 查询面板
 */
Ext.form.Field.prototype.msgTarget = 'side';
SearchPanel = function(config) {
	// 点击查询后的事件
	this.searchClick = function() {
		// 获取当前组织节点
		var oNode = roleTree.getSelectionModel().getSelectedNode();
		var whereHeader = "obj.";
		if (!oNode.isRootNode()) {
			whereHeader = "obj.person.";
		}
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
					// 日期字段,就相应的处理为字符串
				if (temp.triggerClass == "x-form-date-trigger") {
					tempValue = formatDate(tempValue, "yyyy-MM-dd");
					queryParamsSql += whereHeader + temp.id.split("#")[1] + "='" + encodeURIComponent(tempValue)
							+ "' and ";
					// 文本字段模糊查询
				} else {
					queryParamsSql += whereHeader + temp.id.split("#")[1] + " like '"
							+ encodeURIComponent("$" + tempValue + "$") + "' and ";
				}
			}
		};
		});

		var url = link18 + "?queryParamsSql=" + queryParamsSql.substring(0, queryParamsSql.length - 5);
		url += "&roleId=" + oNode.id;
		rightGrid.changeToListDS(url);
		rightGrid.getStore().reload();
	};
	// 清空查询条件
	this.clearSearch = function() {
		// 获取当前组织节点
		var oNode = roleTree.getSelectionModel().getSelectedNode();
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
		var url = link18 + "?roleId=" + oNode.id;
		rightGrid.changeToListDS(url);
		rightGrid.getStore().reload();
	};
	SearchPanel.superclass.constructor.call(this, config);
};
Ext.extend(SearchPanel, Ext.app.SearchPanel, {});