/**
 * 作者:	sds
 * 描述：用于填写唯一关键字的文本框，向服务器发送请求并
 * 		却将输入的文本作为参数传入服务器端，服务器返回
 * 		该关键字是否唯一，且作出不同的反映
 * 		RequestURL:@url?value=@value
 * 		Response:	{success:true,unique:true|false}
 * 日期: 2007-12-12
 */
 
/**
 * config options
 * include origenal textfield config options
 * 
 * @url	请求服务器的链接
 * @notUniqueText 当关键字不唯一的时候显示的提示信息 默认为'该值已经被使用，请使用其他值'
 * 
 */
Ext.app.UniqueTextField=function(config){
	config.notUniqueText?this.notUniqueText=config.notUniqueText:this.notUniqueText='该值已经被使用，请使用其他值';
	config.url?this.url=config.url:this.url='';
	Ext.app.UniqueTextField.superclass.constructor.call(this,config);
	this.unique=false;
}

Ext.extend(Ext.app.UniqueTextField, Ext.form.TextField, {
	/**
	 * 验证是否输入的值是唯一
	 */
	showUniqueIcon:function(){
		if(!this.uniqueIcon){
			var ulp=this.el.findParent('.x-form-element',5,true);
			this.uniqueIcon=ulp.createChild({cls:'x-form-ok-icon'});			
		}
		this.uniqueIcon.show();
		this.uniqueIcon.alignTo(this.el, 'tl-tr', [2, 0]);
	},
	/**
	 * 显示不唯一图标
	 */
	showNotUniqueIcon:function(){
		if(!this.errorIcon){
            var elp = this.el.findParent('.x-form-element', 5, true);
            this.errorIcon = elp.createChild({cls:'x-form-invalid-icon'});
        }
        this.alignErrorIcon();
		this.hideUniqueIcon();
		this.errorIcon.dom.qtip = this.notUniqueText;
        this.errorIcon.dom.qclass = 'x-form-invalid-tip';
        this.errorIcon.show();
	},
	/**
	 * 隐藏唯一图标
	 */
	hideUniqueIcon:function(){
		if(this.uniqueIcon)
			this.uniqueIcon.hide();
	},
    onBlur : function(param1,param2,param3){
    	Ext.app.UniqueTextField.superclass.onBlur.call(this,param1,param2,param3);
 		if(this.readOnly)
 			return;
    	var textField=this;
    	if(!this.isValid()){
    		this.hideUniqueIcon();
		}else{
			if(this.url && this.url!=''){
				var name=this.getName();
				name=(name!='undefined'&& name.length>0)?name:"value";
				eval("var param = {"+name+":'"+this.getValue()+"'}");
				Ext.Ajax.request({
					url:this.url,
					method:'POST',
					params:param,
					success:function(ajax){
						var responseText=ajax.responseText;	
						
						textField.showIcon(responseText.indexOf("unique:true")>=0);
					},
					failure:function(){
						alert("验证失败，服务器忙");
					}
				});
			}else{
				alert("未正确配置UniqueTextField的url参数");
			}
		}
    },
    //private
    showIcon:function(unique){
    	this.unique=unique;
    	if(unique)//如果唯一
    		this.showUniqueIcon();
		else{//不唯一，显示错误信息
			this.showNotUniqueIcon();
		}
    },
    /**
     * 覆盖父类的同名方法，由于父类支持延时验证，将该功能去掉
     */
    filterValidation:function(){
    },
    /**
     * 重置的时候，清除图标
     */
    reset:function(){
    	Ext.app.UniqueTextField.superclass.reset.call(this);
    	this.hideUniqueIcon();
    },
    /**
     * 覆盖父类的同名方法，验证的时候，如果不唯一，则验证失败，否则调用父类的验证方法进行其他验证
     */
    validate:function(){
    	if(this.readOnly)
    		return true;
    	var bResult=this.unique; 	
    	if(!bResult)
    		return false;
    	else{
    		return Ext.app.UniqueTextField.superclass.validate.call(this);
    	}
    }
});
Ext.reg('uniquetextfield', Ext.app.UniqueTextField);