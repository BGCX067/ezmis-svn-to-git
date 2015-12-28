
/**
 * 由于TreeEditor默认拥有连续单击就显示编辑状态
 * 但是由于在某些场合不需要该功能，因此产生该脚本，
 * 用于覆盖员函数功能
 * 
 */
Ext.app.ExTreeEditor=function(tree, config){
	Ext.app.ExTreeEditor.superclass.constructor.call(this,tree, config);
}

Ext.extend(Ext.app.ExTreeEditor,Ext.tree.TreeEditor, {
	bClickEdit	:	false,//默认情况下，连续点击是不会出现编辑器的
	
	/**
	 * 可设置是否支持连续点击会出现编辑器
	 * @bEdit	:	true	出现
	 * 				false 	不出现
	 */
	setClickEdit:	function(bEdit){
		this.bClickEdit=bEdit;
	},
    beforeNodeClick : function(node, e){
//		//调用父类的初始化方法
		if(this.bClickEdit)
			Ext.app.ExTreeEditor.superclass.beforeNodeClick.call(this,node,e);
    }
});