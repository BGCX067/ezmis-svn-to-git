

var radiogroup= new Ext.form.RadioGroup({   
   	fieldLabel : '显示模式',
   	width: 400,
    items : [{   
                boxLabel : '基本信息',   
                inputValue : "1",   
                name : "radiog",   
                checked : true,
                listeners: {
					render: function(){
						this.on('check',function(radio,isChecked){
							if (isChecked){
								var model = xqRightGrid.getColumnModel();
								for(var i=1;i<model.getColumnCount();i++){
									model.setHidden(i,true);
								}
								for(var i=0;i<columns1.length;i++){
									model.setHidden(columns1[i],false);
								}						
							}
						});
					}
                }
            }, {   
                boxLabel : '库存信息', 
                name : "radiog",   
                inputValue : "2",
                listeners: {
					render: function(){
						this.on('check',function(radio,isChecked){
							if (isChecked){
								var model = xqRightGrid.getColumnModel();
								for(var i=1;i<model.getColumnCount();i++){
									model.setHidden(i,true);
								}
								for(var i=0;i<columns2.length;i++){
									model.setHidden(columns2[i],false);
								}
							}
						});
					}
                }
            }, {   
                boxLabel : '其他信息', 
                name : "radiog",   
                inputValue : "3",
                 listeners: {
					render: function(){
						this.on('check',function(radio,isChecked){
							if (isChecked){
								var model = xqRightGrid.getColumnModel();
								for(var i=1;i<model.getColumnCount();i++){
									model.setHidden(i,true);
								}
								for(var i=0;i<columns3.length;i++){
									model.setHidden(columns3[i],false);
								}
							}
						});
					}
                }
            }]
});
var rightPanel = new Ext.form.FormPanel({
	height: 480,
	layout:'border',
	region:'center',
	border:false,
	items :[
		{
		layout:'form',
		region:'north',
		height:35,
		border:false,
		frame:true,
		items:[radiogroup]
	},
	xqRightGrid]
});
