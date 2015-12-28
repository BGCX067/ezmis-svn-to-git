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
	node.remove();
});
/**
 * 功能树组件
 */
ModuleTree = function() {
	moduleTree=this;
    ModuleTree.superclass.constructor.call(this, {
        id:'func-tree',
        region:'west',
        title:'模块',
        split:true,
        width: 220,
        minSize: 180,
        maxSize: 400,
        collapsible: true,
        margins:'2px 2px 2px 2px',
        cmargins:'0 0 0 0',
        enableDD:true,
        rootVisible:true,
        lines:false,
        autoScroll:true,
//		tbar:[{text:'刷新',handler:function(){moduleTree.getRootNode().reload();}}],
        root:new Ext.tree.AsyncTreeNode({
        	text:'所有模块',
        	id:'rootNode',
        	loader:new Ext.tree.TreeLoader({
	            dataUrl:link1
	        }),
	        expanded :true
        }),
        collapseFirst:true       
    });
    
    //选择节点的时候触发事件
    this.getSelectionModel().on("selectionchange",function(oSM,oNode){
		var btnModiModule=mainToolbar.items.get("btnModiModule");
    	var btnDelModule=mainToolbar.items.get("btnDelModule");
    	if(btnModiModule) btnModiModule.setDisabled(false);
    	if(btnDelModule) btnDelModule.setDisabled(false);
		
		/*
		if(oNode && !oNode.isRootNode()){
	    	mainToolbar.items.get("btnModiModule").setDisabled(false);
	    	mainToolbar.items.get("btnDelModule").setDisabled(false);
		}
      	*/
	    if(oNode && !oNode.isRootNode()){
			moduleDetailForm.showDetailPanel(oNode);
    	}
    });
    
    
    //初始化编辑器
    treeEditor=new Ext.app.ExTreeEditor(this,confirmField);
    treeEditor.on("beforecomplete",function(editor,value,oldValue){
		/**==========================================
		 * 此处进行AJAX数据提交
		 **==========================================**/
		 if(value.length<1){
		 	alert('名字不符合规则');
		 	return false;
		 }
		 if(value!=oldValue)
			alert('操作成功'+value+":"+oldValue);
    });
    
    
    /**
     * 修改模块
     */
    this.modifyModule=function(){
    	var oNode=this.getSelectionModel().getSelectedNode();
    	if(!oNode){
    		Ext.Msg.alert('提示', "请选择一个节点");
    	}else{
    		treeEditor.editNode = oNode;
    		treeEditor.startEdit(oNode.ui.textNode);
    	}
    }
    /**
     * 删除模块
     */
    this.delModule=function(){
    	//debugger;
    	var oNode=this.getSelectionModel().getSelectedNode();
    	if(!oNode){
    		alert("请选择一个节点");
    	}else{
    		if(oNode.isRootNode()){
    			alert("不能删除根模块");
    			return ;	
    		}
    		if(window.confirm("确认删除当前模块吗")){
    			//提交数据
			 	Ext.Ajax.request({
			 		url:link3,
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
    //拖拽移动节点
    this.on("beforemovenode",function(tree, node, oldParent, newParent, localIndex ){
    	if(!confirm("确定需要移动当前模块吗?")) return false;
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
	 		url:link2,
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
};

Ext.extend(ModuleTree, Ext.tree.TreePanel, {

});