var myMaskLoad = new Ext.LoadMask(Ext.getBody(), {
	msg : "正在加载，请稍候..."
});
// ************** 页面设计 ****************//
var hdnId = new Ext.form.Hidden({
	name : 'id',
	renderTo : 'id'
})

var cmbJzlx = new Ext.form.TextField({
	hiddenName : 'jzlx',
	renderTo : 'divjzlx',
	readOnly : true
})

var cmbKgmc = new Ext.form.TextField({
	hiddenName : 'kgmc',
	renderTo : 'divkgmc',
	readOnly : true,
	width : 170
})

// 合闸时间
var dtHzsj = new Ext.form.TextField({
	name : 'hzsj',
	renderTo : 'divhzsj',
	readOnly : true,
	width : 170
});

// 合闸记录人
var txtHzjlr = new Ext.form.TextField({
	name : 'hzjlr',
	renderTo : 'divhzjlr',
	allowBlank : false,
	readOnly : true,
	maxLength : 16,
	width : 170
});

// 合闸原因
var txtHzyy = new Ext.form.TextArea({
	name : 'hzyy',
	renderTo : 'divhzyy',
	height:130,
	maxLength : 400,
	readOnly : true,
	width : 450
})

// 分闸时间
var dtFzsj = new Ext.form.TextField({
	name : 'fzsj',
	renderTo : 'divfzsj',
	readOnly : true,
	width : 170
});

// 分闸记录人
var txtFzjlr = new Ext.form.TextField({
	name : 'fzjlr',
	renderTo : 'divfzjlr',
	maxLength : 16,
	readOnly : true,
	width : 170
});

// 分闸原因
var txtFzyy = new Ext.form.TextArea({
	name : 'fzyy',
	renderTo : 'divfzyy',
	height:130,
	maxLength : 400,
	readOnly : true,
	width : 450
})

// 运行时间
var dtYxsj = new Ext.form.TextField({
	name : 'yxsj',
	renderTo : 'divyxsj',
	style : 'text-align:right',
	readOnly : true,
	width : 170
});

// 动作次数
var nmDzcs = new Ext.form.NumberField({
	name : 'dzcs',
	renderTo : 'divdzcs',
	style : 'text-align:right',
	allowNegative : false,
	decimalPrecision : 0,
	readOnly : true,
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
