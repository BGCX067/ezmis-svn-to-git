//根节点加载器
var rootLoader=new Ext.tree.TreeLoader({
	dataUrl:link1+"?catalogCode="+catalogCode,
    listeners:{
    	beforeload:function(loader, node, callback ){
    		this.baseParams.parentId=(node.isRoot?"":node.id);
    	},
    	load:function(loader, node, callback ){
    		//初始化选中第一个节点
    		if(childid != "null"){
    			var items = leftTree.getRootNode().childNodes;
	    			for(var i = 0; i < items.length; i++){
	    				if(items[i].id == childid){
	    					items[i].select();
	    				} else{
	    					if(leftTree.getNodeById(childid)!=null){
	    						leftTree.getNodeById(childid).select();
	    					}else{
	    						leftTree.getRootNode().childNodes[0].select();
	    					}
	    					
	    				}
	    			}	
    		}else{
    			if(leftTree.getRootNode().childNodes[0].childNodes[0]!=null){
    				leftTree.getRootNode().childNodes[0].childNodes[0].select();
    			}
    		}
	    }

    }
});

//to do in the program
var rootNode=new Ext.tree.AsyncTreeNode({
	text:'所有文档',
	loader:rootLoader,
    expanded :true
});

rootNode.on("load",function(selNode){
	//alert(selNode);
	//var selNode=leftTree.getRootNode();
	//selNode.expand(true);
	
	//selNode.select();
	//curNode.select();
});




var treeEditor=null;
var confirmField=new Ext.app.ConfirmTextField({width:160});
var bFlag=false;
confirmField.on("confirmInput",function(){
	//此处进行AJAX操作，确定服务器后台操作成功
	bFlag=true;
	treeEditor.hide();
});

confirmField.on("blur",function(){
	bFlag=true;
}) 

confirmField.on("cancelInput",function(oField){
	bFlag=false;
	var node=treeEditor.editNode;
	
	treeEditor.cancelEdit(false);	
	if(node.id.indexOf('ynode')>=0){
		node.remove();
	}
});



/**
 * 左边的树
 */
