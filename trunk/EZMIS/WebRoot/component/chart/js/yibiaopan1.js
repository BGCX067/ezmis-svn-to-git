Yibiaopan1 = function(config,param) {
	var cfg = {
		layout : 'border',
		bodyStyle:'padding:2px',
		items : [{ 
			title : '当前煤库存量',
			bodyBorder : true,
			style  :'padding:2px',
			region : 'center',
			xtype : 'panel',
			autoHeight : true,
			frame : true,
			items : {
				html : '<div id="flashcontent"/>'
			}
		}]
	}

	config = Ext.applyIf(config || {}, cfg);

	Yibiaopan1.superclass.constructor.call(this, config);

	var chart1 = new FusionCharts(contextPath+"/component/chart/ChartsWidgets/AngularGauge.swf", "chart1Id", document.body.offsetWidth-10, document.body.offsetHeight - 40);
	chart1.setDataXML(param.dataXml);
	chart1.setTransparent(true);
	chart1.render("flashcontent");
}

Ext.extend(Yibiaopan1, Ext.Viewport, {

});
