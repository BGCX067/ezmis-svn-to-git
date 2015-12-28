//工具栏  
//1.添加分类
//2.修改分类
//3.删除分类
//4.添加字段
//5.修改字段
//6.删除字段
//7.导入物理表
//8.构造物理表
//9.查询表数据
//10.清除表数据
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
	/*items:[
		{disabled:false,id:'btnAddIp',text:'添加IP规则',cls: 'x-btn-text-icon',icon:'icon/icon_2.gif',listeners:{click:btnAddIp_Click}},
		{disabled:true,id:'btnModiIpLock',text:'修改规则',cls: 'x-btn-text-icon',icon:'icon/icon_4.gif',listeners:{click:btnModiIpLock_Click}},
		{disabled:true,id:'btnDelIpLock',text:'删除规则',cls: 'x-btn-text-icon',icon:'icon/icon_3.gif',listeners:{click:btnDelIpLock_Click}}
		]*/
});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	/**
	 * if(button.id=='btnAddTable' || button.id=='btnModiTable' 
		|| button.id=='btnDelTable' || button.id=='btnRebuildTable' || button.id=='btnDelColumn'
		|| button.id=='btnQueryTable' || button.id=='btnAddColumn'||button.id=='btnModifyColumn') 
		button.setDisabled(true);
	 */
}

function btnAddIp_Click(){
	var ipForm = new IPFormWindow();
	ipForm.show();
}

function btnDelIpLock_Click(){
	var select=rightGrid.getSelectionModel().getSelections();
    rightGrid.delIpLock(select);
}

	
function btnModiIpLock_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
    var id=select.json.id;
	var ipForm = new IPFormWindow(id);
	ipForm.show();
	ipForm.loadData();
}
	
//左边的树 右边的grid
var leftTree=new LeftTree();
var rightGrid=new RightGrid();

//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'1 0 0 -1',
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


