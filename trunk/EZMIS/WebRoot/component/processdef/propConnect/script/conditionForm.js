//由于该js文件放在最前面，所以在此文件中定义model cell 及其相关方法
var model = window.dialogArguments[0] ;
var cell = window.dialogArguments[1] ;
var rootCell = window.dialogArguments[2] ;
var textareaValue = window.dialogArguments[3]==null?"":window.dialogArguments[3] ;
var getLabelValue= function(nodeName,cell){
	var attrs = cell.value.attributes
	for (var i = 0;i < attrs.length; i++) {
		if(attrs[i].nodeName == nodeName){
			return  attrs[i].nodeValue;
		}
	}
}


//系统变量枚举数据
var systemVarData=[	['系统时间','${systemTime}'],
					['系统日期','${systemDate}']];
					
var titlePanel=new Ext.app.TitlePanel({caption:'条件生成器',border:false,region:'north'});

//系统变量
var systemVar = new Ext.ux.Multiselect({width:150,height:150,store : new Ext.data.SimpleStore({data:systemVarData,fields:['sysVarKey','sysVarName']},['sysVarKey']) ,
	displayField : 'sysVarKey',legend : '系统变量',allowDup : true,copy:true,allowTrash:true,appendOnly:false,isFormField: false
}) ;
systemVar.on("dblclick",function(vw, index, node, e) {
	if(!conditionTxt.hasFocus) {
		conditionTxt.focus();
	} 
	document.selection.createRange().text=vw.store.data.items[index].get('sysVarName')
});


//组合流程变量的数据
var fvItems = Ext.decode(getLabelValue('flowVariable',rootCell)) ;
var data = new   Array() ;
for(var i=0 ; i< fvItems.length ; i++) {
	data[i] = new Array() ;
}

for(var i=0 ; i< fvItems.length ; i++) {
	var obj = fvItems[i] ;
	data[i][0] = obj.name_cn ;
	data[i][1] = obj.name ; 
}
//流程变量
var wfVar = new Ext.ux.Multiselect({width:150,height:150,store : new Ext.data.SimpleStore({data:data,fields:['wfVarKey','wfVarName']},['wfVarKey','wfVarName']),
			displayField : 'wfVarKey',legend : '流程变量',allowDup : true,copy:true,allowTrash:true,appendOnly:false,isFormField: false
});
wfVar.on("dblclick",function(vw, index, node, e) {
	if(!conditionTxt.hasFocus) {
		conditionTxt.focus();
	} 
	document.selection.createRange().text=vw.store.data.items[index].get('wfVarName')
})
		
//函数		
var funVar = new Ext.ux.Multiselect ({width:150,height:150,store : new Ext.data.SimpleStore({ data:[['func1','aaa(VAR1,VAR2)'],['func2','bbb(VAR1,VAR2)']],fields:['funKey','funName']},['funKey']) ,
			displayField : 'funKey',legend : '函数',allowDup : true,copy:true,allowTrash:true,appendOnly:false,isFormField: false
}) ;
funVar.on("dblclick",function(vw, index, node, e) {
	if(!conditionTxt.hasFocus) {
		conditionTxt.focus();
	} 
	document.selection.createRange().text=vw.store.data.items[index].get('funName')
})

var titleCondition = {xtype:'panel' ,border:false,width:480,height:25, html:'条件:'} ;
var conditionTxt = new Ext.form.TextArea({width : 485 ,height : 100 ,value : textareaValue})	



var formPanel = new Ext.Panel({
	autoWidth:true,
	autoHeight:true,
	frame:true,
	buttons:[{
		text:'确定',
		hidden : false ,
		handler : function() {
			window.returnValue = {condition:conditionTxt.getValue()};
			window.close();
		}
	},{
		text : '取消' ,
		hidden : false ,
		handler : function() {
			window.close() ;	    			
		}
	}],
	layout : 'column',
	border:false,
	items : [{
		columnWidth : .33,
		border : false ,
		items:[systemVar]		
	},{
		columnWidth : .33,
		border : false ,
		items:[wfVar]		
	},{
		columnWidth : .33,
		border : false ,
		items:[funVar]
	},{
		border : false ,
		columnWidth : 1,
		items:titleCondition
	},{
		border : false ,
		columnWidth : 1,
		items : conditionTxt
	}]
});

var lyCenter = {
	id : 'center-panel',
	region : 'center',
	items:[formPanel]
}

/**
 * 初始化
 */
function onload(){
	conditionTxt.focus();
}

