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
					if (temp.id.split("#")[1] == "SBMC") {
						queryParamsSql += " a." + temp.id.split("#")[1] + " like '"
								+ encodeURIComponent("%" + tempValue + "%") + "' and ";
					} else if (temp.id.split("#")[1] == "QXMC") {
						queryParamsSql += " a." + temp.id.split("#")[1] + " like '"
								+ encodeURIComponent("%" + tempValue + "%") + "' and ";
					} else if (temp.id.split("#")[1] == "ZRBM") {
						queryParamsSql += " a." + temp.id.split("#")[1] + " = '" + tempValue + "' and ";
					} else if (temp.id.split("#")[1] == "QXFL") {
						queryParamsSql += " a." + temp.id.split("#")[1] + " = '" + encodeURIComponent(tempValue)
								+ "' and ";
					} else if (temp.id.split("#")[1] == "startDate") {
						tempValue = formatDate(new Date(tempValue), "yyyy-MM-dd hh:mm");
						queryParamsSql += " a.fxsj" + " > to_date('" + tempValue + "','yyyy-MM-dd hh24:mi') and ";
					} else if (temp.id.split("#")[1] == "endDate") {
						tempValue = formatDate(new Date(tempValue), "yyyy-MM-dd hh:mm");
						queryParamsSql += " a.fxsj" + " < to_date('" + tempValue + "','yyyy-MM-dd hh24:mi') and ";
					} else if (temp.id.split("#")[1] == "QXDBH") {
						queryParamsSql += " a." + temp.id.split("#")[1] + " like '"
								+ encodeURIComponent("%" + tempValue + "%") + "' and ";
					} else if (temp.id.split("#")[1] == "FXR") {
						queryParamsSql += " a." + temp.id.split("#")[1] + " like '"
								+ encodeURIComponent("%" + tempValue + "%") + "' and ";
					} else if (temp.id.split("#")[1] == "JZBH") {
						queryParamsSql += " a." + temp.id.split("#")[1] + " like '"
								+ encodeURIComponent("%" + tempValue + "%") + "' and ";
					}else if (temp.id.split("#")[1] == "FXBM") {
						queryParamsSql += " a." + temp.id.split("#")[1] + " like '"
								+ encodeURIComponent("%" + tempValue + "%") + "' and ";
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
			icon : 'icon/icon_9.gif',
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
	/**
	 * 初始化查询面板
	 */
	initSearchFieldPanel : function() {
		var thisx = this;
		Ext.each(this.searchAllFs, function(f) {
			var bTmp = true;

			if (thisx.searchDefaultFs.indexOf(f) >= 0)
				bTmp = false;
			thisx.appendSearchField(f, bTmp);
		});

	},
	/**
	 * 初始化条件菜单 菜单相的编号以'mi_'为前缀+查询字段的ID
	 */
	initSearchFieldMenu : function() {
		var menu = this.searchItemMenu;
		var thisx = this;
		Ext.each(this.searchAllFs, function(f) {
			var sLabel = f;
			var sId = 'mi#' + f;
			if (f.indexOf("#") >= 0) {
				var tmp = f.split("#");
				sLabel = tmp[0];
				sId = 'mi#' + tmp[1];
			}

			var bChecked = thisx.searchDefaultFs.indexOf(f) >= 0;
			// 添加菜单项
				menu.add({
					id : sId,
					text : sLabel,
					checked : bChecked,
					// 菜单项点击事件
						checkHandler : function(oItem, bChecked) {
							var oPanel = thisx.items.get(0);
							var sId = oItem.id.split("#")[1];
							var oItem = oPanel.items.get("SFP#sf#" + sId);
							if (!bChecked) {
								oItem.hide();
							} else {
								oItem.show();
							}
							var oParent = thisx.ownerCt;
							var oLayout = oParent.getLayout();
							oLayout.layout();

						}
					});
			})
	},
	/**
	 * 向查询面板中追加一个查询字段
	 * 
	 * @searchField (String) 查询字段的名称,查询字段名称可以是具有一定格式的规则字符串
	 * @bHide (boolean) 确定是否添加是是以隐藏状态隐藏的
	 * 
	 * eg：'姓名_name' 将会生成标签为“姓名”，ID为"sf_name"的文本框，其中"sf_"为查询字段的前缀
	 * 
	 * eg: '姓名' 将会生成标签为“姓名”，ID为"sf_姓名"的文本框
	 */
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
	} else if (sType == "dateFieldShowTime") {
		oSearchField = new Ext.form.DateField({
			fieldLabel : sLabel,
			format : 'Y-m-d H:i',
			width : this.txtWidth,
			readOnly : true,
			id : sId,
			menu : new DatetimeMenu(),
			listeners : {
				change : function() {
					if (Ext.getCmp('sf#startDate').getValue() && Ext.getCmp('sf#endDate').getValue()
							&& Ext.getCmp('sf#startDate').getValue() > Ext.getCmp('sf#endDate').getValue()) {
						alert('查询开始时间不能大于结束时间');
						Ext.getCmp('sf#startDate').setValue('');
						Ext.getCmp('sf#endDate').setValue('');
					}
				}
			}
		});
	} else if (sType == "comboBoxZr") {
		var dict = $dictList("JX_QXD_ZRBM");
		// 缺陷专业数据源
		var storeZY = new Ext.data.Store({
			data : {
				rows : dict
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
			store : storeZY,
			valueField : 'value',
			displayField : 'key',
			triggerAction : 'all',
			mode : 'local'
		})
	} else if (sType == "comboBoxFl") {
		var dict_qxfl = $dictList("QXFL");
		// 缺陷专业数据源
		var storeFL = new Ext.data.Store({
			data : {
				rows : dict_qxfl
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
			store : storeFL,
			valueField : 'key',
			displayField : 'value',
			triggerAction : 'all',
			mode : 'local'
		})
	} else if (sType == "CobomBoxJZ") {
		var dict_Jz= $dictList("jizu");
		var storeJz = new Ext.data.Store({
			data : {
				rows : dict_Jz
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
			valueField : 'key',
			displayField : 'value',
			triggerAction : 'all',
			mode : 'local'
		})
	} else if (sType == "comboBoxFxbm") {
		var tmp = searchField.split("#");
		var displayField = tmp[3];
		var valueField = tmp[4];
		var emptyText = tmp[5];
		oSearchField = new Ext.app.ComboTree({
			id : sId,
			dataUrl : link4,
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
		items : [oSearchField]
	});

	oPanel.add(tmpPanel);
	if (typeof(bHide) == 'boolean' && bHide == true) {
		tmpPanel.hide();
	}

},
/**
 * 在查询面板对象中查找出指定名称的searchFeidl对象
 * 
 * @sField :查询字段的名称 如果是以"Label_FieldId"的格式生成的查询字段，此处应传入FieldId
 *         如果是以"Label"的格式生成的查询字段，此处直接传入Label
 */
findSearchField : function(sField) {
	var oPanel = this.items.get(0);
	var oItem = oPanel.items.find(function(oItem) {
		if (oItem.items.items[0].id == "sf#" + sField)
			return true;
	});
	return oItem;
}

});