/**
 * 功能树组件
 */
FunctionPanel = function(parentId) {
	functionPanel=this;
    FunctionPanel.superclass.constructor.call(this, {
        id:'func-tree',
        region:'west',
        ddGroup    : 'treeDDGroup',
        //tbar:['->',{iconCls : 'btn_userMapping',handler:openIndividual}],
        title:'时间:',
        split:true,
        width: 200,
        minSize: 200,
        maxSize: 400,
        collapsible: true,
        margins:'0 0 0 0',
        cmargins:'0 5 5 5',
        rootVisible:false,
        lines:false,
        autoScroll:true,
        enableDrag :true,
       
       root:new Ext.tree.AsyncTreeNode({
        	text:'系统功能',
        	id:parentId,
        	loader:new Ext.tree.TreeLoader({
	            dataUrl:link4
	           // listeners:{
	           // 		load:function(){
	           // 			alert('x');
	           // 		}
	           // }
	        }),
	        expanded :true
        })
        
    });
    
    
    
    //不让一级节点被选中
    
    this.getSelectionModel().on("beforeselect",function(oDSM, oNode){
    	if(oNode.attributes.link==null || oNode.attributes.link=='')
    		return false;
    });
    //点击加载模块
    this.on('click',function(node,e){
		if(node.attributes.link!=null  && node.attributes.link!=""){
			mainPanel.loadFunction(node.id,node.text,node.attributes.link,node.attributes.resStyle);
		}
		e.stopEvent();
	});
	/*
	this.getRootNode().on("load", function() {
		Ext.Ajax.request({
		    url : link11,
		    method : 'post',
		    success : function(ajax){
		          var responseText = ajax.responseText;
		          var obj = Ext.decode(responseText);
		          var data = obj.data;
		          data.each(function(item){
		              var btn = new Ext.Button({
		                  id : item.id,
		                  text : item.text,
		                  cls : 'x-btn-text-icon',
                          icon : item.icon,
		                  handler : function() {
		                      mainPanel.loadFunction(item.id,item.text,item.link,item.resStyle);
		                  }
		              })
		              mainToolbar.addItem(btn);
		          })
		    }
		})
	})
	*/
	function openIndividual() {
		var url ='system/module/individual.jsp'
		showModule(url,true,730,520);
	}
	

};

Ext.extend(FunctionPanel, Ext.tree.TreePanel, {

});