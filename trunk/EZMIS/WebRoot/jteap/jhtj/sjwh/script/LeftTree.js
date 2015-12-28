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
        enableDD:true,
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
		
		if(oNode==null){
			if(btnAddCatalog) btnAddCatalog.setDisabled(true);
			if(btnModiCatalog) btnModiCatalog.setDisabled(true);
			this.tree.root.select();
		}else{
    		if(oNode.isRootNode()){
    			if(btnAddCatalog) btnAddCatalog.setDisabled(true);
				if(btnModiCatalog) btnModiCatalog.setDisabled(true);
	    	}else{
	    		var flflag=oNode.attributes.flflag;
	    		var kid=oNode.attributes.kid;
	    		//非节点数据,是叶子节点
	    		if(flflag!=0&&oNode.isLeaf()){
	    			if(btnModiCatalog) btnModiCatalog.setDisabled(true);
					if(btnAddCatalog) btnAddCatalog.setDisabled(false);
					//机组数据
					if(flflag==4){
						leftTree.initTable(kid,oNode.id,"");
						var parentNode=oNode.parentNode;
						searchPanel.removeComponent();
						var parentFlflag=parentNode.attributes.flflag;
						//日数据
						if(parentFlflag=="1"){
							//searchPanel.collapse(true);
							//searchPanel.collapse(true);
							//searchPanel.expand(true);
							searchPanel.searchDefaultFs=("年#NIAN#comboBox#"+parentFlflag+"#1,月#YUE#comboBox#"+parentFlflag+"#2").split(",");
							searchPanel.searchAllFs=("年#NIAN#comboBox#"+parentFlflag+"#1,月#YUE#comboBox#"+parentFlflag+"#2").split(",");
						    searchPanel.addComponent();
						}else if(parentFlflag=="2"){
						//月数据
							//searchPanel.collapse(true);
							//searchPanel.expand(true);
							searchPanel.searchDefaultFs=("年#NIAN#comboBox#"+parentFlflag+"#1").split(",");
							searchPanel.searchAllFs=("年#NIAN#comboBox#"+parentFlflag+"#1").split(",");
						    searchPanel.addComponent();
						}else{
						//年数据
							searchPanel.removeComponent();
							searchPanel.collapse(true);
						}
					}else{
					//全厂数据
						leftTree.initTable(kid,"","");
						searchPanel.removeComponent();
						//日数据
						if(flflag=="1"){
							//searchPanel.collapse(true);
							//searchPanel.expand(true);
							searchPanel.searchDefaultFs=("年#NIAN#comboBox#"+flflag+"#1,月#YUE#comboBox#"+flflag+"#2").split(",");
							searchPanel.searchAllFs=("年#NIAN#comboBox#"+flflag+"#1,月#YUE#comboBox#"+flflag+"#2").split(",");
						    searchPanel.addComponent();
						}else if(flflag=="2"){
						//月数据
							//searchPanel.collapse(true);
							//searchPanel.expand(true);
							searchPanel.searchDefaultFs=("年#NIAN#comboBox#"+flflag+"#1").split(",");
							searchPanel.searchAllFs=("年#NIAN#comboBox#"+flflag+"#1").split(",");
						    searchPanel.addComponent();
						}else{
						//年数据
							searchPanel.removeComponent();
							searchPanel.collapse(true);
						}
					}
	    		}else{
	    			searchPanel.removeComponent();
	    			//searchPanel.collapse(true);
					searchPanel.collapse(true);
	    			if(btnAddCatalog) btnAddCatalog.setDisabled(true);
					if(btnModiCatalog) btnModiCatalog.setDisabled(true);
	    		}
	    	}
		    	//var url=link4+"?sysid="+oNode.parentNode.id+"&appioid="+oNode.id;
		    	//rightGrid.changeToListDS(url);
		    	//rightGrid.getStore().reload();
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
	initTable:function(kid,flid,keyList){
		//var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"数据查询中，请稍等"}); 
		//myMask.show();
		var cm;
		var param={};
		param.kid=kid;
		param.flid=flid;
		AjaxRequest_Sync(link8, param, function(req) {
			var responseText=req.responseText;	
			var responseObject=Ext.util.JSON.decode(responseText);
			if(responseObject.success){
				var cols=responseObject.list;
				var cm=leftTree.buildCm(cols);
				var ds=leftTree.buildDs(cols,kid,flid,keyList);
				queryGrid.updateData(cm,ds);
			}else{
				alert("数据查询失败");
			}
		});
	},
	buildCm:function(cols){
		var tmpArray=new Array();
		//tmpArray.push(new Ext.grid.RowSelectionModel({singleSelect:true}));
		for(var i=0;i<cols.length;i++){
			var data=cols[i];
			for (var p in cols[i]) {
				var tmpCm={};
				//条件字段
				tmpCm={"id":data[p],"header":p,"width":60,"sortable":true,"dataIndex":data[p]};
				tmpArray.push(tmpCm);
			}
		}
		cm=new Ext.grid.ColumnModel(tmpArray);
		return cm;
	},
	buildDs:function(cols,kid,flid,keyList){
		var colArray=new Array();
		for(var i=0;i<cols.length;i++){
			var data=cols[i];
			for (var p in cols[i]) {
				var columncode=data[p];
				colArray.push(columncode);
			}
		}
		var ds = new Ext.data.Store({
	        proxy: new Ext.data.HttpProxy({
	            url: link9+"?kid="+kid+"&flid="+flid+"&keyList="+keyList
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount'
	        }, colArray),
	        remoteSort: true	
	    });
		return ds;
	}
});

