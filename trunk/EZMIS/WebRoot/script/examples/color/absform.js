Ext.onReady(function() {
    var form = new Ext.form.FormPanel({
        baseCls: 'x-plain',
        layout:'absolute',
        url:'save-form.php',
        defaultType: 'textfield',

        items: [{
            x: 60,
            y: 0,
            name: 'to',
            anchor:'100%'  // anchor width by percentage
        },{
            x: 0,
            y: 35,
            xtype:'label',
            text: 'Subject:'
        },{
            x: 60,
            y: 30,
            name: 'subject',
            anchor: '100%'  // anchor width by percentage
        },{ x: 60,
            y: 60,
            id:'ssss',
        	xtype:'combocolor',
        	label:'x',
        	value:'#FFFFFF',
        	name:'xx'
        }]
    });

    var window = new Ext.Window({
        title: 'Resize Me',
        width: 500,
        height:300,
        minWidth: 300,
        minHeight: 200,
        layout: 'fit',
        plain:true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        items: form,
		
        buttons: [{
            text: 'Send',handler:function(){
            var xx = Ext.getCmp('ssss').getValue();
            alert(xx);
            }
        },{
            text: 'Cancel'
        }]
    });

    window.show();
});