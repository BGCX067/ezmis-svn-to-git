
var root=new Ext.app.CheckboxAsyncTreeNode({
	id:'rootNode',
	text:'我的快捷',
	loader:new Ext.app.CheckboxTreeNodeLoader({
        dataUrl:link2
    }),
	ccCheck:true,
    expanded :true
});

LeftTree= function(){
	
	var tree = this;
	
	LeftTree.superclass.constructor.call(this, {
        id:'myQuickTree',
        region:'center',
        split:true,
        height: 400,
        width: 300,
        border:false,
        minSize: 180,
        maxSize: 400,
        collapsible: true,
//        style:'padding-left:10px;',
       	margins:'2px 2px 2px 2px',
       	animate:true,
       	ctrlCasecade:true,	//是否只支持 按住ctrl键进行勾选的时候是级联勾选
//        enableDD:true,	禁止拖动
        rootVisible:true,
        lines:true,
        autoScroll:true,
 		root:root,
        collapseFirst:true,
        tbar:['-','<font color="blue">*按住Ctrl键可进行级联选择</font>','-'],
        buttons:[{
        	text:'保存',
        	listeners: {
        		click: function(){
					//展开根节点的所有子节点
        			root.expandChildNodes(true);
        			//取得包含第三状态的节点的被选中节点编号
        			var moduleIds = root.getCheckedIds(true,false);
        			
        			Ext.Ajax.request({
        				url: link3,
        				params:{moduleIds: moduleIds, personId: curPersonId},
        				method: 'post',
        				success: function(ajax){
        					eval("responseObj="+ajax.responseText);
        					if(responseObj.success == true){
        						alert('保存成功');
        						window.close();
        					}
        				},
        				failure: function(){
        					alert('服务器忙,请稍后再试...');
        				}
        			})
        			
        		}
        	}
        },{
        	text:'取消',
        	listeners: {
        		click: function(){
        			window.close();
        		}
        	}
        }]
    });
    
}

Ext.extend(LeftTree, Ext.tree.TreePanel, {
});

var tree = new LeftTree();
var titlePanl = new Ext.app.TitlePanel({caption:'设置我的快捷',border:false});

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

//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [titlePanl]
}
