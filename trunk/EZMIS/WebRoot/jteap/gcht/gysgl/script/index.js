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
	if (button.id == 'btnAddCatalog' || button.id == 'btnModiCatalog' || button.id == 'btnDelCatalog' 
		|| button.id == 'btnModifyGys' || button.id == 'btnDelGys')
		button.setDisabled(true);
}

/**
 * 增加分类
 */
function btnAddCatalog_Click() {
	var oNode = leftTree.getSelectionModel().getSelectedNode();
	if (oNode.getDepth() == 3) {
		alert("此节点下不能增加供应商分类");
		return;
	}
	leftTree.createGysfl();
}

/**
 * 修改分类
 */
function btnModiCatalog_Click() {
	leftTree.modifyGysfl();
}

/**
 * 删除分类
 */
function btnDelCatalog_Click() {
	leftTree.delGysfl();
}

/**
 * 增加供应商信息
 */
function btnAddGys_Click() {
	var url = "/jteap/form/eform/eformRec.jsp?formSn=TB_HT_GYSXX";
	var myTitle = "供应商信息";
	showIFModule(CONTEXT_PATH + url, myTitle, "true", 800, 600, {});
	rightGrid.getStore().reload();
}

/**
 * 修改供应商信息
 */
function btnModifyGys_Click() {
	var select = rightGrid.getSelectionModel().getSelections()[0];
	var id = select.get("id");
	var url = "/jteap/form/eform/eformRec.jsp?formSn=TB_HT_GYSXX&docid=" + id;
	var myTitle = "供应商信息";
	showIFModule(CONTEXT_PATH + url, myTitle, "true", 800, 600, {});
	rightGrid.getStore().reload();
}

/**
 * 删除供应商信息
 */
function btnDelGys_Click() {
	if (window.confirm("确认删除选中的供应商信息吗?")) {
		var select = rightGrid.getSelectionModel().getSelections();
		var ids = "";
		for (var i = 0;i < select.length; i++) {
			ids += select[i].get("id") + ",";
		}

		Ext.Ajax.request({
			url : link5,
			params : {
				formSn : "TB_HT_GYSXX",
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
/**
 * 导出供应商
 */

//Excel导出
function exportSelectedExcel(grid){
	var select = grid.getSelectionModel().selections;
	var allSelect = grid.getStore().data.items;
	//所有选中的供应商id
	var idsArr = new Array();
	//选择性导出Excel
	if(select.length > 0){
		for(var i =0; i < select.length; i++){
			idsArr.push(select.items[i].data.ID);
		}
		var path = contextPath + "/jteap/gcht/gysgl/GysglAction!exportSelectedExcelAction.do?idsArr="+idsArr;
		window.open(path);
	}else{ //表示没有选择，将全部导出
		for(var i =0; i < allSelect.length; i++){
			idsArr.push(allSelect[i].data.id);
		}
		var path = contextPath + "/jteap/gcht/gysgl/GysglAction!exportSelectedExcelAction.do?idsArr="+idsArr;
		window.open(path);
	}
}


// 左边的树 右边的grid
var rightGrid = new RightGrid();
var leftTree = new LeftTree();
var searchAllFs = "供应商名称#gysmc#textField,法人代表#frdb#textField,企业类型#qylx#comboQylb,商务联系人#swlxr#textField"
		.split(',');
var searchDefaultFs = "供应商名称#gysmc#textField,法人代表#frdb#textField,企业类型#qylx#comboQylb,商务联系人#swlxr#textField"
		.split(',');
var searchPanel = new SearchPanel({
	region : 'north',
	searchDefaultFs : searchDefaultFs,
	searchAllFs : searchDefaultFs,
	txtWidth : 120,
	labelWidth : 80
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
