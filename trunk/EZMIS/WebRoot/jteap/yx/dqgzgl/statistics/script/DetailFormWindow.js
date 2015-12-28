
var DetailFormWindow=function(dqgzzy){
	
	Ext.form.Field.prototype.msgTarget = 'side';
	
	var formWindow=this;
	
	DetailFormWindow.superclass.constructor.call(this,{
        title: dqgzzy+'专业详细统计报表',
        width: 700,
        height:400,
        modal:true,
        layout: 'column',
        plain:true,
        draggable :false,
        resizable :false,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        items: [{
	        	border:false,
	        	columnWidth:1,
	        	layout:'form',
	        	items:detailGrid
	        }]
    });
	
}

Ext.extend(DetailFormWindow, Ext.Window, {
	
});