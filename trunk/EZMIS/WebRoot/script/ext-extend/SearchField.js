
/**
 * 用于搜索的文本框
 * 两个按钮,搜索按钮&清除搜索按钮
 */
Ext.app.SearchField=function(config){
	Ext.app.SearchField.superclass.constructor.call(this,config);
	/**
	 * 当查询时调用
	 * @field	:	当前文本框对象
	 * this.on("query",function(Object field));
	 */
	this.addEvents({
        query : false,
        cancel: false
    });
}

Ext.extend(Ext.app.SearchField,Ext.form.TwinTriggerField, {
    initComponent : function(){
		//调用父类的初始化方法
		Ext.app.SearchField.superclass.initComponent.call(this);
		//初始化，添加回车事件
		this.on('specialkey', function(f, e){
            if(e.getKey() == e.ENTER){
                this.onTrigger2Click();	//调用查询事件
            }
        }, this);
    },
    validationEvent:false,
    validateOnBlur:false,
    trigger1Class:'x-form-clear-trigger',
    trigger2Class:'x-form-search-trigger',
    hideTrigger1:true,
    width:80,
    hasSearch : false,
    paramName : 'query',
	emptyText:'输入查询关键字...',
    onTrigger1Click : function(){	//清除查询条件按钮事件
        if(this.hasSearch){
			this.el.dom.value = '';
            this.triggers[0].hide();
            this.hasSearch = false;
			this.focus();
			this.fireEvent("cancel",this);
        }
    },

    onTrigger2Click : function(){	//查询按钮事件
        var v = this.getRawValue();
        if(v.length < 1){
            this.onTrigger1Click();
            return;
        }
		if(v.length < 2){
			Ext.Msg.alert('无效查询', '查询条件必须大于2个字符');
			return;
		}
        this.hasSearch = true;
        this.triggers[0].show();	//显示清除按钮
		this.focus();
		this.fireEvent("query",this);

    }
});