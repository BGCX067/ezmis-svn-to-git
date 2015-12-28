
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
	menu: new DatetimeMenu(),
	width: 130,
	maxValue: new Date().format("Y-m-d 24:00"),
	allowBlank: false,
	disabled: isReadOnly
});
//#01主变(峰值) 表码
var txtzbf_1 = new Ext.form.NumberField({
	id: 'txtzbf_1',
	renderTo: 'divzbf_1',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#01主变(平值) 表码
var txtzbp_1 = new Ext.form.NumberField({
	id: 'txtzbp_1',
	renderTo: 'divzbp_1',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#01主变(谷值) 表码
var txtzbg_1 = new Ext.form.NumberField({
	id: 'txtzbg_1',
	renderTo: 'divzbg_1',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#02主变(峰值) 表码
var txtzbf_2 = new Ext.form.NumberField({
	id: 'txtzbf_2',
	renderTo: 'divzbf_2',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#02主变(平值) 表码
var txtzbp_2 = new Ext.form.NumberField({
	id: 'txtzbp_2',
	renderTo: 'divzbp_2',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#02主变(谷值) 表码
var txtzbg_2 = new Ext.form.NumberField({
	id: 'txtzbg_2',
	renderTo: 'divzbg_2',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});

//启备变E06（峰）
var txtqbbf = new Ext.form.NumberField({
	id: 'txtqbbf',
	renderTo: 'divqbbf',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//启备变E06（平）
var txtqbbp = new Ext.form.NumberField({
	id: 'txtqbbp',
	renderTo: 'divqbbp',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//启备变E06（谷）
var txtqbbg = new Ext.form.NumberField({
	id: 'txtqbbg',
	renderTo: 'divqbbg',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#1高厂变
var txtgcb_1 = new Ext.form.NumberField({
	id: 'txtgcb_1',
	renderTo: 'divgcb_1',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#2高厂变
var txtgcb_2 = new Ext.form.NumberField({
	id: 'txtgcb_2',
	renderTo: 'divgcb_2',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#1尾水发电
var txtwsfd_1 = new Ext.form.NumberField({
	id: 'txtwsfd_1',
	renderTo: 'divwsfd_1',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#2尾水发电
var txtwsfd_2 = new Ext.form.NumberField({
	id: 'txtwsfd_2',
	renderTo: 'divwsfd_2',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//集控启备变 表码
var txtjkqbb= new Ext.form.NumberField({
	id: 'txtjkqbb',
	renderTo: 'divjkqbb',
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
	txtgcb_1.setValue(select.data.gcb_1);
	txtgcb_2.setValue(select.data.gcb_2);
	txtzbf_1.setValue(select.data.zbf_1);
	txtzbp_1.setValue(select.data.zbp_1);
	txtzbg_1.setValue(select.data.zbg_1);
	txtzbf_2.setValue(select.data.zbf_2);
	txtzbp_2.setValue(select.data.zbp_2);
	txtzbg_2.setValue(select.data.zbg_2);
	txtzbf_1.setValue(select.data.zbf_1);
	txtqbbf.setValue(select.data.qbbf);
	txtqbbp.setValue(select.data.qbbp);
	txtqbbg.setValue(select.data.qbbg);
	txtwsfd_1.setValue(select.data.wsfd_1);
	txtwsfd_2.setValue(select.data.wsfd_2);
	txtjkqbb.setValue(select.data.jkqbb);
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
				zbf_1:txtzbf_1.getValue(),zbp_1:txtzbp_1.getValue(),zbg_1:txtzbg_1.getValue(),gcb_1:txtgcb_1.getValue(),gcb_2:txtgcb_2.getValue(),zbf_2:txtzbf_2.getValue(),zbp_2:txtzbp_2.getValue(),zbg_2:txtzbg_2.getValue(),
				qbbf:txtqbbf.getValue(),qbbp:txtqbbp.getValue(),qbbg:txtqbbg.getValue(),wsfd_1:txtwsfd_1.getValue(),wsfd_2:txtwsfd_2.getValue(),jkqbb:txtjkqbb.getValue()
			},
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
