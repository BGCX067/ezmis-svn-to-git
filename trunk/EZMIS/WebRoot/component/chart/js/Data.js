
Data = function(config, param) {

    // create the data store
    var store = new Ext.data.SimpleStore({
        fields: [
		           {name: 'orderid'},
		           {name: 'productname'},
		           {name: 'customer'},
		           {name: 'price'},
		           {name: 'number'},
		           {name: 'total'}
        ]
    });
    store.loadData(eval(param.table));

    // create the Grid
    var grid = new Ext.grid.GridPanel({
    	id : 'grid-panel',
        store: store,
        columns: [
            {id:'orderid', header: "订单编号", width: 50, sortable: true, dataIndex: 'orderid'},
	        {header: "产口名称", width: 40, sortable: true, dataIndex: 'productname'},
	        {header: "客户", width: 40, sortable: true, dataIndex: 'customer'},
	        {header: "单价", width: 40, sortable: true, dataIndex: 'price'},
	        {header: "数量", width: 40, sortable: true, dataIndex: 'number'},
	        {header: "合计", width: 40, sortable: true, dataIndex: 'total'}
        ],
        viewConfig: {
        	forceFit: true
    	},
        stripeRows: true,
        autoExpandColumn: 'orderid',
		width : document.body.offsetWidth / 2 - 15,
		height : document.body.offsetHeight / 2 - 15,
        title:param.title
    });
	
	var cfg = {
		layout : 'column',
		items : [{
			columnWidth : 0.5,
			items : [{
				frame : true,
				border : false,
				width : document.body.offsetWidth / 2,
				height : document.body.offsetHeight / 2,
				html : '<div id="one"/>'
			}, {
				frame : true,
				border : false,
				width : document.body.offsetWidth / 2,
				height : document.body.offsetHeight / 2,
				html : '<div id="three"/>'
			}]
		}, {
			columnWidth : 0.5,
			items : [{
				frame : true,
				border : false,
				width : document.body.offsetWidth / 2,
				height : document.body.offsetHeight / 2,
				html : '<div id="two"/>'
			}, {
				frame : true,
				border : false,
				items : [
					grid
				]
			}]
		}]
	}

	config = Ext.applyIf(config || {}, cfg);

	Data.superclass.constructor.call(this, config);
	var chart1 = new FusionCharts(contextPath + "/component/chart/Charts/MSCombiDY2D.swf", "chart1Id",document.body.offsetWidth / 2 - 15, document.body.offsetHeight / 2 - 15);
	chart1.setDataXML(param.dataXml1);
	chart1.setTransparent(true);
	
	var chart2 = new FusionCharts(contextPath + "/component/chart/Charts/MSCombiDY2D.swf", "chart1Id",document.body.offsetWidth / 2 - 15, document.body.offsetHeight / 2 - 15);
	chart2.setDataXML(param.dataXml2);
	chart2.setTransparent(true);
	
	var chart3 = new FusionCharts(contextPath + "/component/chart/Charts/MSCombiDY2D.swf", "chart1Id",document.body.offsetWidth / 2 - 15, document.body.offsetHeight / 2 - 15);
	chart3.setDataXML(param.dataXml3);
	chart3.setTransparent(true);

	var timer1 = setInterval(function() {
		if (Ext.get("one")) {
			chart1.render("one");
			clearInterval(timer1);
		}
	}, 10)
	var timer2 = setInterval(function() {
		if (Ext.get("two")) {
			chart2.render("two");
			clearInterval(timer2);
		}
	}, 10)
	var timer3 = setInterval(function() {
		if (Ext.get("three")) {
			chart3.render("three");
			clearInterval(timer3);
		}
	}, 10)
}

Ext.extend(Data, Ext.Panel, {

});