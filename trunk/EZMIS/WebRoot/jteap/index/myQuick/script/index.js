
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

var root=new Ext.tree.AsyncTreeNode({
	text:'根节点',
	id:'rootNode',
	loader:tableLoader,
    expanded :true
});

LeftTree= function(){
	
	var tree = this;
	//模块Id的拼接 以,分隔
	var moduleIds = "";
	//子系统模块Id
	var childSysId = "";
	//子系统模块link
	var childSysLink = "";
	var catalogCode ="";
	var ajflag = false;
	
	LeftTree.superclass.constructor.call(this, {
        id:'myQuickTree',
        region:'center',
        split:true,
        height: 300,
        width: 300,
        border:false,
        minSize: 180,
        maxSize: 400,
        collapsible: true,
//        style:'padding-left:10px;',
       	margins:'2px 2px 2px 2px',
//        enableDD:true,  禁止拖动
        rootVisible:false,
        lines:true,
        autoScroll:true,
 		root:root,
        collapseFirst:true
    });
    
    /**
     * 当选中节点的时候，触发事件
     */
    this.getSelectionModel().on("selectionchange",function(oSM,oNode){
    	if(oNode){
    		//选中模块Id
			var childModuleId = oNode.id;
			if(oNode.text.indexOf("首页") != -1){
				//因为子系统首页布局是单独的,所以如此设置
				childModuleId = null;
			}else if(oNode.parentNode.text == "系统管理"){
				//因为系统管理布局是单独的,所以如此设置
			}
			
			//选中模块所属的大模块  如：运行子系统的【交接班管理】
			var bigModuleId = null;
			
			//初始化参数
			moduleIds = "";
    		childSysId = "";
    		childSysLink = "";
			recursionNode(oNode);
			
			moduleIds = moduleIds.substring(0,moduleIds.length-1);
			var array_moduleId = moduleIds.split(",");
			if(array_moduleId.length > 1){
				bigModuleId = array_moduleId[array_moduleId.length-2];
			}
			if(ajflag){
				window.parent.parent.location.reload(contextPath + "/jteap/" + childSysLink + "?moduleId=" + childSysId + "&flag=1&catalogCode=" + catalogCode);
			}else{
				window.parent.parent.location.reload(contextPath + "/jteap/" + childSysLink + "?moduleId=" + childSysId + "&bigModuleId=" + bigModuleId + "&childModuleId=" + childModuleId);
			}
    	}
    });
    
    //递归获取该节点所属的子系统模块Id
    function recursionNode(oNode){
    	moduleIds += oNode.id + ",";
   		if(oNode.parentNode.id != 'rootNode'){
   			if(oNode.attributes.link!=null){
   				if(catalogCode==""&&oNode.attributes.link.indexOf("catalogCode=")!=-1){
					catalogCode = oNode.attributes.link.split("catalogCode=")[1];
				}  			
   			}
			
			recursionNode(oNode.parentNode);
    	}else{
    		//子系统模块Id
    		childSysId = oNode.id;
    		//子系统模块link
    		childSysLink = oNode.attributes.link;
    		if(childSysLink.indexOf("aj")!=-1&&catalogCode!=""){
    			ajflag=true;
    		}
    	}	
    }
    
}

Ext.extend(LeftTree, Ext.tree.TreePanel, {
});

var tree = new LeftTree();

//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[tree]
}
