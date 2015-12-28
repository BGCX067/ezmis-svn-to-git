var myMaskLoad = new Ext.LoadMask(Ext.getBody(), {
	msg : "正在加载，请稍候..."
});
// ************** 页面设计 ****************//
var hdnId = new Ext.form.Hidden({
	name : 'id',
	renderTo : 'id'
})

var cmbJz = new Ext.form.TextField({
	hiddenName : 'jz',
	renderTo : 'divjz',
	readOnly : true,
	width : 170
})

// 保护名称
var txtBhmc = new Ext.form.TextField({
	name : 'bhmc',
	renderTo : 'divbhmc',
	readOnly : true,
	width : 170
});

// 停用时间
var dtTysj = new Ext.form.TextField({
	name : 'tysj',
	renderTo : 'divtysj',
	readOnly : true,
	width : 170
});

// 执行人
var txtTyZxr = new Ext.form.TextField({
	name : 'tyzxr',
	renderTo : 'divtyzxr',
	readOnly : true,
	width : 170
});

// 停用值班员
var txtTyzby = new Ext.form.TextField({
	name : 'tyzby',
	renderTo : 'divtyzby',
	readOnly : true,
	width : 170
});

// 停用原因
var txtTyyy = new Ext.form.TextArea({
	name : 'tyyy',
	renderTo : 'divtyyy',
	height:100,
	readOnly : true,
	width : 450
})

// 加用时间
var dtJysj = new Ext.form.TextField({
	name : 'jysj',
	renderTo : 'divjysj',
	readOnly : true,
	width : 170
});

// 加用执行人
var txtJyZxr = new Ext.form.TextField({
	name : 'jyzxr',
	renderTo : 'divjyzxr',
	readOnly : true,
	width : 170
});

// 加用值班员
var txtJyzby = new Ext.form.TextField({
	name : 'jyzby',
	renderTo : 'divjyzby',
	readOnly : true,
	width : 170
});

// 加用原因
var txtJyyy = new Ext.form.TextArea({
	name : 'jyyy',
	renderTo : 'divjyyy',
	height:100,
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
				cmbJz.setValue(data.jz);
				txtBhmc.setValue(data.bhmc);
				dtTysj.setValue(formatDate(new Date(data.tysj['time']), "yyyy-MM-dd hh:mm"));
				txtTyZxr.setValue(data.tyzxr);
				txtTyzby.setValue(data.tyzby);
				txtTyyy.setValue(data.tyyy);
				if (data.jysj) {
					dtJysj.setValue(formatDate(new Date(data.jysj['time']), "yyyy-MM-dd hh:mm"));
				}
				txtJyZxr.setValue(data.jyzxr);
				txtJyzby.setValue(data.jyzby);
				txtJyyy.setValue(data.jyyy);
			}
		},
		failure : function() {

		}
	})
}
