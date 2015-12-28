/**
 * 用于翻页树节点 tree 的 loader
 * 
 */
Ext.app.PagedTreeNodeLoader=function (config){
	
	//为请求的url加上paged标记
	this.baseParams={};
	if(config.baseParams){
		Ext.apply(config.baseParams,{'paged':'true'});
	}else{
		config.baseParams={'paged':'true'};
	}
	
	//将pageSize和pageNo两个参数加入到config.baseParams里面去
	Ext.apply(config.baseParams,config.pageSize?{pageSize:config.pageSize}:{});
	Ext.apply(config.baseParams,config.pageNo?{pageNo:config.pageNo}:{});
	
	//调用父类构造函数
	Ext.app.PagedTreeNodeLoader.superclass.constructor.call(this,config);
	
	this.on("load",function(loader,node,response ){
		//加载完成后更新分页信息
		node.updatePageCtrlBar();
		//alert("加载完成");
	});
	
}
Ext.extend(Ext.app.PagedTreeNodeLoader,Ext.tree.TreeLoader,{
	/**
	 * 重写父类的处理响应结果的函数
	 * 分页后返回的数据与不分页的数据相比，json数据中多出了分页信息
	 * 而且真实的数据列表存在属性为list的值中，是一个数组
	 * 返回的json数据如下类似：
	 * {'pageNo':1,'pageSize':5,'totalCount':1000,
	 * 	 list:[
	 * 		{"id":"402881471e2516ec011e2516f2410001","text":"测试二级节点0","sortNo":0,"expanded":false},
	 * 		{"id":"402881471e2516ec011e2516f2520002","text":"测试二级节点1","sortNo":1,"expanded":false},
	 * 		{"id":"402881471e2516ec011e2516f2530003","text":"测试二级节点2","sortNo":2,"expanded":false},
	 * 		{"id":"402881471e2516ec011e2516f2530004","text":"测试二级节点3","sortNo":3,"expanded":false},
	 * 		{"id":"402881471e2516ec011e2516f2540005","text":"测试二级节点4","sortNo":4,"expanded":false}
	 *	 ]
	 * }
	 */
	processResponse : function(response, node, callback){
	    var json = response.responseText;
	    try {
	    	
	    	var result = eval("("+json+")");
	        var o = result.list;
	        
	        
	        node.pageCtrl.pageSize=result.pageSize;
	        node.pageCtrl.pageNo=result.pageNo;
	        node.pageCtrl.totalCount=result.totalCount;
	        
	        node.beginUpdate();
	        for(var i = 0, len = o.length; i < len; i++){
	            var n = this.createNode(o[i]);
	            if(n){
	                node.appendChild(n);
	            }
	        }
	        node.endUpdate();
	        if(typeof callback == "function"){
	            callback(this, node);
	        }
	    }catch(e){
	        this.handleFailure(response);
	    }
	},
	 /**
    * 重写父类的创建节点的方法,父类创建的是普通异步节点
    * 而我们需要创建分页的异步节点
    */
    createNode : function(attr){
        if(this.baseAttrs){
            Ext.applyIf(attr, this.baseAttrs);
        }
        if(this.applyLoader !== false){
            attr.loader = this;
        }
        if(typeof attr.uiProvider == 'string'){
           attr.uiProvider = this.uiProviders[attr.uiProvider] || eval(attr.uiProvider);
        }
        if(attr.nodeType){
            return new Ext.tree.TreePanel.nodeTypes[attr.nodeType](attr);
        }else{
            return attr.leaf ?
                        new Ext.app.PagedTreeNode(attr) :
                        new Ext.app.PagedAsyncTreeNode(attr);
        }
    },
    
    
    getParams: function(node){
    	if(node.pageCtrl){
    		this.baseParams.pageNo=node.pageCtrl.pageNo;
    	}
    	return Ext.app.PagedTreeNodeLoader.superclass.getParams.call(this, node);
    }
    
});
