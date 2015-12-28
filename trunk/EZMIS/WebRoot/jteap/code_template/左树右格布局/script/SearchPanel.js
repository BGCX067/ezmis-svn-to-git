/**
 * 查询面板
 */
Ext.form.Field.prototype.msgTarget = 'side';
SearchPanel = function(config) {
	var search = this;
	// 点击查询后的事件
	this.searchClick = function() {
		var oPanel = searchtemp.items.get(0);
		var oItems = oPanel.items.items;
		var queryParamsJson = {};
		var queryParamsSql = "";
		var ywName = "";
		var nsrsbh = "";
		Ext.each(oItems, function(oItem) {
					if (oItem.hidden == false) {
						var temp = oItem.items.items[0];

						// 业务分类
						if (temp.id.split("#")[1] == "yw") {
							tempValue = temp.getRawValue();
							ywName = tempValue;
						}

						// 纳税人识别号
						if (temp.id.split("#")[1] == "nsrsbh") {
							tempValue = temp.getValue();
							if (tempValue != null && tempValue != "") {
								nsrsbh = tempValue;
							}
						}
					}
				});
		var url = link1;
		url += "?ywName=" + ywName;
		url += "&nsrsbh=" + nsrsbh;
		rowExpanderEx.changeToListDS(url);
		rowExpanderEx.getStore().reload();
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
					icon : 'icon/icon_1.gif',
					listeners : {
						click : this.searchClick
								? this.searchClick
								: Ext.emptyFn
					}
				}]
	};
	Ext.apply(pConfig, config);

	SearchPanel.superclass.constructor.call(this, pConfig);
};
Ext.extend(SearchPanel, Ext.app.SearchPanel, {
			/**
			 * 覆盖父类方法，添加comboBox类型的查询框
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
				// 数字类型
				if (sType == "numberField") {
					oSearchField = new Ext.form.NumberField({
								fieldLabel : sLabel,
								width : this.txtWidth,
								id : sId,
								anchor : '95%'
							});
				} else if (sType == "comboYWZ") {
					oSearchField = new Ext.form.ComboBox({
								id : sId,
								fieldLabel : sLabel,
								width : this.txtWidth,
								store : new Ext.data.JsonStore({
											url : link2,
											autoLoad : true,
											fields : ['id', 'className']
										}),
								valueField : 'id',
								displayField : 'className',
								triggerAction : 'all',
								mode : 'local',
								editable : true,
								anchor : '95%',
								listeners : {
									select : function(t, record) {
										Ext.getCmp('sf#yw').store.removeAll();
										Ext.getCmp('sf#yw').setValue("");
										Ext.getCmp('sf#yw').store.load({
													params : {
														pid : record.get('id')
													}
												});
									}
								}
							})
				} else if (sType == "comboYW") {
					oSearchField = new Ext.form.ComboBox({
								id : sId,
								fieldLabel : sLabel,
								width : this.txtWidth,
								store : new Ext.data.JsonStore({
											url : link3,
											fields : ['id', 'classname']
										}),
								valueField : 'id',
								displayField : 'classname',
								triggerAction : 'all',
								listWidth : 200,
								mode : 'local',
								editable : true,
								anchor : '95%'
							})
				}
				tmpPanel = new Ext.Panel({
							labelWidth : this.labelWidth,
							layout : 'form',
							columnWidth : .33,
							id : 'SFP#' + sId,
							items : [oSearchField]
						});

				oPanel.add(tmpPanel);
				if (typeof(bHide) == 'boolean' && bHide == true) {
					tmpPanel.hide();
				}
			}
		});
