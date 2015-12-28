
//工具栏
var mainToolbar = new Ext.Toolbar({height:26,
items:[
{id:'btnAddCatalog',text:'添加分类',cls: 'x-btn-text-icon',icon:'icon/icon_1.gif',
	listeners: {
		click: function(){
			leftTree.createNode();			
		}
	} 
},
{id:'btnModiCatalog',text:'修改分类',cls: 'x-btn-text-icon',icon:'icon/icon_3.gif',disabled:true,
	listeners: {
		click: function(){
			leftTree.modifyNode();
		}
	}
},
{id:'btnDelCatalog',text:'删除分类',cls: 'x-btn-text-icon',icon:'icon/icon_2.gif',disabled:true,
	listeners: {
		click: function(){
			leftTree.deleteSelectedNode();
		}
	} 
}]

});

var leftTree=new LeftTree();