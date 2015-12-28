
//加载table节点的loader
var tableLoader=new Ext.tree.TreeLoader({
	dataUrl:link21
});

//添加数据源对象参数
tableLoader.on("beforeload", function(treeLoader, node) {
		if(!node.parentNode || node.parentNode.attributes.text=='工程类别' ){
			tableLoader.baseParams.isProjcat = '1';		//工程类别
		}else{
			tableLoader.baseParams.isProjcat = '0';		//工程项目
		}
		tableLoader.baseParams.parentId=node.isRoot?"":node.id;
        //treeLoader.baseParams.datasource = node.attributes.datasource;
    }, this);

//to do in the program
var rootLoader=new Ext.tree.AsyncTreeNode({
	text:'工程类别',
	loader:tableLoader,
	type:'table',
    expanded :true
});
/**
 * 左边的树
 */
GroupTree= function(){
	GroupTree.superclass.constructor.call(this, {
        id:'groupTree',
        region:'west',
        split:true,
        width: 300,
        height:500,
        minSize: 180,
        maxSize: 400,
        collapsible: true,
        margins:'2px 2px 2px 2px',
        cmargins:'0 0 0 0',
        enableDD:true,
        rootVisible:true,
        lines:true,
        autoScroll:true,
 		root:rootLoader,
 		tbar:[{
 			icon:'../../../resources/images/hb3e/grid/refresh.gif',
 			iconCls:'x-icon-style',
 			handler:function(){
 				var selectionNode=groupTree.getSelectionModel().getSelectedNode();
 				if(selectionNode){
 					try{
 						//leftTree.getLoader().load(selectionNode);
						//selectionNode.oTreeNode.expand();
 						selectionNode.reload();
 					}catch(e){
 						//alert(e);
 					}
 				}else{
 					groupTree.getRootNode().reload();
 				}
 			}
 		}],
        collapseFirst:true
    });
    
  
    /**
     * 当选中节点的时候，触发事件
     */
    this.getSelectionModel().on("selectionchange",function(oSM,oNode){
   	/**
    	var message = "";
    	if(oNode.leaf){
			if(oNode!=null){
				Ext.Ajax.request({
					url:link24,
					success:function(ajax){
				 		var responseText=ajax.responseText;	
				 		var responseObject=Ext.util.JSON.decode(responseText);
				 		if(responseObject.success){
				 			if(responseObject.taskdesc != "null"){
					 			message = oNode.attributes.text+"：主要包括【"+responseObject.taskdesc+"】";
					 			Ext.MessageBox.show({
							    title:"工程项目说明",
							    msg:message,
							    buttons:{"ok":"关闭"},
							    fn:function(e){},
							    animEl:"test1",
							    width:300,
							    icon:Ext.MessageBox.INFO,
							    closable:false
							    //progress:true,
							    //wait:true
							   // progressText:"进度条"
							   // prompt:true
							   // multiline:true
								});	
					 			Ext.MessageBox.getDialog().setPosition(0, 350);
				 			}
				 		}else{
				 			alert(responseObject.msg);
				 		}				
					},
				 	failure:function(){
				 		alert("提交失败");
				 	},
				 	method:'POST',
				 	params: {projid:oNode.id}//Ext.util.JSON.encode(selections.keys)			
				});
				
				//Ext.MessageBox.alert('项目工程说明','"'+oNode.attributes.text+'"');               
	    	//	window.returnValue=oNode.attributes.text+'|'+oNode.attributes.flbm;
	    	}
    	}
    	**/
    });

}


Ext.extend(GroupTree, Ext.tree.TreePanel, {
});

