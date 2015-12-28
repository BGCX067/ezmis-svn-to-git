/**
 * 编辑属性窗口
 */
var PropertyManyWindow = function(value,initFn){
	
	var editWindow = this;
	this.ok = false;
	
	var txtEditParam = new Ext.form.TextArea({
		name: 'txtEditParam',
		labelSeparator: '',
		region:'center',
		height: 320,
		listeners: { 
			render: function(){
				initFn(value,txtEditParam);
			}
		}
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
	    	text: '保存',
	    	handler: function(){
	    		editWindow.ok = true;
	    		editWindow.close();
	    	}
	    },{
	    	text: '取消',
	    	handler: function(){
	    		editWindow.close();
	    	}
	    }]
	});
	
	PropertyManyWindow.superclass.constructor.call(this,{
        title: '属性设置',
        region : 'center',
        layout: 'border',
        width: 600,
        height: 550,
        modal: true,
        plain: true,
        maximizable :true,
        draggable: true,
        resizable: true,
        items: [simpleForm]
	 })
}

Ext.extend(PropertyManyWindow, Ext.Window, {
	
	getValue: function(){
		var editParam = this.find("name","txtEditParam")[0];
		return editParam.getValue();
	}
	
})