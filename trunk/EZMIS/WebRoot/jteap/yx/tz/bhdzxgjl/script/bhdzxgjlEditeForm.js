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

// 设备名称
var txtbhmc = new Ext.form.TextField({
	name : 'bhmc',
	renderTo : 'divbhmc',
	allowBlank : false,
	maxLength : 25,
	width : 190
});

// 更改时间
var dtggsj = new Ext.form.DateField({
	name : 'ggsj',
	renderTo : 'divggsj',
	format : 'Y-m-d H:i',
	menu : new DatetimeMenu(),
	allowBlank : false,
	readOnly : true,
	width : 200,
	listeners : {
		change : function(dt, newValue, oldValue) {
			var dateFormat = 'yyyy-MM-dd HH:mm';
			var hzsj = newValue ? formatDate(new Date(newValue), dateFormat) : "";
			var now = formatDate(new Date(), dateFormat);
			if (compareDates(hzsj, dateFormat, now, dateFormat) == 1) {
				alert('更改时间不能大于当前时间！');
				this.setValue(oldValue);
			}
		}
	}
});

// 定值修改列表
var txtbhmcgq1 = new Ext.form.TextField({
	name : 'bhmcgq1',
	renderTo : 'divbhmcgq1',
	maxLength : 10,
	width : 170
});
var txtbhmcgq2 = new Ext.form.TextField({
	name : 'bhmcgq2',
	renderTo : 'divbhmcgq2',
	maxLength : 10,
	width : 170
});
var txtbhmcgq3 = new Ext.form.TextField({
	name : 'bhmcgq3',
	renderTo : 'divbhmcgq3',
	maxLength : 10,
	width : 170
});
var txtbhmcgq4 = new Ext.form.TextField({
	name : 'bhmcgq4',
	renderTo : 'divbhmcgq4',
	maxLength : 10,
	width : 170
});
var txtbhmcgq5 = new Ext.form.TextField({
	name : 'bhmcgq5',
	renderTo : 'divbhmcgq5',
	maxLength : 10,
	width : 170
});

var txtzdzgq1 = new Ext.form.TextField({
	name : 'zdzgq1',
	renderTo : 'divzdzgq1',
	maxLength : 10,
	width : 170
});
var txtzdzgq2 = new Ext.form.TextField({
	name : 'zdzgq2',
	renderTo : 'divzdzgq2',
	maxLength : 10,
	width : 170
});
var txtzdzgq3 = new Ext.form.TextField({
	name : 'zdzgq3',
	renderTo : 'divzdzgq3',
	maxLength : 10,
	width : 170
});
var txtzdzgq4 = new Ext.form.TextField({
	name : 'zdzgq4',
	renderTo : 'divzdzgq4',
	maxLength : 10,
	width : 170
});
var txtzdzgq5 = new Ext.form.TextField({
	name : 'zdzgq5',
	renderTo : 'divzdzgq5',
	maxLength : 10,
	width : 170
});

var txtbhmcgh1 = new Ext.form.TextField({
	name : 'bhmcgh1',
	renderTo : 'divbhmcgh1',
	maxLength : 10,
	width : 170
});
var txtbhmcgh2 = new Ext.form.TextField({
	name : 'bhmcgh2',
	renderTo : 'divbhmcgh2',
	maxLength : 10,
	width : 170
});
var txtbhmcgh3 = new Ext.form.TextField({
	name : 'bhmcgh3',
	renderTo : 'divbhmcgh3',
	maxLength : 10,
	width : 170
});
var txtbhmcgh4 = new Ext.form.TextField({
	name : 'bhmcgh4',
	renderTo : 'divbhmcgh4',
	maxLength : 10,
	width : 170
});
var txtbhmcgh5 = new Ext.form.TextField({
	name : 'bhmcgh5',
	renderTo : 'divbhmcgh5',
	maxLength : 10,
	width : 170
});
var txtzdzgh1 = new Ext.form.TextField({
	name : 'zdzgh1',
	renderTo : 'divzdzgh1',
	maxLength : 10,
	width : 170
});
var txtzdzgh2 = new Ext.form.TextField({
	name : 'zdzgh2',
	renderTo : 'divzdzgh2',
	maxLength : 10,
	width : 170
});
var txtzdzgh3 = new Ext.form.TextField({
	name : 'zdzgh3',
	renderTo : 'divzdzgh3',
	maxLength : 10,
	width : 170
});
var txtzdzgh4 = new Ext.form.TextField({
	name : 'zdzgh4',
	renderTo : 'divzdzgh4',
	maxLength : 10,
	width : 170
});
var txtzdzgh5 = new Ext.form.TextField({
	name : 'zdzgh5',
	renderTo : 'divzdzgh5',
	maxLength : 10,
	width : 170
});

