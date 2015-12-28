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
				text : '所有专业',
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
				var btnModiZyfl = mainToolbar.items.get("btnModiZyfl");
				var btnDelZyfl = mainToolbar.items.get("btnDelZyfl");
				var btnAddBpbj = mainToolbar.items.get("btnAddBpbj");
				if (oNode && !oNode.isRootNode()) {
					if (oNode.getDepth() == 2) {
						if (btnAddBpbj)
							btnAddBpbj.setDisabled(true);
					} else {
						if (btnAddBpbj)
							btnAddBpbj.setDisabled(false);
					}
					if (btnModiZyfl)
						btnModiZyfl.setDisabled(false);
					if (btnDelZyfl)
						btnDelZyfl.setDisabled(false);
					var url = link1 + "?zyflId=" + oNode.id;
					rightGrid.changeToListDS(url);
					rightGrid.getStore().reload();
				} else {
					if (btnAddBpbj)
						btnAddBpbj.setDisabled(true);
					if (btnModiZyfl)
						btnModiZyfl.setDisabled(true);
					if (btnDelZyfl)
						btnDelZyfl.setDisabled(true);

					var url = link1;
					rightGrid.changeToListDS(url);
					rightGrid.getStore().reload();
				}
			});

	// 初始化编辑器
	treeEditor = new Ext.app.ExTreeEditor(this, confirmField);

	treeEditor.on("beforecomplete", function(editor, value, oldValue) {
				var preSortNo

				var previousSiblingNode = editor.editNode.previousSibling;
				if (previousSiblingNode != null && previousSiblingNode.attributes.sortNo) {
					preSortNo = previousSiblingNode.attributes.sortNo;
				} else {
					preSortNo = 0;
				}

				if (value.length < 1) {
					alert('名字不符合规则');
					return false;
				}

				if (bFlag) {
					// 创建新的专业
					var parentId = "";
					var parentNode = editor.editNode.parentNode;
					var zyId = editor.editNode.id;
					if (!parentNode.isRoot) {
						parentId = parentNode.id;
					}
					editor.editNode.attributes.sortNo = parseInt(preSortNo) + 1000
					// 提交数据
					Ext.Ajax.request({
								url : link3,
								success : function(ajax) {
									var responseText = ajax.responseText;
									var responseObject = Ext.util.JSON.decode(responseText);
									editor.editNode.id = responseObject.id;
									if (responseObject.success)
										alert("保存分类【" + value + "】成功");
									else
										alert("保存分类【" + value + "】失败");
								},
								failure : function() {
									alert("提交失败");
								},
								method : 'POST',
								params : {
									id : zyId,
									parentId : parentId,
									zymc : value,
									preSortNo : preSortNo
								}
							})
					bFlag = false;
				}
			});

};

Ext.extend(LeftTree, Ext.tree.TreePanel, {
			createZyfl : function(bFirst) {
				var oNode = this.getSelectionModel().getSelectedNode();

				if (!oNode) {
					oNode = this.getRootNode();
				}

				var oNewNode;
				var node = new Ext.tree.TreeNode({
							text : '新建专业'
						});
				if (bFirst)
					oNewNode = oNode.insertBefore(node, oNode.childNodes[0]);
				else
					oNewNode = oNode.appendChild(node);
				// 如果父节点未展开，则需要留出一定的时间给节点展开，否则编辑器定位有问题
				if (!oNode.expanded) {
					oNode.expand();
					oNewNode.select();
					// 给展开时间
					setTimeout(function() {
								treeEditor.editNode = oNewNode;
								treeEditor.startEdit(oNewNode.ui.textNode);
							}, 300);
				} else {
					treeEditor.editNode = oNewNode;
					treeEditor.startEdit(oNewNode.ui.textNode);
				}
			},

			/**
			 * 修改专业
			 */
			modifyZyfl : function() {
				var oNode = this.getSelectionModel().getSelectedNode();

				if (!oNode) {
					Ext.Msg.alert('提示', "请选择一个节点");
				} else {
					if (!oNode.isRootNode()) {
						treeEditor.editNode = oNode;
						treeEditor.startEdit(oNode.ui.textNode);
					} else {
						alert("不能修改当前专业,如有疑问，请联系系统管理员");
					}
				}
			},

			/**
			 * 删除专业
			 */
			delZyfl : function() {
				var oNode = this.getSelectionModel().getSelectedNode();
				if (!oNode) {
					alert("请选择一个节点");
				} else {
					if (oNode == this.getRootNode()) {
						alert("不能删除根节点");
						return;
					}
					if (window.confirm("确认删除当前专业分类吗")) {
						// 提交数据
						Ext.Ajax.request({
									url : link4,
									success : function(ajax) {
										var responseText = ajax.responseText;
										var responseObject = Ext.util.JSON.decode(responseText);
										if (responseObject.success) {
											alert("删除成功");
											oNode.remove();
										} else {
											alert(responseObject.msg);
										}
									},
									failure : function() {
										alert("提交失败");
									},
									method : 'POST',
									params : {
										nodeId : oNode.id
									}
								})
					}
				}
			}
		});