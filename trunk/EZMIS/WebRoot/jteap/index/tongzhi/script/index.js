//工具栏
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnModi' || button.id=='btnDel')
		button.setDisabled(true);
}

/**
 * 添加
 */
function btnAdd_Click(){
	var obj = {};
	obj.grid = rightGrid;
			
	var url = contextPath + '/jteap/index/tongzhi/editForm.jsp';
	showIFModule(url,"添加记录","true",400,350,obj);
}

/**
 * 修改
 */
function btnModi_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		var obj = {};
		obj.grid = rightGrid;
		obj.select = select;
				
		var url = contextPath + '/jteap/index/tongzhi/editForm.jsp?modi=m';
		showIFModule(url,"修改记录","true",400,350,obj);
	}
	
}

/**
 * 删除
 */
function btnDel_Click(){
	var select=rightGrid.getSelectionModel().getSelections();
    rightGrid.deleteSelect(select);
}

//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="发出人#fcr#textField,开始时间#begingYmd#dateField,结束时间#endYmd#dateField".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="发出人#fcr#textField,开始时间#begingYmd#dateField,结束时间#endYmd#dateField".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});

var rightGrid=new RightGrid();
rightGrid.getStore().reload();

//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[searchPanel,rightGrid]
}

//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [mainToolbar]
}
