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
	renderTo : 'divid'
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

// 接地线编号
var txtJdxbh = new Ext.form.TextField({
	name : 'jdxbh',
	renderTo : 'divjdxbh',
	allowBlank : false,
	maxLength : 32,
	width : 170
});

// 装设地点
var txtZsdd = new Ext.form.TextArea({
	name : 'zsdd',
	renderTo : 'divzsdd',
	height:120,
	maxLength : 200,
	width : 440
});

// 装设时间
var dtZssj = new Ext.form.DateField({
	name : 'zssj',
	renderTo : 'divzssj',
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
				alert('装设时间不能大于当前时间！');
				this.setValue(oldValue);
			}
		}
	}
});

// 装设人
var txtZsr = new Ext.form.TextField({
	name : 'zsr',
	renderTo : 'divzsr',
	maxLength : 16,
	width : 170
});

// 拆除时间
var dtccsj = new Ext.form.DateField({
	name : 'ccsj',
	renderTo : 'divccsj',
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
				alert('拆除时间不能大于当前时间！');
				this.setValue(oldValue);
			}
		}
	}
});

// 拆除人
var txtccr = new Ext.form.TextField({
	name : 'ccr',
	renderTo : 'divccr',
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
				txtJdxbh.setValue(data.jdxbh);
				cmbJz.setValue(data.jz);
				txtZsdd.setValue(data.zsdd);
				dtZssj.setValue(formatDate(new Date(data.zssj['time']), "yyyy-MM-dd hh:mm"));
				txtZsr.setValue(data.zsr);
				txtccr.setValue(data.ccr);
				if (data.ccsj) {
					dtccsj.setValue(formatDate(new Date(data.ccsj['time']), "yyyy-MM-dd hh:mm"));
				}
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
	if (!txtJdxbh.isValid()) {
		alert('接地线编号验证不正确');
		return;
	}
	if (!txtZsdd.isValid()) {
		alert('装设地点验证不正确');
		return;
	}
	if (!dtZssj.isValid()) {
		alert('装设时间不能为空');
		return;
	}
	if (!txtZsr.isValid()) {
		alert('装设人验证不正确');
		return;
	}
	if (!txtccr.isValid()) {
		alert('拆除人验证不正确');
		return;
	}
	myMask.show();
	var zssj = dtZssj.getValue()?formatDate(new Date(dtZssj.getValue()), "yyyy-MM-dd hh:mm"):"";
	var ccsj = dtccsj.getValue()?formatDate(new Date(dtccsj.getValue()), "yyyy-MM-dd hh:mm"):"";
	Ext.Ajax.request({
		url : link6,
		method : 'POST',
		params : {
			id : hdnId.getValue(),
			jz : cmbJz.getValue(),
			jdxbh : txtJdxbh.getValue(),
			zsdd : txtZsdd.getValue(),
			zssj : zssj,
			zsr : txtZsr.getValue(),
			ccsj : ccsj,
			ccr : txtccr.getValue()
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
