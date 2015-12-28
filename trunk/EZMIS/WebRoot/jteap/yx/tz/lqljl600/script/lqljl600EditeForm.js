var myMask = new Ext.LoadMask(Ext.getBody(), {
	msg : "正在保存，请稍候..."
});
var myMaskLoad = new Ext.LoadMask(Ext.getBody(), {
	msg : "正在加载，请稍候..."
});
// ************** 页面设计 ****************//
var hdnId = new Ext.form.Hidden({
	name : 'id',
	renderTo : 'id'
})

// 开始时间
var dtKssj = new Ext.form.DateField({
	name : 'kssj',
	renderTo : 'divkssj',
	format : 'Y-m-d H时',
	menu : new DatetimeMenu(),
	readOnly : true,
	width : 170,
	listeners : {
		blur : function(t) {
			if (dtJssj.getValue() != '' && dtKssj.getValue() != '' && dtJssj.getValue() <= dtKssj.getValue()) {
				alert('结束时间必须大于开始时间！');
				return;
			}
			if (dtJssj.getValue() != '' && dtKssj.getValue() != '') {
				var yxsj = (dtJssj.getValue() - dtKssj.getValue()) / 3600000
				nmYxsj.setValue(yxsj);
			}
		},
		change : function(dt, newValue, oldValue) {
			var dateFormat = 'yyyy-MM-dd HH:mm';
			var hzsj =  newValue ? formatDate(new Date(newValue), dateFormat) : "";
			var now =  formatDate(new Date(), dateFormat);
			if (compareDates(hzsj, dateFormat, now, dateFormat) == 1) {
				alert('开始时间不能大于当前时间！');
				this.setValue(oldValue);
			}
		}
	}
});

// 开始氢压P1
var nmKsqyp1 = new Ext.form.NumberField({
	name : 'ksqyp1',
	renderTo : 'divksqyp1',
	style : 'text-align:right',
	allowBlank : false,
	allowNegative : false,
	decimalPrecision : 6,
	width : 140
})

// 氢温t1
var nmQwxt1 = new Ext.form.NumberField({
	name : 'qwxt1',
	renderTo : 'divqwxt1',
	style : 'text-align:right',
	allowBlank : false,
	allowNegative : false,
	decimalPrecision : 6,
	width : 140
})

// 氢温t2
var nmQwxt2 = new Ext.form.NumberField({
	name : 'qwxt2',
	renderTo : 'divqwxt2',
	style : 'text-align:right',
	allowBlank : true,
	allowNegative : false,
	decimalPrecision : 6,
	width : 140
})

// 氢温T1
var nmQwdt1 = new Ext.form.NumberField({
	name : 'qwdt1',
	renderTo : 'divqwdt1',
	style : 'text-align:right',
	allowBlank : true,
	allowNegative : false,
	decimalPrecision : 6,
	width : 140
})

// 结束时间
var dtJssj = new Ext.form.DateField({
	name : 'jssj',
	renderTo : 'divjssj',
	format : 'Y-m-d H时',
	menu : new DatetimeMenu(),
	readOnly : true,
	width : 170,
	listeners : {
		blur : function(t) {
			if (dtJssj.getValue() != '' && dtKssj.getValue() != '' && dtJssj.getValue() <= dtKssj.getValue()) {
				alert('结束时间必须大于开始时间！');
				return;
			}
			if (dtJssj.getValue() != '' && dtKssj.getValue() != '') {
				var yxsj = (dtJssj.getValue() - dtKssj.getValue()) / 3600000
				nmYxsj.setValue(yxsj);
			}
		}
	}
});

// 结束氢压P2
var nmJsqyp2 = new Ext.form.NumberField({
	name : 'jsqyp2',
	renderTo : 'divjsqyp2',
	style : 'text-align:right',
	allowBlank : true,
	allowNegative : false,
	decimalPrecision : 6,
	width : 140
})

// 氢温T2
var nmQwdt2 = new Ext.form.NumberField({
	name : 'qwdt2',
	renderTo : 'divqwdt2',
	style : 'text-align:right',
	allowBlank : true,
	allowNegative : false,
	decimalPrecision : 6,
	width : 140
})

// 运行时间H
var nmYxsj = new Ext.form.NumberField({
	name : 'yxsj',
	renderTo : 'divyxsj',
	style : 'text-align:right',
	readOnly : true,
	width : 140
})

// 漏氢量
var nmLql = new Ext.form.NumberField({
	name : 'lql',
	renderTo : 'divlql',
	style : 'text-align:right',
	readOnly : true,
	width : 140
})

// 漏氢率
var nmLqlv = new Ext.form.NumberField({
	name : 'lqlv',
	renderTo : 'divlqlv',
	style : 'text-align:right',
	readOnly : true,
	width : 140
})

// 填写人1
var txtTxr1 = new Ext.form.TextField({
	name : 'txr1',
	renderTo : 'divtxr1',
	allowBlank : false,
	maxLength : 16,
	width : 170
});

