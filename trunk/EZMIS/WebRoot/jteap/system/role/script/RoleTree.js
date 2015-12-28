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

RoleTree = function() {
	roleTree=this;
    RoleTree.superclass.constructor.call(this, {
		id:'role-tree',
        region:'west',
        title:'角色',
        ddGroup:'ddGrid',
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
 		root:new Ext.tree.AsyncTreeNode({
        	text:'我的角色',
        	loader:new Ext.tree.TreeLoader({
	            dataUrl:link2,
	            listeners:{load:function(){
	            	if(roleTree.getRootNode().childNodes.length>0){
	            		var firstNode=	roleTree.getRootNode().childNodes[0];
	            		firstNode.select();
	            	}
	            	
	            }}
	        }),
	        expanded :true
        }),
        collapseFirst:true,
        bbar:['<font color="blue">*蓝色角色</font>表示为自己创建的角色'],
        tbar:[new Ext.Button({text:"刷新",handler:function(){roleTree.refresh();}})]
    });
    
 
    /**
     * 选择角色事件
     */
    this.getSelectionModel().on("selectionchange",function(oSM,oNode){
    	var btnModiRole=mainToolbar.items.get("btnModiRole");
    	var btnDelRole=mainToolbar.items.get("btnDelRole");
    	if(btnModiRole) btnModiRole.setDisabled(false);
    	if(btnDelRole) btnDelRole.setDisabled(false);
    	
		var btnAddUser=mainToolbar.items.get('btnAddUser');
    	//新增加的数据权限
		if(oNode && !oNode.isRootNode()){
			if (btnAddUser) 
				btnAddUser.setDisabled(false);
			var url = link18 + "?roleId=" + oNode.id;
			rightGrid.changeToListDS(url);
			rightGrid.getStore().reload();
		} else {
			if (btnAddUser) 
				btnAddUser.setDisabled(true);
		}
    	
    });
    
    
    //初始化编辑器
    treeEditor=new Ext.app.ExTreeEditor(this,confirmField);
    treeEditor.on("beforecomplete",function(editor,value,startValue ){
     	var preSortNo
    	if(editor.editNode.parentNode.childNodes.length < 2) {
    		preSortNo = 0 
    	} else {
    		preSortNo = editor.editNode.parentNode.childNodes[editor.editNode.parentNode.childNodes.length-2].attributes.sortNo
    	}
		 if(value.length<1){
		 	alert('名字不符合规则');
		 	return false;
		 }
		 
		 if(bFlag){
		 	//创建新的角色
		 	var parentId="";
		 	var parentNode=editor.editNode.parentNode;
		 	if(!parentNode.isRoot){
		 		parentId=parentNode.id;
		 	}
		 	editor.editNode.attributes.sortNo = parseInt(preSortNo) + 1000
		 	
		 	//提交数据
		 	Ext.Ajax.request({
		 		url:link3+"?parentId="+parentId,
		 		success:function(ajax){
		 			var responseText=ajax.responseText;	
		 			var responseObject=Ext.util.JSON.decode(responseText);
		 			editor.editNode.id=responseObject.id;
		 			if(responseObject.success){
		 				alert("新建角色【"+value+"】成功");
		 				editor.editNode.select();
		 			}
		 			else
		 				alert("新建角色【"+value+"】失败");
		 		},
		 		failure:function(){
		 			alert("提交失败");
		 		},
		 		method:'POST',
		 		params:{roleName:value,preSortNo:preSortNo}
		 	})
		 	bFlag=false;
		 }
    })
    
    
    
    
    
    /**
     * 创建组织
     * 取得选择的组织作为父组织，在旗下创建一个新的子组织，并显示编辑器
     * 如果当前未选择节点则取得根节点作为父节点
     * @bFirst	第一个吗
     */
    this.createRole=function(bFirst){
    	var oNode=this.getSelectionModel().getSelectedNode();
    	if(!oNode){
    		oNode=this.getRootNode();
    	}
 
		var oNewNode;
		var oNewNode=new Ext.tree.TreeNode({text:'新建角色',uiProvider:RoleNodeUI});
		if(bFirst)
			oNewNode=oNode.insertBefore(oNewNode , oNode.childNodes[0]);
		else
        	oNewNode=oNode.appendChild(oNewNode);
        //如果父节点未展开，则需要留出一定的时间给节点展开，否则编辑器定位有问题
        if(!oNode.expanded){
        	oNode.expand();
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
    
    /**
     * 修改角色
     */
    this.modifyRole=function(){
    	var oNode=this.getSelectionModel().getSelectedNode();
    	if(!oNode){
    		Ext.Msg.alert('提示', "请选择一个节点");
    	}else{
    		treeEditor.editNode = oNode;
    		treeEditor.startEdit(oNode.ui.textNode);
    	}
    }
    /**
     * 删除角色
     */
    this.delRole=function(){
    	var oNode=this.getSelectionModel().getSelectedNode();
    	if(!oNode){
    		Ext.Msg.alert('提示', "请选择一个节点");
    	}else{
    		if(oNode==this.getRootNode()){
    			Ext.Msg.alert('提示', "不能删除根角色");
    			return ;	
    		}
    	 if(window.confirm("确认删除当前角色吗")){
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
        //拖拽移动节点
    this.on("beforemovenode",function(tree, node, oldParent, newParent, localIndex ){
    	if(!confirm("确定需要移动当前角色吗?")) return false;
    	
    	var bParentChanged=false;//是否换父亲节点了
    	var bChanged=false;		//是否换位置了
    	
    	if(oldParent.id!=newParent.id){
    		bParentChanged=true;
    		bChanged=true;
    	}
		var sortNo=parseInt(node.attributes.sortNo);
    	var nextSortNo,sortNo;
    	var updateStart=0;//需要排序要批量更新的判断依据,如果这个值大于0，则需要进行批量更新
    	var beginSortNo=1;//默认插入的是第一个节点    	
    	if(newParent.childNodes[localIndex-1]){
    		beginSortNo=parseInt(newParent.childNodes[localIndex-1].attributes.sortNo);
    	}
    	
    	//判断排序号来决定是否换位置了
    	if(sortNo<=beginSortNo)
    		bChanged=true;
    	
    	if(newParent.childNodes[localIndex]){
    		nextSortNo=parseInt(newParent.childNodes[localIndex].attributes.sortNo);
    		if(sortNo>=nextSortNo)
    			bChanged=true;
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
			sortNo=beginSortNo+1000;
		}else{
			//否则需要插入到两个节点之间，然后记录下是否需要批量修改同级以下的节点的排序号
			sortNo=Math.floor(beginSortNo+(nextSortNo-beginSortNo)/2);
    		if(sortNo==beginSortNo){
    			updateStart=nextSortNo;//此处将updateStart记录下来，提交到服务器，只要是同级别，小于等于updateStart的节点，排序要都需要乘以10
    			sortNo=Math.floor(beginSortNo+((nextSortNo+1)*10-beginSortNo)/2);
    		}
		}
		var newParentId=(newParent.id==tree.getRootNode().id)?"":newParent.id;
		
	 	//提交改变
	 	Ext.Ajax.request({
	 		url:link1,
	 		success:function(ajax){
	 			var responseText=ajax.responseText;	
	 			var responseObject=Ext.util.JSON.decode(responseText);
	 			alert("移动成功");
	 		},
	 		failure:function(){
	 			alert("服务器忙，请稍后操作");
	 		},
	 		method:'POST',
	 		params:{nodeId:node.id,bParentChanged:bParentChanged,oldParentId:oldParent.id,newParentId:newParentId,sortNo:sortNo,updateStart:updateStart}
	 	})
    	
    })
};



Ext.extend(RoleTree, Ext.tree.TreePanel, {
	refresh:function(){
		this.getRootNode().reload();
	}
});