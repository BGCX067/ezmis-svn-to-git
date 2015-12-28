/**
 * 标题面板
 * 用于form中标题显示
 */
Ext.app.TitlePanel=function(config){
	
	if(typeof(config)=='string'){
		config={caption:config};
	}
	Ext.apply(config,{height:30})
	Ext.app.TitlePanel.superclass.constructor.call(this,config);
	
	/**
	 * 渲染的时候，将描述信息添加到panel中
	 */
	this.on("render",function(panel){
		panel.getEl().dom.childNodes[0].childNodes[0].innerHTML="<center><div style='color: #114581;font:bolder 16px 仿宋;line-height: 180%;'>"+panel.caption+"</div></center>";
	})
}

Ext.extend(Ext.app.TitlePanel, Ext.Panel, {});