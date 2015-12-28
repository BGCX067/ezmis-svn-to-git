
//工具栏  
//1.添加分类
//2.修改分类
//3.删除分类
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
//	items:[
//		{disabled:true,id:'btnAdd',text:'新建',cls: 'x-btn-text-icon',icon:'icon/icon_4.gif',listeners:{click:mainToolBarButtonClick}},
//		{disabled:true,id:'btnModify',text:'修改',cls: 'x-btn-text-icon',icon:'icon/icon_6.gif',listeners:{click:mainToolBarButtonClick}},
//		{disabled:true,id:'btnDel',text:'删除',cls: 'x-btn-text-icon',icon:'icon/icon_7.gif',listeners:{click:mainToolBarButtonClick}}
//		]
});
/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnFindCatalog'){ 
		button.setDisabled(true);
	}
}

/**
 * 报表查看
 */
function btnFindCatalog_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		var url=link12+"?id="+select.json.id+"&isUpdate=false";
		result=showModule(url,true,700,550);
		if(result=="true"){
			alert("修改成功!");
			rightGrid.getStore().reload();
		}
	}
}
	
//左边的树 右边的grid

var rightGrid=new RightGrid();
//用户查询面板								
var searchPanel=new SearchPanel({searchDefaultFs:"年#NIAN#comboBox#1,月#YUE#comboBox#2".split(","),searchAllFs:"年#NIAN#comboBox#1,月#YUE#comboBox#2".split(",")});
//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[rightGrid,searchPanel]
}
//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [mainToolbar]
}


