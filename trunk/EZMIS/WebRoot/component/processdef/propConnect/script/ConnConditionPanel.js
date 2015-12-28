ConnConditionPanel = function() {

	var connConditionPanel = this ;
	
	this.chkIsUse = new Ext.form.Checkbox({boxLabel:'使用条件判断',labelSeparator:'' ,inputValue : '1' ,checked : getLabelValue('chkIsUse',cell)=="0"?false:true,listeners : {
			"check":function(combox,checked ){
				btnConnMake.setDisabled(!checked) ;
				if(!checked) {
					connConditionPanel.textarea.setValue("") ;
				}
			}
		}})
	 ;

	this.chkIsDefault = new Ext.form.Checkbox({boxLabel:'默认路由',labelSeparator:'',inputValue : '1' ,checked : getLabelValue('chkIsDefault',cell)=="0"?false:true}) ;
	
	this.textarea = new Ext.form.TextArea({
					width : 350,
					height : 200 ,
					readOnly : true ,
					value : getLabelValue('connCondition',cell)==null?"":decodeChars(getLabelValue('connCondition',cell),"',\",&,<,>")
					//fieldLabel : '路由条件'
	});
	
	var btnConnMake = new Ext.Button({xtype:'button',text:'条件生成器',disabled :true ,handler:function(){
		var returnValue = window.showModalDialog("conditionForm.jsp",[model,cell,rootCell,connConditionPanel.textarea.getValue()],'dialogHeight:435px;dialogWidth:530px;dialogLeft:300px;dialogTop:250px;edge:raised;scroll:1;status:0;help:0;resizable:1;');
		connConditionPanel.textarea.setValue(returnValue)
	}}) ;
	
	var westPanel = {xtype : 'panel' , border : false , html:"<font>路由条件</font>"} ;
	
	
	ConnConditionPanel.superclass.constructor.call(this,{
	})
	
	this.chkIsUse.on("check",function(obj,checked ) {
		this.checked = checked ;
	})
	
	this.chkIsDefault.on("check",function(obj,checked ) {
		this.checked = checked ;
	})
}

Ext.extend(ConnConditionPanel , Ext.Panel , {
	getTextAreaValue : function() {
		return this.textarea.getValue();
	} ,
	getChkIsUseValue : function() {
		return this.chkIsUse.checked?this.chkIsUse.inputValue:"0"
	} ,
	getChkIsDefault : function () {
		return this.chkIsDefault.checked?this.chkIsDefault.inputValue:"0"
	}
})