// 更改原因
var txtggyy = new Ext.form.TextArea({
	name : 'ggyy',
	renderTo : 'divggyy',
	maxLength : 200,
	width : 570
});

// 发令人
var txtggflr = new Ext.form.TextField({
	name : 'ggflr',
	renderTo : 'divggflr',
	maxLength : 10,
	width : 150
});

// 执行人
var txtggzhr = new Ext.form.TextField({
	name : 'ggzhr',
	renderTo : 'divggzhr',
	maxLength : 10,
	width : 150
});

// 运行检查
var txtggyhjc = new Ext.form.TextField({
	name : 'ggyhjc',
	renderTo : 'divggyhjc',
	maxLength : 10,
	width : 150
});

var txtbhmchfq1 = new Ext.form.TextField({
	name : 'bhmchfq1',
	renderTo : 'divbhmchfq1',
	maxLength : 10,
	width : 170
});
var txtbhmchfq2 = new Ext.form.TextField({
	name : 'bhmchfq2',
	renderTo : 'divbhmchfq2',
	maxLength : 10,
	width : 170
});
var txtbhmchfq3 = new Ext.form.TextField({
	name : 'bhmchfq3',
	renderTo : 'divbhmchfq3',
	maxLength : 10,
	width : 170
});
var txtbhmchfq4 = new Ext.form.TextField({
	name : 'bhmchfq4',
	renderTo : 'divbhmchfq4',
	maxLength : 10,
	width : 170
});
var txtbhmchfq5 = new Ext.form.TextField({
	name : 'bhmchfq5',
	renderTo : 'divbhmchfq5',
	maxLength : 10,
	width : 170
});

var txtzdzhfq1 = new Ext.form.TextField({
	name : 'zdzhfq1',
	renderTo : 'divzdzhfq1',
	maxLength : 10,
	width : 170
});
var txtzdzhfq2 = new Ext.form.TextField({
	name : 'zdzhfq2',
	renderTo : 'divzdzhfq2',
	maxLength : 10,
	width : 170
});
var txtzdzhfq3 = new Ext.form.TextField({
	name : 'zdzhfq3',
	renderTo : 'divzdzhfq3',
	maxLength : 10,
	width : 170
});
var txtzdzhfq4 = new Ext.form.TextField({
	name : 'zdzhfq4',
	renderTo : 'divzdzhfq4',
	maxLength : 10,
	width : 170
});
var txtzdzhfq5 = new Ext.form.TextField({
	name : 'zdzhfq5',
	renderTo : 'divzdzhfq5',
	maxLength : 10,
	width : 170
});

