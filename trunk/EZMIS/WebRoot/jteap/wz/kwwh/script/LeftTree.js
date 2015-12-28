
//加载table节点的loader
var tableLoader=new Ext.tree.TreeLoader({
	dataUrl:link18
});

//添加数据源对象参数
tableLoader.on("beforeload", function(treeLoader, node) {
        treeLoader.baseParams.datasource = node.attributes.datasource;
    }, this);

//to do in the program
var rootLoader=new Ext.tree.AsyncTreeNode({
	text:'仓库',
	loader:tableLoader,
	type:'table',
    expanded :true
});
/**
 * 左边的树
 */
LeftTree= function(){
	LeftTree.superclass.constructor.call(this, {
        id:'tableTree',
        region:'west',
        split:true,
        width: 220,
        minSize: 180,
        maxSize: 400,
        collapsible: true,
        margins:'2px 2px 2px 2px',
        cmargins:'0 0 0 0',
        enableDD:true,
        rootVisible:true,
        lines:true,
        autoScroll:true,
 		root:rootLoader,
 		tbar:[{
 			icon:'../../../resources/images/hb3e/grid/refresh.gif',
 			iconCls:'x-icon-style',
 			handler:function(){
 				var selectionNode=leftTree.getSelectionModel().getSelectedNode();
 				if(selectionNode){
 					try{
 						//leftTree.getLoader().load(selectionNode);
						//selectionNode.oTreeNode.expand();
 						selectionNode.reload();
 					}catch(e){
 						//alert(e);
 					}
 				}else{
 					leftTree.getRootNode().reload();
 				}
 			}
 		}],
        collapseFirst:true
    });
    
  
    /**
     * 当选中节点的时候，触发事件
     */
    this.getSelectionModel().on("selectionchange",function(oSM,oNode){
		if(oNode){
    		//kwTreeGrid.store.removeAll();
    		kwTreeGrid.store.proxy.conn.url = link19+"?ckid="+oNode.id;
    		kwTreeGrid.store.reload({callback :function(r){
    			kwTreeGrid.store.removeAll();
    			kwTreeGrid.store.add(r);
    		}});
    	}
    });

}


Ext.extend(LeftTree, Ext.tree.TreePanel, {
});

