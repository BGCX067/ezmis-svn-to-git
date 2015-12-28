//主工具栏
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}});
/*({
	items:[
		{id:'btnDelLog',text:'删除日志',cls:'x-btn-text-icon',icon:'icon/icon_2.gif',listeners:{click:btnDelLog_Click},tooltip:{title:'删除日志',text:'删除选中的日志'}},
		{id:'btnDelAllLog',text:'清空日志',cls:'x-btn-text-icon',icon:'icon/icon_10.gif',listeners:{click:btnDelAllLog_Click},tooltip:{title:'清空日志',text:'删除所有的日子'}}
	]
})*/
/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnDelAllLog'){ 
		button.setDisabled(false);
	}else if(button.id=='btnDelLog') {
		button.setDisabled(true);
	}	
}
//点击删除日志按钮的操作
function btnDelLog_Click(){
	gridPanel.delSelectedLog();
}
//点击清空日志按钮的操作
function btnDelAllLog_Click(){
	gridPanel.delAllLog();
}
//定义北边
var lyNorth = {
	id:'sorth_panel_log',
	height:27,
	region:'north',
	border:false,
	mairgins:'2 2 2 2',
	items:[mainToolbar]
}

//日志查询面板								
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});
//日志列表
var gridPanel=new LogGrid(type);
gridPanel.getStore().load({params:{start:0, limit:25}});
//中间的部分
var lyCenter = {
	id:'center_panel_log',
	border:false,
	layout:'border',
	region:'center',
	items:[searchPanel,gridPanel]
}
