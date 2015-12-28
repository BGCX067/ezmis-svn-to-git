var myMaskLoad = new Ext.LoadMask(Ext.getBody(), {
	msg : "正在加载，请稍候..."
});
// ************** 页面设计 ****************//
var hdnId = new Ext.form.Hidden({
	name : 'id',
	renderTo : 'id'
})

//机组编号
var cmJz= new Ext.form.TextField({
	name : 'jz',
	renderTo : 'divJz',
	readOnly : true,
	width : 170
});

// 开始时间
var dtKssj = new Ext.form.TextField({
	name : 'kssj',
	renderTo : 'divkssj',
	readOnly : true,
	width : 170
});

// 开始氢压P1
var nmKsqy = new Ext.form.NumberField({
	name : 'ksqy',
	renderTo : 'divksqy',
	style : 'text-align:right',
	readOnly : true,
	width : 140
})

// 氢温t1
var nmKsqw = new Ext.form.NumberField({
	name : 'ksqw',
	renderTo : 'divksqw',
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
var nmJsqy = new Ext.form.NumberField({
	name : 'jsqy',
	renderTo : 'divjsqy',
	style : 'text-align:right',
	readOnly : true,
	width : 140
})

// 氢温t4
var nmJsqw = new Ext.form.NumberField({
	name : 'jsqw',
	renderTo : 'divjsqw',
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
				cmJz.setValue(data.jz);
				if(data.kssj != null)
				dtKssj.setValue(formatDate(new Date(data.kssj['time']), "yyyy-MM-dd hh:mm"));
				nmKsqy.setValue(data.ksqy);
				nmKsqw.setValue(data.ksqw);
				if(data.jssj != null)
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
