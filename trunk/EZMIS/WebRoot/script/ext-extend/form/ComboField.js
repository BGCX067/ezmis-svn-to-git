var ds = new Ext.data.Store({
      proxy : new Ext.data.ScriptTagProxy( {
      			method:'post',
				url : contextPath+'/jteap/wz/wzda/WzdaAction!showListAction.do'
	  }),
	  reader : new Ext.data.JsonReader( {
				root : 'list',
				totalProperty : 'totalCount',
				id : 'id'
	  },['id','wzmc','xhgg','jldw','jhdj','pjj',
	  'dqkc','je','yfpsl','dyx','zgcbde','zdcbde','wzlb.wzlbmc',
	  'abcfl','tsfl','kw.cwmc','cskc','tjm','wzmcxh'])
});
Ext.app.SelectBox = function(config){	
	this.searchResetDelay = 1000;
	obj=null;
	config = config || {};
	field = this;
	config = Ext.apply(config || {}, {
		width: 570,
		editable: true,
		forceSelection: false,
//		validateOnBlur:true,
//		forceSelection: false,
//		rowHeight: false,
//		lastSearchTerm: false,
	//	selectOnFocus:true,
        triggerAction: 'query',
        mode: 'remote',
        store:ds,
     //   valueField:'wzmc',
        displayField: 'wzmcxh',
        minChars:'2',
        readOnly:config.readOnly?config.readOnly:false,
        onSelect: function(record,index){ // override default onSelect to do redirect
//        	alert(record.data.wzmc);
        	if(this.fireEvent('beforeselect', this, record, index) !== false){
        		field.obj = record.data;
            	this.setValue(record.data[this.valueField || this.displayField]);
            	this.collapse();
            	this.fireEvent('select', this, record, index);
        	}        	
        }
    /*    listeners:{
		render  : function(field){
			var xx =field.getEl();
			xx.dom.attachEvent("change",function(){
				field.obj = null;//alert("sdf");
			})
		}
	}*/
        //hideTrigger:true
    });

	Ext.app.SelectBox.superclass.constructor.apply(this, arguments);

	this.lastSelectedIndex = this.selectedIndex || 0;
//	field = this;
};




Ext.extend(Ext.app.SelectBox, Ext.form.ComboBox, {

});

Ext.reg('ext_selectBox', Ext.app.SelectBox);