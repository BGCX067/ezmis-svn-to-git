var LeftMenu = function() {
	var menu=this;
	LeftMenu.superclass.constructor.call(this, {
			title : '&nbsp;',
			collapsible : true,
			region : 'west',
			style  :'padding:2px',
			frame : false,
			autoScroll :false,
			name:'table',
			width : 250,
			height:'100%',
			xtype : 'panel',
			items : {
				html : '<iframe width="100%" height="1000" frameBorder=0 marginwidth="0" marginheight="0" name="leftIframe" src="zyzbIndex.jsp"></iframe>'
			}
		});

}

Ext.extend(LeftMenu, Ext.Panel, {});
