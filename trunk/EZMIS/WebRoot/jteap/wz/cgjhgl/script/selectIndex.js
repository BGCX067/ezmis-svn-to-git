
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
	mxGrid.enableCgjh();

} 
	
//左边的树 右边的grid


//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="表单标题#title#textField".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="表单标题#title#textField".split(",");
//var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});

var rightGrid=new RightGrid();    //采购计划
var mxGrid=new MxRightGrid();     //采购计划明细

var zdrightGrid=new zdRightGrid();    //采购计划
var zdmxGrid=new zdMxRightGrid();     //采购计划明细

var ZyRkGrid = new ZyRkGrid();	  //自由入库
var wzmcGrid = ZyRkGrid;

var xqjhSqGrid = new xqjhSqGrid();	  //需求计划申请
var xqjhsqMxGrid = new xqjhsqMxGrid(); //需求计划申请明细

////////////匹配采购计划布局
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
		var select=mxGrid.getSelectionModel().getSelected();
		var selections = mxGrid.getSelectionModel().getSelections();
		if(selections.length>0){
			var arg = window.dialogArguments;
			for (var index = 0; index < selections.length; index++) {
				if(arg.CGY && arg.CGY!=selections[index].get('person.userLoginName')){
					alert("请选择采购员是 ： "+arg.CGYXM+" 的采购明细");
					return;
					//select.get("wzdagl.kw.ck.ckmc");
				}
			}
			//window.returnValue = selections;
			//var ss = fw;
			zdy.returnFun(selections);
			//sssss
			//var aa =Ext.util.JSON.decode(select.json);
			//alert(select.json.wzdagl.id);
		}
		//window.close();
	}}]
}
var rightCenter={
	layout:'border',
	id:'rightCenter',
	region:'center',
	minSize: 175,
	maxSize: 400,
	title:'采购计划',
	border:false,
	margins:'0 0 0 0',
	items:[lyCenter,slectSouth]
}
/////////////自由入库布局

var ZyRkGridCenter={
	layout:'border',
	id:'ZyRkGridCenter',
	region:'center',
	minSize: 175,
	maxSize: 400,
	title:'自由入库',
	border:false,
	margins:'0 0 0 0',
	items:[ZyRkGrid],
	buttons:[{text:'确定',handler:function(){
		var select=ZyRkGrid.getSelectionModel().getSelected();
		var selections = ZyRkGrid.getSelectionModel().getSelections();
		var returnObjList=new Array();//数组
		if(selections.length>0){
			for(var i = 0;i<selections.length;i++){
				var returnObj = {};
				var dataObjs = selections[i].data;
				returnObj.json=dataObjs;
				returnObjList.push(returnObj);
			}
			//window.returnValue = returnObjList;
			zdy.returnFun(returnObjList);
			//var aa =Ext.util.JSON.decode(select.json);
			//alert(select.json.wzdagl.id);
		}
		 //sssss
		//window.close();
	}}]
}
//////////////需求计划申请布局
//中间布局
var sqlyCenter={
	layout:'border',
	id:'xqjhsqGrid',
	region:'center',
	minSize: 175,
	maxSize: 400,
//	height:100,
	border:false,
	margins:'0 0 0 0',
	items:[xqjhSqGrid]
}

