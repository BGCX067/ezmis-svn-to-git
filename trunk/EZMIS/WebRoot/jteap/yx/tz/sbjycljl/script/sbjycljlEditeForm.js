var distsTq = $dictListAjax("YX_TQ");

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

// 时间
var dtSj = new Ext.form.DateField({
	name : 'sj',
	renderTo : 'divsj',
	format : 'Y-m-d H:i',
	allowBlank : false,
	menu : new DatetimeMenu(),
	readOnly : true,
	width : 170,
	listeners : {
		change : function(dt, newValue, oldValue) {
			var dateFormat = 'yyyy-MM-dd HH:mm';
			var hzsj =  newValue ? formatDate(new Date(newValue), dateFormat) : "";
			var now =  formatDate(new Date(), dateFormat);
			if (compareDates(hzsj, dateFormat, now, dateFormat) == 1) {
				alert('时间不能大于当前时间！');
				this.setValue(oldValue);
			}
		}
	}
});

// 设备名称
var txtSbmc = new Ext.form.TextField({
	name : 'sbmc',
	renderTo : 'divsbmc',
	allowBlank : false,
	maxLength : 50,
	width : 170
});

// 测量项目
var txtClxm = new Ext.form.TextField({
	name : 'clxm',
	renderTo : 'divclxm',
	allowBlank : false,
	maxLength : 50,
	width : 170
});

// R15
var nmR15 = new Ext.form.NumberField({
	name : 'r15',
	renderTo : 'divr15',
	style : 'text-align:right',
	allowNegative : false,
	allowBlank : false,
	decimalPrecision : 6,
	width : 170,
	listeners : {
		blur : function(t) {
			if (nmR60.getValue() != '' && nmR60.getValue() != 0 && nmR15.getValue() != '') {
				nmR1560.setValue(nmR15.getValue() / nmR60.getValue());
			}
		}
	}
})

// R60
var nmR60 = new Ext.form.NumberField({
	name : 'r15',
	renderTo : 'divr60',
	style : 'text-align:right',
	allowNegative : false,
	allowBlank : false,
	decimalPrecision : 6,
	width : 170,
	listeners : {
		blur : function(t) {
			if (nmR60.getValue() != '' && nmR60.getValue() != 0 && nmR15.getValue() != '') {
				nmR1560.setValue(nmR15.getValue() / nmR60.getValue());
			}
		}
	}
})

// R15/R60
var nmR1560 = new Ext.form.NumberField({
	name : 'r1560',
	renderTo : 'divr1560',
	style : 'text-align:right',
	decimalPrecision : 6,
	readOnly : true,
	width : 170
})

// 使用仪表
var txtSyyb = new Ext.form.TextField({
	name : 'syyb',
	renderTo : 'divsyyb',
	allowBlank : false,
	maxLength : 50,
	width : 170
});

// 天气
var dsTq = new Ext.data.Store({
	reader : new Ext.data.JsonReader({
		root : 'rows',
		id : 'id'
	}, ['key', 'value', 'id']),
	data : {
		rows : distsTq
	}
})
var cmbTq = new Ext.form.ComboBox({
	hiddenName : 'tq',
	renderTo : 'divtq',
	store : dsTq,
	editable : false,
	displayField : 'value',
	valueField : 'key',
	mode : 'local',
	allowBlank : false,
	triggerAction : 'all',
	readOnly : true,
	typeAhead : true,
	typeAheadDelay : 2000,
	selectOnFocus : true,
	emptyText : '请选择天气',
	width : 170
})

// 测量人
var txtClr = new Ext.form.TextField({
	name : 'clr',
	renderTo : 'divclr',
	allowBlank : false,
	maxLength : 16,
	width : 170
});

// 监护人
var txtJhr = new Ext.form.TextField({
	name : 'jhr',
	renderTo : 'divjhr',
	allowBlank : false,
	maxLength : 16,
	width : 170
});

// 备注
var txtBz = new Ext.form.TextArea({
	name : 'bz',
	renderTo : 'divbz',
	maxLength : 200,
	width : 450
})

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
				dtSj.setValue(formatDate(new Date(data.sj['time']), "yyyy-MM-dd hh:mm"));
				txtSbmc.setValue(data.sbmc);
				txtClxm.setValue(data.clxm);
				nmR15.setValue(data.r15);
				nmR60.setValue(data.r60);
				nmR1560.setValue(data.r1560);
				txtSyyb.setValue(data.syyb);
				cmbTq.setValue(data.tq);
				txtClr.setValue(data.clr);
				txtJhr.setValue(data.jhr);
				txtBz.setValue(data.bz);
			}
		},
		failure : function() {

		}
	})
}
// 保存
function save() {
	if (!dtSj.isValid()) {
		alert('时间不能为空');
		return;
	}
	if (!txtSbmc.isValid()) {
		alert('设备名称验证不正确');
		return;
	}
	if (!txtClxm.isValid()) {
		alert('测量验证不正确');
		return;
	}
	if (!nmR15.isValid()) {
		alert('R15验证不正确');
		return;
	}
	if (!nmR60.isValid()) {
		alert('R60验证不正确');
		return;
	}
	if (!txtSyyb.isValid()) {
		alert('使用仪表验证不正确');
		return;
	}
	if (!cmbTq.isValid()) {
		alert('天气不能为空');
		return;
	}
	if (!txtClr.isValid()) {
		alert('测量人验证不正确');
		return;
	}
	if (!txtJhr.isValid()) {
		alert('监护人验证不正确');
		return;
	}
	if (!txtBz.isValid()) {
		alert('备注验证不正确');
		return;
	}
	myMask.show();
	var sj = dtSj.getValue() ? formatDate(new Date(dtSj.getValue()), "yyyy-MM-dd hh:mm") : "";
	Ext.Ajax.request({
		url : link6,
		method : 'POST',
		params : {
			id : hdnId.getValue(),
			sj : sj,
			sbmc : txtSbmc.getValue(),
			clxm : txtClxm.getValue(),
			r15 : nmR15.getValue(),
			r60 : nmR60.getValue(),
			r1560 : nmR1560.getValue(),
			syyb : txtSyyb.getValue(),
			tq : cmbTq.getValue(),
			clr : txtClr.getValue(),
			jhr : txtJhr.getValue(),
			bz : txtBz.getValue()
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
