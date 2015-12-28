
/**
 * 功能树组件
 */
DictCatalogTree = function() {
	var dictCatalogTree=this;
	
    DictCatalogTree.superclass.constructor.call(this, {
        id:'func-tree',
        region:'west',
        title:'字典类型列表:',
        split:true,
        width: 220,
        minSize: 180,
        maxSize: 400,
        collapsible: true,
        margins:'2px 2px 2px 2px',
        cmargins:'0 0 0 0',
        enableDD:true,
        ddGroup:'GridDD',
        dropAllowed: true, 
		dragAllowed: true,
        rootVisible:true,
        lines:false,
        autoScroll:true,
 		root:new Ext.tree.AsyncTreeNode({
        	text:'所有类型',
        	allowDrag : false, 
        	id:'rootNode',
		    allowDrop : true, 
        	loader:new Ext.tree.TreeLoader({
	            dataUrl:link2,
	            listeners:{load:function(){
		        	if(dictCatalogTree.getRootNode().childNodes.length>0){
		        		var firstNode=dictCatalogTree.getRootNode().childNodes[0];
		        		firstNode.select();
		        	}	            	
	            }}
	        }),
	        expanded :true
        }),
        collapseFirst:true     
    });
    

    /**
     * 当选中节点的时候，触发事件
     */
    this.getSelectionModel().on("selectionchange",function(oSM,oNode){
    	if(oNode){
    		if(oNode==dictCatalogTree.getRootNode()){
    			if(mainToolbar.items.get("btnModiDictCatalog")) mainToolbar.items.get("btnModiDictCatalog").setDisabled(true);
	    		if(mainToolbar.items.get("btnDelDictCatalog")) mainToolbar.items.get("btnDelDictCatalog").setDisabled(true);
    		}else{
		    	if(mainToolbar.items.get("btnModiDictCatalog")) mainToolbar.items.get("btnModiDictCatalog").setDisabled(false);
	    		if(mainToolbar.items.get("btnDelDictCatalog")) mainToolbar.items.get("btnDelDictCatalog").setDisabled(false);
    		}
    		if(oNode == this.tree.root) {
    			return ;
    		}
    		var	url=link1+"?catalogId="+oNode.id;
		    gridPanel.changeToListDS(url);
		    gridPanel.getStore().reload();
    	}
    });
    
    
      
    
    /**
     * 删除字典类型
     */
    this.delDictCatalog=function(){
    	var oNode=this.getSelectionModel().getSelectedNode();
    	if(!oNode){
	   	  	alert("请选择一个节点");
    	}else{
    		if(oNode==this.getRootNode()){
				alert("无法删除根节点");
    			return ;	
    		}
    		if(window.confirm("确认删除当前类型吗?")){
    			
    			//提交数据
			 	Ext.Ajax.request({
			 		url:link5,
			 		success:function(ajax){
			 			var responseText=ajax.responseText;	
			 			var responseObject=Ext.util.JSON.decode(responseText);
			 			if(responseObject.success){
			 				alert("删除成功");
			 				oNode.remove();
			 			}else{
			 				alert(responseObject.msg);
			 			}
			 		},
			 		failure:function(){
			 			alert("提交失败");
			 		},
			 		method:'POST',
			 		params:{nodeId:oNode.id}
			 	})	
    		}
    	}
    }
    
    /**
     * 相应拖放事件，主要用于将grid中的数据拖放到Tree中
    */
    this.on("beforenodedrop",function(dropEvent){  	
    	//判断数据来源是否来源于人员列表grid
    	if(!dropEvent.source.grid)	return;
    	var oNode=dropEvent.target;
    	//不允许将字典移动到根节点上
    	if(oNode==dictCatalogTree.getRootNode()){
    		alert('无法将字典移动到根类型');
    		return;
		}
    	var dicts=dropEvent.data.selections;
    	var dictIds="";
    	var oCurrentNode=this.getSelectionModel().getSelectedNode();
    	var isCurrentRoot=(oCurrentNode.id==this.getRootNode().id);
    	if(oCurrentNode){
    		if(oCurrentNode.id==oNode.id) return;
    	}
    	for(var i=0;i<dicts.length;i++){
    		dictIds+=(isCurrentRoot || oCurrentNode.dissociation)?(dicts[i].id):(dicts[i].json.id);
    		dictIds+=",";
    	}

    	Ext.MessageBox.show({
           title:'移动还是复制?',
           msg: '您是希望将选中的字典移动到当前类型，还是复制到当前类型？如果选择【是】，表示移动。如果选择【不】，表示复制，如果点击【取消】则不做任何操作',
           buttons: {yes:'移动',no:'复制',cancel:'取消'},
           width:400,
           fn: function(bt){
           		if(bt=='yes'){
           			//移动
  					dictCatalogTree.moveDictsToTheCatalog(dictIds,oNode.id);
           		}else if(bt=='no'){
           			//复制
           			dictCatalogTree.copyDictsToTheCatalog(dictIds,oNode.id);
           		}
           },
           icon: Ext.MessageBox.QUESTION
       });
    })

    //拖拽移动节点
    this.on("beforemovenode",function(tree, node, oldParent, newParent, localIndex ){
    	if(!window.confirm("确定需要移动当前类型吗?")) return false;
    	var bParentChanged=false;//是否换父亲节点了
    	var bChanged=false;		//是否换位置了
    	
    	//如果移动到根节点，则指定其id为空
    	if(newParent==tree.getRootNode()){
    		newParent.id='';
    	}
    	
    	if(oldParent.id!=newParent.id){dictCatalogTree
    		bParentChanged=true;
    		bChanged=true;
    	}
		var sortNo=node.attributes.sortNo;
    	var nextSortNo,sortNo;
    	var updateStart=0;//需要排序要批量更新的判断依据,如果这个值大于0，则需要进行批量更新
    	var beginSortNo=1;//默认插入的是第一个节点    	
    	
    	if(newParent.childNodes[localIndex-1]){
    		beginSortNo=newParent.childNodes[localIndex-1].attributes.sortNo;
    	}
    	
    	//判断排序号来决定是否换位置了
    	if(sortNo<beginSortNo)
    		bChanged=true;
    	
    	if(newParent.childNodes[localIndex]){
    		nextSortNo=newParent.childNodes[localIndex].attributes.sortNo;
    		if(sortNo>nextSortNo)
    			bChanged=true;
    	}
    	//如果是nextSortNo为空，表明是追加到队列最后，及beginSortNo直接加上每次的增量1000
    	//beginSortNo+(nextSortNo-beginSortNo)/2=beginSortNo+1000
    	if(!nextSortNo){
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
			//sortNo=beginSortNo + 1000;
			sortNo=beginSortNo + 1000;
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
	 		url:link4,
	 		success:function(ajax){
	 			var responseText=ajax.responseText;	
	 			var responseObject=Ext.util.JSON.decode(responseText);
	 			alert("移动成功");
	 		},
	 		failure:function(){
	 			alert("服务器繁忙，请稍后操作");
	 		},
	 		method:'POST',
	 		params:{nodeId:node.id,bParentChanged:bParentChanged,oldParentId:oldParent.id,newParentId:newParent.id,sortNo:sortNo,updateStart:updateStart}
	 	})	
    })   
};

