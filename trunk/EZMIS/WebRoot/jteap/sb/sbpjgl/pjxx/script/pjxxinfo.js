var flag = false;
if(type == "show" || type == "pj"){
	flag = true;
}

var storeLevel =  new Ext.data.SimpleStore({
				fields: ["retrunValue", "displayText"],
				data: [['一类','一类'],['二类','二类'],['三类','三类'],['未定','未定']]
			});
//当前日期
var year = new Date().getFullYear();
var month = new Date().getMonth()+1;
var day = new Date().getDate();
if(month < 10){
	month = "0" + month;
}else{
	month = month;
}
if(day < 10){
	day = "0" + day;
}else{
	day = day;
}
var currentDate = year+"-"+month+"-"+day;
var sbbm = "";
var scpjrq = "";

//评级分类
var txtPjfl= new Ext.form.TextField({
	id: 'txtPjfl',
	renderTo: 'divPjfl',
	maxLength: 32,
	width: 120,
	value: window.dialogArguments.pjfl,
	maxLengthText: '最长32个字符',
	blankText: '请输入设备评级分类',
	allowBlank: false,
	listeners:{
		"beforerender":function(param){
			if(type == 'pj'  || type == "show"){
				Ext.getCmp('txtPjfl').disabled = true;
			}else{
				Ext.getCmp('txtPjfl').disabled = false;
			}
		}
	}
});


//设备名称
var txtSbmc= new Ext.form.TextField({
	id: 'txtSbmc',
	renderTo: 'divSbmc',
	minValue: 0,
	maxLength: 64,
	width: 120,
	blankText: '请输入设备名称',
	allowBlank: false,
	listeners:{
		"beforerender":function(param){
			if(type == 'pj'  || type == "show"){
				Ext.getCmp('txtSbmc').disabled = true;
			}else{
				Ext.getCmp('txtSbmc').disabled = false;
			}
		},
		"focus":function(){
			var obj = {};
			var url = contextPath + "/jteap/sb/common/index.jsp";
			var result = showIFModule(url,"设备名称选择","true",800,600,obj);
			if(typeof(result) == "undefined"){
				sbbm = "";
				txtSbmc.setValue("");
				txtSbgg.setValue("");
			}else{
				sbbm = result.sbbm;
				txtSbmc.setValue(result.sbmc);
				txtSbgg.setValue(result.sbgg);
			}
			txtSbmc.blur();
		}
	}
});

//设备规格
var txtSbgg= new Ext.form.TextField({
	id: 'txtSbgg',
	renderTo: 'divSbgg',
	maxLength: 500,
	maxLength: 64,
	width: 120,
	maxLengthText: '最长500个字符',
	blankText: '请输入型式及规范',
	listeners:{
		"beforerender":function(param){
			if(type == 'pj'  || type == "show"){
				Ext.getCmp('txtSbgg').disabled = true;
			}else{
				Ext.getCmp('txtSbgg').disabled = false;
			}
		}
	}
});

//上次评级日期
var txtScpjrq= new Ext.form.DateField({
	id: 'txtScpjrq',
	renderTo: 'divScpjrq',
	width: 120,
	format: 'Y-m-d',
	readOnly:true,
	allowBlank: flag,
	listeners:{
		"beforerender":function(param){
			if(type == 'pj'  || type == "show"){
				Ext.getCmp('txtScpjrq').disabled = true;
			}else{
				Ext.getCmp('txtScpjrq').disabled = false;
			}
		}
	}
});

//上次评级级别
var txtScpjjb= new Ext.form.ComboBox({
	id: 'txtScpjjb',
	renderTo: 'divScpjjb',
	valueField: "retrunValue",
	displayField: "displayText",
	mode: 'local',
	triggerAction: 'all',
	width: 120,
	readOnly:true,
	maxLengthText: '最长5个字符',
	allowBlank: flag,
    store  : storeLevel,
    listeners:{
		"beforerender":function(param){
			if(type == 'pj'  || type == "show"){
				Ext.getCmp('txtScpjjb').disabled = true;
			}else{
				Ext.getCmp('txtScpjjb').disabled = false;
			}
		}
	}
});

//上次评级人
var txtScpjr= new Ext.form.TextField({
	id: 'txtScpjr',
	renderTo: 'divScpjr',
	maxLength: 32,
	width: 120,
	maxLengthText: '最长32个字符',
	allowBlank: flag,
	listeners:{
		"beforerender":function(param){
			if(type == 'pj' || type == "show"){
				Ext.getCmp('txtScpjr').disabled = true;
			}else{
				Ext.getCmp('txtScpjr').disabled = false;
			}
		}
	}
});

//本次评级日期
var txtBcpjrq= new Ext.form.DateField({
	id: 'txtBcpjrq',
	renderTo: 'divBcpjrq',
	format: 'Y-m-d',
	value:currentDate,
	readOnly:true,
	width: 120,
	blankText: '请输入本次评级日期',
	allowBlank: flag,
	listeners:{
		"beforerender":function(param){
			if(type == "show"){
				Ext.getCmp('txtBcpjrq').disabled = true;
			}else{
				Ext.getCmp('txtBcpjrq').disabled = false;
			}
		}
	}
});

//本次评级级别
var txtBcpjjb= new Ext.form.ComboBox({
	id: 'txtBcpjjb',
	renderTo: 'divBcpjjb',
	width: 120,
	valueField: "retrunValue",
	displayField: "displayText",
	mode: 'local',
	triggerAction: 'all',
	readOnly:true,
	blankText: '请选择本次评级级别',
	maxLengthText: '最长5个字符',
	blankText: '请选择本次评级级别',
	allowBlank: flag,
    store  : storeLevel,
    listeners:{
		"beforerender":function(param){
			if(type == "show"){
				Ext.getCmp('txtBcpjjb').disabled = true;
			}else{
				Ext.getCmp('txtBcpjjb').disabled = false;
			}
		}
	}
});

