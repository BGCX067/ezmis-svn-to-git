/**
 * 专业分类树
 */
LeftTree = function() {
	var tree = this;
	var rootLoader = new Ext.tree.AsyncTreeNode({
		text : '燃料合同类别',
		loader : new Ext.tree.TreeLoader({
			dataUrl : link1,
			listeners : {
				beforeload : function(loader, node, callback) {
					this.baseParams.parentId = (node.isRoot ? "" : node.id);
					this.baseParams.flbm = "RL";
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
		title : '燃料合同类别',
		split : false,
		width : 220,
		minSize : 180,
		maxSize : 400,
		collapsible : true,
		margins : '2px 2px 2px 2px',
		cmargins : '0 0 0 0',
		rootVisible : true,
		lines : false,
		autoScroll : true,
		root : rootLoader
	});

	/**
	 * 当选中节点的时候，触发事件
	 */
	this.getSelectionModel().on("selectionchange", function(oSM, oNode) {
		if (oNode && !oNode.isRootNode()) {
			var queryParams = "a.htlx = '"+oNode.text+"'";
			var url = link2 + "&queryParamsSql=" +encodeURIComponent(queryParams);
			rightGrid.changeToListDS(url);
			rightGrid.getStore().reload();
		} else {
			var url = link2;
			rightGrid.changeToListDS(url);
			rightGrid.getStore().reload();
		}
	});

};

Ext.extend(LeftTree, Ext.tree.TreePanel, {});