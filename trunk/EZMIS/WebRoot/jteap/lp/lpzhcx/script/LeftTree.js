var root=new Ext.tree.TreeNode({
	id : 'qc',
	text:'全厂查询',
	leaf : false,
	expanded : true
});
var node1 = new Ext.tree.TreeNode({
	id : 'zzcx',
	text : '值长查询',
	leaf : true
})
var node2 = new Ext.tree.TreeNode({
	id : 'FDB',
	text : '运行查询',
	leaf : true
})
var node3 = new Ext.tree.TreeNode({
	id : 'JXB',
	text : '检修查询',
	leaf : true
})
var node4 = new Ext.tree.TreeNode({
	id : 'RYB',
	text : '燃运查询',
	leaf : true
})
var node5 = new Ext.tree.TreeNode({
	id : 'grcx',
	text : '个人查询',
	leaf : true
})
root.appendChild(node1);
root.appendChild(node2);
root.appendChild(node3);
root.appendChild(node4);
root.appendChild(node5);

/**
 * 左边的树
 */
LeftTree= function(){
	LeftTree.superclass.constructor.call(this, {
        id:'statusTree',
        region:'west',
        width: 130,
       	margins:'2px 2px 2px 2px',
        lines:true,
        autoScroll:true,
 		root:root
    });
    
    /**
     * 当选中节点的时候，触发事件
     */
    this.getSelectionModel().on("selectionchange",function(oSM,oNode){
    	// 重新绑定searchPanel中发现班组的数据源，根据部门来过滤
    	var urlBz = "";
    	if (oNode.id != "FDB" && oNode.id != "RYB" && oNode.id != "JXB") {
    		urlBz = link2 + "?cxid=" + oNode.id;
    	} else {
    		urlBz = link2 + "?groupSn=" + oNode.id;
    	}
    	// 创建部门
    	searchPanel.items.get(0).items.items[2].items.items[0].view.loader.dataUrl = urlBz;
    	searchPanel.items.get(0).items.items[2].items.items[0].view.root.reload();
    	var url = link1 + "?cxid=" + oNode.id;;
	    rightGrid.changeToListDS(url);
	    rightGrid.getStore().reload();
    });
}

Ext.extend(LeftTree, Ext.tree.TreePanel, {
});