Ext.extend(DictCatalogTree, Ext.tree.TreePanel, {
	oldSelectNodeId:null,//最近一次被选中的节点id
	/**
     * 复制选中的字典到指定类型
     */
    copyDictsToTheCatalog:function(dictIds,catalogId){
    	var catalogTree=this;
    	//提交改变
	 	Ext.Ajax.request({
	 		url:link11+"?dictIds="+dictIds+"&tocatalogId="+catalogId,
	 		success:function(ajax){
	 			var responseText=ajax.responseText;	
	 			var responseObject=Ext.util.JSON.decode(responseText);
	 			if(responseObject.success){
	 				alert("成功复制"+responseObject.copy+"个字典\r\n"+responseObject.msg)
	 				catalogTree.oldSelectNodeId=catalogTree.getSelectionModel().getSelectedNode().id;
	 				catalogTree.getRootNode().reload(function(){});
	 			}
	 		},
	 		failure:function(){
	 			alert("服务器忙，请稍后操作");
	 		},
	 		method:'POST',
	 		params:{}
	 	})
    },
    /**
     * 拷贝选中的字典到指定类型
     */
    moveDictsToTheCatalog:function(dictIds,tocatalogId){
    	var catalogTree=this;
    	//提交改变
	 	Ext.Ajax.request({
	 		url:link12+"?dictIds="+dictIds+"&tocatalogId="+tocatalogId,
	 		success:function(ajax){
	 			var responseText=ajax.responseText;	
	 			var responseObject=Ext.util.JSON.decode(responseText);
	 			if(responseObject.success){
	 				alert("成功移动"+responseObject.copy+"个字典\r\n"+responseObject.msg);
	 				gridPanel.getStore().reload();
	 				//重新加载
    				catalogTree.getRootNode().reload(function(){				
    				});
	 			}
	 		},
	 		failure:function(){
	 			alert("服务器忙，请稍后操作");
	 		},
	 		method:'POST',
	 		params:{}
	 	})
    }
});