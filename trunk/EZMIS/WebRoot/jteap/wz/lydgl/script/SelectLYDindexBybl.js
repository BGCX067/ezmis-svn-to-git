
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
		var url=link11+"&docid="+select.json.id;
		result=showModule(url,true,650,500,"param1|value1");

		rightGrid.getStore().on('load',function(){
			var whereSql = "obj.cgjhgl.id ='"+select.json.id+"'";
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
 * 删除分类
 */
function btnDelCatalog_Click(){
	leftTree.deleteSelectedNode();
}

/**
 * 添加分类
 */
function btnAddCatalog_Click(){
	leftTree.createNode();
}

/**
 * 修改分类
 */
function btnModiCatalog_Click(){
	leftTree.modifyNode();
}


/**
 * 清空子表Grid显示
 */
function clearMxGrid(){
	var whereSql = "obj.cgjhgl.id ='" + 0 +"'";
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

//南边布局
var slectSouth={
	layout:'border',
	id:'south-panel',
	region:'south',
	minSize: 175,
	maxSize: 400,
	height:300,
	border:false,
	margins:'0 0 0 0',
	items:[mxGrid],
	buttons:[{text:'确定',handler:function(){
		var select  =rightGrid.getSelectionModel().getSelected();
		if(select){
			var mxstore = mxGrid.getStore();
			var newStore = new Ext.data.Store();

			//newStore.add(mxstore.getAt(0));
			//var n = mxstore.find('wzlysqDetail.id','40288a542d0bc112012d0bcdc9c00003');
			for(i=0;i<mxstore.getCount();i++){
				var res = newStore.getRange();
				var res1 = mxstore.getRange(i,i);
				var index = newStore.find('wzlysqDetail.id',mxstore.getAt(i).get('wzlysqDetail.id'));
				if(index>-1){
					var record = newStore.getAt(index);
					var sjlysl = record.get('sjlysl');
					var pzlysl = record.get('pzlysl');
					record.set('pzlysl',pzlysl+mxstore.getAt(i).get('pzlysl'));
					record.set('sjlysl',sjlysl+mxstore.getAt(i).get('sjlysl'));
				}else{
					newStore.add(res1);
				}
			}
			window.returnValue = newStore.data;
		}
		window.close();
	}}]
}

//北边布局
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [mainToolbar]
}