LeftTree = function(){
	LeftTree.superclass.constructor.call(this, {
		id:'tableTree',
        region:'west',
        split:true,
        width: 230,
        minSize: 180,
        maxSize: 400,
        collapsible: true,
        margins:'0 0 1 1',
        cmargins:'0 0 0 0',
        rootVisible:true,
        lines:true,
        autoScroll:true,
 		root:rootNode,
 		tbar:[{
 			text:'刷新',
 			handler:function(){
 				rootNode.reload();
 			}
 		}],
        collapseFirst:true
    });
    //初始化编辑器
    treeEditor=new Ext.app.ExTreeEditor(this,confirmField);
	treeEditor.on("beforecomplete",function(editor,value,oldValue){
		//如果不是新创建的节点,新创建的节点的id是以ynode打头的，保存成功后会更新其id属性
		if(editor.editNode.id.indexOf('ynode')<0 && value==oldValue)
			return;
		if(bFlag){
		 	//创建新的组织
		 	var parentId="";
		 	var parentNode=editor.editNode.parentNode;
		 	if(!parentNode.isRoot){
		 		parentId=parentNode.id;
		 	}
		 	
		 	//提交数据
		 	Ext.Ajax.request({
		 		url:link3+"?parentId="+parentId,
		 		success:function(ajax){
		 			var responseText=ajax.responseText;	
		 			var responseObject=Ext.util.JSON.decode(responseText);
		 			editor.editNode.id=responseObject.id;
		 			if(responseObject.success)
		 				alert("新建分类【"+value+"】成功!");
		 			else
		 				alert("新建分类【"+value+"】失败!");
		 		},
		 		failure:function(){
		 			alert("提交失败!");
		 		},
		 		method:'POST',
		 		params:{nodeName:value,id:editor.editNode.id.indexOf("ynode")>=0?"":editor.editNode.id}
		 	})
		 	bFlag=false;
		 }
    });    
    
    /**
     * 当选中节点的时候，触发事件
     */
    this.getSelectionModel().on("selectionchange",function(oSM,oNode){
    	//修改菜单栏按钮状态
    	var btnAddCatalog = mainToolbar.items.get('btnAddCatalog');
    	var btnModiCatalog = mainToolbar.items.get('btnModiCatalog');
    	var btnDelCatalog = mainToolbar.items.get('btnDelCatalog');
    	var btnAddDocument = mainToolbar.items.get('btnAddDocument');
    	//var btnModiDocument = mainToolbar.items.get('btnModiDocument');
    	//var btnDelDocument = mainToolbar.items.get('btnDelDocument');
    	
    	if(oNode == null){
    		if(btnAddCatalog) btnAddCatalog.setDisabled(true);
    		if(btnModiCatalog) btnModiCatalog.setDisabled(true);
    		if(btnDelCatalog) btnDelCatalog.setDisabled(true);
    		if(btnAddDocument) btnAddDocument.setDisabled(true);
    		//if(btnModiDocument) btnModiDocument.setDisabled(true);
    		//if(btnDelDocument) btnDelDocument.setDisabled(true);
    		this.tree.root.select();
    	}else{
    		if(btnAddCatalog) btnAddCatalog.setDisabled(false);
    		if(oNode.isRootNode()){
    			if(btnModiCatalog) btnModiCatalog.setDisabled(true);
	    		if(btnDelCatalog) btnDelCatalog.setDisabled(true);
	    		if(btnAddDocument) btnAddDocument.setDisabled(true);
	    		if(flagRoots!='null'){
	    			if(btnAddCatalog) btnAddCatalog.setDisabled(true);
	    		}
	    		//
	    		//if(btnModiDocument) btnModiDocument.setDisabled(true);
	    		//if(btnDelDocument) btnDelDocument.setDisabled(true);
    		}else{
    			if(btnModiCatalog) btnModiCatalog.setDisabled(false);
	    		if(btnDelCatalog) btnDelCatalog.setDisabled(false);
	    		if(btnAddDocument) btnAddDocument.setDisabled(false);
	    		//if(btnModiDocument) btnModiDocument.setDisabled(false);
	    		//if(btnDelDocument) btnDelDocument.setDisabled(false);
    		}
    		//var url=link4+(oNode.isRootNode()?"":"?catalogId="+oNode.id);
	    	//rightGrid.changeToListDS(url);	 
	    	var catalogId = oNode.isRootNode()?"":oNode.id ;
	    	changeToListDS(catalogId,"","","");
	    	//rightGrid.changeToListDS(link4+"?catalogId="+catalogId);
		    //rightGrid.getStore().reload();
	    	leftTree.oldSelectedNodeId=oNode.id;
    	}
    });
    
    this.on("load",function(node){
    	//this.findChilds(node,childid);
    });
    
    this.loadData=function(){
	    //var selNode=leftTree.getRootNode();
    }
    
    this.findChilds=function(curNode,childid){
		curNode.eachChild(function(node){
			if(node.id==childid){
				this.node.select();
			}
		});
	}
	
	
	//拖拽移动节点
    this.on("beforemovenode",function(tree, node, oldParent, newParent, localIndex ){
    	if(!confirm("确定需要移动当前分类吗?")) return false;
    	var bParentChanged=false;//是否换父亲节点了
    	var bChanged=false;		//是否换位置了
    	var newParentId="";		//新父亲节点的编号,用于判断是否是根节点，如果为根节点，则保持为空
    	//如果移动到根节点，则指定其id为空
    	if(newParent==tree.getRootNode()){
    		newParent.id='';
    	}
    	if(oldParent == tree.getRootNode()){
    		oldParent.id = '';
    	}
    	
    	
    	if(oldParent.id!=newParent.id){
    		bParentChanged=true;
    		bChanged=true;
    		if(!newParent.isRootNode()){
    			newParentId=newParent.id;
    		}
    	}else if((localIndex-1)!=node.getIndex()){
    		bChanged=true;
    	}
    	
    	/*
    	if(oldParent.id!=newParent.id){
    		bParentChanged=true;
    		bChanged=true;
    	}
    	*/
    	 
		var sortNo=parseInt(node.attributes.sortNo);
    	var nextSortNo,sortNo;
    	var updateStart=0;//需要排序要批量更新的判断依据,如果这个值大于0，则需要进行批量更新
    	var beginSortNo=1;//默认插入的是第一个节点    	
    	if(newParent.childNodes[localIndex-1]){
    		var tmpSortNo=newParent.childNodes[localIndex-1].attributes.sortNo;
    		if(tmpSortNo!=null){
    			beginSortNo=parseInt(tmpSortNo);
    		}
    	}
    	
    	/*
    	if(newParent.childNodes[localIndex-1]){
    		beginSortNo=newParent.childNodes[localIndex-1].attributes.sortNo;
    	}
    	*/
    	
    	//判断排序号来决定是否换位置了
    	/*
    	if(sortNo<beginSortNo)
    		bChanged=true;
    	*/
    	if(newParent.childNodes[localIndex]){
    		/*
    		nextSortNo=newParent.childNodes[localIndex].attributes.sortNo;
    		if(sortNo>nextSortNo)
    			bChanged=true;
    		*/
    		var tmpSortNo=newParent.childNodes[localIndex].attributes.sortNo;
    		if(tmpSortNo!=null)
    			nextSortNo=parseInt(tmpSortNo);
    	}
    	//如果是nextSortNo为空，表明是追加到队列最后，及beginSortNo直接加上每次的增量1000
    	//beginSortNo+(nextSortNo-beginSortNo)/2=beginSortNo+1000
    	if(!nextSortNo){
    		nextSortNo=2*1000+beginSortNo;
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
			//sortNo=beginSortNo+1000;
			sortNo=beginSortNo+10;
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
	 		url:link16,
	 		success:function(ajax){
	 			var responseText=ajax.responseText;	
	 			var responseObject=Ext.util.JSON.decode(responseText);
	 			alert("移动成功");
	 		},
	 		failure:function(){
	 			alert("服务器忙，请稍后操作");
	 		},
	 		method:'POST',
	 		params:{nodeId:node.id,bParentChanged:bParentChanged,oldParentId:oldParent.id,newParentId:newParent.id,sortNo:sortNo,updateStart:updateStart}
	 	})
    	
    })
}

