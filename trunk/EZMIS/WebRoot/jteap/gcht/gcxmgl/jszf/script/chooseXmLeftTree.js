var root=new Ext.tree.TreeNode({
	id : 'zfd',
	text:'支付单',
	leaf : false,
	expanded : true
});
var node1 = new Ext.tree.TreeNode({
	id : 'qkzfd',
	text : '未完成支付单',
	leaf : true
})
var node2 = new Ext.tree.TreeNode({
	id : 'yys',
	text : '已验收',
	leaf : true
})


root.appendChild(node1);
root.appendChild(node2);

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
    	if(oNode == root){
    		return;
    	}
    	var flbs = oNode.id;
    	var params = "";
	    rightGrid.changeToListDS(flbs, params);
	    rightGrid.getStore().reload();
    });
}


Ext.extend(LeftTree, Ext.tree.TreePanel, {
});

