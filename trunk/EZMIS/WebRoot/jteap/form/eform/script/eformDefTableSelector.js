var pWin = window.dialogArguments.opener;

//加载数据源节点的loader
var dataSourceLoader=new Ext.tree.TreeLoader({
	dataUrl:link3,
   	applyLoader:false			//表示下级节点采用动态产生loader的方式，以返回的数据决定下一个loader是哪个loader
});

//加载schema节点的loader
var schemaLoader=new Ext.tree.TreeLoader({
	dataUrl:link1,
   	applyLoader:false			//表示下级节点采用动态产生loader的方式，以返回的数据决定下一个loader是哪个loader
});

//加载table节点的loader
var tableLoader=new Ext.tree.TreeLoader({
	dataUrl:link2
});
//添加数据源对象参数 
tableLoader.on("beforeload", function(treeLoader, node) {
    }, this);

//to do in the program
var rootLoader=new Ext.tree.AsyncTreeNode({
	text:'所有表',
	id:'rootNode',
	loader:tableLoader,
    expanded :true
});

/**
 * 左边的树
 */
DefTableTree= function(){
	var defTableTree=this;
	DefTableTree.superclass.constructor.call(this, {
        id:'tableTree',
        region:'center',
        split:true,
        width: 180,
        minSize: 180,
        maxSize: 400,
        collapsible: true,
        margins:'0 0 1 1',
        cmargins:'0 5 5 5',
        enableDD:true,
        rootVisible:true,
        lines:true,
        autoScroll:true,
 		root:rootLoader,
 		tbar:[{
 			text:'刷新',
 			handler:function(){
 				var selectionNode=defTableTree.getSelectionModel().getSelectedNode();
 				
 				if(selectionNode){
 					try{
 						selectionNode.reload();
 					}catch(e){}
 				}else{
 					defTableTree.getRootNode().reload();
 				}
 			}
 		}],
 		bbar:[{
 			text:'确定'
 		},{text:'取消'}],
        collapseFirst:true
    });
    
    
    /**
     * 当选中节点的时候，触发事件
     */
    this.on("dblclick",function(node,e){
		var type=node.attributes.type;
		if(type=="schema"){
			return;
		}else if(type=="table"){
			var schema=node.parentNode.text;	//schema name
			var retV={};
			retV.caption=node.attributes.tableCode+"【"+node.text+"】";
			retV.tableCode=node.attributes.tableCode;
			retV.tableName=node.text;
			retV.id=node.id;
			window.returnValue=retV;
			window.close();
		}else{
			alert('您选择的不是具体表名，请选择具体的表');
		}
		
    });
    
}


Ext.extend(DefTableTree, Ext.tree.TreePanel, {

});




/**
 * 页面初始化
 */
function onload() {
}

var tableTree=new DefTableTree();

var lyCenter = {
	id : 'center-panel',
	region : 'center',
	layout:'border',
	border : true,
	//layout:"fit",
	items : [tableTree]
}
