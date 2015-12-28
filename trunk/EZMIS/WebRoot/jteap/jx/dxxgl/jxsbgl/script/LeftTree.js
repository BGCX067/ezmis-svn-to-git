/**
 * 专业分类树
 */
LeftTree = function() {
	var tree = this;
	var rootLoader = new Ext.tree.AsyncTreeNode({
				text : '所有专业',
				loader : new Ext.tree.TreeLoader({
							dataUrl : link1
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
				title : '专业分类',
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
				var btnAddJxsb = mainToolbar.items.get("btnAddJxsb");

				if (oNode && !oNode.isRootNode()) {
					if (btnAddJxsb)
						btnAddJxsb.setDisabled(false);
					var url = link2 + "?sszy=" + oNode.attributes.value;
					rightGrid.changeToListDS(url);
					rightGrid.getStore().reload();
				} else {
					if (btnAddJxsb)
						btnAddJxsb.setDisabled(true);

					var url = link2;
					rightGrid.changeToListDS(url);
					rightGrid.getStore().reload();
				}
			});
};

Ext.extend(LeftTree, Ext.tree.TreePanel, {});