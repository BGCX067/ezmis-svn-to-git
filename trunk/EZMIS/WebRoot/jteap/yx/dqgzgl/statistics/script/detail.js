
//数据字典
var dict_dqgzgl=$dictList("dqgzgl");
var dict_dqgzzy=$dictList("dqgzzy");

//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="执行状态#status#comboBox,负责部门#fzbm#comboBox,负责岗位#fzgw#comboBox,工作规律#gzgl#comboBox,应执时间#dqgzCreateDt#dateField,执行时间#chuliDt#dateField".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="执行状态#status#comboBox,负责部门#fzbm#comboBox,负责岗位#fzgw#comboBox,工作规律#gzgl#comboBox,应执时间#dqgzCreateDt#dateField,执行时间#chuliDt#dateField".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});

var detailGrid=new DetailGrid();
detailGrid.getStore().reload();

//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[searchPanel,detailGrid]
}
