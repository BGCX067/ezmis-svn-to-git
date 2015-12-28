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

//jz comboBox
var distsJz = $dictListAjax("jizu");

var dsJz = new Ext.data.Store({
	reader : new Ext.data.JsonReader({
		root : 'rows',
		id : 'id'
	}, ['key', 'value', 'id']),
	data : {
		rows : distsJz
	}
});

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

	

	
// 开始时间
var dtKssj = new Ext.form.DateField({
	name : 'kssj',
	renderTo : 'divkssj',
	format : 'Y-m-d H:i',
	menu : new DatetimeMenu(),
	readOnly : false,
	width : 170,
	listeners : {
		blur : function(t) {
			if (dtJssj.getValue() != '' && dtKssj.getValue() != '' && dtJssj.getValue() <= dtKssj.getValue()) {
				alert('结束时间必须大于开始时间！');
				return;
			}
			if (dtJssj.getValue() != '' && dtKssj.getValue() != '') {
				var yxsj = (dtJssj.getValue() - dtKssj.getValue()) / 3600000;
				nmYxsj.setValue(yxsj);
			}
		},
		change : function(dt, newValue, oldValue) {
			var dateFormat = 'yyyy-MM-dd HH:mm';
			var hzsj = newValue ? formatDate(new Date(newValue), dateFormat) : "";
			var now = formatDate(new Date(), dateFormat);
			if (compareDates(hzsj, dateFormat, now, dateFormat) == 1) {
				alert('开始时间不能大于当前时间！');
				this.setValue(oldValue);
			}
		}

	}
});

// 开始氢压P1
var nmKsqy = new Ext.form.NumberField({
	name : 'ksqy',
	renderTo : 'divksqy',
	style : 'text-align:right',
	allowBlank : false,
	allowNegative : false,
	decimalPrecision : 6,
	width : 140
})

// 氢温t1
var nmKsqw = new Ext.form.NumberField({
	name : 'ksqw',
	renderTo : 'divksqw',
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
	format : 'Y-m-d H:i',
	menu : new DatetimeMenu(),
	readOnly : false,
	width : 170,
	listeners : {
		blur : function(t) {
			if (dtJssj.getValue() != '' && dtKssj.getValue() != '' && dtJssj.getValue() <= dtKssj.getValue()) {
				alert('结束时间必须大于开始时间！');
				return;
			}
			if (dtJssj.getValue() != '' && dtKssj.getValue() != '') {
				var yxsj = (dtJssj.getValue() - dtKssj.getValue()) / 3600000;
				nmYxsj.setValue(yxsj);
			}
		}
	}
});

// 结束氢压P2
var nmJsqy = new Ext.form.NumberField({
	name : 'jsqy',
	renderTo : 'divjsqy',
	style : 'text-align:right',
	allowBlank : true,
	allowNegative : false,
	decimalPrecision : 6,
	width : 140
})

// 氢温t2
var nmJsqw = new Ext.form.NumberField({
	name : 'jsqw',
	renderTo : 'divjsqw',
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
	decimalPrecision : 6,
	width : 140
})

// 漏氢率
var nmLqlv = new Ext.form.NumberField({
	name : 'lqlv',
	renderTo : 'divlqlv',
	style : 'text-align:right',
	readOnly : true,
	decimalPrecision : 6,
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
				if(data.kssj)
				dtKssj.setValue(formatDate(new Date(data.kssj['time']), "yyyy-MM-dd hh:mm"));
				cmbJz.setValue(data.jz);
				nmKsqy.setValue(data.ksqy);
				nmKsqw.setValue(data.ksqw);
				if(data.jssj)
				dtJssj.setValue(formatDate(new Date(data.jssj['time']), "yyyy-MM-dd hh:mm"));
				nmJsqy.setValue(data.jsqy);
				nmJsqw.setValue(data.jsqw);
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
	if(checkValidation())  calLql2();
		
	myMask.show();
	var kssj = dtKssj.getValue() ? formatDate(new Date(dtKssj.getValue()), "yyyy-MM-dd hh:mm") : "";
	var jssj = dtJssj.getValue() ? formatDate(new Date(dtJssj.getValue()), "yyyy-MM-dd hh:mm") : "";
	Ext.Ajax.request({
		url : link6,
		method : 'POST',
		params : {
			id : hdnId.getValue(),
			jz : cmbJz.getValue(),
			kssj : kssj,
			jssj : jssj,
			ksqy : nmKsqy.getValue(),
			ksqw : nmKsqw.getValue(),
			jsqy : nmJsqy.getValue(),
			jsqw : nmJsqw.getValue(),
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

function checkValidation(){
	return  !(nmYxsj.getValue() == null || nmYxsj.getValue() == '')
	        &&dtKssj.isValid()
	        &&dtJssj.isValid()
			&&nmKsqy.isValid()
			&&nmKsqw.isValid()
			&&nmJsqy.isValid()
			&&nmJsqw.isValid()
			;
}
function calLql(){
	if(!checkValidation()){
		alert("请填写所有参与计算的信息");
	}else{
		calLql2();
	}

}

function calLql2() {
	var p1 = nmKsqy.getValue();
	var t1 = nmKsqw.getValue();
	var p2 = nmJsqy.getValue();
	var t2 = nmJsqw.getValue();
	var h = nmYxsj.getValue();
	var jz = cmbJz.getValue();
	var lqlv=70320/h*((p1+0.1)/(t1+273)-(p2+0.1)/(t2+273));
	var lql=(jz.indexOf("1")!=-1&&jz.indexOf("2")!=-1)?lqlv*90:lqlv*72;
	nmLql.setValue(lql.toFixed(6));
	nmLqlv.setValue(lqlv.toFixed(6));
}

function formatFloat(src, pos) {
	return Math.round(src * Math.pow(10, pos)) / Math.pow(10, pos);
}