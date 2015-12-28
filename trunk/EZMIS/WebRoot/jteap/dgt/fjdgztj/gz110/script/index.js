//工具栏  
//1.添加分类
//2.修改分类
//3.删除分类
var mainToolbar = new Ext.Toolbar({height:26});




/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){

}

var searchAllFs="工会#gonghui#comboBox,年#year#comboBox,季度#jidu#comboBox,诉求职工姓名#zgname#textField".split(",");
var searchDefaultFs="工会#gonghui#comboBox,年#year#comboBox,季度#jidu#comboBox,诉求职工姓名#zgname#textField".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});
	

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

//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [mainToolbar]
}


