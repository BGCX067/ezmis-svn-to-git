var myMaskSave = new Ext.LoadMask(Ext.getBody(), {
	msg : "正在保存，请稍候..."
});
var myMaskLoad = new Ext.LoadMask(Ext.getBody(), {
	msg : "正在加载，请稍候..."
});
// ************** 页面设计 ****************//
var hdnId = new Ext.form.Hidden({
	name : 'id',
	renderTo : 'csid'
})
var hdnFlid = new Ext.form.Hidden({
	name : 'flid',
	renderTo : 'flid'
})
// 所属分类
var txtSsfl = new Ext.form.TextField({
	name : 'aqcsCatalog',
	renderTo : 'divSsfl',
	readOnly : true,
	width : 150
})
// 措施名称
var txtCsmc = new Ext.form.TextField({
	name : 'csmc',
	renderTo : 'divCsmc',
	readOnly : true,
	width : 150
})

// 措施内容
var txtCsnr = new Ext.form.TextArea({
	name : 'csnr',
	renderTo : 'divCsnr',
	readOnly : true,
	height : 80,
	width : 409
})

// *********** 功能处理 ***********//
// 初始化
function load() {
	txtSsfl.setValue(flmc);
	if (id == null || id == '') {
		return;
	}
	myMaskLoad.show();
	Ext.Ajax.request({
		url : link7,
		method : 'POST',
		params : {
			id : id
		},
		success : function(ajax) {
			var responseText = ajax.responseText;
			var obj = Ext.decode(responseText);
			var data = obj.data[0];
			myMaskLoad.hide();
			if (obj.success) {
				txtCsmc.setValue(data.csmc);
				txtCsnr.setValue(data.csnr);
			}
		},
		failure : function() {
			myMaskLoad.hide();
		}
	})
}
