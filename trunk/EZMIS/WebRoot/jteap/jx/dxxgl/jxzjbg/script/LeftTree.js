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
				jhId = oNode.id;
				var btnEditBg = mainToolbar.items.get('btnEditBg');
				var btnDelBg = mainToolbar.items.get('btnDelBg');
				var btnSaveBg = mainToolbar.items.get('btnSaveBg');

				if (jhId.lastIndexOf("@") != -1) {
					if (btnEditBg) {
						btnEditBg.setDisabled(true);
					}
					if (btnDelBg) {
						btnDelBg.setDisabled(true);
					}
					if (btnSaveBg) {
						btnSaveBg.setDisabled(true);
					}
					return;
				}
				if (btnEditBg) {
					btnEditBg.setDisabled(false);
				}
				if (btnDelBg) {
					btnDelBg.setDisabled(false);
				}
				if (btnSaveBg) {
					btnSaveBg.setDisabled(true);
				}
				rightForm.setFormDisabled(true);
				rightForm.loadData();
			});
}

Ext.extend(LeftTree, Ext.tree.TreePanel, {});
