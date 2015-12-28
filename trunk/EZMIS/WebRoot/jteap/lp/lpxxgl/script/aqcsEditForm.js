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
	allowBlank : false,
	maxLength : 15,
	width : 150
})

// 措施内容
var txtCsnr = new Ext.form.TextArea({
	name : 'csnr',
	renderTo : 'divCsnr',
	maxLength : 250,
	allowBlank : false,
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
// 保存
function save() {
	if (!txtCsmc.isValid()) {
		alert('措施名称验证不正确');
		return;
	}
	if (!txtCsnr.isValid()) {
		alert('措施内容验证不正确');
		return;
	}
	myMaskSave.show();
	Ext.Ajax.request({
		url : link8,
		method : 'POST',
		params : {
			csmc : txtCsmc.getValue(),
			csnr : txtCsnr.getValue(),
			flid : hdnFlid.getValue(),
			id : hdnId.getValue()
		},
		success : function(ajax) {
			myMaskSave.hide();
			var resText = ajax.responseText;
			var obj = Ext.decode(resText);
			if (obj.success) {
				alert('保存成功');
				window.returnValue = "true";
				window.close();
			} else {
				alert(obj.msg)
			}
		},
		failure : function() {
			myMaskSave.hide();
			alert("数据库异常，请联系管理员！");
		}
	})
}
