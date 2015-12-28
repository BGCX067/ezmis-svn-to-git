//var editConnectFormWindow = new EditConnectFormWindow() ;
var titlePanel=new Ext.app.TitlePanel({caption:'路由属性',border:false,region:'north'});

var connName = {xtype:'textfield' , allowBlank : true , blankText : '请输入路由名称' , fieldLabel : '路由名称' , height :25 , name : 'connName' , width : 250 , value : getLabelValue('connName',cell) }
	
var connDesc = {xtype:'textfield' , allowBlank : true , blankText : '请输入路由描述' , fieldLabel : '路由描述' , height :25 , name : 'connDesc' , width : 250 , value : getLabelValue('connDesc',cell)}
	
var aimTask = {xtype:'textfield' , allowBlank : true , blankText : '请输入目标环节' , fieldLabel : '目标环节' , height :25 , name : 'aimTask' , width : 250 , value : getLabelValue('aimTask',cell) , readOnly : true }

var chkIsUse = new Ext.form.Checkbox({boxLabel:'使用条件判断',labelSeparator:'' ,inputValue : '1' ,checked : getLabelValue('chkIsUse',cell)=="0"?false:true,listeners : {
	"check":function(combox,checked ){
		btnConnMake.setDisabled(!checked) ;
		textarea.setDisabled(!checked) ;
	}
}}) ;
var btnConnMake = new Ext.Button({xtype:'button',text:'条件生成器',disabled :getLabelValue('chkIsUse',cell)=="0"?true:false ,handler:function(){
	var returnValue = window.showModalDialog("conditionForm.jsp",[model,cell,rootCell,textarea.getValue()],'dialogHeight:435px;dialogWidth:530px;dialogLeft:300px;dialogTop:250px;edge:raised;scroll:1;status:0;help:0;resizable:1;');
	if(returnValue!=null)
		textarea.setValue(returnValue.condition);
}});
var chkIsDefault = new Ext.form.Checkbox({boxLabel:'默认路由',labelSeparator:'',inputValue : '1' ,checked : getLabelValue('chkIsDefault',cell)=="0"?false:true}) ;
var textarea = new Ext.form.TextArea({width : 350,height : 100 ,disabled:getLabelValue('chkIsUse',cell)=="0"?true:false,readOnly : false ,value : getLabelValue('connCondition',cell)==null?"":decodeChars(getLabelValue('connCondition',cell),"',\",&,<,>")});
	
var westPanel = {xtype : 'panel' , border : false , html:"路由条件:"} ;


function getTextAreaValue() {
	return textarea.getValue();
}

function getChkIsUseValue(){
	return this.chkIsUse.checked?this.chkIsUse.inputValue:"0"
} 

function getChkIsDefault() {
	return this.chkIsDefault.checked?this.chkIsDefault.inputValue:"0"
}
	
	
var xxPanel = new Ext.Panel({
	width:520,
	height:350,
	layout : 'border' ,
	border : false ,
	items:[
		{
			width:400,
			height:30,
			border : false ,
			region : 'north',
			buttonAlign : 'right',
			layout : 'column',
			items: [
				{
					border : false ,
					columnWidth : .35 ,
					items:[chkIsUse]
				},
				{
					border : false ,
					columnWidth : .35 ,
					items:this.chkIsDefault
				},
				{
					border : false ,
					columnWidth : .3,
					items:btnConnMake
				}
			]
		},
		{
			width:100,
			height:300,
			border : false ,
			region : 'west',
			items:[westPanel]
		},
		{
			region:'center',
			border : false ,
			items:[this.textarea]
		}
	]
});

function submitForm(){
	var value = model.getValue(cell);
	model.beginUpdate();
	try {
		//修改路由名称的xml
		var edit = new mxCellAttributeChange(cell,"connName",Ext.get("connName").getValue());
		model.execute(edit) ;
		
		//修改路由label的xml
		var edit = new mxCellAttributeChange(cell,"label",Ext.get("connName").getValue());
		model.execute(edit) ;
		
		//修改路由描述的xml
		edit = new mxCellAttributeChange(cell,"connDesc",Ext.get("connDesc").getValue()) ;
		model.execute(edit) ;
		
		//修改使用判断条件的xml
		edit = new mxCellAttributeChange(cell,"chkIsUse",getChkIsUseValue()) ;
		model.execute(edit) ;
		
		//修改默认路由的xml
		edit = new mxCellAttributeChange(cell,"chkIsDefault",getChkIsDefault()) ;
		model.execute(edit) ;
		var condition = encodeChars(getTextAreaValue(),"',\",&,<,>");
		//修改流程分类的xml
		edit = new mxCellAttributeChange(cell,"connCondition",condition) ;
		model.execute(edit) ;
		
		var router = {
			name : Ext.get("connName").getValue()==""?null:Ext.get("connName").getValue() ,
			description : Ext.get("connDesc").getValue()==""?null:Ext.get("connDesc").getValue() ,
			is_default_route : getChkIsDefault()=="0"?false:true ,
			condition : getTextAreaValue()==""?null:condition
		}
		router =Ext.encode(router).replace(/"/g,"'");
		//修改router的xml
		edit = new mxCellAttributeChange(cell,"router",router) ;
		model.execute(edit) ;
		
	} finally {
		model.endUpdate();		
		window.close() ;
	}
}

//编辑formPanel	
var formPanel  = new Ext.form.FormPanel( {
	reader : new Ext.data.JsonReader({
			successProperty : 'success',
			root : 'data'
		},[{name:'connName',mapping:'connName'},{name:'connDesc',mapping:'connDesc'},{name:'aimTask',mapping:'aimTask'}]),
	height : 340 , 
	width : 540 ,
	bodyStyle:'padding:20px 10px 0px 20px',
	frame:true, 					//圆角风格
    labelWidth:80,					//标签宽度
    labelAlign: 'left',
    buttonAlign:'right',
    items: [{
		layout: 'column',
		items : [{	
				columnWidth:1 ,
				layout : 'form',
				items : [connName]
			},{
				columnWidth:1 ,
				layout : 'form' ,
				items : [connDesc]
			},{
				columnWidth:1 ,
				layout : 'form' ,
				items : [aimTask]
			},{
					columnWidth:1 ,
					bodyStyle : 'margin-top:10px',
					defaults:{bodyStyle:'padding:2px,margin: 2px 2px 2px 2px'},
					items: [xxPanel]
		}]
	}],
    buttons : [{
    		text:'确定',
    		hidden : false ,
    		handler : submitForm
    	},{
    		text : '取消' ,
    		hidden : false ,
    		handler : function() {
				window.close() ;	    			
    		}
    	}
    ]
}) 



var lyCenter = {
	id : 'center-panel',
	region : 'center',
	items:[formPanel]
}


function onload(){
	
}