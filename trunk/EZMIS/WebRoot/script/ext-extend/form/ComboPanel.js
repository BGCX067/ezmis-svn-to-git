Ext.app.ComboPanel = Ext.extend(Ext.form.TriggerField, {
	shadow : 'frame',
	maxHeight : 300,
	hideTrigger : false,
	resizable : true,
	minListWidth : 70,
	handleHeight : 8,
	editable : false,
	lazyInit : true,
	hiddenValue : '',

	initComponent : function() {
		Ext.app.ComboPanel.superclass.initComponent.call(this);
		this.addEvents('expend', 'collapse', 'select', 'click');
	},

	onRender : function(ct, position) {
		Ext.app.ComboPanel.superclass.onRender.call(this, ct, position);

		this.el.dom.setAttribute('readOnly', true);
		this.el.on('mousedown', this.onTriggerClick, this);
		this.el.addClass('x-combo-noedit');
		this.initPanelLayer();
	},
	initPanelLayer : function() {
		cg = this;

		if (!this.panelLayer) {
			var cls = 'x-combo-list';

			this.list = new Ext.Layer({
						shadow : this.shadow,
						cls : [cls].join(' '),
						constrain : false
					});

			var lw = this.listWidth
					|| Math.max(this.wrap.getWidth() - this.trigger.getWidth(),
							this.minListWidth);
			this.list.setWidth(100);
			this.list.swallowEvent('mousewheel');
			this.assetHeight = 0;

			var store = new Ext.data.SimpleStore({
						fields : ["value", "url"],
						data : [['1', 'icon/121.png'], ['2', 'icon/21.png'],
								['3', 'icon/12.png'], ['4', 'icon/11.png']]
					})

			var tpl = new Ext.XTemplate(
					'<tpl for=".">',
					'<div id="{value}" class="thumb-wrap" style="float:left;padding:1px;">',
					'<img src="{url}"></div>', '</tpl>');

			var comboPanelx = this;
			var dataView = new Ext.DataView({
						store : store,
						tpl : tpl,
						style : 'overflow:auto',
						multiSelect : true,
						itemSelector : 'div.thumb-wrap',
						listeners : {
							'click' : function(dataview, index, node, e) {
								var record = dataView.getRecord(node);
								comboPanelx.onImgClick(record.data.value);
							}
						}
					})

			this.panel = new Ext.Panel({
						applyTo : this.list,
						height : this.initialConfig.panelHeight,
						width : this.initialConfig.panelWidth,
						layout : 'fit',
						items : [dataView]
					});
		}
	},
	onTriggerClick : function() {
		if (this.disabled) {
			return;
		}
		if (this.isExpanded()) {
			this.collapse();
			this.el.focus();
		} else {
			this.onFocus({});
			this.expand();
			this.el.focus();
		}
	},
	onImgClick : function(value) {
		var record = value;
		this.onSelect(record);
		this.list.hide();
		this.el.focus();
		this.fireEvent('click', this, true);
	},
	onSelect : function(record) {
		this.fireEvent('select', this, record);
	},
	isExpanded : function() {
		return this.list && this.list.isVisible();
	},

	expand : function() {
		if (this.isExpanded() || !this.hasFocus) {
			return;
		}
		this.list.alignTo(this.wrap, this.listAlign);
		this.list.show();
		Ext.getDoc().on('mousewheel', this.collapseIf, this);
		Ext.getDoc().on('mousedown', this.collapseIf, this);
		this.fireEvent('expand', this);
	},

	collapse : function() {
		if (!this.isExpanded()) {
			return;
		}
		this.list.hide();
		Ext.getDoc().un('mousewheel', this.collapseIf, this);
		Ext.getDoc().un('mousedown', this.collapseIf, this);
		this.fireEvent('collapse', this);
	},

	collapseIf : function(e) {
		if (!e.within(this.wrap) && !e.within(this.list)) {
			this.collapse();
		}
	},
	clearValue : function() {
		if (this.hiddenField) {
			this.hiddenField.value = '';
		}
		this.setValue('');
		this.applyEmptyText();
	},
	onDestroy : function() {

		if (this.panel) {
			this.panel.el.removeAllListeners();
			this.panel.el.remove();
			this.panel.purgeListeners();
		}
		if (this.panel) {
			this.panel.destroy();
		}
		Ext.app.ComboPanel.superclass.onDestroy.call(this);
	},
	getValue : function() {
		if (this.valueField) {
			return typeof this.value != 'undefined' ? this.value : '';
		} else {
			return Ext.app.ComboPanel.superclass.getValue.call(this);
		}
	},
	getHiddenValue : function() {
		return this.hiddenValue;
	}

});

Ext.reg('combopanel', Ext.app.ComboPanel);