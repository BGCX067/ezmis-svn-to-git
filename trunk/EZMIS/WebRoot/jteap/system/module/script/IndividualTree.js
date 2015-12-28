/**
 * 功能树组件
 */
var IndividualTree = function() {
	moduleTree = this;

	var root = new Ext.tree.AsyncTreeNode( {
		text : '所有模块',
		loader : new Ext.tree.TreeLoader( {
			dataUrl : link12
		}),
		expanded : true
	});
	
	root.on("load",function(){
		var oNode = moduleTree.getRootNode();
		oNode.select();
	})
	
	IndividualTree.superclass.constructor.call(this, {
		id : 'func-tree',
		region : 'west',
		title : '模块',
		split : true,
		width: 220,
        minSize: 180,
        maxSize: 400,
		collapsible : true,
		margins:'2px 2px 2px 2px',
        cmargins:'0 0 0 0',
		enableDD : false,
		rootVisible : true,
		lines : false,
		autoScroll : true,
		tbar : [{
			text : '刷新',
			handler : function() {
				moduleTree.getRootNode().reload();
			}
		}],
		root : root,
		collapseFirst : true
	});

	// 选择节点的时候触发事件
	this.getSelectionModel().on("selectionchange", function(oSM, oNode) {
		var btnDelModule = mainToolbar.items.get("btnDelModule");
		var btnReset = mainToolbar.items.get("btnReset");
		if (oNode) {
			var resStyle = oNode.attributes.resStyle;
			// 系统模块
			if (resStyle == 0) {
				btnDelModule.setDisabled(true);
				btnReset.setDisabled(false);
			} else {
				btnDelModule.setDisabled(false);
				btnReset.setDisabled(true);
			}
			if (oNode.isRootNode())
				btnReset.setDisabled(true);
		} else {
			btnDelModule.setDisabled(true);
		}

		if (oNode && !oNode.isRootNode()) {
			individualDetailForm.loadData(oNode);
		}
	});

	/**
	 * 删除模块
	 */
	this.delModule = function() {
		// debugger;
		var oNode = this.getSelectionModel().getSelectedNode();
		if (!oNode) {
			alert("请选择一个节点");
		} else {
			if (oNode.isRootNode()) {
				alert("不能删除根模块");
				return;
			}
			if (window.confirm("确认删除当前模块吗")) {
				// 提交数据
				Ext.Ajax
						.request( {
							url : link3,
							success : function(ajax) {
								var responseText = ajax.responseText;
								var responseObject = Ext.util.JSON
										.decode(responseText);
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
};

Ext.extend(IndividualTree, Ext.tree.TreePanel, {

});