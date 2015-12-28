
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

//#3主变高压侧A相
var txtzbgycA3 = new Ext.form.NumberField({
	id: 'txtzbgycA3',
	renderTo: 'divzbgycA3',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#3主变高压侧B相
var txtzbgycB3 = new Ext.form.NumberField({
	id: 'txtzbgycB3',
	renderTo: 'divzbgycB3',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#3主变高压侧C相
var txtzbgycC3 = new Ext.form.NumberField({
	id: 'txtzbgycC3',
	renderTo: 'divzbgycC3',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#3主变高压侧中性点
var txtzbgyczx3 = new Ext.form.NumberField({
	id: 'txtzbgyczx3',
	renderTo: 'divzbgyczx3',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});

//#4主变高压侧A相
var txtzbgycA4 = new Ext.form.NumberField({
	id: 'txtzbgycA4',
	renderTo: 'divzbgycA4',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#4主变高压侧B相
var txtzbgycB4 = new Ext.form.NumberField({
	id: 'txtzbgycB4',
	renderTo: 'divzbgycB4',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#4主变高压侧C相
var txtzbgycC4 = new Ext.form.NumberField({
	id: 'txtzbgycC4',
	renderTo: 'divzbgycC4',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#4主变高压侧中性点
var txtzbgyczx4 = new Ext.form.NumberField({
	id: 'txtzbgyczx4',
	renderTo: 'divzbgyczx4',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});

//#02启备变高压侧A相
var txtqbbgycA02 = new Ext.form.NumberField({
	id: 'txtqbbgycA02',
	renderTo: 'divqbbgycA02',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#02启备变高压侧B相
var txtqbbgycB02 = new Ext.form.NumberField({
	id: 'txtqbbgycB02',
	renderTo: 'divqbbgycB02',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//#02启备变高压侧C相
var txtqbbgycC02 = new Ext.form.NumberField({
	id: 'txtqbbgycC02',
	renderTo: 'divqbbgycC02',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});

//220KV #4母线A相
var txtmxA4 = new Ext.form.NumberField({
	id: 'txtmxA4',
	renderTo: 'divmxA4',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//220KV #4母线B相
var txtmxB4 = new Ext.form.NumberField({
	id: 'txtmxB4',
	renderTo: 'divmxB4',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//220KV #4母线C相
var txtmxC4 = new Ext.form.NumberField({
	id: 'txtmxC4',
	renderTo: 'divmxC4',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});

//220KV #5母线A相
var txtmxA5 = new Ext.form.NumberField({
	id: 'txtmxA5',
	renderTo: 'divmxA5',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//220KV #5母线B相
var txtmxB5 = new Ext.form.NumberField({
	id: 'txtmxB5',
	renderTo: 'divmxB5',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//220KV #5母线C相
var txtmxC5 = new Ext.form.NumberField({
	id: 'txtmxC5',
	renderTo: 'divmxC5',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});

//220KV 鄂光线A相
var txtegxA = new Ext.form.NumberField({
	id: 'txtegxA',
	renderTo: 'divegxA',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//220KV 鄂光线B相
var txtegxB = new Ext.form.NumberField({
	id: 'txtegxB',
	renderTo: 'divegxB',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//220KV 鄂光线C相
var txtegxC = new Ext.form.NumberField({
	id: 'txtegxC',
	renderTo: 'divegxC',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});

//220KV 鄂凤线A相
var txtefxA = new Ext.form.NumberField({
	id: 'txtefxA',
	renderTo: 'divefxA',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//220KV 鄂凤线B相
var txtefxB = new Ext.form.NumberField({
	id: 'txtefxB',
	renderTo: 'divefxB',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//220KV 鄂凤线C相
var txtefxC = new Ext.form.NumberField({
	id: 'txtefxC',
	renderTo: 'divefxC',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});

//220KV 杜山一回A相
var txttsyhA = new Ext.form.NumberField({
	id: 'txttsyhA',
	renderTo: 'divtsyhA',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//220KV 杜山一回B相
var txttsyhB = new Ext.form.NumberField({
	id: 'txttsyhB',
	renderTo: 'divtsyhB',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//220KV 杜山一回C相
var txttsyhC = new Ext.form.NumberField({
	id: 'txttsyhC',
	renderTo: 'divtsyhC',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});

//220KV 杜山二回A相
var txttsehA = new Ext.form.NumberField({
	id: 'txttsehA',
	renderTo: 'divtsehA',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//220KV 杜山二回B相
var txttsehB = new Ext.form.NumberField({
	id: 'txttsehB',
	renderTo: 'divtsehB',
	width: 120,
	allowBlank: true,
	readOnly: isReadOnly
});
//220KV 杜山二回C相
var txttsehC = new Ext.form.NumberField({
	id: 'txttsehC',
	renderTo: 'divtsehC',
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
	txtzbgycA3.setValue(select.data.zbgycA3);
	txtzbgycB3.setValue(select.data.zbgycB3);
	txtzbgycC3.setValue(select.data.zbgycC3);
	txtzbgyczx3.setValue(select.data.zbgyczx3);
	txtzbgycA4.setValue(select.data.zbgycA4);
	txtzbgycB4.setValue(select.data.zbgycB4);
	txtzbgycC4.setValue(select.data.zbgycC4);
	txtzbgyczx4.setValue(select.data.zbgyczx4);
	txtqbbgycA02.setValue(select.data.qbbgycA02);
	txtqbbgycB02.setValue(select.data.qbbgycB02);
	txtqbbgycC02.setValue(select.data.qbbgycC02);
	txtmxA4.setValue(select.data.mxA4);
	txtmxB4.setValue(select.data.mxB4);
	txtmxC4.setValue(select.data.mxC4);
	txtmxA5.setValue(select.data.mxA5);
	txtmxB5.setValue(select.data.mxB5);
	txtmxC5.setValue(select.data.mxC5);
	txtegxA.setValue(select.data.egxA);
	txtegxB.setValue(select.data.egxB);
	txtegxC.setValue(select.data.egxC);
	txtefxA.setValue(select.data.efxA);
	txtefxB.setValue(select.data.efxB);
	txtefxC.setValue(select.data.efxC);
	txttsyhA.setValue(select.data.tsyhA);
	txttsyhB.setValue(select.data.tsyhB);
	txttsyhC.setValue(select.data.tsyhC);
	txttsehA.setValue(select.data.tsehA);
	txttsehB.setValue(select.data.tsehB);
	txttsehC.setValue(select.data.tsehC);
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
		params: {id:id,cbr:txtcbr.getValue(),cbsj:txtcbsj.getValue().format("Y-m-d"),tianXieShiJian:tianXieShiJian,tianXieRen:tianXieRen,
				zbgycA3:txtzbgycA3.getValue(),zbgycB3:txtzbgycB3.getValue(),zbgycC3:txtzbgycC3.getValue(),zbgyczx3:txtzbgyczx3.getValue(),
				zbgycA4:txtzbgycA4.getValue(),zbgycB4:txtzbgycB4.getValue(),zbgycC4:txtzbgycC4.getValue(),zbgyczx4:txtzbgyczx4.getValue(),
				qbbgycA02:txtqbbgycA02.getValue(),qbbgycB02:txtqbbgycB02.getValue(),qbbgycC02:txtqbbgycC02.getValue(),mxA4:txtmxA4.getValue(),
				mxB4:txtmxB4.getValue(),mxC4:txtmxC4.getValue(),mxA5:txtmxA5.getValue(),mxB5:txtmxB5.getValue(),mxC5:txtmxC5.getValue(),
				egxA:txtegxA.getValue(),egxB:txtegxB.getValue(),egxC:txtegxC.getValue(),efxA:txtefxA.getValue(),efxB:txtefxB.getValue(),
				efxC:txtefxC.getValue(),tsyhA:txttsyhA.getValue(),tsyhB:txttsyhB.getValue(),tsyhC:txttsyhC.getValue(),tsehA:txttsehA.getValue(),
				tsehB:txttsehB.getValue(),tsehC:txttsehC.getValue()
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
