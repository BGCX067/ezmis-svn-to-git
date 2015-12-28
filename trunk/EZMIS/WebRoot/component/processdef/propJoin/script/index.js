var titlePanel=new Ext.app.TitlePanel({caption:'修改会签名称',border:false,region:'north'});

var joinField = {
		xtype : 'textfield',
        fieldLabel: '会签名称',
        blankText : '请输入会签名称' ,
        name: 'join',
        value : getLabelValue('label'),
        allowBlank:false,
        anchor : '100%'
	}

var lyCenter = new Ext.FormPanel({
	frame:false,
	bodyStyle:'padding:50px 50px',
	region : 'center',
	border : true,
	labelWidth : 60,
	items : [joinField],
	buttons : [{
    		text:'确定',
    		handler : function(){
    			submitForm();
    		}
    	},{
    		text : '取消' ,
    		handler : function() {
				window.close() ;	    			
    		}
    	}
    ]
})

function submitForm(){
	var value = model.getValue(cell);
	model.beginUpdate();
	try {
		var label = new mxCellAttributeChange(cell,"label",Ext.get('join').getValue());
		var name = new mxCellAttributeChange(cell,"name",Ext.get('join').getValue());
		model.execute(label);
		model.execute(name);
	} finally {
		model.endUpdate();		
		window.close() ;
	}
}
