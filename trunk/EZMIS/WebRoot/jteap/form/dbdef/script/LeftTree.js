

//加载schema节点的loader
var schemaLoader=new Ext.tree.TreeLoader({
	dataUrl:link1,
   	applyLoader:false			//表示下级节点采用动态产生loader的方式，以返回的数据决定下一个loader是哪个loader
});

//加载table节点的loader
var tableLoader=new Ext.tree.TreeLoader({
	dataUrl:link2,
	beforeload:function(loader, node, callback ){
		this.baseParams.parentId=(node.isRoot?"":node.id);
	}
});

//添加数据源对象参数
tableLoader.on("beforeload", function(treeLoader, node) {
        treeLoader.baseParams.datasource = node.attributes.datasource;
    }, this);

//to do in the program
var rootLoader=new Ext.tree.AsyncTreeNode({
	text:'所有表',
	id:'rootNode',
	loader:tableLoader,
    expanded :true
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
LeftTree= function(){
    
	var tree = this;    
	LeftTree.superclass.constructor.call(this, {
        id:'tableTree',
        region:'west',
        split:true,
        width: 300,
        minSize: 180,
        maxSize: 400,
        collapsible: true,
        margins:'0 0 1 1',
        cmargins:'0 5 5 5',
        enableDD:true,
        rootVisible:true,
        lines:true,
        autoScroll:true,
        //启用body滚动
//        ddScroll:true,
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
		 	}else if(parentNode.id != tree.getRootNode().id){
			 	if(parentNode.attributes.type != 'catalog'){
			 		alert("不能在【" + parentNode.text + "】表定义节点上新建分类");
			 		return;
			 	}
		 	}	
		 		
		 	
		 	//提交数据
		 	Ext.Ajax.request({
		 		url:link21+"?parentId="+parentId,
		 		success:function(ajax){
		 			var responseText=ajax.responseText;	
		 			var responseObject=Ext.util.JSON.decode(responseText);
		 			editor.editNode.id=responseObject.id;
		 			if(responseObject.success){
		 				alert("新建分类【"+value+"】成功");
		 				tree.getRootNode().reload();
		 			}else{
			 			alert("新建分类【"+value+"】失败");
		 			}
		 		},
		 		failure:function(){
		 			alert("提交失败");
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
    	if(oNode){
    		var btnModiTable=mainToolbar.items.get("btnModiTable");
    		var btnDelTable=mainToolbar.items.get("btnDelTable");
    		var btnAddColumn=mainToolbar.items.get("btnAddColumn");
    		var btnRebuildTable=mainToolbar.items.get("btnRebuildTable");
    		var btnQueryTable=mainToolbar.items.get("btnQueryTable");
    		var btnModiCatalog=mainToolbar.items.get("btnModiCatalog");
    		var btnDelCatalog=mainToolbar.items.get("btnDelCatalog");
    		var btnAddCatalog = mainToolbar.items.get("btnAddCatalog");
    		
    		//选中的是表节点,即可修改、删除对应的表
    		if(oNode.attributes.type=="table"){
    			var url=link3+"?tableid="+oNode.id;
    			columnGrid.table_id = oNode.id;
    			columnGrid.changeToListDS(url);
    			columnGrid.store.reload();
    			if(btnModiTable) btnModiTable.setDisabled(false);
    			if(btnDelTable) btnDelTable.setDisabled(false);
    			if(btnAddColumn) btnAddColumn.setDisabled(false);
    			if(btnRebuildTable) btnRebuildTable.setDisabled(false);
    			if(btnQueryTable) btnQueryTable.setDisabled(false);
    		}else{
    			var url=link3+"?tableid=null";
    			columnGrid.changeToListDS(url);
				columnGrid.store.reload();
				
    			if(btnModiTable) btnModiTable.setDisabled(true);
    			if(btnDelTable) btnDelTable.setDisabled(true);
    			if(btnAddColumn) btnAddColumn.setDisabled(true);
    			if(btnRebuildTable) btnRebuildTable.setDisabled(true);
    			if(btnQueryTable) btnQueryTable.setDisabled(true);
    		}
    		
    		//选中的是分类节点,即可修改、删除对应的分类
    		if(oNode.attributes.type=="catalog" || oNode.id=="rootNode"){
    			if(btnAddCatalog) btnAddCatalog.setDisabled(false);
    			if(btnModiCatalog) btnModiCatalog.setDisabled(false);
    			if(btnDelCatalog) btnDelCatalog.setDisabled(false);
    		}else{
    			if(btnAddCatalog) btnAddCatalog.setDisabled(true);
    			if(btnModiCatalog) btnModiCatalog.setDisabled(true);
    			if(btnDelCatalog) btnDelCatalog.setDisabled(true);
    		}
    		
    	}
    });
    
    //拖拽移动节点
    this.on("beforenodedrop",function(dropEvent){
    	
    	//判断数据来源是否来源于tree
    	if(!dropEvent.source.tree)	return;
    	
		//移动至的节点
    	var oCurrentNode = dropEvent.target;
    	//选中的节点
		var eventNode = dropEvent.data.node;
		
    	if(oCurrentNode.id == null ||  oCurrentNode.id == ""){
    		dropEvent.cancel = true; 
			return;
		}
		if(oCurrentNode.attributes.type != "catalog"){
			alert('不能将【' + eventNode.text + '】节点移动到一个表节点中,请移动到分类节点');
			dropEvent.cancel = true;
			return;
		}
		
    	var bParentChanged = false; 	//是否换父亲节点了
    	var newParentId = "";		  	//新父亲节点的编号,用于判断是否是根节点，如果为根节点，则保持为空
    	
    	//判断是否换父节点
    	if(eventNode.parentNode.id != oCurrentNode.id){
    		bParentChanged=true;
			//获取新父节点Id
    		if(!oCurrentNode.isRootNode()){
    			newParentId = oCurrentNode.id;
    		}
    	}
    	//没有换父节点 返回
		if(!bParentChanged){
			return;
		}
		
		if(window.confirm('确定移动 【' + eventNode.text + '】节点 至 【' + oCurrentNode.text + '】分类下吗?')){	
		 	Ext.Ajax.request({
		 		url:link20,
		 		success:function(ajax){
		 			var responseText=ajax.responseText;	
		 			var responseObject=Ext.util.JSON.decode(responseText);
		 			alert("移动成功");
		 		},
		 		failure:function(){
		 			alert("服务器忙，请稍后操作");
		 		},
		 		method:'POST',
		 		params:{nodeId:eventNode.id,newParentId:newParentId}
		 	})		
		}
    });
    
}


Ext.extend(LeftTree, Ext.tree.TreePanel, {
	delTable : function(){
		var oNode=this.getSelectionModel().getSelectedNode();
		var tableId= oNode.id;
		var tableCode = oNode.text;
		var ajaxUrl;
		
		if(window.confirm("确认删除当前表定义吗")){
			ajaxUrl=link5+"?id="+tableId+"&type="+oNode.attributes.type;
			//提交数据
		 	Ext.Ajax.request({
		 		url:ajaxUrl,
		 		success:function(ajax){
		 			var responseText=ajax.responseText;	
		 			var responseObject=Ext.util.JSON.decode(responseText);
		 			if(responseObject.success){
		 				alert("删除成功");
		 				oNode.remove();
		 				Ext.Ajax.request({
		 					url: link19 + '?tablecode=' + tableCode,
		 					method: 'post',
		 					success: function(ajax){
		 						eval('responseObj=' + ajax.responseText);
	 							if(responseObj.success == true && responseObj.exist == true){
	 								if(window.confirm("是否删除对应的物理表")){
					 					Ext.Ajax.request({
					 						url: link18 + '?tablecode=' + tableCode,
					 						method: 'post',
					 						success: function(ajax){
					 							eval('responseObj=' + ajax.responseText);
					 							if(responseObj.success == true){
					 								alert('对应物理表删除成功');
					 							}else{
													alert('服务器忙,请稍后再试...');								
					 							}
					 						},
					 						failure: function(){
					 							alert('服务器忙,请稍后再试...');	
					 						}
					 					});
					 				}
	 							}
		 					},
		 					failure: function(){
		 						alert('服务忙,请稍后操作...');
		 					}
		 				});
		 			}else{
		 				alert('服务器忙,请稍后再试...');
		 			}
		 		},
		 		failure:function(){
		 			alert("提交失败");
		 		},
		 		method:'POST'
		 	});	
		}
	},
	
	deleteSelectedCatalog : function(){
		var oNode=this.getSelectionModel().getSelectedNode();
		
		if(window.confirm("确认删除当前分类吗")){
			//提交数据
		 	Ext.Ajax.request({
		 		url:link22,
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
	},
	/**
	 * 修改节点
	 */
	modifyCatalog:function(){
		var oNode=this.getSelectionModel().getSelectedNode();
		treeEditor.editNode = oNode;
	   	treeEditor.startEdit(oNode.ui.textNode);        
	},
	
	/**
	 * 创建新的节点
	 */
	createCatalog:function(bFirst){
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

