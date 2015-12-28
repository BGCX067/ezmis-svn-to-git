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
	if (button.id == 'btnModiCatalog' || button.id == 'btnDelCatalog' || button.id == 'btnModifyZb'
			|| button.id == 'btnDelZb')
		button.setDisabled(true);
}

/**
 * 增加分类
 */
function btnAddCatalog_Click() {
	var oNode = leftTree.getSelectionModel().getSelectedNode();
	if (oNode.getDepth() == 1) {
		alert("此节点下不能增加招标分类");
		return;
	}
	leftTree.createZbfl();
}

/**
 * 修改分类
 */
function btnModiCatalog_Click() {
	leftTree.modifyZbfl();
}

/**
 * 删除分类
 */
function btnDelCatalog_Click() {
	leftTree.delZbfl();
}

/**
 * 增加招标信息
 */
function btnAddZb_Click() {
	var url = "/jteap/form/eform/eformRec.jsp?formSn=TB_HT_ZBXX";
	var myTitle = "招标信息";
	showIFModule(CONTEXT_PATH + url, myTitle, "true", 800, 600, {});
	rightGrid.getStore().reload();
}

/**
 * 修改招标信息
 */
function btnModifyZb_Click() {
	var select = rightGrid.getSelectionModel().getSelections()[0];
	var id = select.get("id");
	var url = "/jteap/form/eform/eformRec.jsp?formSn=TB_HT_ZBXX&docid=" + id;
	var myTitle = "招标信息";
	showIFModule(CONTEXT_PATH + url, myTitle, "true", 800, 600, {});
	rightGrid.getStore().reload();
}

/**
 * 删除招标信息
 */
function btnDelZb_Click() {
	if (window.confirm("确认删除选中的招标信息吗?")) {
		var select = rightGrid.getSelectionModel().getSelections();
		var ids = "";
		for (var i = 0;i < select.length; i++) {
			ids += select[i].get("id") + ",";
		}

		Ext.Ajax.request({
			url : link5,
			params : {
				formSn : "TB_HT_ZBXX",
				ids : ids
			},
			method : 'post',
			success : function(ajax) {
				eval("responseObj=" + ajax.responseText);
				if (responseObj.success) {
					alert('删除成功');
					rightGrid.getStore().reload();
				} else {
					alert('删除失败');
				}
			},
			failure : function() {
				alert('服务器忙,请稍后再试...');
			}
		})
	}
}

// 左边的树 右边的grid
var rightGrid = new RightGrid();
var leftTree = new LeftTree();
var searchAllFs = "项目名称#xmmc#textField,招标批次#zbpc#textField,招标方式#zbfs#comboZbfs,招标地址#zbdz#textField,计划时间#jhzbsj1#dateField,至#jhzbsj2#dateField,实际时间#sjzbsj1#dateField,至#sjzbsj2#dateField"
		.split(',');
var searchDefaultFs = "项目名称#xmmc#textField,招标批次#zbpc#textField,招标方式#zbfs#comboZbfs,招标地址#zbdz#textField,计划时间#jhzbsj1#dateField,至#jhzbsj2#dateField,实际时间#sjzbsj1#dateField,至#sjzbsj2#dateField"
		.split(',');
var searchPanel = new SearchPanel({
	region : 'north',
	searchDefaultFs : searchDefaultFs,
	searchAllFs : searchDefaultFs,
	txtWidth : 150
});

// 中间
var lyCenter = {
	layout : 'border',
	id : 'center-panel',
	region : 'center',
	minSize : 175,
	maxSize : 400,
	border : false,
	margins : '0 0 0 0',
	items : [rightGrid, searchPanel]
}

// 北边
var lyNorth = {
	id : 'north-panel',
	region : 'north',
	height : 27,
	border : false,
	items : [mainToolbar]
}
