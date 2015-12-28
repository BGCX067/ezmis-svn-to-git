var myMaskSave = new Ext.LoadMask(Ext.getBody(), {
	msg : "正在保存，请稍候..."
});
// ************** 页面设计 ****************//
var hdnId = new Ext.form.Hidden({
	name : 'id',
	renderTo : 'pzid'
})

var hdnTablename = new Ext.form.Hidden({
	name : 'tablename',
	renderTo : 'divTablename'
})

// 票号
var txtPh = new Ext.form.TextField({
	name : 'ph',
	renderTo : 'divPh',
	readOnly : true,
	width : 200
})

// 票种类
var txtPmc = new Ext.form.TextField({
	name : 'pmc',
	renderTo : 'divPmc',
	readOnly : true,
	width : 200
})

// 开始时间
var txtJhkssj = new Ext.form.TextField({
	name : 'jhkssj',
	renderTo : 'divJhkssj',
	readOnly : true,
	width : 200
})

// 结束时间
var txtJhjssj = new Ext.form.TextField({
	name : 'jhjssj',
	renderTo : 'divJhjssj',
	readOnly : true,
	width : 200
})

// 审核部门
var cmbShbm = new Ext.form.TextField({
	name : 'shbm',
	renderTo : 'divShbm',
	width : 200,
	readOnly : true
});

// 审核人
var txtShr = new Ext.form.TextField({
	name : 'shr',
	renderTo : 'divShr',
	readOnly : true,
	width : 200
})

// 审核时间
var txtShsj = new Ext.form.TextField({
	name : 'shsj',
	renderTo : 'divShsj',
	readOnly : true,
	width : 200
})

// 票状态
var rdShzt = new Ext.form.RadioGroup({
	renderTo : 'divShzt',
	disabled : true,
	width : 130,
	columns : 2,
	items : [{
		boxLabel : '合格',
		name : 'shzt',
		inputValue : '1',
		checked : true
	}, {
		boxLabel : '不合格',
		name : 'shzt',
		inputValue : '0',
		checked : false
	}]
})

// 批注
var txtPz = new Ext.form.TextArea({
	name : 'pz',
	renderTo : 'divPz',
	readOnly : true,
	height : 80,
	width : 473
})

// *********** 功能处理 ***********//
// 初始化
function load() {
	hdnId.setValue(arg.id);
	txtPh.setValue(arg.ph);
	txtPmc.setValue(arg.pmc);
	txtJhkssj.setValue(arg.jhkssj);
	txtJhjssj.setValue(arg.jhjssj);
	hdnTablename.setValue(arg.tablename);
	if (arg.shbm != null && arg.shbm != '') {
		cmbShbm.setValue(arg.shbm);
	}
	if (arg.shr != null && arg.shr != '') {
		txtShr.setValue(arg.shr);
	}
	if (arg.shsj != null && arg.shsj != '') {
		txtShsj.setValue(arg.shsj);
	}
	if (arg.shzt != null && arg.shzt != '') {
		rdShzt.setValue(arg.shzt);
	}
	if (arg.pz != null && arg.pz != '') {
		txtPz.setValue(arg.pz);
	}
}
