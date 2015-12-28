/*
 * Ext JS Library 2.2
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

/**
 * @class Ext.form.Hidden
 * @extends Ext.form.Field
 * A basic hidden field for storing hidden values in forms that need to be passed in the form submit.
 * @constructor
 * Create a new Hidden field.
 * @param {Object} config Configuration options
 */
Ext.app.PopTextAreaField = Ext.extend(Ext.form.Hidden, {
    // private
    onRender : function(){
        Ext.app.PopTextAreaField.superclass.onRender.apply(this, arguments);
        this._value = null;
        this.addEvents('close'); 
    },
    setTxtValue : function(value){
    	this._value = $escapeHtml(value);
    },
    getTxtValue : function(){
    	return $unesacpeHtml(this._value);
    },
    showPop:function(){
    	var pop = this;
    	pop.ok = false;
    	var txtEditParam = new Ext.form.TextArea({
			name: 'txtEditParam',
			labelSeparator: '',
			region:'center',
			height: 200,
			value:pop._value
		});
		
		var simpleForm = new Ext.FormPanel({
			buttonAlign: 'right',
			style: 'margin:2px',
			bodyStyle:'padding:0px',
			border:false,
			region:'center',
			layout:'border',
		    width: '100%',
		    labelWidth: 70,					//标签宽度
			waitMsgTarget: true,
		    frame: false, 					//圆角风格
		    items: [txtEditParam],
		    buttons: [{
		    	text: '确定',
		    	handler: function(){
		    		pop.ok = true;
		    		var value=txtEditParam.getValue();
		    		pop.setTxtValue(value);
		    		editWindow.close();
		    	}
		    },{
		    	text: '取消',
		    	handler: function(){
		    		editWindow.close();
		    	}
		    }]
		});
		
		var editWindow = new Ext.Window({
			title: '属性设置',
	        region : 'center',
	        layout: 'border',
	        width: 450,
	        height: 350,
	        modal: true,
	        plain: true,
	        maximizable :true,
	        draggable: true,
	        resizable: true,
	        items: [simpleForm]
		});
		
		editWindow.on("close",function(){
			pop.fireEvent("close");
		});
		editWindow.show();
    }
});
Ext.reg('poptextarea', Ext.app.PopTextAreaField);

