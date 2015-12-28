var distsJz = $dictListAjax("jizu");

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

var dsJz = new Ext.data.Store({
	reader : new Ext.data.JsonReader({
		root : 'rows',
		id : 'id'
	}, ['key', 'value', 'id']),
	data : {
		rows : distsJz
	}
})
var cmbJz = new Ext.form.ComboBox({
	hiddenName : 'jz',
	renderTo : 'divjz',
	store : dsJz,
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
	emptyText : '请选择机组',
	width : 170
})

// 保护名称
var txtBhmc = new Ext.form.TextField({
	name : 'bhmc',
	renderTo : 'divbhmc',
	allowBlank : false,
	maxLength : 50,
	width : 170
});

// 停用时间
var dtTysj = new Ext.form.DateField({
	name : 'tysj',
	renderTo : 'divtysj',
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
				alert('停用时间不能大于当前时间！');
				this.setValue(oldValue);
			}
		}
	}
});

// 执行人
var txtTyZxr = new Ext.form.TextField({
	name : 'tyzxr',
	renderTo : 'divtyzxr',
	maxLength : 16,
	width : 170
});

// 停用值班员
var txtTyzby = new Ext.form.TextField({
	name : 'tyzby',
	renderTo : 'divtyzby',
	maxLength : 16,
	width : 170
});

// 停用原因
var txtTyyy = new Ext.form.TextArea({
	name : 'tyyy',
	renderTo : 'divtyyy',
	height:100,
	maxLength : 200,
	width : 450
})

// 加用时间
var dtJysj = new Ext.form.DateField({
	name : 'jysj',
	renderTo : 'divjysj',
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
				alert('加用时间不能大于当前时间！');
				this.setValue(oldValue);
			}
		}
	}
});

// 加用执行人
var txtJyZxr = new Ext.form.TextField({
	name : 'jyzxr',
	renderTo : 'divjyzxr',
	maxLength : 16,
	width : 170
});

// 加用值班员
var txtJyzby = new Ext.form.TextField({
	name : 'jyzby',
	renderTo : 'divjyzby',
	maxLength : 16,
	width : 170
});

// 加用原因
var txtJyyy = new Ext.form.TextArea({
	name : 'jyyy',
	renderTo : 'divjyyy',
	height:100,
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
				cmbJz.setValue(data.jz);
				txtBhmc.setValue(data.bhmc);
				if (data.tysj) {
					dtTysj.setValue(formatDate(new Date(data.tysj['time']), "yyyy-MM-dd hh:mm"));
				}
				txtTyZxr.setValue(data.tyzxr);
				txtTyzby.setValue(data.tyzby);
				txtTyyy.setValue(data.tyyy);
				if (data.jysj) {
					dtJysj.setValue(formatDate(new Date(data.jysj['time']), "yyyy-MM-dd hh:mm"));
				}
				txtJyZxr.setValue(data.jyzxr);
				txtJyzby.setValue(data.jyzby);
				txtJyyy.setValue(data.jyyy);
			}
		},
		failure : function() {

		}
	})
}
// 保存
function save() {
	if (!cmbJz.isValid()) {
		alert('机组不能为空');
		return;
	}
	if (!txtBhmc.isValid()) {
		alert('保护名称不能为空');
		return;
	}
	if (!txtTyZxr.isValid()) {
		alert('停用执行人验证不正确');
		return;
	}
	if (!txtTyzby.isValid()) {
		alert('停用值班员验证不正确');
		return;
	}
	if (!txtTyyy.isValid()) {
		alert('停用原因验证不正确');
		return;
	}
	if (!txtJyZxr.isValid()) {
		alert('加用执行人验证不正确');
		return;
	}
	if (!txtJyzby.isValid()) {
		alert('加用值班员验证不正确');
		return;
	}
	if (!txtJyyy.isValid()) {
		alert('加用原因验证不正确');
		return;
	}
	myMask.show();
	var jysj = dtJysj.getValue()?formatDate(new Date(dtJysj.getValue()), "yyyy-MM-dd hh:mm"):"";
	var tysj = dtTysj.getValue()?formatDate(new Date(dtTysj.getValue()), "yyyy-MM-dd hh:mm"):"";
	Ext.Ajax.request({
		url : link6,
		method : 'POST',
		params : {
			id : hdnId.getValue(),
			jz : cmbJz.getValue(),
			bhmc : txtBhmc.getValue(),
			tyyy : txtTyyy.getValue(),
			tysj : tysj,
			tyzxr : txtTyZxr.getValue(),
			tyzby : txtTyzby.getValue(),
			jysj : jysj,
			jyzxr : txtJyZxr.getValue(),
			jyzby : txtJyzby.getValue(),
			jyyy : txtJyyy.getValue()
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