//本次评级人
var txtBcpjr= new Ext.form.TextField({
	id: 'txtBcpjr',
	renderTo: 'divBcpjr',
	maxLength: 32,
	width: 120,
	maxLengthText: '最长32个字符',
	blankText: '请输入本次评级人',
	allowBlank: flag,
	listeners:{
		"beforerender":function(param){
			if(type == "show"){
				Ext.getCmp('txtBcpjr').disabled = true;
			}else{
				Ext.getCmp('txtBcpjr').disabled = false;
			}
		}
	}
});

//备注
var txtRemark= new Ext.form.TextArea({
	id: 'txtRemark',
	renderTo: 'divRemark',
	maxLength: 500,
	height:130,
	width: 560,
	maxLengthText: '最长500个字符',
	listeners:{
		"beforerender":function(param){
			if(type == "show"){
				Ext.getCmp('txtRemark').disabled = true;
			}else{
				Ext.getCmp('txtRemark').disabled = false;
			}
		}
	}
});

var id = window.dialogArguments.id;
var tableId = window.dialogArguments.tableId;

if(id != null){
	//修改时 赋值
	txtPjfl.setValue(window.dialogArguments.pjfl);
	txtSbmc.setValue(window.dialogArguments.sbmc);
	txtSbgg.setValue(window.dialogArguments.sbgg);
	txtScpjrq.setValue((window.dialogArguments.scpjrq==null)?"":formatDate(new Date(window.dialogArguments.scpjrq.time),"yyyy-MM-dd"));
	txtScpjjb.setValue(window.dialogArguments.scpjjb);
	txtScpjr.setValue(window.dialogArguments.scpjr);
	txtBcpjrq.setValue(formatDate(new Date(window.dialogArguments.bcpjrq.time),"yyyy-MM-dd"));
	if(type == 'pj'){
		txtBcpjjb.setValue("");
		txtBcpjr.setValue("");
	}else{
		txtBcpjjb.setValue(window.dialogArguments.bcpjjb);
		txtBcpjr.setValue(window.dialogArguments.bcpjr);
	}
	txtRemark.setValue(window.dialogArguments.remark);
	if(type == "show"){
		txtPjfl.disabled = true;
		txtSbmc.disabled = true;
		txtSbgg.disabled = true;
		txtScpjrq.disabled = true;
		txtScpjjb.disabled = true;
		txtScpjr.disabled = true;
		txtBcpjrq.disabled = true;
		txtBcpjjb.disabled = true;
		txtBcpjr.disabled = true;
		txtRemark.disabled = true;
	}
}

/**
 * 保存
 */
var save = function(){
	/** 数据验证 */
	if(!txtPjfl.validate()){
		alert('请输入正确的设备评级分类!');
		txtPjfl.focus(true);
		return;
	}
	if(!txtSbmc.validate()){
		alert('请输入设备名称!');
		txtSbmc.focus(true);
		return;
	}
	if(!txtSbgg.validate()){
		alert('请输入正确的设备规格!');
		txtSbgg.focus(true);
		return;
	}
	/*
	if(!txtScpjrq.validate()){
		alert('请输入正确的上次评级日期');
		txtScpjrq.focus(true);
		return;
	}
	if(!txtScpjjb.validate()){
		alert('请输入正确的上次评级级别');
		txtScpjjb.focus(true);
		return;
	}
	if(!txtScpjr.validate()){
		alert('请输入正确的上次评级人');
		txtAzdd.focus(true);
		return;
	}
	*/
	if(!txtBcpjrq.validate()){
		alert('请输入正确的本次评级日期!');
		txtBcpjrq.focus(true);
		return;
	}
	if(txtBcpjrq.getValue() < txtScpjrq.getValue()){
		alert("本次评级日期不能早于上次评级日期!");
		txtBcpjrq.focus(true);
		return ;
	}
	if(!txtBcpjjb.validate()){
		alert('请输入正确的本次评级级别!');
		txtBcpjjb.focus(true);
		return;
	}
	if(!txtBcpjr.validate()){
		alert('请输入正确的本次评级人!');
		txtBcpjr.focus(true);
		return;
	}
	
	scpjrq = (txtScpjrq.getValue() == "")?"":txtScpjrq.getValue().format('Y-m-d');
	
	/** 保存 */	
	Ext.Ajax.request({
		url: link6,
		method: 'post',
		params: {id:id, tableId:tableId,
				pjfl:txtPjfl.getValue().trim(),sbbm:(sbbm == "")?window.dialogArguments.sbbm:sbbm,sbmc:txtSbmc.getValue().trim(), 
				sbgg:txtSbgg.getValue().trim(),scpjrq:scpjrq,
				scpjjb:(txtScpjjb.getValue().trim()=="")?"":txtScpjjb.getValue().trim(),scpjr:(txtScpjr.getValue().trim()=="")?"":txtScpjr.getValue().trim(),
				bcpjrq:txtBcpjrq.getValue().format('Y-m-d'),bcpjjb:txtBcpjjb.getValue().trim(),
				bcpjr:txtBcpjr.getValue().trim(),remark:txtRemark.getValue().trim(),
				sbpjCatalogId:sbpjCatalogId},
		success: function(ajax){
			eval("responseObj="+ajax.responseText);
			if(responseObj.success == true){
				alert('保存成功');
				window.close(); 
			}else{
				alert('保存失败');
			}
		},
		failure: function(){
			alert('服务器忙,请稍后再试...');
		}
	})	
}

	
	
	
	
	