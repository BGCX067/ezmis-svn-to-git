var distsJzlx = $dictListAjax("YX_JZLX");
var dists300Kgmc = $dictListAjax("YX_300_KGMC");
var dists600Kgmc = $dictListAjax("YX_600_KGMC");

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

var dsJzlx = new Ext.data.Store({
	reader : new Ext.data.JsonReader({
		root : 'rows',
		id : 'id'
	}, ['key', 'value', 'id']),
	data : {
		rows : distsJzlx
	}
})
var cmbJzlx = new Ext.form.ComboBox({
	hiddenName : 'jzlx',
	renderTo : 'divjzlx',
	store : dsJzlx,
	editable : false,
	displayField : 'value',
	valueField : 'key',
	mode : 'local',
	allowBlank : false,
	triggerAction : 'all',
	typeAhead : true,
	typeAheadDelay : 2000,
	selectOnFocus : true,
	emptyText : '请选择机组类型',
	width : 170,
	listeners : {
		select : function(t, r, index) {
			var value = r.data.value;
			if (value == "600MW机组") {
				cmbKgmc.bindStore(ds600Kgmc);
			} else {
				cmbKgmc.bindStore(ds300Kgmc);
			}
		}
	}
})

var ds300Kgmc = new Ext.data.Store({
	reader : new Ext.data.JsonReader({
		root : 'rows',
		id : 'id'
	}, ['key', 'value', 'id']),
	data :{
		rows : dists300Kgmc
	}
});

var ds600Kgmc = new Ext.data.Store({
	reader : new Ext.data.JsonReader({
		root : 'rows',
		id : 'id'
	}, ['key', 'value', 'id']),
	data :{
		rows : dists600Kgmc
	}
});
var dsKgmc = new Ext.data.Store({
	reader : new Ext.data.JsonReader({
		root : 'rows',
		id : 'id'
	}, ['key', 'value', 'id'])
});

var cmbKgmc = new Ext.form.ComboBox({
	hiddenName : 'kgmc',
	renderTo : 'divkgmc',
	store : dsKgmc,
	editable : false,
	displayField : 'value',
	valueField : 'key',
	mode : 'local',
	allowBlank : false,
	triggerAction : 'all',
	typeAhead : true,
	typeAheadDelay : 2000,
	selectOnFocus : true,
	emptyText : '请选择开关名称',
	width : 170
})

// 合闸时间
var dtHzsj = new Ext.form.DateField({
	name : 'hzsj',
	renderTo : 'divhzsj',
	format : 'Y-m-d H:i',
	menu:new DatetimeMenu(),
	readOnly : true,
	allowBlank : false,
	width : 170,
	listeners : {
		change : function(dt, newValue, oldValue) {
			var dateFormat = 'yyyy-MM-dd HH:mm';
			var hzsj =  newValue ? formatDate(new Date(newValue), dateFormat) : "";
			var now =  formatDate(new Date(), dateFormat);
			if (compareDates(hzsj, dateFormat, now, dateFormat) == 1) {
				alert('合闸时间不能大于当前时间！');
				this.setValue(oldValue);
			}
		}
	}
});

// 合闸记录人
var txtHzjlr = new Ext.form.TextField({
	name : 'hzjlr',
	renderTo : 'divhzjlr',
	allowBlank : false,
	maxLength : 16,
	width : 170
});

// 合闸原因
var txtHzyy = new Ext.form.TextArea({
	name : 'hzyy',
	renderTo : 'divhzyy',
	height:130,
	maxLength : 200,
	width : 450
})

// 分闸时间
var dtFzsj = new Ext.form.DateField({
	name : 'fzsj',
	renderTo : 'divfzsj',
	format : 'Y-m-d H:i',
	menu:new DatetimeMenu(),
	readOnly : true,
	width : 170,
	listeners : {
		change : function(dt, newValue, oldValue) {
			var dateFormat = 'yyyy-MM-dd HH:mm';
			var hzsj =  newValue ? formatDate(new Date(newValue), dateFormat) : "";
			var now =  formatDate(new Date(), dateFormat);
			if (compareDates(hzsj, dateFormat, now, dateFormat) == 1) {
				alert('分闸时间不能大于当前时间！');
				this.setValue(oldValue);
			}
		}
	}
});

