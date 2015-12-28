
// 工具栏
// 1.添加分类
// 2.修改分类
// 3.删除分类
var mainToolbar = new Ext.Toolbar({
			height : 26,
			listeners : {
				render : function(tb) {
					operationsToolbarInitialize(tb);
				}
			}
		});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button) {
	if (button.id == 'btnModify' || button.id == 'btnDel')
		button.setDisabled(true);
}

/**
 * 流程申请
 */
function btnAddSq_Click() {
	var url = contextPath
			+ "/jteap/wfengine/wfi/WorkFlowInstanceAction!showUpdateAction.do";
	var url = url + "?flowConfigId=" + flowConfigId;
	var title = "物资领用申请起草";
	var args = "url|" + url + ";title|" + title;
	var result = showModule(contextPath + "/jteap/ModuleWindowForm.jsp", "yes",
			800, 600, args);

	// var result=showModule(link6,true,880,490);
	// if(result=="" || result==null){
	rightGrid.getStore().reload();
	// }
}
/**
 * 流程处理
 */
function btnProcess_Click() {
		if (rightGrid.getSelectionModel().getSelections().length != 1) {
			alert("请选取一条记录查看");
			return;
		}
	
		var node = leftTree.getSelectionModel().getSelectedNode();
		var select = rightGrid.getSelectionModel().getSelections()[0];
		if (node.id == 'dcl') {
			var id = select.json.TASKTODOID;
			var pid = select.json.FLOW_INSTANCE_ID;
			var token = select.json.TOKEN;
	
			// 弹出流程查看窗口
			var url = link9;
			var windowUrl = link10 + "?pid=" + pid + "&id=" + id + "&token="
					+ token + "&isEdit=true";
			var args = "url|" + windowUrl + ";title|" + '查看流程';
			var retValue = showModule(url, "yes", 800, 600, args);
	
			// 进行释放签收操作
			Ext.Ajax.request({
						url : link5,
						method : 'POST',
						params : {
							pid : pid,
							token : token
						},
						success : function(ajax) {
							var responseText = ajax.responseText;
							var obj = Ext.decode(responseText);
							if (obj.success) {
								rightGrid.getStore().reload();
							} else {
								alert("数据库操作异常，请联系管理员！");
							}
						},
						failure : function() {
							alert("数据库操作异常，请联系管理员！");
						}
					})
			rightGrid.getStore().reload();
		} else if (node.id == 'cgx') {
			var url = link9;
			var windowUrl = link8 + "?pid=" + select.get("ID_");
			var args = "url|" + windowUrl + ";title|" + '查看流程';
			var retValue = showModule(url, "yes", 800, 600, args);
			rightGrid.getStore().reload();
		} else {
			var url = link9;
			var windowUrl = link10 + "?pid=" + select.get("ID_") + "&status=false";
			var args = "url|" + windowUrl + ";title|" + '查看流程';
			var retValue = showModule(url, "yes", 800, 600, args);
		}
}

/**
 * 流程撤销
 */
function btnUndo_Click() {
		if (window.confirm('确认要撤销吗？')) {
			var url = link12;
			var select = rightGrid.getSelectionModel().getSelected();
			var pid = select.get('PROCESSINSTANCE_');
			var docid = select.get('ID');
			var flowConfigId = select.get('FLOW_CONFIG_ID');
			var formSn = 'TB_JX_QXGL_QXD';
			Ext.Ajax.request({
				url : url,
				method : 'POST',
				params : {
					pid : pid,
					docid : docid,
					flowConfigId : flowConfigId,
					formSn : formSn
				},
				success : function(ajax) {
					var html = ajax.responseText;
					var reg = /'[^ ']*'/;
					var result = reg.exec(html);
					alert(result);
					rightGrid.getStore().reload();
				},
				failure : function(ajax) {
					var html = ajax.responseText;
					var reg = /'[^ ']*'/;
					var result = reg.exec(html);
					alert(result);
					rightGrid.getStore().reload();
				}
			})
		}
}

/**
 * 流程作废
 */
function btnCancel_Click() {
		var select = rightGrid.getSelectionModel().getSelected();
		if (select.get('FLOW_STATUS') != '申请') {
			alert('此异动报告不符合作废标准，无法作废');
			return;
		}
		if (window.confirm('是否作废此异动报告？')) {
			Ext.Ajax.request({
						url : link7,
						method : 'POST',
						params : {
							id : select.get('ID')
						},
						success : function(ajax) {
							var responseText = ajax.responseText;
							var obj = Ext.decode(responseText);
							if (obj.success) {
								alert('异动报告作废成功');
								rightGrid.getStore().reload();
							} else {
								alert("数据库操作异常，请联系管理员！");
							}
						},
						failure : function() {
							alert("数据库操作异常，请联系管理员！");
						}
					})
		}
}

/**
 * 流程删除
 */
