var queryParams = "";

//放置饼图面板 
var piePanel=new Ext.Panel({
	id:'panle_hello', 
	layout:'fit',
	region:'center',
	autoWidth:true, 
	autohHight:true,
	html:"<IFRAME id='sbpjgc' name='sbpjgc' scrolling='no' src='../pjgc/showSbgc.jsp' style='width:100%;height:100%' frameboder='0'></IFRAME>"
});

//评级查询面板
//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="开始日期#startDate#dateFieldShowTime,结束日期#endDate#dateFieldShowTime".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="开始日期#startDate#dateFieldShowTime,结束日期#endDate#dateFieldShowTime".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});

//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[searchPanel,piePanel]
}
