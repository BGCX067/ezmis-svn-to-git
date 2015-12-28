/**
 * 功能树组件
 */
FunctionPanel = function(parentId,moduleResName,childId) {
	functionPanel=this;
	var rootloader=new Ext.tree.TreeLoader({
        dataUrl:link14,
        listeners:{
	    	load:function(loader, node, callback ){
	    		//初始化选中第一个节点
	    		if(childId != "null"){
	    			var items = functionPanel.getRootNode().childNodes;
	    			for(var i = 0; i < items.length; i++){
	    				if(items[i].id == childId){
	    					items[i].select();
	    				}else{
	    					functionPanel.getNodeById(childId).select();
	    				}
	    			}	
	    		}else{
	    			//判断是否有权限，如果没有权限就不显示内容(modify by 2010-08-06)
	    			if(typeof(functionPanel.getRootNode().childNodes[0]) == 'undefined'){
	    				//alert("没有数据");
	    			}else{
		    			if(functionPanel.getRootNode().childNodes[0].leaf){
				    		functionPanel.getRootNode().childNodes[0].select();
		    			}else{
		    				if (functionPanel.getRootNode().childNodes[0].childNodes.length  > 0) {
			    				functionPanel.getRootNode().childNodes[0].childNodes[0].select();
		    				}
		    			}
	    			}
	    		}
	    	},
	    	beforeload:function(loader, node, callback ){
	       		this.baseParams.moduleId=(node.isRoot?parentId:node.id);
        	}
	    }
    });
	        
    FunctionPanel.superclass.constructor.call(this, {
        id:'func-tree',
       // region:'west',
        renderTo:"leftMenu",
        ddGroup    : 'treeDDGroup',
        //tbar:['->',{iconCls : 'btn_userMapping',handler:openIndividual}],
        title:moduleResName,
        split:true,
        //width: 200,
        minSize: 200,
        maxSize: 400,
        border:false,
        collapsed: false,
        collapsible: true,
        margins:'0 0 0 0',
        cmargins:'0 0 0 0',
        rootVisible:false,
        lines:false,
        autoScroll:true,
        enableDrag :true,
       	root:new Ext.tree.AsyncTreeNode({
        	text:'系统功能',
        	id:parentId,
        	collapsed:false,
        	loader:rootloader,
	        listeners : {
	        	 beforeexpand:function(node) {
	        	 		rootloader.baseParams.moduleId=node.id;
				    }
	        },
	        expanded :true
    	})
    });
    this.expandAll();
	//初始化触发第一个节点的onclick事件(显示第一个选中节点的右边内容)
    this.getSelectionModel().on("selectionchange",function(oSM,node) {
    	if(node.attributes.link!=null  && node.attributes.link!=""){
			//document.frames[0].loadFunction(node.id,node.text,node.attributes.link,node.attributes.resStyle);
    		//mainPanel.loadFunction(node.id,node.text,node.attributes.link,node.attributes.resStyle);
    		if(node.attributes.link.indexOf("?") == -1){
	    		$('rightModule').src = contextPath + '/jteap/'+node.attributes.link+"?moduleId="+node.id;
    		}else{
    			$('rightModule').src = contextPath + '/jteap/'+node.attributes.link+"&moduleId="+node.id;
    		}
			document.getElementById("moduleResName").innerText = node.text;
		}
	});
    //不让一级节点被选中
    this.getSelectionModel().on("beforeselect",function(oDSM, oNode){
    	if(oNode.attributes.link==null || oNode.attributes.link=='')
    		return false;
    });
    //点击加载模块
    this.on('click',function(node,e){
		if(node.attributes.link!=null  && node.attributes.link!=""){
			//document.frames[0].loadFunction(node.id,node.text,node.attributes.link,node.attributes.resStyle);
			//mainPanel.loadFunction(node.id,node.text,node.attributes.link,node.attributes.resStyle);
			if(node.attributes.link.indexOf("?") == -1){
	    		$('rightModule').src = contextPath + '/jteap/'+node.attributes.link+"?moduleId="+node.id;
    		}else{
    			$('rightModule').src = contextPath + '/jteap/'+node.attributes.link+"&moduleId="+node.id;
    		}
			document.getElementById("moduleResName").innerText = node.text;
		}
		e.stopEvent();
	});
	
	function openIndividual() {
		var url ='system/module/individual.jsp'
		showModule(url,true,730,520);
	}
	

};

Ext.extend(FunctionPanel, Ext.tree.TreePanel, {

});