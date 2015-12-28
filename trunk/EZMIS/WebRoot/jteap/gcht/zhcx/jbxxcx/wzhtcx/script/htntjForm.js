var dtNf = new Ext.form.TimeField({
	fieldLabel : '查询年份',
	renderTo : 'divNf',
	minValue : '2010',
	maxValue : formatDate(new Date(), 'yyyy'),
	format : 'Y',
	maxHeight : '100',
	readOnly : true,
	allowBlank : false,
	increment : 525600
})

// 合同总份数
var txtHtZfs = new Ext.form.TextField({
	fieldLabel : '合同总份数',
	renderTo : 'divHtZfs',
	style : 'text-align:right',
	readOnly : true

})

// 合同总金额
var txtHtZje = new Ext.form.NumberField({
	fieldLabel : '合同总金额',
	renderTo : 'divHtZje',
	style : 'text-align:right',
	readOnly : true
})

// 在审批合同数
var txtZspHt = new Ext.form.TextField({
	fieldLabel : '在审批合同数',
	renderTo : 'divZspHt',
	style : 'text-align:right',
	readOnly : true

})

// 在执行份数
var txtZzxHt = new Ext.form.TextField({
	fieldLabel : '在执行份数',
	renderTo : 'divZzxHt',
	style : 'text-align:right',
	readOnly : true
})

// 终结份数
var txtZjHt = new Ext.form.TextField({
	fieldLabel : '终结份数',
	renderTo : 'divZjHt',
	style : 'text-align:right',
	readOnly : true
})

// 总支付金额
var txtZzfje = new Ext.form.NumberField({
	fieldLabel : '总支付金额',
	renderTo : 'divZzfje',
	style : 'text-align:right',
	readOnly : true
})

function query() {
	if (dtNf.getValue() == '') {
		alert('请选择统计年份')
		return;
	}

	Ext.Ajax.request({
		url : link9,
		method : 'POST',
		params : {
			tjnf : dtNf.getValue(),
			tableName : tableName
		},
		success : function(ajax) {
			var resText = ajax.responseText;
			var obj = Ext.decode(resText);
			if (obj.success) {
				var data = obj.data[0];
				txtHtZfs.setValue(data.htzfs);
				txtHtZje.setValue(data.htzje);
				txtZspHt.setValue(data.zspht);
				txtZzxHt.setValue(data.zzxht);
				txtZjHt.setValue(data.zjht);
				txtZzfje.setValue(data.zzfje);
			} else {
				alert("数据库异常，请联系管理员!");
			}
		},
		failure : function() {
			alert('数据库异常，请联系管理员！')
		}
	})
}