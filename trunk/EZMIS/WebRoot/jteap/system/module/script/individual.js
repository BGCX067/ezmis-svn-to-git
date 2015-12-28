// 主工具栏
var mainToolbar = new Ext.Toolbar( {
	height : 26,
	items : [ {
		id : 'btnAddModule',
		text : '添加自定义模块',
		cls : 'x-btn-text-icon',
		icon : 'icon/icon_6.gif',
		listeners : {
			click : btnAddModule_Click
		}
	}, {
		id : 'btnDelModule',
		text : '删除自定义模块',
		disabled : true,
		cls : 'x-btn-text-icon',
		icon : 'icon/icon_2.gif',
		listeners : {
			click : btnDelModule_Click
		},
		tooltip : {
			title : '删除自定义模块',
			text : '选中需要删除的模块，再执行该操作。只能删除自定义模块'
		}
	}, '-', {
		id : 'btnReset',
		text : '恢复默认',
		disabled : true,
		cls : 'x-btn-text-icon',
		icon : 'icon/icon_16.gif',
		listeners : {
			click : btnReset_Click
		},
		tooltip : {
			title : '恢复模块默认值',
			text : '选中需要恢复的模块，再执行该操作。只能恢复系统模块'
		}
	}, '-', {
		id : 'btnAddQuickLink',
		text : '添加快速链接',
		disabled : false,
		cls : 'x-btn-text-icon',
		icon : 'icon/icon_6.gif',
		listeners : {
			click : btnAddQuickLink_Click
		},
		tooltip : {
			title : '添加快速链接'
		}
	},'-',{
		id : 'btnAddDocLink',
		text : '添加文档链接',
		disabled : false,
		cls : 'x-btn-text-icon',
		icon : 'icon/icon_6.gif',
		listeners : {
			click : btnAddDocLink_Click
		},
		tooltip : {
			title : '添加文档链接'
		}
	}]
});

var individualTree = new IndividualTree();

/**
 * 添加自定义模块
 */
function btnAddModule_Click() {
	var oNode = individualTree.getSelectionModel().getSelectedNode();
	individualDetailForm.reset(oNode);
}

/**
 * 删除自定义模块
 */
function btnDelModule_Click() {
	if (window.confirm('确认要删除此模块吗?')) {
		individualTree.delModule();
	}
}

/**
 * 恢复系统默认值
 */
function btnReset_Click() {
	if (window.confirm('确认要恢复默认值吗?')) {
		var oCurrentModule = individualTree.getSelectionModel()
				.getSelectedNode();

		Ext.Ajax.request( {
			url : link15,
			method : 'post',
			params : {
				resourceId : oCurrentModule.id
			},
			success : function(ajax) {
				var responseText = ajax.responseText;
				var obj = Ext.decode(responseText);
				if (obj.success) {
					alert("恢复默认值完成");
					individualTree.getRootNode().reload(function() {
						var node = individualTree
								.getNodeById(oCurrentModule.id)
						node.select();
					});
				} else {
					alert("恢复默认值失败");
				}
			},
			failure : function() {
				alert("操作失败，请联系管理员");
			}
		})
	}
}

/**
 * 添加快速链接
 */
function btnAddQuickLink_Click() {
	individualDetailForm.setupLink();
}

/**
 * 添加文档链接
 */
function btnAddDocLink_Click() {
	individualDetailForm.setupDocLink();
}

// 模块信息
var individualDetailForm = new IndividualDetailForm();

// 中间
var lyCenter = {
	layout : 'border',
	id : 'center-panel',
	region : 'center',
	minSize : 175,
	maxSize : 400,
	border : false,
	margins : '0 0 0 0',
	items : [individualDetailForm],
	check : function(oNode) {
		individualDetailForm.showDetailPanel(oNode);
	}
}

// 北边
var lyNorth = {
	id : 'north-panel',
	region : 'north',
	height : 27,
	border : false,
	items : [mainToolbar]
}