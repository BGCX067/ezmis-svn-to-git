
var Tree = Ext.tree;

function createTree3(){
	var tree3=new Tree.TreePanel({
    	id:'xxTree3',
        el:'tree-div3',
        collapsible :true,
        autoScroll:true,
        title:'异步分页加载数据',
        animate:true,
        
        containerScroll: true,
        
        bbar:new Ext.Toolbar({
        	items:['工具栏','->',{
        		enableToggle :false,
        		handler : function(){alert('ok')},
        		tooltip :'工具按钮',
        		text:'按钮'
        	}]
        }),
        enableDrag :true,
        enableDD :true,
        border :false,
        
        buttonAlign :'left',
        hideBorders :false,
        rootVisible :true,
        lines :true,
        shim :true,
        titleCollapse : true,
        bodyBorder :true,

        buttons :[{
        		enableToggle :true,
        		handler : function(){alert('ok')},
        		tooltip :'工具按钮',
        		text:'操作按钮',
        		disabled :false
        		
        	}],
        root:new Ext.app.PagedAsyncTreeNode({
        	text:'根节点',
        	id:'root',
        	uiProvider:Ext.app.PagedTreeNodeUI,
        	loader:new Ext.app.PagedTreeNodeLoader({
        		baseAttrs :{
	        		expanded:false,
	        		disabled:false
        		},
    			pageSize:100,
        		pageNo:1,
	            dataUrl:'/JTEAP/jteap/component/NodeAction!showTreeAction.do'
	        }),
	        expanded :false
        })
    });
    return tree3;
}


function createTree2(){
	var tree2=new Tree.TreePanel({
    	id:'xxTree2',
        el:'tree-div2',
        collapsible :true,
        autoScroll:true,
        title:'异步大量加载数据',
        animate:true,
        
        containerScroll: true,
        
        bbar:new Ext.Toolbar({
        	items:['工具栏','->',{
        		enableToggle :false,
        		handler : function(){alert('ok')},
        		tooltip :'工具按钮',
        		text:'按钮'
        	}]
        }),
        enableDrag :true,
        enableDD :true,
        border :false,
        
        buttonAlign :'left',
        hideBorders :false,
        rootVisible :true,
        lines :true,
        shim :true,
        titleCollapse : true,
        bodyBorder :true,

        buttons :[{
        		enableToggle :true,
        		handler : function(){alert('ok')},
        		tooltip :'工具按钮',
        		text:'操作按钮',
        		disabled :false
        		
        	}],
        root:new Tree.AsyncTreeNode({
        	text:'根节点',
        	loader:new Tree.TreeLoader({
        		baseAttrs :{
	        		expanded:false,
	        		disabled:false
        		
        		},
	            dataUrl:'/JTEAP/jteap/component/NodeAction!showTreeAction.do'
	        }),
	        expanded :false
        })
    });
    return tree2;
}

function createTree1(){
	var tree=new Tree.TreePanel({
    	id:'xxTree',
        el:'tree-div',
        collapsible :true,
        autoScroll:true,
        title:'sss',
        animate:true,
        enableDD:true,
        containerScroll: true,
        defaults: {bodyStyle:'padding:50px'},
        bodyStyle:'padding:50px',
        bbar:new Ext.Toolbar({
        	items:['工具栏','->',{
        		enableToggle :false,
        		handler : function(){alert('ok')},
        		tooltip :'工具按钮',
        		text:'按钮'
        	}]
        }),
        enableDrag :false,
        enableDD :false,
        border :false,
        
        buttonAlign :'center',
        hideBorders :true,
        rootVisible :true,
        lines :false,
        shim :true,
        titleCollapse : true,
        bodyBorder :false,
        buttons :[{
        		enableToggle :false,
        		handler : function(){alert('ok')},
        		tooltip :'工具按钮',
        		text:'按钮',
        		disabled :true
        	}],
        root:new Tree.TreeNode('根节点')
    });
    return tree;
}