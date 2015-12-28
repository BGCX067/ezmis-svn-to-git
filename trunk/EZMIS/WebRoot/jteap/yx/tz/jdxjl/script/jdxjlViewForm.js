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

var cmbJz = new Ext.form.TextField({
	hiddenName : 'jz',
	renderTo : 'divjz',
	readOnly : true,
	width : 170
})

// 接地线编号
var txtJdxbh = new Ext.form.TextField({
	name : 'jdxbh',
	renderTo : 'divjdxbh',
	readOnly : true,
	width : 170
});

// 装设地点
var txtZsdd = new Ext.form.TextField({
	name : 'zsdd',
	renderTo : 'divzsdd',
	height:120,
	readOnly : true,
	width : 440
});

// 装设时间
var dtZssj = new Ext.form.TextField({
	name : 'zssj',
	renderTo : 'divzssj',
	readOnly : true,
	width : 170
});

// 装设人
var txtZsr = new Ext.form.TextField({
	name : 'zsr',
	renderTo : 'divzsr',
	readOnly : true,
	width : 170
});

// 拆除时间
var dtccsj = new Ext.form.TextField({
	name : 'ccsj',
	renderTo : 'divccsj',
	readOnly : true,
	width : 170
});

// 拆除人
var txtccr = new Ext.form.TextField({
	name : 'ccr',
	renderTo : 'divccr',
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
