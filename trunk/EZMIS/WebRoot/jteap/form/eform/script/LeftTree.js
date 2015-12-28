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
	text:'所有分类',
	loader:rootLoader,
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
	var leftTree = this;
	LeftTree.superclass.constructor.call(this, {
        id:'tableTree',
        region:'west',
        width: 180,
        minSize: 180,
        maxSize: 400,
        collapsible: true,
        margins:'2px 2px 2px 2px',
        cmargins:'0 0 0 0',
        enableDD:true,
        rootVisible:true,
        //启用拖动
        ddGroup:'GridDD',
        dropAllowed: true, 
		dragAllowed: true,
        split:false,
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
		var btnAddEFormCForm=mainToolbar.items.get("btnAddEFormCForm");
		var btnAddExcelCForm=mainToolbar.items.get("btnAddExcelCForm");
		
		
		if(oNode==null){
			if(btnAddCatalog) btnAddCatalog.setDisabled(true);
			if(btnModiCatalog) btnModiCatalog.setDisabled(true);
			if(btnDelCatalog) btnDelCatalog.setDisabled(true);
			if(btnAddEFormCForm) btnAddEFormCForm.setDisabled(true);
			if(btnAddExcelCForm) btnAddExcelCForm.setDisabled(true);
			this.tree.root.select();
		}else{
			if(btnAddCatalog) btnAddCatalog.setDisabled(false);
			
    		
    		if(oNode.isRootNode()){
    			if(btnModiCatalog) btnModiCatalog.setDisabled(true);
    			if(btnDelCatalog) btnDelCatalog.setDisabled(true);
	    		if(btnAddEFormCForm) btnAddEFormCForm.setDisabled(true);
	    		if(btnAddExcelCForm) btnAddExcelCForm.setDisabled(true);
	    	}else{
	    		if(btnModiCatalog) btnModiCatalog.setDisabled(false);
    			if(btnDelCatalog) btnDelCatalog.setDisabled(false);
		    	if(btnAddEFormCForm) btnAddEFormCForm.setDisabled(false);
		    	if(btnAddExcelCForm) btnAddExcelCForm.setDisabled(false);
	    	}
	    	
	    	var url=link4+(oNode.isRootNode()?"":"?catalogId="+oNode.id);
	    	rightGrid.changeToListDS(url);
	    	rightGrid.getStore().reload();
		}


    	
    });
    
    /**
     * 相应拖放事件，主要用于将grid中的数据拖放到Tree中
    */
    this.on("beforenodedrop",function(dropEvent){
    	
    	//判断数据来源是否来源于grid
    	if(!dropEvent.source.grid)	return;
    	
		//移动至的节点
    	var oCurrentNode = dropEvent.target;
    	//如果拖拽至根节点 返回
		if( oCurrentNode.id == null ||  oCurrentNode.id == "" ||  oCurrentNode.id == this.getRootNode().id){
			return;
		}
    	
		//grid拖选项
    	var forms = dropEvent.data.selections;
    	var formIds = "";
    	var formNames = "";
    	for(var i=0; i<forms.length; i++){
    		if(forms[i].json.catalog.id !=  oCurrentNode.id){
	    		formIds += forms[i].id + ",";
	    		formNames += forms[i].data.title + ",";
    		}
    	}
		if(formIds == ""){
			return;
		}
    	
       if(window.confirm('确定移动 【' + formNames + '】表单 至 【' + oCurrentNode.text + '】分类下吗?')){
       		//移动
			leftTree.moveFormToCatalog(formIds,oCurrentNode);
       }
       
    });
    
}


Ext.extend(LeftTree, Ext.tree.TreePanel, {
	deleteSelectedNode : function(){
		var oNode=this.getSelectionModel().getSelectedNode();
		
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
    /**
     * 移动表单至一个分类
     * @param {} formIds
     * @param {} oCurrentNode
     */
    moveFormToCatalog: function(formIds,oCurrentNode){
    	var leftTree = this;
    	Ext.Ajax.request({
    		url: link7,
    		method: 'post',
    		params: {formIds: formIds, oCurrentNodeId: oCurrentNode.id},
    		success: function(ajax){
				eval("responseObj="+ajax.responseText);
				if(responseObj.success == true){
					alert('移动成功');
					oCurrentNode.select();
					var url=link4 + "?catalogId=" + oCurrentNode.id;
			    	rightGrid.changeToListDS(url);
			    	rightGrid.getStore().reload();
				}
    		},
    		failure: function(){
    			alert('服务器忙,请稍后再试...');
    		}
    	})
    }
    
});

