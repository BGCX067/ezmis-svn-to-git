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
	if (button.id == 'btnShenPiHt' || button.id == 'btnCheXiaoShenPi' || button.id == 'btnZuoFeiHt' || button.id == 'btnShanChuHt')
		button.setDisabled(true);
}

//起草项目
function btnQicaoHt_Click() {
	var url = link8 + "?flowConfigId=" + flowConfigId;
	showIFModule(url,"起草项目","true",735,582,{});
	rightGrid.getStore().reload();
}

//审批项目
function btnShenPiHt_Click() {
	if (rightGrid.getSelectionModel().getSelections().length != 1) {
		alert("请选取一条记录进行审批");
		return;
	}

	var node = leftTree.getSelectionModel().getSelectedNode();
	var select = rightGrid.getSelectionModel().getSelections()[0];
	if (node.id == 'dsp') {
		var id = select.json.TASKTODOID;
		var pid = select.json.FLOW_INSTANCE_ID;
		var token = select.json.TOKEN;

		// 弹出流程查看窗口
		var url = link10 + "?pid=" + pid + "&id=" + id + "&token=" + token + "&isEdit=true";
		alert(url);
		showIFModule(url,"查看流程","true",735,582,{});
		
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
		var url = link8 + "?pid=" + select.get("ID_");
		showIFModule(url,"查看流程","true",735,582,{});
		rightGrid.getStore().reload();
	} else {
		var url = link10 + "?pid=" + select.get("ID_") + "&status=false";
		showIFModule(url,"查看流程","true",735,582,{});
	}
}

//撤销审批
function btnCheXiaoShenPi_Click() {
	if (window.confirm('确认要撤销吗？')) {
		var url = link12;
		var select = rightGrid.getSelectionModel().getSelected();
		var pid = select.get('PROCESSINSTANCE_');
		var docid = select.get('ID');
		var flowConfigId = select.get('FLOW_CONFIG_ID');
		var formSn = tableName;
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

//作废项目
function btnZuoFeiHt_Click() {
	var select = rightGrid.getSelectionModel().getSelected();
	if (select.get('STATUS') != '起草项目') {
		alert('此项目不符合作废标准，无法作废');
		return;
	}
	
	if (window.confirm('是否作废此项目？')) {
		Ext.Ajax.request({
					url : link7,
					method : 'POST',
					params : {
						id : select.get('ID'),
						pid : select.get('FLOW_INSTANCE_ID'),
						tableName: tableName
					},
					success : function(ajax) {
						var responseText = ajax.responseText;
						var obj = Ext.decode(responseText);
						if (obj.success) {
							alert('项目作废成功');
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

//删除项目
function btnShanChuHt_Click() {
	if (rightGrid.getSelectionModel().getSelections().length < 1) {
		alert("请选择草稿箱中项目后再删除");
		return;
	}
	var url = contextPath + "/jteap/wfengine/wfi/WorkFlowInstanceAction!deleteProcessInstranceAction.do";
	var select = rightGrid.getSelectionModel().getSelections();
	var ids = getIds(select);
	var formIds = getFormIds(select);
	if (window.confirm("确认删除选中的项目草稿吗?")) {
		var myMask = showExtMask("项目草稿删除中，请稍候。。。");
		// 提交数据
		Ext.Ajax.request({
			url : url,
			success : function(ajax) {
				var responseText = ajax.responseText;
				var responseObject = Ext.decode(responseText);
				
				if (responseObject.success) {
					//删除项目表单数据
					Ext.Ajax.request({
						url : link9,
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
							ids : formIds,
							formSn: tableName
						}
					})
					
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

function getFormIds(select) {
	var ids = "";
	for (var i = 0; i < select.length; i++) {
		var temp = select[i];
		ids += temp.get("ID") + ",";
	}
	return ids;
}

var rightGrid = new RightGrid();
var leftTree = new LeftTree();

var searchAllFs = "项目编号#XMBH#textField,项目名称#XMMC#textField,申请部门#SQBM#comboBox,创建年份#CJSJ#yearField".split(",");
var searchDefaultFs = "项目编号#XMBH#textField,项目名称#XMMC#textField,申请部门#SQBM#comboBox,创建年份#CJSJ#yearField".split(",");
var searchPanel = new SearchPanel({
	searchDefaultFs : searchDefaultFs,
	searchAllFs : searchAllFs
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
