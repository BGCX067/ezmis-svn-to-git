// 工具栏
var mainToolbar = new Ext.Toolbar({
			height : 26,
			listeners : {
				render : function(tb) {
					operationsToolbarInitialize(tb);
				}
			}
		});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button) {
	if (button.id == 'btnSave' || button.id == 'btnDqkgl')
		button.setDisabled(false);
}

/**
 * 保存设备停复役信息
 */
function btnSave_Click() {
	var dataStore = rightGrid.getStore();
	var modifyData = dataStore.modified.slice(0);
	var modifyDataArray = [];
	Ext.each(modifyData, function(item) {
		modifyDataArray.push(item.data);
	});
	//var modifyCount = modifyDataArray.length;
	for(var i =0; i < modifyDataArray.length; i++){
		var sysj = modifyDataArray[i].sysj;
		if(typeof sysj == 'object' && sysj.time){
			sysj = sysj.time;
		}
		modifyDataArray[i].sysj = formatDate(new Date(sysj), "yyyy-MM-dd");
	}
	var gridData = Ext.encode(modifyDataArray);
	Ext.Ajax.request({
		url:link5,
		success:function(ajax){
	 		var responseText=ajax.responseText;	
	 		var responseObject=Ext.util.JSON.decode(responseText);
	 		if(responseObject.success){
	 			alert("更新成功");
	 			rightGrid.getStore().reload();
	 		}else{
	 			alert(responseObject.msg);
	 		}				
		},
	 	failure:function(){
	 		alert("提交失败");
	 	},
	 	method:'POST',
	 	params: {gridData:gridData}//Ext.util.JSON.encode(selections.keys)			
	});
}

/**
 * 从SIS读取数据
 */
function btnDqkgl_Click() {

}

// 设备停复役查询面板
// 查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs = "开始日期#startDate#dateFieldShowTime,结束日期#endDate#dateFieldShowTime,机组#jz#comboBox"
		.split(",");
// 查询面板中默认显示的条件，格式同上
var searchDefaultFs = "开始日期#startDate#dateFieldShowTime,结束日期#endDate#dateFieldShowTime,机组#jz#comboBox"
		.split(",");
var searchPanel = new SearchPanel({
			searchDefaultFs : searchDefaultFs,
			searchAllFs : searchAllFs
		});

// 右边的grid
var rightGrid = new RightGrid();

// 中间
var lyCenter = {
	layout : 'border',
	id : 'center-panel',
	region : 'center',
	minSize : 175,
	maxSize : 400,
	border : false,
	margins : '0 0 0 0',
	items : [searchPanel, rightGrid]
}

// 北边
var lyNorth = {
	id : 'north-panel',
	region : 'north',
	height : 27,
	border : false,
	items : [mainToolbar]
}
