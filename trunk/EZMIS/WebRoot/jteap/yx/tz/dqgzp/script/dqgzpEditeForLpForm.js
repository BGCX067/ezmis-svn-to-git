var myMask = new Ext.LoadMask(Ext.getBody(), {
	msg : "正在保存，请稍候..."
});
var myMaskLoad = new Ext.LoadMask(Ext.getBody(), {
	msg : "正在加载，请稍候..."
});
// ************** 页面设计 ****************//
var hdnId = new Ext.form.Hidden({
	name : 'id',
	renderTo : 'divid'
})

var distsZt = $dictListAjax("YX_GZPZT");
// 工作票编号
var txtgzpbh = new Ext.form.TextField({
	name : 'gzpbh',
	renderTo : 'divgzpbh',
	allowBlank : false,
	maxLength : 25,
	readOnly : true,
	width : 170,
	value : ph
});

// 工作票状态
var dsZt = new Ext.data.Store({
	reader : new Ext.data.JsonReader({
		root : 'rows',
		id : 'id'
	}, ['key', 'value', 'id']),
	data : {
		rows : distsZt
	}
})
var cmbgzpzt = new Ext.form.ComboBox({
	hiddenName : 'gzpzt',
	renderTo : 'divgzpzt',
	store : dsZt,
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
	emptyText : '请选择状态',
	width : 170
})

// 地点及任务
var txtddjrw = new Ext.form.TextArea({
	name : 'ddjrw',
	renderTo : 'divddjrw',
	allowBlank : false,
	readOnly : (flowStep == '1') ? false : true,
	maxLength : 100,
	width : 480
});

// 工作负责人
var txtgzfzr = new Ext.form.TextField({
	name : 'gzfzr',
	renderTo : 'divgzfzr',
	allowBlank : false,
	readOnly : (flowStep == '1') ? false : true,
	maxLength : 10,
	width : 170
});

// 工作票登记人
var txtgzpdjr = new Ext.form.TextField({
	name : 'gzpdjr',
	renderTo : 'divgzpdjr',
	allowBlank : false,
	readOnly : (flowStep == '1') ? false : true,
	maxLength : 10,
	width : 170
});

// 工作班人员
var txtgzbry = new Ext.form.TextField({
	name : 'gzbry',
	renderTo : 'divgzbry',
	allowBlank : false,
	readOnly : (flowStep == '1') ? false : true,
	maxLength : 50,
	width : 480
});

// 收票时间
var dtspsj = new Ext.form.DateField({
	name : 'spsj',
	renderTo : 'divspsj',
	format : 'Y-m-d H:i',
	menu : new DatetimeMenu(),
	readOnly : true,
	width : 170
});

// 收票人
var txtspr = new Ext.form.TextField({
	name : 'spr',
	renderTo : 'divspr',
	readOnly : (flowStep == '1') ? false : true,
	maxLength : 10,
	width : 170
});

// 批准工作期限
var dtpzgzqx = new Ext.form.DateField({
	name : 'pzgzqx',
	renderTo : 'divpzgzqx',
	format : 'Y-m-d H:i',
	menu : new DatetimeMenu(),
	readOnly : true,
	width : 170
});

// 批准值长
var cmbpzzz = new Ext.form.TextField({
	name : 'pzzzmc',
	renderTo : 'divpzzz',
	readOnly : (flowStep == '2') ? false : true,
	maxLength : 10,
	width : 170
})

// 许可开工时间
var dtxksj = new Ext.form.DateField({
	name : 'xksj',
	renderTo : 'divxksj',
	format : 'Y-m-d H:i',
	menu : new DatetimeMenu(),
	readOnly : true,
	width : 170
});

// 许可人
var cmbxkr = new Ext.form.TextField({
	name : 'xkrmc',
	renderTo : 'divxkr',
	readOnly : (flowStep == '3') ? false : true,
	maxLength : 10,
	width : 170
})

// 延期期限
var dtyqsj = new Ext.form.DateField({
	name : 'yqsj',
	renderTo : 'divyqsj',
	format : 'Y-m-d H:i',
	menu : new DatetimeMenu(),
	readOnly : true,
	width : 170
});

// 批准延期值长
var cmbpzyqzz = new Ext.form.TextField({
	name : 'pzyqzzmc',
	renderTo : 'divpzyqzz',
	readOnly : (flowStep == '3') ? false : true,
	maxLength : 10,
	width : 170
})

