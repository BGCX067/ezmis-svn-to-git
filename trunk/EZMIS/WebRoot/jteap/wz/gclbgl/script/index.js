
//工具栏  
//1.添加分类
//2.修改分类
//3.删除分类
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnModify' || button.id=='btnDel' ) 
		button.setDisabled(true);
}

/**
 * 增加工程类别
 */
function btnAdd_Click(){
	
	var result=showModule(link6,true,480,235);
//	if(result=="" || result==null){
		rightGrid.getStore().reload();
//	}
}
/**
 * 修改工程类别
 */
function btnModify_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		var url=link6+"&docid="+select.json.id;
		result=showModule(url,true,480,235);
//		if(result=="" || result==null){
			rightGrid.getStore().reload();
//		}
	}
	
}

/**
 * 删除工程类别
 */
function btnDel_Click(){
	var select=rightGrid.getSelectionModel().getSelections();
    rightGrid.deleteSelect(select);
}



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
	items:[rightGrid]
}

//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [mainToolbar]
}


