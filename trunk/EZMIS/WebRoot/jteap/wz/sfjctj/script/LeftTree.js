
//加载schema节点的loader
var schemaLoader=new Ext.tree.TreeLoader({
	dataUrl:link3,
   	applyLoader:false,
    listeners:{
    	load:function(loader, node, callback ){
    		//初始化选中第一个节点
   			var items = leftTree.getRootNode().childNodes;
   			if(items.length>0){
   				leftTree.getRootNode().childNodes[0].select();
   			}
	    }
    }			//表示下级节点采用动态产生loader的方式，以返回的数据决定下一个loader是哪个loader
});

//加载table节点的loader
var tableLoader=new Ext.tree.TreeLoader({
	dataUrl:link3
});


//to do in the program
var rootLoader=new Ext.tree.AsyncTreeNode({
	text:'已生成报表年月',
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
 				leftTree.getRootNode().reload();
 			}
 		}],
        collapseFirst:true
    });
    
    
    /**
     * 当选中节点的时候，触发事件
     */
    this.getSelectionModel().on("selectionchange",function(oSM,oNode){
		
		//导出按钮
		var btnExce=mainToolbar.items.get('btnExce');
		//删除按钮
		var btnDel=mainToolbar.items.get('btnDel');
		//新建按钮
		var btnAdd=mainToolbar.items.get('btnAdd');
		if(oNode==null){
			if(btnAdd)btnAdd.setDisabled(true);
			if(btnExce)btnExce.setDisabled(true);
			if(btnDel)btnDel.setDisabled(true);
		}else if(oNode.isRoot){
			if(btnAdd){btnAdd.setDisabled(false);btnAdd.setText("新建统计")};
			if(btnExce)btnExce.setDisabled(true);
			if(btnDel)btnDel.setDisabled(true);
		}else{
			if(btnExce)btnExce.setDisabled(false);
			if(btnDel)btnDel.setDisabled(false);
			if(btnAdd){btnAdd.setDisabled(false);btnAdd.setText("重新统计")};
			var url2=link2+"&ny="+encodeURIComponent(oNode.attributes.text)+"";
			$('sf#ny').value=oNode.attributes.text;
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

