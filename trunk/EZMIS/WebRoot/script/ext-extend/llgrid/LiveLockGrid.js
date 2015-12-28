Ext.namespace('Ext.ux.grid.llgrid');

Ext.ux.grid.llgrid.GridPanel = Ext.extend(Ext.ux.grid.livegrid.GridPanel,{
	
});

Ext.ux.grid.llgrid.GridView = function(config){
	 // shorthands for often used parent classes
    this._gridViewSuperclass = Ext.ux.grid.llgrid.GridView.superclass;
    this._gridViewSuperclass.constructor.call(this,config);
   
    this.templates.master = new Ext.Template(
        '<div class="x-grid3" hidefocus="true">' ,
        	'<div class="ext-ux-livegrid-liveScroller">' ,
        		'<div></div>' ,
        	'</div>',
            '<div class="x-grid3-viewport"">',
                '<div class="x-grid3-header"><div class="x-grid3-header-inner"><div class="x-grid3-header-offset">{header}</div></div><div class="x-clear"></div></div>',
                '<div class="x-grid3-scroller" style="overflow-y:hidden !important;"><div class="x-grid3-body">{body}</div><a href="#" class="x-grid3-focus" tabIndex="-1"></a></div>',
            "</div>",
            '<div class="x-grid3-resize-marker">&#160;</div>',
            '<div class="x-grid3-resize-proxy">&#160;</div>',
        "</div>"
    );
		
}

Ext.extend(Ext.ux.grid.llgrid.GridView,Ext.ux.grid.livegrid.GridView,{
	initTemplates : function(){
		Ext.ux.grid.llgrid.GridView.superclass.initTemplates.call(this);
	}
})
