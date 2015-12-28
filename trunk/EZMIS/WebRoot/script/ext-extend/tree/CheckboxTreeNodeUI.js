/**
 * author:sds
 * checkbox Tree Node UI
 * 供Ext.app.CheckboxTreeNode使用的NodeUI
 */
Ext.app.CheckboxTreeNodeUI=function (config){
	//调用父类构造函数
	Ext.app.CheckboxTreeNodeUI.superclass.constructor.call(this,config);
	
	
}
Ext.extend(Ext.app.CheckboxTreeNodeUI,Ext.tree.TreeNodeUI,{
	/**
	 * 覆盖父类的方法，
	 * 由于在某些场合需要按住ctrl键才进行级联check
	 * 但是由于TreeNodeUI类未将事件对象传入checkchange事件的回调函数，导致
	 * 事件函数调用不到ctrl是否按下，所以重写父类的该方法，并将EventObject传入
	 * 事件函数中去
	 */
    onCheckChange : function(){
      	var checked = this.checkbox.checked;
        this.node.attributes.checked = checked;
        this.fireEvent('checkchange', this.node, checked,arguments[0]);
        }
});
