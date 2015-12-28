var myMaskLoad = new Ext.LoadMask(Ext.getBody(), {
	msg : "正在加载，请稍候..."
});
// ************** 页面设计 ****************//
var hdnId = new Ext.form.Hidden({
	name : 'id',
	renderTo : 'id'
})

// 时间
var dtSj = new Ext.form.TextField({
	name : 'sj',
	renderTo : 'divsj',
	readOnly : true,
	width : 170
});

// 设备名称
var txtSbmc = new Ext.form.TextField({
	name : 'sbmc',
	renderTo : 'divsbmc',
	readOnly : true,
	width : 170
});

// 测量项目
var txtClxm = new Ext.form.TextField({
	name : 'clxm',
	renderTo : 'divclxm',
	align : 'right',
	readOnly : true,
	width : 170
});

// R15
var nmR15 = new Ext.form.TextField({
	name : 'r15',
	renderTo : 'divr15',
	style : 'text-align:right',
	readOnly : true,
	width : 170
})

// R60
var nmR60 = new Ext.form.TextField({
	name : 'r15',
	renderTo : 'divr60',
	style : 'text-align:right',
	readOnly : true,
	width : 170
})

// R15/R60
var nmR1560 = new Ext.form.TextField({
	name : 'r1560',
	renderTo : 'divr1560',
	style : 'text-align:right',
	readOnly : true,
	width : 170
})

// 使用仪表
var txtSyyb = new Ext.form.TextField({
	name : 'syyb',
	renderTo : 'divsyyb',
	readOnly : true,
	width : 170
});

// 天气
var cmbTq = new Ext.form.TextField({
	hiddenName : 'tq',
	renderTo : 'divtq',
	readOnly : true,
	width : 170
})

// 测量人
var txtClr = new Ext.form.TextField({
	name : 'clr',
	renderTo : 'divclr',
	readOnly : true,
	width : 170
});

// 监护人
var txtJhr = new Ext.form.TextField({
	name : 'jhr',
	renderTo : 'divjhr',
	readOnly : true,
	width : 170
});

// 备注
var txtBz = new Ext.form.TextArea({
	name : 'bz',
	renderTo : 'divbz',
	readOnly : true,
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
				dtSj.setValue(formatDate(new Date(data.sj['time']), "yyyy-MM-dd hh:mm"));
				txtSbmc.setValue(data.sbmc);
				txtClxm.setValue(data.clxm);
				nmR15.setValue(data.r15);
				nmR60.setValue(data.r60);
				nmR1560.setValue(data.r1560);
				txtSyyb.setValue(data.syyb);
				cmbTq.setValue(data.tq);
				txtClr.setValue(data.clr);
				txtJhr.setValue(data.jhr);
				txtBz.setValue(data.bz);
			}
		},
		failure : function() {

		}
	})
}
