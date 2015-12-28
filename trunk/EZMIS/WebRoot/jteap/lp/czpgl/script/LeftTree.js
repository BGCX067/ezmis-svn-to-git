var root=new Ext.tree.TreeNode({
	id : 'qc',
	text:'全厂',
	leaf : false,
	expanded : true
});
var node1 = new Ext.tree.TreeNode({
	id : 'dcl',
	text : '待处理',
	leaf : true
})
var node2 = new Ext.tree.TreeNode({
	id : 'ycl',
	text : '已处理',
	leaf : true
})
var node3 = new Ext.tree.TreeNode({
	id : 'cgx',
	text : '草稿箱',
	leaf : true
})
var node4 = new Ext.tree.TreeNode({
	id : 'zf',
	text : '作废',
	leaf : true
})

root.appendChild(node1);
root.appendChild(node2);
root.appendChild(node3);
root.appendChild(node4);

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
    	var status = oNode.id;
	    rightGrid.changeToListDS(oNode.id, "");
	    rightGrid.getStore().reload();
    });
}


Ext.extend(LeftTree, Ext.tree.TreePanel, {
});

