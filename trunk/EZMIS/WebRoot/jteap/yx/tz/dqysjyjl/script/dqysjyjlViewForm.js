var myMaskLoad = new Ext.LoadMask(Ext.getBody(), {
	msg : "正在加载，请稍候..."
});
// ************** 页面设计 ****************//
var hdnId = new Ext.form.Hidden({
	name : 'id',
	renderTo : 'id'
})

// 借用时间
var dtJysj = new Ext.form.TextField({
	name : 'jysj',
	renderTo : 'divjysj',
	readOnly : true,
	width : 170
});

// 借用钥匙
var txtJyys = new Ext.form.TextField({
	name : 'jyys',
	renderTo : 'divjyys',
	readOnly : true,
	width : 170
});

// 借用原因
var txtJyyy = new Ext.form.TextArea({
	name : 'jyyy',
	renderTo : 'divjyyy',
	height:120,
	readOnly : true,
	width : 450
});

// 借用人
var txtJyr = new Ext.form.TextField({
	name : 'jyr',
	renderTo : 'divjyr',
	readOnly : true,
	width : 170
})

// 借出值班员
var txtJczby = new Ext.form.TextField({
	name : 'jczby',
	renderTo : 'divjczby',
	readOnly : true,
	width : 170
})

// 借出值长
var txtJczz = new Ext.form.TextField({
	name : 'jczz',
	renderTo : 'divjczz',
	readOnly : true,
	width : 170
})

// 归还时间
var dtGhsj = new Ext.form.TextField({
	name : 'ghsj',
	renderTo : 'divghsj',
	readOnly : true,
	width : 170
});

// 归还人
var txtGhr = new Ext.form.TextField({
	name : 'ghr',
	renderTo : 'divghr',
	readOnly : true,
	width : 170
});

// 收回值班员
var txtShzby = new Ext.form.TextField({
	name : 'shzby',
	renderTo : 'divshzby',
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
				dtJysj.setValue(formatDate(new Date(data.jysj['time']), "yyyy-MM-dd hh:mm"));
				txtJyys.setValue(data.jyys);
				txtJyyy.setValue(data.jyyy);
				txtJyr.setValue(data.jyr);
				txtJczby.setValue(data.jczby);
				txtJczz.setValue(data.jczz);
				if (data.ghsj) {
					dtGhsj.setValue(formatDate(new Date(data.ghsj['time']), "yyyy-MM-dd hh:mm"));
				}
				txtGhr.setValue(data.ghr);
				txtShzby.setValue(data.shzby);
			}
		},
		failure : function() {

		}
	})
}
