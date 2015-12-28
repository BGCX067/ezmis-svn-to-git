
//值别
var dict_zbzb=$dictList("zbzb");
var storezb =  new Ext.data.Store({
	data: {rows:dict_zbzb},
	reader: new Ext.data.JsonReader( {
		root: 'rows',
		id: 'id'
	}, ['key', 'value', 'id'])
});
var combozb = new Ext.form.ComboBox({
	id: 'combozb',
    renderTo: 'divzb',
    store: storezb,
	valueField: "value",
	displayField: "key",
	mode: 'local',
	triggerAction: 'all',
	blankText: '请选择',
	emptyText: '请选择',
	width: 120,
	editable: false,
	forceSelection: true,
	allowBlank: false,
	disabled: isReadOnly
});
//抄表人
var txtcbr = new Ext.form.TextField({
	id: 'txtcbr',
	renderTo: 'divcbr',
	maxLength: 30,
	width: 120,
	maxLengthText: '最长15个字符',
	allowBlank: false,
	readOnly: isReadOnly
});
//抄表时间
var txtcbsj = new Ext.form.DateField({
	id: 'txtcbsj',
	renderTo: 'divcbsj',
	format: 'Y-m-d H:00',
	maxValue: new Date().format("Y-m-d 24:00"),
	menu: new DatetimeMenu(),
	width: 130,
	allowBlank: false,
	disabled: isReadOnly
});
//#3发电机有功 表码
var txtfdjyg_3 = new Ext.form.NumberField({
	id: 'txtfdjyg_3',
	renderTo: 'divfdjyg_3',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#3高厂变 表码
var txtgcb_3 = new Ext.form.NumberField({
	id: 'txtgcb_3',
	renderTo: 'divgcb_3',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#4发电机有功 表码
var txtfdjyg_4 = new Ext.form.NumberField({
	id: 'txtfdjyg_4',
	renderTo: 'divfdjyg_4',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#4高厂变 表码
var txtgcb_4 = new Ext.form.NumberField({
	id: 'txtgcb_4',
	renderTo: 'divgcb_4',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#3励磁变 表码
var txtlcb_3 = new Ext.form.NumberField({
	id: 'txtlcb_3',
	renderTo: 'divlcb_3',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#4励磁变 表码
var txtlcb_4 = new Ext.form.NumberField({
	id: 'txtlcb_4',
	renderTo: 'divlcb_4',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#02启备变 表码
var txtqbb_2 = new Ext.form.NumberField({
	id: 'txtqbb_2',
	renderTo: 'divqbb_2',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});

var select = window.dialogArguments.select;
var tianXieShiJian = new Date().format("Y-m-d H");
var tianXieRen = curPersonName;

if(select != null){
	var id = select.data.id;
	tianXieShiJian = select.data.tianXieShiJian;
	tianXieRen = select.data.tianXieRen;
	combozb.setValue(select.data.zb);
	txtcbr.setValue(select.data.cbr);
	txtcbsj.setValue(select.data.cbsj);
	txtfdjyg_3.setValue(select.data.fdjyg_3);
	txtgcb_3.setValue(select.data.gcb_3);
	txtfdjyg_4.setValue(select.data.fdjyg_4);
	txtgcb_4.setValue(select.data.gcb_4);
	txtlcb_3.setValue(select.data.lcb_3);
	txtlcb_4.setValue(select.data.lcb_4);
	txtqbb_2.setValue(select.data.qbb_2);
}
txtcbr.focus();

//保存
function save(jx){
	/** 数据验证 */
	if(!txtcbr.validate()){
		if(txtcbr.getValue().length > 30){
			alert("请输入15个字符以内的名字");
			txtcbr.setValue("");
		}else{
			alert("请输入抄表人");
		}
		txtcbr.focus();
		return;
	}
	if(!txtcbsj.validate()){
		alert("请选择抄表时间");
		txtcbsj.setValue("");
		txtcbsj.focus();
		return;
	}
	if(!combozb.validate()){
		alert("请选择值别");
		combozb.focus();
		return;
	}
		
	/** 保存 */	
	Ext.Ajax.request({
		url: link3,
		method: 'post',
		params: {id:id,zb:combozb.getValue(),cbr:txtcbr.getValue(),cbsj:txtcbsj.getValue().format("Y-m-d H:00"),
				tianXieShiJian:tianXieShiJian,tianXieRen:tianXieRen,
				fdjyg_3:txtfdjyg_3.getValue(),gcb_3:txtgcb_3.getValue(),fdjyg_4:txtfdjyg_4.getValue(),gcb_4:txtgcb_4.getValue(),
				qbb_2:txtqbb_2.getValue(),lcb_3:txtlcb_3.getValue(),lcb_4:txtlcb_4.getValue()},
		success: function(ajax){
			eval("responseObj=" + ajax.responseText);
			if(responseObj.success == true){
				alert('保存成功');
				if(jx == null){
					window.close();
				}else{
					window.location = "editForm.jsp";
				}
				if(window.dialogArguments.grid != null){
					window.dialogArguments.grid.getStore().reload();
				}
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
