var mainToolbar = new Ext.Toolbar({height:26});
/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
}
var searchAllFs="工会#gonghui#comboBox,年#year#comboBox,季度#jidu#comboBox,师傅#shifu#textField,徒弟#tudi#textField".split(",");
var searchDefaultFs="工会#gonghui#comboBox,年#year#comboBox,季度#jidu#comboBox,师傅#shifu#textField,徒弟#tudi#textField".split(",");
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


