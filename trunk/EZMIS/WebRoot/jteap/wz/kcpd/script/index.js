
//工具栏  
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnModify' || button.id=='btnDel' || button.id=='btnPrint' ||button.id=='btnEnable') 
		button.setDisabled(true);
	if(button.id=='btnQuery') button.hide();	
}

/**
 * 打印采购计划单
 */
function btnPrint_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		var url=link9+"?id="+select.json.id;
		window.open(url);
	}
}

/**
 * 采购物资汇总
 */
function btnWzSum_Click(){
	var WzSum = new WzSumWindow();
	WzSum.show();
} 
 

/**
 * 采购计划查询
 */
function btnQuery_Click(){
	var result=showModule(link1,true,700,700);
//	var url = contextPath+"/jteap/wz/wzlb/selectWzdaIndex.jsp";
//	var result = showModule(url,true,900,450);
//	if(result==null) alert('没有选中');
//		else alert(result);
}

/**
 * 添加EForm表单
 */
function btnAdd_Click(){
	var result=showModule(link6,true,650,500);
	rightGrid.getStore().reload();
}
/**
 * 修改表单
 */
function btnModify_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		var url=link13+"&docid="+select.json.id;
		/*
		var pserons = {
									czr		:		select.json.personCzr.userName+"|"+select.json.personCzr.userLoginName,
									ysr		:		select.json.personYsr.userName+"|"+select.json.personYsr.userLoginName,
									thr		:		select.json.personThr.userName+"|"+select.json.personThr.userLoginName,
									cwfzr	:		select.json.personCwfzr.userName+"|"+select.json.personCwfzr.userLoginName
								};
								*/
		var param = "czr|"+select.json.personCzr.userName+"|"+select.json.personCzr.userLoginName+";"+
							   "cyr1|"+select.json.personCyr1.userName+"|"+select.json.personCyr1.userLoginName+";"+
							   "cyr2|"+select.json.personCyr2.userName+"|"+select.json.personCyr2.userLoginName+";"+
							   "cyr3|"+select.json.personCyr3.userName+"|"+select.json.personCyr3.userLoginName+";"+
							   "cyr4|"+select.json.personCyr4.userName+"|"+select.json.personCyr4.userLoginName;
		result=showModule(url,true,650,500,param);

		rightGrid.getStore().on('load',function(){
			var whereSql = "obj.pdd.id ='"+select.json.id+"'";
			mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
			//mxGrid.changeToListDS(link2);
			mxGrid.store.load();
		});
		rightGrid.getStore().reload();

	}
}

/**
 * 删除表单
 */
function btnDel_Click(){
	var select=rightGrid.getSelectionModel().getSelections();
    var result=rightGrid.deleteSelect(select);
    if(result!='cancle') clearMxGrid();
}


/**
 * 清空子表Grid显示
 */
function clearMxGrid(){
	var whereSql = "obj.pdd.id ='" + 0 +"'";
	mxGrid.store.baseParams={queryParamsSql:whereSql};
	mxGrid.store.load();
}

/**
 * 采购计划生效
 */
function btnEnable_Click(){
	rightGrid.enableCgjh();

} 
	
//左边的树 右边的grid


//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="表单标题#title#textField".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="表单标题#title#textField".split(",");
//var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});

var rightGrid=new RightGrid();    //采购计划
var mxGrid=new MxRightGrid();     //采购计划明细

//中间布局
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
//	height:100,
	border:false,
	margins:'0 0 0 0',
	items:[rightGrid]
}

//南边布局
var lySouth={
	layout:'border',
	id:'south-panel',
	region:'south',
	minSize: 175,
	maxSize: 400,
	height:300,
	border:false,
	margins:'0 0 0 0',
	items:[mxGrid]
}

//北边布局
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [mainToolbar]
}


