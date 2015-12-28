//设备台帐查询面板
//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="申请人#dysczr#comboBox".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="申请人#dysczr#comboBox".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs,labelWidth:80});


//左边的树 右边的grid

var rightGrid=new RightGrid();

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

//Excel导出
function exportSelectedExcel(grid){
	var select = grid.getSelectionModel().selections;
	var allSelect = grid.getStore().data.items;
	//所有选中的需求计划申请id
	var idsArr = new Array();
	//选择性导出Excel
	if(select.length > 0){
		for(var i =0; i < select.length; i++){
			idsArr.push(select.items[i].id);
		}
		var path = contextPath + "/jteap/wz/wzlysq/WzlysqxxAction!exportSelectedExcelAction.do?idsArr="+idsArr;
		window.open(path);
	}else{ //表示没有选择，将全部导出
		for(var i =0; i < allSelect.length; i++){
			idsArr.push(allSelect[i].data.id);
		}
		var path = contextPath + "/jteap/wz/wzlysq/WzlysqxxAction!exportSelectedExcelAction.do?idsArr="+idsArr;
		window.open(path);
	}
}
