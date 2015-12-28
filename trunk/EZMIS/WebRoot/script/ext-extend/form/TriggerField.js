Ext.app.TriggerField = function(config){
	displayValue='';
	obj={};
	if(config!=null){
		if(config.onTriggerClick){
			this.onTriggerClick = config.onTriggerClick;
		}
		if(config.change){
			this.change = config.change;
		}
	}
	var pConfig = {};
	Ext.apply(pConfig,config);
    Ext.app.TriggerField.superclass.constructor.call(this,pConfig);
}


Ext.extend(Ext.app.TriggerField, Ext.form.TriggerField, {
	
});

Ext.reg('ext_triggerfield', Ext.app.TriggerField);