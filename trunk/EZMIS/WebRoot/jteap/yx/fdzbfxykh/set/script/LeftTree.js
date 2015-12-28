

//加载schema节点的loader
var schemaLoader=new Ext.tree.TreeLoader({
	dataUrl:link1,
   	applyLoader:false			//表示下级节点采用动态产生loader的方式，以返回的数据决定下一个loader是哪个loader
});

//加载table节点的loader
var tableLoader=new Ext.tree.TreeLoader({
	dataUrl:link1
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

/**
 * 左边的树
 */
LeftTree= function(){
    
	LeftTree.superclass.constructor.call(this, {
        id:'tableTree',
        region:'west',
        split:true,
        width: 155,
        minSize: 180,
        maxSize: 400,
        collapsible: true,
        margins:'0 0 1 1',
        cmargins:'0 5 5 5',
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
    	if(oNode){
    		var btnModiTable=mainToolbar.items.get("btnModiTable");
    		var btnDelTable=mainToolbar.items.get("btnDelTable");
    		var btnAddColumn=mainToolbar.items.get("btnAddColumn");
    		var btnRebuildTable=mainToolbar.items.get("btnRebuildTable");
			
    		//选中的是表节点,即可修改、删除对应的表
    		if(oNode.id != "rootNode"){
    			var url=link8+"?limit=18&directiveId="+oNode.id;
    			columnGrid.table_id = oNode.id;
    			columnGrid.changeToListDS(url);
    			columnGrid.store.reload();
    			
    			if(btnModiTable) btnModiTable.setDisabled(false);
    			if(btnDelTable) btnDelTable.setDisabled(false);
    			if(btnAddColumn) btnAddColumn.setDisabled(false);
    			if(btnRebuildTable) btnRebuildTable.setDisabled(false);
    		}else{
    			var url=link8+"?limit=18&directiveId=null";
    			columnGrid.changeToListDS(url);
				columnGrid.store.reload();
				
    			if(btnModiTable) btnModiTable.setDisabled(true);
    			if(btnDelTable) btnDelTable.setDisabled(true);
    			if(btnAddColumn) btnAddColumn.setDisabled(true);
    			if(btnRebuildTable) btnRebuildTable.setDisabled(true);
    		}
    		
    	}
    });
    
}

Ext.extend(LeftTree, Ext.tree.TreePanel, {
	delTable : function(){
		var oNode=this.getSelectionModel().getSelectedNode();
		var tableId= oNode.id;
				 
		if(window.confirm("确认删除当前表定义吗")){
			//删除小指标表定义、字段定义 返回该表定义的 tableCode
		 	Ext.Ajax.request({
		 		params: {id: tableId},
		 		method: 'post',
		 		url: link4,
		 		success:function(ajax){
		 			var responseText=ajax.responseText;	
		 			var responseObject=Ext.util.JSON.decode(responseText);
		 			if(responseObject.success){
		 				alert("删除成功");
		 				oNode.remove();
		 				columnGrid.getStore().reload();
		 				
		 				var tableCode = responseObject.tableCode;
		 				if(tableCode == ""){
		 					return;
		 				}
		 				
		 				//判断该 tableCode对应的物理表是否存在
		 				Ext.Ajax.request({
		 					url: link5,
		 					method: 'post',
		 					params: {tableCode: tableCode},
		 					success: function(ajax){
		 						eval('responseObj=' + ajax.responseText);
	 							if(responseObj.success == true && responseObj.exist == true){
	 								if(window.confirm("是否删除对应的物理表")){
	 									//删除该 tableCode对应的物理表
					 					Ext.Ajax.request({
					 						url: link6,
					 						method: 'post',
					 						params: {tableCode: tableCode},
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
		 			alert('服务忙,请稍后操作...');
		 		},
		 		method:'POST'
		 	});	
		}
	}
});

