MSCombiDY2D = function(config,param) {
	var cfg = {
		layout : 'border',
		bodyStyle:'padding:2px',
		items : [{
			title : '查询面板',
			region : 'north',
			style  :'padding:2px',
			height : 60,
			frame : true,
			fitToFrame:true,
			split : false,
			collapsible : true,
			items : [{
				layout : 'column',
				html : '查询：<select id="year" name="year" onChange="GetResult(this.value)"></select>'
			}]
		}, {
			title : '图表',
			style  :'padding:2px',
			region : 'center',
			xtype : 'panel',
			autoHeight : true,
			frame : true,
			items : {
				html : '<div id="flashcontent"/>'
			}
		}, {
			title : '表格',
			style  :'padding:2px',
			collapsible : true,
			region : 'south',
			frame : true,
			height : 130,
//			autoHeight : true,
			xtype : 'panel',
			autoScroll : false,
			items : {
				html : '<div id="tablecontent"/>'
			}
		}]
	}

	config = Ext.applyIf(config || {}, cfg);

	MSCombiDY2D.superclass.constructor.call(this, config);

	var chart1 = new FusionCharts(contextPath+"/component/chart/Charts/MSCombiDY2D.swf", "chart1Id", document.body.offsetWidth-10, document.body.offsetHeight-230);
	chart1.setDataXML(param.dataXml);
	chart1.setTransparent(true);
	chart1.render("flashcontent");
	
	Ext.getDom("tablecontent").innerHTML = param.table;

	for(var i = 0;i < param.years.length;i++){
		var oOption = document.createElement("OPTION");
		Ext.getDom("year").options.add(oOption);
		oOption.innerText = param.years[i];
		oOption.value = param.years[i];
	}
}

Ext.extend(MSCombiDY2D, Ext.Viewport, {

});
