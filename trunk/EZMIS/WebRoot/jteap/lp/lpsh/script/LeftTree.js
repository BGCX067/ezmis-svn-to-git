var rootLoader = new Ext.tree.TreeLoader({
	dataUrl : link1
})

var root = new Ext.tree.AsyncTreeNode({
	id : 'qcp',
	text : '全厂票',
	loader : rootLoader
})

root.on('load', function() {
	root.select();
})
/**
 * 左边的树
 */
LeftTree = function() {
	LeftTree.superclass.constructor.call(this, {
		id : 'statusTree',
		region : 'west',
		width : 200,
		margins : '2px 2px 2px 2px',
		lines : true,
		expand : true,
		autoScroll : true,
		root : root
	});

	/**
	 * 当选中节点的时候，触发事件
	 */
	this.getSelectionModel().on("selectionchange", function(oSM, oNode) {
		var url = link2 + "?pfl=" + oNode.attributes.pfl + (oNode.isRootNode() ? "" : "&pzid=" + oNode.id)
		rightGrid.changeToListDS(url);
		rightGrid.getStore().reload();
	});
}

Ext.extend(LeftTree, Ext.tree.TreePanel, {});
