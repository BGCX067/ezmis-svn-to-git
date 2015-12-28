
/**
 * 用于确认输入的文本框
 * 两个按钮,取消按钮&确认按钮
 */
Ext.app.ConfirmTextField=function(config){
	Ext.app.ConfirmTextField.superclass.constructor.call(this,config);
    this.addEvents({
    	/**
    	 * 当确认输入时触发
    	 * @field	:	当前文本框对象
		 * this.on("confirmInput",function(Object field));
    	 */
    	confirmInput:true,
    	/**
    	 * 当取消输入时触发
    	 * @field	:	当前文本框对象
		 * this.on("cancelInput",function(Object field));
    	 */
    	cancelInput:true
    });
}

Ext.extend(Ext.app.ConfirmTextField,Ext.form.TwinTriggerField, {
    initComponent : function(){
		//调用父类的初始化方法
		Ext.app.ConfirmTextField.superclass.initComponent.call(this);
		//初始化，添加回车事件
		this.on('specialkey', function(f, e){
			
            if(e.getKey() == e.ENTER){
                this.onTrigger2Click();	//调用确认事件
            }
        }, this);
    },
    validationEvent:false,
    validateOnBlur:false,
    trigger1Class:'x-form-clear-trigger',
    trigger2Class:'x-form-confirm-trigger',
//    hideTrigger1:true,
    width:80,
	emptyText:'新增节点',
    onTrigger1Click : function(){	//取消按钮
    	this.fireEvent("cancelInput",this);
    	
    },

    onTrigger2Click : function(){	//确认按钮事件
		this.fireEvent("confirmInput",this);
    }
});