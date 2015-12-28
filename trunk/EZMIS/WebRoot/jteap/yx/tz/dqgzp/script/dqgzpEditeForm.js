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
var hdnpzzzmc = new Ext.form.Hidden({
	name : 'pzzzmc',
	renderTo : 'divpzzzmc'
})
var hdnxkrmc = new Ext.form.Hidden({
	name : 'xkrmc',
	renderTo : 'divxkrmc'
})
var hdnzjrmc = new Ext.form.Hidden({
	name : 'zjrmc',
	renderTo : 'divzjrmc'
})
var hdnpzyqzzmc = new Ext.form.Hidden({
	name : 'pzyqzzmc',
	renderTo : 'divpzyqzzmc'
})
var hdnzfrmc = new Ext.form.Hidden({
	name : 'zfrmc',
	renderTo : 'divzfrmc'
})

var distsZt = $dictListAjax("YX_GZPZT");
// 工作票编号
var txtgzpbh = new Ext.form.TextField({
	name : 'gzpbh',
	renderTo : 'divgzpbh',
	allowBlank : false,
	maxLength : 16,
	width : 170
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
	maxLength : 100,
	width : 480
});

// 工作负责人
var txtgzfzr = new Ext.form.TextField({
	name : 'gzfzr',
	renderTo : 'divgzfzr',
	allowBlank : false,
	maxLength : 10,
	width : 170
});

// 工作票登记人
var txtgzpdjr = new Ext.form.TextField({
	name : 'gzpdjr',
	renderTo : 'divgzpdjr',
	allowBlank : false,
	maxLength : 10,
	width : 170
});

