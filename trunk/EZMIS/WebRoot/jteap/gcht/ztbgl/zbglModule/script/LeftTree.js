var treeEditor = null;
var confirmField = new Ext.app.ConfirmTextField({
	width : 160
});
var bFlag = false;
confirmField.on("confirmInput", function() {
	// 此处进行AJAX操作，确定服务器后台操作成功
		bFlag = true;
		treeEditor.hide();
	});

confirmField.on("blur", function() {
	bFlag = true;
})

confirmField.on("cancelInput", function(oField) {
	bFlag = false;
	var node = treeEditor.editNode;
	treeEditor.cancelEdit(false);
	node.remove();
});

/**
 * 专业分类树
 */
LeftTree = function() {
	var tree = this;
	var rootLoader = new Ext.tree.AsyncTreeNode({
		text : '招标类别',
		loader : new Ext.tree.TreeLoader({
			dataUrl : link2,
			listeners : {
				beforeload : function(loader, node, callback) {
					this.baseParams.parentId = (node.isRoot ? "" : node.id);
				}
			}
		}),
		expanded : true
	});

	rootLoader.on("load", function() {
		var selNode;
		if (!selNode)
			selNode = tree.getRootNode();
		selNode.select();
	});
	LeftTree.superclass.constructor.call(this, {
		id : 'tree',
		region : 'west',
		title : '招标类别',
		split : false,
		width : 140,
		minSize : 180,
		maxSize : 400,
		collapsible : true,
		margins : '2px 2px 2px 2px',
		cmargins : '0 0 0 0',
		rootVisible : true,
		lines : false,
		autoScroll : true,
		root : rootLoader,
		tbar : [{
			text : '刷新',
			handler : function() {
				tree.getRootNode().reload();
			}
		}]
	});

	/**
	 * 当选中节点的时候，触发事件
	 */
	this.getSelectionModel().on("selectionchange", function(oSM, oNode) {
		if (oNode && !oNode.isRootNode()) {
			var url = link1 + "?zbflId=" + oNode.id;
			rightGrid.changeToListDS(url);
			rightGrid.getStore().reload();
		} else {
			var url = link1;
			rightGrid.changeToListDS(url);
			rightGrid.getStore().reload();
		}
	});
};

Ext.extend(LeftTree, Ext.tree.TreePanel, {});