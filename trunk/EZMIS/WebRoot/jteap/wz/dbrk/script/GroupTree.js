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
GroupTree = function() {
	
	var gTree=this;
	var rootLoader=new Ext.tree.AsyncTreeNode({
        	text:'所有用户',
        	loader:new Ext.tree.TreeLoader({
	            dataUrl:link2,
	            listeners:{
	            	beforeload:function(loader, node, callback ){
	            		this.baseParams.parentId=(node.isRoot?"":node.id);
	            	}
	            }
	        
	        }),
	        expanded :true,
	        allowDrag :true,
	        allowDrop :true
        });
        
        rootLoader.on("load",function(){
			
	        	//当所有组织添加完成之后,需要追加一个不属于任何组织的人员的节点
			gTree.createDissociationGroup();
		
			//还原选中状态
			var selNode;
			if(gTree.oldSelectedNodeId!=null){
				var oNode=gTree.findById(gTree.oldSelectedNodeId);
				if(oNode) selNode=oNode;
				gTree.oldSelectedNodeId=null;
			}
			if(!selNode) selNode=gTree.getRootNode();
			selNode.select();
        });
    GroupTree.superclass.constructor.call(this, {
        id:'func-tree',
        region:'west',
        title:'组织',
        ddGroup:'GridDD',
        dropAllowed: true, 
		dragAllowed: true,
        split:false,
        width: 220,
        minSize: 180,
        maxSize: 400,
        collapsible: true,
        margins:'2px 2px 2px 2px',
        cmargins:'0 0 0 0',
        enableDD:true,
        rootVisible:false,
        lines:false,
        autoScroll:true,
 		root:rootLoader,
 		bbar:['<font color="blue">*蓝色组织</font>表示为自己创建的组织'],
 		tbar:[{text:'刷新',handler:function(){gTree.getRootNode().reload();}}],
        collapseFirst:true        
    });
    
    /**
     * 当选中节点的时候，触发事件
     */
    this.getSelectionModel().on("selectionchange",function(oSM,oNode){
    	if(oNode){
    		
    		var btnModiGroup=mainToolbar.items.get("btnModiGroup");
    		var btnDelGroup=mainToolbar.items.get("btnDelGroup");
    		//新增加的数据权限
    		var btnDataperm=mainToolbar.items.get("btnDataperm");
	    	if(oNode.dissociation || oNode.isRootNode()){
	    		if(btnModiGroup) btnModiGroup.setDisabled(true);
		    	if(btnDelGroup) btnDelGroup.setDisabled(true);
		    	if(oNode.isRootNode() && !isRoot) return;	//非root用户不能显示所有用户
		    	var url=oNode.dissociation?link11:link12;
		    	gridPanel.changeToPersonDS(url);
		    	if(oNode.dissociation){
		    		if(btnDataperm)btnDataperm.setDisabled(false); 
		    	}
	    	}else{
		    	//改变操作按钮状态
	    		if(btnModiGroup) btnModiGroup.setDisabled(false);
		    	if(btnDelGroup) btnDelGroup.setDisabled(false); 
		    	//新增的数据权限
		    	if(btnDataperm)btnDataperm.setDisabled(false); 
		    	var url = link1+"?groupId="+oNode.id;
		    	gridPanel.changeToListDS(url);
	    	}
	    	
	    	
	    	
	    	gridPanel.getStore().reload();
	    	gTree.oldSelectedNodeId=oNode.id;
    	}
    });
    
    
    //初始化编辑器
    treeEditor=new Ext.app.ExTreeEditor(this,confirmField);
    
    treeEditor.on("beforecomplete",function(editor,value,oldValue){
    	
    	var preSortNo
    	
    	var previousSiblingNode = editor.editNode.previousSibling;
    	if(previousSiblingNode!=null && previousSiblingNode.attributes.sortNo){
    		preSortNo = previousSiblingNode.attributes.sortNo;
    	}else{
    		preSortNo = 0;
    	}
    	/*
    	if(editor.editNode.parentNode.childNodes.length < 2) {
    		preSortNo = 0 
    	} else {
    		preSortNo = editor.editNode.parentNode.childNodes[editor.editNode.parentNode.childNodes.length-2].attributes.sortNo
    	}
    	*/
    	
		 if(value.length<1){
		 	alert('名字不符合规则');
		 	return false;
		 }
		 
		 if(bFlag){
		 	//创建新的组织
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
		 			if(responseObject.success)
		 				alert("新建组织【"+value+"】成功");
		 			else
		 				alert("新建组织【"+value+"】失败");
		 		},
		 		failure:function(){
		 			alert("提交失败");
		 		},
		 		method:'POST',
		 		params:{groupName:value,preSortNo:preSortNo}
		 	})
		 	bFlag=false;
		 }
    });
    
    

    /**
     * 相应拖放事件，主要用于将grid中的数据拖放到Tree中
    */
    this.on("beforenodedrop",function(dropEvent){
    	
    	//判断数据来源是否来源于人员列表grid
    	if(!dropEvent.source.grid)	return;
    	
    	var oNode=dropEvent.target;
    	var persons=dropEvent.data.selections;
    	var personIds="";
    	var oCurrentNode=this.getSelectionModel().getSelectedNode();
    	var isCurrentRoot=(oCurrentNode.id==this.getRootNode().id);
    	if(oCurrentNode){
    		if(oCurrentNode.id==oNode.id) return;
    	}
    	for(var i=0;i<persons.length;i++){
    		personIds+=(isCurrentRoot || oCurrentNode.dissociation)?(persons[i].id):(persons[i].json.person.id);
    		personIds+=",";
    	}

    	Ext.MessageBox.show({
           title:'移动还是复制?',
           msg: '您是希望将选中的用户移动到当前组织，还是复制到当前组织？如果选择【是】，表示移动。如果选择【不】，表示复制，如果点击【取消】则不做任何操作',
           buttons: {yes:'移动',no:'复制',cancel:'取消'},
           width:400,
           fn: function(bt){
            	var currentGroupId=oCurrentNode?oCurrentNode.id:"";
           		if(bt=='yes'){
           			//移动
  					gTree.movePersonToTheGroup(personIds,currentGroupId,oNode.id);
           		}else if(bt=='no'){
           			//复制
           			gTree.copyPersonsToTheGroup(personIds,oNode.id);
           		}
           },
           icon: Ext.MessageBox.QUESTION
       });
    })
      
    //拖拽移动节点
    this.on("beforemovenode",function(tree, node, oldParent, newParent, localIndex ){
    	//不能移动非本人创建的组织
    	if(node.attributes.creator != curPersonLoginName){
    		alert("该组织不是您创建的，您没有权限移动");
    		return false;
    	}
    	//不能将本人创建的组织移动到本人的非管理组织
    	if(newParent.attributes.creator != curPersonLoginName && adminGroupIds.indexOf(newParent.id)<0){
    		alert("您不是[目的组织]的管理员，所以您无权将组织拖拽到该组织下");
    		return false;
    	}
    	if(!confirm("确定需要移动当前组织吗?")) return false;
    	if(node.dissociation){
    		alert("由于该组织为保留组织，不能移动");return false;
    	}
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
	 		url:link4,
	 		success:function(ajax){
	 			var responseText=ajax.responseText;	
	 			var responseObject=Ext.util.JSON.decode(responseText);
	 			node.select();
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

Ext.extend(GroupTree, Ext.tree.TreePanel, {
	oldSelectedNodeId:null,//最近一次被选中的节点id
	/**
	 * 创建游离组织
	 */
	createDissociationGroup:function(){
		var oNode=new Ext.tree.TreeNode({text:'游离用户',qtip:'不属于任何组织的用户',singleClickExpand :true,icon:imgPathPrefix+'resources/icon/icon_2.gif',allowDrag:false,allowDrop:false});
		oNode.dissociation=true;
		this.getRootNode().insertBefore(oNode,this.getRootNode().firstChild);
	},
	 
    /**
     * 创建组织
     * 取得选择的组织作为父组织，在旗下创建一个新的子组织，并显示编辑器
     * 如果当前未选择节点则取得根节点作为父节点
     * @bFirst	第一个吗
     */
    createGroup:function(bFirst){
    	var oNode=this.getSelectionModel().getSelectedNode();
    	
    	if(!oNode){
    		oNode=this.getRootNode();
    	} 
    	
    	//判断是否游离用户节点,不能在该节点下创建组织
    	if(oNode.dissociation){
    		alert("当前组织为系统保留组织，不能在该组织下创建子组织");
    		return false;
    	}
    	
    	//除了root用户之外，其他用户无法建立根组织
    	if(!isRoot && oNode.isRootNode()){
    	    //非Root用户不能添加根组织
    		if(!isRoot){
    			alert("您无权添加根组织，如有任何疑问请联系管理员");
    			return;
    		}
    	}
    	

    	//在不是自己创建的组织下创建子组织，当前用户必需是组织的管理员才有权限进行
    	if(oNode.attributes.creator!=curPersonLoginName && adminGroupIds.indexOf(oNode.id)<0 && !isRoot && !oNode.isRootNode()){
    		alert("您不是当前组织的管理员，所以无权在该组织下创建子组织");
    		return;
    	}
    	

		var oNewNode;
		var node=new Ext.tree.TreeNode({text:'新建组织',uiProvider:GroupNodeUI,creator:curPersonLoginName});
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
    
    },
    
    /**
     * 修改组织
     */
    modifyGroup:function(){
    	var oNode=this.getSelectionModel().getSelectedNode();
    	

    	if(!oNode){
    		Ext.Msg.alert('提示', "请选择一个节点");
    	}else{
    		if(!isRoot && oNode.attributes.creator!=curPersonLoginName){
	    		alert("您无权限修改或删除不是您创建的组织");
	    		return;
    		}
    		
    		if(!oNode.isRootNode() && !oNode.dissociation){	
				var groupEditForm=new GroupEditFormWindow(oNode.id);
				groupEditForm.show();
				groupEditForm.loadData();
			}else{
				alert("不能修改当前组织,如有疑问，请联系系统管理员");
			}
    	}
    },
    
    
    
     /**
     * 删除组织
     */
    delGroup:function(){
    	
    	var oNode=this.getSelectionModel().getSelectedNode();
    	if(!isRoot && oNode.attributes.creator!=curPersonLoginName){
    		alert("您无权限修改或删除不是您创建的组织");
    		return;
    	}
    	if(!oNode){
    		alert("请选择一个节点");
    	}else{
    		if(oNode==this.getRootNode()){
    			alert("不能删除根组织");
    			return ;	
    		}
    		if(window.confirm("确认删除当前组织吗")){
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
    },
    /**
     * 复制选中的人员到指定组织
     */
    copyPersonsToTheGroup:function(personIds,groupId){
    	var gTree=this;
    	//提交改变
	 	Ext.Ajax.request({
	 		url:link13+"?personIds="+personIds+"&groupId="+groupId,
	 		success:function(ajax){
	 			var responseText=ajax.responseText;	
	 			var responseObject=Ext.util.JSON.decode(responseText);
	 			if(responseObject.success){
	 				alert("成功复制"+responseObject.copy+"个用户\r\n"+responseObject.msg)
	 				gTree.oldSelectNodeId=gTree.getSelectionModel().getSelectedNode().id;
	 				gTree.getRootNode().reload(function(){});
	 			}
	 		},
	 		failure:function(){
	 			alert("服务器忙，请稍后操作");
	 		},
	 		method:'POST',
	 		params:{}
	 	})
    },
    movePersonToTheGroup:function(personIds,fromGroupId,toGroupId){
    	
    	var gTree=this;
    	//提交改变
	 	Ext.Ajax.request({
	 		url:link14+"?personIds="+personIds+"&toGroupId="+toGroupId+"&fromGroupId="+fromGroupId,
	 		success:function(ajax){
	 			var responseText=ajax.responseText;	
	 			var responseObject=Ext.util.JSON.decode(responseText);
	 			if(responseObject.success){
	 				alert("成功移动"+responseObject.copy+"个用户\r\n"+responseObject.msg)
	 				//重新加载
    				gTree.getRootNode().reload(function(){
    					
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