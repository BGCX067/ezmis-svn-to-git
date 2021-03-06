//根节点加载器
var rootLoader=new Ext.tree.TreeLoader({
	dataUrl:link1,
    listeners:{
    	beforeload:function(loader, node, callback ){
    		this.baseParams.parentId=(node.isRoot?"":node.id);
    	}
    }
});



//to do in the program
var rootNode=new Ext.tree.AsyncTreeNode({
	text:'数据分类',
	loader:rootLoader,
	ccCheck : true,
    expanded :true
});

 rootNode.on("load",function(){
	var selNode=leftTree.getRootNode();
	selNode.select();
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
	LeftTree.superclass.constructor.call(this, {
        id:'tableTree',
        region:'west',
        width: 230,
        minSize: 180,
        maxSize: 400,
        title:'&nbsp;',
        collapsible: true,
        margins:'2px 2px 2px 2px',
        cmargins:'0 0 0 0',
        enableDD:false,
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
		 				alert("新建分类【"+value+"】成功");
		 			else
		 				alert("新建分类【"+value+"】失败");
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
     * 
     */
    this.getSelectionModel().on("selectionchange",function(oSM,oNode){
    	//修改菜单栏按钮状态
    	var btnAddCatalog=mainToolbar.items.get("btnAddCatalog");
		var btnModiCatalog=mainToolbar.items.get("btnModiCatalog");
		var btnDelCatalog=mainToolbar.items.get("btnDelCatalog");
		var btnAddKey=mainToolbar.items.get("btnAddKey");
		var btnModiKey=mainToolbar.items.get("btnModiKey");
		var btnDelKey=mainToolbar.items.get("btnDelKey");
		var btnGhostKey=mainToolbar.items.get("btnGhostKey");
		
		if(oNode==null){
			if(btnAddCatalog) btnAddCatalog.setDisabled(true);
			if(btnModiCatalog) btnModiCatalog.setDisabled(true);
			if(btnDelCatalog) btnDelCatalog.setDisabled(true);
			this.tree.root.select();
		}else{
			var flflag=oNode.attributes.flflag;
    		if(oNode.isRootNode()){
    			if(btnAddCatalog) btnAddCatalog.setDisabled(false);
				if(btnModiCatalog) btnModiCatalog.setDisabled(true);
				if(btnDelCatalog) btnDelCatalog.setDisabled(true);
				if(btnAddKey) btnAddKey.setDisabled(true);
				if(btnModiKey) btnModiKey.setDisabled(true);
				if(btnDelKey) btnDelKey.setDisabled(true);
				if(btnGhostKey) btnGhostKey.setDisabled(true);
	    	}else{
	    		//节点数据
	    		if(flflag==0){
	    			if(btnAddCatalog) btnAddCatalog.setDisabled(false);
	    			if(btnAddKey) btnAddKey.setDisabled(true);
					if(btnModiKey) btnModiKey.setDisabled(true);
					if(btnDelKey) btnDelKey.setDisabled(true);
					if(btnGhostKey) btnGhostKey.setDisabled(true);
	    		}else{
	    			if(btnAddCatalog) btnAddCatalog.setDisabled(true);
	    			if(btnAddKey) btnAddKey.setDisabled(false);
	    			if(btnGhostKey) btnGhostKey.setDisabled(false);
	    			var kid=oNode.attributes.kid;
	    			if(kid==undefined){
	    				kid="";
	    			}
	    			var url=link4+"?kid="+kid;
			    	rightGrid.changeToListDS(url);
			    	rightGrid.getStore().reload();
	    		}
    			if(btnModiCatalog) btnModiCatalog.setDisabled(false);
				if(btnDelCatalog) btnDelCatalog.setDisabled(false);
	    	}
	    	
	    	if(oNode.isLeaf()){
	    		//if(btnModiCatalog) btnModiCatalog.setDisabled(false);
				//if(btnDelCatalog) btnDelCatalog.setDisabled(false);
		    	//var url=link4+"?sysid="+oNode.parentNode.id+"&appioid="+oNode.id;
		    	//rightGrid.changeToListDS(url);
		    	//rightGrid.getStore().reload();
	    	}
	    	
		}
    	
    });
    
    
}


Ext.extend(LeftTree, Ext.tree.TreePanel, {
	deleteSelectedNode : function(){
		var oNode=this.getSelectionModel().getSelectedNode();
		var firstChild =oNode.firstChild;
		if(firstChild==null){
			if(window.confirm("确认删除当前分类吗")){
				//提交数据
			 	Ext.Ajax.request({
			 		url:link2,
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
		}else{
			alert("请先删除子节点!");
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
    
    },
    deleteChildrenSelectedNode : function(){
		var oNode=this.getSelectionModel().getSelectedNode();
		if(window.confirm("确认删除当前定义吗")){
			//提交数据
		 	Ext.Ajax.request({
		 		url:link14,
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
		 		params:{sisid:oNode.parentNode.id,vname:oNode.text}
		 	})	
		}
	},
	ghostState:function(kid){
		var param={};
		param.kid=kid;
		AjaxRequest_Sync(link14,param,function(request){
			var responseText = request.responseText;
			var responseObj = responseText.evalJSON();
			if(responseObj.success){
				alert("复位成功!");
			}else{
				alert("复位失败!");
			}
		});
	}
});

