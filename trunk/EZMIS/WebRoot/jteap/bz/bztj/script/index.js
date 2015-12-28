//工具栏  
//1.添加分类
//2.修改分类
//3.删除分类
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
	/*items:[
		{disabled:false,id:'btnAddIp',text:'添加IP规则',cls: 'x-btn-text-icon',icon:'icon/icon_2.gif',listeners:{click:btnAddIp_Click}},
		{disabled:true,id:'btnModiIpLock',text:'修改规则',cls: 'x-btn-text-icon',icon:'icon/icon_4.gif',listeners:{click:btnModiIpLock_Click}},
		{disabled:true,id:'btnDelIpLock',text:'删除规则',cls: 'x-btn-text-icon',icon:'icon/icon_3.gif',listeners:{click:btnDelIpLock_Click}}
		]*/
});



function btnAdd_Click(){
	var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn=TB_FORM_BZTJ";
	//showIFModule(url,"班组通知专栏","true",550,510,{},null,null,null,false,"auto");
	new $FW({url:url,callback:function(){
			rightGrid.getStore().reload();
		}}).show();
}

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnMod' || button.id=='btnDel') 
		button.setDisabled(true);
}
//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="填写人#txr#textField,统计时间#txsj#dateField".split(',');
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="填写人#txr#textField,统计时间#txsj#dateField".split(',');
var searchPanel=new SearchPanel({region:'north',searchDefaultFs:searchDefaultFs,searchAllFs:searchDefaultFs});

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


