
//工具栏  
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnDel' || button.id=='btnModify') 
		button.setDisabled(true);
}

/**
 * 增加个人工器具台账
 */
function btnAdd_Click(){
	var result=showModule(link6,true,555,295);
	rightGrid.getStore().reload();
}
/**
 * 修改个人工器具台账
 */
function btnModify_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		var params = "wzmc|"+select.json.wzbm.wzmc+";"+
						 "zje|"+select.json.zje;
		var url=link6+"&docid="+select.json.id;
		result=showModule(url,true,555,295,params);
		rightGrid.getStore().reload();
	}
}

/**
 * 删除个人工器具台账
 */
function btnDel_Click(){
	var select=rightGrid.getSelectionModel().getSelections();
    rightGrid.deleteSelect(select);
}


/**
 * 删除分类
 */
function btnDelCatalog_Click(){
	leftTree.deleteSelectedNode();
}

//左边的树 右边的grid


//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="编号#bh#textField,发放时间#ffsj#dateField,到期时间#dqsj#dateField,物资#wzbm.wzmc#wzdagl,部门#bm#group,主管#zg#persons,操作员#czr#persons,部门主管#bmzg#persons,生计处#sjc#persons,发料人#flr#persons".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="编号#bh#textField,发放时间#ffsj#dateField,到期时间#dqsj#dateField,物资#wzbm.wzmc#wzdagl,部门#bm#group,主管#zg#persons,操作员#czr#persons,部门主管#bmzg#persons,生计处#sjc#persons,发料人#flr#persons".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});

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

//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [mainToolbar]
}


