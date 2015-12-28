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
								queryParamsSql += " obj.qsrq >= to_date('" + tempValue + "','yyyy-MM-dd') and ";
							} else if (temp.id.split("#")[1] == "endDate") {
								tempValue = formatDate(new Date(tempValue), "yyyy-MM-dd");
								queryParamsSql += " obj.jsrq <= to_date('" + tempValue + "','yyyy-MM-dd') and ";
							} else if (temp.id.split("#")[1] == "jxxz") {
								queryParamsSql += " obj.jxxz ='"+encodeURIComponent(tempValue) + "' and ";
							} else if (temp.id.split("#")[1] == "jz") {
								tempValue = tempValue.replace("#", "@");
								queryParamsSql += " obj.jz = '" + encodeURIComponent(tempValue) + "' and ";
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
		var url = link1;
		rightGrid.changeToListDS(url);
		rightGrid.getStore().reload();
	};
	var pConfig = {
		title : '查询面板',
		region : 'north',
		width : 600,
		frame : true,
		margins : '1px 1px 1px 1px',
		bodyStyle : 'padding:5px',
		fitToFrame : true,
		autoHeight : true,
		split : true,
		collapsible : true,
		items : [{
					layout : 'column',
					labelSeparator : ' ：'
				}],

		bbar : [{
					text : '查询',
					cls : 'x-btn-text-icon',
					icon : 'icon/icon_8.gif',
					listeners : {
						click : this.searchClick ? this.searchClick : Ext.emptyFn
					}
				}, {
					text : '清除条件',
					cls : 'x-btn-text-icon',
					icon : 'icon/trash-delete.gif',
					listeners : {
						click : this.clearSearch ? this.clearSearch : Ext.emptyFn
					}
				}]
	};
	Ext.apply(pConfig, config);
	SearchPanel.superclass.constructor.call(this, pConfig);
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
				} else if (sType == "dateFieldShowTime") {
					oSearchField = new Ext.form.DateField({
								fieldLabel : sLabel,
								format : 'Y-m-d H:i',
								width : this.txtWidth,
								id : sId,
								menu : new DatetimeMenu()
							});
				} else if (sType == "comboBoxXz") {
					var dict_jxxz = $dictList("JXXZ");
					// 缺陷专业数据源
					var storeXz = new Ext.data.Store({
								data : {
									rows : dict_jxxz
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
								store : storeXz,
								valueField : 'value',
								displayField : 'key',
								triggerAction : 'all',
								mode : 'local'
							})
				} else if (sType == "comboBoxJz") {
					var dict_jz = $dictList("JXJZ");
					// 缺陷专业数据源
					var storeJz = new Ext.data.Store({
								data : {
									rows : dict_jz
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