var txtbhmchfh1 = new Ext.form.TextField({
	name : 'bhmchfh1',
	renderTo : 'divbhmchfh1',
	maxLength : 10,
	width : 170
});
var txtbhmchfh2 = new Ext.form.TextField({
	name : 'bhmchfh2',
	renderTo : 'divbhmchfh2',
	maxLength : 10,
	width : 170
});
var txtbhmchfh3 = new Ext.form.TextField({
	name : 'bhmchfh3',
	renderTo : 'divbhmchfh3',
	maxLength : 10,
	width : 170
});
var txtbhmchfh4 = new Ext.form.TextField({
	name : 'bhmchfh4',
	renderTo : 'divbhmchfh4',
	maxLength : 10,
	width : 170
});
var txtbhmchfh5 = new Ext.form.TextField({
	name : 'bhmchfh5',
	renderTo : 'divbhmchfh5',
	maxLength : 10,
	width : 170
});
var txtzdzhfh1 = new Ext.form.TextField({
	name : 'zdzhfh1',
	renderTo : 'divzdzhfh1',
	maxLength : 10,
	width : 170
});
var txtzdzhfh2 = new Ext.form.TextField({
	name : 'zdzhfh2',
	renderTo : 'divzdzhfh2',
	maxLength : 10,
	width : 170
});
var txtzdzhfh3 = new Ext.form.TextField({
	name : 'zdzhfh3',
	renderTo : 'divzdzhfh3',
	maxLength : 10,
	width : 170
});
var txtzdzhfh4 = new Ext.form.TextField({
	name : 'zdzhfh4',
	renderTo : 'divzdzhfh4',
	maxLength : 10,
	width : 170
});
var txtzdzhfh5 = new Ext.form.TextField({
	name : 'zdzhfh5',
	renderTo : 'divzdzhfh5',
	maxLength : 10,
	width : 170
});

// 恢复原因
var txthfyy = new Ext.form.TextArea({
	name : 'hfyy',
	renderTo : 'divhfyy',
	maxLength : 200,
	width : 570
});

// 发令人
var txthfflr = new Ext.form.TextField({
	name : 'hfflr',
	renderTo : 'divhfflr',
	maxLength : 10,
	width : 150
});

// 执行人
var txthfzhr = new Ext.form.TextField({
	name : 'hfzhr',
	renderTo : 'divhfzhr',
	maxLength : 10,
	width : 150
});

// 运行检查
var txthfyhjc = new Ext.form.TextField({
	name : 'hfyhjc',
	renderTo : 'divhfyhjc',
	maxLength : 10,
	width : 150
});

// 备注
var txtbz = new Ext.form.TextField({
	name : 'bz',
	renderTo : 'divbz',
	maxLength : 25,
	width : 190
});

