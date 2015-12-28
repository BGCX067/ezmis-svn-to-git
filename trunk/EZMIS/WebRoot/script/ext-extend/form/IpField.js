/**
 * @class Ext.ux.form.TimeField
 * @extends Ext.ux.form.FieldPanel
 * This class creates a time field using spinners.
 * @license: BSD
 * @author: Robert B. Williams (extjs id: vtswingkid)
 * @constructor
 * Creates a new FieldPanel
 * @param {Object} config Configuration options
 */
Ext.namespace("Ext.ux.form");
Ext.ux.form.IpField = Ext.extend(Ext.ux.form.FieldPanel, {
	border: false, 
	baseCls: null,
	layout: 'table',
	token: '.',
	value: '192.168.0.1',
	layoutConfig: {
		columns: 7
	},
	width: 180,
	// private
	defaults:{
		maskRe: /[0-9]/,
		maxLength: 3,
		listeners: {
			'focus': function(f){
				f.selectText();
			}
		}
	},
	    // private
    // Subclasses should provide the validation implementation by overriding this
    validateValue : function(value){
    	var pattern=/^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/;
    	return pattern.test(value);
    },
	initComponent: function()
	{
		this.items = [{
			xtype:'numberfield',
			width:40,
			name: this.name + '0'
		}, {
			html: '.',
			baseCls: null,
			border: false
		}, {
			xtype:'numberfield',
			width:40,
			name: this.name + '1'
		}, {
			html: '.',
			baseCls: null,
			border: false
		}, {
			xtype:'numberfield',
			width:40,
			name: this.name + '2'
		}, {
			html: '.',
			baseCls: null,
			border: false
		}, {
			xtype:'numberfield',
			width:40,
			name: this.name + '3'
		}]
		Ext.ux.form.IpField.superclass.initComponent.call(this);
	}
});
Ext.reg('uxipfield', Ext.ux.form.IpField);
