Column2D = function(config,param) {
	var cfg = {
		layout : 'border',
		bodyStyle:'padding:2px',
		

		items : [{
			title : '查询面板',
			region : 'north',
			height : 62,
			frame : true,
			style  :'padding:2px',
			fitToFrame:true,
			split : false,
			collapsible : true,
			items : [{
				layout : 'column',
				html : '查询：<select id="year" name="year" onChange="GetResult(this.value)"></select>'
			}]
		}, {
			title : '图表',
			bodyBorder : true,
			style  :'padding:2px',
			region : 'center',
			xtype : 'panel',
			autoHeight : true,
			frame : true,
			items : {
				html : '<div id="flashcontent"/>'
			}
		}, {
			fitToFrame:true,
			title : '表格',
			collapsible : true,
			region : 'south',
			style  :'padding:2px',
			frame : true,
			height : 90,
//			autoHeight : true,
			xtype : 'panel',
			autoScroll : false,
			items : {
				html : '<div id="tablecontent"/>'
			}
		}]
	}

	config = Ext.applyIf(config || {}, cfg);

	Column2D.superclass.constructor.call(this, config);

	var chart1 = new FusionCharts(contextPath+"/component/chart/Charts/Column2D.swf", "chart1Id", document.body.offsetWidth-10, document.body.offsetHeight-190);
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

Ext.extend(Column2D, Ext.Viewport, {

});
