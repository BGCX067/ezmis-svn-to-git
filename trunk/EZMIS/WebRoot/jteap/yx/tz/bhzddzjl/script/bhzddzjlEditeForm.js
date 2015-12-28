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
var txtSbmc = new Ext.form.TextField({
	name : 'sbmc',
	renderTo : 'divsbmc',
	allowBlank : false,
	maxLength : 50,
	width : 480
});

// 动作时间
var dtDzsj = new Ext.form.DateField({
	name : 'dzsj',
	renderTo : 'divdzsj',
	format : 'Y-m-d H:i',
	menu:new DatetimeMenu(),
	allowBlank : false,
	readOnly : true,
	width : 170,
	listeners : {
		change : function(dt, newValue, oldValue) {
			var dateFormat = 'yyyy-MM-dd HH:mm';
			var hzsj = newValue ? formatDate(new Date(newValue), dateFormat) : "";
			var now = formatDate(new Date(), dateFormat);
			if (compareDates(hzsj, dateFormat, now, dateFormat) == 1) {
				alert('动作时间不能大于当前时间！');
				this.setValue(oldValue);
			}
		}
	}
});

// 保护名称及吊牌
var txtBhmc = new Ext.form.TextArea({
	name : 'bhmc',
	renderTo : 'divbhmc',
	allowBlank : false,
	maxLength : 200,
	width : 480
});

// 光字牌信号
var txtGzpxh = new Ext.form.TextArea({
	name : 'gzpxh',
	renderTo : 'divgzpxh',
	allowBlank : false,
	maxLength : 200,
	width : 480
});

// 检查人
var txtJcr = new Ext.form.TextField({
	name : 'jcr',
	renderTo : 'divjcr',
	allowBlank : false,
	maxLength : 16,
	width : 170
});

// 复归人
var txtFgr = new Ext.form.TextField({
	name : 'fgr',
	renderTo : 'divfgr',
	allowBlank : false,
	maxLength : 16,
	width : 170
});

// 备注
var txtBz = new Ext.form.TextField({
	name : 'bz',
	renderTo : 'divbz',
	maxLength : 50,
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
				dtDzsj.setValue(formatDate(new Date(data.dzsj['time']), "yyyy-MM-dd hh:mm"));
				txtSbmc.setValue(data.sbmc);
				txtBhmc.setValue(data.bhmc);
				txtGzpxh.setValue(data.gzpxh);
				txtJcr.setValue(data.jcr);
				txtFgr.setValue(data.fgr);
				txtBz.setValue(data.bz);
			}
		},
		failure : function() {

		}
	})
}
// 保存
function save() {
	if (!dtDzsj.isValid()) {
		alert('动作时间不能为空');
		return;
	}
	if (!txtSbmc.isValid()) {
		alert('设备名称验证不正确');
		return;
	}
	if (!txtBhmc.isValid()) {
		alert('保护名称验证不正确');
		return;
	}
	if (!txtGzpxh.isValid()) {
		alert('光字牌信号验证不正确');
		return;
	}
	if (!txtJcr.isValid()) {
		alert('检查人验证不正确');
		return;
	}
	if (!txtFgr.isValid()) {
		alert('复归人验证不正确');
		return;
	}
	if (!txtBz.isValid()) {
		alert('备注验证不正确');
		return;
	}
	myMask.show();
	var dzsj = dtDzsj.getValue()?formatDate(new Date(dtDzsj.getValue()), "yyyy-MM-dd hh:mm"):"";
	Ext.Ajax.request({
		url : link6,
		method : 'POST',
		params : {
			id : hdnId.getValue(),
			dzsj : dzsj,
			sbmc : txtSbmc.getValue(),
			bhmc : txtBhmc.getValue(),
			gzpxh : txtGzpxh.getValue(),
			jcr : txtJcr.getValue(),
			fgr : txtFgr.getValue(),
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
