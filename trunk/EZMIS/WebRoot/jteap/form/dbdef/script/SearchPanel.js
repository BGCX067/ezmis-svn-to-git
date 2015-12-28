/**
 * 查询面板
 */
Ext.form.Field.prototype.msgTarget = 'side';
SearchPanel = function(config) {
	//点击查询后的事件
	this.searchClick=function(){
		
	};  
	
	var pConfig={title: '查询面板',
		region:'north',
	    width: 600,
	    frame:true,
	    bodyStyle:'padding:5px',
	    fitToFrame:true,
	    autoHeight :true,
	    split:true,
		collapsible: true,
		collapsed:false,
    	items:[{
    		layout:'column',
    		labelSeparator:' ：'
    	}],
    	
    	bbar:[{text:'添加条件',cls: 'x-btn-text-icon',
       		icon:'icon/icon_9.gif',menu:this.searchItemMenu},{text:'查询',        cls: 'x-btn-text-icon',
       		icon:'icon/icon_8.gif',listeners:{click:this.searchClick}
       	}]
    };
    Ext.apply(pConfig,config);
    SearchPanel.superclass.constructor.call(this, pConfig);
};
Ext.extend(SearchPanel, Ext.FormPanel, {
});