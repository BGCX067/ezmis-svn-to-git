// 工具栏
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
	if (button.id == 'btnProcess' || button.id == 'btnCancel' || button.id == 'btnDelSq' || button.id == 'btnUndo')
		button.setDisabled(true);
}

/**
 * 历史票导入
 */
function btnImpHistory_Click() {
	
}

/**
 * 标准票申请
 */
function btnAddBzp_Click() {
	var result = showModule(link11, "yes", 680, 250);
	if (result == true) {
		rightGrid.getStore().reload();
	}
}

/**
 * 查看
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
		var windowUrl = link10 + "?pid=" + pid + "&id=" + id + "&token=" + token + "&isEdit=true";
		var args = "url|" + windowUrl + ";title|" + '查看流程';
		var retValue = showModule(url, "yes", 1100, 650, args);

		// 进行释放签收操作
		Ext.Ajax.request({
					url : link4,
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
		var retValue = showModule(url, "yes", 1100, 650, args);
		rightGrid.getStore().reload();
	} else {
		var url = link9;
		var windowUrl = link10 + "?pid=" + select.get("ID_") + "&status=false";
		var args = "url|" + windowUrl + ";title|" + '查看流程';
		var retValue = showModule(url, "yes", 1100, 650, args);
	}
}

/**
 * 撤销
 */
function btnUndo_Click() {
	if (window.confirm('确认要撤销吗？')) {
		var url = link12;
		var select = rightGrid.getSelectionModel().getSelected();
		var pid = select.get('PROCESSINSTANCE_');
		var docid = select.get('ID');
		var flowConfigId = select.get('FLOW_CONFIG_ID');
		var formSn = select.data.TABLENAME;;
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
				rightGrid.getStore().reload();
			},
			failure : function(ajax) {
				var html = ajax.responseText;
				var reg = /'[^ ']*'/;
				var result = reg.exec(html);
				rightGrid.getStore().reload();
			}
		})
	}
}

/**
 * 作废
 */
function btnCancel_Click() {
	var select = rightGrid.getSelectionModel().getSelected();
	if (select.get('STATUS') != '操作票申请') {
		alert('此操作票不符合作废标准，无法作废');
		return;
	}
	if (window.confirm('是否作废此申请单？')) {
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
							alert('操作票作废成功');
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
 * 删除草稿
 */
function btnDelSq_Click() {
	if (rightGrid.getSelectionModel().getSelections().length < 1) {
		alert("请选择草稿箱中操作票后再删除");
		return;
	}
	var url = contextPath + "/jteap/wfengine/wfi/WorkFlowInstanceAction!deleteProcessInstranceAction.do";
	var select = rightGrid.getSelectionModel().getSelections();
	var ids = getIds(select);
	if (window.confirm("确认删除选中的操作票草稿吗?")) {
		var myMask = showExtMask("操作票草稿删除中，请稍候。。。");
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

// 左边的树 右边的grid
var rightGrid = new RightGrid();
var leftTree = new LeftTree();

// 中间
var lyCenter = {
	layout : 'border',
	id : 'center-panel',
	region : 'center',
	minSize : 175,
	maxSize : 400,
	border : false,
	margins : '0 0 0 0',
	items : [rightGrid]
}

// 北边
var lyNorth = {
	id : 'north-panel',
	region : 'north',
	height : 27,
	border : false,
	items : [mainToolbar]
}
