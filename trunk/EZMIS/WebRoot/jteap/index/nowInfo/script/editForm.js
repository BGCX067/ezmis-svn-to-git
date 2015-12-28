
var jzStore = new Ext.data.SimpleStore({
	fields: ['key','value'],
	data: [
			['连续运行','连续运行'],
			['连续停行','连续停运']
		]
});

//#1机组状态
var comboStatus1 = new Ext.form.ComboBox({
	id: 'comboStatus1',
    renderTo: 'divStatus1',
    store: jzStore,
	valueField: "value",
	displayField: "key",
	mode: 'local',
	triggerAction: 'all',
	blankText: '请选择状态',
	emptyText: '请选择状态',
	width: 150,
	editable: false,
	forceSelection: true,
	allowBlank: false
});
//#1连续天数
var txtRunDay1 = new Ext.form.NumberField({
	id: 'txtRunDay1',
	renderTo: 'divRunDay1',
	width: 120,
	allowBlank: false
});
//#1备查天数
var txtBcDay1 = new Ext.form.TextField({
	id: 'txtBcDay1',
	renderTo: 'divBcDay1',
	width: 120,
	allowBlank: false
});

//#2机组状态
var comboStatus2 = new Ext.form.ComboBox({
	id: 'comboStatus2',
    renderTo: 'divStatus2',
    store: jzStore,
	valueField: "value",
	displayField: "key",
	mode: 'local',
	triggerAction: 'all',
	blankText: '请选择状态',
	emptyText: '请选择状态',
	width: 150,
	editable: false,
	forceSelection: true,
	allowBlank: false
});
//#2连续天数
var txtRunDay2 = new Ext.form.NumberField({
	id: 'txtRunDay2',
	renderTo: 'divRunDay2',
	width: 120,
	allowBlank: false
});
//#2备查天数
var txtBcDay2 = new Ext.form.TextField({
	id: 'txtBcDay2',
	renderTo: 'divBcDay2',
	width: 120,
	allowBlank: false
});
//#3机组状态
var comboStatus3 = new Ext.form.ComboBox({
	id: 'comboStatus3',
    renderTo: 'divStatus3',
    store: jzStore,
	valueField: "value",
	displayField: "key",
	mode: 'local',
	triggerAction: 'all',
	blankText: '请选择状态',
	emptyText: '请选择状态',
	width: 150,
	editable: false,
	forceSelection: true,
	allowBlank: false
});
//#3连续天数
var txtRunDay3 = new Ext.form.NumberField({
	id: 'txtRunDay3',
	renderTo: 'divRunDay3',
	width: 120,
	allowBlank: false
});
//#3备查天数
var txtBcDay3 = new Ext.form.TextField({
	id: 'txtBcDay3',
	renderTo: 'divBcDay3',
	width: 120,
	allowBlank: false
});
//#4机组状态
var comboStatus4 = new Ext.form.ComboBox({
	id: 'comboStatus4',
    renderTo: 'divStatus4',
    store: jzStore,
	valueField: "value",
	displayField: "key",
	mode: 'local',
	triggerAction: 'all',
	blankText: '请选择状态',
	emptyText: '请选择状态',
	width: 150,
	editable: false,
	forceSelection: true,
	allowBlank: false
});
//#4连续天数
var txtRunDay4 = new Ext.form.NumberField({
	id: 'txtRunDay4',
	renderTo: 'divRunDay4',
	width: 120,
	allowBlank: false
});
//#4备查天数
var txtBcDay4 = new Ext.form.TextField({
	id: 'txtBcDay4',
	renderTo: 'divBcDay4',
	width: 120,
	allowBlank: false
});
function getStatus(){
	var url = contextPath + "/jteap/index/NowInfoAction!getJzStatusAction.do";
	Ext.Ajax.request({
		url: url,
		method: 'post',
		success: function(ajax){
			eval("responseObj=" + ajax.responseText);
			if(responseObj.success == true){
				var list = responseObj.list;
				for(var i=0; i<list.length; i++){
					var jizu = list[i].JIZU;
					eval("comboStatus"+jizu+".setValue(list[i].STATUS)");
					eval("txtRunDay"+jizu+".setValue(list[i].RUN_DAY)");
					eval("txtBcDay"+jizu+".setValue(list[i].ZT_BC)");
				}
			}
		},
		failure: function(){
			alert('服务器忙,请稍后再试...');
		}
	})
}
getStatus();

//保存
function save(){
	/** 数据验证 */
	if(!comboStatus1.validate()){
		alert("请选择#1机组状态");
		comboStatus1.focus();
		return;
	}
	if(!txtRunDay1.validate()){
		alert("请输入#1机组连续天数");
		txtRunDay1.setValue("");
		txtRunDay1.focus();
		return;
	}
	if(!comboStatus2.validate()){
		alert("请选择#2机组状态");
		comboStatus2.focus();
		return;
	}
	if(!txtRunDay2.validate()){
		alert("请输入#2机组连续天数");
		txtRunDay2.setValue("");
		txtRunDay2.focus();
		return;
	}
	if(!comboStatus3.validate()){
		alert("请选择#1机组状态");
		comboStatus3.focus();
		return;
	}
	if(!txtRunDay3.validate()){
		alert("请输入#3机组连续天数");
		txtRunDay3.setValue("");
		txtRunDay3.focus();
		return;
	}
	if(!comboStatus4.validate()){
		alert("请选择#4机组状态");
		comboStatus4.focus();
		return;
	}
	if(!txtRunDay4.validate()){
		alert("请输入#4机组连续天数");
		txtRunDay4.setValue("");
		txtRunDay4.focus();
		return;
	}
	
	var url = contextPath + "/jteap/index/NowInfoAction!setJzStatusAction.do";
	/** 保存 */	
	Ext.Ajax.request({
		url: url,
		method: 'post',
		params: {status1:comboStatus1.getValue(), runDay1:txtRunDay1.getValue(),
				status2:comboStatus2.getValue(), runDay2:txtRunDay2.getValue(),
				status3:comboStatus3.getValue(), runDay3:txtRunDay3.getValue(),
				status4:comboStatus4.getValue(), runDay4:txtRunDay4.getValue()},
		success: function(ajax){
			eval("responseObj=" + ajax.responseText);
			if(responseObj.success == true){
				alert('保存成功');
			}else{
				alert('保存失败,请联系管理员...');
			}
		},
		failure: function(){
			alert('服务器忙,请稍后再试...');
		}
	})
}
