/**
 * 字段提示面板
 * 用于form中字段填写的提示
 */
Ext.app.LabelValuePanel=function(config){
	
	
	config.label?void(0):config.label='Label';
	config.value?void(0):config.value='Value';

	config.width?void(0):config.width=400;	
	config.height?void(0):config.height=30;
	
	Ext.app.LabelValuePanel.superclass.constructor.call(this,config);
	
	/**
	 * 渲染的时候，将描述信息添加到panel中
	 */
	this.on("render",function(panel){
		var str="<span style='color: #114581;font:bolder 12px 仿宋;line-height: 180%;'>"+panel.label+"</span>";
		str+="<span style='color:black;font:12px 宋体'>"+this.value+"</span>";
		panel.getEl().dom.childNodes[0].childNodes[0].innerHTML=str;
	});
	
}

Ext.extend(Ext.app.LabelValuePanel, Ext.Panel, {
	/**
	 * 设置值
	 */
	setValue:function(value){
		var str="<span style='color: #114581;font:bolder 12px 仿宋;line-height: 180%;'>"+this.label+"</span>";
		str+="<span style='color:black;font:12px 宋体'>"+value+"</span>";
		this.getEl().dom.childNodes[0].childNodes[0].innerHTML=str;
		this.value=value;
		this.render();
	}
});