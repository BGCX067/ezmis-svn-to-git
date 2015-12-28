
var nowDate = new Date();
var currentDate = getDataByDay(nowDate,-1);

//取数时间
var qushuDate = new Ext.form.DateField({
	id: 'dateZbsj',
	format: 'Y-m-d',
	value: currentDate,
	listeners: {
		 change: function(){
			var selectDate = dateZbsj.getValue().format("Y-m-d");
			if(selectDate > currentDate){
				alert("取数时间应为今天以前");
				dateZbsj.setValue(currentDate.format("Y-m-d"));
			}
		 }
	}
});

//时间(月份)
var queryDate = new Ext.app.DateField({
	id: 'queryDate',
	format: 'Y-m',
	anchor : '90%',
	value: currentDate,
	allowBlank : false,
	readOnly :true
});

//工具栏
var mainToolbar = new Ext.Toolbar({
	height:26,
	items:[
		'查询时间:', queryDate, '-',
		{id:'btnQuery',text:'查询',cls: 'x-btn-text-icon',icon:'icon/icon_10.gif',
			listeners: {
				click: function(){
					var selectDate = queryDate.getValue().format("Y-m");
					var nowYm = nowDate.format("Y-m");
					
					if(selectDate > nowYm){
						alert("只能操作当前月以前的指标数据");
						queryDate.setValue(nowYm);
					}
					xzbQs();						
				}
			}
		}]
});

var rightGrid = new RightGrid();

//小指标取数
function xzbQs(){
	//机组号
	var jizuNum = tableCode.substring(tableCode.indexOf("_")+1,tableCode.length);
	//取数年月 yyyy-MM
	var qushuYm = qushuDate.getValue().format("Y-m");
	//取数年月 yyyyMM
	var qushuYm2 = qushuDate.getValue().format("Ym");
	
	Ext.Ajax.request({
		url: link3,
		method: 'post',
		params: {tableCode:tableCode, jizuNum:jizuNum, qushuYm:qushuYm, qushuYm2:qushuYm2},
		success: function(ajax){
			var responseObject = Ext.util.JSON.decode(ajax.responseText);
			if(responseObject.success){
				initGrid();
			}/*else{
				alert("服务器忙,请稍后再试...");
			}*/
		},
		failure: function(){
			alert("服务器忙,请稍后再试...");
		}
		
	})
}
xzbQs();	

/**
 * 初始化
 */
function initGrid(){
	var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"数据查询中,请稍等..."}); 
	myMask.show();
		
 	Ext.Ajax.request({
 		url: link1,
 		method: 'post',
 		params: {tableCode: tableCode},
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
 				var cm = buildCm(cols);
 				//grid数据源
 				var ds = buildDs(cols);
 				rightGrid.updateData(cm,ds);
 				
 				if(!existPhysic){
 					alert('该指标未同步物理表, 程序已自动同步');
 					window.location.reload();
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
function buildCm(cols){
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
function buildDs(cols){
	var colsArray = new Array();
	for (var col in cols) {
		var fieldObj = {};
		colsArray.push(col);
	}
	
	 var ds = new Ext.data.Store({
        proxy: new Ext.data.ScriptTagProxy({
            url: link2 + "?tableCode=" + tableCode + "&zbsj=" + queryDate.getValue().format("Y-m")
        }),
        reader: new Ext.data.JsonReader({
            root: 'list',
            totalProperty: 'totalCount'
        },colsArray),
        remoteSort: true
    });
	
	return ds;
}

/**
 * 点击导出按钮 obj是要导出表的GridPanel对象（有一约定：obj的exportLink属性）
 * flag标志GridPanel中是否有checkbox
 */
function exportMyExcel(flag) {
	var path = rightGrid.store.proxy.url;
	var paths = path.split("?");
	var temp1 = paths[0];
	var temp3 = paths[1];
	temp1 = temp1.split("!")[0];
	path = temp1 + "!exportExcel.do?" + temp3;
	var cm = rightGrid.getColumnModel();
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
			+ "&paraWidth=" + encodeURIComponent(paraWidth) + "&tableCode=" + tableCode;
	window.open(path);
}

//中间
var lyCenter = {
	layout: 'border',
	id: 'center-panel',
	region: 'center',
	minSize: 175,
	maxSize: 400,
	border: false,
	margins: '0 0 0 0',
	items: [rightGrid]
}

//北边
var lyNorth = {
	id: 'north-panel',
	region: 'north',
	height: 27,
	border: false,
 	items: [mainToolbar]
}
