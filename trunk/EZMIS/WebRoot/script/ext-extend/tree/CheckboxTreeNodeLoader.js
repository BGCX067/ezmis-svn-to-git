/**
 * 用于checkbox tree 的 loader
 * 主要是覆盖了父类创建节点的时候，创建的是普通节点的做法
 */
Ext.app.CheckboxTreeNodeLoader=function (config){
	
	Ext.app.CheckboxTreeNodeLoader.superclass.constructor.call(this,config);
}
Ext.extend(Ext.app.CheckboxTreeNodeLoader,Ext.tree.TreeLoader,{
	/**
    * Override this function for custom TreeNode node implementation
    */
    createNode : function(attr){
        // apply baseAttrs, nice idea Corey!
        if(this.baseAttrs){
            Ext.applyIf(attr, this.baseAttrs);
        }
        if(this.applyLoader !== false){
            attr.loader = this;
        }
        if(typeof attr.uiProvider == 'string'){
           attr.uiProvider = this.uiProviders[attr.uiProvider] || eval(attr.uiProvider);
        }

        return(attr.leaf ?
                        new Ext.app.CheckboxTreeNode(attr) :
                        new Ext.app.CheckboxAsyncTreeNode(attr));
    }
});
