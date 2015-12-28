var root=new Ext.tree.TreeNode({
	id : 'qc',
	text:'全厂',
	leaf : false,
	expanded : true
});
var node1 = new Ext.tree.TreeNode({
	id : 'dsp',
	text : '待审批',
	leaf : true
})
var node2 = new Ext.tree.TreeNode({
	id : 'ysp',
	text : '已审批',
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
var node5 = new Ext.tree.TreeNode({
	id : 'zj',
	text : '终结',
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
    	var status = oNode.id;
    	var params = "?tableName="+tableName+"&htcjsj="+new Date().format("Y");
	    rightGrid.changeToListDS(oNode.id, params);
	    rightGrid.getStore().reload();
    });
}


Ext.extend(LeftTree, Ext.tree.TreePanel, {
});