// 办理延期手续时间
var dtyqsxsj = new Ext.form.DateField({
	name : 'yqsxsj',
	renderTo : 'divyqsxsj',
	format : 'Y-m-d H:i',
	menu : new DatetimeMenu(),
	readOnly : true,
	width : 170
});

// 终结时间
var dtzjsj = new Ext.form.DateField({
	name : 'zjsj',
	renderTo : 'divzjsj',
	format : 'Y-m-d H:i',
	menu : new DatetimeMenu(),
	readOnly : true,
	width : 170
});

// 终结人
var cmbgzpzjr = new Ext.form.TextField({
	name : 'zjrmc',
	renderTo : 'divgzpzjr',
	readOnly : (flowStep == '4') ? false : true,
	maxLength : 10,
	width : 170
})

// 工作票作废人
var cmbgzpzfr = new Ext.form.TextField({
	name : 'zfrmc',
	renderTo : 'divgzpzfr',
	readOnly : (flowStep == '4') ? false : true,
	maxLength : 10,
	width : 170
})

// 作废时间
var dtzfsj = new Ext.form.DateField({
	name : 'zfsj',
	renderTo : 'divzfsj',
	format : 'Y-m-d H:i',
	menu : new DatetimeMenu(),
	readOnly : true,
	width : 170
});

// 作废原因
var txtzfyy = new Ext.form.TextArea({
	name : 'zfyy',
	renderTo : 'divzfyy',
	readOnly : (flowStep == '4') ? false : true,
	maxLength : 200,
	width : 480
});

// 工作负责人终结交代
var txtzjjd = new Ext.form.TextArea({
	name : 'zjjd',
	renderTo : 'divzjjd',
	readOnly : (flowStep == '4') ? false : true,
	maxLength : 200,
	width : 480
});

// 终结检查情况
var txtzjjcqk = new Ext.form.TextArea({
	name : 'zjjcqk',
	renderTo : 'divzjjcqk',
	readOnly : (flowStep == '4') ? false : true,
	maxLength : 200,
	width : 480
});

