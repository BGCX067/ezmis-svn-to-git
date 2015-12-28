
var nowDate = new Date().format("Y-m");
var currentDate = nowDate;
if(zbsj != 'null'){
	currentDate = zbsj;
}

//时间
var dateZbsj = new Ext.app.DateField({
	id: 'dateZbsj',
	format: 'Y-m',
	anchor : '90%',
	value: currentDate,
	allowBlank : false,
	readOnly :true
});

var mainTool = new Ext.Toolbar({
	height:26,
	items:[
		'时间:  ', dateZbsj, '-',
		{id:'btnQuery',text:'查询汇总',cls: 'x-btn-text-icon',icon:'icon/icon_2.gif',
			listeners: {
				click: function(){
					var selectDate = dateZbsj.getValue().format("Y-m");
					if(selectDate > nowDate){
						alert("只能操作当前月以前的指标数据");
						window.location = "index.jsp?zbsj=" + nowDate;
					}else{
						window.location = "index.jsp?zbsj=" + selectDate;
					}
				}
			} 
		}]
});

var grid1 = new Grid(1);
var grid2 = new Grid(2); 
var grid3 = new Grid(3); 
var grid4 = new Grid(4); 

/**
 * 初始化
 */
function initGrid(jzNum){
	var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"数据查询中,请稍等..."}); 
	myMask.show();
	
 	Ext.Ajax.request({
 		url: link1,
 		method: 'post',
 		params: {jzNum: jzNum},
 		success:function(ajax){
 			var responseText = ajax.responseText;	
 			var responseObject = Ext.util.JSON.decode(responseText);
 			if(responseObject.success){
				 				
 				var existTable = responseObject.existTable;
 				if(!existTable){
 					alert('该指标的表定义被删除,请联系管理员');
 					return;
 				}
 				
 				var existPhysic = responseObject.existPhysic;
 				
 				var cols = responseObject.cols;
 				//grid列模型
 				//var cm = buildCm(cols,jzNum);
 				//grid数据源
 				//var ds = buildDs(cols,jzNum);
 				var ds = buildDs2(jzNum);
 				
 				switch(jzNum){
 					case 1:
//		 				grid1.updateData(cm,ds);
 						grid1.updateData2(ds);
	 					break;
 					case 2:
//		 				grid2.updateData(cm,ds);
 						grid2.updateData2(ds);
	 					break;
	 				case 3:
//		 				grid3.updateData(cm,ds);
	 					grid3.updateData2(ds);
	 					break;
	 				case 4:
//		 				grid4.updateData(cm,ds);
	 					grid4.updateData2(ds);
	 					break;
 				}
 				
 				
 				if(!existPhysic){
 					alert('#'+jzNum+'机组指标未同步物理表, 程序已自动同步');
 				}
 				
 			}else{
 				alert("数据查询失败");
 			}
 			myMask.hide();
 		},
 		failure:function(){
 			alert("连接超时，数据查询失败");
 			myMask.hide();
 		}
 	})
}

//构建列模型对象
function buildCm(cols,jzNum){
	var colsArray = new Array();
	colsArray.push(new Ext.grid.CheckboxSelectionModel());
	
	for (var col in cols) {
		var width = 60;
		if(cols[col].length > 4){
			width = 80;
		}
		if(cols[col].length > 6){
			width = 100;
		}
		if(cols[col].length > 8){
			width = 120;
		}
		
		var tmpCm = {"id":col,"header":cols[col],"width":width,"sortable":true,"dataIndex":col};
		colsArray.push(tmpCm);
	}
	
	var cm = new Ext.grid.ColumnModel(colsArray);
	return cm;
};

//构建数据源对象
function buildDs(cols,jzNum){
	var colsArray = new Array();
	for (var col in cols) {
		var fieldObj = {};
		colsArray.push(col);
	}
	
	 var ds = new Ext.data.Store({
        proxy: new Ext.data.ScriptTagProxy({
            url: link2 + "?jzNum=" + jzNum + "&zbsj=" + currentDate
        }),
        reader: new Ext.data.JsonReader({
            root: 'list',
            totalProperty: 'totalCount'
        },colsArray),
        remoteSort: true
    });
	    
	return ds;
	
}

//构建数据源对象
function buildDs2(jzNum){
	 var ds = new Ext.data.Store({
        proxy: new Ext.data.ScriptTagProxy({
            url: link2 + "?jzNum=" + jzNum + "&zbsj=" + currentDate
        }),
        reader: new Ext.data.JsonReader({
            root: 'list',
            totalProperty: 'totalCount'
        },["directiveName","remark","mubiaozhi","yizhi","erzhi","sanzhi","sizhi","wuzhi","zongji","dbjg"]),
        remoteSort: true
    });
	return ds;
	
}

/**
 * 点击导出按钮 obj是要导出表的GridPanel对象（有一约定：obj的exportLink属性）
 * flag标志GridPanel中是否有checkbox
 */
function exportMyExcel(flag,jzNum){
	var grid = {};
	switch(jzNum){
		case 1:
			grid = grid1;
			break;
		case 2:
			grid = grid2;
			break;
		case 3:
			grid = grid3;
			break;
		case 4:
			grid = grid4;
			break;
	}
	var path = grid.store.proxy.url;
	var paths = path.split("?");
	var temp1 = paths[0];
	var temp3 = paths[1];
	temp1 = temp1.split("!")[0];
	path = temp1 + "!exportSumExcel.do?" + temp3;
	var cm = grid.getColumnModel();
	var sum = cm.getColumnCount();
	var j = 1;
	if (!flag) {
		j = 0;
	}
	var paraHeader = "";
	var paraDataIndex = "";
	var paraWidth = "";
	for (var i = j;i < sum; i++) {
		paraHeader += cm.getColumnHeader(i) + ",";
		paraDataIndex += cm.getDataIndex(i) + ",";
		paraWidth += cm.getColumnWidth(i) + ",";
	}
	paraHeader = paraHeader.substr(0, paraHeader.length - 1);
	paraDataIndex = paraDataIndex.substr(0, paraDataIndex.length - 1);
	paraWidth = paraWidth.substr(0, paraWidth.length - 1);
	path = path + "&paraHeader=" + encodeURIComponent(paraHeader)
			+ "&paraDataIndex=" + encodeURIComponent(paraDataIndex)
			+ "&paraWidth=" + encodeURIComponent(paraWidth);
	window.open(path);
}

var tabPanel = new Ext.TabPanel({
	activeTab: 0,
	height: 480,
	defaults:{bodyStyle:'padding:2px'},
	items: [{
		title: '#4机组小指标汇总',
		layout: 'border',
		items: [grid4],
		listeners: {
			render: function(){
				initGrid(4);
			}
		}
	},{
		title: '#3机组小指标汇总',
		layout: 'border',
		items: [grid3],
		listeners: {
			render: function(){
				initGrid(3);
			}
		}
	},{
		title: '#2机组小指标汇总',
		layout: 'border',
		items: [grid2],
		listeners: {
			render: function(){
				initGrid(2);
			}
		}
	},{
		title: '#1机组小指标汇总',
		layout: 'border',
		items: [grid1],
		listeners: {
			render: function(){
				initGrid(1);
			}
		}
	}]
})

//中间
var lyCenter = {
	id: 'center-panel',
	region: 'center',
	minSize: 175,
	maxSize: 400,
	border: false,
	margins: '0 0 0 0',
	items: [tabPanel]
}

//北边
var lyNorth = {
	id: 'north-panel',
	region: 'north',
	height: 27,
	border: false,
 	items: [mainTool]
}