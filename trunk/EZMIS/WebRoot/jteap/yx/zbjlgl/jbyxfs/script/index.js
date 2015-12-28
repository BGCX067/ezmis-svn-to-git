//工具栏  
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){}

//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="值班时间#zbsj#dateField,值班班次#zbbc#comboBox,运行方式#gwlb#comboBox".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="值班时间#zbsj#dateField,值班班次#zbbc#comboBox,运行方式#gwlb#comboBox".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});

//设置查询条件为当前时间、班次
Ext.getCmp("sf#zbsj").setValue(nowYmd);
Ext.getCmp("sf#zbbc").setValue(nowBc);

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