Ext.extend(LeftTree, Ext.tree.TreePanel, {
	oldSelectedNodeId:null,//最近一次被选中的节点id
	deleteSelectedNode : function(){
		var oNode=this.getSelectionModel().getSelectedNode();
		
		if(window.confirm("确认删除当前分类吗?删除当前分类将会删除分类下所有文档！请谨慎操作！")){
			//提交数据
		 	Ext.Ajax.request({
		 		url:link2,
		 		success:function(ajax){
		 			var responseText=ajax.responseText;	
		 			var responseObject=Ext.util.JSON.decode(responseText);
		 			if(responseObject.success){
		 				alert("删除成功!");
		 				oNode.remove();
		 			}else{
		 				alert(responseObject.msg);
		 			}
		 		},
		 		failure:function(){
		 			alert("提交失败!");
		 		},
		 		method:'POST',
		 		params:{nodeId:oNode.id}
		 	})	
		}
	},
	/**
	 * 修改节点
	 */
	modifyNode:function(){
		var oNode=this.getSelectionModel().getSelectedNode();
		treeEditor.editNode = oNode;
	   	treeEditor.startEdit(oNode.ui.textNode);        
	},
	/**
	 * 创建新的节点
	 */
	createNode:function(bFirst){
    	var oNode=this.getSelectionModel().getSelectedNode();
    	
    	if(!oNode){
    		oNode=this.getRootNode();
    	} 
    	
    	

		var oNewNode;
		var node=new Ext.tree.TreeNode({text:'新建分类'});
		if(bFirst)
			oNewNode=oNode.insertBefore(node,oNode.childNodes[0]);
		else
        	oNewNode=oNode.appendChild(node);
        	//如果父节点未展开，则需要留出一定的时间给节点展开，否则编辑器定位有问题
        if(!oNode.expanded){
        	oNode.expand();
			oNewNode.select();
			//给展开时间
        	setTimeout(function(){
	            treeEditor.editNode = oNewNode;
	    		treeEditor.startEdit(oNewNode.ui.textNode);            	
        	},300);
        }else{
        	treeEditor.editNode = oNewNode;
			treeEditor.startEdit(oNewNode.ui.textNode);    	
        }
    
    }
	
});

