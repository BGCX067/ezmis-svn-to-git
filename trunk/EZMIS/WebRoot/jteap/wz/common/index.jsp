<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
  </head>
 
  <body scroll="no" id="index">
	 <!-- 加载等待图标 开始 -->
	<div id="loading-mask" style=""></div>
	<div id="loading">
	  <div class="loading-indicator">
	  	<img src="${contextPath}/resources/extanim32.gif" width="32" height="32" style="margin-right:8px;" align="absmiddle"/>Loading...
	  </div>
	</div>
   	 <!-- 加载等待图标 结束 -->
   	 
   	<!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>
		
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>
	<!-- 入口程序 -->
    <script type="text/javascript">
  //加载table节点的loader
    var tableLoader=new Ext.tree.TreeLoader({
    	dataUrl:link1+"?flbm=WZ"
    });

    //添加数据源对象参数
    tableLoader.on("beforeload", function(treeLoader, node) {
    	 tableLoader.baseParams.parentId=(node.isRoot?"":node.id);
        }, this);

    //to do in the program
    var rootLoader=new Ext.tree.AsyncTreeNode({
    	id:'root',
    	text:'工程项目信息',
    	loader:tableLoader,
    	type:'table',
        expanded :true
    });

    /**
     * 左边的树
     */
    LeftTree= function(){
    	LeftTree.superclass.constructor.call(this, {
            id:'tableTree',
            region:'north',
//        	layout:'border',
            split:true,
            width: 260,
            minSize: 180,
            maxSize: 400,
            height:400,
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
     				var selectionNode=leftTree.getSelectionModel().getSelectedNode();
     				if(selectionNode){
     					try{
     						//leftTree.getLoader().load(selectionNode);
    						//selectionNode.oTreeNode.expand();
     						selectionNode.reload();
     					}catch(e){
     						//alert(e);
     					}
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
    		if(oNode&&!oNode.isRootNode()){
        		window.returnValue=oNode.attributes.text+'|'+oNode.attributes.flbm;
        		window.close();
	       	}
        });

    }


    Ext.extend(LeftTree, Ext.tree.TreePanel, {
    });
  //左边的树
    var leftTree=new LeftTree()
 
    	Ext.onReady(function(){			
			Ext.QuickTips.init();
			var viewport=new Ext.Viewport({
	//			layout:'border',
				items:[leftTree]
			});
			
		  	//程序加载完成后撤除加载图片
		    setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250);
		});	
		
		
    </script>
    <!-- 加载脚本库  结束 -->
    
    <!-- 页面内容 to do something in here -->

  </body>
</html>
