
var tsflForm = new Ext.form.FormPanel({
	height: 40,
	
	border:false,
	frame:true,
	//layout:'border',
	region:'center',
	//defaults:{width:200,xtype:"textfield"},
	method:"post",
	url:link8,
	 items:[
	      {layout:'form',
	        fieldLabel:"特殊分类",//文本框标题
	        xtype:"textfield",//表单文本框
	        name:"tsfl",
	        id:"tsfl_textfield",
	        width:150
	      },
	      {
	      		layout:'form',
		region:'north',
	        xtype:"textfield",
	        hideLabel:true,
	        hidden :true,
	        name:"ids",
	        id:"ids_textfield"
	      }
	   ]
	  // buttons:[{text:"确定"},{text:"取消",handler:function(){win}}]
});

var dqkcForm = new Ext.form.FormPanel({
	height: 40,
	
	border:false,
	frame:true,
	//layout:'border',
	region:'center',
	//defaults:{width:200,xtype:"textfield"},
	method:"post",
	url:link9,
	 items:[
	      {layout:'form',
	        fieldLabel:"库存量",//文本框标题
	        xtype:"numberfield",//表单文本框
	        name:"dqkc",
	      //  allowDecimals:false,//不允许小数
	        id:"dqkc_textfield",
	        width:150        
	      },
	      {
	      	layout:'form',
	        xtype:"textfield",
	        hideLabel:true,
	        hidden :true,
	        name:"ids",
	        id:"ids_textfield"
	      }
	   ]
});
