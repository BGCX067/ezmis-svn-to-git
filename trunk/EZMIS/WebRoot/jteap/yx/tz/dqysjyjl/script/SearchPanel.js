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
					if (temp.id.split("#")[1] == "jysj1") {
						tempValue = formatDate(tempValue, "yyyy-MM-dd");
						queryParamsSql += "to_char(obj.jysj,'yyyy-mm-dd') >= '" + tempValue + "' and ";
					} else if (temp.id.split("#")[1] == "jysj2") {
						tempValue = formatDate(tempValue, "yyyy-MM-dd");
						queryParamsSql += "to_char(obj.jysj,'yyyy-mm-dd') <= '" + tempValue + "' and ";
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
Ext.extend(SearchPanel, Ext.app.SearchPanel, {});