//南边布局
var sqslectSouth={
	layout:'border',
	id:'xqjhsqMxGrid',
	region:'south',
	minSize: 175,
	maxSize: 400,
	height:300,
	border:false,
	margins:'0 0 0 0',
	items:[xqjhsqMxGrid],
	buttons:[{text:'确定',handler:function(){
		var select = xqjhsqMxGrid.getSelectionModel().getSelected();
		var selections = xqjhsqMxGrid.getSelectionModel().getSelections();
		var ids = "";
		if(selections.length>0){
			for(var i = 0;i<selections.length;i++){
				ids += selections[i].data.id+",";
			}
		}
		Ext.Ajax.request({
				url:link14,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			alert("指定成功");
			 		}else{
			 			alert(responseObject.msg);
			 		}				
				},
			 	failure:function(){
			 		alert("提交失败");
			 	},
			 	method:'POST',
			 	params: {ids:ids}//Ext.util.JSON.encode(selections.keys)			
			});
			xqjhSqGrid.getStore().reload();
			xqjhSqGrid.getSelectionModel().selectFirstRow();
			var jhid = xqjhSqGrid.store.data.items[0].id;
			xqjhsqMxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',id:jhid};
			xqjhsqMxGrid.store.load();
		//window.close();
	}}]
}
var xqsqGridCenter={
	layout:'border',
	id:'xqjhsqCenter',
	region:'center',
	minSize: 175,
	maxSize: 400,
	title:'需求计划',
	border:false,
	margins:'0 0 0 0',
	items:[sqlyCenter,sqslectSouth]
}
////////////////////指定采购计划
//中间布局
var zdCenter={
	layout:'border',
	id:'zdcenter-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
//	height:100,
	border:false,
	margins:'0 0 0 0',
	items:[zdrightGrid]
}

//南边布局
var zdslectSouth={
	layout:'border',
	id:'zdsouth-panel',
	region:'south',
	minSize: 175,
	maxSize: 400,
	height:300,
	border:false,
	margins:'0 0 0 0',
	items:[zdmxGrid],
	buttons:[{text:'确定',handler:function(){
		var select=zdmxGrid.getSelectionModel().getSelected();
		var selections = zdmxGrid.getSelectionModel().getSelections();
		if(selections.length>0){
			var arg = window.dialogArguments;
			for (var index = 0; index < selections.length; index++) {
				if(arg.CGY && arg.CGY!=selections[index].get('person.userLoginName')){
					alert("请选择采购员是 ： "+arg.CGYXM+" 的采购明细");
					return;
					//select.get("wzdagl.kw.ck.ckmc");
				}
			}
			//window.returnValue = selections;
			//var ss = fw;
			zdy.returnFun(selections);
			//sssss
			//var aa =Ext.util.JSON.decode(select.json);
			//alert(select.json.wzdagl.id);
		}
		//window.close();
	}}]
}
var zdrightCenter={
	layout:'border',
	id:'zdrightCenter',
	region:'center',
	minSize: 175,
	maxSize: 400,
	title:'指定采购计划',
	border:false,
	margins:'0 0 0 0',
	items:[zdCenter,zdslectSouth]
}
////////////////tab页
var WzdaTabPanel = function(config){
	var wzdaTabPanel = this;
	this.rightCenter = rightCenter;
	this.ZyRkGridCenter = ZyRkGridCenter;
	this.xqsqGridCenter = xqsqGridCenter;
	this.zdrightCenter = zdrightCenter;
	
	WzdaTabPanel.superclass.constructor.call(this, {
		region : 'center',
		width : 180,
		border : true,
		margins : '5 5 5 5',
		cmargins : '0 0 0 0',
		activeTab : 0,
		autoTabs  :true,
		layoutOnTabChange :true,
		items : [wzdaTabPanel.rightCenter,
			wzdaTabPanel.ZyRkGridCenter]
	});
}
Ext.extend(	WzdaTabPanel,Ext.TabPanel,{})
var wzdaTabPanel = new WzdaTabPanel();
/*
wzdaTabPanel.activate('llGrid');
wzdaTabPanel.activate('slGrid');
wzdaTabPanel.activate('cgGrid');
wzdaTabPanel.activate('xqGrid');
wzdaTabPanel.activate('fpGrid');
wzdaTabPanel.activate('rkGrid');
wzdaTabPanel.activate('wzCenter');
*/
//中间
var xqjhCenter={
	layout:'border',
	id:'xqjhCenter',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[wzdaTabPanel]
}

