
//加载schema节点的loader
var schemaLoader=new Ext.tree.TreeLoader({
	dataUrl:link1,
   	applyLoader:false			//表示下级节点采用动态产生loader的方式，以返回的数据决定下一个loader是哪个loader
});

//加载table节点的loader
var tableLoader=new Ext.tree.TreeLoader({
	dataUrl:link2
});


//to do in the program
var rootLoader=new Ext.tree.AsyncTreeNode({
	text:'所有表',
	loader:schemaLoader,
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
        width: 180,
        minSize: 180,
        maxSize: 400,
        collapsible: true,
        margins:'0 0 1 1',
        cmargins:'0 0 0 0',
        enableDD:true,
        rootVisible:true,
        lines:true,
        autoScroll:true,
 		root:rootLoader,
 		tbar:[{
 			text:'刷新',
 			handler:function(){
 				var selectionNode=leftTree.getSelectionModel().getSelectedNode();
 				if(selectionNode){
 					try{
 						selectionNode.reload();
 					}catch(e){}
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
    	
    });
    
    
}


Ext.extend(LeftTree, Ext.tree.TreePanel, {
});

