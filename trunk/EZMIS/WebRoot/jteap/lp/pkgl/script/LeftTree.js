var root = new Ext.tree.TreeNode({
	id : 'pk',
	text : '票库',
	leaf : false,
	expanded : true
});
var node1 = new Ext.tree.TreeNode({
	id : 'gzp',
	text : '工作票票库',
	leaf : true
})
var node2 = new Ext.tree.TreeNode({
	id : 'czp',
	text : '操作票票库',
	leaf : true
})
var node3 = new Ext.tree.TreeNode({
	id : 'dbzp',
	text : '待标准票票库',
	leaf : true
})
var node4 = new Ext.tree.TreeNode({
	id : 'lsp',
	text : '临时票票库',
	leaf : true
})

root.appendChild(node1);
root.appendChild(node2);
root.appendChild(node4);
root.appendChild(node3);

/**
 * 左边的树
 */
LeftTree = function() {
	LeftTree.superclass.constructor.call(this, {
		id : 'statusTree',
		region : 'west',
		width : 150,
		margins : '2px 2px 2px 2px',
		lines : true,
		autoScroll : true,
		root : root
	});

	/**
	 * 当选中节点的时候，触发事件
	 */
	this.getSelectionModel().on("selectionchange", function(oSM, oNode) {
		var url = link1 + (oNode.isRootNode() ? "" : "?pfl=" + oNode.id);
		rightGrid.changeToListDS(url);
		rightGrid.getStore().reload();
	});
}

Ext.extend(LeftTree, Ext.tree.TreePanel, {});