// 填写人2
var txtTxr2 = new Ext.form.TextField({
	name : 'txr2',
	renderTo : 'divtxr2',
	allowBlank : false,
	maxLength : 16,
	width : 170
});

// *********** 功能处理 ***********//
// 初始化
function load() {
	if (id == null || id == '') {
		return;
	}
	myMaskLoad.show();
	Ext.Ajax.request({
		url : link5,
		method : 'POST',
		params : {
			id : id
		},
		success : function(ajax) {
			var responseText = ajax.responseText;
			var obj = Ext.decode(responseText);
			myMaskLoad.hide();
			if (obj.success) {
				var data = obj.data[0];
				if(data.kssj != null)
				dtKssj.setValue(formatDate(new Date(data.kssj['time']), "yyyy-MM-dd hh时"));
				nmKsqyp1.setValue(data.ksqyp1);
				nmQwxt1.setValue(data.qwxt1);
				nmQwxt2.setValue(data.qwxt2);
				nmQwdt1.setValue(data.qwdt1);
				if(data.jssj != null)
				dtJssj.setValue(formatDate(new Date(data.jssj['time']), "yyyy-MM-dd hh时"));
				nmJsqyp2.setValue(data.jsqyp2);
				nmQwdt2.setValue(data.qwdt2);
				nmYxsj.setValue(data.yxsj);
				nmLql.setValue(data.lql);
				nmLqlv.setValue(data.lqlv);
				txtTxr1.setValue(data.txr1);
				txtTxr2.setValue(data.txr2);
			}
		},
		failure : function() {

		}
	})
}
// 保存
function save() {
//	if (nmLql.getValue() == null || nmLql.getValue() == '') {
//		alert('请先计算漏氢量');
//		return;
//	}
	myMask.show();
	var kssj = dtKssj.getValue() ? formatDate(new Date(dtKssj.getValue()), "yyyy-MM-dd hh:mm") : "";
	var jssj = dtJssj.getValue() ? formatDate(new Date(dtJssj.getValue()), "yyyy-MM-dd hh:mm") : "";
	Ext.Ajax.request({
		url : link6,
		method : 'POST',
		params : {
			id : hdnId.getValue(),
			kssj : kssj,
			jssj : jssj,
			ksqyp1 : nmKsqyp1.getValue(),
			qwxt1 : nmQwxt1.getValue(),
			qwxt2 : nmQwxt2.getValue(),
			qwdt1 : nmQwdt1.getValue(),
			jsqyp2 : nmJsqyp2.getValue(),
			qwdt2 : nmQwdt2.getValue(),
			yxsj : nmYxsj.getValue(),
			lql : nmLql.getValue(),
			lqlv : nmLqlv.getValue(),
			txr1 : txtTxr1.getValue(),
			txr2 : txtTxr2.getValue()
		},
		success : function(ajax) {
			var resText = ajax.responseText;
			var obj = Ext.decode(resText);
			if (obj && obj.success) {
				alert("保存成功");
				window.returnValue = true;
				window.close();
			} else {
				alert(obj.msg);
				window.returnValue = false;
				window.close();
			}
		},
		failure : function() {
			alert("数据库异常，请联系管理员");
			window.returnValue = false;
			window.close();
		}
	})
}

function calLql() {
	if (nmYxsj.getValue() == null || nmYxsj.getValue() == '') {
		alert('请填写开始时间和结束时间！');
		return;
	}
	if (!nmKsqyp1.isValid()) {
		alert('开始氢压P1验证不正确！');
		return;
	}
	if (!nmQwxt1.isValid()) {
		alert('氢温t1验证不正确！');
		return;
	}
	if (!nmQwxt2.isValid()) {
		alert('氢温t2验证不正确！');
		return;
	}
	if (!nmQwdt1.isValid()) {
		alert('氢温T1验证不正确！');
		return;
	}
	if (!nmJsqyp2.isValid()) {
		alert('结束氢压P2验证不正确！');
		return;
	}
	if (!nmQwdt2.isValid()) {
		alert('氢温T2验证不正确！');
		return;
	}
	if (!txtTxr1.isValid()) {
		alert('填写人1验证不正确！');
		return;
	}
	if (!txtTxr2.isValid()) {
		alert('填写人2验证不正确！');
		return;
	}

	var P1 = nmKsqyp1.getValue();
	var t1 = nmQwxt1.getValue();
	var t2 = nmQwxt2.getValue();
	var T1 = nmQwdt1.getValue();
	var P2 = nmJsqyp2.getValue();
	var T2 = nmQwdt2.getValue();
	var H = nmYxsj.getValue();
	var lql = d;
	lql = formatFloat(lql, 2);
	nmLql.setValue(lql);

	var lqlv = 24 / H * (1 - (P2 + 0.1) / (P1 + 0.1) * (273 + (t1 + T1) / 2) / (273 + (t2 + T2) / 2));
	lqlv = formatFloat(lqlv, 2);
	nmLqlv.setValue(lqlv);
}

function formatFloat(src, pos) {
	return Math.round(src * Math.pow(10, pos)) / Math.pow(10, pos);
}