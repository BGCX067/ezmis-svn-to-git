//根节点加载器
var rootLoader=new Ext.tree.TreeLoader({
	dataUrl:link1,
    listeners:{
    	beforeload:function(loader, node, callback ){
    		this.baseParams.parentId=(node.isRoot?"":node.id);
    	}
    }
});

var rootNode=new Ext.tree.AsyncTreeNode({
	text:'所有分类',
	loader:rootLoader,
    expanded :true
});

rootNode.on("load",function(){
	var selNode=groupTree.getRootNode();
	selNode.select();
});


/**
 * 组织树
 */
GroupTree= function(){
	
	GroupTree.superclass.constructor.call(this, {
		result:{},
        id:'groupTree',
        region:'center',
        width: 295,
        minSize: 180,
        maxSize: 300,
        collapsible: true,
        margins:'2px 2px 2px 2px',
        cmargins:'0 0 0 0',
        enableDD:false,
        rootVisible:false,
        lines:true,
        autoScroll:true,
 		root:rootNode,
 		tbar:[{
 			text:'刷新',
 			handler:function(){
 				rootNode.reload();
 			}
 		}],
        collapseFirst:false
    });
    
	     
    /**
     * 当选中节点的时候，触发事件
     */
    this.getSelectionModel().on("selectionchange",function(oSM,oNode){
    	//修改菜单栏按钮状态
    	if(oNode!=null){
    		this.tree.result.id = oNode.id;
    		this.tree.result.text = oNode.text;
    	}else this.result=null;

    });
}


Ext.extend(GroupTree, Ext.tree.TreePanel, {

});

