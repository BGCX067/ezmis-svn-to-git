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

// 借用时间
var dtJysj = new Ext.form.DateField({
	name : 'jysj',
	renderTo : 'divjysj',
	format : 'Y-m-d H:i',
	menu:new DatetimeMenu(),
	allowBlank : false,
	readOnly : true,
	width : 170,
	listeners : {
		change : function(dt, newValue, oldValue) {
			var dateFormat = 'yyyy-MM-dd HH:mm';
			var hzsj =  newValue ? formatDate(new Date(newValue), dateFormat) : "";
			var now =  formatDate(new Date(), dateFormat);
			if (compareDates(hzsj, dateFormat, now, dateFormat) == 1) {
				alert('借用时间不能大于当前时间！');
				this.setValue(oldValue);
			}
		}
	}
});

// 借用钥匙
var txtJyys = new Ext.form.TextField({
	name : 'jyys',
	renderTo : 'divjyys',
	allowBlank : false,
	maxLength : 25,
	width : 170
});

// 借用原因
var txtJyyy = new Ext.form.TextArea({
	name : 'jyyy',
	renderTo : 'divjyyy',
	allowBlank : false,
	height:120,
	maxLength : 200,
	width : 450
});

// 借用人
var txtJyr = new Ext.form.TextField({
	name : 'jyr',
	renderTo : 'divjyr',
	allowBlank : false,
	maxLength : 16,
	width : 170
})

// 借出值班员
var txtJczby = new Ext.form.TextField({
	name : 'jczby',
	renderTo : 'divjczby',
	allowBlank : false,
	maxLength : 16,
	width : 170
})

// 借出值长
var txtJczz = new Ext.form.TextField({
	name : 'jczz',
	renderTo : 'divjczz',
	allowBlank : false,
	maxLength : 16,
	width : 170
})

// 归还时间
var dtGhsj = new Ext.form.DateField({
	name : 'ghsj',
	renderTo : 'divghsj',
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
				alert('归还时间不能大于当前时间！');
				this.setValue(oldValue);
			}
		}
	}
});

// 归还人
var txtGhr = new Ext.form.TextField({
	name : 'ghr',
	renderTo : 'divghr',
	maxLength : 16,
	width : 170
});

// 收回值班员
var txtShzby = new Ext.form.TextField({
	name : 'shzby',
	renderTo : 'divshzby',
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
				dtJysj.setValue(formatDate(new Date(data.jysj['time']), "yyyy-MM-dd hh:mm"));
				txtJyys.setValue(data.jyys);
				txtJyyy.setValue(data.jyyy);
				txtJyr.setValue(data.jyr);
				txtJczby.setValue(data.jczby);
				txtJczz.setValue(data.jczz);
				if (data.ghsj) {
					dtGhsj.setValue(formatDate(new Date(data.ghsj['time']), "yyyy-MM-dd hh:mm"));
				}
				txtGhr.setValue(data.ghr);
				txtShzby.setValue(data.shzby);
			}
		},
		failure : function() {

		}
	})
}
// 保存
function save() {
	if (!dtJysj.isValid()) {
		alert('借用时间不能为空');
		return;
	}
	if (!txtJyys.isValid()) {
		alert('借用钥匙验证不正确');
		return;
	}
	if (!txtJyyy.isValid()) {
		alert('借用原因验证不正确');
		return;
	}
	if (!txtJyr.isValid()) {
		alert('借用人验证不正确');
		return;
	}
	if (!txtJczby.isValid()) {
		alert('借出值班员验证不正确');
		return;
	}
	if (!txtJczz.isValid()) {
		alert('借出值长验证不正确');
		return;
	}
	myMask.show();
	var jysj = dtJysj.getValue()?formatDate(new Date(dtJysj.getValue()), "yyyy-MM-dd hh:mm"):"";
	var ghsj = dtGhsj.getValue()?formatDate(new Date(dtGhsj.getValue()), "yyyy-MM-dd hh:mm"):"";
	Ext.Ajax.request({
		url : link6,
		method : 'POST',
		params : {
			id : hdnId.getValue(),
			jysj : jysj,
			jyys : txtJyys.getValue(),
			jyyy : txtJyyy.getValue(),
			jyr : txtJyr.getValue(),
			ghsj : ghsj,
			jczby : txtJczby.getValue(),
			jczz : txtJczz.getValue(),
			ghr : txtGhr.getValue(),
			shzby : txtShzby.getValue()
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
