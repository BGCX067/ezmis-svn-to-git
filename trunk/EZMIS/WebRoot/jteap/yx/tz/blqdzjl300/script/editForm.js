
//检查人
var txtcbr = new Ext.form.TextField({
	id: 'txtcbr',
	renderTo: 'divcbr',
	maxLength: 30,
	width: 120,
	maxLengthText: '最长15个字符',
	allowBlank: false,
	readOnly: isReadOnly
});
//检查时间
var txtcbsj = new Ext.form.DateField({
	id: 'txtcbsj',
	renderTo: 'divcbsj',
	format: 'Y-m-d',
	maxValue: new Date().format("Y-m-d"),
	width: 120,
	allowBlank : false,
	disabled: isReadOnly
});

//#1主变高压侧A相
var txtzbgyc_a1 = new Ext.form.NumberField({
	id: 'txtzbgyc_a1',
	renderTo: 'divzbgyc_a1',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#1主变高压侧B相
var txtzbgyc_b1 = new Ext.form.NumberField({
	id: 'txtzbgyc_b1',
	renderTo: 'divzbgyc_b1',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#1主变高压侧C相
var txtzbgyc_c1 = new Ext.form.NumberField({
	id: 'txtzbgyc_c1',
	renderTo: 'divzbgyc_c1',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#1主变高压侧中性点
var txtzbgyczxd_1 = new Ext.form.NumberField({
	id: 'txtzbgyczxd_1',
	renderTo: 'divzbgyczxd_1',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#2主变高压侧A相
var txtzbgyc_a2 = new Ext.form.NumberField({
	id: 'txtzbgyc_a2',
	renderTo: 'divzbgyc_a2',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#2主变高压侧B相
var txtzbgyc_b2 = new Ext.form.NumberField({
	id: 'txtzbgyc_b2',
	renderTo: 'divzbgyc_b2',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#2主变高压侧C相
var txtzbgyc_c2 = new Ext.form.NumberField({
	id: 'txtzbgyc_c2',
	renderTo: 'divzbgyc_c2',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#2主变高压侧中性点
var txtzbgyczxd_2 = new Ext.form.NumberField({
	id: 'txtzbgyczxd_2',
	renderTo: 'divzbgyczxd_2',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#01启备变高压侧A相
var txtqbbgyc_a01 = new Ext.form.NumberField({
	id: 'txtqbbgyc_a01',
	renderTo: 'divqbbgyc_a01',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#01启备变高压侧B相
var txtqbbgyc_b01 = new Ext.form.NumberField({
	id: 'txtqbbgyc_b01',
	renderTo: 'divqbbgyc_b01',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#01启备变高压侧C相
var txtqbbgyc_c01 = new Ext.form.NumberField({
	id: 'txtqbbgyc_c01',
	renderTo: 'divqbbgyc_c01',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//220KV #1母线A相
var txtmx_a1 = new Ext.form.NumberField({
	id: 'txtmx_a1',
	renderTo: 'divmx_a1',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//220KV #1母线B相
var txtmx_b1 = new Ext.form.NumberField({
	id: 'txtmx_b1',
	renderTo: 'divmx_b1',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//220KV #1母线C相
var txtmx_c1 = new Ext.form.NumberField({
	id: 'txtmx_c1',
	renderTo: 'divmx_c1',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//220KV #2母线A相
var txtmx_a2 = new Ext.form.NumberField({
	id: 'txtmx_a2',
	renderTo: 'divmx_a2',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//220KV #2母线B相
var txtmx_b2 = new Ext.form.NumberField({
	id: 'txtmx_b2',
	renderTo: 'divmx_b2',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//220KV #2母线C相
var txtmx_c2 = new Ext.form.NumberField({
	id: 'txtmx_c2',
	renderTo: 'divmx_c2',
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
	txtcbr.setValue(select.data.cbr);
	txtcbsj.setValue(select.data.cbsj);
	txtzbgyc_a1.setValue(select.data.zbgyc_a1);
	txtzbgyc_b1.setValue(select.data.zbgyc_b1);
	txtzbgyc_c1.setValue(select.data.zbgyc_c1);
	txtzbgyczxd_1.setValue(select.data.zbgyczxd_1);
	txtzbgyc_a2.setValue(select.data.zbgyc_a2);
	txtzbgyc_b2.setValue(select.data.zbgyc_b2);
	txtzbgyc_c2.setValue(select.data.zbgyc_c2);
	txtzbgyczxd_2.setValue(select.data.zbgyczxd_2);
	txtqbbgyc_a01.setValue(select.data.qbbgyc_a01);
	txtqbbgyc_b01.setValue(select.data.qbbgyc_b01);
	txtqbbgyc_c01.setValue(select.data.qbbgyc_c01);
	txtmx_a1.setValue(select.data.mx_a1);
	txtmx_b1.setValue(select.data.mx_b1);
	txtmx_c1.setValue(select.data.mx_c1);
	txtmx_a2.setValue(select.data.mx_a2);
	txtmx_b2.setValue(select.data.mx_b2);
	txtmx_c2.setValue(select.data.mx_c2);
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
			alert("请输入检查人");
		}
		txtcbr.focus();
		return;
	}
	if(!txtcbsj.validate()){
		alert("请选择检查时间");
		txtcbsj.setValue("");
		txtcbsj.focus();
		return;
	}
	
	/** 保存 */	
	Ext.Ajax.request({
		url: link3,
		method: 'post',
		params: {id:id,cbr:txtcbr.getValue(),cbsj:txtcbsj.getValue().format("Y-m-d"),
				tianXieShiJian:tianXieShiJian,tianXieRen:tianXieRen,
				zbgyc_a1:txtzbgyc_a1.getValue(),zbgyc_b1:txtzbgyc_b1.getValue(),zbgyc_c1:txtzbgyc_c1.getValue(),zbgyczxd_1:txtzbgyczxd_1.getValue(),
				zbgyc_a2:txtzbgyc_a2.getValue(),zbgyc_b2:txtzbgyc_b2.getValue(),zbgyc_c2:txtzbgyc_c2.getValue(),zbgyczxd_2:txtzbgyczxd_2.getValue(),
				qbbgyc_a01:txtqbbgyc_a01.getValue(),qbbgyc_b01:txtqbbgyc_b01.getValue(),qbbgyc_c01:txtqbbgyc_c01.getValue(),mx_a1:txtmx_a1.getValue(),
				mx_b1:txtmx_b1.getValue(),mx_c1:txtmx_c1.getValue(),mx_a2:txtmx_a2.getValue(),mx_b2:txtmx_b2.getValue(),mx_c2:txtmx_c2.getValue()
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
