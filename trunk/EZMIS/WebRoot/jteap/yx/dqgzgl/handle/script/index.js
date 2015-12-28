
//数据字典
var dict_dqgzgl=$dictList("dqgzgl");
var dict_dqgzzy=$dictList("dqgzzy");

//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="执行状态#status#comboBox,负责部门#fzbm#comboBox,负责岗位#fzgw#comboBox,专业#dqgzzy#comboBox,工作规律#gzgl#comboBox,开始时间#startDate#dateField,结束时间#endDate#dateField".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="执行状态#status#comboBox,负责部门#fzbm#comboBox,负责岗位#fzgw#comboBox,专业#dqgzzy#comboBox,工作规律#gzgl#comboBox,开始时间#startDate#dateField,结束时间#endDate#dateField".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});

var nowDate = new Date();
var beginYmd = nowDate.format("Y-m")+"-01";
var nowYmd = nowDate.format("Y-m-d");
var initStatus = "未完成";
Ext.getCmp("sf#startDate").setValue(beginYmd);
Ext.getCmp("sf#endDate").setValue(nowYmd);
Ext.getCmp("sf#status").setValue(initStatus);

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
