/**
 * 字段提示面板
 * 用于form中字段填写的提示
 */
Ext.app.LabelPanel=function(config){
	
	if(typeof(config)=='string'){
		config={label:config};
	}
	
	if(config.height==null){
		Ext.apply(config,{height:30})
	}
	
	if(config.style==null){
		Ext.apply(config,{style:'padding-top:5px;color:red;font:10pt 宋体;line-height: 15px;'})
	}
	Ext.app.LabelPanel.superclass.constructor.call(this,config);
	
	/**
	 * 渲染的时候，将描述信息添加到panel中
	 */
	this.on("render",function(panel){
		panel.getEl().dom.childNodes[0].childNodes[0].innerHTML="<span style='"+this.style+"'>"+panel.label+"</span>";
	})
}

Ext.extend(Ext.app.LabelPanel, Ext.Panel, {});