var items = [];	
var items2 = [];	
var items3 = [];	
var dict_zbzb = $dictList("zbzb");

//值次数据源
var storeZbzb = new Ext.data.Store({
	data: {rows:dict_zbzb},
	reader: new Ext.data.JsonReader({
		root: 'rows',
		id: 'id'
	},['key','value','id'])	
});

/** 早班值次 */
//值次1
var comboZao1 = new Ext.form.ComboBox({
	id: 'comboZao1',
    renderTo: 'divZao1',
    store: storeZbzb,
	valueField: "value",
	displayField: "value",
	mode: 'local',
	triggerAction: 'all',
	fieldLabel: '',
	blankText: '请选择值次',
	emptyText: '请选择值次',
	width: 100,
	editable: false,
	forceSelection: true,
	allowBlank: false
});
//值次2
var comboZao2 = new Ext.form.ComboBox({
	id: 'comboZao2',
    renderTo: 'divZao2',
    store: storeZbzb,
	valueField: "value",
	displayField: "value",
	mode: 'local',
	triggerAction: 'all',
	fieldLabel: '',
	blankText: '请选择值次',
	emptyText: '请选择值次',
	width: 100,
	editable: false,
	forceSelection: true,
	allowBlank: false
});
//值次3
var comboZao3 = new Ext.form.ComboBox({
	id: 'comboZao3',
    renderTo: 'divZao3',
    store: storeZbzb,
	valueField: "value",
	displayField: "value",
	mode: 'local',
	triggerAction: 'all',
	fieldLabel: '',
	blankText: '请选择值次',
	emptyText: '请选择值次',
	width: 100,
	editable: false,
	forceSelection: true,
	allowBlank: false
});
//值次4
var comboZao4 = new Ext.form.ComboBox({
	id: 'comboZao4',
    renderTo: 'divZao4',
    store: storeZbzb,
	valueField: "value",
	displayField: "value",
	mode: 'local',
	triggerAction: 'all',
	fieldLabel: '',
	blankText: '请选择值次',
	emptyText: '请选择值次',
	width: 100,
	editable: false,
	forceSelection: true,
	allowBlank: false
});
//值次5
var comboZao5 = new Ext.form.ComboBox({
	id: 'comboZao5',
    renderTo: 'divZao5',
    store: storeZbzb,
	valueField: "value",
	displayField: "value",
	mode: 'local',
	triggerAction: 'all',
	fieldLabel: '',
	blankText: '请选择值次',
	emptyText: '请选择值次',
	width: 100,
	editable: false,
	forceSelection: true,
	allowBlank: false
});
items.push(comboZao1);
items.push(comboZao2);
items.push(comboZao3);
items.push(comboZao4);
items.push(comboZao5);

/** 中班值次 */
//值次1
var comboZhong1 = new Ext.form.ComboBox({
	id: 'comboZhong1',
    renderTo: 'divZhong1',
    store: storeZbzb,
	valueField: "value",
	displayField: "value",
	mode: 'local',
	triggerAction: 'all',
	fieldLabel: '',
	blankText: '请选择值次',
	emptyText: '请选择值次',
	width: 100,
	editable: false,
	forceSelection: true,
	allowBlank: false
});
//值次2
var comboZhong2 = new Ext.form.ComboBox({
	id: 'comboZhong2',
    renderTo: 'divZhong2',
    store: storeZbzb,
	valueField: "value",
	displayField: "value",
	mode: 'local',
	triggerAction: 'all',
	fieldLabel: '',
	blankText: '请选择值次',
	emptyText: '请选择值次',
	width: 100,
	editable: false,
	forceSelection: true,
	allowBlank: false
});
//值次3
var comboZhong3 = new Ext.form.ComboBox({
	id: 'comboZhong3',
    renderTo: 'divZhong3',
    store: storeZbzb,
	valueField: "value",
	displayField: "value",
	mode: 'local',
	triggerAction: 'all',
	fieldLabel: '',
	blankText: '请选择值次',
	emptyText: '请选择值次',
	width: 100,
	editable: false,
	forceSelection: true,
	allowBlank: false
});
//值次4
var comboZhong4 = new Ext.form.ComboBox({
	id: 'comboZhong4',
    renderTo: 'divZhong4',
    store: storeZbzb,
	valueField: "value",
	displayField: "value",
	mode: 'local',
	triggerAction: 'all',
	fieldLabel: '',
	blankText: '请选择值次',
	emptyText: '请选择值次',
	width: 100,
	editable: false,
	forceSelection: true,
	allowBlank: false
});
//值次5
var comboZhong5 = new Ext.form.ComboBox({
	id: 'comboZhong5',
    renderTo: 'divZhong5',
    store: storeZbzb,
	valueField: "value",
	displayField: "value",
	mode: 'local',
	triggerAction: 'all',
	fieldLabel: '',
	blankText: '请选择值次',
	emptyText: '请选择值次',
	width: 100,
	editable: false,
	forceSelection: true,
	allowBlank: false
});
items2.push(comboZhong1);
items2.push(comboZhong2);
items2.push(comboZhong3);
items2.push(comboZhong4);
items2.push(comboZhong5);

