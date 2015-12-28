var root = new Ext.tree.AsyncTreeNode({
			id : 'root',
			text : '全部',
			loader : new Ext.tree.TreeLoader({
						dataUrl : link1,
						listeners : {
							beforeload : function(loader, node, callback) {
								this.baseParams.parentId = (node.isRoot ? "" : node.id);
							}
						}

					}),
			expanded : true
		})
/**
 * 左边的树
 */
LeftTree = function() {
	LeftTree.superclass.constructor.call(this, {
				region : 'west',
				width : 200,
				margins : '2px 2px 2px 2px',
				lines : false,
				autoScroll : true,
				rootVisible : false,
				root : root
			});

	/**
	 * 当选中节点的时候，触发事件
	 */
	this.getSelectionModel().on("selectionchange", function(oSM, oNode) {
				var jhId = oNode.id;
				var btnAddXm = mainToolbar.items.get('btnAddXm');
				var btnExport = mainToolbar.items.get('btnExport');
				if (jhId.lastIndexOf("@") != -1) {
					if (btnAddXm) {
						btnAddXm.setDisabled(true);
					}
					if (btnExport) {
						btnExport.setDisabled(true);
					}
					return;
				}
				if (btnAddXm) {
					btnAddXm.setDisabled(false);
				}
				if (btnExport) {
					btnExport.setDisabled(false);
				}
				var url = link2 + "?jhId=" + jhId;
				rightGrid.changeToListDS(url);
				rightGrid.getStore().reload();
			});
}

Ext.extend(LeftTree, Ext.tree.TreePanel, {});
