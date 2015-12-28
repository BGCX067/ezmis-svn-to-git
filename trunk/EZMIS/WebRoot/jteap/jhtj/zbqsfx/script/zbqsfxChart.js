ZbqsfxChart = function(config,param) {
	
	var cfg = {
		layout : 'border',
		bodyStyle:'padding:2px',
		items : [searchPanel, {
			title : '图表',
			bodyBorder : true,
			style  :'padding:2px',
			region : 'center',
			xtype : 'panel',
			frame : true,
			items : {
				html : '<div id="flashcontent"></div>'
			}
		}, {
			title : '表格',
			collapsible : true,
			region : 'south',
			style  :'padding:2px',
			frame : true,
			autoScroll :true,
			name:'table',
			height : 120,
			xtype : 'panel',
			items : {
				html : '<div id="tablecontent" style="overflow-x:auto;overflow-y:auto;"></div>'
			}
		}]
	}
	

	config = Ext.applyIf(config || {}, cfg);

	ZbqsfxChart.superclass.constructor.call(this, config);
}

Ext.extend(ZbqsfxChart, Ext.Viewport, {

});