/** 夜班 */
//值次1
var comboYe1 = new Ext.form.ComboBox({
	id: 'comboYe1',
    renderTo: 'divYe1',
    store: storeZbzb,
	valueField: "value",
	displayField: "value",
	mode: 'local',
	triggerAction: 'all',
	fieldLabel: '',
	blankText: '请选择值次',
	emptyText: '请选择值次',
	width: 100,
	editable: false,
	forceSelection: true,
	allowBlank: false
});
//值次2
var comboYe2 = new Ext.form.ComboBox({
	id: 'comboYe2',
    renderTo: 'divYe2',
    store: storeZbzb,
	valueField: "value",
	displayField: "value",
	mode: 'local',
	triggerAction: 'all',
	fieldLabel: '',
	blankText: '请选择值次',
	emptyText: '请选择值次',
	width: 100,
	editable: false,
	forceSelection: true,
	allowBlank: false
});
//值次3
var comboYe3 = new Ext.form.ComboBox({
	id: 'comboYe3',
    renderTo: 'divYe3',
    store: storeZbzb,
	valueField: "value",
	displayField: "value",
	mode: 'local',
	triggerAction: 'all',
	fieldLabel: '',
	blankText: '请选择值次',
	emptyText: '请选择值次',
	width: 100,
	editable: false,
	forceSelection: true,
	allowBlank: false
});
//值次4
var comboYe4 = new Ext.form.ComboBox({
	id: 'comboYe4',
    renderTo: 'divYe4',
    store: storeZbzb,
	valueField: "value",
	displayField: "value",
	mode: 'local',
	triggerAction: 'all',
	fieldLabel: '',
	blankText: '请选择值次',
	emptyText: '请选择值次',
	width: 100,
	editable: false,
	forceSelection: true,
	allowBlank: false
});
//值次5
var comboYe5 = new Ext.form.ComboBox({
	id: 'comboYe5',
    renderTo: 'divYe5',
    store: storeZbzb,
	valueField: "value",
	displayField: "value",
	mode: 'local',
	triggerAction: 'all',
	fieldLabel: '',
	blankText: '请选择值次',
	emptyText: '请选择值次',
	width: 100,
	editable: false,
	forceSelection: true,
	allowBlank: false
});
items3.push(comboYe1);
items3.push(comboYe2);
items3.push(comboYe3);
items3.push(comboYe4);
items3.push(comboYe5);

//初始化数据
function initData(){
	var arryBaiBzb = baiBzb.split(",");
	comboZao1.setValue(arryBaiBzb[0]);
	comboZao2.setValue(arryBaiBzb[1]);
	comboZao3.setValue(arryBaiBzb[2]);
	comboZao4.setValue(arryBaiBzb[3]);
	comboZao5.setValue(arryBaiBzb[4]);
	
	var arryZhongBzb = zhongBzb.split(",");
	comboZhong1.setValue(arryZhongBzb[0]);
	comboZhong2.setValue(arryZhongBzb[1]);
	comboZhong3.setValue(arryZhongBzb[2]);
	comboZhong4.setValue(arryZhongBzb[3]);
	comboZhong5.setValue(arryZhongBzb[4]);
	
	var arryYeBzb = yeBzb.split(",");
	comboYe1.setValue(arryYeBzb[0]);
	comboYe2.setValue(arryYeBzb[1]);
	comboYe3.setValue(arryYeBzb[2]);
	comboYe4.setValue(arryYeBzb[3]);
	comboYe5.setValue(arryYeBzb[4]);
}
initData();

/**
 * 保存
 */
function save(){
	var baiResult = "";
	var zhongResult = "";
	var yeResult = "";
	
	items.each(function(f){
		baiResult += f.getValue() + ",";
	});
	items2.each(function(f){
		zhongResult += f.getValue() + ",";
	});
	items3.each(function(f){
		yeResult += f.getValue() + ",";
	});
	
	Ext.Ajax.request({
		url: link1,
		method: 'post',
		params: {baiResult:baiResult, zhongResult:zhongResult, yeResult:yeResult},
		success: function(ajax){
			eval("responseObj=" + ajax.responseText);
			if(responseObj.success == true){
				alert('保存成功');
				window.close();
			}else{
				alert('保存失败,请联系管理员...');
				window.close();
			}
		},
		failure: function(){
			alert('服务器忙,请稍后再试...');
		}
	})	
}
