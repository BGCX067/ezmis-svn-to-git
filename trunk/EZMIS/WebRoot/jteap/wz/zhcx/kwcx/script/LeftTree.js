
//加载table节点的loader
var tableLoader=new Ext.tree.TreeLoader({
	dataUrl:link1
});

//添加数据源对象参数
tableLoader.on("beforeload", function(treeLoader, node) {
	//alert(node.id);
	 tableLoader.baseParams.parentId=(node.isRoot?"":node.id);
//	  store.proxy.conn.url = link19+"?parentId="+node.id;
//        treeLoader.baseParams.datasource = node.attributes.datasource;
    }, this);

//to do in the program
var rootLoader=new Ext.tree.AsyncTreeNode({
	id:'root',
	text:'库位',
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
		//window.open(link2+"?wzlbbm="+oNode.id);
			rightGrid.changeToListDS(link2+"?queryParamsSql=obj.kw.id='"+oNode.id+"'");
			rightGrid.getStore().reload();
//    		rightGrid.store.proxy.conn.url = link2+"?ckid="+oNode.id;
  //  		rightGrid.store.reload();
    	}
    });
 //拖拽移动节点
    this.on("beforemovenode",function(tree, node, oldParent, newParent, localIndex ){
    	var bParentChanged=false;//是否换父亲节点了
    	var bChanged=false;		//是否换位置了
    	var newParentId="";		//新父亲节点的编号,用于判断是否是根节点，如果为根节点，则保持为空
    	if(oldParent.id!=newParent.id){
    		bParentChanged=true;
    		bChanged=true;
    		if(!newParent.isRootNode()){
    			newParentId=newParent.id;
    		}
    	}else if((localIndex-1)!=node.getIndex()){
    		bChanged=true;
    	}
		var sortNo=parseInt(node.attributes.sortNo);
    	var nextSortNo,sortNo;
    	var updateStart=0;//需要排序要批量更新的判断依据,如果这个值大于0，则需要进行批量更新
    	var beginSortNo=1;//默认插入的是第一个节点    	
    	if(newParent.childNodes[localIndex-1]){
    		beginSortNo = newParent.childNodes[localIndex-1].attributes.sortNo;
    	}
    	
    	//判断排序号来决定是否换位置了
    	
    	 if(sortNo<beginSortNo)
    		bChanged=true;
    		
    	if(newParent.childNodes[localIndex]){
    		nextSortNo=newParent.childNodes[localIndex].attributes.sortNo;
    		if(sortNo>nextSortNo)
    			bChanged=true;
/*
    		if(sortNo>nextSortNo)
    			bChanged=true;
  */
    	}
    	//如果是nextSortNo为空，表明是追加到队列最后，及beginSortNo直接加上每次的增量1000
    	//beginSortNo+(nextSortNo-beginSortNo)/2=beginSortNo+1000
    	if(!nextSortNo){
    		//nextSortNo=2*1000+beginSortNo;
    		nextSortNo=10+beginSortNo;
    	}
    	
    	//如果未有改变,则直接返回
    	if(!bChanged){
    		//alert("未有改变");
    		return false;
    	}
    	//以下是如果节点改变位置（无论是换了父亲还是换了位置)
		//if(node.isLeaf()){
		if(localIndex == newParent.childNodes.length){
			//如果是最后一个节点，排序号直接加上增量
			sortNo=beginSortNo+1000;
		}else{
			//否则需要插入到两个节点之间，然后记录下是否需要批量修改同级以下的节点的排序号
			sortNo=Math.floor(beginSortNo+(nextSortNo-beginSortNo)/2);
    		if(sortNo==beginSortNo){
    			updateStart=nextSortNo;//此处将updateStart记录下来，提交到服务器，只要是同级别，小于等于updateStart的节点，排序要都需要乘以10
    			sortNo=Math.floor(beginSortNo+(nextSortNo*10-beginSortNo)/2);
    		}
		}
	 	//提交改变
	 	Ext.Ajax.request({
	 		url:link6,
	 		success:function(ajax){
	 			var responseText=ajax.responseText;	
	 			var responseObject=Ext.util.JSON.decode(responseText);
	 			alert("移动成功");
	 		},
	 		failure:function(){
	 			alert("服务器忙，请稍后操作");
	 		},
	 		method:'POST',
	 		params:{nodeId:node.id,bParentChanged:bParentChanged,oldParentId:oldParent.id,newParentId:newParent.id=='root'?null:newParent.id,sortNo:sortNo,updateStart:updateStart}
	 	})
    	
    })
    
}


Ext.extend(LeftTree, Ext.tree.TreePanel, {
});

