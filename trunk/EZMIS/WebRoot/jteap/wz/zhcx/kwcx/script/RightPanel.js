//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs=["物资名称#wzmc#textField","助记码#zjm#textField","物资编码#id#textField"];//,"仓库#kw.ckid#comboCkgl"
//查询面板中默认显示的条件，格式同上
var searchDefaultFs=["物资名称#wzmc#textField","助记码#zjm#textField","物资编码#id#textField"];//,"仓库#kw.ckid#comboCkgl"
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchDefaultFs});


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
								var model = rightGrid.getColumnModel();
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
								var model = rightGrid.getColumnModel();
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
								var model = rightGrid.getColumnModel();
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
	rightGrid]
});