function btnDelSq_Click() {
		if (rightGrid.getSelectionModel().getSelections().length < 1) {
			alert("请选择草稿箱中异动报告后再删除");
			return;
		}
		var url = contextPath + "/jteap/wfengine/wfi/WorkFlowInstanceAction!deleteProcessInstranceAction.do";
		var select = rightGrid.getSelectionModel().getSelections();
		var ids = getIds(select);
		if (window.confirm("确认删除选中的异动报告草稿吗?")) {
			var myMask = showExtMask("异动报告草稿删除中，请稍候。。。");
			// 提交数据
			Ext.Ajax.request({
						url : url,
						success : function(ajax) {
							var responseText = ajax.responseText;
							var responseObject = Ext.decode(responseText);
	
							if (responseObject.success) {
								alert("删除成功");
								rightGrid.getStore().reload();
								myMask.hide();
							} else {
								alert("删除失败");
							}
						},
						failure : function() {
							alert("提交失败");
						},
						method : 'POST',
						params : {
							ids : ids
						}
					})
		}
}

function getIds(select) {
	var ids = "";
	for (var i = 0; i < select.length; i++) {
		var temp = select[i];
		ids += temp.get("ID_") + ",";
	}
	return ids;
}

/**
 * 导出表单
 */
function btnExportXqjhsq_Click() {
	if (rightGrid.getSelectionModel().getSelections().length != 1) {
		alert('选择一行导出');
	}
	var select = rightGrid.getSelectionModel().getSelected();
	if (select) {
		window.open(link9 + "?id=" + select.json.ID);
	}
}
/**
 * 删除仓库
 */
function btnDel_Click() {
	var select = rightGrid.getSelectionModel().getSelections();
	rightGrid.deleteSelect(select);
}



// 左边的树 右边的grid
var rightGrid = new RightGrid();
var leftTree = new LeftTree();
var lymxGrid=new LyMxRightGrid(); 
var zylyGrid = new zylyGrid();	  //自由入库
var wzmcGrid = zylyGrid;
// 中间
var lyCenter = {
	layout : 'border',
	id : 'center-panel',
	region : 'center',
	minSize : 175,
	maxSize : 400,
	border : false,
	margins : '0 0 0 0',
	items : [leftTree, rightGrid]
}
var lySelectCenter = {
	//layout : 'form',
	title:'匹配需求计划',
	id : 'center-panel',
	region : 'center',
	minSize : 175,
	maxSize : 400,
	height:200,
	autoWidth:true,
	border : false,
	autoScroll :true,
	margins : '0 0 0 0',
	items : [rightGrid,lymxGrid],
	buttons:[{text:'确定',handler:function(){
		var select=lymxGrid.getSelectionModel().getSelected();
		var selections = lymxGrid.getSelectionModel().getSelections();
		if(selections.length>0){
			//window.returnValue = xqjhId+"!"+selections;
			if(zdy){
				zdy.returnFn(selections);
			}
			if(gcxm==""){
				gcxm = select.data.GCXM;
				gclb = select.data.GCLB;
				var url = link13+"?bm="+bm+"&gclb="+gclb+"&gcxm="+gcxm.replace("#","!");
				rightGrid.changeToListDS(url);
				rightGrid.getStore().reload();
			}
		}
		
	}}]
}
/*
var lySelectFromWzda = {
	layout:'border',
	title:'匹配库存物资',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[rightPanel]
}

//南边布局
var lySouth={
	layout:'border',
	id:'south-panel',
	title:'匹配库存物资',
	region:'south',
	minSize: 175,
	maxSize: 400,
	height:300,
	border:false,
	margins:'0 0 0 0',
	items:[leftTree,rightPanel],
	buttons:[{text:'确定',handler:function(){
		var select=xqRightGrid.getSelectionModel().getSelected();
		var selections = xqRightGrid.getSelectionModel().getSelections();
		if(selections.length>0){
			window.returnValue = selections;
		}
		window.close();
	}}]
}*/

var zylyGridCenter={
	layout:'border',
	id:'zylyGridCenter',
	region:'center',
	minSize: 175,
	maxSize: 400,
	title:'自由领用',
	border:false,
	margins:'0 0 0 0',
	items:[zylyGrid],
	buttons:[{text:'确定',handler:function(){
		var select=zylyGrid.getSelectionModel().getSelected();
		var selections = zylyGrid.getSelectionModel().getSelections();
		var returnObjList=new Array();//数组
		if(selections.length>0){
			for(var i = 0;i<selections.length;i++){
				var returnObj = {};
				var dataObjs = selections[i].data;
				if(dataObjs.WZMC!=""){
					returnObj.json=dataObjs;
					returnObjList.push(returnObj);
				}
			}
			zdy.returnFn(returnObjList);
		}
	}}]
}
// 北边
var lyNorth = {
	id : 'north-panel',
	region : 'north',
	height : 27,
	border : false,
	items : [mainToolbar]
}

var WzdaTabPanel = function(config){
	var wzdaTabPanel = this;
	this.lySelectCenter = lySelectCenter;
	this.zylyGridCenter = zylyGridCenter;
	//this.lySouth = lySouth;
	
	WzdaTabPanel.superclass.constructor.call(this, {
		region : 'center',
		width : 180,
		border : true,
		margins : '5 5 5 5',
		cmargins : '0 0 0 0',
		activeTab : 0,
		items : [wzdaTabPanel.lySelectCenter,
		wzdaTabPanel.zylyGridCenter]//,wzdaTabPanel.lySouth
	});
}
Ext.extend(	WzdaTabPanel,Ext.TabPanel,{})
var wzdaTabPanel = new WzdaTabPanel();

//中间
var lysqCenter={
	layout:'border',
	id:'lysqCenter',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[wzdaTabPanel]
}