// 恢复时间
var dthfsj = new Ext.form.DateField({
	name : 'hfsj',
	renderTo : 'divhfsj',
	format : 'Y-m-d H:i',
	menu : new DatetimeMenu(),
	readOnly : true,
	width : 200,
	listeners : {
		change : function(dt, newValue, oldValue) {
			var dateFormat = 'yyyy-MM-dd HH:mm';
			var hzsj = newValue ? formatDate(new Date(newValue), dateFormat) : "";
			var now = formatDate(new Date(), dateFormat);
			if (compareDates(hzsj, dateFormat, now, dateFormat) == 1) {
				alert('更改时间不能大于当前时间！');
				this.setValue(oldValue);
			}
		}
	}
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
				txtbhmc.setValue(data.bhmc);
				dtggsj.setValue(formatDate(new Date(data.ggsj['time']), "yyyy-MM-dd hh:mm"));
				txtbhmcgq1.setValue(data.bhmcgq1);
				txtbhmcgq2.setValue(data.bhmcgq2);
				txtbhmcgq3.setValue(data.bhmcgq3);
				txtbhmcgq4.setValue(data.bhmcgq4);
				txtbhmcgq5.setValue(data.bhmcgq5);
				txtzdzgq1.setValue(data.zdzgq1);
				txtzdzgq2.setValue(data.zdzgq2);
				txtzdzgq3.setValue(data.zdzgq3);
				txtzdzgq4.setValue(data.zdzgq4);
				txtzdzgq5.setValue(data.zdzgq5);
				txtbhmcgh1.setValue(data.bhmcgh1);
				txtbhmcgh2.setValue(data.bhmcgh2);
				txtbhmcgh3.setValue(data.bhmcgh3);
				txtbhmcgh4.setValue(data.bhmcgh4);
				txtbhmcgh5.setValue(data.bhmcgh5);
				txtzdzgh1.setValue(data.zdzgh1);
				txtzdzgh2.setValue(data.zdzgh2);
				txtzdzgh3.setValue(data.zdzgh3);
				txtzdzgh4.setValue(data.zdzgh4);
				txtzdzgh5.setValue(data.zdzgh5);
				txtggyy.setValue(data.ggyy);
				txtggflr.setValue(data.ggflr);
				txtggzhr.setValue(data.ggzhr);
				txtggyhjc.setValue(data.ggyhjc);
				txtbhmchfq1.setValue(data.bhmchfq1);
				txtbhmchfq2.setValue(data.bhmchfq2);
				txtbhmchfq3.setValue(data.bhmchfq3);
				txtbhmchfq4.setValue(data.bhmchfq4);
				txtbhmchfq5.setValue(data.bhmchfq5);
				txtzdzhfq1.setValue(data.zdzhfq1);
				txtzdzhfq2.setValue(data.zdzhfq2);
				txtzdzhfq3.setValue(data.zdzhfq3);
				txtzdzhfq4.setValue(data.zdzhfq4);
				txtzdzhfq5.setValue(data.zdzhfq5);
				txtbhmchfh1.setValue(data.bhmchfh1);
				txtbhmchfh2.setValue(data.bhmchfh2);
				txtbhmchfh3.setValue(data.bhmchfh3);
				txtbhmchfh4.setValue(data.bhmchfh4);
				txtbhmchfh5.setValue(data.bhmchfh5);
				txtzdzhfh1.setValue(data.zdzhfh1);
				txtzdzhfh2.setValue(data.zdzhfh2);
				txtzdzhfh3.setValue(data.zdzhfh3);
				txtzdzhfh4.setValue(data.zdzhfh4);
				txtzdzhfh5.setValue(data.zdzhfh5);
				txthfyy.setValue(data.hfyy);
				txthfflr.setValue(data.hfflr);
				txthfzhr.setValue(data.hfzhr);
				txthfyhjc.setValue(data.hfyhjc);
				txtbz.setValue(data.bz);
				if (data.hfsj) {
					dthfsj.setValue(formatDate(new Date(data.hfsj['time']), "yyyy-MM-dd hh:mm"));
				}
			}
		},
		failure : function() {

		}
	})
}
// 保存
function save() {
	if (!txtbhmc.isValid()) {
		alert('设备名称验证不正确');
		return;
	}
	if (!dtggsj.isValid()) {
		alert('更改时间不能为空');
		return;
	}
	if (!txtbhmcgq1.isValid()) {
		alert('保护名称（改前）1验证不正确');
		return;
	}
	if (!txtbhmcgq2.isValid()) {
		alert('保护名称（改前）2验证不正确');
		return;
	}
	if (!txtbhmcgq3.isValid()) {
		alert('保护名称（改前）3验证不正确');
		return;
	}
	if (!txtbhmcgq4.isValid()) {
		alert('保护名称（改前）4验证不正确');
		return;
	}
	if (!txtbhmcgq5.isValid()) {
		alert('保护名称（改前）5验证不正确');
		return;
	}
	if (!txtzdzgq1.isValid()) {
		alert('整定值（改前）1验证不正确');
		return;
	}
	if (!txtzdzgq2.isValid()) {
		alert('整定值（改前）2验证不正确');
		return;
	}
	if (!txtzdzgq3.isValid()) {
		alert('整定值（改前）3验证不正确');
		return;
	}
	if (!txtzdzgq4.isValid()) {
		alert('整定值（改前）4验证不正确');
		return;
	}
	if (!txtzdzgq5.isValid()) {
		alert('整定值（改前）5验证不正确');
		return;
	}
	if (!txtbhmcgh1.isValid()) {
		alert('保护名称（改后）1验证不正确');
		return;
	}
	if (!txtbhmcgh2.isValid()) {
		alert('保护名称（改后）2验证不正确');
		return;
	}
	if (!txtbhmcgh3.isValid()) {
		alert('保护名称（改后）3验证不正确');
		return;
	}
	if (!txtbhmcgh4.isValid()) {
		alert('保护名称（改后）4验证不正确');
		return;
	}
	if (!txtbhmcgh5.isValid()) {
		alert('保护名称（改后）5验证不正确');
		return;
	}
	if (!txtzdzgh1.isValid()) {
		alert('整定值（改后）1验证不正确');
		return;
	}
	if (!txtzdzgh2.isValid()) {
		alert('整定值（改后）2验证不正确');
		return;
	}
	if (!txtzdzgh3.isValid()) {
		alert('整定值（改后）3验证不正确');
		return;
	}
	if (!txtzdzgh4.isValid()) {
		alert('整定值（改后）4验证不正确');
		return;
	}
	if (!txtzdzgh5.isValid()) {
		alert('整定值（改后）5验证不正确');
		return;
	}
	if (!txtggyy.isValid()) {
		alert('更改原因验证不正确');
		return;
	}
	if (!txtggflr.isValid()) {
		alert('发令人验证不正确');
		return;
	}
	if (!txtggzhr.isValid()) {
		alert('执行人验证不正确');
		return;
	}
	if (!txtggyhjc.isValid()) {
		alert('运行检查验证不正确');
		return;
	}
	if (!txtbhmchfq1.isValid()) {
		alert('保护名称（恢复前）1验证不正确');
		return;
	}
	if (!txtbhmchfq2.isValid()) {
		alert('保护名称（恢复前）2验证不正确');
		return;
	}
	if (!txtbhmchfq3.isValid()) {
		alert('保护名称（恢复前）3验证不正确');
		return;
	}
	if (!txtbhmchfq4.isValid()) {
		alert('保护名称（恢复前）4验证不正确');
		return;
	}
	if (!txtbhmchfq5.isValid()) {
		alert('保护名称（恢复前）5验证不正确');
		return;
	}
	if (!txtzdzhfq1.isValid()) {
		alert('整定值（恢复前）1验证不正确');
		return;
	}
	if (!txtzdzhfq2.isValid()) {
		alert('整定值（恢复前）2验证不正确');
		return;
	}
	if (!txtzdzhfq3.isValid()) {
		alert('整定值（恢复前）3验证不正确');
		return;
	}
	if (!txtzdzhfq4.isValid()) {
		alert('整定值（恢复前）4验证不正确');
		return;
	}
	if (!txtzdzhfq5.isValid()) {
		alert('整定值（恢复前）5验证不正确');
		return;
	}
	if (!txtbhmchfh1.isValid()) {
		alert('保护名称（恢复后）1验证不正确');
		return;
	}
	if (!txtbhmchfh2.isValid()) {
		alert('保护名称（恢复后）2验证不正确');
		return;
	}
	if (!txtbhmchfh3.isValid()) {
		alert('保护名称（恢复后）3验证不正确');
		return;
	}
	if (!txtbhmchfh4.isValid()) {
		alert('保护名称（恢复后）4验证不正确');
		return;
	}
	if (!txtbhmchfh5.isValid()) {
		alert('保护名称（恢复后）5验证不正确');
		return;
	}
	if (!txtzdzhfh1.isValid()) {
		alert('整定值（恢复后）1验证不正确');
		return;
	}
	if (!txtzdzhfh2.isValid()) {
		alert('整定值（恢复后）2验证不正确');
		return;
	}
	if (!txtzdzhfh3.isValid()) {
		alert('整定值（恢复后）3验证不正确');
		return;
	}
	if (!txtzdzhfh4.isValid()) {
		alert('整定值（恢复后）4验证不正确');
		return;
	}
	if (!txtzdzhfh5.isValid()) {
		alert('整定值（恢复后）5验证不正确');
		return;
	}
	if (!txthfyy.isValid()) {
		alert('恢复原因验证不正确');
		return;
	}
	if (!txthfflr.isValid()) {
		alert('恢复发令人验证不正确');
		return;
	}
	if (!txthfzhr.isValid()) {
		alert('恢复执行人验证不正确');
		return;
	}
	if (!txthfyhjc.isValid()) {
		alert('恢复运行检查验证不正确');
		return;
	}
	if (!txtbz.isValid()) {
		alert('备注验证不正确');
		return;
	}
	myMask.show();
	var hfsj = dthfsj.getValue() ? formatDate(new Date(dthfsj.getValue()), "yyyy-MM-dd hh:mm") : "";
	var ggsj = dtggsj.getValue() ? formatDate(new Date(dtggsj.getValue()), "yyyy-MM-dd hh:mm") : "";
	Ext.Ajax.request({
		url : link6,
		method : 'POST',
		params : {
			id : hdnId.getValue(),
			bhmc : txtbhmc.getValue(),
			ggsj : ggsj,
			bhmcgq1 : txtbhmcgq1.getValue(),
			bhmcgq2 : txtbhmcgq2.getValue(),
			bhmcgq3 : txtbhmcgq3.getValue(),
			bhmcgq4 : txtbhmcgq4.getValue(),
			bhmcgq5 : txtbhmcgq5.getValue(),
			zdzgq1 : txtzdzgq1.getValue(),
			zdzgq2 : txtzdzgq2.getValue(),
			zdzgq3 : txtzdzgq3.getValue(),
			zdzgq4 : txtzdzgq4.getValue(),
			zdzgq5 : txtzdzgq5.getValue(),
			bhmcgh1 : txtbhmcgh1.getValue(),
			bhmcgh2 : txtbhmcgh2.getValue(),
			bhmcgh3 : txtbhmcgh3.getValue(),
			bhmcgh4 : txtbhmcgh4.getValue(),
			bhmcgh5 : txtbhmcgh5.getValue(),
			zdzgh1 : txtzdzgh1.getValue(),
			zdzgh2 : txtzdzgh2.getValue(),
			zdzgh3 : txtzdzgh3.getValue(),
			zdzgh4 : txtzdzgh4.getValue(),
			zdzgh5 : txtzdzgh5.getValue(),
			ggyy : txtggyy.getValue(),
			ggflr : txtggflr.getValue(),
			ggzhr : txtggzhr.getValue(),
			ggyhjc : txtggyhjc.getValue(),
			bhmchfq1 : txtbhmchfq1.getValue(),
			bhmchfq2 : txtbhmchfq2.getValue(),
			bhmchfq3 : txtbhmchfq3.getValue(),
			bhmchfq4 : txtbhmchfq4.getValue(),
			bhmchfq5 : txtbhmchfq5.getValue(),
			zdzhfq1 : txtzdzhfq1.getValue(),
			zdzhfq2 : txtzdzhfq2.getValue(),
			zdzhfq3 : txtzdzhfq3.getValue(),
			zdzhfq4 : txtzdzhfq4.getValue(),
			zdzhfq5 : txtzdzhfq5.getValue(),
			bhmchfh1 : txtbhmchfh1.getValue(),
			bhmchfh2 : txtbhmchfh2.getValue(),
			bhmchfh3 : txtbhmchfh3.getValue(),
			bhmchfh4 : txtbhmchfh4.getValue(),
			bhmchfh5 : txtbhmchfh5.getValue(),
			zdzhfh1 : txtzdzhfh1.getValue(),
			zdzhfh2 : txtzdzhfh2.getValue(),
			zdzhfh3 : txtzdzhfh3.getValue(),
			zdzhfh4 : txtzdzhfh4.getValue(),
			zdzhfh5 : txtzdzhfh5.getValue(),
			hfyy : txthfyy.getValue(),
			hfflr : txthfflr.getValue(),
			hfzhr : txthfzhr.getValue(),
			hfyhjc : txthfyhjc.getValue(),
			bz : txtbz.getValue(),
			hfsj : hfsj
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