// 工作班人员
var txtgzbry = new Ext.form.TextField({
	name : 'gzbry',
	renderTo : 'divgzbry',
	allowBlank : false,
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
var dszz = new Ext.data.JsonStore({
	url : link7 + "?roleSn=ZZ",
	fields : ['id', 'name']
})
dszz.load();
var cmbpzzz = new Ext.form.ComboBox({
	hiddenName : 'pzzz',
	renderTo : 'divpzzz',
	store : dszz,
	editable : false,
	displayField : 'name',
	valueField : 'id',
	mode : 'local',
	triggerAction : 'all',
	readOnly : true,
	typeAhead : true,
	typeAheadDelay : 1000,
	selectOnFocus : true,
	width : 170,
	listeners : {
		select : function() {
			hdnpzzzmc.setValue(this.getRawValue());
		}
	}
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
var dsGzr = new Ext.data.JsonStore({
	url : link7 + "?roleSn=YX_QDTZ",
	fields : ['id', 'name']
})
dsGzr.load();
var cmbxkr = new Ext.form.ComboBox({
	hiddenName : 'xkr',
	renderTo : 'divxkr',
	store : dsGzr,
	editable : false,
	displayField : 'name',
	valueField : 'id',
	mode : 'local',
	triggerAction : 'all',
	readOnly : true,
	typeAhead : true,
	typeAheadDelay : 1000,
	selectOnFocus : true,
	width : 170,
	listeners : {
		select : function() {
			hdnxkrmc.setValue(this.getRawValue());
		}
	}
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
var cmbpzyqzz = new Ext.form.ComboBox({
	hiddenName : 'pzyqzz',
	renderTo : 'divpzyqzz',
	store : dszz,
	editable : false,
	displayField : 'name',
	valueField : 'id',
	mode : 'local',
	triggerAction : 'all',
	readOnly : true,
	typeAhead : true,
	typeAheadDelay : 3000,
	selectOnFocus : true,
	width : 170,
	listeners : {
		select : function() {
			hdnpzyqzzmc.setValue(this.getRawValue());
		}
	}
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
var cmbgzpzjr = new Ext.form.ComboBox({
	hiddenName : 'gzpzjr',
	renderTo : 'divgzpzjr',
	store : dsGzr,
	editable : false,
	displayField : 'name',
	valueField : 'id',
	mode : 'local',
	triggerAction : 'all',
	readOnly : true,
	typeAhead : true,
	typeAheadDelay : 3000,
	selectOnFocus : true,
	width : 170,
	listeners : {
		select : function() {
			hdnzjrmc.setValue(this.getRawValue());
		}
	}
})

// 工作票作废人
var cmbgzpzfr = new Ext.form.ComboBox({
	hiddenName : 'gzpzfr',
	renderTo : 'divgzpzfr',
	store : dsGzr,
	editable : false,
	displayField : 'name',
	valueField : 'id',
	mode : 'local',
	triggerAction : 'all',
	readOnly : true,
	typeAhead : true,
	typeAheadDelay : 3000,
	selectOnFocus : true,
	width : 170,
	listeners : {
		select : function() {
			hdnzfrmc.setValue(this.getRawValue());
		}
	}
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
	maxLength : 200,
	width : 480
});

// 终结交待
var txtzjjd = new Ext.form.TextArea({
	name : 'zjjd',
	renderTo : 'divzjjd',
	maxLength : 200,
	width : 480
});

// 终结检查情况
var txtzjjcqk = new Ext.form.TextArea({
	name : 'zjjcqk',
	renderTo : 'divzjjcqk',
	maxLength : 200,
	width : 480
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
				
				if (data.gzpbh) {
					txtgzpbh.setValue(data.gzpbh);
					txtgzpbh.el.dom.readOnly = true;
				}
				if (data.ddjrw) {
					txtddjrw.setValue(data.ddjrw);
					txtddjrw.el.dom.readOnly = true;
				}
				cmbgzpzt.setValue(data.gzpzt);
				if (data.gzfzr) {
					txtgzfzr.setValue(data.gzfzr);
					txtgzfzr.el.dom.readOnly = true;
				}
				if (data.gzpdjr) {
					txtgzpdjr.setValue(data.gzpdjr);
					txtgzpdjr.el.dom.readOnly = true;
				}
				if (data.gzbry) {
					txtgzbry.setValue(data.gzbry);
					txtgzbry.el.dom.readOnly = true;
				}
				if (data.spr) {
					txtspr.setValue(data.spr);
					txtspr.el.dom.readOnly = true;
				}
				if (data.zfyy) {
					txtzfyy.setValue(data.zfyy);
					txtzfyy.el.dom.readOnly = true;
				}
				if (data.zjjd) {
					txtzjjd.setValue(data.zjjd);
					txtzjjd.el.dom.readOnly = true;
				}
				if (data.zjjcqk) {
					txtzjjcqk.setValue(data.zjjcqk);
					txtzjjcqk.el.dom.readOnly = true;
				}
				
				if (data.spsj) {
					dtspsj.setValue(formatDate(new Date(data.spsj['time']), "yyyy-MM-dd hh:mm"));
					diableDatePullDown(dtspsj);
				}
				if (data.pzgzqx) {
					dtpzgzqx.setValue(formatDate(new Date(data.pzgzqx['time']), "yyyy-MM-dd hh:mm"));
					diableDatePullDown(dtpzgzqx);
				}
				if (data.xksj) {
					dtxksj.setValue(formatDate(new Date(data.xksj['time']), "yyyy-MM-dd hh:mm"));
					diableDatePullDown(dtxksj);
				}
				if (data.yqsj) {
					dtyqsj.setValue(formatDate(new Date(data.yqsj['time']), "yyyy-MM-dd hh:mm"));
					diableDatePullDown(dtyqsj);
				}
				if (data.yqsxsj) {
					dtyqsxsj.setValue(formatDate(new Date(data.yqsxsj['time']), "yyyy-MM-dd hh:mm"));
					diableDatePullDown(dtyqsxsj);
				}
				if (data.zjsj) {
					dtzjsj.setValue(formatDate(new Date(data.zjsj['time']), "yyyy-MM-dd hh:mm"));
					diableDatePullDown(dtzjsj);
				}
				if (data.zfsj) {
					dtzfsj.setValue(formatDate(new Date(data.zfsj['time']), "yyyy-MM-dd hh:mm"));
					diableDatePullDown(dtzfsj);
				}

				hdnpzzzmc.setValue(data.pzzzmc);
				hdnxkrmc.setValue(data.xkrmc);
				hdnzjrmc.setValue(data.zjrmc);
				hdnpzyqzzmc.setValue(data.pzyqzzmc);
				hdnzfrmc.setValue(data.zfrmc);

				cmbpzzz.setValue(data.pzzz);
				cmbpzzz.setRawValue(data.pzzzmc);
				if (data.pzzz) {
					disablePullDown(cmbpzzz);
				}
				cmbxkr.setValue(data.xkr);
				cmbxkr.setRawValue(data.xkrmc);
				if (data.xkr) {
					disablePullDown(cmbxkr);
				}
				cmbpzyqzz.setValue(data.pzyqzz);
				cmbpzyqzz.setRawValue(data.pzyqzzmc);
				if (data.pzyqzz) {
					disablePullDown(cmbpzyqzz);
				}
				cmbgzpzjr.setValue(data.gzpzjr);
				cmbgzpzjr.setRawValue(data.zjrmc);
				if (data.gzpzjr) {
					disablePullDown(cmbgzpzjr);
				}
				cmbgzpzfr.setValue(data.gzpzfr);
				cmbgzpzfr.setRawValue(data.zfrmc);
				if (data.gzpzfr) {
					disablePullDown(cmbgzpzfr);
				}
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
			pzzz : cmbpzzz.getValue(),
			xkr : cmbxkr.getValue(),
			pzyqzz : cmbpzyqzz.getValue(),
			gzpzjr : cmbgzpzjr.getValue(),
			gzpzfr : cmbgzpzfr.getValue(),
			zfyy : txtzfyy.getValue(),
			zjjd : txtzjjd.getValue(),
			zjjcqk : txtzjjcqk.getValue(),
			pzzzmc : hdnpzzzmc.getValue(),
			xkrmc : hdnxkrmc.getValue(),
			zjrmc : hdnzjrmc.getValue(),
			pzyqzzmc : hdnpzyqzzmc.getValue(),
			zfrmc : hdnzfrmc.getValue(),
			spsj : spsj,
			xksj : xksj,
			pzgzqx : pzgzqx,
			yqsj : yqsj,
			yqsxsj : yqsxsj,
			zjsj : zjsj,
			zfsj : zfsj
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

/**
 * 禁用combobox的下拉框
 */
function disablePullDown(combo) {
	combo.addListener("expand", function() {
		this.collapse();
	}, combo);// 处理展开事件
}

/**
 * 禁用时间控件选择框
 */
function diableDatePullDown(dt) {
	dt.menu.addListener("show", function() {
		dt.menu.hide();
	}, dt)
}
