var myMaskLoad = new Ext.LoadMask(Ext.getBody(), {
	msg : "正在加载，请稍候..."
});
// ************** 页面设计 ****************//
var hdnId = new Ext.form.Hidden({
	name : 'id',
	renderTo : 'id'
})

// 设备名称
var txtSbmc = new Ext.form.TextField({
	name : 'sbmc',
	renderTo : 'divsbmc',
	readOnly : true,
	width : 480
});

// 动作时间
var dtDzsj = new Ext.form.TextField({
	name : 'dzsj',
	renderTo : 'divdzsj',
	readOnly : true,
	width : 170
});

// 保护名称及吊牌
var txtBhmc = new Ext.form.TextArea({
	name : 'bhmc',
	renderTo : 'divbhmc',
	readOnly : true,
	width : 480
});

// 光字牌信号
var txtGzpxh = new Ext.form.TextArea({
	name : 'gzpxh',
	renderTo : 'divgzpxh',
	readOnly : true,
	width : 480
});

// 检查人
var txtJcr = new Ext.form.TextField({
	name : 'jcr',
	renderTo : 'divjcr',
	readOnly : true,
	width : 170
});

// 复归人
var txtFgr = new Ext.form.TextField({
	name : 'fgr',
	renderTo : 'divfgr',
	readOnly : true,
	width : 170
});

// 备注
var txtBz = new Ext.form.TextField({
	name : 'bz',
	renderTo : 'divbz',
	readOnly : true,
	width : 480
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
				dtDzsj.setValue(formatDate(new Date(data.dzsj['time']), "yyyy-MM-dd hh:mm"));
				txtSbmc.setValue(data.sbmc);
				txtBhmc.setValue(data.bhmc);
				txtGzpxh.setValue(data.gzpxh);
				txtJcr.setValue(data.jcr);
				txtFgr.setValue(data.fgr);
				txtBz.setValue(data.bz);
			}
		},
		failure : function() {

		}
	})
}