// 分闸记录人
var txtFzjlr = new Ext.form.TextField({
	name : 'fzjlr',
	renderTo : 'divfzjlr',
	maxLength : 16,
	width : 170
});

// 分闸原因
var txtFzyy = new Ext.form.TextArea({
	name : 'fzyy',
	renderTo : 'divfzyy',
	height:130,
	maxLength : 200,
	width : 450
})

// 运行时间
var dtYxsj = new Ext.form.NumberField({
	name : 'yxsj',
	renderTo : 'divyxsj',
	allowNegative : false,
	style : 'text-align:right',
	decimalPrecision : 0,
	maxValue : 9999,
	width : 170
});

// 动作次数
var nmDzcs = new Ext.form.NumberField({
	name : 'dzcs',
	renderTo : 'divdzcs',
	style : 'text-align:right',
	allowNegative : false,
	decimalPrecision : 0,
	maxValue : 9999,
	width : 170
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
				cmbJzlx.setValue(data.jzlx);
				cmbKgmc.setValue(data.kgmc);
				dtHzsj.setValue(formatDate(new Date(data.hzsj['time']), "yyyy-MM-dd hh:mm"));
				txtHzjlr.setValue(data.hzjlr);
				txtHzyy.setValue(data.hzyy);
				if (data.fzsj) {
					dtFzsj.setValue(formatDate(new Date(data.fzsj['time']), "yyyy-MM-dd hh:mm"));
				}
				txtFzjlr.setValue(data.fzjlr);
				txtFzyy.setValue(data.fzyy);
				dtYxsj.setValue(data.yxsj);
				nmDzcs.setValue(data.dzcs);
			}
		},
		failure : function() {

		}
	})
}
// 保存
function save() {
	if (!cmbJzlx.isValid()) {
		alert('机组类型不能为空');
		return;
	}
	if (!cmbKgmc.isValid()) {
		alert('开关名称不能为空');
		return;
	}
	if (!dtHzsj.isValid()) {
		alert('合闸时间不能为空');
		return;
	}
	if (!txtHzjlr.isValid()) {
		alert('合闸记录人验证不正确');
		return;
	}
	if (!txtHzyy.isValid()) {
		alert('合闸原因验证不正确');
		return;
	}
	if (!txtFzjlr.isValid()) {
		alert('分闸记录人验证不正确');
		return;
	}
	if (!txtFzyy.isValid()) {
		alert('分闸原因验证不正确');
		return;
	}
	if (!dtYxsj.isValid()) {
		alert('运行时间验证不正确');
		return;
	}
	if (!nmDzcs.isValid()) {
		alert('动作次数验证不正确');
		return;
	}
	myMask.show();
	var hzsj = dtHzsj.getValue()?formatDate(new Date(dtHzsj.getValue()), "yyyy-MM-dd hh:mm"):"";
	var fzsj = dtFzsj.getValue()?formatDate(new Date(dtFzsj.getValue()), "yyyy-MM-dd hh:mm"):"";
	Ext.Ajax.request({
		url : link6,
		method : 'POST',
		params : {
			id : hdnId.getValue(),
			jzlx : cmbJzlx.getValue(),
			kgmc : cmbKgmc.getValue(),
			hzsj : hzsj,
			hzjlr : txtHzjlr.getValue(),
			hzyy : txtHzyy.getValue(),
			fzsj : fzsj,
			fzjlr : txtFzjlr.getValue(),
			fzyy : txtFzyy.getValue(),
			yxsj : dtYxsj.getValue(),
			dzcs : nmDzcs.getValue()
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
