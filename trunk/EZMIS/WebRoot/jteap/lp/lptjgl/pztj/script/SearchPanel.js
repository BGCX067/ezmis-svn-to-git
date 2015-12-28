
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
						queryParamsSql += " to_char(" + temp.id.split("#")[1] + ",'yyyy-mm-dd')>='" + tempValue
								+ "' and ";
					} else if (temp.id.split("#")[1] == "jhjssj") {
						tempValue = formatDate(tempValue, "yyyy-MM-dd");
						queryParamsSql += " to_char(" + temp.id.split("#")[1] + ",'yyyy-mm-dd')<='" + tempValue
								+ "' and ";
					}
				}
			};
		});
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
		var url = link1
		rightGrid.changeToListDS(url);
		rightGrid.getStore().reload();
	};
	SearchPanel.superclass.constructor.call(this, config);
};
Ext.extend(SearchPanel, Ext.app.SearchPanel, {});