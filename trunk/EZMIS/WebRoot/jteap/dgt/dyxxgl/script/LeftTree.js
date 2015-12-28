
//加载schema节点的loader
var schemaLoader=new Ext.tree.TreeLoader({
	dataUrl:link3,
   	applyLoader:false			//表示下级节点采用动态产生loader的方式，以返回的数据决定下一个loader是哪个loader
});

//加载table节点的loader
var tableLoader=new Ext.tree.TreeLoader({
	dataUrl:link3
});


//to do in the program
var rootLoader=new Ext.tree.AsyncTreeNode({
	text:'党组织',
	loader:schemaLoader,
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
        width: 180,
        minSize: 180,
        maxSize: 400,
        collapsible: true,
       	margins:'2px 2px 2px 2px',
        enableDD:true,
        rootVisible:true,
        lines:true,
        autoScroll:true,
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
    
    
    /**
     * 当选中节点的时候，触发事件
     */
    this.getSelectionModel().on("selectionchange",function(oSM,oNode){
    	var btnModDzz=mainToolbar.items.get('btnModDzz');
		var btnDelDzz=mainToolbar.items.get('btnDelDzz');
		
		if(oNode==null){
			if(btnModDzz)btnModDzz.setDisabled(true);
			if(btnDelDzz)btnDelDzz.setDisabled(true);
		}else if(oNode.isRoot){
			if(btnModDzz)btnModDzz.setDisabled(true);
			if(btnDelDzz)btnDelDzz.setDisabled(true);
		}else{
			if(btnModDzz)btnModDzz.setDisabled(false);
			if(btnDelDzz)btnDelDzz.setDisabled(false);
			var url2=link2+"?id='"+oNode.attributes.id+"'";
	    	rightGrid.changeToListDS(url2);
	    	rightGrid.getStore().reload();
		}
		
		
    });
    
}


Ext.extend(LeftTree, Ext.tree.TreePanel, {
/**
     * 删除组织
    
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
    }
*/
   
});

