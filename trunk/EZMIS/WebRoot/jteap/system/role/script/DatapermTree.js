// chooseType : "single"为单选，"multipe"为多选
// checkPath : true为选择完整路径，false为只显示节点文字
// checkLeaf : true为只选择叶子节点，false可以选择非叶子节点
DatapermTree = function(chooseType, checkPath, checkLeaf ,roleid) {
	this.checkPath = checkPath;
	var root;
	var bbar;

	// 单选或者只能选择末端节点时，只用一般AsyncTreeNode
	if (chooseType == 'single' || checkLeaf) {
		root = new Ext.tree.AsyncTreeNode( {
			text : '我的数据权限',
			checked : false,
			loader : new Ext.tree.TreeLoader( {
				dataUrl : link18+"?roleid="+roleid
			}),
			expanded : true
		});
	} else {
		// 可以多选或者可以选择非末端节点时，使用共通的CheckboxTreeNode
		root = new Ext.app.CheckboxAsyncTreeNode( {
			// 级联
			ccCheck : true,
			text : '我的数据权限',
			loader : new Ext.app.CheckboxTreeNodeLoader( {
				dataUrl : link18+"?roleid="+roleid
			}),
			expanded : true
		});
		// bbar提示：“按住CTRL键可进行级联选择”
		bbar = ['-', '<font color="blue">*按住CTRL键可进行级联选择</font>', '-'];
	}

	DatapermTree.superclass.constructor.call(this, {
		id : 'pTree',
		autoScroll : true,
		autoHeight : false,
		height : 170,
		width : 150,
		originalValue : "",
		animate : false,
		ctrlCasecade : true, // 是否只支持 按住ctrl键进行勾选的时候是级联勾选
			enableDD : true,
			containerScroll : true,
			defaults : {
				bodyStyle : 'padding:0px'
			},
			border : false,
			hideBorders : true,
			rootVisible : true,
			lines : false,
			bbar : bbar,
			bodyBorder : false,
			root : root
		});

	this.on("checkchange", function(t) {
		// 只能选择末端节点时，将选中非末端节点功能屏蔽掉
			if (checkLeaf) {
				if (!t.isLeaf()) {
					var ui = t.getUI();
					ui.checkbox.checked = false;
				}
			}

			// 只能单选时，设置单选属性
			if (chooseType == "single") {
				var checkedNodes = this.getChecked();
				for (var i = 0;i < checkedNodes.length; i++) {
					var node = checkedNodes[i];
					if (node.id != t.id) {
						node.getUI().checkbox.checked = false;
						node.attributes.checked = false;
						this.fireEvent('check', node, false);
					}
				}
			}

		})

}

Ext.extend(DatapermTree, Ext.tree.TreePanel, {

	checkPath : false,

	// 获得选中节点ID和TEXT
		getRoleNameAndId : function() {
			var result = "";
			// 获得选中节点ID和TEXT
		result = this.getNodeNameAndId(this.getRootNode());
		// 去掉末端的“,”
		result = result.substring(0, result.length - 1);
		return result;
	},

	// 递归获得选中节点的ID和TEXT
	getNodeNameAndId : function(node) {
		var result = "";
		var isRoot = (node.getOwnerTree().getRootNode().id == node.id);
		var ui = node.getUI();
		var isChecked = ui.checkbox.checked;

		// 不是根节点，并且被选中
		if (isChecked) {
			result += "{";
			result += "'id':'" + node.id + "',";

			var text = "";
			// 是否需要完整路径
			if (this.checkPath) {
				text = this.getPathName(node);
			} else {
				text = node.text;
			}

			result += "'text':'" + text + "'";
			result += "},";

		}

		for (var i = 0;i < node.childNodes.length; i++) {
			var tmp = this.getNodeNameAndId(node.childNodes[i]);
			if (tmp != "") {
				result += tmp;
			}
		}

		return result;
	},

	// 获得节点完整路径
	getPathName : function(node) {
		var pathName = "";
		pathName = node.text;
		if (node.parentNode) {
			pathName = this.getPathName(node.parentNode) + "/" + pathName;
		}
		return pathName;
	}
});