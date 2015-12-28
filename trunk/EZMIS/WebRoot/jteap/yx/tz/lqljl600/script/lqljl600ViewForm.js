var myMaskLoad = new Ext.LoadMask(Ext.getBody(), {
	msg : "正在加载，请稍候..."
});
// ************** 页面设计 ****************//
var hdnId = new Ext.form.Hidden({
	name : 'id',
	renderTo : 'id'
})

// 开始时间
var dtKssj = new Ext.form.TextField({
	name : 'kssj',
	renderTo : 'divkssj',
	readOnly : true,
	width : 170
});

// 开始氢压P1
var nmKsqyp1 = new Ext.form.NumberField({
	name : 'ksqyp1',
	renderTo : 'divksqyp1',
	style : 'text-align:right',
	readOnly : true,
	width : 140
})

// 氢温t1
var nmQwxt1 = new Ext.form.NumberField({
	name : 'qwxt1',
	renderTo : 'divqwxt1',
	style : 'text-align:right',
	readOnly : true,
	width : 140
})

// 氢温t2
var nmQwxt2 = new Ext.form.NumberField({
	name : 'qwxt2',
	renderTo : 'divqwxt2',
	style : 'text-align:right',
	readOnly : true,
	width : 140
})

// 氢温T1
var nmQwdt1 = new Ext.form.NumberField({
	name : 'qwdt1',
	renderTo : 'divqwdt1',
	style : 'text-align:right',
	readOnly : true,
	width : 140
})

// 结束时间
var dtJssj = new Ext.form.TextField({
	name : 'jssj',
	renderTo : 'divjssj',
	readOnly : true,
	width : 170
});

// 结束氢压P2
var nmJsqyp2 = new Ext.form.NumberField({
	name : 'jsqyp2',
	renderTo : 'divjsqyp2',
	style : 'text-align:right',
	readOnly : true,
	width : 140
})

// 氢温T2
var nmQwdt2 = new Ext.form.NumberField({
	name : 'qwdt2',
	renderTo : 'divqwdt2',
	style : 'text-align:right',
	readOnly : true,
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
	width : 140
})

// 漏氢率
var nmLqlv = new Ext.form.NumberField({
	name : 'lqlv',
	renderTo : 'divlqlv',
	style : 'text-align:right',
	readOnly : true,
	width : 140
})

// 填写人1
var txtTxr1 = new Ext.form.TextField({
	name : 'txr1',
	renderTo : 'divtxr1',
	readOnly : true,
	width : 170
});

// 填写人2
var txtTxr2 = new Ext.form.TextField({
	name : 'txr2',
	renderTo : 'divtxr2',
	readOnly : true,
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
				if(data.kssj != null)
				dtKssj.setValue(formatDate(new Date(data.kssj['time']), "yyyy-MM-dd hh"));
				nmKsqyp1.setValue(data.ksqyp1);
				nmQwxt1.setValue(data.qwxt1);
				nmQwxt2.setValue(data.qwxt2);
				nmQwdt1.setValue(data.qwdt1);
				if(data.jssj != null)
				dtJssj.setValue(formatDate(new Date(data.jssj['time']), "yyyy-MM-dd hh"));
				nmJsqyp2.setValue(data.jsqyp2);
				nmQwdt2.setValue(data.qwdt2);
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