// *********** 功能处理 ***********//
// 初始化
function load() {
	if (flowStep != '1') {
		diableDatePullDown(dtspsj);
	}
	if (flowStep != '2') {
		diableDatePullDown(dtpzgzqx);
	}
	if (flowStep != '3') {
		diableDatePullDown(dtxksj);
		diableDatePullDown(dtyqsj);
		diableDatePullDown(dtyqsxsj);
	}
	if (flowStep != '4') {
		diableDatePullDown(dtzfsj);
		diableDatePullDown(dtzjsj);
	}
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

				txtgzpbh.setValue(data.gzpbh);
				txtddjrw.setValue(data.ddjrw);
				cmbgzpzt.setValue(data.gzpzt);
				txtgzfzr.setValue(data.gzfzr);
				txtgzpdjr.setValue(data.gzpdjr);
				txtgzbry.setValue(data.gzbry);
				txtspr.setValue(data.spr);
				txtzfyy.setValue(data.zfyy);
				txtzjjd.setValue(data.zjjd);
				txtzjjcqk.setValue(data.zjjcqk);

				if (data.spsj) {
					dtspsj.setValue(formatDate(new Date(data.spsj['time']), "yyyy-MM-dd hh:mm"));
				}
				if (data.pzgzqx) {
					dtpzgzqx.setValue(formatDate(new Date(data.pzgzqx['time']), "yyyy-MM-dd hh:mm"));
				}
				if (data.xksj) {
					dtxksj.setValue(formatDate(new Date(data.xksj['time']), "yyyy-MM-dd hh:mm"));
				}
				if (data.yqsj) {
					dtyqsj.setValue(formatDate(new Date(data.yqsj['time']), "yyyy-MM-dd hh:mm"));
				}
				if (data.yqsxsj) {
					dtyqsxsj.setValue(formatDate(new Date(data.yqsxsj['time']), "yyyy-MM-dd hh:mm"));
				}
				if (data.zjsj) {
					dtzjsj.setValue(formatDate(new Date(data.zjsj['time']), "yyyy-MM-dd hh:mm"));
				}
				if (data.zfsj) {
					dtzfsj.setValue(formatDate(new Date(data.zfsj['time']), "yyyy-MM-dd hh:mm"));
				}

				cmbxkr.setValue(data.xkrmc);
				cmbpzyqzz.setValue(data.pzyqzzmc);
				cmbpzzz.setValue(data.pzzzmc);
				cmbgzpzjr.setValue(data.zjrmc);
				cmbgzpzfr.setValue(data.zfrmc);
				txtddjrw.focus();
				txtgzpbh.focus();
			}
		},
		failure : function() {

		}
	})
}
// 保存
function save() {
	if (!txtgzpbh.isValid()) {
		alert('编号验证不正确');
		return;
	}
	if (!cmbgzpzt.isValid()) {
		alert('工作票状态不能为空');
		return;
	}
	if (!txtddjrw.isValid()) {
		alert('地点及任务验证不正确');
		return;
	}
	if (!txtgzfzr.isValid()) {
		alert('工作负责人验证不正确');
		return;
	}
	if (!txtgzpdjr.isValid()) {
		alert('工作票登记人验证不正确');
		return;
	}
	if (!txtgzbry.isValid()) {
		alert('工作班人员验证不正确');
		return;
	}
	if (!txtspr.isValid()) {
		alert('收票人验证不正确');
		return;
	}
	if (!cmbpzzz.isValid()) {
		alert('批准值长验证不正确');
		return;
	}
	if (!cmbxkr.isValid()) {
		alert('许可人验证不正确');
		return;
	}
	if (!cmbpzyqzz.isValid()) {
		alert('批准延期值长验证不正确');
		return;
	}
	if (!cmbgzpzjr.isValid()) {
		alert('终结人验证不正确');
		return;
	}
	if (!cmbgzpzfr.isValid()) {
		alert('工作票作废人验证不正确');
		return;
	}
	if (!txtzfyy.isValid()) {
		alert('作废原因验证不正确');
		return;
	}
	if (!txtzjjd.isValid()) {
		alert('工作负责人终结交代验证不正确');
		return;
	}
	if (!txtzjjcqk.isValid()) {
		alert('工作终结人检查情况验证不正确');
		return;
	}
	myMask.show();

	var spsj = dtspsj.getValue() ? formatDate(new Date(dtspsj.getValue()), "yyyy-MM-dd hh:mm") : "";
	var pzgzqx = dtpzgzqx.getValue() ? formatDate(new Date(dtpzgzqx.getValue()), "yyyy-MM-dd hh:mm") : "";
	var xksj = dtxksj.getValue() ? formatDate(new Date(dtxksj.getValue()), "yyyy-MM-dd hh:mm") : "";
	var yqsj = dtyqsj.getValue() ? formatDate(new Date(dtyqsj.getValue()), "yyyy-MM-dd hh:mm") : "";
	var yqsxsj = dtyqsxsj.getValue() ? formatDate(new Date(dtyqsxsj.getValue()), "yyyy-MM-dd hh:mm") : "";
	var zjsj = dtzjsj.getValue() ? formatDate(new Date(dtzjsj.getValue()), "yyyy-MM-dd hh:mm") : "";
	var zfsj = dtzfsj.getValue() ? formatDate(new Date(dtzfsj.getValue()), "yyyy-MM-dd hh:mm") : "";

	Ext.Ajax.request({
		url : link6,
		method : 'POST',
		params : {
			id : hdnId.getValue(),
			gzpbh : txtgzpbh.getValue(),
			gzpzt : cmbgzpzt.getValue(),
			ddjrw : txtddjrw.getValue(),
			gzfzr : txtgzfzr.getValue(),
			gzpdjr : txtgzpdjr.getValue(),
			gzbry : txtgzbry.getValue(),
			spr : txtspr.getValue(),
			pzzzmc : cmbpzzz.getValue(),
			xkrmc : cmbxkr.getValue(),
			pzyqzzmc : cmbpzyqzz.getValue(),
			zjrmc : cmbgzpzjr.getValue(),
			zfrmc : cmbgzpzfr.getValue(),
			zfyy : txtzfyy.getValue(),
			zjjd : txtzjjd.getValue(),
			zjjcqk : txtzjjcqk.getValue(),
			spsj : spsj,
			xksj : xksj,
			pzgzqx : pzgzqx,
			yqsj : yqsj,
			yqsxsj : yqsxsj,
			zjsj : zjsj,
			zfsj : zfsj,
			type : type
		},
		success : function(ajax) {
			var resText = ajax.responseText;
			var obj = Ext.decode(resText);
			if (obj && obj.success) {
				alert("保存成功");
				window.returnValue = obj.id;
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

/**
 * 禁用时间控件选择框
 */
function diableDatePullDown(dt) {
	dt.menu.addListener("show", function() {
		dt.menu.hide();
	}, dt)
}